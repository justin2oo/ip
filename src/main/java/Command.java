/**
 * Represents one user command that can be executed by the chatbot.
 */
public abstract class Command {
    /**
     * Performs this command's work using the application's collaborators.
     *
     * @param tasks Mutable task list managed by the application.
     * @param ui Component responsible for user-facing output.
     * @param storage Component responsible for task persistence.
     * @throws PeanutButterCatException If the command arguments are invalid.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws PeanutButterCatException;

    /**
     * Indicates whether executing this command should end the application.
     *
     * @return {@code true} only for the exit command.
     */
    public boolean isExit() {
        return false;
    }
}
