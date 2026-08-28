package peanutbuttercat;

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
        this.description = requireNonBlank(description, "description");
        this.isDone = false;
    }

    /**
     * Validates a required text field shared by all task types.
     *
     * @param value Value to validate.
     * @param fieldName Name used in the exception message.
     * @return The original value with surrounding whitespace removed.
     */
    protected static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Task " + fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    /** Escapes storage delimiters while keeping the legacy file format readable. */
    protected static String escapeStorageField(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
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
        return "T | " + getCompletionState() + " | " + escapeStorageField(description);
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

    /**
     * Returns this task in the format shown in the task list.
     *
     * @return A display string containing the completion status and description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
