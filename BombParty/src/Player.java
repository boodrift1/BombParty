public class Player
{
    private int lives;
    private String name;

    public Player(String initName)
    {
        name = initName;
        lives = 1;
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
    public String getName()
    {
        return name;
    }
}
