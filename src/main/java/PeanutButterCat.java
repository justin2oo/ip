import java.util.Scanner;

/**
 * Starts the peanutbuttercat chatbot.
 */
public class PeanutButterCat {
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final int MAX_TASKS = 100;

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
        Task[] tasks = new Task[MAX_TASKS];
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

            try {
                if (command.equals("list")) {
                    printTaskList(tasks, taskCount);
                } else if (isCommand(command, "mark")) {
                    int taskIndex = parseTaskIndex(command, "mark", taskCount);
                    tasks[taskIndex].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskIndex]);
                } else if (isCommand(command, "unmark")) {
                    int taskIndex = parseTaskIndex(command, "unmark", taskCount);
                    tasks[taskIndex].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskIndex]);
                } else if (isCommand(command, TODO_COMMAND)) {
                    ensureTaskListHasSpace(taskCount);
                    String description = getDescription(command, TODO_COMMAND);
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    printTaskAdded(tasks[taskCount - 1], taskCount);
                } else if (isCommand(command, DEADLINE_COMMAND)) {
                    ensureTaskListHasSpace(taskCount);
                    String[] deadlineDetails = parseDeadline(command);
                    tasks[taskCount] = new Deadline(deadlineDetails[0], deadlineDetails[1]);
                    taskCount++;
                    printTaskAdded(tasks[taskCount - 1], taskCount);
                } else if (isCommand(command, EVENT_COMMAND)) {
                    ensureTaskListHasSpace(taskCount);
                    String[] eventDetails = parseEvent(command);
                    tasks[taskCount] = new Event(eventDetails[0], eventDetails[1], eventDetails[2]);
                    taskCount++;
                    printTaskAdded(tasks[taskCount - 1], taskCount);
                } else {
                    throw new PeanutButterCatException("Hiss-terical mix-up! I don't know that command yet. "
                            + "Try another one, purr-lease!");
                }
            } catch (PeanutButterCatException exception) {
                System.out.println(exception.getMessage());
            }
            System.out.println(horizontalLine);
        }
    }

    /**
     * Returns whether the input contains the given command word, optionally followed by arguments.
     *
     * @param input Full input entered by the user.
     * @param commandWord Command word to look for.
     * @return True when the input starts with the complete command word.
     */
    private static boolean isCommand(String input, String commandWord) {
        return input.equals(commandWord) || input.startsWith(commandWord + " ");
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
        String details = command.substring(DEADLINE_COMMAND.length()).trim();
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
        String details = command.substring(EVENT_COMMAND.length()).trim();
        int fromIndex = details.indexOf("/from");
        int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to", fromIndex + "/from".length());
        if (fromIndex < 0 || toIndex < 0) {
            throw new PeanutButterCatException("I need the whole time trail! Use: "
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
     * Parses a one-based task number and converts it to an array index.
     *
     * @param command Full mark or unmark command entered by the user.
     * @param commandWord Command word preceding the task number.
     * @param taskCount Number of tasks currently stored.
     * @return The corresponding zero-based array index.
     * @throws PeanutButterCatException If the task number is missing, malformed, or out of range.
     */
    private static int parseTaskIndex(String command, String commandWord, int taskCount)
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
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new PeanutButterCatException("I can't find task " + taskNumber
                    + " in my basket. Check 'list' and try again!");
        }
        return taskNumber - 1;
    }

    /**
     * Checks that another task can be stored without overflowing the fixed-size task list.
     *
     * @param taskCount Number of tasks currently stored.
     * @throws PeanutButterCatException If the task list is full.
     */
    private static void ensureTaskListHasSpace(int taskCount) throws PeanutButterCatException {
        if (taskCount >= MAX_TASKS) {
            throw new PeanutButterCatException("My task basket is full with " + MAX_TASKS
                    + " tasks! Finish some before adding more, purr-lease.");
        }
    }

    /**
     * Prints all tasks currently stored in the list.
     *
     * @param tasks Array containing the stored tasks.
     * @param taskCount Number of tasks currently stored.
     */
    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Prints the confirmation shown after a task is added.
     *
     * @param task Newly added task.
     * @param taskCount Number of tasks currently stored.
     */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
