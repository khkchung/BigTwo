/**
 * This class is a subclass of the CardList class, and is used to 
 * model a hand of cards. It has a private instance variable for 
 * storing the player who plays this hand.
 * @author Chung Hok Kan
 *
 */
public abstract class Hand extends CardList {
	/**
	 * Builds a hand with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i=0; i<cards.size(); i++)
			addCard(cards.getCard(i));
	}
	
	private CardGamePlayer player;
	
	/**
	 * Retrieves the player of this hand
	 * @return a CardGamePlayer object, the player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * Retrieves the top card of this hand
	 * @return a Card object, the top card of this hand
	 */
	public Card getTopCard() {
		sort();
		return getCard(size()-1);
	}
	
	/**
	 * Checks if this hand beats a specified hand
	 * @param hand 
	 * 			the hand pending for checking
	 * @return a boolean value of this hand whether it can beat the specified hand, true indicates can beat
	 */
	public boolean beats(Hand hand) {
		return false;
	};
	
	/**
	 * Checks if this is a valid hand
	 * @return a boolean value, true means the hand is valid
	 */
	abstract public boolean isValid();
	/**
	 * Returns a string value specifying the type of this hand
	 * @return a string value specifying the type of this hand
	 */
	abstract public String getType();
}
