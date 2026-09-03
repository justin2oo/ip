package peanutbuttercat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents a task with a stated start and end date and time. */
public class Event extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an incomplete event with the given description and time range.
     *
     * @param description Description of the event.
     * @param from Date and time at which the event starts.
     * @param to Date and time at which the event ends.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        if (from == null || to == null) {
            throw new IllegalArgumentException("Event dates cannot be blank.");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the date and time at which this event starts.
     *
     * @return This event's start date and time.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the date and time at which this event ends.
     *
     * @return This event's end date and time.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns the event in the format shown to users.
     *
     * @return A display string containing the task status, description, and time range.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from.format(OUTPUT_FORMAT) + " to: " + to.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Returns the event in the format used by the storage file.
     *
     * @return A pipe-delimited event record.
     */
    @Override
    public String toFileString() {
        return "E | " + getCompletionState() + " | " + escapeStorageField(getDescription())
                + " | " + from + " | " + to;
    }
}
