import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Game
{
    private final HashSet<String> DICTIONARY;

    private String letterCombo;
    private ArrayList<Player> playerList;
    private int currentPlayer;

    // precondition: initPlayers cannot be null
    public Game(Player[] initPlayers)
    {
        DICTIONARY = formDictSet();
        letterCombo = generateNewLetterCombo();
        currentPlayer = 0;
        playerList = new ArrayList<>();
        for (Player user : initPlayers)
        {
            playerList.add(user);
        }
    }

    public boolean userInputCheck(String userInput)
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
        return "Player: " + playerList.get(currentPlayer).getName()
                + "\n\nYour combination: " + letterCombo + "\nPlease type a word: ";
    }
}
