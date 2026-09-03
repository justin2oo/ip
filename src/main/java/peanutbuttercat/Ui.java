package peanutbuttercat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Handles all console input/output presentation for the chatbot.
 */
public class Ui {
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /** Displays the chatbot's welcome message. */
    public void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(" /\\_/\\\n"
                + "( o.o )  peanutbuttercat\n"
                + " > u <");
        System.out.println("Hey! I'm peanutbuttercat, and I'm pawsitively ready to help!");
        System.out.println("What pawsome task can we tackle together?");
        showLine();
    }

    /** Displays the separator between interactions. */
    public void showLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    /** Displays the chatbot's farewell message. */
    public void showFarewell() {
        System.out.println(getFarewellMessage());
        showLine();
    }

    /** Returns the chatbot's farewell message. */
    public String getFarewellMessage() {
        return "Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!";
    }

    /** Displays a command-processing error. */
    public void showError(String message) {
        System.out.println(getErrorMessage(message));
    }

    /** Returns a command-processing error message. */
    public String getErrorMessage(String message) {
        return message;
    }

    /** Displays all tasks currently stored. */
    public void showTaskList(TaskList tasks) {
        System.out.println(getTaskListMessage(tasks));
    }

    /** Returns a formatted list of all tasks currently stored. */
    public String getTaskListMessage(TaskList tasks) {
        StringBuilder message = new StringBuilder("Here are the tasks in my cat basket:");
        for (int i = 0; i < tasks.size(); i++) {
            message.append(System.lineSeparator()).append(i + 1).append('.').append(tasks.get(i));
        }
        return message.toString();
    }

    /** Displays tasks whose descriptions contain the searched keyword. */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println(getMatchingTasksMessage(matchingTasks));
    }

    /** Returns a formatted list of tasks matching a description keyword. */
    public String getMatchingTasksMessage(List<Task> matchingTasks) {
        StringBuilder message = new StringBuilder("Here are the matching tasks in your list:");
        if (matchingTasks.isEmpty()) {
            return message.append(System.lineSeparator()).append("No matching tasks found.").toString();
        }
        for (int i = 0; i < matchingTasks.size(); i++) {
            message.append(System.lineSeparator()).append(i + 1).append('.').append(matchingTasks.get(i));
        }
        return message.toString();
    }

    /** Displays tasks whose deadline or event range includes the supplied date. */
    public void showTasksOnDate(LocalDate date, TaskList tasks) {
        System.out.println(getTasksOnDateMessage(date, tasks));
    }

    /** Returns a formatted list of tasks whose scheduled date includes the supplied date. */
    public String getTasksOnDateMessage(LocalDate date, TaskList tasks) {
        StringBuilder message = new StringBuilder("Here are the tasks on ")
                .append(date.format(DISPLAY_DATE)).append(':');
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            boolean matchesDeadline = task instanceof Deadline deadline
                    && deadline.getBy().toLocalDate().equals(date);
            boolean matchesEvent = task instanceof Event event
                    && !event.getFrom().toLocalDate().isAfter(date)
                    && !event.getTo().toLocalDate().isBefore(date);
            boolean matches = matchesDeadline || matchesEvent;
            if (matches) {
                message.append(System.lineSeparator()).append(i + 1).append('.').append(task);
            }
        }
        return message.toString();
    }

    /** Displays confirmation after adding a task. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(getTaskAddedMessage(task, taskCount));
    }

    /** Returns a confirmation that a task was added. */
    public String getTaskAddedMessage(Task task, int taskCount) {
        return "Purr-fect! I've added this task to my cat basket:" + System.lineSeparator()
                + task + System.lineSeparator() + getTaskCountMessage(taskCount);
    }

    /** Displays confirmation after deleting a task. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(getTaskDeletedMessage(task, taskCount));
    }

    /** Returns a confirmation that a task was deleted. */
    public String getTaskDeletedMessage(Task task, int taskCount) {
        return "Purr-fect! I've removed this task from my cat basket:" + System.lineSeparator()
                + "  " + task + System.lineSeparator() + getTaskCountMessage(taskCount);
    }

    /** Displays confirmation after changing a task's completion status. */
    public void showTaskStatus(Task task, boolean isDone) {
        System.out.println(getTaskStatusMessage(task, isDone));
    }

    /** Returns a confirmation that a task's completion status changed. */
    public String getTaskStatusMessage(Task task, boolean isDone) {
        String message = isDone
                ? "Pawsome! I've marked this task as done:"
                : "No paw-blem! I've marked this task as not done yet:";
        return message + System.lineSeparator() + "  " + task;
    }

    private String getTaskCountMessage(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        return "My cat basket now holds " + taskCount + " " + taskWord + ".";
    }
}
