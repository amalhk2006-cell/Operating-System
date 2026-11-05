import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter waiting room capacity: ");
        int capacity = InputValidator.getValidNumber(input);

        System.out.print("Enter number of doctors: ");
        int doctors = InputValidator.getValidNumber(input);

        System.out.print("Enter number of patients: ");
        int patients = InputValidator.getValidNumber(input);

        Hospital hospital = new Hospital(capacity, doctors, patients);

        hospital.startHospital();

        input.close();
    }
}