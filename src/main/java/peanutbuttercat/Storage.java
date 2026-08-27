package peanutbuttercat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads tasks from and saves tasks to the application's storage file.
 */
public class Storage {
    private final Path saveFile;

    /** Creates storage backed by the supplied file path. */
    public Storage(String filePath) {
        this.saveFile = Path.of(filePath);
    }

    /**
     * Saves all tasks using the existing atomic-replacement strategy.
     *
     * @param tasks Tasks to save.
     */
    public void save(TaskList tasks) {
        List<String> taskRecords = new ArrayList<>();
        for (Task task : tasks.asList()) {
            taskRecords.add(task.toFileString());
        }

        try {
            Files.createDirectories(saveFile.getParent());
            Path temporaryFile = Files.createTempFile(saveFile.getParent(), "duke", ".tmp");
            try {
                Files.write(temporaryFile, taskRecords);
                try {
                    Files.move(temporaryFile, saveFile, StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.ATOMIC_MOVE);
                } catch (java.nio.file.AtomicMoveNotSupportedException exception) {
                    Files.move(temporaryFile, saveFile, StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                Files.deleteIfExists(temporaryFile);
            }
        } catch (IOException | SecurityException exception) {
            System.err.println("Unable to save tasks: " + exception.getMessage());
        }
    }

    /**
     * Loads saved tasks, ignoring blank or malformed records as before.
     *
     * @return The loaded tasks, or an empty list when the file is unavailable.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            if (!Files.exists(saveFile)) {
                return tasks;
            }
            int lineNumber = 0;
            for (String taskRecord : Files.readAllLines(saveFile)) {
                lineNumber++;
                if (taskRecord.isBlank()) {
                    continue;
                }
                try {
                    tasks.add(createTaskFromRecord(taskRecord));
                } catch (IllegalArgumentException exception) {
                    System.err.println("Ignoring invalid task record at line " + lineNumber + ".");
                }
            }
        } catch (IOException | SecurityException exception) {
            System.err.println("Unable to load tasks: " + exception.getMessage());
        }
        return tasks;
    }

    private static Task createTaskFromRecord(String taskRecord) {
        String[] details = splitStorageRecord(taskRecord);
        if (details.length < 3 || details.length > 5
                || (!details[1].equals("0") && !details[1].equals("1"))) {
            throw new IllegalArgumentException("Malformed task record");
        }
        Task task;
        switch (details[0]) {
        case "T":
            requireFieldCount(details, 3);
            task = new Todo(details[2]);
            break;
        case "D":
            requireFieldCount(details, 4);
            task = new Deadline(details[2], parseStoredDateTime(details[3]));
            break;
        case "E":
            requireFieldCount(details, 5);
            task = new Event(details[2], parseStoredDateTime(details[3]), parseStoredDateTime(details[4]));
            break;
        default:
            throw new IllegalArgumentException("Unknown task type in saved data: " + details[0]);
        }

        if (details[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    private static void requireFieldCount(String[] details, int expected) {
        if (details.length != expected) {
            throw new IllegalArgumentException("Malformed task record");
        }
    }

    /** Splits a record without treating escaped pipe characters as delimiters. */
    private static String[] splitStorageRecord(String record) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < record.length(); i++) {
            char current = record.charAt(i);
            if (escaped) {
                if (current != '|' && current != '\\') {
                    throw new IllegalArgumentException("Invalid escape sequence");
                }
                field.append(current);
                escaped = false;
            } else if (current == '\\') {
                escaped = true;
            } else if (current == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(current);
            }
        }
        if (escaped) {
            throw new IllegalArgumentException("Unterminated escape sequence");
        }
        fields.add(field.toString().trim());
        return fields.toArray(String[]::new);
    }

    private static LocalDateTime parseStoredDateTime(String value) {
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid stored date", exception);
        }
    }
}
