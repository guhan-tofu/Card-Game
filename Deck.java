import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Deck {
    private ConcurrentLinkedQueue<Card> cards = new ConcurrentLinkedQueue<>(); // Thread safe
    private String fileName;


    public Deck(int deckId) {
        // Assign a unique file name based on the deck ID
        this.fileName = "deck"+(deckId+1) + "_output.txt";
        createDeckFile();
    }


    // Function to create a file for a deck
    private void createDeckFile() {
        try {
            File deckFile = new File(fileName);
            if (deckFile.createNewFile()) {
                System.out.println("File created: " + deckFile.getName());
            } else {
                System.out.println("File already exists: " + deckFile.getName());
            }
        } catch (IOException e) {
            System.err.println("Error creating file for deck: " + fileName);
            e.printStackTrace();
        }
    }

    //Getters and Setters 
    public void addCard(Card card) {
        cards.add(card);  
    }

    public Card drawCard() {
        return cards.poll();  
    }

    public int getSize() {
        return cards.size();  
    }

    //Clear the deck from the cards
    public void clearDeck() {
        cards.clear();
    }

    //Print method to see cards currently in the deck
    public void showCards() {
        for (Card card : cards) {
            if (card != null) {
                System.out.println("card id: " + card.getId() + " card value: " + card.getValue());
            }
        }
    }


    // Function to delete file, used in test cases only
    public void deleteDeckFile(String path){

        File file = new File(path);

        if (file.delete()) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("Failed to delete the file. File may not exist.");
        }
    }

    // Function to write all cards in the deck to respective file
    public void writeAllCardsToFile() {
        try (FileWriter writer = new FileWriter(fileName)) {
           
            writer.write(fileName + " contents: ");
            
            for (Card card : cards) {
                if (card != null) {
                    writer.write(card.getValue() + " ");
                }
            }
    
            writer.write("\n");
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}