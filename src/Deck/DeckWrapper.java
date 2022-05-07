package Deck;

import java.util.Iterator;

public class DeckWrapper<T extends Card> implements Iterable<T> {
    private Deck<T> deck;

    public DeckWrapper(Deck<T> deck){
        this.deck = deck;
    }

    /**
     * @return the number of cards in the deck
     */
    public int getNumCards() {
        return deck.getNumCards();
    }

    /**
     * Gets the card at a given index. Returns null if out of bounds
     * @param index the index of the desired card
     * @return the card at the given index in the deck
     */
    public T getCard(int index){
        try {
            return deck.peek(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return deck.getDeck().iterator();
    }

    @Override
    public String toString(){
        return deck.toString();
    }
}
