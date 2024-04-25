/**
 * This is a game class for the BilboradGameSelector.
 * This class defines a game object.
 * @author Deniz Sahin
 */


public class Game {

    public enum GameType { 
        Strategy, 
        Party, 
        Both;

        public static String enumStringAtPos(int pos)
        {
            switch (pos) {
                case 0:
                    return "Strategy";
                case 1:
                    return "Party";
                case 2:
                    return "Eighter";
                default:
                    break;
            }

            return "";
        }

        public static int enumIntFromString(String str)
        {
            switch (str) {
                case "Strategy":
                    return 0;
                case "Party":
                    return 1;
                case "Eighter":
                    return 2;
                default:
                    break;
            }

            return 0;
        }

        public static boolean includesGame(int selection, Game game)
        {
            if(selection == 2)
            {
                return true;
            } else if (selection == 1) {
                return (game.getType() == Party);
            } else if (selection == 0) {
                return (game.getType() == Strategy);
            }

            return false;
        }

    };

    // instance variables
    private String name;
    private int count;
    private int minPlayerCount, maxPlayerCount;
    private boolean evenPlayerCount;
    private int[] opPlayerCount;
    private int minPlayTime, maxPlayTime;
    private int difficulty, rating;
    private GameType type;


    /**
     * This constructor creates a game by the values provided.
     * @param gameName Name of the game
     * @param gameCount The count of how many games our club has
     * @param gameMinPlayerCount The minimum number of players needed
     * @param gameMaxPlayerCount The maximum number of players possible
     * @param gameEvenPlayerCount Is it mandatory to have an even number of players?
     * @param gameOpPlayerCount The optimal player count array including all the optimal values
     * @param gameMinPlayTime The minimum gameplay time
     * @param gameMaxPlayTime The maximum gameplay time
     * @param gameDifficulty The difficulty rating of the game out of 5 (BGG)
     * @param gameRating The rating of the game out of 10 (BGG)
     * @param gameType The type of the game: Strategy, Party or Both
     */
    public Game (String gameName,
                int gameCount, int gameMinPlayerCount, int gameMaxPlayerCount, boolean gameEvenPlayerCount,
                int[] gameOpPlayerCount, int gameMinPlayTime, int gameMaxPlayTime,
                int gameDifficulty, int gameRating,
                String gameType)
    {
        name = gameName;
        count = gameCount;
        minPlayerCount = gameMinPlayerCount;
        maxPlayerCount = gameMaxPlayerCount;
        evenPlayerCount = gameEvenPlayerCount;
        opPlayerCount = gameOpPlayerCount;
        minPlayTime = gameMinPlayTime;
        maxPlayTime = gameMaxPlayTime;

        if(gameDifficulty < 100 || gameDifficulty > 500)
        {
            System.out.println("Difficulty level of " + name + " was entered wrong. Difficulty: " + gameDifficulty);
            System.exit(2);
        }
        difficulty = gameDifficulty;

        if(gameRating < 10 || gameRating > 100)
        {
            System.out.println("Rating of " + name + " was entered wrong." + gameRating);
            System.exit(2);
        }
        rating = gameRating;

        if(gameType.equals("Strategy"))
        {
            type = GameType.Strategy;
        } else if (gameType.equals("Party"))
        {
            type = GameType.Party;
        } else if (gameType.equals("Both"))
        {
            type = GameType.Both;
        } else {
            System.out.println("Game type definition was wrong for " + name + ". Game type: " + gameType);
            System.exit(2);
        }
    }

    /**
     * Copy constructor for game class.
     * @param copied The game being coppied (opPlayerCount is bound do not change)
     */
    public Game (Game copied)
    {
        name = copied.name;
        count = copied.count;
        minPlayerCount = copied.minPlayerCount;
        maxPlayerCount = copied.maxPlayerCount;
        evenPlayerCount = copied.evenPlayerCount;
        opPlayerCount = copied.opPlayerCount;
        minPlayTime = copied.minPlayTime;
        maxPlayTime = copied.maxPlayTime;
        difficulty = copied.difficulty;
        rating = copied.rating;
        type = copied.type;
    }



    public String toString() {

        return "{" +
            " name='" + getName() + "'" +
            ", count='" + getCount() + "'" +
            ", minPlayerCount='" + getMinPlayerCount() + "'" +
            ", maxPlayerCount='" + getMaxPlayerCount() + "'" +
            ", evenPlayerCount='" + isEvenPlayerCount() + "'" +
            ", opPlayerCount='" + getOpPlayerCountStr() + "'" +
            ", minPlayTime='" + getMinPlayTime() + "'" +
            ", maxPlayTime='" + getMaxPlayTime() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            ", rating='" + getRating() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }


//#region Getters
    public String getName() {
        return this.name;
    }

    public int getCount() {
        return this.count;
    }

    public int getMinPlayerCount() {
        return this.minPlayerCount;
    }

    public int getMaxPlayerCount() {
        return this.maxPlayerCount;
    }

    public boolean isEvenPlayerCount() {
        return this.evenPlayerCount;
    }

    public int[] getOpPlayerCount() {
        return this.opPlayerCount;
    }

    /**
     * @return Formatted stirng of opPlayerCount
     */
    public String getOpPlayerCountStr() {
        String str = "[ " + String.valueOf(opPlayerCount[0]);
        
        for (int i = 1; i < opPlayerCount.length; i++)
        {
            str += (", " + opPlayerCount[i]);
        }

        str +=  "]";

        return str;
    }

    public int getMinPlayTime() {
        return this.minPlayTime;
    }

    public int getMaxPlayTime() {
        return this.maxPlayTime;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public int getRating() {
        return this.rating;
    }

    public GameType getType() {
        return this.type;
    }
//#endregion Getters

}
