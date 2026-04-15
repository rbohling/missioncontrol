# Mission Control Dataset Processor

## Overview

Mission Control Dataset Processor is a multithreaded Java client-server application built for an Advanced Programming Concepts final project.

The program allows multiple clients to connect to a central server and send integer datasets for processing. Each client keeps its own dataset session, which means one client's data does not affect another client's data.

The light Mission Control theme makes the project more polished:
- The server acts as **Mission Control**
- Each client acts as a **Remote Station**
- Each dataset represents **telemetry readings**

The project still follows the exact technical requirements of the assignment.

## Features

- Multithreaded server using one thread per client
- Separate dataset session for every connected client
- Dataset upload with the `Dataset` command
- Merge Sort for sorting
- Linear Search for unsorted datasets
- Binary Search for sorted datasets
- Statistics command for count, minimum, maximum, and average
- Reset command to clear the current session dataset
- Input validation and graceful error handling

## Files

- `MissionControlServer.java` - main server program
- `StationClient.java` - interactive client program
- `TelemetrySession.java` - stores each client's dataset and sorted state
- `DatasetProcessor.java` - sorting, searching, formatting, and statistics logic

## Requirements

- Java 17 or later
- Windows Command Prompt, PowerShell, or any terminal that can run Java
- Localhost access on port 5000

## How to Compile

Open a terminal inside the project folder and run:

```bash
javac MissionControlServer.java StationClient.java TelemetrySession.java DatasetProcessor.java
```

## How to Run

Start the server first:

```bash
java MissionControlServer
```

Then open another terminal and run the client:

```bash
java StationClient
```

You can open multiple client windows to demonstrate concurrent connections.

## Supported Commands

### 1. Dataset Command

Stores a dataset of integers for the current client session.

Example:

```text
Dataset 12 5 8 3 20 7
```

Response:

```text
Dataset received.
```

### 2. Sort Command

Sorts the current dataset using Merge Sort.

Example:

```text
Sort
```

Response:

```text
3 5 7 8 12 20
```

### 3. Search Command

Searches for a value in the dataset.

Example:

```text
Search 8
```

Possible responses:

```text
Found at index 3 using Binary Search
```

or

```text
Item not found
```

### 4. Stat Command

Calculates statistics for the current dataset.

Example:

```text
Stat
```

Response:

```text
Count = 6
Min = 3
Max = 20
Average = 9.17
```

### 5. Reset Command

Clears the current dataset.

Example:

```text
Reset
```

Response:

```text
Dataset cleared.
```

### 6. Exit Command

Disconnects the client from the server.

Example:

```text
Exit
```

Response:

```text
Disconnecting from Mission Control.
```

## Example Session

```text
Connected to Mission Control at 127.0.0.1:5000
Connected to Mission Control. Enter commands.
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
Disconnecting from Mission Control.
```

## Error Handling

The program handles common errors such as:

```text
No dataset loaded. Please send a Dataset command first.
Invalid command
Dataset must contain only integers
Dataset command requires at least one integer
Search command requires exactly one integer value
```

## Threading Model

- The server listens for incoming client connections using `ServerSocket`.
- Each new client is assigned to a separate `ClientHandler` thread.
- Each thread owns its own `TelemetrySession`.
- Because each client has its own session object, datasets remain independent across clients.

## Algorithms Used

### Sorting
- **Merge Sort** is used for the `Sort` command.

### Searching
- **Linear Search** is used if the dataset has not been sorted yet.
- **Binary Search** is used after the dataset has been sorted.

### Statistics
The `Stat` command computes:
- Count
- Minimum
- Maximum
- Average

## Suggested Report Talking Points

You can use these ideas in your design report:
- Explain how sockets are used for communication between the client and server
- Describe how the multithreaded server allows multiple clients at the same time
- Explain why a separate session object is needed for each client
- Describe why Merge Sort, Linear Search, and Binary Search were selected
- Include screenshots showing server startup, normal command flow, and error handling

## Submission Tip

Before submitting, place these files in one folder and take a few screenshots of:
- the server running
- one client showing successful commands
- another client connected at the same time
- one error example
