import java.util.Scanner;

public class InputValidator {

    public static int getValidNumber(Scanner number) {
        while (true) {

            if (!number.hasNextInt()) {
                System.out.println("Error: Number is not valid.");
                number.next();
                continue;
            }
            int value = number.nextInt();

            if (value < 1) {
                System.out.println("Error: Number can't be less than 1.");
                continue;

            }
            return value;
        }
    }
}
