import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
//         Uncomment this block to pass the first stage
        while (true) {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if ("exit 0".equals(input)) {
                break;
            } else if (input.startsWith("echo ")) {
                System.out.println(input.substring(5));
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }
}
