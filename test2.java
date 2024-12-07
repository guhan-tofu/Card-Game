public class test2 {
    public static void main(String[] args) {
        CardImplementor impl = new CardImplementor();

        impl.createPlayers(2);

        impl.loadCardsFromFile("pack2.txt", 2 );
        // impl.showCardValues();


        impl.distributeToPlayers(2);
        impl.distributeToDecks(2);
        System.out.println(impl.showPlayerDetails(0));
        System.out.println(impl.showPlayerDetails(1));

        System.out.println("Cards in player 0 :");
        impl.showCardsInHand(0);
        System.out.println("Cards in deck 0 :");
        impl.showCardsInDeck(0);

        System.out.println("  ");

        System.out.println("Cards in player 1 :");
        impl.showCardsInHand(1);
        System.out.println("Cards in deck 1 :");
        impl.showCardsInDeck(1);
        


        for(PlayerMoveThread myThing : CardImplementor.myPlayers){
            myThing.start();
      
        }

    }
}


//clean up
//comments
//error handling
//jUnit
// readME