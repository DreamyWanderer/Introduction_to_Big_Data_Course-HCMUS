import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class ConnectedComponents {

    public void main(String[] args) throws Exception {
    
        int iterate = 1;
        int iterations = 0;
        Path input = new Path(args[0]);
        Path output = new Path(args[1]);
        Path inputTmp, outputTmp = null;
        FileSystem fileSystem = FileSystem.get(new Configuration());

        System.out.println("hi");
        System.out.println("hi");

        while (iterate > 0) {

            if (iterations == 0) {
                inputTmp = input;
            }
            else {
                inputTmp = output.suffix( Integer.toString(iterations));
            }

            outputTmp = output.suffix(Integer.toString(iterations + 1));

            HashToMin hashToMin = new HashToMin();
            iterate = hashToMin.setupJob(args, inputTmp, outputTmp, fileSystem.getConf() );
            if (iterate != 0) {
                fileSystem.delete(inputTmp, true);
            }

            iterations++;
        }
    }
}
