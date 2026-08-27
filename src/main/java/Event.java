/**
 * Represents a task with a stated start and end date or time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event with the given description and time range.
     *
     * @param description Description of the event.
     * @param from Date or time at which the event starts.
     * @param to Date or time at which the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = requireNonBlank(from, "event start time");
        this.to = requireNonBlank(to, "event end time");
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toFileString() {
        return "E | " + getCompletionState() + " | " + escapeStorageField(getDescription())
                + " | " + escapeStorageField(from) + " | " + escapeStorageField(to);
    }
}
