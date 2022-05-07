package Deck;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

public class Deck<T extends Card> implements Iterable<T> {
	///////////// Globals //////////////
	protected ArrayList<T> deck; // a list of Card objects in the deck

	///////////// Constructors /////////////
	/**
	 * Creates an empty deck with no cards in it
	 */
	public Deck() {
		deck = new ArrayList<T>();
	}

	/**
	 * Creates a deck of cards with the given cards
	 *
	 * @param cards The cards that will be in the deck
	 */
	public Deck(ArrayList<T> cards) {
		this.deck = cards;
	}

	public Deck(Deck<T> deck){
		for(var c: deck){
			addCard(c);
		}
	}

	public Deck(DeckWrapper<T> deck){
		for(var c: deck){
			addCard(c);
		}
	}

	//////// Functions /////////

	/**
	 * Shuffles the cards in the deck that have not been dealt
	 */
	public void shuffle() {
		Collections.shuffle(deck);
	}

	/**
	 * Deals a card from the top of the deck
	 *
	 * @return The Card on top of the deck. If no cards to deal, returns None
	 */
	public T deal() {
		return deal(0);
	}

	/**
	 * Deals a card from the given spot in the deck
	 *
	 * @param place The place in the deck to deal from. If more than the number of cards, returns null
	 * @return The Card at the given place
	 */
	public T deal(int place) {
		try {
			return deck.remove(place);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Deals a card from the bottom of the deck
	 *
	 * @return The Card on the bottom of the deck
	 */
	public T dealBottom() {
		return deck.remove(deck.size()-1);
	}

	/**
	 * Deals and return multiple cards from the top of the deck
	 * @param numCards the number of cards to deal
	 * @return an ArrayList of the dealt cards
	 */
	public ArrayList<T> dealMultiple(int numCards){
		return dealMultiple(numCards, 0);
	}

	/**
	 * Deals and returns multiple cards from a place in the deck
	 * @param numCards the number of cards to deal
	 * @param place the place in the deck to deal from
	 * @return an ArrayList of the dealt cards
	 */
	public ArrayList<T> dealMultiple(int numCards, int place){
		var cards = new ArrayList<T>();
		for(int i = 0; i < numCards; i++){
			cards.add(deal(place));
		}
		return cards;
	}

	/**
	 * Deals all cards in the given deck from the deck
	 * @param otherdeck the deck to remove from the deck
	 * @return a boolean indicating if the operation succeeded
	 */
	public boolean dealMultiple(ArrayList<T> otherdeck){
		for(var c: otherdeck){
			deal(find(c));
		}
		return true;
	}

	/**
	 * Finds the first instance of the given card in the deck. If it isn't in the deck, returns -1
	 *
	 * @param card The card to be found
	 * @return The position of the card in the deck
	 */
	public int find(T card) {
		return deck.indexOf(card);
	}

	/**
	 * Finds the first card with the given value in the deck. If it isn't in the deck, returns -1
	 *
	 * @param value The value of the card to be found
	 * @return The position of the first card in the deck with that value
	 */
	public int find(String value) {
		for (int i = 0; i < deck.size(); i++) {
			if (deck.get(i).getStringValue().equals(value))
				return i;
		}
		return -1;
	}

	/**
	 * Returns the value of the card at that place in the deck without moving or removing it
	 * @param place The index of the card to look at
	 * @return The card or null if the index was invalid
	 */
	public T peek(int place){
		try {
			return deck.get(place);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Replaces the card at the given place with a new card
	 * @param place The index of the card to be replaced
	 * @param card The card to insert into the deck
	 * @return the card that was replaced
	 */
	public T replace(int place, T card){
		try {
			return deck.set(place, card);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Adds the cards from the given deck at the given place
	 *
	 * @param otherDeck the deck with the cards to be added
	 * @param place     the place in the deck where the cards will be added
	 * @return whether or not the operation succeeded
	 */
	public final boolean addDeck(Deck<T> otherDeck, int place) {
		return addCards(otherDeck.getDeck(), place);
	}

	/**
	 * Adds the given cards to the bottom of the deck
	 * @param cards The cards to be added
	 * @return whether or not the operation succeeded
	 */
	public final boolean addCards(ArrayList<T> cards) {
		return addCards(cards, getNumCards());
	}

	/**
	 * Adds the given cards to the deck at the given place
	 *
	 * @param cards The cards to be added
	 * @param place The place in the deck where the cards will be added
	 * @return whether or not the operation succeeded
	 */
	public final boolean addCards(ArrayList<T> cards, int place) {
		try {
			for(var c: cards){
				addCard(c, place);
				place++;
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Adds the given new card to the bottom of the deck
	 *
	 * @param card The card object to be added
	 * @return a boolean indicating whether the operation succeeded
	 */
	public boolean addCard(T card) {
		return addCard(card, deck.size());
	}

	/**
	 * Inserts the given new card into the deck at the given place
	 *
	 * @param card  The card object to be added
	 * @param place The place in the deck to add the card
	 * @return a boolean indicating whether the operation succeeded
	 */
	public boolean addCard(T card, int place) {
		try {
			deck.add(place, card);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Removes all cards from the deck
	 */
	public void empty(){
		deck.clear();
	}

	/**
	 * @return The entire deck in the form of a list of cards
	 */
	public ArrayList<T> getDeck() {
		return deck;
	}

	/**
	 * @return Returns the number of cards in the deck
	 */
	public int getNumCards() {
		return deck.size();
	}

	/**
	 * @return The entire deck in the form of a list of the string values of the cards
	 */
	public String[] getDeckValues() {
		String[] values = new String[deck.size()];
		for (int i = 0; i < deck.size(); i++) {
			values[i] = deck.get(i).toString();
		}
		return values;
	}

	@Override
	public String toString() {
		return Arrays.toString(getDeckValues());
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator iterator() {
		return deck.iterator();
	}
}