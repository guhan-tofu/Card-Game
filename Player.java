import java.util.ArrayList;
import java.util.List;



public class Player implements Runnable {
    private static int idCounter = 0;
    private final int id;
    private final Deck leftDeck;
    private final Deck rightDeck;
    private final Hand hand = new Hand();

    public Player(Deck leftDeck, Deck rightDeck) {
        this.id = idCounter++;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
    }

    public Deck getLeftDeck(){
        return this.leftDeck;
    }

    public Deck getRightDeck(){
        return this.rightDeck;
    }

    @Override
    public void run() {
        // Game logic for the player, e.g., drawing and discarding cards
        while (!hand.isWinningHand()) {
            drawCard();
            discardCard();
            // Additional game logic here
        }
        System.out.println("Player " + (id+1) + " has won!");
    }

    private void drawCard() {
        Card cardToDraw = leftDeck.drawCard();
        hand.addCard(cardToDraw);
    }

    private void discardCard() {
        Card cardToDiscard = hand.discardCard();
        rightDeck.addCard(cardToDiscard);
    }

    private class Hand {
        private List<Card> cards = new ArrayList<>();

        public void addCard(Card card) {//id of card instead
            cards.add(card);
        }

        public Card discardCard() {
            // Implement discard logic, e.g., selecting a card to discard
            return cards.remove(0); // Example of discarding the first card
        }

        public boolean isWinningHand() {
            // Logic to check if the hand is winning
            return false; // Placeholder for actual winning condition
        }
    }
}
