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
        System.out.println(myGame);

        String userInput = input.nextLine();

        if (myGame.userInputCheck(userInput))
        {
            System.out.println("You are valid.");
        }
        else
        {
            System.out.println("YOU SUCK");
        }
    }
}
