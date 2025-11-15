/**
 * SyncDemo is just a small test driver for BoundedBuffer.
 * One producer makes items, one consumer eats them.
 */
public class SyncDemo {
    public static void main(String[] args) {
        // Shared buffer with 3 slots
        BoundedBuffer buffer = new BoundedBuffer(3);

        // Producer thread: creates 5 items and drops them into the buffer
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    buffer.produce(i, 1);   // producer "1" producing item i
                    Thread.sleep(500);      // small delay between items
                } catch (InterruptedException e) {
                    // If someone interrupts, we just stop quietly
                    System.err.println("Producer interrupted");
                    return;
                }
            }
        });

        // Consumer thread: pulls 5 items from the buffer
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    int item = buffer.consume(1);  // consumer "1" grabs an item
                    Thread.sleep(800);             // consumer is a bit slower
                } catch (InterruptedException e) {
                    System.err.println("Consumer interrupted");
                    return;
                }
            }
        });

        // Fire both threads and let them do their thing
        producer.start();
        consumer.start();

        // Optionally wait for both to finish
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted while waiting");
        }

        System.out.println("Demo done. All producer/consumer work finished.");
    }
}
