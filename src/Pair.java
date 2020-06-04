/**
 * This is a subclass of the Hand class, and are used to model a hand of pair
 * 
 * @author Chung Hok Kan
 */
public class Pair extends Hand {
	/**
	 * Builds Pair with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * Checks if this hand beats a specified hand
	 * @param hand 
	 * 			the hand pending for checking
	 * @return a boolean value of this hand whether it can beat the specified hand, true indicates can beat
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Pair")
			if (getTopCard().compareTo(hand.getTopCard()) > 0)
				return true;
		return false;
	}
	/**
	 * Checks if this is a valid Pair
	 * @return a boolean value, true means it is a valid Pair
	 */
	public boolean isValid() {
		return size() == 2 && getCard(0).getRank() == getCard(1).getRank();
	}
	/**
	 * Returns a string value specifying Pair
	 * @return a string value Pair
	 */
	public String getType() {
		return "Pair";
	}
}
