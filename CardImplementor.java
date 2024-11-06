import java.util.*;

public class CardImplementor {
    private ArrayList<Deck> myDecks = new ArrayList<>();
    private ArrayList<Player> myPlayers = new ArrayList<>();
    private ArrayList<Card> myCards = new ArrayList<>();

    public void createPlayers(int nPlayer) {

        
        myDecks.clear();
        myPlayers.clear();

        // Create the decks
        for (int i = 0; i < nPlayer; i++) {
            myDecks.add(new Deck());
        }
        
        // Create the players with circular deck references
        for (int i = 0; i < nPlayer; i++) {
            Deck leftDeck;
            Deck rightDeck;
    
            if (i == 0) {
                // Player 0's left deck is the last deck, and right deck is the first deck
                leftDeck = myDecks.get(nPlayer - 1);
                rightDeck = myDecks.get(0);
            } else {
                // For other players, left deck is at index i and right deck is at index i - 1
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
}
