package codes;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;

public class QuizServer {
    private static final int PORT = 1234;
    private static final List<String[]> QUESTIONS = Arrays.asList(
        new String[]{"What is the capital of France?", "Paris"},
        new String[]{"1 + 4 * 5?", "21"},
        new String[]{"What is the color of the sky?", "Blue"},
		new String[]{"What is full of holes but still holds water?", "Sponge"},
		new String[]{"5 + 3 * 2 / 6", "6"}
    );

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(5); // Support multiple clients

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Quiz Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                int score = 0;

                for (String[] qa : QUESTIONS) {
                    out.println(QuizProtocol.createQuestionMessage(qa[0])); // Send question
                    String clientResponse = in.readLine();

                    if (clientResponse != null && QuizProtocol.isAnswerMessage(clientResponse)) {
                        String answer = QuizProtocol.extractAnswer(clientResponse);
                        boolean correct = answer.equalsIgnoreCase(qa[1]);
                        out.println(QuizProtocol.createFeedbackMessage(correct));
                        if (correct) {
                            score++;
                        }
                    }
                }

                out.println(QuizProtocol.createFinalScoreMessage(score));
                out.println(QuizProtocol.CLOSE);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
