# UI Test Plan

## Project configuration

- Main class: `PeanutButterCat`
- Source directory: `src/main/java`
- Required Java major version: `25`
- Capacity: The task list grows dynamically and has no fixed 100-task limit.
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
What pawsome task can we tackle together?
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
What pawsome task can we tackle together?
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] borrow book
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[D][ ] return book (by: Sunday)
My cat basket now holds 2 tasks.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
My cat basket now holds 3 tasks.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[D][ ] do homework (by: no idea :-p)
My cat basket now holds 4 tasks.
____________________________________________________________
____________________________________________________________
Pawsome! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
No paw-blem! I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
4.[D][ ] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Reject empty todos and unknown commands
**Aim:** Verify that an empty todo description and an unknown command produce friendly errors without stopping the chatbot.

### Inputs
```text
todo
sing
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
Oops, this kitty needs a description for your todo! Please add one after 'todo'.
____________________________________________________________
____________________________________________________________
Hiss-terical mix-up! I don't know that command yet. Try another one, purr-lease!
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Reject incomplete deadline and event details
**Aim:** Verify that missing task descriptions and date or time fields produce useful errors and that the chatbot continues accepting commands.

### Inputs
```text
deadline return book
deadline /by Sunday
deadline return book /by
event team meeting
event /from 2pm /to 3pm
event team meeting /from /to 3pm
todo recover gracefully
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
My whiskers can't find the deadline! Use: deadline DESCRIPTION /by TIME
____________________________________________________________
____________________________________________________________
Oops, this kitty needs a description for your deadline!
____________________________________________________________
____________________________________________________________
When is it due? Add a time after '/by', purr-lease!
____________________________________________________________
____________________________________________________________
My whiskers need the whole time trail! Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Oops, this kitty needs a description for your event!
____________________________________________________________
____________________________________________________________
An event needs both start and end times - no missing paws!
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] recover gracefully
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Reject invalid task numbers
**Aim:** Verify that missing, non-numeric, and out-of-range task numbers are handled without crashing or corrupting existing tasks.

### Inputs
```text
mark
mark first
mark 1
todo chase string
unmark 2
mark 1
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
Which task should I mark? Give me its number, purr-lease!
____________________________________________________________
____________________________________________________________
My paws can only count whole task numbers. Try 'mark 1', for example!
____________________________________________________________
____________________________________________________________
I can't find task 1 in my cat basket. Check 'list' and try again!
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] chase string
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
I can't find task 2 in my cat basket. Check 'list' and try again!
____________________________________________________________
____________________________________________________________
Pawsome! I've marked this task as done:
  [T][X] chase string
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Preserve task count across rejected additions
**Aim:** Verify that malformed additions and unknown commands interleaved with valid additions do not consume task slots or alter the valid tasks.

### Inputs
```text
todo keep first
todo
deadline submit report /by Friday
deadline missing date
event demo /from 2pm /to 3pm
event broken /from 2pm
sing
todo keep last
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
[T][ ] keep first
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
Oops, this kitty needs a description for your todo! Please add one after 'todo'.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[D][ ] submit report (by: Friday)
My cat basket now holds 2 tasks.
____________________________________________________________
____________________________________________________________
My whiskers can't find the deadline! Use: deadline DESCRIPTION /by TIME
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[E][ ] demo (from: 2pm to: 3pm)
My cat basket now holds 3 tasks.
____________________________________________________________
____________________________________________________________
My whiskers need the whole time trail! Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Hiss-terical mix-up! I don't know that command yet. Try another one, purr-lease!
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] keep last
My cat basket now holds 4 tasks.
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][ ] keep first
2.[D][ ] submit report (by: Friday)
3.[E][ ] demo (from: 2pm to: 3pm)
4.[T][ ] keep last
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Preserve completion state across invalid task numbers
**Aim:** Verify that invalid mark and unmark commands interleaved with valid updates do not change any task's completion state.

### Inputs
```text
todo first
todo second
mark 1
mark 0
unmark 3
mark second
list
unmark 1
mark 2
unmark -1
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
[T][ ] first
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] second
My cat basket now holds 2 tasks.
____________________________________________________________
____________________________________________________________
Pawsome! I've marked this task as done:
  [T][X] first
____________________________________________________________
____________________________________________________________
I can't find task 0 in my cat basket. Check 'list' and try again!
____________________________________________________________
____________________________________________________________
I can't find task 3 in my cat basket. Check 'list' and try again!
____________________________________________________________
____________________________________________________________
My paws can only count whole task numbers. Try 'mark 1', for example!
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][X] first
2.[T][ ] second
____________________________________________________________
____________________________________________________________
No paw-blem! I've marked this task as not done yet:
  [T][ ] first
____________________________________________________________
____________________________________________________________
Pawsome! I've marked this task as done:
  [T][X] second
____________________________________________________________
____________________________________________________________
I can't find task -1 in my cat basket. Check 'list' and try again!
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][ ] first
2.[T][X] second
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Reject blank and near-match commands without changing the list
**Aim:** Verify that empty, whitespace-only, case-mismatched, and command-prefix inputs interleaved with valid commands leave the task list unchanged.

### Inputs
```text
list
todo alpha

todo    
Todo beta
todoish gamma
list
todo beta
marking 1
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
Here are the tasks in my cat basket:
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] alpha
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
Hiss-terical mix-up! I don't know that command yet. Try another one, purr-lease!
____________________________________________________________
____________________________________________________________
Oops, this kitty needs a description for your todo! Please add one after 'todo'.
____________________________________________________________
____________________________________________________________
Hiss-terical mix-up! I don't know that command yet. Try another one, purr-lease!
____________________________________________________________
____________________________________________________________
Hiss-terical mix-up! I don't know that command yet. Try another one, purr-lease!
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][ ] alpha
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] beta
My cat basket now holds 2 tasks.
____________________________________________________________
____________________________________________________________
Hiss-terical mix-up! I don't know that command yet. Try another one, purr-lease!
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][ ] alpha
2.[T][ ] beta
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```

## Test case: Delete a task and reject invalid delete numbers
**Aim:** Verify that deleting a task removes it, shifts later task numbers, preserves the remaining tasks, and rejects invalid task numbers without changing the list.

### Inputs
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
mark 4
list
delete 3
delete
delete third
delete 0
delete 5
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
[T][ ] read book
My cat basket now holds 1 task.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[D][ ] return book (by: June 6th)
My cat basket now holds 2 tasks.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
My cat basket now holds 3 tasks.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] join sports club
My cat basket now holds 4 tasks.
____________________________________________________________
____________________________________________________________
Purr-fect! I've added this task to my cat basket:
[T][ ] borrow book
My cat basket now holds 5 tasks.
____________________________________________________________
____________________________________________________________
Pawsome! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Pawsome! I've marked this task as done:
  [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
Pawsome! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Purr-fect! I've removed this task from my cat basket:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
My cat basket now holds 4 tasks.
____________________________________________________________
____________________________________________________________
Which task should I delete? Give me its number, purr-lease!
____________________________________________________________
____________________________________________________________
My paws can only count whole task numbers. Try 'delete 1', for example!
____________________________________________________________
____________________________________________________________
I can't find task 0 in my cat basket. Check 'list' and try again!
____________________________________________________________
____________________________________________________________
I can't find task 5 in my cat basket. Check 'list' and try again!
____________________________________________________________
____________________________________________________________
Here are the tasks in my cat basket:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[T][X] join sports club
4.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Bye! Hope to see you again soon. Stay pawsitive and keep spreading the peanut butter!
____________________________________________________________
```
