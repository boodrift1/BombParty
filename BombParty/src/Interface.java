import java.util.Scanner;

public class Interface
{
    public static void main(String[] args)
    {
        Game myGame = new Game();

        System.out.println("Your combination: " + myGame.getLetterCombo());

        Scanner input = new Scanner(System.in);
        System.out.print("Please type a word: ");
        String userInput = input.nextLine();

        if (myGame.userInputCheck(userInput))
        {
            System.out.println("Valid");
        }
        else
        {
            System.out.println("You are not valid");
        }
    }
}
