public class test {
    public static void main(String[] args){

        //add user input for number of players and the pack.txt

        CardImplementor impl = new CardImplementor();

        impl.createPlayers(4);
        // System.out.println(impl.showPlayerDetails(0));
        // System.out.println(impl.showPlayerDetails(1));
        // System.out.println(impl.showPlayerDetails(2));
        // System.out.println(impl.showPlayerDetails(3));

        
        
        impl.loadCardsFromFile("Pack.txt");
        // impl.showCardValues();


        impl.distributeToPlayers(4);
        impl.distributeToDecks(4);



        // System.out.println("Cards in player 0");
        // impl.showCardsInHand(0);
        // System.out.println("Cards in deck 0");
        // impl.showCardsInDeck(0);
        // System.out.println("Cards in deck 3");
        // impl.showCardsInDeck(3);
        
        // impl.playerMove(0);
        // impl.playerMove(1);
        // impl.playerMove(2);
        // impl.playerMove(3);
        // System.out.println("Cards in player 0");
        // impl.showCardsInHand(0);
        // System.out.println("Cards in deck 0");
        // impl.showCardsInDeck(0);
        // System.out.println("Cards in deck 3");
        // impl.showCardsInDeck(3);

        

        impl.startPlayers();

        
        System.out.println("Cards in player 1");
        impl.showCardsInHand(0);
        System.out.println("Cards in player 2");
        impl.showCardsInHand(1);
        System.out.println("Cards in player 3");
        impl.showCardsInHand(2);
        System.out.println("Cards in player 4");
        impl.showCardsInHand(3);
        //then initialise the running of player threads
        //events and listeners 
        //generate 2n .txt files for players and decks



        System.out.println("Cards in deck 1");
        impl.showCardsInDeck(0);
        System.out.println("Cards in deck 2");
        impl.showCardsInDeck(1);
        System.out.println("Cards in deck 3");
        impl.showCardsInDeck(2);
        System.out.println("Cards in deck 4");
        impl.showCardsInDeck(3);

        

    }
}
