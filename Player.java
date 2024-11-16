import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Player extends Thread {
    private static int idCounter = 0;
    private final int id;
    private final Deck leftDeck;
    private final Deck rightDeck;
    private final Hand hand = new Hand();
    private static volatile boolean gameOver = false;

    public Player(Deck leftDeck, Deck rightDeck) {
        this.id = idCounter++;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
    }

    public synchronized Deck getLeftDeck(){
        return this.leftDeck;
    }

    public synchronized Deck getRightDeck(){
        return this.rightDeck;
    }

    
    @Override
    public void run() {
        // Game logic for the player, e.g., drawing and discarding cards
        while (!hand.isWinningHand() && !gameOver) {
            // while(leftDeck.getSize() ==4){
            
            //     doBoth();
            // }
            doBoth();
            
            // Additional game logic here
        }

        if (hand.isWinningHand()) {
            synchronized (Player.class) {  // Ensure that only one thread can set gameOver
                if (!gameOver) {  // If the game is not already over, set it to true
                    gameOver = true;
                    System.out.println("Player " + (id + 1) + " has won!");
                      // Set gameOver to true to stop other threads
                }
            }
        }
    }

    public synchronized void doBoth(){
        Card cardToDraw = leftDeck.drawCard();
        hand.addCard(cardToDraw);
        Card cardToDiscard = hand.discardCard();
        rightDeck.addCard(cardToDiscard);
    }

    public synchronized void drawCard() {
        Card cardToDraw = leftDeck.drawCard();
        hand.addCard(cardToDraw);
    }

    public synchronized void discardCard() {
        Card cardToDiscard = hand.discardCard();
        rightDeck.addCard(cardToDiscard);
    }

    public synchronized void addCardToHand(Card card){
        hand.addCard(card);
    }

    public synchronized void showCardsInHand() {
        hand.showCardsInHand(); // Delegate to Hand's method
    }



    private class Hand {
        private List<Card> cards = new ArrayList<>();

        public synchronized void addCard(Card card) {//id of card instead
            cards.add(card);
        }

        public synchronized Card discardCard() {
            if (cards.isEmpty()) {
                throw new IllegalStateException("No cards to discard.");
            }
        
            // Step 1: Count occurrences of card values
            Map<Integer, Integer> valueCount = new HashMap<>();
            for (Card card : cards) {
                if (card == null) {
                    continue; // Skip null cards
                }
                int value = card.getValue();
                valueCount.put(value, valueCount.getOrDefault(value, 0) + 1);
            }
        
            // Step 2: Find the least occurring card value
            int leastOccurringValue = -1;
            int minCount = Integer.MAX_VALUE;
        
            for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
                if (entry.getValue() < minCount) {
                    minCount = entry.getValue();
                    leastOccurringValue = entry.getKey();
                }
            }
        
            // Step 3: Remove a card with the least occurring value
            for (int i = 0; i < cards.size(); i++) {
                Card card = cards.get(i);
                if (card != null && card.getValue() == leastOccurringValue) {
                    return cards.remove(i);
                }
            }
        
            // Fallback: Remove the first non-null card
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i) != null) {
                    return cards.remove(i);
                }
            }
        
            // If all cards are null, throw an exception
            throw new IllegalStateException("All cards are null; cannot discard.");
        }

        public synchronized boolean isWinningHand() {
            if (cards.isEmpty()) {
                return false; // A hand with no cards cannot be a winning hand
            }
        
            // Create a map to store the frequency of each card value
            Map<Integer, Integer> valueCountMap = new HashMap<>();
            
            // Count the occurrences of each card value
            for (Card card : cards) {
                if (card != null) {
                    int value = card.getValue();
                    valueCountMap.put(value, valueCountMap.getOrDefault(value, 0) + 1);
                }
            }
        
            // Check if any value occurs exactly 4 times
            for (int count : valueCountMap.values()) {
                if (count == 4) {
                    return true; // A winning hand if there are exactly 4 cards of the same value
                }
            }
        
            return false; // No value occurs exactly 4 times
        }

        public synchronized void showCardsInHand(){
            for (Card card : cards){
                if (card != null) {
                    System.out.println("card id: " +card.getId()+ " card value: " + card.getValue());
            }}
        }
    }
}
