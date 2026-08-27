package peanutbuttercat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        System.out.println("Bye! Hope to see you again soon. Stay pawsitive and keep spreading "
                + "the peanut butter!");
        showLine();
    }

    /** Displays a command-processing error. */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Displays all tasks currently stored. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in my cat basket:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays tasks whose deadline or event range includes the supplied date. */
    public void showTasksOnDate(LocalDate date, TaskList tasks) {
        System.out.println("Here are the tasks on " + date.format(DISPLAY_DATE) + ":");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            boolean matches = task instanceof Deadline deadline
                    && deadline.getBy().toLocalDate().equals(date)
                    || task instanceof Event event
                    && (!event.getFrom().toLocalDate().isAfter(date)
                    && !event.getTo().toLocalDate().isBefore(date));
            if (matches) {
                System.out.println((i + 1) + "." + task);
            }
        }
    }

    /** Displays confirmation after adding a task. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Purr-fect! I've added this task to my cat basket:");
        System.out.println(task);
        showTaskCount(taskCount);
    }

    /** Displays confirmation after deleting a task. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Purr-fect! I've removed this task from my cat basket:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /** Displays confirmation after changing a task's completion status. */
    public void showTaskStatus(Task task, boolean isDone) {
        System.out.println(isDone
                ? "Pawsome! I've marked this task as done:"
                : "No paw-blem! I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    private void showTaskCount(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("My cat basket now holds " + taskCount + " " + taskWord + ".");
    }
}
