public class TreatmentRoom {
    private Semaphore mutex;

    public TreatmentRoom() {
        mutex = new Semaphore(2);
    }

    public boolean tryAcquire(long timeout) throws InterruptedException {

        if (mutex.tryAcquire(timeout)) {
            // accuire the room
            return true;
        } else {
            // not accuire the room
            return false;
        }
    }

    public void release() {
        mutex.release();
    }
}
