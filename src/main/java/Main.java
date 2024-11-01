import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> arguments = Arrays.asList("echo", "type", "exit", "pwd", "cd");
        while (true) {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input == null || input.isEmpty()) {
//                pass
            } else if ("exit 0".equals(input)) {
                break;
            } else if (input.startsWith("echo ")) {
                System.out.println(input.substring(5));
            } else if (input.startsWith("type ")) {
                if (arguments.contains(input.substring(5))) {
                    System.out.println(input.substring(5) + " is a shell builtin");
                } else {
                    String path = getPath(input.substring(5));
                    if (path != null) {
                        System.out.println(input.substring(5) + " is " + path);
                    } else {
                        System.out.println(input.substring(5) + ": not found");
                    }
                }
            } else if ("pwd".equals(input)) {
                System.out.println(System.getProperty("user.dir"));
            } else if ("cd".equals(input)) {
                System.setProperty("user.dir", System.getProperty("user.home"));
            } else if (input.startsWith("cd ")) {
                String path = input.substring(3);
                if (path.startsWith("~")) {
                    System.setProperty("user.dir", System.getenv("HOME"));
                    continue;
                }
                File file = new File(path);
                if (!file.isAbsolute()) {
                    file = Paths.get(System.getProperty("user.dir"), path).normalize().toFile();
                }
                if (file.exists() && file.isDirectory()) {
                    System.setProperty("user.dir", file.getAbsolutePath());
                } else {
                    System.out.println("cd: " + path + ": No such file or directory");
                }
            } else {
                String command = input.split(" ")[0];
                String path = getPath(command);
                if (path != null) {
                    path = path + input.substring(command.length());
                    ProcessBuilder processBuilder = new ProcessBuilder(path.split(" "));
                    Process process = processBuilder.start();
                    process.getInputStream().transferTo(System.out);
                } else {
                    System.out.println(command + ": not found");
                }
            }
        }
    }

    public static String getPath(String command) {
        String path = System.getenv("PATH");
        String[] paths = path.split(":");
        for (String p : paths) {
            String fullPath = p + "/" + command;
            if (new File(fullPath).exists()) {
                return fullPath;
            }
        }
        return null;
    }
}