package peanutbuttercat;

import java.util.Scanner;

/**
 * Starts the peanutbuttercat chatbot.
 */
public class PeanutButterCat {
    /** Relative, OS-independent location used for the application's saved tasks. */
    private static final Storage STORAGE = new Storage("data/duke.txt");
    private static final Parser PARSER = new Parser();
    private static final Ui UI = new Ui();

    /**
     * Runs the chatbot and responds to commands read from standard input.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        TaskList tasks = new TaskList(STORAGE.load());
        UI.showWelcome();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            CommandType commandType = PARSER.parseCommandType(command);

            UI.showLine();
            if (commandType == CommandType.BYE) {
                UI.showFarewell();
                break;
            }

            try {
                switch (commandType) {
                    case LIST:
                        UI.showTaskList(tasks);
                        break;
                    case FIND:
                        String keyword = PARSER.getDescription(command, commandType.getCommandWord());
                        UI.showMatchingTasks(tasks.findByDescription(keyword));
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
                        UI.showTaskDeleted(removedTask, tasks.size());
                        break;
                    case TODO:
                        String description = PARSER.getDescription(command, commandType.getCommandWord());
                        Task todo = new Todo(description);
                        tasks.add(todo);
                        STORAGE.save(tasks);
                        UI.showTaskAdded(todo, tasks.size());
                        break;
                    case DEADLINE:
                        String[] deadlineDetails = PARSER.parseDeadline(command);
                        Task deadline = new Deadline(deadlineDetails[0], PARSER.parseDateTime(deadlineDetails[1]));
                        tasks.add(deadline);
                        STORAGE.save(tasks);
                        UI.showTaskAdded(deadline, tasks.size());
                        break;
                    case EVENT:
                        String[] eventDetails = PARSER.parseEvent(command);
                        Task event = new Event(eventDetails[0], PARSER.parseDateTime(eventDetails[1]),
                                PARSER.parseDateTime(eventDetails[2]));
                        tasks.add(event);
                        STORAGE.save(tasks);
                        UI.showTaskAdded(event, tasks.size());
                        break;
                    case ON:
                        UI.showTasksOnDate(PARSER.parseDate(command), tasks);
                        break;
                    case UNKNOWN:
                    default:
                        throw new PeanutButterCatException(
                                "Hiss-terical mix-up! I don't know that command yet. "
                                        + "Try another one, purr-lease!");
                }
            } catch (PeanutButterCatException exception) {
                UI.showError(exception.getMessage());
            }
            UI.showLine();
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
        } else {
            task.markAsNotDone();
        }
        UI.showTaskStatus(task, isDone);
        STORAGE.save(tasks);
    }
}
