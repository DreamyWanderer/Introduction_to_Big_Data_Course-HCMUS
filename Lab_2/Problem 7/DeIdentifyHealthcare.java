import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DeIdentifyHealthcare {

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
 
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
 
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }

    public static class DeIdentifyHealthcareMapper extends Mapper<Object, Text, NullWritable, Text> {

        public static Integer[] encryptCol = {2, 3, 4, 5, 6, 8};

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            StringTokenizer itr = new StringTokenizer( value.toString(), ",");
            List<Integer> list = new ArrayList<Integer>();
            Collections.addAll(list, encryptCol);

            String newStr = "";

            int counter = 1;

            while ( itr.hasMoreTokens())  {
                String token = itr.nextToken();

                if (list.contains(counter)) {
                    if (newStr.length() > 0) newStr += ",";
                    try {
                        newStr += toHexString( getSHA(token));
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    if (newStr.length() > 0) newStr += ",";
                    newStr += token;
                }
                
                ++counter;
            }

            context.write(NullWritable.get(), new Text( newStr.toString() ));
        }
    }

    public static void main(String[] args ) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "deidentify health care");
        job.setJarByClass(DeIdentifyHealthcare.class);
        job.setMapperClass(DeIdentifyHealthcareMapper.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
