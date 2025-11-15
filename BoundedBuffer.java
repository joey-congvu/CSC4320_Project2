import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * BoundedBuffer is our shared space where producers drop items
 * and consumers come grab them. We wrap it with semaphores + a lock
 * so threads don’t step on each other.
 */
public class BoundedBuffer {
    // Our "storage" — fixed-size array = circular queue
    private final int[] buffer;

    // 'in' = next index to write to
    // 'out' = next index to read from
    private int in = 0;
    private int out = 0;

    // How many items are currently in the buffer
    private final Semaphore full;

    // How many free slots are left in the buffer
    private final Semaphore empty;

    // Only one thread can touch the buffer at a time
    private final ReentrantLock mutex;

    /**
     * Set up the buffer and synchronization tools.
     *
     * @param size how many slots the buffer has
     */
    public BoundedBuffer(int size) {
        buffer = new int[size];
        full = new Semaphore(0);        // initially nothing is in the buffer
        empty = new Semaphore(size);    // all slots are free at the start
        mutex = new ReentrantLock();    // used to protect read/write on the array
    }

    /**
     * Called by a producer to put an item into the buffer.
     * If the buffer is full, this will block until there is space.
     *
     * @param item the value to produce
     * @param pid  which producer is doing this (for printing)
     */
    public void produce(int item, int pid) throws InterruptedException {
        // Wait until there is at least one empty slot
        empty.acquire();

        // Enter critical section
        mutex.lock();
        try {
            // Put the item into the current 'in' position
            buffer[in] = item;
            System.out.println("[Producer " + pid + "] Produced " + item + " at index " + in);

            // Move 'in' forward in circular fashion
            in = (in + 1) % buffer.length;
        } finally {
            // Make sure we always release the lock
            mutex.unlock();
        }

        // We just added one item, so there is one more "full" slot now
        full.release();
    }

    /**
     * Called by a consumer to take an item from the buffer.
     * If the buffer is empty, this will block until something appears.
     *
     * @param pid which consumer is doing this (for printing)
     * @return the item taken from the buffer
     */
    public int consume(int pid) throws InterruptedException {
        // Wait until there is at least one item to consume
        full.acquire();

        // Enter critical section
        mutex.lock();
        int item;
        try {
            // Take the item at the current 'out' index
            item = buffer[out];
            System.out.println("[Consumer " + pid + "] Consumed " + item + " from index " + out);

            // Move 'out' forward in circular fashion
            out = (out + 1) % buffer.length;
        } finally {
            // Always unlock no matter what
            mutex.unlock();
        }

        // We just removed one item, so there is one more empty slot now
        empty.release();

        return item;
    }
}
