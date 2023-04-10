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

// WordCount là lớp chính của chương trình
public class WordCount {
  /* 	WordMapper là một mapper class - kế thừa từ lớp Mapper, và được sử dụng để xử lý dữ liệu đầu vào
	Mapper nhận vào một cặp key-value (Object và Text), tạo ra một danh sách các cặp key-value mới (Text và IntWritable): Text là từ trong văn bản và IntWritable là số lần xuất hiện
  */
  public static class WordMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1); // tạo một đối tượng IntWritable mới, với giá trị ban đầu được thiết lập là 1
    private Text word = new Text(); // tạo một đối tượng Text mới, với giá trị ban đầu được thiết lập là rỗng -> lưu trữ từ hiện tại đang được xử lý trong mapper hoặc reducer
    
    /*
	Phương thức map() được sử dụng để xử lý các cặp key-value đầu vào và tạo ra các cặp key-value mới.
	- key: Key của cặp key-value đầu vào. Trong trường hợp của chương trình đếm từ trong Hadoop, key không được sử dụng và có thể được thiết lập bất kỳ giá trị nào.
	- value: Giá trị của cặp key-value đầu vào. Đây là văn bản đầu vào mà mapper sẽ phân tích và đếm số lần xuất hiện của các từ trong đó.
	- context: Một đối tượng của lớp Context trong Hadoop, cung cấp cho mapper một cách để ghi các cặp key-value mới xuống đĩa để reducer có thể tiếp tục xử lý chúng.
    */
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer str = new StringTokenizer(value.toString());
      
      /*
	hasMoreTokens() là một phương thức của lớp StringTokenizer trong Java, được sử dụng để kiểm tra xem có còn từ nào khác trong chuỗi được phân tách bởi các delimiter (ký tự ngăn cách).
	-> kiểm tra xem có còn từ nào khác trong văn bản đầu vào được phân tích bởi mapper hay không. Nếu có, mapper sẽ tiếp tục lấy từ tiếp theo và ghi xuống context với số lần xuất hiện ban đầu là 1 (one). Nếu không còn từ nào khác, mapper sẽ quay trở lại và reducer sẽ được gọi để xử lý các cặp key-value được tạo ra bởi mapper.
	-> Vì vậy, hasMoreTokens() là một phương thức quan trọng để đảm bảo rằng chúng ta đã duyệt hết tất cả các từ trong văn bản đầu vào.
      */
      while (str.hasMoreTokens()) {
        word.set(str.nextToken()); // thiết lập giá trị của Text thành từ cần thao tác
        context.write(word, one); // ghi các cặp key-value mới xuống context
      }
    }
  }

  /*
	Lớp CountWordReducer là một reducer class, kế thừa từ lớp Reducer, và được sử dụng để xử lý dữ liệu đầu ra từ mapper. Reducer sử dụng các giá trị có cùng key (tức là các từ đã được phân tích thông qua mapper) và tính tổng số lần xuất hiện của từ đó. Sau đó, nó tạo ra một cặp key-value mới (ở đây là Text và IntWritable), trong đó Text là từ đó và IntWritable là tổng số lần xuất hiện của từ đó.
  */
  public static class CountWordReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    
    /*
	Phương thức reduce() xử lý các cặp key-value đầu vào từ mapper và tạo ra các cặp key-value mới
	- key: Key của cặp key-value đầu vào. Trong trường hợp của chương trình đếm từ trong Hadoop, key là một từ trong văn bản đầu vào.
	- values: Một danh sách các giá trị IntWritable tương ứng với key đó. Trong trường hợp của chương trình đếm từ trong Hadoop, danh sách này chứa các giá trị IntWritable biểu thị số lần xuất hiện ban đầu của từ đó do mapper tính toán.
	- context: nhu tren
    */
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum); // thiết lập giá trị của result thành sum
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(WordMapper.class);
    job.setCombinerClass(CountWordReducer.class);
    job.setReducerClass(CountWordReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
