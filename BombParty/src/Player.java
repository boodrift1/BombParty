public class Player
{
    private int timeLeft; // counted in milliseconds
    private int lives;
    private boolean currentlyPlaying;

    public Player(boolean initTurn)
    {
        lives = 3;
        timeLeft = 5000;
        currentlyPlaying = initTurn;
    }

    
}
