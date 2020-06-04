/**
 * This is a subclass of the Hand class, and are used to model a hand of straight flush
 * 
 * @author Chung Hok Kan
 */
public class StraightFlush extends Hand {
	/**
	 * Builds StraightFlush with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * Checks if this hand beats a specified hand
	 * @param hand 
	 * 			the hand pending for checking
	 * @return a boolean value of this hand whether it can beat the specified hand, true indicates can beat
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse" || hand.getType() == "Quad")
			return true;
		if (hand.getType() == "StraightFlush")
			if (getTopCard().compareTo(hand.getTopCard()) > 0)
				return true;
		return false;
	}
	/**
	 * Checks if this is a valid StraightFlush
	 * @return a boolean value, true means it is a valid StraightFlush
	 */
	public boolean isValid() {
		sort();
		if (size() == 5) {
			for (int i=0; i<4; i++)
				if ((getCard(i).getRank() + 11) % 13 + 1 != (getCard(i+1).getRank() + 11) % 13)
					return false;
			for (int i=0; i<4; i++)
				if (getCard(i).getSuit() != getCard(i+1).getSuit())
					return false;
			return true;
		}
		return false;
	}
	/**
	 * Returns a string value specifying StraightFlush
	 * @return a string value StraightFlush
	 */
	public String getType() {
		return "StraightFlush";
	}
}
