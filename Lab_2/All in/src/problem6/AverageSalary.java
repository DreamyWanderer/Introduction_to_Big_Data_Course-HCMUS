import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class AverageSalary {

    public static class AverageSalaryMapper extends Mapper<LongWritable, Text, Text, FloatWritable> {

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String valueStr = value.toString();
            String[] columns = valueStr.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            try {
                context.write( new Text(columns[2]), new FloatWritable(Float.parseFloat(columns[8])) );
            } catch (Exception e) {
                return;
            }
        }
    }

    public static class AverageSalaryReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

        private FloatWritable result = new FloatWritable();

        @Override 
        public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {

            float average = 0;
            int cnt = 0;
            
            for (FloatWritable val : values) {
                ++cnt;
                average = average + (val.get() - average)/cnt;
            }

            result.set(average);
            context.write(key, result);
        }
    }

    public static void main(String[] args ) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "average salary");
        job.setJarByClass(AverageSalary.class);
        job.setMapperClass(AverageSalaryMapper.class);
        job.setReducerClass(AverageSalaryReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
