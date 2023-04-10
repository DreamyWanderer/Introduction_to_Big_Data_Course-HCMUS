import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordSizeWordCount {

    public static class WordSizeWordCountMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    
            StringTokenizer itr = new StringTokenizer( value.toString() );
            while (itr.hasMoreTokens() ) {
                // I do not want to count the special characters (like :,.) in the word. So for each word seperated by whitespace, I remove all special characters so that the received size of the word will be correct!
                word.set( itr.nextToken().replaceAll("[^\\w]", "") );
                context.write( new IntWritable(word.getLength()), one );
            }
            
        }
    }

    public static class WordSizeWordCountReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

        private IntWritable result = new IntWritable();

        @Override
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }   

    public static void main(String[] args ) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word size word count");
        job.setJarByClass(WordSizeWordCount.class);
        job.setMapperClass(WordSizeWordCountMapper.class);
        job.setCombinerClass(WordSizeWordCountReducer.class);
        job.setReducerClass(WordSizeWordCountReducer.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}