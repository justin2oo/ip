import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

/**
 * Interprets raw user input and validates command arguments.
 */
public class Parser {
    private static final DateTimeFormatter[] INPUT_FORMATS = {
        DateTimeFormatter.ofPattern("d/M/uuuu HHmm"),
        DateTimeFormatter.ofPattern("d/M/uuuu HH:mm"),
        DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm"),
        DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"),
        DateTimeFormatter.ofPattern("uuuu-MM-dd")
    };

    /** Identifies the command represented by the input. */
    public CommandType parseCommandType(String input) {
        return CommandType.fromInput(input);
    }

    /**
     * Creates command objects for the command cases already extracted from the main loop.
     *
     * @param input Full command entered by the user.
     * @return The corresponding command, or {@code null} while a command is still handled by
     *         the transition switch in the application coordinator.
     */
    public Command parse(String input) throws PeanutButterCatException {
        return switch (parseCommandType(input)) {
        case BYE -> new ExitCommand();
        case LIST -> new ListCommand();
        case TODO -> new TodoCommand(getDescription(input, CommandType.TODO.getCommandWord()));
        default -> null;
        };
    }

    /** Extracts a non-empty description after a command word. */
    public String getDescription(String command, String commandWord) throws PeanutButterCatException {
        String description = command.substring(commandWord.length()).trim();
        if (description.isEmpty()) {
            throw new PeanutButterCatException("Oops, this kitty needs a description for your "
                    + commandWord + "! Please add one after '" + commandWord + "'.");
        }
        return description;
    }

    /** Parses a deadline description and due time. */
    public String[] parseDeadline(String command) throws PeanutButterCatException {
        String details = command.substring(CommandType.DEADLINE.getCommandWord().length()).trim();
        int byIndex = details.indexOf("/by");
        if (byIndex < 0) {
            throw new PeanutButterCatException("My whiskers can't find the deadline! Use: "
                    + "deadline DESCRIPTION /by TIME");
        }
        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new PeanutButterCatException("Oops, this kitty needs a description for your deadline!");
        }
        if (by.isEmpty()) {
            throw new PeanutButterCatException("When is it due? Add a time after '/by', purr-lease!");
        }
        parseDateTime(by);
        return new String[]{description, by};
    }

    /** Parses an event description, start time, and end time. */
    public String[] parseEvent(String command) throws PeanutButterCatException {
        String details = command.substring(CommandType.EVENT.getCommandWord().length()).trim();
        int fromIndex = details.indexOf("/from");
        int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to", fromIndex + "/from".length());
        if (fromIndex < 0 || toIndex < 0) {
            throw new PeanutButterCatException("My whiskers need the whole time trail! Use: "
                    + "event DESCRIPTION /from START /to END");
        }
        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = details.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new PeanutButterCatException("Oops, this kitty needs a description for your event!");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new PeanutButterCatException("An event needs both start and end times - no missing paws!");
        }
        parseDateTime(from);
        parseDateTime(to);
        return new String[]{description, from, to};
    }

    /** Parses a supported date or date-time value. */
    public LocalDateTime parseDateTime(String value) throws PeanutButterCatException {
        for (DateTimeFormatter format : INPUT_FORMATS) {
            try {
                TemporalAccessor parsed = format.parse(value);
                LocalDate date = LocalDate.from(parsed);
                return parsed.isSupported(java.time.temporal.ChronoField.HOUR_OF_DAY)
                        ? LocalDateTime.from(parsed) : date.atStartOfDay();
            } catch (DateTimeParseException ignored) {
                // Try the next accepted input format.
            }
        }
        throw new PeanutButterCatException("I couldn't understand that date. Use yyyy-MM-dd "
                + "or d/M/yyyy HHmm, purr-lease!");
    }

    /** Parses the date argument of an {@code on} command. */
    public LocalDate parseDate(String command) throws PeanutButterCatException {
        String value = command.substring(CommandType.ON.getCommandWord().length()).trim();
        if (value.isEmpty()) {
            throw new PeanutButterCatException("Which date should I search? Use: on yyyy-MM-dd");
        }
        try {
            return parseDateTime(value).toLocalDate();
        } catch (PeanutButterCatException exception) {
            throw new PeanutButterCatException("I couldn't understand that date. Use yyyy-MM-dd, purr-lease!");
        }
    }

    /** Converts a one-based task number into a zero-based list index. */
    public int parseTaskIndex(String command, String commandWord, int numberOfTasks)
            throws PeanutButterCatException {
        String numberText = command.substring(commandWord.length()).trim();
        if (numberText.isEmpty()) {
            throw new PeanutButterCatException("Which task should I " + commandWord
                    + "? Give me its number, purr-lease!");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new PeanutButterCatException("My paws can only count whole task numbers. "
                    + "Try '" + commandWord + " 1', for example!");
        }
        if (taskNumber < 1 || taskNumber > numberOfTasks) {
            throw new PeanutButterCatException("I can't find task " + taskNumber
                    + " in my cat basket. Check 'list' and try again!");
        }
        return taskNumber - 1;
    }
}
