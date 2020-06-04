/**
 * This is a subclass of the Hand class, and are used to model a hand of single
 * @author Chung Hok Kan
 *
 */
public class Single extends Hand {
	/**
	 * Builds Single with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * Checks if this hand beats a specified hand
	 * @param hand 
	 * 			the hand pending for checking
	 * @return a boolean value of this hand whether it can beat the specified hand, true indicates can beat
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Single")
			if (getTopCard().compareTo(hand.getTopCard()) > 0)
				return true;
		return false;
	}
	/**
	 * Checks if this is a valid Single
	 * @return a boolean value, true means it is a valid Single
	 */
	public boolean isValid() {
		return size() == 1;
	}
	/**
	 * Returns a string value specifying Straight
	 * @return a string value Straight
	 */
	public String getType() {
		return "Single";
	}
}
