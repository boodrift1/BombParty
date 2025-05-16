public class Player
{
    private int lives;
    private String name;

    public Player(String initName)
    {
        name = initName;
        lives = 3;
    }

    public void reset() {
        lives = 3;
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
