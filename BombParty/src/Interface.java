import java.util.*;
import java.util.concurrent.*;

public class Interface
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter a name: ");
        Player[] userList = {new Player(input.nextLine())};
        System.out.println();

        Game myGame = new Game(userList);

        System.out.println(myGame.playOneTurn());
    }
}
