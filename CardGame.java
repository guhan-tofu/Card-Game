import java.io.File;
import java.util.Scanner;

public class CardGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CardImplementor impl = new CardImplementor();
        int numberOfPlayers = 0;
        String filePath = null;

        // Prompt user for number of players
        while (true) {
            try {
                System.out.print("Enter the number of players: ");
                numberOfPlayers = Integer.parseInt(scanner.nextLine());
                if (numberOfPlayers < 2) {
                    throw new IllegalArgumentException("There must be at least 2 players.");
                }
                System.out.print("Enter the path to the pack.txt file: ");
                filePath = scanner.nextLine();
                File file = new File(filePath);
                if (!file.exists() || !file.canRead()) {
                    throw new IllegalArgumentException("File does not exist or cannot be read. Please check the path and try again.");
                }
                break; // Exit loop on valid input
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for the number of players.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

 

        // Create players and load cards
        impl.createPlayers(numberOfPlayers);
        impl.loadCardsFromFile(filePath, numberOfPlayers);

        // Distribute cards to players and decks
        impl.distributeToPlayers(numberOfPlayers);
        impl.distributeToDecks(numberOfPlayers);

        // Start player threads
        for (PlayerMoveThread myThing : CardImplementor.myPlayers) {
            myThing.start();
        }
    }
}

//modify loadCardsFromFile -- validate pack and load path/directory
//actual executable
//clean up
//comments
//error handling
//jUnit
// readME