public class Semaphore {

    private int permits;

    public Semaphore(int permits) {
        if (permits < 0) {
            throw new IllegalArgumentException("Permits cannot be negative");
        }

        this.permits = permits;
    }

    // Wait until a permit becomes available
    public synchronized void acquire() throws InterruptedException {

        while (permits == 0) {
            wait();
        }

        permits--;
    }

    // Release a permit and notify waiting threads
    public synchronized void release() {

        permits++;

        notifyAll();
    }

    // Try to acquire a permit within a specific time
    public synchronized boolean tryAcquire(long timeout)
            throws InterruptedException {

        long deadline = System.nanoTime() + timeout * 1_000_000L;

        while (permits == 0) {

            long remaining = deadline - System.nanoTime();

            if (remaining <= 0) {
                return false;
            }

            long millis = remaining / 1_000_000L;
            int nanos = (int) (remaining % 1_000_000L);

            wait(millis, nanos);
        }

        permits--;

        return true;
    }
}