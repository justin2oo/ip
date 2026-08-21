import java.util.Scanner;

/**
 * Starts the peanutbuttercat chatbot.
 */
public class PeanutButterCat {
    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";
        String banner = " /\\_/\\\n"
                + "( o.o )  peanutbuttercat\n"
                + " > u <";
        String[] tasks = new String[100];
        int taskCount = 0;

        System.out.println(horizontalLine);
        System.out.println(banner);
        System.out.println("Hey! I'm peanutbuttercat, and I'm pawsitively ready to help!");
        System.out.println("What awesome task can we tackle together?");
        System.out.println(horizontalLine);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            System.out.println(horizontalLine);
            if (command.equals("bye")) {
                System.out.println("Bye! Hope to see you again soon. Stay pawsitive and keep spreading "
                        + "the peanut butter!");
                System.out.println(horizontalLine);
                break;
            }

            if (command.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("Pawsome! I've added: " + command);
            }
            System.out.println(horizontalLine);
        }
    }
}
