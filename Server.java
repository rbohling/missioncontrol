// These imports bring in the network and data tools the server needs
import java.io.BufferedReader;       // lets us read text from a client one line at a time
import java.io.IOException;          // handles errors that happen during network communication
import java.io.InputStreamReader;    // converts raw bytes into readable characters
import java.io.PrintWriter;          // lets us send text back to a client easily
import java.net.ServerSocket;        // the "listening ear" — waits for clients to connect
import java.net.Socket;              // represents one connected client
import java.util.ArrayList;          // a resizable list we use to store numbers
import java.util.List;               // the general List type (ArrayList implements this)

// The Server class runs continuously, waiting for clients to connect and send commands
public class Server {
    // The IP address the server listens on — "127.0.0.1" means only local connections
    private static final String HOST = "127.0.0.1";

    // The port number the server will listen on — must match what the client uses
    private static final int PORT = 5000;

    // main() is where the server program starts
    public static void main(String[] args) {
        // Tell us the server has started up
        System.out.println("Server is running on " + HOST + ":" + PORT);

        // Open a ServerSocket on the chosen port — this lets clients connect to us
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Keep the server running forever, accepting new clients
            while (true) {
                // Wait here until a client connects, then get that connection as a Socket
                Socket clientSocket = serverSocket.accept();
                // Print a message showing which computer just connected
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a handler (a separate thread) to talk to this specific client
                // This lets the server handle multiple clients at the same time
                ClientHandler handler = new ClientHandler(clientSocket);
                // Start running the handler in its own thread
                handler.start();
            }
        } catch (IOException e) {
            // If the server crashes or has a network problem, show the error
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // ClientHandler is a Thread — it runs at the same time as the rest of the server
    // Each connected client gets its own ClientHandler so they don't block each other
    private static class ClientHandler extends Thread {
        // The socket is the connection to this specific client
        private final Socket socket;
        // The session stores this client's dataset and whether it's been sorted
        private final Session session;

        // Constructor — called when a new client connects
        public ClientHandler(Socket socket) {
            this.socket = socket;           // save the client's connection
            this.session = new Session();   // create a fresh, empty session for this client
        }

        // run() is what executes when handler.start() is called above
        @Override
        public void run() {
            try (
                    // Set up a reader so we can receive text lines from this client
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // Set up a writer so we can send text lines back to this client
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Send the client a welcome message as soon as they connect
                out.println("Connected to server. Enter commands.");

                // Keep reading commands from the client until they disconnect
                String line;
                while ((line = in.readLine()) != null) {
                    // Process the command and get the result, trimming extra spaces
                    String response = processCommand(line.trim());
                    // Send the result back to the client
                    out.println(response);

                    // If the client typed "Exit", stop the loop and close the connection
                    if (line.equalsIgnoreCase("Exit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                // If something goes wrong while talking to this client, log the error
                System.out.println("Client communication error: " + e.getMessage());
            } finally {
                // "finally" always runs — even if there was an error above
                try {
                    socket.close(); // close the network connection to this client
                } catch (IOException ignored) {
                    // if closing fails we just ignore it — not much we can do
                }
                // Log that this client has left
                System.out.println("Client disconnected: " + socket.getInetAddress());
            }
        }

        // Figures out which command the client sent and calls the right handler method
        private String processCommand(String input) {
            // If the client sent an empty string, that's not a valid command
            if (input.isEmpty()) {
                return "Invalid command";
            }

            // Split the input on whitespace so we can look at each word separately
            // e.g. "dataset 3 7 2" becomes ["dataset", "3", "7", "2"]
            String[] parts = input.split("\\s+");
            // The first word is always the command name; convert to lowercase for easy comparison
            String command = parts[0].toLowerCase();

            // Check which command was sent and call the matching handler
            switch (command) {
                case "dataset":
                    return handleDataset(parts);   // load a list of numbers into the session
                case "sort":
                    return handleSort(parts);       // sort the loaded dataset
                case "search":
                    return handleSearch(parts);     // search for a number in the dataset
                case "stat":
                    return handleStat(parts);       // calculate stats (count, min, max, average)
                case "reset":
                    return handleReset(parts);      // clear the dataset from memory
                case "exit":
                    return "Disconnecting.";        // tell the client we're closing the connection
                default:
                    return "Invalid command";       // the client sent something we don't recognize
            }
        }

        // Handles the "dataset" command — reads the numbers and stores them in the session
        private String handleDataset(String[] parts) {
            // "dataset" must be followed by at least one number, so parts must have 2+ elements
            if (parts.length < 2) {
                return "Dataset command requires at least one integer";
            }

            // Create an empty list to hold the numbers we're about to read
            List<Integer> values = new ArrayList<>();
            // Loop through every word after "dataset" (starting at index 1)
            for (int i = 1; i < parts.length; i++) {
                try {
                    // Try to convert the word to a whole number and add it to our list
                    values.add(Integer.parseInt(parts[i]));
                } catch (NumberFormatException e) {
                    // If a word isn't a valid number, tell the client what went wrong
                    return "Dataset must contain only integers";
                }
            }

            // Save the list of numbers into the session so other commands can use it
            session.setDataset(values);
            return "Dataset received.";
        }

        // Handles the "sort" command — sorts the dataset using bubble sort
        private String handleSort(String[] parts) {
            // "sort" takes no extra arguments, so parts should only have one element
            if (parts.length != 1) {
                return "Invalid command";
            }

            // Can't sort if no dataset has been loaded yet
            if (!session.hasDataset()) {
                return "No dataset loaded. Please send a Dataset command first.";
            }

            // Sort the current dataset using bubble sort (defined in Processor.java)
            List<Integer> sortedData = Processor.bubbleSort(session.getDataset());
            // Save the sorted version back into the session
            session.setDataset(sortedData);
            // Mark the session as sorted — this lets Search use the faster binary search
            session.setSorted(true);
            // Send the sorted numbers back to the client as a string
            return Processor.formatList(sortedData);
        }

        // Handles the "search" command — finds a number in the dataset
        private String handleSearch(String[] parts) {
            // "search" needs exactly one argument: the number to look for
            if (parts.length != 2) {
                return "Search command requires exactly one integer value";
            }

            // Can't search if no dataset has been loaded yet
            if (!session.hasDataset()) {
                return "No dataset loaded. Please send a Dataset command first.";
            }

            // Try to convert the search argument to a whole number
            int target;
            try {
                target = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                // If the argument isn't a number, tell the client
                return "Search command requires exactly one integer value";
            }

            // Choose the search method based on whether the dataset is sorted
            int index;
            if (session.isSorted()) {
                // Binary search is much faster, but only works on a sorted list
                index = Processor.binarySearch(session.getDataset(), target);
                return index >= 0
                        ? "Found at index " + index + " using Binary Search"
                        : "Item not found";
            } else {
                // Linear search checks every element one by one — works on any list
                index = Processor.linearSearch(session.getDataset(), target);
                return index >= 0
                        ? "Found at index " + index + " using Linear Search"
                        : "Item not found";
            }
        }

        // Handles the "stat" command — calculates and returns statistics about the dataset
        private String handleStat(String[] parts) {
            // "stat" takes no extra arguments
            if (parts.length != 1) {
                return "Invalid command";
            }

            // Can't compute stats if there's no data
            if (!session.hasDataset()) {
                return "No dataset loaded. Please send a Dataset command first.";
            }

            // Let the Processor class do the math and return the result
            return Processor.calculateStats(session.getDataset());
        }

        // Handles the "reset" command — clears the dataset from the session
        private String handleReset(String[] parts) {
            // "reset" takes no extra arguments
            if (parts.length != 1) {
                return "Invalid command";
            }

            // Wipe all stored data so the client can start fresh
            session.clear();
            return "Dataset cleared.";
        }
    }
}
