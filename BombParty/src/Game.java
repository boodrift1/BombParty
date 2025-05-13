import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Game
{
    private final HashSet<String> DICTIONARY;

    private Scanner input;
    private String letterCombo;
    private ArrayList<Player> playerList;
    private ArrayList<String> usedWordList;
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
        usedWordList = new ArrayList<>();
        initiateGame();
    }

    public void play()
    {
        while (winCheck() == -1)
        {
            // interval between 5 and 15 seconds
            long interval = (long)(Math.random()*10001) + 5000;
            playOneTurn(interval);
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

        System.out.print("Type any key to start the game (first player goes first): ");
        input.nextLine();
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

    private void playOneTurn(long timeInterval)
    {
        System.out.println();
        System.out.print(this);
        long start = System.currentTimeMillis();
        String userInput = input.nextLine();
        long userInterval = System.currentTimeMillis() - start;

        while ((!userInputCheck(userInput) || isInUsedWordList(userInput)) && timeInterval > userInterval) {
            System.out.print("Try again: ");
            userInput = input.nextLine();
            userInterval = System.currentTimeMillis() - start;
        }

        if (userInputCheck(userInput) && !isInUsedWordList(userInput) && timeInterval > userInterval) {
            System.out.println();
            System.out.println("Good answer!");
            usedWordList.add(userInput);
            changeLetterCombo();
        }
        else {
            System.out.println();
            System.out.println("You lost a life!");
            getCurrentPlayer().lifeLost();
            repeatWordCount++;
            if (repeatWordCount == 2)
            {
                changeLetterCombo();
                repeatWordCount = 0;
            }
        }
        turnSwitch();
    }

    // return true if input is in list; return false if otherwise
    private boolean isInUsedWordList(String input)
    {
        for (String word : usedWordList) {
            if (input.equals(word)) {
                return true;
            }
        }
        return false;
    }

    private void turnSwitch()
    {
        do {
            currentPlayer++;
            if (currentPlayer == playerList.size()) {
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

    private Player getCurrentPlayer()
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

    private String printWin()
    {
        int winnerIndex = winCheck();
        return "\nPlayer " + playerList.get(winnerIndex).getName() + " wins!";
    }
}
