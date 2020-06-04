import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;

/**
 * BigTwoClient is used to model a Big Two card game that supports 4 players over the Internet.
 * 
 * @author Chung Hok Kan
 */
public class BigTwoClient implements CardGame, NetworkGame {

	/**
	 * Creates 4 players and add them to the list of players and a Big Two table which builds the GUI for the game and handles user actions
	 */
	public BigTwoClient() {
		for (int i=0; i<4; i++) {
			CardGamePlayer player = new CardGamePlayer();
			playerList.add(player);
		}
		table = new BigTwoTable(this);
		table.disable();
		table.repaint();
		playerName = (String) JOptionPane.showInputDialog("Please enter your name: ");
		if (playerName == null || playerName.trim().isEmpty() == true)
			playerName = "Noname";
		makeConnection();
		table.repaint();
	}
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>();;
	private ArrayList<Hand> handsOnTable = new ArrayList<Hand>();
	private int playerID = -1;
	private String playerName;
	private String serverIP = "127.0.0.1";
	private int serverPort = 2396;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx = -1;
	private BigTwoTable table;
	private boolean firstTurn = true;
	
	/**
	 * Gets the number of players
	 * 
	 * @return the number of players in the game.
	 */
	public int getNumOfPlayers() {
		return playerList.size();
	}
	
	/**
	 * Retrieves the deck of cards being used
	 * 
	 * @return the deck of cards being used
	 */
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * Retrieves the list of players
	 * 
	 * @return the list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}
	
	/**
	 * Retrieves the list of hands played on the table
	 * 
	 * @return the list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}
	
	/**
	 * Retrieves the index of the current player
	 * 
	 * @return the index of the current player
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}
	
	/**
	 * Starts the game with a (shuffled) deck of cards and implements the 
	 * Big Two game logics
	 * 
	 * @param deck the deck of card being used
	 */
	public void start(Deck deck) {
		this.deck = deck;
		handsOnTable.clear();
		firstTurn = true;
		for (int i=0; i<4; i++)
			playerList.get(i).removeAllCards();
		
		for (int i=0; i<4; i++)
			for (int j=0; j<13; j++) {
				playerList.get(i).addCard(this.deck.getCard(0));
				this.deck.removeCard(0);
			}
		
		for (int i=0; i<4; i++)
			playerList.get(i).sortCardsInHand();
		
		BigTwoCard threeOfDiamond = new BigTwoCard(0, 2);
		
		for (int i=0; i<4; i++)
			if (playerList.get(i).getCardsInHand().getCard(0).equals(threeOfDiamond))
				currentIdx = i;
		
		table.setActivePlayer(playerID);
		
		if (currentIdx == this.playerID)
			table.enable();
		else
			table.disable();
		table.repaint();
		
		table.printMsg(playerList.get(currentIdx).getName() + "'s turn:\n" );
	}
	
	/**
	 * Makes a move by a player with the specified playerID using the cards specified by the list of indices
	 * @param playerID the ID of the current player
	 * @param cardIdx the array of the card indexes the current player wants to select
	 */		
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
	}
	
	/**
	 * Checks a move by a player with the specified playerID using the cards specified by the list of indices
	 * @param playerID the ID of the current player
	 * @param cardIdx the array of the card indexes the current player wants to select
	 */	
	public void checkMove(int playerID, int[] cardIdx) {

		BigTwoCard threeOfDiamond = new BigTwoCard(0, 2);
		
		int[] selectedIdx = null;
		CardList selectedCards = new CardList();
		selectedCards.removeAllCards();
		Hand handPlayed = null;
		boolean legal = false;

		selectedIdx = cardIdx;
		if (firstTurn) {
			if (selectedIdx != null) {
				selectedCards = playerList.get(playerID).play(selectedIdx);
				if (selectedCards.contains(threeOfDiamond)) {
					handPlayed = composeHand(playerList.get(playerID), selectedCards);
					if (handPlayed != null) {
						legal = true;
						firstTurn = false;
					}
				}
			}
		}
		else {
			if (selectedIdx != null) {
				
				selectedCards = playerList.get(playerID).play(selectedIdx);
				handPlayed = composeHand(playerList.get(playerID), selectedCards);
				if (handPlayed != null)
					if (handPlayed.beats(handsOnTable.get(handsOnTable.size()-1)))
						legal = true;
					else if (handsOnTable.get(handsOnTable.size()-1).getPlayer() == playerList.get(playerID))
						legal = true;
			}
			else if (handsOnTable.get(handsOnTable.size()-1).getPlayer() != playerList.get(playerID))
				legal = true;
		}

		if (!legal)
			if (selectedIdx != null)
				table.printMsg(selectedCards.toString() + " <====== Not a legal move!!!\n");
			else
				table.printMsg("{Pass} <====== Not a legal move!!!\n");
		else {
			if (handPlayed != null) {
				playerList.get(playerID).removeCards(selectedCards);
				handsOnTable.add(handPlayed);
				table.printMsg("{" + handPlayed.getType() + "} " + handPlayed.toString() + "\n");
			}
			else 
				table.printMsg("{Pass}\n");
			
			currentIdx = (currentIdx + 1) % 4;
			if (currentIdx == this.playerID)
				table.enable();
			else
				table.disable();
		}
		
		if (endOfGame()) {
			table.disable();
			table.printMsg(playerList.get((currentIdx+3)%4).getName() + " wins the game!!\n");
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			currentIdx = -1;
		}
		else
			table.printMsg(playerList.get(currentIdx).getName() + "'s turn:\n" );
	}
	
	/**
	 * Checks if the game ends
	 * 
	 * @return end of game status, true indicates end of game is reached	
	 */	
	public boolean endOfGame() {
		return playerList.get((currentIdx+3)%4).getNumOfCards() == 0;
	}
	
	/**
	 * Checks if connection is established
	 * 
	 * @return whether connection is established
	 */	
	public boolean connected() {
		return playerID != -1;
	}
	
	/**
	 * Checks if game is in progress
	 * 
	 * @return whether game is in progress
	 */	
	public boolean gameInProgress() {
		return currentIdx != -1;
	}
	
	/**
	 * A method for getting the playerID i.e. the index of the local player.
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * A method for setting the playerID.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * A method for getting the name of the local player.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * A method for setting the name of the local player.
	 */
	public void setPlayerName(String playerName) {
		playerList.get(playerID).setName(playerName);
	}

	/**
	 * A method for getting the IP address of the game server.
	 */
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * A method for setting the IP address of the game server.
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * A method for getting the TCP port of the game server.
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * A method for setting the TCP port of the game server.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * A method for making a socket connection with the game server.
	 */
	public void makeConnection() {
		try {
			sock = new Socket(serverIP, serverPort);
		} catch (Exception ex) { ex.printStackTrace(); }
		
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException ex) { ex.printStackTrace(); }
		
		Thread t = new Thread(new ServerHandler());
		t.start();
		
		sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
		sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
	}

	/**
	 * A method for parsing the messages received from the game server.
	 */
	public void parseMessage(GameMessage message) {
		switch (message.getType()) {
		case CardGameMessage.PLAYER_LIST:
			playerID = message.getPlayerID();
			
			String[] names = (String[]) message.getData();
			for (int i=0; i<playerList.size(); i++) {
				//System.out.println(names[i]);
				if (names[i] != null)
					playerList.get(i).setName(names[i]);
			}
			
			table.repaint();
			break;
			
		case CardGameMessage.JOIN:
			String name = (String) message.getData();
			playerList.get(message.getPlayerID()).setName(name);
			table.printMsg(name + " joined!\n");
			
			table.repaint();
			break;
			
		case CardGameMessage.FULL:
			playerID = -1;
			table.printMsg("This game is full!\n");
			
			table.repaint();
			break;
		
		case CardGameMessage.QUIT:
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " left!\n");
			playerList.get(message.getPlayerID()).setName("");
			
			if (gameInProgress()) {
				table.disable();
				currentIdx = -1;
				sendMessage((new CardGameMessage(CardGameMessage.READY, -1, null)));
			}

			table.repaint();
			break;
			
		case CardGameMessage.READY:
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready!\n");
			
			table.repaint();
			break;
			
		case CardGameMessage.START:
			start((BigTwoDeck) message.getData());
			table.printMsg("Game start!\n");
			
			table.repaint();
			break;
			
		case CardGameMessage.MOVE:
			checkMove(message.getPlayerID(), (int[]) message.getData());
			
			table.repaint();
			break;
		
		case CardGameMessage.MSG:
			table.printChatMsg((String) message.getData());
			
			table.repaint();
			break;
			
		}
	}
	
	/**
	 * A method for sending the specified message to the game server.
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException ex) { ex.printStackTrace(); }
	}
	
	/**
	 * An runnable for receiving messages from server
	 * @author Chung Hok Kan
	 */
	class ServerHandler implements Runnable {

		public void run() {
			CardGameMessage message = null;
			
			try {
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				while ((message = (CardGameMessage) ois.readObject()) != null) {
					parseMessage(message);
				}

			} catch (Exception ex) { ex.printStackTrace(); }	
		}
		
	}
	
	/**
	 * Returns a valid hand from the specified list of cards of the player
	 * 
	 * @param player the current player
	 * @param cards the list of cards the player intended to play
	 * 
	 * @return a valid hand from the specified list of cards of the player, null indicates there is no valid hand
	 */

	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Single single = new Single(player, cards);
		if (single.isValid())
			return single;
		
		Pair pair = new Pair(player, cards);
		if (pair.isValid())
			return pair;
	
		Triple triple = new Triple(player, cards);
		if (triple.isValid()) 
			return triple;
		
		StraightFlush straightFlush = new StraightFlush(player, cards);
		if (straightFlush.isValid()) 
			return straightFlush;
		
		Straight straight = new Straight(player, cards);
		if (straight.isValid())
			return straight;
		
		Flush flush = new Flush(player, cards);
		if (flush.isValid())
			return flush;
		
		FullHouse fullHouse = new FullHouse(player, cards);
		if (fullHouse.isValid())
			return fullHouse;
		
		Quad quad = new Quad(player, cards);
		if (quad.isValid())
			return quad;
		
		return null;
	}
	
	/**
	 * Creates and shuffles a deck of cards and start the game with the deck of cards
	 *
	 * @param args string arguments input
	 * 			
	 */
	public static void main(String[] args) {
		BigTwoClient bigTwoClient = new BigTwoClient();
	}

}
