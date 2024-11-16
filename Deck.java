import java.util.concurrent.ConcurrentLinkedQueue;

public class Deck {
    private ConcurrentLinkedQueue<Card> cards = new ConcurrentLinkedQueue<>();

    public void addCard(Card card) {
        cards.add(card);  // No need to synchronize
    }

    public Card drawCard() {
        return cards.poll();  // No need to synchronize
    }

    public int getSize() {
        return cards.size();  // No need to synchronize
    }

    public void showCards() {
        for (Card card : cards) {
            if (card != null) {
                System.out.println("card id: " + card.getId() + " card value: " + card.getValue());
            }
        }
    }
}