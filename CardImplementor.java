import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CardImplementor implements CardInterface {
    
    public static ArrayList<Deck> myDecks = new ArrayList<>();
    public static ArrayList<PlayerMoveThread> myPlayers = new ArrayList<>();
    private final ArrayList<Card> myCards = new ArrayList<>();
    

    public ArrayList<Card> getMyCards() {
        return myCards;
    }
    
    @Override
    public void createPlayers(int nPlayer) {

        if (nPlayer < 2){
            throw new IllegalArgumentException(
                    "Number of players must be at least 2. Found: " + nPlayer);
        }
        myDecks.clear();
        myPlayers.clear();

        // create the decks
        for (int i = 0; i < nPlayer; i++) {
            myDecks.add(new Deck(i));
        }
        
        // create the players with circular deck references
        for (int i = 0; i < nPlayer; i++) {
            Deck leftDeck;
            Deck rightDeck;
    
            if (i == 0) {
                // player 0's left deck is the last deck, and right deck is the first deck
                leftDeck = myDecks.get(nPlayer - 1);
                rightDeck = myDecks.get(0);
            } else {
                // for other players, left deck is at index i-1 and right deck is at index i
                leftDeck = myDecks.get(i-1);
                rightDeck = myDecks.get(i );
            }
    
            try {
                myPlayers.add(new PlayerMoveThread(leftDeck, rightDeck));  // Try to create a new PlayerMoveThread
            } catch (IOException e) {
                e.printStackTrace();  // Handle the exception (you can log it or rethrow as needed)
            }
        }
    }

    @Override
    public String showPlayerDetails(int playerId) {
        PlayerMoveThread player = myPlayers.get(playerId);
        Deck leftDeck = player.getLeftDeck();
        Deck rightDeck = player.getRightDeck();
    
        // Retrieve and show the indices of the decks in myDecks
        int leftDeckIndex = myDecks.indexOf(leftDeck);
        int rightDeckIndex = myDecks.indexOf(rightDeck);
    
        return "Left deck: " + leftDeckIndex + ", Right deck: " + rightDeckIndex;
    }

    @Override
    public void loadCardsFromFile(String path, int numofPlayers) {
        myCards.clear();
        ArrayList<Card> tempCards = new ArrayList<>();
        HashMap<Integer, Integer> cardFrequency = new HashMap<>();

        File file = new File(path);

        try {
            // Ensure the path points to a file and ends with ".txt"
            if (!file.isFile() || !file.getName().endsWith(".txt")) {
                throw new IllegalArgumentException("Invalid path: Must provide a valid .txt file.");
            }

            // Process the .txt file
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    int value = Integer.parseInt(line.trim()); // Convert each line to an integer
                    Card card = new Card(value);
                    tempCards.add(card); // Create a new Card object and add to the ArrayList

                    // Count the occurrences of each card value
                    cardFrequency.put(value, cardFrequency.getOrDefault(value, 0) + 1);
                }
            }

            // Check if the pack size matches the required size
            if (tempCards.size() != 8 * numofPlayers) {
                throw new IllegalArgumentException(
                    "The file does not contain the required " + (8 * numofPlayers) + " cards. Found: " + tempCards.size());
            }

            // Check if at least one set of 4 cards matches one of the player indices
            // boolean hasValidRepetitionsForPlayers = false;
            // for (int playerId = 1; playerId <= numofPlayers; playerId++) { // Loop from 1 to numofPlayers
            //     if (cardFrequency.getOrDefault(playerId, 0) >= 4) {
            //         hasValidRepetitionsForPlayers = true;
            //         break;
            //     }
            // }

            // if (!hasValidRepetitionsForPlayers) {
            //     throw new IllegalArgumentException(
            //         "The pack is invalid: no card value repeats four or more times for any player index.");
            // }

            myCards.addAll(tempCards); // Add all valid cards to the main card collection

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + e.getMessage());
        }
    }

    

    @Override
    public void showCardValues(){
        for (Card card : myCards) {
            System.out.println("Card{id=" + card.getId() + ", value=" + card.getValue() + "}");
        }
    }

    @Override
    public void distributeToPlayers(int nPlayer){
        for (int i = 0; i < (4*nPlayer); i++){
            Card card = myCards.get(i);
            PlayerMoveThread player = myPlayers.get(i%nPlayer);
            player.addCardToHand(card);
        }
    }
   
    @Override
    public void distributeToDecks(int nPlayer){
        for (int i = 4*nPlayer; i < (8*nPlayer); i++){
            Card card = myCards.get(i);
            Deck deck = myDecks.get(i%nPlayer);
            deck.addCard(card);
        }
    }

    @Override        
    public void showCardsInDeck(int deckId){
        Deck deck = myDecks.get(deckId);
        deck.showCards();
    }

    @Override 
    public void showCardsInHand(int playerId){
        PlayerMoveThread player = myPlayers.get(playerId);
        player.showCardsInHand();
    }

 
}
