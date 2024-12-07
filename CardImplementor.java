import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CardImplementor implements CardInterface {
    
    public static ArrayList<Deck> myDecks = new ArrayList<>();
    public static ArrayList<PlayerMoveThread> myPlayers = new ArrayList<>();
    private final ArrayList<Card> myCards = new ArrayList<>();
    
    // Function to get the array with all cards used in test case only
    public ArrayList<Card> getMyCards() {
        return myCards;
    }
    

    // Function that creates players then assigns respective decks 
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
                e.printStackTrace();  // Handle the exception 
            }
        }
    }

    // Print function to verify the left and right deck of each player
    @Override
    public String showPlayerDetails(int playerId) {
        PlayerMoveThread player = myPlayers.get(playerId);
        Deck leftDeck = player.getLeftDeck();
        Deck rightDeck = player.getRightDeck();

        int leftDeckIndex = myDecks.indexOf(leftDeck);
        int rightDeckIndex = myDecks.indexOf(rightDeck);
    
        return "Left deck: " + leftDeckIndex + ", Right deck: " + rightDeckIndex;
    }

    // Function to load all cards from a directory of valid txt file
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


            myCards.addAll(tempCards); // Add all valid cards to the main card collection

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + e.getMessage());
        }
    }

    
    // Print function to check all card values and their respective Ids
    @Override
    public void showCardValues(){
        for (Card card : myCards) {
            System.out.println("Card{id=" + card.getId() + ", value=" + card.getValue() + "}");
        }
    }


    // Function to distribute cards to hand of each player in round robin
    @Override
    public void distributeToPlayers(int nPlayer){
        for (int i = 0; i < (4*nPlayer); i++){
            Card card = myCards.get(i);
            PlayerMoveThread player = myPlayers.get(i%nPlayer);
            player.addCardToHand(card);
        }
    }
   
    // Function to distribute cards to decks in round robin
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
        try {
            Deck deck = myDecks.get(deckId);
            deck.showCards();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid deck ID: " + deckId + ". Deck does not exist.");
        }
    }

    @Override 
    public void showCardsInHand(int playerId){

        try{
            PlayerMoveThread player = myPlayers.get(playerId);
            player.showCardsInHand();
        }catch (IndexOutOfBoundsException e){
            System.err.println("Invalid player ID: " + playerId + ". Player does not exist.");
        }
    }

 
}
