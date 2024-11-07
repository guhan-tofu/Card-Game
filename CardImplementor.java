import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CardImplementor {
    private ArrayList<Deck> myDecks = new ArrayList<>();
    private ArrayList<Player> myPlayers = new ArrayList<>();
    private ArrayList<Card> myCards = new ArrayList<>();
    

    public void createPlayers(int nPlayer) {

        
        myDecks.clear();
        myPlayers.clear();

        // create the decks
        for (int i = 0; i < nPlayer; i++) {
            myDecks.add(new Deck());
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
    
            myPlayers.add(new Player(leftDeck, rightDeck));
        }
    }

    public String showPlayerDetails(int playerId) {
        Player player = myPlayers.get(playerId);
        Deck leftDeck = player.getLeftDeck();
        Deck rightDeck = player.getRightDeck();
    
        // Retrieve and show the indices of the decks in myDecks
        int leftDeckIndex = myDecks.indexOf(leftDeck);
        int rightDeckIndex = myDecks.indexOf(rightDeck);
    
        return "Left deck: " + leftDeckIndex + ", Right deck: " + rightDeckIndex;
    }


    public void loadCardsFromFile(String filename) {

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int value = Integer.parseInt(line.trim()); // Convert each line to an integer
                Card card = new Card(value);               // Create a new Card object
                myCards.add(card);                         // Add the card to the ArrayList
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + e.getMessage());
        }
    }


    public void showCardValues(){
        for (Card card : myCards) {
            System.out.println("Card{id=" + card.getId() + ", value=" + card.getValue() + "}");
        }
    }



    // public void distributeToPlayers(){
        // logic here
    // }


    //public void distributeToDecks(){
        // logic here
    // }
}
