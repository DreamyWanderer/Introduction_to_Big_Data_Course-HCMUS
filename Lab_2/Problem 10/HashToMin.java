import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class HashToMin {

    public enum JobCounters {
        GO_ON, // if positive another round of HtM must be run (HtM job)
    }

    public static class HashToMinMapper extends Mapper<LongWritable, Text, IntWritable, ClusterWritable> {

        @Override
        public void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException {

            String[] vertexes = line.toString().split("[\\s\\t]+");
            TreeSet<Integer> cluster = new TreeSet();

            // Build (v_min, C_v)
            for (String v: vertexes) {
                cluster.add( Integer.parseInt(v) );
            }

            Integer vMin = cluster.first();

            context.write(new IntWritable(vMin), new ClusterWritable(cluster));

            // Build (u, v_min)
            TreeSet<Integer> cTmp = new TreeSet();
            cTmp.add( cluster.first() );

            for (Integer u : cluster) {
                if (u.intValue() != vMin.intValue() ) {
                    context.write(new IntWritable(u), new ClusterWritable(cTmp) );
                }
            }
        }
    }

    public static class HashToMinReducer extends Reducer<IntWritable, ClusterWritable, IntWritable, Text> {
        
        public void reduce(IntWritable vertex, Iterable<ClusterWritable> clusters, Context context) throws IOException, InterruptedException {

        TreeSet<Integer> cluster = new TreeSet<Integer>();
        int v = vertex.get();

        //updates C_v in (v,C_v)
        for (ClusterWritable c : clusters) {   
            cluster.addAll(c);
        }
        //checks whether there has been convergence to <u, {v_min}> and <v_min, C> couples
        if (cluster.size() > 1 && v > cluster.first()) {
                context.getCounter(JobCounters.GO_ON).increment(1);
            }
        
        //writes updated (u,C_u) 
        context.write(vertex, new Text(new ClusterWritable(cluster).toString()));
        }
    }

    public int setupJob(String[] args, Path input, Path output, Configuration conf) throws Exception {

        Job job = Job.getInstance(conf, "hash to min");
        job.setJarByClass(HashToMin.class);
        job.setMapperClass(HashToMinMapper.class);
        job.setReducerClass(HashToMinReducer.class); 

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(ClusterWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, input);
        FileOutputFormat.setOutputPath(job, output);

        job.waitForCompletion(true);
        Counters counters = job.getCounters();
        long tmpCnt = counters.findCounter( JobCounters.GO_ON ).getValue();
        counters.findCounter( JobCounters.GO_ON ).setValue(0);

        if (tmpCnt > 0) return 1; else return 0;
    }
}