
public interface CardInterface {

    /**
     * Creates players and their respective decks.
     * @param nPlayer the number of players to create.
     */
    void createPlayers(int nPlayer);

    /**
     * Loads cards from a file and validates the count against the required number of cards.
     * @param filename the name of the file containing card values.
     * @param numofPlayers the number of players in the game.
     */
    void loadCardsFromFile(String filename, int numofPlayers);

    /**
     * Distributes cards to players' hands.
     * @param nPlayer the number of players.
     */
    void distributeToPlayers(int nPlayer);

    /**
     * Distributes cards to decks.
     * @param nPlayer the number of players.
     */
    void distributeToDecks(int nPlayer);

    /**
     * Shows all cards currently assigned to a specific deck.
     * @param deckId the ID of the deck.
     */
    void showCardsInDeck(int deckId);

    /**
     * Shows all cards currently in a specific player's hand.
     * @param playerId the ID of the player.
     */
    void showCardsInHand(int playerId);

    /**
     * Displays the left and right deck details of a player.
     * @param playerId the ID of the player.
     * @return a string describing the player's decks.
     */
    String showPlayerDetails(int playerId);

    /**
     * Displays all the card values currently loaded in the game.
     */
    void showCardValues();
}

