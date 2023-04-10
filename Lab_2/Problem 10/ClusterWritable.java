import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.hadoop.io.Writable;

public class ClusterWritable extends TreeSet<Integer> implements Writable {

    public ClusterWritable() {
        super();
    }

    public ClusterWritable(Collection<Integer> c) {
        super(c);
    }

    @Override
    public void write(DataOutput d) throws IOException {
        int size = this.size();
        d.writeInt(size);
        for (Integer i : this.descendingSet()) {
            d.writeInt(i);
        }
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        int size = di.readInt();
        this.clear();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.add(di.readInt());
            }
        }
    }

    @Override
    public String toString() {
        return super.toString().replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll(", ", " ");
    }
}