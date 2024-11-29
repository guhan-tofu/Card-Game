import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerMoveThread extends BasicThread implements PlayerMoveEventListener {

    public PlayerMoveThread(Deck leftDeck, Deck rightDeck) throws IOException {
        // Initialize with a unique file for each player
        super((idCounter+1) + "_output.txt");
        this.id = idCounter++;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        
    }


    @Override
    public void eventOccured(PlayerMoveEvent evt) throws IOException {
        // Write the event message to the player-specific file
        this.writeToFile(evt.getMessage());
    }

    private static int idCounter = 0;
    private final int id;
    private final Deck leftDeck;
    private final Deck rightDeck;
    private final Hand hand = new Hand();
    private static volatile boolean gameOver = false;



    public Deck getLeftDeck(){
        return this.leftDeck;
    }

    public Deck getRightDeck(){
        return this.rightDeck;
    }
    CardImplementor imple = new CardImplementor();

    @Override
    public void run() {
        while (!gameOver) { // Top-level check
            if (hand.isWinningHand()) {
                synchronized (PlayerMoveThread.class) {  // Ensure one thread declares victory
                    if (!gameOver) {  // Double-check the flag to avoid race conditions
                        gameOver = true;
                        System.out.println("Player " + (id + 1) + " has won!");
                        System.out.println(" ");
                        imple.showCardsInHand(0);
                        System.out.println(" ");
                        imple.showCardsInHand(1);
                      
                        
                    }
                }
                break; // Exit the loop after declaring the winner
            }

            if (leftDeck.getSize() != 3) {
                try {
                    Thread.sleep(10); // Delay for realism
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    break; // Exit the loop if interrupted
                }

                // Exit if game is over to avoid unnecessary work
                if (gameOver) {
                    for(Deck decks : CardImplementor.myDecks){
                        decks.writeAllCardsToFile();
                    }
                    break;
                }

                doBoth(id+1); // Perform card operations
            }
        }
        // Thread naturally exits here
    }

    public synchronized void doBoth(int idnum){
        Card cardToDraw = leftDeck.drawCard();
        System.out.println("Card to Draw : "+cardToDraw);
        hand.addCard(cardToDraw);
        // add thread writing here 
        Card cardToDiscard = hand.discardCard();
        System.out.println("Card to Discard : "+ cardToDiscard);
        rightDeck.addCard(cardToDiscard);
        // add thread writing here
        try (FileWriter writer = new FileWriter(Integer.toString(idnum)+"_output.txt", true)) { // true for append mode
        writer.write("player" + Integer.toString(idnum)+  " draws a " +cardToDraw+ " from deck 1" + "\n");
        writer.write("player" + Integer.toString(idnum)+  " discards a " +cardToDiscard+ " to deck 1" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


        
    }

    public synchronized void drawCard() {
        Card cardToDraw = leftDeck.drawCard();
        if (cardToDraw != null) {
            hand.addCard(cardToDraw);
        } else {
            throw new IllegalStateException("No cards left to draw.");
        }
    }

    public synchronized void discardCard() {
        Card cardToDiscard = hand.discardCard();
        rightDeck.addCard(cardToDiscard);
    }

    public void addCardToHand(Card card){
        hand.addCard(card);
    }

    public void showCardsInHand() {
        hand.showCardsInHand(); // Delegate to Hand's method
    }

    public boolean isWinningHand(){
        return hand.isWinningHand();
    }



    private class Hand {
        private volatile List<Card> cards = new ArrayList<>();

        public void addCard(Card card) {//id of card instead
            cards.add(card);
        }

    

        // public Card discardCard() {
        //     // Implement discard logic, e.g., selecting a card to discard
        //     return cards.remove(0); // Example of discarding the first card
        // }

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
        
            Card firstCard = null;
        
            // Find the first non-null card
            for (Card card : cards) {
                if (card != null) {
                    firstCard = card;
                    break;
                }
            }
        
            if (firstCard == null) {
                return false; // If all cards are null, it's not a winning hand
            }
        
            int firstValue = firstCard.getValue();
        
            // Check if all non-null cards have the same value
            for (Card card : cards) {
                if (card != null && card.getValue() != firstValue) {
                    return false; // Not a winning hand if values differ
                }
            }
        
            return true; // All non-null cards have the same value
        }

        public void showCardsInHand(){
            for (Card card : cards){
                if (card != null) {
                    System.out.println("card id: " +card.getId()+ " card value: " + card.getValue());
            }}
        }
    }
}
