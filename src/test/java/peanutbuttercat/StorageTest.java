package peanutbuttercat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests loading tasks from and saving tasks to storage. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void save_multipleTasks_writesAllTaskRecordsInOrder() throws IOException {
        Path saveFile = temporaryDirectory.resolve("data").resolve("tasks.txt");
        Storage storage = new Storage(saveFile.toString());
        TaskList tasks = new TaskList(List.of(
                new Todo("read book"),
                new Deadline("return book", LocalDateTime.of(2026, 6, 6, 18, 0))));

        storage.save(tasks);

        assertEquals(List.of(
                "T | 0 | read book",
                "D | 0 | return book | 2026-06-06T18:00"), Files.readAllLines(saveFile));
    }
}
