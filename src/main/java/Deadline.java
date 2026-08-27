/**
 * Represents a task that must be completed by a stated date or time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline with the given description and due time.
     *
     * @param description Description of the deadline.
     * @param by Date or time by which the deadline should be completed.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toFileString() {
        return "D | " + getCompletionState() + " | " + getDescription() + " | " + by;
    }
}
