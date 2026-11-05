public class Hospital {

    private int capacity;
    private int numberOfDoctors;
    private int numberOfPatients;

    private WaitingRoom waitingRoom;
    private TreatmentRoom treatmentRoom;

    private int completedPatients = 0;

    public Hospital(int capacity, int doctors, int patients) {
        this.capacity = capacity;
        this.numberOfDoctors = doctors;
        this.numberOfPatients = patients;

        waitingRoom = new WaitingRoom(capacity);
        treatmentRoom = new TreatmentRoom();
    }

    public synchronized void patientFinished() {
        completedPatients++;
        notifyAll();
    }

    private synchronized void waitForAllPatients() throws InterruptedException {
        while (completedPatients < numberOfPatients) {
            wait();
        }
    }

    public void startHospital() {
        System.out.println("Patients arriving:");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= numberOfPatients; i++) {
            sb.append("P").append(i).append(i == numberOfPatients ? "" : " ");
        }
        System.out.println(sb.toString());

        Thread[] doctorThreads = new Thread[numberOfDoctors];
        Thread[] patientThreads = new Thread[numberOfPatients];

        for (int i = 0; i < numberOfDoctors; i++) {
            Doctor doctor = new Doctor(i + 1, waitingRoom, treatmentRoom, this);
            doctorThreads[i] = new Thread(doctor, "Doctor-" + (i + 1));
            doctorThreads[i].start();
        }
        for (int i = 0; i < numberOfPatients; i++) {
            Patient patient = new Patient(i + 1, waitingRoom);
            patientThreads[i] = new Thread(patient, "Patient-" + (i + 1));
            patientThreads[i].start();

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            for (Thread patientThread : patientThreads) {
                patientThread.join();
            }

            waitForAllPatients();

            System.out.println("All patients have been treated.");

            waitingRoom.close(numberOfDoctors);

            for (Thread doctorThread : doctorThreads) {
                doctorThread.join();
            }

            System.out.println("Simulation completed.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Hospital was interrupted.");
        }
    }
}
