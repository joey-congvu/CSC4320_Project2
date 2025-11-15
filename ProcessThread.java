/**
 * Represents one process running as a thread.
 * Each process waits until its arrival time, runs its burst time,
 * then prints when it finishes.
 */
class ProcessThread extends Thread {

    int pid, arrivalTime, burstTime, priority;

    public ProcessThread(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    @Override
    public void run() {
        try {
            // Simulate arrival delay
            Thread.sleep(arrivalTime * 1000);

            System.out.println("[Process " + pid + "] Starting (Priority " + priority + ")");

            // Simulate CPU burst time
            Thread.sleep(burstTime * 1000);

            System.out.println("[Process " + pid + "] Finished");

        } catch (InterruptedException e) {
            System.err.println("[Process " + pid + "] Interrupted");
        }
    }
}