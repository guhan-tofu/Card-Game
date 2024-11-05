public class CardGame {
    public static void main(String[] args) {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();
        
        Player player1 = new Player(1, deck1, deck2);
        Player player2 = new Player(2, deck2, deck1);

        Thread playerThread1 = new Thread(player1);
        Thread playerThread2 = new Thread(player2);

        playerThread1.start();
        playerThread2.start();
    }
}

