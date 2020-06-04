
/**
 * This is a subclass of the Hand class, and are used to model a hand of full house
 * 
 * @author Chung Hok Kan
 */
public class FullHouse extends Hand {
	/**
	 * Builds FullHouse with the specified player and list of cards
	 * @param player
	 * 			the specified player
	 * @param cards
	 * 			the cards the specified player wants to play
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Retrieves the top card of FullHouse
	 * @return a Card object, the top card of this hand
	 */
	public Card getTopCard() {
		sort();
		return getCard(2);
	}
	
	/** Checks if this hand beats a specified hand
	 * @param hand 
	 * 			the hand pending for checking
	 * @return a boolean value of this hand whether it can beat the specified hand, true indicates can beat
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Straight" || hand.getType() == "Flush")
			return true;
		if (hand.getType() == "FullHouse")
			if (getTopCard().compareTo(hand.getTopCard()) > 0)
				return true;
		return false;
	}
	/**
	 * Checks if this is a valid FullHouse
	 * @return a boolean value, true means it is a valid FullHouse
	 */
	public boolean isValid() {
		sort();
		if (size() == 5) {
			if (getCard(0).getRank() == getCard(1).getRank() && getCard(3).getRank() == getCard(4).getRank() && 
					(getCard(2).getRank() == getCard(0).getRank() || getCard(2).getRank() == getCard(4).getRank()))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns a string value specifying FullHouse
	 * @return a string value FullHouse
	 */
	public String getType() {
		return "FullHouse";
	}
}
