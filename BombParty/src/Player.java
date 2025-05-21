import java.util.HashMap;

public class Player
{
    // determines number of lives player starts out with
    private final int START_LIVES;

    // determines max number of lives player can have
    private final int MAX_LIVES;

    // number of lives for this player
    private int lives;

    // name of this player
    private String name;

    // list of used letters that determine whether a player gains a life
    // includes all letters in the alphabet except for k, w, x, y, & z (uncommon letters)
    // true letters = have not been used in user input
    private HashMap<String, Boolean> bonusMap;

    public Player(String initName)
    {
        START_LIVES = 2;
        MAX_LIVES = 2;
        name = initName;
        lives = START_LIVES;

        bonusMap = new HashMap<>();
        bonusMap.put("a", false);
        bonusMap.put("b", false);
        bonusMap.put("c", false);
        bonusMap.put("d", false);
        bonusMap.put("e", false);
        bonusMap.put("f", false);
        bonusMap.put("g", false);
        bonusMap.put("h", false);
        bonusMap.put("i", false);
        bonusMap.put("j", false);
        bonusMap.put("l", false);
        bonusMap.put("m", false);
        bonusMap.put("n", false);
        bonusMap.put("o", false);
        bonusMap.put("p", false);
        bonusMap.put("q", false);
        bonusMap.put("r", false);
        bonusMap.put("s", false);
        bonusMap.put("t", false);
        bonusMap.put("u", false);
        bonusMap.put("v", false);
    }

    public void reset() {
        lives = START_LIVES;
        bonusReset();
    }

    public boolean hasLives()
    {
        return !(lives == 0);
    }

    // returns true if there is a false bonus letter in input
    // returns false if bonus letter is not found or bonus letter is true
    // chosen bonus letter is changed to true
    public boolean bonusLetterCheck(String input) {
        for (String letter : bonusMap.keySet()) {
            // checks if input contains letter
            if (input.indexOf(letter) != -1) {
                // checks if letter is false
                if (bonusMap.get(letter).equals(false)) {
                    // put method overwrites current letter
                    bonusMap.put(letter, true);
                    return true;
                }
            }
        }
        return false;
    }

    // returns true if all letters in bonusMap are true
    public boolean eligibleForBonusLife() {
        for (String letter : bonusMap.keySet()) {
            if (bonusMap.get(letter).equals(false)) {
                return false;
            }
        }
        return true;
    }

    public void lifeLost()
    {
        if (hasLives())
        {
            lives--;
        }
    }

    // returns true if life is gained
    // returns false if life is not gained due to MAX_LIVES check
    public boolean lifeGained() {
        if (lives != MAX_LIVES)
        {
            lives++;
            return true;
        }
        return false;
    }

    public void bonusReset() {
        bonusMap.put("a", false);
        bonusMap.put("b", false);
        bonusMap.put("c", false);
        bonusMap.put("d", false);
        bonusMap.put("e", false);
        bonusMap.put("f", false);
        bonusMap.put("g", false);
        bonusMap.put("h", false);
        bonusMap.put("i", false);
        bonusMap.put("j", false);
        bonusMap.put("l", false);
        bonusMap.put("m", false);
        bonusMap.put("n", false);
        bonusMap.put("o", false);
        bonusMap.put("p", false);
        bonusMap.put("q", false);
        bonusMap.put("r", false);
        bonusMap.put("s", false);
        bonusMap.put("t", false);
        bonusMap.put("u", false);
        bonusMap.put("v", false);
    }

    public int getLives()
    {
        return lives;
    }
    public String getName()
    {
        return name;
    }

    // returns false letters in bonusMap
    public String getBonusLetters()
    {
        String output = "";
        for (String letter : bonusMap.keySet())
        {
            if (bonusMap.get(letter).equals(false))
            {
                output += letter + " ";
            }
        }
        return output;
    }
}
