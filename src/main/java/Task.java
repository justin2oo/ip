/**
 * Represents the shared description and completion state of a task.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task in the format used by the on-disk task list.
     *
     * @return A pipe-delimited task record.
     */
    public String toFileString() {
        return "T | " + getCompletionState() + " | " + description;
    }

    /**
     * Returns this task's completion state in the storage format.
     *
     * @return {@code 1} when the task is done, otherwise {@code 0}.
     */
    protected String getCompletionState() {
        return isDone ? "1" : "0";
    }

    /**
     * Returns the task description for subclasses that format storage records.
     *
     * @return This task's description.
     */
    protected String getDescription() {
        return description;
    }

    private String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
