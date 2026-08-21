# UI Test Plan

## Project configuration

- Main class: `PeanutButterCat`
- Source directory: `src/main/java`
- Required Java major version: `25`
- Isolation: Each test case starts a fresh application process. Inputs within a test case run in order in the same process.
- Comparison: Output must match exactly after normalizing CRLF/LF line endings and ignoring one final line terminator.

## Test case: Exit the application
**Aim:** Verify that the `bye` command prints the farewell and exits cleanly.

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
What awesome task can we tackle together?
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Add and manage all task types
**Aim:** Verify that todos, deadlines, and events retain their type-specific details as strings and can be marked, unmarked, and listed.

### Inputs
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
deadline do homework /by no idea :-p
mark 2
unmark 2
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
What awesome task can we tackle together?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] do homework (by: no idea :-p)
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
4.[D][ ] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```
