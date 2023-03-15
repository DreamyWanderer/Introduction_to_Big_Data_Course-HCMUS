import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Unhealthy_relationship {

     public static class TokenizerMapper extends Mapper <Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private final static IntWritable negOne = new IntWritable(-1);
        
        private Text vertexFrom = new Text();
        private Text vertexDest = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] lines = value.toString().split("\n");
            for (String ln : lines) {
                vertexFrom.set(String.valueOf(ln.charAt(0)));
                vertexDest.set(String.valueOf(ln.charAt(2)));
                context.write(vertexFrom, one);
                context.write(vertexDest, negOne);
            }
        }
        
    }

    public static class IntSumReducer extends Reducer <Text, IntWritable, Text, Text> {
        
        private Text result = new Text();
        
        public void reduce(Text key, Iterable <IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val: values) {
                sum += val.get();
            }
            if (sum > 0) {
                result.set("pos");
            } else if (sum < 0) {
                result.set("neg");
            } else {
                result.set("eq");
            }
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: Unhealthy_relationship <in> [<in>...] <out>");
            System.exit(2);
        }
        
        Job job = Job.getInstance(conf, "unhealthy relationship");
        job.setJarByClass(Unhealthy_relationship.class);
        job.setMapperClass(TokenizerMapper.class);
        //job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}