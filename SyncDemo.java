/**
 * SyncDemo is a simple test for the BoundedBuffer.
 * One producer adds items, and one consumer takes them out.
 */
public class SyncDemo {
    public static void main(String[] args) {

        // Shared buffer with 3 slots
        BoundedBuffer buffer = new BoundedBuffer(3);

        // Producer thread: creates 5 items and adds them to the buffer
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    buffer.produce(i, 1);    // producer "1" produces item i
                    Thread.sleep(500);       // short pause between items
                } catch (InterruptedException e) {
                    System.err.println("Producer interrupted");
                    return;
                }
            }
        });

        // Consumer thread: takes 5 items from the buffer
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    int item = buffer.consume(1);   // consumer "1" takes an item
                    System.out.println("Consumer took: " + item);
                    Thread.sleep(800);              // consumer runs a bit slower
                } catch (InterruptedException e) {
                    System.err.println("Consumer interrupted");
                    return;
                }
            }
        });

        // Start both threads
        producer.start();
        consumer.start();

        // Wait for both threads to finish
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted while waiting");
        }

        System.out.println("Demo complete. Producer and consumer are done.");
    }
}
