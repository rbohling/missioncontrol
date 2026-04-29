import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final String HOST = "127.0.0.1";

    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Server is running on " + HOST + ":" + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;
        private final Session session;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.session = new Session();
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println("Connected to server. Enter commands.");

                String line;
                while ((line = in.readLine()) != null) {
                    String response = processCommand(line.trim());
                    out.println(response);

                    if (line.equalsIgnoreCase("Exit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Client communication error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                System.out.println("Client disconnected: " + socket.getInetAddress());
            }
        }

        private String processCommand(String input) {
            if (input.isEmpty()) {
                return "Invalid command";
            }

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "dataset":
                    return handleDataset(parts);
                case "sort":
                    return handleSort(parts);
                case "search":
                    return handleSearch(parts);
                case "stat":
                    return handleStat(parts);
                case "reset":
                    return handleReset(parts);
                case "exit":
                    return "Disconnecting.";
                default:
                    return "Invalid command";
            }
        }

        private String handleDataset(String[] parts) {
            if (parts.length < 2) {
                return "Dataset command requires at least one integer";
            }

            List<Integer> values = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                try {
                    values.add(Integer.parseInt(parts[i]));
                } catch (NumberFormatException e) {
                    return "Dataset must contain only integers";
                }
            }

            session.setDataset(values);
            return "Dataset received.";
        }

        private String handleSort(String[] parts) {
            if (parts.length != 1) {
                return "Invalid command";
            }

            if (!session.hasDataset()) {
                return "No dataset loaded. Please send a Dataset command first.";
            }

            List<Integer> sortedData = Processor.bubbleSort(session.getDataset());
            session.setDataset(sortedData);
            session.setSorted(true);
            return Processor.formatList(sortedData);
        }

        private String handleSearch(String[] parts) {
            if (parts.length != 2) {
                return "Search command requires exactly one integer value";
            }

            if (!session.hasDataset()) {
                return "No dataset loaded. Please send a Dataset command first.";
            }

            int target;
            try {
                target = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return "Search command requires exactly one integer value";
            }

            int index;
            if (session.isSorted()) {
                index = Processor.binarySearch(session.getDataset(), target);
                return index >= 0
                        ? "Found at index " + index + " using Binary Search"
                        : "Item not found";
            } else {
                index = Processor.linearSearch(session.getDataset(), target);
                return index >= 0
                        ? "Found at index " + index + " using Linear Search"
                        : "Item not found";
            }
        }

        private String handleStat(String[] parts) {
            if (parts.length != 1) {
                return "Invalid command";
            }

            if (!session.hasDataset()) {
                return "No dataset loaded. Please send a Dataset command first.";
            }

            return Processor.calculateStats(session.getDataset());
        }

        private String handleReset(String[] parts) {
            if (parts.length != 1) {
                return "Invalid command";
            }

            session.clear();
            return "Dataset cleared.";
        }
    }
}
