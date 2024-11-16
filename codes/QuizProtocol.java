package codes;

public class QuizProtocol {
    // Client to Server
    public static final String START = "START";
    public static final String ANSWER_PREFIX = "ANSWER:";
    public static final String QUIT = "QUIT";

    // Server to Client
    public static final String QUESTION_PREFIX = "QUESTION:";
    public static final String FEEDBACK_PREFIX = "FEEDBACK:";
    public static final String FINAL_SCORE_PREFIX = "FINAL_SCORE:";
    public static final String CLOSE = "CLOSE";

    // Utility methods
    public static String createQuestionMessage(String question) {
        return QUESTION_PREFIX + question;
    }

    public static String createFeedbackMessage(boolean correct) {
        return FEEDBACK_PREFIX + (correct ? "CORRECT" : "INCORRECT");
    }

    public static String createFinalScoreMessage(int score) {
        return FINAL_SCORE_PREFIX + score;
    }

    public static boolean isAnswerMessage(String message) {
        return message.startsWith(ANSWER_PREFIX);
    }

    public static String extractAnswer(String message) {
        return message.substring(ANSWER_PREFIX.length()).trim();
    }
}
