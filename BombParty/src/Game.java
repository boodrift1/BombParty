import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.*;

public class Game
{
    private final HashSet<String> DICTIONARY;

    private Scanner input;
    private String letterCombo;
    private ArrayList<Player> playerList;
    private int currentPlayer;
    private int repeatWordCount;

    // precondition: initPlayers cannot be null
    public Game()
    {
        DICTIONARY = formDictSet();

        input = new Scanner(System.in);
        letterCombo = generateNewLetterCombo();
        currentPlayer = 0;
        repeatWordCount = 0;
        playerList = new ArrayList<>();
        initiateGame();
    }

    public void play()
    {
        while (winCheck() == -1)
        {
            playOneTurn();
        }
        System.out.print(printWin());
    }

    private void initiateGame()
    {
        System.out.print("Enter the number of players (2 or more): ");
        int playerCount = input.nextInt();
        input.nextLine();

        for (int i = 0; i < playerCount; i++) {
            System.out.print("Please enter a name: ");
            playerList.add(new Player(input.nextLine()));
        }
    }

    private boolean userInputCheck(String userInput)
    {
        return (comboCheck(userInput, letterCombo) && dictionaryCheck(userInput, DICTIONARY));
    }

    private boolean comboCheck(String userInput, String letterCombo)
    {
        return userInput.indexOf(letterCombo) != -1;
    }

    private boolean dictionaryCheck(String userInput, HashSet<String> dict)
    {
        if (dict == null)
        {
            return false;
        }
        return dict.contains(userInput);
    }

    private void playOneTurn()
    {
        /*
        Callable<String> k = () -> input.nextLine();
        long start = System.currentTimeMillis();
        String userInput;
        boolean valid;
        boolean correctInput = false;
        ExecutorService l = Executors.newFixedThreadPool(1);
        Future<String> g;

        System.out.println(this);
        g = l.submit(k);
        done: while(System.currentTimeMillis() - start < 5 * 1000)
        {
            do {
                valid = true;
                if (g.isDone()) {
                    try {
                        userInput = g.get();
                        if (userInputCheck(userInput)) {
                            correctInput = true;
                            break done;
                        }
                        else {
                            throw new IllegalArgumentException();
                        }
                    }
                    catch (InterruptedException | ExecutionException | IllegalArgumentException e) {
                        g = l.submit(k);
                        valid = false;
                    }
                }
            }
            while (!valid);
        }
        g.cancel(true);
         */

        System.out.println();
        System.out.println(this);

        boolean correctInput = userInputCheck(input.nextLine());

        if (currentPlayerIsAlive()) {
            if (correctInput) {
                changeLetterCombo();
                turnSwitch();
            }
            else {
                getCurrentPlayer().lifeLost();
                repeatWordCount++;
                if (repeatWordCount == 2)
                {
                    changeLetterCombo();
                    repeatWordCount = 0;
                }
                turnSwitch();
            }
        }
    }

    private boolean currentPlayerIsAlive()
    {
        return getCurrentPlayer().hasLives();
    }

    private void turnSwitch()
    {
        do
        {
            currentPlayer++;
            if (currentPlayer == playerList.size())
            {
                currentPlayer = 0;
            }
        } while ((winCheck() == -1) && !getCurrentPlayer().hasLives());
    }

    private HashSet<String> formDictSet()
    {
        try
        {
            File dictText = new File("C:\\Users\\Daniel Huang\\Documents\\GitHub\\BombParty\\BombParty\\src\\words_alpha.txt");
            Scanner reader = new Scanner(dictText);
            HashSet<String> output = new HashSet<>();
            while (reader.hasNextLine())
            {
                output.add(reader.nextLine());
            }
            reader.close();
            return output;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    private void changeLetterCombo()
    {
        letterCombo = generateNewLetterCombo();
    }

    private String generateNewLetterCombo()
    {
        String[] dictArray = DICTIONARY.toArray(new String[DICTIONARY.size()]);
        String selectedWord = "";

        int wordType = (int)(Math.random()*2);
        // 2 letters
        if (wordType == 0)
        {
            while (selectedWord.length() < 2)
            {
                int arrayIndex = (int)(Math.random() * dictArray.length);
                selectedWord = dictArray[arrayIndex];
            }
            int wordSubstringIndex = (int)(Math.random() * (selectedWord.length() - 1));
            return selectedWord.substring(wordSubstringIndex, wordSubstringIndex + 2);
        }
        // 3 letters
        else
        {
            while (selectedWord.length() < 3)
            {
                int arrayIndex = (int)(Math.random() * dictArray.length);
                selectedWord = dictArray[arrayIndex];
            }
            int wordSubstringIndex = (int)(Math.random() * (selectedWord.length() - 2));
            return selectedWord.substring(wordSubstringIndex, wordSubstringIndex + 3);
        }
    }

    public String getLetterCombo()
    {
        return letterCombo;
    }

    public Player getCurrentPlayer()
    {
        return playerList.get(currentPlayer);
    }

    public String toString()
    {
        return "Player: " + getCurrentPlayer().getName()
                + "\nLives: " + getCurrentPlayer().getLives()
                + "\nYour combination: " + letterCombo
                + "\nPlease type a word: ";
    }

    // precondition: at least one person has lives
    // postcondition: index of winner; if no winners, then return -1
    private int winCheck()
    {
        int winnerIndex = -1;
        for (int i = 0; i < playerList.size(); i++)
        {
            if (playerList.get(i).hasLives())
            {
                if (winnerIndex != -1)
                {
                    return -1;
                }
                else
                {
                    winnerIndex = i;
                }
            }
        }
        return winnerIndex;
    }

    public String printWin()
    {
        int winnerIndex = winCheck();
        return "Player " + playerList.get(winnerIndex).getName() + " wins!";
    }
}
