public class Player
{
    private final int MAX_LIVES;
    private int lives;
    private String name;

    public Player(String initName)
    {
        MAX_LIVES = 3;
        name = initName;
        lives = MAX_LIVES;
    }

    public void reset() {
        lives = MAX_LIVES;
    }

    public boolean hasLives()
    {
        return !(lives == 0);
    }
    public void lifeLost()
    {
        if (hasLives())
        {
            lives--;
        }
    }
    public int getLives()
    {
        return lives;
    }
    public String getName()
    {
        return name;
    }
}
