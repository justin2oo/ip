import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

/**
 * Starts the peanutbuttercat chatbot.
 */
public class PeanutButterCat {
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter[] INPUT_FORMATS = {
        DateTimeFormatter.ofPattern("d/M/uuuu HHmm"),
        DateTimeFormatter.ofPattern("d/M/uuuu HH:mm"),
        DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm"),
        DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"),
        DateTimeFormatter.ofPattern("uuuu-MM-dd")
    };
    /** Relative, OS-independent location used for the application's saved tasks. */
    private static final Storage STORAGE = new Storage("data/duke.txt");

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
                    STORAGE.save(tasks);
                    printTaskDeleted(removedTask, tasks.size());
                    break;
                case TODO:
                    String description = getDescription(command, commandType.getCommandWord());
                    Task todo = new Todo(description);
                    tasks.add(todo);
                    STORAGE.save(tasks);
                    printTaskAdded(todo, tasks.size());
                    break;
                case DEADLINE:
                    String[] deadlineDetails = parseDeadline(command);
                    Task deadline = new Deadline(deadlineDetails[0], parseDateTime(deadlineDetails[1]));
                    tasks.add(deadline);
                    STORAGE.save(tasks);
                    printTaskAdded(deadline, tasks.size());
                    break;
                case EVENT:
                    String[] eventDetails = parseEvent(command);
                    Task event = new Event(eventDetails[0], parseDateTime(eventDetails[1]),
                            parseDateTime(eventDetails[2]));
                    tasks.add(event);
                    STORAGE.save(tasks);
                    printTaskAdded(event, tasks.size());
                    break;
                case ON:
                    printTasksOnDate(parseDate(command), tasks);
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
        parseDateTime(by);
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
        parseDateTime(from);
        parseDateTime(to);
        return new String[]{description, from, to};
    }

    private static LocalDateTime parseDateTime(String value) throws PeanutButterCatException {
        for (DateTimeFormatter format : INPUT_FORMATS) {
            try {
                TemporalAccessor parsed = format.parse(value);
                LocalDate date = LocalDate.from(parsed);
                return parsed.isSupported(java.time.temporal.ChronoField.HOUR_OF_DAY)
                        ? LocalDateTime.from(parsed) : date.atStartOfDay();
            } catch (DateTimeParseException ignored) {
                // Try the next accepted input format.
            }
        }
        throw new PeanutButterCatException("I couldn't understand that date. Use yyyy-MM-dd "
                + "or d/M/yyyy HHmm, purr-lease!");
    }

    private static LocalDate parseDate(String command) throws PeanutButterCatException {
        String value = command.substring(CommandType.ON.getCommandWord().length()).trim();
        if (value.isEmpty()) {
            throw new PeanutButterCatException("Which date should I search? Use: on yyyy-MM-dd");
        }
        try {
            return parseDateTime(value).toLocalDate();
        } catch (PeanutButterCatException exception) {
            throw new PeanutButterCatException("I couldn't understand that date. Use yyyy-MM-dd, purr-lease!");
        }
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
     * @param tasks Task list containing the stored tasks.
     * @param isDone Whether the selected task should be marked as done.
     * @throws PeanutButterCatException If the task number is invalid.
     */
    private static void updateTaskStatus(String command, CommandType commandType,
            TaskList tasks, boolean isDone) throws PeanutButterCatException {
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
