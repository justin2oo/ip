/**
 * Adds a simple todo task to the chatbot's task list.
 */
public class TodoCommand extends Command {
    private final String description;

    /**
     * Creates a todo command with the supplied description.
     *
     * @param description Description of the task to add.
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task todo = new Todo(description);
        tasks.add(todo);
        storage.save(tasks);
        ui.showTaskAdded(todo, tasks.size());
    }
}
