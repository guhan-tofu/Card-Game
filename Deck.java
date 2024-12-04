import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Deck {
    private ConcurrentLinkedQueue<Card> cards = new ConcurrentLinkedQueue<>();
    private String fileName;

    // Constructor
    public Deck(int deckId) {
        // Assign a unique file name based on the deck ID
        this.fileName = (deckId+1) + "_deck.txt";
        createDeckFile();
    }

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

    public void addCard(Card card) {
        cards.add(card);  // No need to synchronize
    }

    public Card drawCard() {
        return cards.poll();  // No need to synchronize
    }

    public int getSize() {
        return cards.size();  // No need to synchronize
    }

    public void showCards() {
        for (Card card : cards) {
            if (card != null) {
                System.out.println("card id: " + card.getId() + " card value: " + card.getValue());
            }
        }
    }

    public void writeAllCardsToFile() {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Start with the deck name
            writer.write(fileName + " contents: ");
            
            // Append each card's value to the line
            for (Card card : cards) {
                if (card != null) {
                    writer.write(card.getValue() + " ");
                }
            }
    
            // Add a newline at the end of the file
            writer.write("\n");
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}