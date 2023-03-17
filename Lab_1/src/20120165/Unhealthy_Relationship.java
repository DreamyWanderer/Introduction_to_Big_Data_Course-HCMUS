import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Unhealthy_Relationship {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one_p = new IntWritable(1);
    private final static IntWritable one_n = new IntWritable(-1);
    private Text word_1 = new Text();
    private Text word_2 = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	String line = value.toString();
    	String[] word = line.split("//s");
    	word_1.set(word[0]);
    	word_2.set(word[1]);
    	context.write(word_1, one_p);
    	context.write(word_2, one_n);
    }
  }

  public static class IntSumCombiner
       extends Reducer<Text,IntWritable,Text,IntWritable> 
  {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException 
    {
      int sum = 0;
      for (IntWritable val : values) 
      {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }
  
  public static class UnhealthyRelationshipReducer
  	   extends Reducer<Text,IntWritable,Text,Text> {
	private Text result = new Text();

	public void reduce(Text key, Iterable<IntWritable> values,
                  Context context
                  ) throws IOException, InterruptedException {
	  int sum = 0;
	  for (IntWritable val : values) {
		  sum += val.get();
	  }
	  
	  if (sum > 0) {
		  result.set("pos");
	  }
	  else if (sum < 0) {
		  result.set("neg");
	  }
	  else {
		  result.set("eq");
	  }
	  
	  context.write(key, result);
	}
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "unhealthy relationship");
    job.setJarByClass(Unhealthy_Relationship.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumCombiner.class);
    job.setReducerClass(UnhealthyRelationshipReducer.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}