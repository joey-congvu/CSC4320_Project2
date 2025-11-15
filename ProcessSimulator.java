import java.io.*;
import java.util.*;

/**
 * This program loads process info from a text file and creates a thread for
 * each process. Every process runs independently as its own thread.
 */
public class ProcessSimulator {

    public static void main(String[] args) {
        // Store all created process threads
        List<ProcessThread> processes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("processes.txt"))) {

            // First line is a header -> skip it
            String line = br.readLine();

            // Read file line-by-line and create a ProcessThread for each process
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");

                // Expect: PID, arrival time, burst time, priority
                int pid = Integer.parseInt(parts[0]);
                int arrivalTime = Integer.parseInt(parts[1]);
                int burstTime = Integer.parseInt(parts[2]);
                int priority = Integer.parseInt(parts[3]);

                // Build a process thread and store it
                processes.add(new ProcessThread(pid, arrivalTime, burstTime, priority));
            }

            // Start all the threads
            for (ProcessThread pt : processes) {
                pt.start();
            }

            // Wait for every process to finish before printing the final message
            for (ProcessThread pt : processes) {
                pt.join();
            }

        } catch (Exception e) {
            // If something goes wrong (file missing, bad input format, etc.)
            e.printStackTrace();
        }

        System.out.println("All processes completed.");
    }
}