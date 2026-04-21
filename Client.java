import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(HOST, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to server at " + HOST + ":" + PORT);
            System.out.println(in.readLine());

            while (true) {
                System.out.print("Enter command: ");
                String command = scanner.nextLine();

                out.println(command);

                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server disconnected.");
                    break;
                }

                System.out.println(response);

                while (in.ready()) {
                    System.out.println(in.readLine());
                }

                if (command.equalsIgnoreCase("Exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
