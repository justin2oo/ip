package peanutbuttercat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents a task that must be completed by a calendar date and time. */
public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
    private final LocalDateTime by;

    /**
     * Creates an incomplete deadline with the given description and due time.
     *
     * @param description Description of the deadline.
     * @param by Date and time by which the deadline should be completed.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        if (by == null) {
            throw new IllegalArgumentException("Task due time cannot be blank.");
        }
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    @Override
    public String toFileString() {
        return "D | " + getCompletionState() + " | " + escapeStorageField(getDescription())
                + " | " + by;
    }
}
