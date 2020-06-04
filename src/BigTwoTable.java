import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * This class implements the CardGameTable interface. It is used to build a GUI for
 * the Big Two card game and handle all user actions
 * 
 * @author Chung Hok Kan
 */
public class BigTwoTable implements CardGameTable {
	/**
	 * A constructor for creating a BigTwoTable.
	 * @param game
	 * 			a reference to a card game associates with this table
	 */
	public BigTwoTable(BigTwoClient game) {
		this.game = game;
		selected = new boolean[13];
		Arrays.fill(selected, false);
		
		char[] suits = { 'd', 'c', 'h', 's'};
		char[] ranks = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' };
		cardImages = new Image[4][13];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 13; j++){
				String path = "img/cards/"+ ranks[j] + suits[i] + ".gif";
				cardImages[i][j] = new ImageIcon(path).getImage();
			}
		cardBackImage = new ImageIcon("img/cards/b.png").getImage();
		
		avatars = new Image[4];
		avatars[0] = new ImageIcon("img/avatars/Batman-128.png").getImage();
		avatars[1] = new ImageIcon("img/avatars/Captain_America-128.png").getImage();
		avatars[2] = new ImageIcon("img/avatars/Flash-128.png").getImage();
		avatars[3] = new ImageIcon("img/avatars/Ironman-128.png").getImage();
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		
		bigTwoPanel = new JPanel();
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));
		for (int i=0; i<game.getNumOfPlayers(); i++)
			bigTwoPanel.add(new BigTwoPanel(i));
		bigTwoPanel.add(new BigTwoPanel(-1));
		leftPanel.add(BorderLayout.CENTER, bigTwoPanel);
		
		JPanel buttonPanel = new JPanel();
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		buttonPanel.setPreferredSize(new Dimension(512, 50));
		leftPanel.add(BorderLayout.SOUTH, buttonPanel);
		
		leftPanel.setPreferredSize(new Dimension(512, 1024));
		frame.add(BorderLayout.CENTER, leftPanel);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		msgArea = new JTextArea();
		clearMsgArea();
		msgArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret)msgArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setPreferredSize(new Dimension(512, 512));
		rightPanel.add(scrollPane);
		
		chatArea = new JTextArea();
		clearChatArea();
		chatArea.setEditable(false);
		DefaultCaret caret2 = (DefaultCaret)chatArea.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane2 = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane2.setPreferredSize(new Dimension(512, 512));
		rightPanel.add(scrollPane2);
		
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BorderLayout());
		
		JLabel label = new JLabel("Message: ");
		typePanel.add(BorderLayout.WEST, label);
		
		field = new JTextField();
		field.setPreferredSize(new Dimension(300, 10));
		field.addActionListener(new TextFieldListener());
		typePanel.add(BorderLayout.CENTER, field);
		
		typePanel.setPreferredSize(new Dimension(512, 50));
		rightPanel.add(typePanel);
		
		rightPanel.setPreferredSize(new Dimension(512, 1024));
		frame.add(BorderLayout.EAST, rightPanel);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Game");
		JMenuItem restartItem = new JMenuItem("Connect");
		JMenuItem quitItem = new JMenuItem("Quit");
		restartItem.addActionListener(new RestartMenuItemListener());
		quitItem.addActionListener(new QuitMenuItemListener());
		menu.add(restartItem);
		menu.add(quitItem);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		frame.setSize(1024, 1024);
		frame.setVisible(true);
	}
	
	private BigTwoClient game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea chatArea;
	private JTextField field;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;

	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}
	
	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected() {

		int[] cardIdx = null;
		int count = 0;
		for (int i = 0; i < selected.length; i++)
			if (selected[i])
				count++;

		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int i = 0; i < selected.length; i++)
				if (selected[i]) {
					cardIdx[count] = i;
					count++;
				}
		}
		return cardIdx;
	}
	
	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected() {
		Arrays.fill(selected, false);
	}
	
	/**
	 * Repaints the GUI.
	 */
	public void repaint() {
		bigTwoPanel.repaint();
	}
	
	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	/**
	 * Clears the message area of the card game table.
	 */
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * Prints the specified string to the chat area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the chat area of the card game
	 *            table
	 */
	
	public void printChatMsg(String msg) {
		chatArea.append(msg+"\n");
	}
	
	/**
	 * Clears the chat area of the card game table.
	 */
	public void clearChatArea() {
		chatArea.setText("");
	}
	
	/**
	 * Resets the GUI.
	 */
	public void reset() {
		resetSelected();
		clearMsgArea();
		clearChatArea();
		enable();
	}
	
	/**
	 * Enables user interactions.
	 */
	public void enable() {
		bigTwoPanel.setEnabled(true);
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}
	
	/**
	 * Disables user interactions.
	 */
	public void disable() {
		bigTwoPanel.setEnabled(false);
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}

	/**
	 * an inner class that extends the JPanel class and implements the MouseListener interface.
	 * It draws the card game table and handle mouse click events.
	 *
	 * @author Chung Hok Kan
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		
		static final int CARDX = 73;
		static final int CARDY = 97;
		
		/**
		 * A constructor for creating a BigTwoPanel
		 * @param option the index of the player for creating the panel of that player or -1 for the panel for last played cards
		 */
		public BigTwoPanel(int option) {
			this.addMouseListener(this);
			this.option = option;
		}
		
		private int option;
		
		/**
		 * Overrides the paintComponent() method inherited from the JPanel class to draw the card game table
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.green.darker().darker());
			this.setBorder(BorderFactory.createLineBorder(Color.black));
			
			int xpos = 20;
			int ypos = 20;
			
			if (option >= 0 && option < game.getNumOfPlayers()) {
				if (activePlayer == option)
					g.setColor(Color.blue);
				g.drawString(game.getPlayerList().get(option).getName(), xpos, ypos);
				g.drawImage(avatars[option], xpos, ypos, this);
				xpos = 160;
				ypos = 40;

				if (activePlayer == option)
					for (int i=0; i<game.getPlayerList().get(option).getNumOfCards(); i++) {
						int suit = game.getPlayerList().get(option).getCardsInHand().getCard(i).getSuit();
						int rank = game.getPlayerList().get(option).getCardsInHand().getCard(i).getRank();						
						if (selected[i])
							g.drawImage(cardImages[suit][rank], xpos, ypos-20, this);
						else
							g.drawImage(cardImages[suit][rank], xpos, ypos, this);
						xpos = xpos + 20;
					}
				else
					for (int i=0; i < game.getPlayerList().get(option).getNumOfCards(); i++) {
						g.drawImage(cardBackImage, xpos, ypos, this);
						xpos = xpos + 20;
					}
			}
			else if (option == -1 && game.getHandsOnTable().size() != 0) {
				g.drawString("Played by " + game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName(), xpos, ypos);
				ypos = 40;
				for (int i=0; i<game.getHandsOnTable().get(game.getHandsOnTable().size()-1).size(); i++) {
					int suit = game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getSuit();
					int rank = game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getRank();						
					g.drawImage(cardImages[suit][rank], xpos, ypos, this);
					xpos = xpos + 20;
				}
			}
		}
		
		/**
		 * Implements the mouseClicked() method from the MouseListener interface to handle mouse click events.
		 */
		public void mouseClicked(MouseEvent e) {
			if (activePlayer == option) {
				int ypos = 40;
				int xcoord = e.getX();
				int ycoord = e.getY();
				int noOfCards = game.getPlayerList().get(option).getNumOfCards();
				
				for (int i = noOfCards - 1; i>=0; i--) {
					int yoffset = 0;
					if (selected[i])
						yoffset = 20;
					if (xcoord >= 160 + i * 20 && xcoord <= 160 + i * 20 + CARDX && 
						ycoord >= ypos - yoffset && ycoord <= ypos + CARDY - yoffset) {
							selected[i] = !selected[i];
							break;
					}
				}

				repaint();
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface.
	 * It handles button-click events for the "Play" button and make a corresponding move.
	 * 
	 * @author Chung Hok Kan
	 */
	class PlayButtonListener implements ActionListener {

		/**
		 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Play” button.
		 */
		public void actionPerformed(ActionEvent e) {
			game.makeMove(activePlayer, getSelected());
			resetSelected();
			repaint();
			if (game.endOfGame()) {
				disable();
			}
		}
		
	}
	
	/** 
	 * an inner class that implements the ActionListener interface.
	 * It handles button-click events for the "Pass" button and make a corresponding move.
	 * 
	 * @author Chung Hok Kan
	 */
	class PassButtonListener implements ActionListener {
		
		/**
		 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Pass” button.
		 */
		public void actionPerformed(ActionEvent e) {
			game.makeMove(activePlayer, null);
			resetSelected();
			repaint();
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface.
	 * It handles chat message sending.
	 * 
	 * @author Chung Hok Kan
	 */
	class TextFieldListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			game.sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, e.getActionCommand()));
			field.setText("");
		}

	}
	
	/**
	 * an inner class that implements the ActionListener interface.
	 * It handles menu-item click events for the "Restart" menu item. When it is selected,
	 * it will create a new Big Two deck, shuffle the deck and start the game.
	 *
	 * @author Chung Hok Kan
	 */
	class RestartMenuItemListener implements ActionListener {

		/**
		 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the “Restart” menu item.
		 */
		public void actionPerformed(ActionEvent e) {
			if (!game.connected())
				game.makeConnection();
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface.
	 * It handles menu-item click events for the "Quit" menu item. WHen it is selected,
	 * it will terminate the application.
	 *
	 * @author Chung Hok Kan
	 */
	class QuitMenuItemListener implements ActionListener {

		/**
		 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the “Quit” menu item.
		 */
		public void actionPerformed(ActionEvent e) {
			System.exit(1);
		}
		
	}
}
