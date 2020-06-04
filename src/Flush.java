
/**
 * This is a subclass of the Hand class, and are used to model a hand of flush
 *
 * @author Chung Hok Kan
 */
public class Flush extends Hand {
	/**
	 * Builds Flush with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks if this hand beats a specified hand
	 * @param hand 
	 * 			the hand pending for checking
	 * @return a boolean value of this hand whether it can beat the specified hand, true indicates can beat
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Straight")
			return true;
		if (hand.getType() == "Flush")
			if (getTopCard().compareTo(hand.getTopCard()) > 0)
				return true;
		return false;
	}
	
	/**
	 * Checks if this is a valid Flush
	 * @return a boolean value, true means it is a valid Flush
	 */
	public boolean isValid() {
		if (size() == 5) {
			for (int i=0; i<4; i++)
				if (getCard(i).getSuit() != getCard(i+1).getSuit())
					return false;
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a string value specifying Flush
	 * @return a string value Flush
	 */
	public String getType() {
		return "Flush";
	}
}
