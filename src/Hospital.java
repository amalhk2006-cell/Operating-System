
public class Hospital {
    int capacity;
    int numberOfDoctors;
    int numberOfPatients;

    //WaitingRoom waitingRoom;
    //TreatmentRoom treatmentRoom;

    //Doctor[] doctors;
    //Patient[] patients;

    public Hospital(int capacity, int doctors, int patients) {
        this.capacity = capacity;
        this.numberOfDoctors = doctors;
        this.numberOfPatients = patients;
       // waitingRoom = new WaitingRoom(capacity);
        //treatmentRoom = new TreatmentRoom();
        //this.doctors = new Doctor[doctors];
        //this.patients = new Patient[patients];
    }

    public void startHospital() {
        System.out.println("Hospital is starting...");

    }
}
