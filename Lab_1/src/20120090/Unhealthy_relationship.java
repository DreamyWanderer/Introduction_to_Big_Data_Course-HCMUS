import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Unhealthy_relationship {

    public static class UnhealthyRelationshipMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private Text word_1 = new Text(); 
        private Text word_2 = new Text();
        
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String stringValue = value.toString();
            String[] splittedValue = stringValue.split("\\s+"); // Split the value string with white space(s)
            word_1.set(splittedValue[0]);
            word_2.set(splittedValue[1]);
            System.out.println(word_1);
            System.out.println(word_2);
            context.write(word_1, new IntWritable(1));
            context.write(word_2, new IntWritable(-1));
        } 
    }

    public static class UnhealthyRelationshipReducer
            extends Reducer<Text, IntWritable, Text, Text> {

        private Text result = new Text();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value: values) {
                sum += value.get();
            }
            
            if (sum > 0) result.set("pos");
            else if (sum == 0) result.set("eq");
            else result.set("neg");

            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {

        Job job = new Job();
        job.setJarByClass(Unhealthy_relationship.class);
        job.setJobName("Unhealthy Relationship");
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(UnhealthyRelationshipMapper.class);
        //job.setCombinerClass(UnhealthyRelationshipReducer.class);
        job.setReducerClass(UnhealthyRelationshipReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}