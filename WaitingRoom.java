import java.util.Queue;
import java.util.ArrayDeque;

public class WaitingRoom {

    private Queue<Patient> queue;
    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutex;
    private boolean closed = false;

    public WaitingRoom(int capacity) {
        queue = new ArrayDeque<>();
        empty = new Semaphore(capacity);
        full = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    public void addPatient(Patient patient) throws InterruptedException {
        boolean seatAvailable;
        mutex.acquire();
        try {
            seatAvailable = empty.tryAcquire(0);
            if (!seatAvailable) {
                System.out.println("The waiting room is full.");
                System.out.println("P" + patient.getId() + " is waiting outside for an available seat.");
            }
        } finally {
            mutex.release();
        }

        if (!seatAvailable) {
            empty.acquire();
        }

        mutex.acquire();
        try {
            if (closed) {
                empty.release();
                return;
            }
            queue.add(patient);
            System.out.println("P" + patient.getId() + " entered the waiting room.");
        } finally {
            mutex.release();
        }

        full.release();
    }

    public Patient getPatient(int doctorId) throws InterruptedException {
        full.acquire();

        mutex.acquire();
        try {
            if (closed && queue.isEmpty()) {
                return null;
            }
            Patient patient = queue.remove();

            System.out.println("Doctor " + doctorId +
                    " is assigned to P" + patient.getId() + ".");

            empty.release();
            return patient;
        } finally {
            mutex.release();
        }
    }

    public void close(int numberOfDoctors) throws InterruptedException {
        mutex.acquire();
        try {
            closed = true;
        } finally {
            mutex.release();
        }

        for (int i = 0; i < numberOfDoctors; i++) {
            full.release();
        }
    }
}
