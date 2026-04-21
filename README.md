# Dataset Processor

A multithreaded Java client-server program for an Advanced Programming Concepts final project.

Multiple clients can connect to the server at the same time and send integer datasets for processing. Each client has its own session, so one client's data doesn't affect another's.

## Files

- `Server.java` - the server
- `Client.java` - the client
- `Session.java` - stores each client's dataset
- `Processor.java` - sorting, searching, and statistics

## Requirements

- Java 17 or later
- Port 5000 available on localhost

## How to Compile

```bash
javac Server.java Client.java Session.java Processor.java
```

## How to Run

Start the server first:

```bash
java Server
```

Then open another terminal and run the client:

```bash
java Client
```

You can open multiple client terminals at the same time.

## Commands

| Command | Description |
|---------|-------------|
| `Dataset 1 2 3` | Load a list of integers |
| `Sort` | Sort the dataset (bubble sort) |
| `Search 8` | Search for a value |
| `Stat` | Show count, min, max, and average |
| `Reset` | Clear the current dataset |
| `Exit` | Disconnect from the server |

## Example Session

```text
Connected to server at 127.0.0.1:5000
Connected to server. Enter commands.
Enter command: Dataset 12 5 8 3 20 7
Dataset received.
Enter command: Sort
3 5 7 8 12 20
Enter command: Search 8
Found at index 3 using Binary Search
Enter command: Stat
Count = 6
Min = 3
Max = 20
Average = 9.17
Enter command: Reset
Dataset cleared.
Enter command: Exit
Disconnecting.
```

## Error Messages

```text
No dataset loaded. Please send a Dataset command first.
Invalid command
Dataset must contain only integers
Dataset command requires at least one integer
Search command requires exactly one integer value
```
