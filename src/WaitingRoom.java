import java.util.Queue;
import java.util.ArrayDeque;

public class WaitingRoom {

    private Queue<Patient> queue;

    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutex;


    public WaitingRoom(int capacity) {

        queue = new ArrayDeque<>();

        empty = new Semaphore(capacity);
        full = new Semaphore(0);
        mutex = new Semaphore(1);
    }



    //Patient enters the waiting room
    public void addPatient(Patient patient)
            throws InterruptedException {

        //Try to get an empty seat immediately => permit--
        if (!empty.tryAcquire(0)) {

            System.out.println("The waiting room is full.");
            System.out.println(  "P" + patient.getId()  + " is waiting outside for an available seat.");

            //Wait until a seat becomes available
            empty.acquire();
        }



        //for protect the queue
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            empty.release();
            throw e;
        }


        try {
            queue.add(patient);

            System.out.println("P" + patient.getId()+ " entered the waiting room.");

            } 
        finally {
            mutex.release();
        }


        //Notify doctors that a patient is available
        full.release();
    }

    //Doctor takes a patient from the waiting room
    public Patient getPatient()
            throws InterruptedException {

        //Wait until a patient is available
        full.acquire();

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            full.release();
            throw e;
        }

        Patient patient;

        try {
            patient = queue.remove();
        } 
        finally {
            mutex.release();
        }

        //A seat becomes available
        empty.release();

        return patient;
    }
}
