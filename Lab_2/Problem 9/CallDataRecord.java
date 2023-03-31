import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat; 

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// From phone number = 0
// To phone number = 1
// call start time = 2
// call end time = 3
// STD = 4

public class CallDataRecord {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, LongWritable>{

    private Text phone_num = new Text();
    private LongWritable duration = new LongWritable();

    private long get_time(String str) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = null;
    	try {
    		date = formatter.parse(str);
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	return date.getTime();
    }
    
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String[] word = value.toString().split("[|]");
      if (word[4].equalsIgnoreCase("1")) {
    	  phone_num.set(word[0]);
    	  String call_end = word[3];
    	  String call_start = word[2];
    	  long dur = get_time(call_end) - get_time(call_start);  //dur: time in millisecond
    	  duration.set(dur / (1000 * 60));    //duration: time in minute
    	  context.write(phone_num, duration);
      }
    }
    
  }

  public static class SumReducer
       extends Reducer<Text,LongWritable,Text,LongWritable> {
    private LongWritable result = new LongWritable();

    public void reduce(Text key, Iterable<LongWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      long sum = 0;
      for (LongWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      if (sum >= 60) {
    	  context.write(key, result);
      }
    }
  }	
    

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = new Job(conf, "CDR");
    job.setJarByClass(CallDataRecord.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setReducerClass(SumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}