package codes;

import java.io.*;
import java.net.*;

public class QuizClient {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        int serverPort = 1234;

        // Load server info from file
        try (BufferedReader fileReader = new BufferedReader(new FileReader("server_info.dat"))) {
            serverIP = fileReader.readLine();
            serverPort = Integer.parseInt(fileReader.readLine());
        } catch (IOException e) {
            System.out.println("Using default server info: " + serverIP + ":" + serverPort);
        }

        try (Socket socket = new Socket(serverIP, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to Quiz Server.");

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.startsWith(QuizProtocol.QUESTION_PREFIX)) {
                    System.out.println(serverMessage.substring(QuizProtocol.QUESTION_PREFIX.length())); // Display question
                    System.out.print("Your Answer: ");
                    String answer = console.readLine();
                    out.println(QuizProtocol.ANSWER_PREFIX + answer);
                } else if (serverMessage.startsWith(QuizProtocol.FEEDBACK_PREFIX)) {
                    System.out.println("Server: " + serverMessage.substring(QuizProtocol.FEEDBACK_PREFIX.length()));
                } else if (serverMessage.startsWith(QuizProtocol.FINAL_SCORE_PREFIX)) {
                    System.out.println("Your Final Score: " + serverMessage.substring(QuizProtocol.FINAL_SCORE_PREFIX.length()));
                } else if (serverMessage.equals(QuizProtocol.CLOSE)) {
                    System.out.println("Quiz ended. Goodbye!");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
