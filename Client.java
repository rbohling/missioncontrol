// These imports bring in the tools we need to read/write data over the network
import java.io.BufferedReader;       // lets us read text from the server one line at a time
import java.io.IOException;          // handles errors that can happen during network communication
import java.io.InputStreamReader;    // converts raw bytes from the network into characters we can read
import java.io.PrintWriter;          // lets us send text to the server easily
import java.net.Socket;              // represents our connection to the server
import java.util.Scanner;            // lets us read what the user types on the keyboard

// The Client class is the program the user runs to talk to the server
public class Client {
    // HOST is the address of the server we want to connect to
    // "127.0.0.1" means "this same computer" (called localhost)
    private static final String HOST = "127.0.0.1";

    // PORT is like a door number — the server is listening on port 5000
    // Both client and server must use the same port number
    private static final int PORT = 5000;

    // main() is where the program starts running
    public static void main(String[] args) {
        // "try-with-resources" automatically closes everything listed here when we're done
        try (
                // Open a network connection (socket) to the server
                Socket socket = new Socket(HOST, PORT);
                // Set up a reader so we can receive text messages from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Set up a writer so we can send text messages to the server
                // The second argument "true" means it sends the message right away (auto-flush)
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // Set up a scanner so we can read what the user types in the terminal
                Scanner scanner = new Scanner(System.in)
        ) {
            // Tell the user they successfully connected
            System.out.println("Connected to server at " + HOST + ":" + PORT);
            // Print the first message the server sends us (usually a welcome message)
            System.out.println(in.readLine());

            // Keep looping forever until we decide to break out
            while (true) {
                // Ask the user to type a command
                System.out.print("Enter command: ");
                // Read the full line the user typed and store it in "command"
                String command = scanner.nextLine();

                // Send the user's command to the server
                out.println(command);

                // Wait for the server to send back a response and store it
                String response = in.readLine();
                // If response is null, the server closed the connection
                if (response == null) {
                    System.out.println("Server disconnected.");
                    break; // exit the loop
                }

                // Print the server's response on the screen
                System.out.println(response);

                // Some responses have multiple lines (like stats) — print all remaining lines
                while (in.ready()) {
                    System.out.println(in.readLine());
                }

                // If the user typed "Exit" (case-insensitive), stop the loop and quit
                if (command.equalsIgnoreCase("Exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            // If anything goes wrong with the network, print an error message
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
