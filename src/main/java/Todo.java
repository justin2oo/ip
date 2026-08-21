/**
 * Represents a task without an attached date or time.
 */
public class Todo {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete todo with the given description.
     *
     * @param description Description of the todo.
     */
    public Todo(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this todo as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this todo as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    private String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }
}
