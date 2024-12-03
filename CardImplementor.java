import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CardImplementor extends Thread {//can be BasicThread
    
    public static ArrayList<Deck> myDecks = new ArrayList<>();
    public static ArrayList<PlayerMoveThread> myPlayers = new ArrayList<>();
    private final ArrayList<Card> myCards = new ArrayList<>();
   
    

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

    public String showPlayerDetails(int playerId) {
        PlayerMoveThread player = myPlayers.get(playerId);
        Deck leftDeck = player.getLeftDeck();
        Deck rightDeck = player.getRightDeck();
    
        // Retrieve and show the indices of the decks in myDecks
        int leftDeckIndex = myDecks.indexOf(leftDeck);
        int rightDeckIndex = myDecks.indexOf(rightDeck);
    
        return "Left deck: " + leftDeckIndex + ", Right deck: " + rightDeckIndex;
    }


    public void loadCardsFromFile(String filename, int numofPlayers) {
        ArrayList<Card> tempCards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int value = Integer.parseInt(line.trim()); // Convert each line to an integer
                Card card = new Card(value);
                tempCards.add(card);               // Create a new Card object
                                        // Add the card to the ArrayList
            }

            if (tempCards.size() != 8 * numofPlayers) {
                throw new IllegalArgumentException(
                    "The file does not contain the required " + (8 * numofPlayers) + " cards. Found: " + tempCards.size());
            }
            myCards.addAll(tempCards); 
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + e.getMessage()); // add exceptions for when nPlayers is not 8n cards 
        }
    }


    public void showCardValues(){
        for (Card card : myCards) {
            System.out.println("Card{id=" + card.getId() + ", value=" + card.getValue() + "}");
        }
    }



    public void distributeToPlayers(int nPlayer){
        for (int i = 0; i < (4*nPlayer); i++){
            Card card = myCards.get(i);
            PlayerMoveThread player = myPlayers.get(i%nPlayer);
            player.addCardToHand(card);
        }
        }
   

    public void distributeToDecks(int nPlayer){
        for (int i = 4*nPlayer; i < (8*nPlayer); i++){
            Card card = myCards.get(i);
            Deck deck = myDecks.get(i%nPlayer);
            deck.addCard(card);
        }
        }

    public void showCardsInDeck(int deckId){
        Deck deck = myDecks.get(deckId);
        deck.showCards();
    }

    public void showCardsInHand(int playerId){
        PlayerMoveThread player = myPlayers.get(playerId);
        player.showCardsInHand();
    }

 


 





    



       

}
