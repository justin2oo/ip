package peanutbuttercat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests task-list filtering by description. */
class TaskListTest {
    @Test
    void findByDescription_caseInsensitiveSubstring_returnsMatchingTasksInOrder() {
        TaskList tasks = new TaskList(List.of(
                new Todo("Read a book"),
                new Todo("Buy groceries"),
                new Deadline("Return book", java.time.LocalDateTime.of(2026, 6, 6, 18, 0))));

        List<Task> matches = tasks.findByDescription("BOOK");

        assertEquals(List.of(tasks.get(0), tasks.get(2)), matches);
    }

    @Test
    void findByDescription_noMatch_returnsEmptyList() {
        TaskList tasks = new TaskList(List.of(new Todo("Read a book")));

        assertEquals(List.of(), tasks.findByDescription("travel"));
    }
}
