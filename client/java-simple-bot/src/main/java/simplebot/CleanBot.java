package simplebot;

import udphelper.MessageListener;
import udphelper.MessageSender;

import java.io.IOException;

import static java.lang.Integer.parseInt;

/**
 * @author egga
 */
public class CleanBot implements MessageListener {

    private final MessageSender messageSender;

    private String currentTurnId;

    private Integer lastAnnouncement;

    public CleanBot(String name, MessageSender messageSender) {
        this.messageSender = messageSender;
        tryToSend("REGISTER;" + name);
    }

    public void onMessage(String message) {
        //System.out.println(message);
        String[] parts = message.split(";");
        if (parts[0].equals("ROUND STARTING")) {
            tryToSend("JOIN;" + parts[1]);
        } else if (parts[0].equals("YOUR TURN")) {
            tryToSend("ROLL;" + parts[1]);
        } else if (parts[0].equals("ANNOUNCED")) {
            lastAnnouncement = parseResult(parts[2]);
        } else if (parts[0].equals("ROLLED")) {
            currentTurnId = parts[2];
            tellTruthOrLie(parseResult(parts[1]));
        }
    }

    private void tellTruthOrLie(final Integer ownDice) {
        System.out.println("let's see " + ownDice);
        // TODO implement bluffing
        announceResult(ownDice);

    }

    private void announceResult(final Integer result) {
        Integer x = result / 10;
        Integer y = result % 10;
        String answer = x +","+ y;
        System.out.println("antwort: " + answer);
        tryToSend("ANNOUNCE;" + answer + ";" + currentTurnId);
    }

    private void tryToSend(String message) {
        try {
            messageSender.send(message);
        } catch (IOException e) {
            System.err.println("Failed to send " + message + ": " + e.getMessage());
        }
    }

    private Integer parseResult(final String result) {
        String[] dice = result.split(",");

        Integer x = parseInt(dice[0]);
        Integer y = parseInt(dice[1]);

        Integer value;

        // TODO same and mia
        if (x > y) {
            value = 10 * x + y;

        } else {
            value = 10 * y + x;
        }

        return value;
    }
}
