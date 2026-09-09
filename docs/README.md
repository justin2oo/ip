# peanutbuttercat User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Viewing recent completion statistics

Use `stats` to see how many tasks were completed today or during the previous six calendar days.

Example:

```text
stats
```

```text
Tasks completed in the last 7 calendar days: 2.
```

The command does not accept arguments. Completed tasks saved by an older version do not have a
completion date, so they are excluded and reported separately. To give such a task a completion date,
unmark it and mark it again.

Completion dates are saved as an optional final `yyyy-MM-dd` field in `data/duke.txt`. The application
can read older save files, but files containing completion dates should not be opened and saved with an
older application version because that version may discard the extended records.

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
