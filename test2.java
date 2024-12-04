public class test2 {
    public static void main(String[] args) {
        CardImplementor impl = new CardImplementor();

        impl.createPlayers(2);

<<<<<<< HEAD
        impl.loadCardsFromFile("pack3.txt", 2);
=======
        impl.loadCardsFromFile("pack2.txt", 2);
>>>>>>> 312354be11bd28da7ca2b13f140a5ac7617f38d1
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
//modify loadCardsFromFile -- validate pack and load path/directory
//actual executable
//clean up
//comments
//error handling
//jUnit
// readME