package peanutbuttercat;

import java.util.Scanner;

/**
 * Starts the peanutbuttercat chatbot.
 */
public class PeanutButterCat {
    /** Relative, OS-independent location used for the application's saved tasks. */
    private static final String DEFAULT_STORAGE_PATH = "data/duke.txt";

    private final Storage storage;
    private final Parser parser;
    private final Ui ui;
    private final TaskList tasks;

    /** Creates the chatbot backed by its default task storage file. */
    public PeanutButterCat() {
        this(new Storage(DEFAULT_STORAGE_PATH), new Parser());
    }

    /**
     * Creates the chatbot with collaborators supplied for application setup and testing.
     *
     * @param storage Storage used to load and save tasks.
     * @param parser Parser used to interpret commands.
     */
    PeanutButterCat(Storage storage, Parser parser) {
        this.storage = storage;
        this.parser = parser;
        this.ui = new Ui();
        this.tasks = new TaskList(storage.load());
    }

    /**
     * Runs the chatbot and responds to commands read from standard input.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        PeanutButterCat peanutButterCat = new PeanutButterCat();
        Ui ui = new Ui();
        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            ui.showLine();
            System.out.println(peanutButterCat.getResponse(command));
            ui.showLine();
            if (peanutButterCat.isExitCommand(command)) {
                break;
            }
        }
    }

    /**
     * Returns the chatbot's reply to a command and updates its stored task list when needed.
     *
     * @param input Raw command entered by the user.
     * @return The reply for the user interface to display.
     */
    public String getResponse(String input) {
        String command = input == null ? "" : input.trim();
        CommandType commandType = parser.parseCommandType(command);

        try {
            return switch (commandType) {
                case BYE -> ui.getFarewellMessage();
                case LIST -> ui.getTaskListMessage(tasks);
                case FIND -> {
                    String keyword = parser.getDescription(command, commandType.getCommandWord());
                    yield ui.getMatchingTasksMessage(tasks.findByDescription(keyword));
                }
                case MARK -> updateTaskStatus(command, commandType, true);
                case UNMARK -> updateTaskStatus(command, commandType, false);
                case DELETE -> deleteTask(command, commandType);
                case TODO -> addTodo(command, commandType);
                case DEADLINE -> addDeadline(command);
                case EVENT -> addEvent(command);
                case ON -> ui.getTasksOnDateMessage(parser.parseDate(command), tasks);
                case UNKNOWN -> throw new PeanutButterCatException(
                        "Hiss-terical mix-up! I don't know that command yet. Try another one, purr-lease!");
            };
        } catch (PeanutButterCatException exception) {
            return ui.getErrorMessage(exception.getMessage());
        }
    }

    /** Returns whether the supplied input requests that the application exit. */
    public boolean isExitCommand(String input) {
        return parser.parseCommandType(input.trim()) == CommandType.BYE;
    }

    private String deleteTask(String command, CommandType commandType) throws PeanutButterCatException {
        int taskIndex = parser.parseTaskIndex(command, commandType.getCommandWord(), tasks.size());
        Task removedTask = tasks.remove(taskIndex);
        storage.save(tasks);
        return ui.getTaskDeletedMessage(removedTask, tasks.size());
    }

    private String addTodo(String command, CommandType commandType) throws PeanutButterCatException {
        String description = parser.getDescription(command, commandType.getCommandWord());
        Task todo = new Todo(description);
        tasks.add(todo);
        storage.save(tasks);
        return ui.getTaskAddedMessage(todo, tasks.size());
    }

    private String addDeadline(String command) throws PeanutButterCatException {
        String[] deadlineDetails = parser.parseDeadline(command);
        Task deadline = new Deadline(deadlineDetails[0], parser.parseDateTime(deadlineDetails[1]));
        tasks.add(deadline);
        storage.save(tasks);
        return ui.getTaskAddedMessage(deadline, tasks.size());
    }

    private String addEvent(String command) throws PeanutButterCatException {
        String[] eventDetails = parser.parseEvent(command);
        Task event = new Event(eventDetails[0], parser.parseDateTime(eventDetails[1]),
                parser.parseDateTime(eventDetails[2]));
        tasks.add(event);
        storage.save(tasks);
        return ui.getTaskAddedMessage(event, tasks.size());
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
    private String updateTaskStatus(String command, CommandType commandType, boolean isDone)
            throws PeanutButterCatException {
        int taskIndex = parser.parseTaskIndex(command, commandType.getCommandWord(), tasks.size());
        Task task = tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.save(tasks);
        return ui.getTaskStatusMessage(task, isDone);
    }
}
