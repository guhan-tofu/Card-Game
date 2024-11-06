import java.util.*;

public class CardImplementor {
    private ArrayList<Deck> myDecks = new ArrayList<>();
    private ArrayList<Player> myPlayers = new ArrayList<>();
    private ArrayList<Card> myCards = new ArrayList<>();

    public void createPlayers(int nPlayer) {
        List<Deck> decks = new ArrayList<>();
        
        // Create the decks
        for (int i = 0; i < nPlayer; i++) {
            decks.add(new Deck());
        }
        
        // Create the players with circular deck references
        for (int i = 0; i < nPlayer; i++) {
            Deck leftDeck = decks.get((i - 1 + nPlayer) % nPlayer);
            Deck rightDeck = decks.get((i + 1) % nPlayer);
            myPlayers.add(new Player(leftDeck, rightDeck));
        }
    }

    public String showPlayerDetails(int playerId){
        Player player = myPlayers.get(playerId);
        Deck leftDeck = player.getLeftDeck();
        Deck rightDeck = player.getRightDeck();
        return ("Left deck: " + leftDeck + "Right deck " + rightDeck);
    }
}
