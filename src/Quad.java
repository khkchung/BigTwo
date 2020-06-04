/**
 * This is a subclass of the Hand class, and are used to model a hand of quad
 * 
 * @author Chung Hok Kan
 */
public class Quad extends Hand {
	/**
	 * Builds Quad with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * Retrieves the top card of Quad
	 * @return a Card object, the top card of this hand
	 */
	public Card getTopCard() {
		sort();
		return getCard(2);
	}
	/**
	 * Checks if this hand beats a specified hand
	 * @param hand 
	 * 			the hand pending for checking
	 * @return a boolean value of this hand whether it can beat the specified hand, true indicates can beat
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse")
			return true;
		if (hand.getType() == "Quad")
			if (getTopCard().compareTo(hand.getTopCard()) > 0)
				return true;
		return false;
	}
	/**
	 * Checks if this is a valid Quad
	 * @return a boolean value, true means it is a valid Quad
	 */
	public boolean isValid() {
		sort();
		if (size() == 5) {
			if (getCard(0).getRank() == getCard(1).getRank() &&
					getCard(0).getRank() == getCard(2).getRank() &&
					getCard(0).getRank() == getCard(3).getRank())
				return true;
			if (getCard(4).getRank() == getCard(1).getSuit() &&
					getCard(4).getRank() == getCard(2).getRank() &&
					getCard(4).getRank() == getCard(3).getRank())
				return true;
		}
		return false;
	}
	/**
	 * Returns a string value specifying Quad
	 * @return a string value Quad
	 */
	public String getType() {
		return "Quad";
	}
}
