import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Starts the peanutbuttercat chatbot.
 */
public class PeanutButterCat {
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    /** Relative, OS-independent location used for the application's saved tasks. */
    private static final Storage STORAGE = new Storage("data/duke.txt");
    private static final Parser PARSER = new Parser();

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
        TaskList tasks = new TaskList(STORAGE.load());

        System.out.println(horizontalLine);
        System.out.println(banner);
        System.out.println("Hey! I'm peanutbuttercat, and I'm pawsitively ready to help!");
        System.out.println("What pawsome task can we tackle together?");
        System.out.println(horizontalLine);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            CommandType commandType = PARSER.parseCommandType(command);

            System.out.println(horizontalLine);
            if (commandType == CommandType.BYE) {
                System.out.println("Bye! Hope to see you again soon. Stay pawsitive and keep spreading "
                        + "the peanut butter!");
                System.out.println(horizontalLine);
                break;
            }

            try {
                switch (commandType) {
                case LIST:
                    printTaskList(tasks);
                    break;
                case MARK:
                    updateTaskStatus(command, commandType, tasks, true);
                    break;
                case UNMARK:
                    updateTaskStatus(command, commandType, tasks, false);
                    break;
                case DELETE:
                    int taskIndex = PARSER.parseTaskIndex(command, commandType.getCommandWord(), tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    STORAGE.save(tasks);
                    printTaskDeleted(removedTask, tasks.size());
                    break;
                case TODO:
                    String description = PARSER.getDescription(command, commandType.getCommandWord());
                    Task todo = new Todo(description);
                    tasks.add(todo);
                    STORAGE.save(tasks);
                    printTaskAdded(todo, tasks.size());
                    break;
                case DEADLINE:
                    String[] deadlineDetails = PARSER.parseDeadline(command);
                    Task deadline = new Deadline(deadlineDetails[0], PARSER.parseDateTime(deadlineDetails[1]));
                    tasks.add(deadline);
                    STORAGE.save(tasks);
                    printTaskAdded(deadline, tasks.size());
                    break;
                case EVENT:
                    String[] eventDetails = PARSER.parseEvent(command);
                    Task event = new Event(eventDetails[0], PARSER.parseDateTime(eventDetails[1]),
                            PARSER.parseDateTime(eventDetails[2]));
                    tasks.add(event);
                    STORAGE.save(tasks);
                    printTaskAdded(event, tasks.size());
                    break;
                case ON:
                    printTasksOnDate(PARSER.parseDate(command), tasks);
                    break;
                case BYE, UNKNOWN:
                    throw new PeanutButterCatException(
                            "Hiss-terical mix-up! I don't know that command yet. "
                                    + "Try another one, purr-lease!");
                }
            } catch (PeanutButterCatException exception) {
                System.out.println(exception.getMessage());
            }
            System.out.println(horizontalLine);
        }
    }

    /**
     * Marks or unmarks the task selected by the command.
     *
     * @param command Full mark or unmark command entered by the user.
     * @param commandType Type of status-changing command.
     * @param tasks Task list containing the stored tasks.
     * @param isDone Whether the selected task should be marked as done.
     * @throws PeanutButterCatException If the task number is invalid.
     */
    private static void updateTaskStatus(String command, CommandType commandType,
            TaskList tasks, boolean isDone) throws PeanutButterCatException {
        int taskIndex = PARSER.parseTaskIndex(command, commandType.getCommandWord(), tasks.size());
        Task task = tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
            System.out.println("Pawsome! I've marked this task as done:");
        } else {
            task.markAsNotDone();
            System.out.println("No paw-blem! I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
        STORAGE.save(tasks);
    }

    /**
     * Prints all tasks currently stored in the list.
     *
     * @param tasks Task list containing the stored tasks.
     */
    private static void printTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in my cat basket:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    private static void printTasksOnDate(LocalDate date, TaskList tasks) {
        System.out.println("Here are the tasks on " + date.format(DISPLAY_DATE) + ":");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            boolean matches = task instanceof Deadline deadline
                    && deadline.getBy().toLocalDate().equals(date)
                    || task instanceof Event event
                    && (!event.getFrom().toLocalDate().isAfter(date)
                    && !event.getTo().toLocalDate().isBefore(date));
            if (matches) {
                System.out.println((i + 1) + "." + task);
            }
        }
    }

    /**
     * Prints the confirmation shown after a task is added.
     *
     * @param task Newly added task.
     * @param taskCount Number of tasks currently stored.
     */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println("Purr-fect! I've added this task to my cat basket:");
        System.out.println(task);
        printTaskCount(taskCount);
    }

    /**
     * Prints the confirmation shown after a task is deleted.
     *
     * @param task Deleted task.
     * @param taskCount Number of tasks still stored.
     */
    private static void printTaskDeleted(Task task, int taskCount) {
        System.out.println("Purr-fect! I've removed this task from my cat basket:");
        System.out.println("  " + task);
        printTaskCount(taskCount);
    }

    /**
     * Prints the current task count using the correct singular or plural noun.
     *
     * @param taskCount Number of tasks currently stored.
     */
    private static void printTaskCount(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("My cat basket now holds " + taskCount + " " + taskWord + ".");
    }
}
