import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<String> BUILTIN_COMMANDS = Arrays.asList("echo", "type", "exit", "pwd", "cd");

    public static void main(String[] args) throws Exception {
        setHomeDirectory();
        runShell();
    }

    private static void setHomeDirectory() {
        String home = System.getenv("HOME");
        if (home != null) {
            System.setProperty("user.home", home);
        }
    }

    private static void runShell() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input == null || input.isEmpty()) {
                continue;
            }
            if ("exit 0".equals(input)) {
                break;
            } else if (!handleBuiltinCommands(input)) {
                executeCommand(input);
            }
        }
    }

    private static boolean handleBuiltinCommands(String input) {
        boolean flag = true;
        if (input.startsWith("echo ")) {
            System.out.println(input.substring(5));
        } else if (input.startsWith("type ")) {
            handleTypeCommand(input.substring(5));
        } else if ("pwd".equals(input)) {
            System.out.println(System.getProperty("user.dir"));
        } else if (input.startsWith("cd")) {
            handleCdCommand(input);
        } else {
            flag = false;
        }
        return flag;
    }

    private static void handleTypeCommand(String command) {
        if (BUILTIN_COMMANDS.contains(command)) {
            System.out.println(command + " is a shell builtin");
        } else {
            String path = getPath(command);
            if (path != null) {
                System.out.println(command + " is " + path);
            } else {
                System.out.println(command + ": not found");
            }
        }
    }

    private static void handleCdCommand(String input) {
        String path = input.equals("cd") ? System.getProperty("user.home") : input.substring(3);
        if (path.startsWith("~")) {
            path = System.getProperty("user.home");
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
    }

    private static void executeCommand(String input) throws Exception {
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