public class test {
    public static void main(String[] args){

        CardImplementor impl = new CardImplementor();

        impl.createPlayers(4);
        System.out.println(impl.showPlayerDetails(0));
        System.out.println(impl.showPlayerDetails(1));
        System.out.println(impl.showPlayerDetails(2));
        System.out.println(impl.showPlayerDetails(3));
        

        

    }
}
