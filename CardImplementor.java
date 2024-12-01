import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CardImplementor extends Thread {//can be BasicThread
    
    public static ArrayList<Deck> myDecks = new ArrayList<>();
    public static ArrayList<PlayerMoveThread> myPlayers = new ArrayList<>();
    private ArrayList<Card> myCards = new ArrayList<>();
    private ArrayList<PlayerMoveEventListener> PlayerListeners = new ArrayList<>(); // Keep array of our threads that listen to PlayerMove
    private static volatile boolean gameInProgress = true;

    // private CardImplementor() throws java.io.IOException {
    //     super("IOProcessor_input.txt");
    // }

    public static void main(String[] args) throws IOException, InterruptedException {

        myDecks.clear();
        myPlayers.clear();
        // construct the threads
        CardImplementor ioProcessor = new CardImplementor();
        // create the decks
        
        


        
        
        // the "source" of events in this application. This object generates events and is responsible for notifying registered listeners when certain events occur.
        PlayerMoveThread player1Thread = new PlayerMoveThread(myDecks.get(3), myDecks.get(0));
        PlayerMoveThread player2Thread = new PlayerMoveThread(myDecks.get(0), myDecks.get(1));
        PlayerMoveThread player3Thread = new PlayerMoveThread(myDecks.get(1), myDecks.get(2));
        PlayerMoveThread player4Thread = new PlayerMoveThread(myDecks.get(2), myDecks.get(3));

        myPlayers.add(player1Thread);
        myPlayers.add(player2Thread);
        myPlayers.add(player3Thread);
        myPlayers.add(player4Thread);

        //ioProcessor.loadCardsFromFile("Pack.txt");
        // impl.showCardValues();


        ioProcessor.distributeToPlayers(4);
        ioProcessor.distributeToDecks(4);

        // // register listeners with the source
        // ioProcessor.addplayerMoveEventListener(player1Thread); // fileThread being registered as a FileWrite Event listener
        // ioProcessor.addplayerMoveEventListener(player2Thread);
        // ioProcessor.addplayerMoveEventListener(player3Thread);
        // ioProcessor.addplayerMoveEventListener(player4Thread);

        System.out.println("starting threads");
        // start listening threads
        player1Thread.start();
        player2Thread.start();
        player3Thread.start();
        player4Thread.start();
        Thread.sleep(1000); // allows threads to initialize fully

        // start ioProcessor once all other threads are ready
        ioProcessor.start();
        ioProcessor.showCardsInDeck(0);
        ioProcessor.showCardsInDeck(1);
        ioProcessor.showCardsInDeck(2);
        ioProcessor.showCardsInDeck(3);

      
    }


    @Override
    public void run() {
        while (gameInProgress) {
            try {

                System.out.println("Game STARTING!");
                this.showCardsInDeck(0);
                this.showCardsInDeck(1);
                this.showCardsInDeck(2);
                this.showCardsInDeck(3);
                for (PlayerMoveThread player : myPlayers) {
                    // Perform actions: drawing and discarding cards
                    player.drawCard();   // Player draws a card
                    player.discardCard(); // Player discards a card
                    
                    // Optionally, print the player's hand
                   
                    this.showCardsInDeck(0);
                    this.showCardsInDeck(1);
                    this.showCardsInDeck(2);
                    this.showCardsInDeck(3);
                                                     
                                                                
                }

                Thread.sleep(500);

                if (isGameOver()) {
                    gameInProgress = false;
                    System.out.println("Game Over!");
                    
                }
                
            } catch (InterruptedException e) {

            }
        }
    }

    private boolean isGameOver() {
        // Implement your game-over conditions here, such as when all players have won
        // This is just a placeholder logic to stop the game if all players are done
        for (PlayerMoveThread player : myPlayers) {
            if (!player.isWinningHand()) {
                return false; // If any player hasn't won, the game is still in progress
            }
        }
        return true; // If all players are done, the game is over
    }

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

    public void playerMove(int playerId){
        PlayerMoveThread player = myPlayers.get(playerId);
        player.drawCard();
        player.discardCard();
        
    }

    public void startPlayers() {
        List<Thread> playerThreads = new ArrayList<>();
    
        // Start all player threads
        for (PlayerMoveThread player : myPlayers) {
            Thread playerThread = new Thread(player); // Wrap each Player in a Thread
            playerThreads.add(playerThread);
            playerThread.start(); // Start the Thread
        }
    
        // Sleep for a brief moment to allow all threads to start simultaneously
        try {
            Thread.sleep(100); // 100 milliseconds or any brief delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        // Join all player threads to wait for their completion
        for (Thread playerThread : playerThreads) {
            try {
                playerThread.join(); // Wait for each thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    

    private void notifyplayerMoveWriteEventListeners(PlayerMoveEvent evt) throws IOException{ // responsible for notifying every thread in the List that the event occured  
        for (PlayerMoveEventListener l : PlayerListeners)
            l.eventOccured(evt);
    }

    public void addplayerMoveEventListener(PlayerMoveEventListener listener) {  // adding playermove event listener threads into the list (only allows threads that implement the respective listener)
        this.PlayerListeners.add(listener);
    }






    



       

}
