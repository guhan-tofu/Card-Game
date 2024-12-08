import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerMoveThread extends Thread  { 

    private String fileName;

    // Constructor
    public PlayerMoveThread(Deck leftDeck, Deck rightDeck) throws IOException {
        this.fileName = "player"+(idCounter+1) + "_output.txt";
        this.id = idCounter++;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        createPlayerFile();
        
    }

    // Function to create file for every player
    private void createPlayerFile() {
        try {
            File playerFile = new File(fileName);

            // Check if the file already exists
            if (playerFile.exists()) {
                try (FileWriter writer = new FileWriter(playerFile)) {
                    writer.write(""); // Write an empty string to clear the file
                    System.out.println("File cleared: " + playerFile.getName());
                }
            } else {
                // If file doesn't exist, create a new one
                if (playerFile.createNewFile()) {
                    System.out.println("File created: " + playerFile.getName());
                } else {
                    System.err.println("Failed to create file: " + playerFile.getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating or clearing file for deck: " + fileName);
            e.printStackTrace();
        }
    }

    // Function used to delete any player file only used in test case
    public void deletePlayerFile(String path) {
        File file = new File(path);
        System.out.println("Attempting to delete file: " + file.getAbsolutePath());
    
        if (file.exists()) {
            // Ensure the file is writable
            if (!file.setWritable(true)) {
                System.err.println("Failed to make the file writable: " + path);
            }
    
            // Attempt to delete the file
            if (file.delete()) {
                System.out.println("Successfully deleted the file: " + path);
            } else {
                System.err.println("Failed to delete the file: " + path);
            }
        } else {
            System.err.println("File does not exist: " + path);
        }
    }


    private static int idCounter = 0;
    private final int id;
    private final Deck leftDeck;
    private final Deck rightDeck;
    private final Hand hand = new Hand();
    private static volatile boolean gameOver = false;
    
    //Getters and Setters for the player class
    public Deck getLeftDeck(){
        return this.leftDeck;
    }

    public Deck getRightDeck(){
        return this.rightDeck;
    }

    //Instance of the CardImplementor class to retrieve static fields
    CardImplementor imple = new CardImplementor();

    @Override
    public void run() {
        while (!gameOver) { // Top-level check
            if (hand.isWinningHand()) {
                synchronized (PlayerMoveThread.class) {  // Ensure one thread declares victory
                    if (!gameOver) {  // Double-check the flag to avoid race conditions
                        gameOver = true;
                        String winnerHand = hand.getCardsInHand();
                        try (FileWriter writer = new FileWriter("player"+Integer.toString(id+1)+"_output.txt", true)) { // true for append mode
                            writer.write("player " + Integer.toString(id + 1)+  " wins" + "\n");
                            writer.write("player " + Integer.toString(id + 1)+  " exits" + "\n");
                            writer.write("player " + (id + 1) + " final hand: " + winnerHand + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        for (PlayerMoveThread player : CardImplementor.myPlayers){
                            if (player.id != this.id) { // Exclude the current winning player
                                String finalHand = player.hand.getCardsInHand();
                                try (FileWriter writer = new FileWriter("player"+Integer.toString(player.id + 1) + "_output.txt", true)) {
                                    writer.write("player " + (this.id + 1) + " has informed player " + (player.id + 1) + " that player " + (this.id + 1) + " has won\n");
                                    writer.write("player " + Integer.toString(player.id + 1)+  " exits" + "\n");
                                    writer.write("player " + (player.id + 1) + " final hand: " + finalHand + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        System.out.println("Player " + (id + 1) + " has won!");
                        for(Deck decks : CardImplementor.myDecks){
                            decks.writeAllCardsToFile();
                        }
                      
                        
                    }
                }
                break; // Exit the loop after declaring the winner
            }

            if (leftDeck.getSize() != 3) {
                
                doBoth(id+1); // Perform card operations
            }
        }
        // Thread naturally exits here
    }


    // Function that Draws and Discards a card (Atomic)
    public synchronized void doBoth(int idnum){
        
        Card cardToDraw = leftDeck.drawCard();
        int leftDeckIndex = CardImplementor.myDecks.indexOf(leftDeck);
        int rightDeckIndex = CardImplementor.myDecks.indexOf(rightDeck);
        System.out.println("Card to Draw : "+cardToDraw);
        hand.addCard(cardToDraw); 
        Card cardToDiscard = hand.discardCard();
        System.out.println("Card to Discard : "+ cardToDiscard);
        rightDeck.addCard(cardToDiscard);
        // Write to player file what card has been drawn and discarded
        try (FileWriter writer = new FileWriter("player"+Integer.toString(idnum)+"_output.txt", true)) { // true for append mode
        writer.write("player " + Integer.toString(idnum)+  " draws a " +cardToDraw+ " from deck "+leftDeckIndex + "\n");
        writer.write("player " + Integer.toString(idnum)+  " discards a " +cardToDiscard+ " to deck "+rightDeckIndex + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


        
    }

    // Function to draw Card
    public synchronized void drawCard() {
        Card cardToDraw = leftDeck.drawCard();
        if (cardToDraw != null) {
            hand.addCard(cardToDraw);
        } else {
            throw new IllegalStateException("No cards left to draw.");
        }
    }

    // Function to discard Card
    public synchronized void discardCard() {
        Card cardToDiscard = hand.discardCard();
        rightDeck.addCard(cardToDiscard);
    }

    //Methods to work with the nested class Hand
    public void addCardToHand(Card card){
        hand.addCard(card);
    }

    public void clearHand(){
        hand.clearHand();
    }

    public int getPlayerId(){
        return this.id;
    }

    public void showCardsInHand() {
        hand.showCardsInHand(); // Delegate to Hand's method
    }

    public boolean isWinningHand(){
        return hand.isWinningHand();
    }

    public int[] getCardValues(){
        return hand.getCardValues();
    }

    public void setChecker(){
        hand.setChecker();
    }

    // Nested Hand Class
    private class Hand {
        private volatile List<Card> cards = new ArrayList<>();
        private volatile boolean checker = false;

        // Function to add card to hand
        public void addCard(Card card) {
            cards.add(card);
        }

        public void setChecker(){
            checker = true;
        }

        // Function to get all cards in hand
        public int[] getCardValues() {
            int[] cards4 = new int[4]; // hand has 4 cards
            int index = 0; // Track the position to insert into the array
        
            for (Card card : cards) {
                if (card == null) {
                    continue; // Skip null cards
                }
                if (index < 4) { // Ensure we only fill up to 4 values
                    cards4[index] = card.getValue();
                    index++;
                }
            }
        
            return cards4;
        }
        
        // Function that returns true if player has a winning hand
        public synchronized boolean isWinningHand() {
            if (cards.isEmpty()) {
                return false; // A hand with no cards cannot be a winning hand
            }
        
            Card firstCard = null;
        
            // Find the first non null card
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
        
            // Check if all non null cards have the same value
            for (Card card : cards) {
                if (card != null && card.getValue() != firstValue) {
                    return false; // Not a winning hand if values differ
                }
            }
        
            return true; 
        }


        // Print funtion to show every card in hand and it's Id
        public void showCardsInHand(){
            for (Card card : cards){
                if (card != null) {
                    System.out.println("card id: " +card.getId()+ " card value: " + card.getValue());
            }}
        }

        //Print function to return the cards in hand as a String
        public String getCardsInHand() {
            StringBuilder sb = new StringBuilder();
            for (Card card : cards) {
                if (card != null) {
                    sb.append(card.getValue()).append(" ");
                }       
            }
            return sb.toString().trim(); // Remove trailing space
        }

        // Function to remove a card from hand
        public synchronized Card discardCard() {
            int playerId = (getPlayerId()+1); // Use the player's ID as the preferred card value

            if (cards.isEmpty()) {
                throw new IllegalStateException("No cards to discard.");
            }


            if(checker == true){
                // Fallback: Remove the first non-null card
                for (int i = 0; i < cards.size(); i++) {
                    if (cards.get(i) != null) {
                        checker = false;
                        return cards.remove(i);
                    }
                }    
            }
        
            for (int i = 0; i < cards.size(); i++) {
                Card card = cards.get(i);
                if (card != null && card.getValue() != playerId) {
                    return cards.remove(i); // Discard the first non-preferred card
                }
            }
            
            
        
            // If all cards are null, throw an exception
            throw new IllegalStateException("All cards are null; cannot discard.");
        }

        public synchronized void clearHand() {
            cards.clear(); // Remove all cards from the hand
        }

    }
}
