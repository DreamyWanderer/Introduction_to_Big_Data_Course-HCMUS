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
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MaxTemp {
	public static class TempMap extends Mapper<LongWritable, Text, Text, IntWritable> {

		Text word = new Text();

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			//Converting the record (single line) to String and storing it in a String variable line
			String line = value.toString();

			//StringTokenizer is breaking the record (line) according to the delimiter whitespace
			StringTokenizer tokenizer = new StringTokenizer(line, " ");
		
			//Iterating through all the tokens and forming the key value pair
			while (tokenizer.hasMoreTokens()) {
				//The first token is going in year variable of type string
				String year = tokenizer.nextToken();
				word.set(year);
			
				//Takes next token and removes all the whitespaces around it and then stores it in the string variable called temp
				String temp = tokenizer.nextToken().trim();

				//Converts string temp into integer v
				int temper = Integer.parseInt(temp);
				//Sending to output collector which inturn passes the same to reducer
				context.write(word, new IntWritable(temper));
			}
		}
	}

	public static class TempReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			//Defining a local variable maxtemp of type int
			int maxtemp = 0;

			for(IntWritable it : values) {
				//Defining a local variable temperature of type int which is taking all the temperature
				int temperature = it.get();
				if(maxtemp < temperature) {
					maxtemp = temperature;
				}
			}

			// Finally the output is collected as the year and maximum temperature corresponding to that year
			context.write(key, new IntWritable(maxtemp));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "MaxTemp");
		job.setJarByClass(MaxTemp.class);
		job.setMapperClass(TempMap.class);
		job.setReducerClass(TempReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
