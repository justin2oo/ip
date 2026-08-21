import java.util.Scanner;

/**
 * Starts the peanutbuttercat chatbot.
 */
public class PeanutButterCat {
    private static final String TODO_COMMAND = "todo ";
    private static final String DEADLINE_COMMAND = "deadline ";
    private static final String EVENT_COMMAND = "event ";

    /**
     * Runs the chatbot and responds to commands read from standard input.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";
        String banner = " /\\_/\\\n"
                + "( o.o )  peanutbuttercat\n"
                + " > u <";
        Object[] tasks = new Object[100];
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
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                markAsDone(tasks[taskIndex]);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                markAsNotDone(tasks[taskIndex]);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (command.startsWith(TODO_COMMAND)) {
                String description = command.substring(TODO_COMMAND.length());
                tasks[taskCount] = new Todo(description);
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (command.startsWith(DEADLINE_COMMAND)) {
                int byIndex = command.indexOf(" /by ");
                String description = command.substring(DEADLINE_COMMAND.length(), byIndex);
                String by = command.substring(byIndex + " /by ".length());
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (command.startsWith(EVENT_COMMAND)) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ", fromIndex);
                String description = command.substring(EVENT_COMMAND.length(), fromIndex);
                String from = command.substring(fromIndex + " /from ".length(), toIndex);
                String to = command.substring(toIndex + " /to ".length());
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else {
                System.out.println("I don't recognise that command.");
            }
            System.out.println(horizontalLine);
        }
    }

    /**
     * Prints the confirmation shown after a task is added.
     *
     * @param task Newly added task.
     * @param taskCount Number of tasks currently stored.
     */
    private static void printTaskAdded(Object task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Marks one of the three independent task types as done.
     *
     * <p>The explicit type checks are necessary while these classes do not share
     * a parent class. They can be replaced by one polymorphic method call after
     * inheritance is introduced.</p>
     *
     * @param task Todo, deadline, or event to mark.
     */
    private static void markAsDone(Object task) {
        if (task instanceof Todo) {
            ((Todo) task).markAsDone();
        } else if (task instanceof Deadline) {
            ((Deadline) task).markAsDone();
        } else if (task instanceof Event) {
            ((Event) task).markAsDone();
        }
    }

    /**
     * Marks one of the three independent task types as not done.
     *
     * @param task Todo, deadline, or event to unmark.
     */
    private static void markAsNotDone(Object task) {
        if (task instanceof Todo) {
            ((Todo) task).markAsNotDone();
        } else if (task instanceof Deadline) {
            ((Deadline) task).markAsNotDone();
        } else if (task instanceof Event) {
            ((Event) task).markAsNotDone();
        }
    }
}
