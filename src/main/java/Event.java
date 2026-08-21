/**
 * Represents a task with a stated start and end date or time.
 */
public class Event {
    private final String description;
    private final String from;
    private final String to;
    private boolean isDone;

    /**
     * Creates an incomplete event with the given description and time range.
     *
     * @param description Description of the event.
     * @param from Date or time at which the event starts.
     * @param to Date or time at which the event ends.
     */
    public Event(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
        this.isDone = false;
    }

    /**
     * Marks this event as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this event as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    private String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + from + " to: " + to + ")";
    }
}
