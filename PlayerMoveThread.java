import java.io.IOException;

public class PlayerMoveThread extends BasicThread implements PlayerMoveEventListener {

    public PlayerMoveThread(String playerName) throws IOException {
        // Initialize with a unique file for each player
        super(playerName + "_output.txt");
    }

    @Override
    public void eventOccured(PlayerMoveEvent evt) throws IOException {
        // Write the event message to the player-specific file
        this.writeToFile(evt.getMessage());
    }
}