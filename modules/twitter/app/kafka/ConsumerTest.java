package kafka;

/**
 * Created by bharadwaj on 01/04/14.
 */
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import play.Logger;

public class ConsumerTest implements Runnable {
    private KafkaStream m_stream;
    private int m_threadNumber;

    public ConsumerTest(KafkaStream a_stream, int a_threadNumber) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
        Logger.info("creating ConsumerTest");
    }

    public void run() {
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
        while (it.hasNext())
            Logger.info("Thread " + m_threadNumber + ": " + new String(it.next().message()));
        Logger.info("Shutting down Thread: " + m_threadNumber);

    }
}