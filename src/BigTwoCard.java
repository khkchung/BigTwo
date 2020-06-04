
/**
 * This class is a subclass of the Card class, 
 * and is used to model a card used in a Big Two card game.
 * 
 * @author Chung Hok Kan
 *
 */
public class BigTwoCard extends Card {
	/**
	 * Builds a card with the specified suit and rank
	 * 
	 * @param suit
	 * 			suit of the card, an integer between 0 and 3
	 * @param rank
	 * 			rank of the card, an integer between 0 and 12
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * Compares this card with the specified card for order
	 * @param card
	 * 			the card for comparison
	 * 
	 * @return a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card
	 * 
	 */
	public int compareTo(Card card) {
		if ((rank + 11) % 13 > (card.rank + 11) % 13)
			return 1;
		else if ((rank + 11) % 13 < (card.rank + 11) % 13)
			return -1;
		else if (suit > card.suit)
			return 1;
		else if (suit < card.suit)
			return -1;
		else
			return 0;
	}
}
