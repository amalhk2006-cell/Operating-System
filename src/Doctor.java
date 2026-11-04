import java.util.concurrent.ThreadLocalRandom;

public class Doctor implements Runnable {
    private int doctorId;
    private WaitingRoom waitingRoom;
    private TreatmentRoom treatmentRoom;
    private Hospital hospital;

    public Doctor(int doctorId, WaitingRoom waitingRoom,TreatmentRoom treatmentRoom, Hospital hospital) {
        this.doctorId=doctorId;
        this.waitingRoom=waitingRoom;
        this.treatmentRoom=treatmentRoom;
        this.hospital=hospital;
    }

    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                Patient patient=waitingRoom.getPatient();
                if(patient==null){
                  break;
                }
                System.out.println("Doctor "+ doctorId +" is assigned to P"+patient.getId());
                treatPatient(patient);
                System.out.println("Doctor "+doctorId +" is now available");
            }
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private void treatPatient(Patient patient) throws InterruptedException{
        System.out.println("Doctor "+doctorId +" is trying to acquire a treatment room");
        boolean roomAvailable=treatmentRoom.tryAcquire(3000);

        if(!roomAvailable){
          System.out.println("Doctor "+doctorId +" could not acquire a treatment room");
          System.out.println("Doctor "+doctorId+ " is sending P"+ patient.getId()+" back to the waiting room");
          waitingRoom.addPatient(patient);
          return;
        }

        try{
          System.out.println("Doctor "+ doctorId +" acquired a treatment room");
          System.out.println("Doctor "+doctorId +"begins treating P "+patient.getId());

          int time = ThreadLocalRandom.current().nextInt(1000, 5001);
          Thread.sleep(time);

          System.out.println("Doctor "+doctorId+" finished treating P "+patient.getId());
          hospital.patientFinished();

        } 
        finally{
          treatmentRoom.release();
          System.out.println("Doctor "+doctorId+" released the treatment room");
        }
    }
}
