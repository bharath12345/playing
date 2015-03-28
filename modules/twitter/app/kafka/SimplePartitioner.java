package kafka;

/**
 * Created by bharadwaj on 01/04/14.
 */
import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class SimplePartitioner implements Partitioner {

    public SimplePartitioner (VerifiableProperties props) {
    }

    public int partition(Object okey, int a_numPartitions) {
        String key = (String) okey;
        int partition = 0;
        int offset = key.lastIndexOf('.');
        if (offset > 0) {
            partition = Integer.parseInt( key.substring(offset+1)) % a_numPartitions;
        }
        return partition;
    }

}