import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.*;

public class Game
{
    // lowest time interval possible
    // units: seconds
    private final long TIME_START;

    // range for randomly generated time from TIME_START
    // units: seconds
    private final long TIME_INTERVAL;

    // takes all player inputs
    private Scanner input;

    // dictionary that all user-submitted words are compared to
    private final HashSet<String> DICTIONARY;

    // set used to create new letterCombo
    private HashSet<String> comboSet;

    // letter combination that the player is prompted with
    // changes after one correct answer or two consecutive incorrect answers
    private String letterCombo;

    // index of current player playing
    private int currentPlayer;

    // counts how many times a letterCombo has been repeated
    private int repeatWordCount;

    // ArrayList of all players
    private ArrayList<Player> playerList;

    // tracks all used words; used words cannot be repeated
    private ArrayList<String> usedWordList;

    // precondition: initPlayers cannot be null
    public Game()
    {
        TIME_START = 5;
        TIME_INTERVAL = 10;

        input = new Scanner(System.in);
        DICTIONARY = formDictSet(3);

        letterCombo = "";
        comboSet = null;

        currentPlayer = 0;
        repeatWordCount = 0;

        playerList = new ArrayList<>();
        usedWordList = new ArrayList<>();
    }

    private void initiateGame(int playerCount) {
        for (int i = 0; i < playerCount; i++) {
            System.out.print("Enter a name: ");
            playerList.add(new Player(input.nextLine()));
        }

        System.out.print("Enter difficulty (1 = easy, 2 = medium, 3 = entire dictionary): ");
        comboSet = formDictSet(input.nextInt());
        input.nextLine();

        changeLetterCombo();
    }

    // code that runs play session given player count; allows for resetting game
    public void playSession() {
        System.out.print("Enter the number of players (2 or more): ");
        int playerCount = input.nextInt();
        input.nextLine();

        initiateGame(playerCount);

        String retry = "y";
        while (retry.equals("y")) {
            System.out.print("Type any key to start (first player goes first): ");
            input.nextLine();
            playOneGame();

            System.out.print("\nContinue playing? [y/n]: ");
            retry = input.nextLine();
            resetGame();
        }
        System.exit(0);
    }

    // plays actual game, with players swapping turns
    // includes timer
    private void playOneGame() {
        while (winCheck() == -1)
        {
            // variables need to be converted to milliseconds (*1000)
            // +1 required for interval to be inclusive from TIME_START to TIME_START + TIME_INTERVAL
            long interval = (long)(Math.random() * (TIME_INTERVAL * 1000) + 1) + (TIME_START * 1000);
            playOneTurn(interval);
        }
        System.out.print(printWin());
    }

    // resets game; players & difficulty kept
    private void resetGame() {
        for (Player player : playerList) {
            player.reset();
        }
        currentPlayer = 0;
        changeLetterCombo();
    }

    // returns true if userInput includes combination & is an actual word; false otherwise
    private boolean userInputCheck(String userInput)
    {
        return (comboCheck(userInput, letterCombo) && dictionaryCheck(userInput, DICTIONARY));
    }

    // returns true if userInput includes letterCombo; false otherwise
    private boolean comboCheck(String userInput, String letterCombo)
    {
        return userInput.indexOf(letterCombo) != -1;
    }

    // returns true if userInput is an actual word; false otherwise
    private boolean dictionaryCheck(String userInput, HashSet<String> dict)
    {
        return dict.contains(userInput);
    }

    // used during a player's turn; determines validity of answer & switches turn
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

        System.out.println();
        if (userInputCheck(userInput) && !isInUsedWordList(userInput) && timeInterval > userInterval) {
            System.out.println("Good answer!");
            usedWordList.add(userInput);
            changeLetterCombo();
            repeatWordCount = 0;
        }
        else {
            getCurrentPlayer().lifeLost();
            System.out.println(getCurrentPlayer().getName() + " lost a life!");
            if (!getCurrentPlayer().hasLives()) {
                System.out.println(getCurrentPlayer().getName() + " is eliminated!");
            }

            // only change letterCombo if it has been attempted twice
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

    // changes turn; skips over players with no lives
    private void turnSwitch()
    {
        do {
            currentPlayer++;
            if (currentPlayer == playerList.size()) {
                currentPlayer = 0;
            }
        } while ((winCheck() == -1) && !getCurrentPlayer().hasLives());
    }

    // type 1 = 1k, type 2 = 5k, type 3 = dictionary
    private HashSet<String> formDictSet(int type)
    {
        try
        {
            File dictText = null;
            if (type == 1) {
                dictText = new File("C:\\Users\\Daniel Huang\\Documents\\GitHub\\BombParty\\BombParty\\src\\1k.txt");
            }
            else if (type == 2) {
                dictText = new File("C:\\Users\\Daniel Huang\\Documents\\GitHub\\BombParty\\BombParty\\src\\5k.txt");
            }
            else if (type == 3) {
                dictText = new File("C:\\Users\\Daniel Huang\\Documents\\GitHub\\BombParty\\BombParty\\src\\dictionary.txt");
            }
            Scanner reader = new Scanner(dictText);
            HashSet<String> output = new HashSet<>();
            while (reader.hasNextLine())
            {
                output.add(reader.nextLine());
            }
            reader.close();
            return output;
        }
        // this code runs if the text file is not found
        catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    private void changeLetterCombo()
    {
        letterCombo = generateNewLetterCombo(comboSet);
    }

    // returns 2 or 3 letter substring from a random word
    private String generateNewLetterCombo(HashSet<String> input)
    {
        String[] comboArray = input.toArray(new String[input.size()]);
        String selectedWord = "";

        int wordType = (int)(Math.random()*2);
        // 2 letters
        if (wordType == 0)
        {
            while (selectedWord.length() < 2)
            {
                int arrayIndex = (int)(Math.random() * comboArray.length);
                selectedWord = comboArray[arrayIndex];
            }
            int wordSubstringIndex = (int)(Math.random() * (selectedWord.length() - 1));
            return selectedWord.substring(wordSubstringIndex, wordSubstringIndex + 2);
        }
        // 3 letters
        else
        {
            while (selectedWord.length() < 3)
            {
                int arrayIndex = (int)(Math.random() * comboArray.length);
                selectedWord = comboArray[arrayIndex];
            }
            int wordSubstringIndex = (int)(Math.random() * (selectedWord.length() - 2));
            return selectedWord.substring(wordSubstringIndex, wordSubstringIndex + 3);
        }
    }

    // returns index of winner; if no winners, then return -1
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

    // used when a player wins
    private String printWin()
    {
        int winnerIndex = winCheck();
        return "\nPlayer " + playerList.get(winnerIndex).getName() + " wins!";
    }

    public Player getCurrentPlayer()
    {
        return playerList.get(currentPlayer);
    }

    // used in playOneGame() method to show game state
    public String toString()
    {
        return "Player: " + getCurrentPlayer().getName()
                + "\nLives: " + getCurrentPlayer().getLives()
                + "\nYour combination: " + letterCombo
                + "\nPlease type a word: ";
    }


    // used for local multiplayer
    public static void main(String[] args) {
        Game myGame = new Game();
        myGame.playSession();
    }
}