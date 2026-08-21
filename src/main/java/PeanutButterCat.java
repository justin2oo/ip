import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Starts the peanutbuttercat chatbot.
 */
public class PeanutButterCat {
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
        List<Task> tasks = new ArrayList<>();

        System.out.println(horizontalLine);
        System.out.println(banner);
        System.out.println("Hey! I'm peanutbuttercat, and I'm pawsitively ready to help!");
        System.out.println("What pawsome task can we tackle together?");
        System.out.println(horizontalLine);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            CommandType commandType = CommandType.fromInput(command);

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
                    int taskIndex = parseTaskIndex(command, commandType.getCommandWord(), tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    printTaskDeleted(removedTask, tasks.size());
                    break;
                case TODO:
                    String description = getDescription(command, commandType.getCommandWord());
                    Task todo = new Todo(description);
                    tasks.add(todo);
                    printTaskAdded(todo, tasks.size());
                    break;
                case DEADLINE:
                    String[] deadlineDetails = parseDeadline(command);
                    Task deadline = new Deadline(deadlineDetails[0], deadlineDetails[1]);
                    tasks.add(deadline);
                    printTaskAdded(deadline, tasks.size());
                    break;
                case EVENT:
                    String[] eventDetails = parseEvent(command);
                    Task event = new Event(eventDetails[0], eventDetails[1], eventDetails[2]);
                    tasks.add(event);
                    printTaskAdded(event, tasks.size());
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
     * Extracts and validates a task description after a command word.
     *
     * @param command Full command entered by the user.
     * @param commandWord Command word preceding the description.
     * @return The non-empty task description.
     * @throws PeanutButterCatException If the description is empty.
     */
    private static String getDescription(String command, String commandWord)
            throws PeanutButterCatException {
        String description = command.substring(commandWord.length()).trim();
        if (description.isEmpty()) {
            throw new PeanutButterCatException("Oops, this kitty needs a description for your "
                    + commandWord + "! Please add one after '" + commandWord + "'.");
        }
        return description;
    }

    /**
     * Parses and validates the description and due time of a deadline command.
     *
     * @param command Full deadline command entered by the user.
     * @return The description and due time, in that order.
     * @throws PeanutButterCatException If a required deadline detail is missing.
     */
    private static String[] parseDeadline(String command) throws PeanutButterCatException {
        String details = command.substring(CommandType.DEADLINE.getCommandWord().length()).trim();
        int byIndex = details.indexOf("/by");
        if (byIndex < 0) {
            throw new PeanutButterCatException("My whiskers can't find the deadline! Use: "
                    + "deadline DESCRIPTION /by TIME");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new PeanutButterCatException("Oops, this kitty needs a description for your deadline!");
        }
        if (by.isEmpty()) {
            throw new PeanutButterCatException("When is it due? Add a time after '/by', purr-lease!");
        }
        return new String[]{description, by};
    }

    /**
     * Parses and validates the description, start time, and end time of an event command.
     *
     * @param command Full event command entered by the user.
     * @return The description, start time, and end time, in that order.
     * @throws PeanutButterCatException If a required event detail is missing.
     */
    private static String[] parseEvent(String command) throws PeanutButterCatException {
        String details = command.substring(CommandType.EVENT.getCommandWord().length()).trim();
        int fromIndex = details.indexOf("/from");
        int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to", fromIndex + "/from".length());
        if (fromIndex < 0 || toIndex < 0) {
            throw new PeanutButterCatException("My whiskers need the whole time trail! Use: "
                    + "event DESCRIPTION /from START /to END");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = details.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new PeanutButterCatException("Oops, this kitty needs a description for your event!");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new PeanutButterCatException("An event needs both start and end times - no missing paws!");
        }
        return new String[]{description, from, to};
    }

    /**
     * Parses a one-based task number and converts it to a list index.
     *
     * @param command Full mark, unmark, or delete command entered by the user.
     * @param commandWord Command word preceding the task number.
     * @param numberOfTasks Number of tasks currently stored.
     * @return The corresponding zero-based list index.
     * @throws PeanutButterCatException If the task number is missing, malformed, or out of range.
     */
    private static int parseTaskIndex(String command, String commandWord, int numberOfTasks)
            throws PeanutButterCatException {
        String numberText = command.substring(commandWord.length()).trim();
        if (numberText.isEmpty()) {
            throw new PeanutButterCatException("Which task should I " + commandWord
                    + "? Give me its number, purr-lease!");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new PeanutButterCatException("My paws can only count whole task numbers. "
                    + "Try '" + commandWord + " 1', for example!");
        }
        if (taskNumber < 1 || taskNumber > numberOfTasks) {
            throw new PeanutButterCatException("I can't find task " + taskNumber
                    + " in my cat basket. Check 'list' and try again!");
        }
        return taskNumber - 1;
    }

    /**
     * Marks or unmarks the task selected by the command.
     *
     * @param command Full mark or unmark command entered by the user.
     * @param commandType Type of status-changing command.
     * @param tasks List containing the stored tasks.
     * @param isDone Whether the selected task should be marked as done.
     * @throws PeanutButterCatException If the task number is invalid.
     */
    private static void updateTaskStatus(String command, CommandType commandType,
            List<Task> tasks, boolean isDone) throws PeanutButterCatException {
        int taskIndex = parseTaskIndex(command, commandType.getCommandWord(), tasks.size());
        Task task = tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
            System.out.println("Pawsome! I've marked this task as done:");
        } else {
            task.markAsNotDone();
            System.out.println("No paw-blem! I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /**
     * Prints all tasks currently stored in the list.
     *
     * @param tasks List containing the stored tasks.
     */
    private static void printTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in my cat basket:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
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
