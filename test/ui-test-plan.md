# UI Test Plan

## Project configuration

- Main class: `PeanutButterCat`
- Source directory: `src/main/java`
- Required Java major version: `25`
- Capacity: The task list grows dynamically and has no fixed 100-task limit.
- Isolation: Each test case starts a fresh application process with no `data/duke.txt` file.
- Comparison: Output must match exactly after normalizing CRLF/LF line endings and ignoring one final line terminator.

## Test case: Exit the application
**Aim:** Verify that `bye` prints the farewell and exits cleanly.
### Inputs
```text
bye
```
### Expected output
```text
____________________________________________________________
 /\_/\
( o.o )  peanutbuttercat
 > u <
Hey! I'm peanutbuttercat, and I'm pawsitively ready to help!
What pawsome task can we tackle together?
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Parse, format, save, and query dates
**Aim:** Verify that slash and ISO date/time inputs become typed calendar values, display readably, persist, and can be queried by date.
### Inputs
```text
deadline return book /by 2/12/2019 1800
event project meeting /from 2019-12-02 1900 /to 2019-12-02 2000
on 2019-12-02
list
bye
```
### Expected output
```text
____________________________________________________________
 /\_/\
( o.o )  peanutbuttercat
 > u <
Hey! I'm peanutbuttercat, and I'm pawsitively ready to help!
What pawsome task can we tackle together?
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[D][ ] return book (by: Dec 02 2019 6:00 pm)
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[E][ ] project meeting (from: Dec 02 2019 7:00 pm to: Dec 02 2019 8:00 pm)
My cat basket now holds 2 tasks.
____________________________________________________________
____________________________________________________________
Here are the tasks on Dec 02 2019:
1.[D][ ] return book (by: Dec 02 2019 6:00 pm)
2.[E][ ] project meeting (from: Dec 02 2019 7:00 pm to: Dec 02 2019 8:00 pm)
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[D][ ] return book (by: Dec 02 2019 6:00 pm)
2.[E][ ] project meeting (from: Dec 02 2019 7:00 pm to: Dec 02 2019 8:00 pm)
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Reject invalid dates and continue
**Aim:** Verify that malformed dates are rejected without adding tasks.
### Inputs
```text
deadline invalid /by no idea
event invalid /from 2019-02-30 /to 2019-03-01
todo recover gracefully
list
bye
```
### Expected output
```text
____________________________________________________________
 /\_/\
( o.o )  peanutbuttercat
 > u <
Hey! I'm peanutbuttercat, and I'm pawsitively ready to help!
What pawsome task can we tackle together?
____________________________________________________________
____________________________________________________________
I couldn't understand that date. Use yyyy-MM-dd or d/M/yyyy HHmm, purr-lease!
____________________________________________________________
____________________________________________________________
I couldn't understand that date. Use yyyy-MM-dd or d/M/yyyy HHmm, purr-lease!
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] recover gracefully
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][ ] recover gracefully
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```
