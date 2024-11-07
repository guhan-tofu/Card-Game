import java.util.concurrent.ConcurrentLinkedQueue;

public class Deck {
    // private int id;
    // private static int idCounter =0;
    private ConcurrentLinkedQueue<Card> cards = new ConcurrentLinkedQueue<>();

    public synchronized void addCard(Card card) {
        cards.add(card);
    }

    public synchronized Card drawCard() {
        return cards.poll();
    }

    public synchronized int getSize() {
        return cards.size();
    }

    public synchronized void showCards(){
        for (Card card : cards){
            System.out.println("card id: " +card.getId()+ " card value: " + card.getValue());
        }
    }
}
