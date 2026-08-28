package peanutbuttercat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Stores the tasks managed by the chatbot and provides collection operations.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks Tasks to copy into this list.
     */
    public TaskList(Collection<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the task at the zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns the tasks for read-only iteration by coordinating components. */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }

    /**
     * Returns tasks whose descriptions contain the supplied keyword.
     * Matching is case-insensitive and keeps the task-list order.
     *
     * @param keyword Text to find in task descriptions.
     * @return Matching tasks in their original order.
     */
    public List<Task> findByDescription(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword))
                .toList();
    }
}
