public class SingleGame extends Game
{
    private int score; // increments by 1 for each successful word

    public SingleGame(Player[] player)
    {
        super(player);
        score = 0;
    }

    public void play()
    {

    }

    public String toString()
    {
        return "Score: " + score + "\n" + super.toString();
    }
}
