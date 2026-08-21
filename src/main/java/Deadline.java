/**
 * Represents a task that must be completed by a stated date or time.
 */
public class Deadline {
    private final String description;
    private final String by;
    private boolean isDone;

    /**
     * Creates an incomplete deadline with the given description and due time.
     *
     * @param description Description of the deadline.
     * @param by Date or time by which the deadline should be completed.
     */
    public Deadline(String description, String by) {
        this.description = description;
        this.by = by;
        this.isDone = false;
    }

    /**
     * Marks this deadline as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this deadline as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    private String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }
}
