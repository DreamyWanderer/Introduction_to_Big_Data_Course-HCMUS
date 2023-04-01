import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Weather {

  public static class WeatherMapper
       extends Mapper<Object, Text, Text, Text>{

    private Text date = new Text();
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String line = value.toString();
      String d = line.substring(14, 22);
      float max = Float.parseFloat(line.substring(104, 108).trim());
      float min = Float.parseFloat(line.substring(112, 116).trim());
      
      if (max > 40.0) {
    	  date.set(d);
    	  word.set("Hot day");
    	  context.write(date, word);
      }
      
      if (min < 10.0) {
    	  date.set(d);
    	  word.set("Cold day");
    	  context.write(date, word);
      }

      
      }
    }


  public static class WeatherReducer
       extends Reducer<Text,Text,Text,Text> {
    private Text result = new Text();

    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
      String res = "";
      for (Text val : values) {
        res = res + val + " ";
      }
      result.set(res);
      context.write(key, result);
    }
  }


  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = new Job(conf, "weather");
    job.setJarByClass(Weather.class);
    job.setMapperClass(WeatherMapper.class);
    job.setReducerClass(WeatherReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}