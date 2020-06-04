/**
 * This is a subclass of the Hand class, and are used to model a hand of straight
 * 
 * @author Chung Hok Kan 
 */
public class Straight extends Hand {
	/**
	 * Builds Straight with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public Straight(CardGamePlayer player, CardList cards) {
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
			if (getTopCard().compareTo(hand.getTopCard()) > 0)
				return true;
		return false;
	}
	/**
	 * Checks if this is a valid Straight
	 * @return a boolean value, true means it is a valid Straight
	 */
	public boolean isValid() {
		sort();
		if (size() == 5) {
			for (int i=0; i<4; i++)
				if ((getCard(i).getRank() + 11) % 13 + 1 != (getCard(i+1).getRank() + 11) % 13)
					return false;
			return true;
		}
		return false;
	}
	/**
	 * Returns a string value specifying Straight
	 * @return a string value Straight
	 */
	public String getType() {
		return "Straight";
	}
}
