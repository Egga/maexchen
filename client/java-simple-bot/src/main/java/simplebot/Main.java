package simplebot;

import udphelper.MessageListener;
import udphelper.MessageSender;
import udphelper.UdpCommunicator;

import java.io.IOException;

public class Main {

	private enum BotTypes {
		simple,
		random,
        clean
	}
	
	public static void main(String[] args) throws IOException {
		String serverHost = args[0];
		int serverPort = Integer.parseInt(args[1]);
		String clientName = args[2];
		BotTypes botType = BotTypes.simple;
		if (args.length > 3) {
			botType = BotTypes.valueOf(args[3]);
		}
		
		UdpCommunicator communicator = new UdpCommunicator(serverHost, serverPort);
		MessageListener bot = createBot(botType, clientName, communicator.getMessageSender());
		communicator.addMessageListener(bot);
		communicator.listenForMessages();
	}

	private static MessageListener createBot(BotTypes botType, String clientName, MessageSender messageSender) {
		if (botType == BotTypes.random) {
			return new RandomBot(clientName, messageSender);
		} else if (botType == BotTypes.clean) {
            return new CleanBot(clientName, messageSender);
        }
		return new SimpleBot(clientName, messageSender);
	}

}
