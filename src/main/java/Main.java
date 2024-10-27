import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
//         Uncomment this block to pass the first stage
        List<String> arguments = Arrays.asList("echo", "type", "exit");
        while (true) {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if ("exit 0".equals(input)) {
                break;
            } else if (input.startsWith("echo ")) {
                System.out.println(input.substring(5));
            } else if (input.startsWith("type ")) {
                if (arguments.contains(input.substring(5))) {
                    System.out.println(input.substring(5) + " is a shell builtin");
                } else {
                    System.out.println(input.substring(5) + ": not found");
                }
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }
}
