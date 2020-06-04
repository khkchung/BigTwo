/**
 * This is a subclass of the Hand class, and are used to model a hand of triple
 * @author Chung Hok Kan
 */
public class Triple extends Hand {
	/**
	 * Builds Triple with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * Checks if this hand beats a specified hand
	 * @param hand 
	 * 			the hand pending for checking
	 * @return a boolean value of this hand whether it can beat the specified hand, true indicates can beat
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Triple")
			if (getTopCard().compareTo(hand.getTopCard()) > 0)
				return true;
		return false;
	}
	/**
	 * Checks if this is a valid Triple
	 * @return a boolean value, true means it is a valid Triple
	 */
	public boolean isValid() {
		return size() == 3 && getCard(0).getRank() == getCard(1).getRank() && getCard(0).getRank() == getCard(2).getRank();
	}
	/**
	 * Returns a string value specifying Triple
	 * @return a string value Triple
	 */
	public String getType() {
		return "Triple";
	}
}
