public class test {
    public static void main(String[] args){

        //add user input for number of players and the pack.txt

        CardImplementor impl = new CardImplementor();

        impl.createPlayers(4);
        System.out.println(impl.showPlayerDetails(0));
        System.out.println(impl.showPlayerDetails(1));
        System.out.println(impl.showPlayerDetails(2));
        System.out.println(impl.showPlayerDetails(3));

        
        
        impl.loadCardsFromFile("Pack.txt");
        impl.showCardValues();

        //first set up the playing ground
        //then initialise the running of player threads
        //events and listeners 
        //generate 2n .txt files for players and decks

        

    }
}
