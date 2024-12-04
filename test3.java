import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class test3 {
    

    public static void main(String[] args) {
        Deck deck = new Deck(5);
        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);


        deck.showCards();

    }
        
}
