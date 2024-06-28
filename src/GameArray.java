/**
 * This is the game array class of BilboardGameSelector.
 * This class defines a game array of all the games that exist in the club.
 * @author Deniz Sahin
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class GameArray {
    
    //variables
    Game[] games;

    public GameArray (String gameListFile)
    {
        this.CreateGameList(gameListFile);
        
    }

    /**
     * This private method creates the game list by reading the game list file
     * @param gameListFile .txt file with game info (copied from google sheets)
     *                  Column info: Name, MinPlayer, MaxPlayer, EvenPlCount, OptimalPlayer, PlayTime, Difficulty, Rating, Gameplay Type
     */
    private void CreateGameList(String gameListFile)
    {

        //#region Scanner Definition
        File listFile;
        Scanner listScanner;
        
        listFile = new File(gameListFile);
        
        try {
            listScanner = new Scanner(listFile);            
        } catch (FileNotFoundException e)
        {
            System.out.println("Game list file not found. File: " + gameListFile);
            //e.printStackTrace();
            System.exit(1);
            return;
        }
        //#endregion Scanner Definition


        //#region Games Array Definition
        int gameCount;
        for(gameCount = 0; listScanner.hasNextLine(); gameCount++)
        {
            listScanner.nextLine();
        }
        
        games = new Game[gameCount];

        listScanner.close();
        try {
            listScanner = new Scanner(listFile);    
        } catch (Exception e)
        {
            System.out.println("Game list not found on second definition. I don't even know how this can happen.");
            System.exit(1);
            listScanner.close();
            return;
        }

        //#endregion Games Array Definition
        

        //#region Game Creation By String Manipulation

        // Done for each game
        for (int i = 0; i < gameCount; i++)
        {
            // Obtaining the game variables
            String entireGame = listScanner.nextLine();

            String[] partsOfGame = entireGame.split("\t");

            Boolean evenPlayer = (partsOfGame[4].equals("Evet") || partsOfGame[4].equals("Yes"));
            
            // Finding the optimal player count array
            int[] opPlayerCount = {0};
            if(partsOfGame[5].contains(","))
            {
                String[] opPlStrings = partsOfGame[5].split(",");

                opPlayerCount = new int[opPlStrings.length];
                for(int j = 0; j < opPlStrings.length; j++)
                {
                    opPlayerCount[j] = Integer.parseInt(opPlStrings[j]);
                }
            }
            else if(partsOfGame[5].contains("-"))
            {
                String[] opPlStrings = partsOfGame[5].split("-");

                opPlayerCount = new int[Integer.parseInt(opPlStrings[1]) - Integer.parseInt(opPlStrings[0]) + 1];
                int opPlayerCur = Integer.parseInt(opPlStrings[0]);
                for(int j = 0; j < opPlayerCount.length; j++)
                {
                    opPlayerCount[j] = opPlayerCur + j;
                }
            }
            else
            {
                opPlayerCount = new int[1];
                opPlayerCount[0] = Integer.parseInt(partsOfGame[5]);
            }

            // Finding the play time
            int minTime, maxTime;
            if(partsOfGame[6].contains("-"))
            {
                String[] timeStrings = partsOfGame[6].split("-");
                minTime = Integer.parseInt(timeStrings[0]);
                maxTime = Integer.parseInt(timeStrings[1]);
            }
            else 
            {
                minTime = Integer.parseInt(partsOfGame[6]);
                maxTime = minTime;
            }

            // Removing the dots from the difficulty and rating
            partsOfGame[7] = partsOfGame[7].replace(".", "");
            int difficulty = Integer.parseInt(partsOfGame[7]);
            while(difficulty < 100)
            {
                difficulty = difficulty * 10;
            } 
            
            partsOfGame[8] = partsOfGame[8].replace(".", "");
            int rating = Integer.parseInt(partsOfGame[8]);
            while(rating <= 10)
            {
                rating = rating * 10;
            }

            this.games[i] = new Game(partsOfGame[0], Integer.parseInt(partsOfGame[1]), Integer.parseInt(partsOfGame[2]), Integer.parseInt(partsOfGame[3]), 
                                        evenPlayer, opPlayerCount, minTime, maxTime,
                                        difficulty, rating, partsOfGame[9]);
        }

        //#endregion Game Creation By String Manipulation
        
        listScanner.close();
    }

    /**
     * @return the amaount of games in the list
     */
    public int getGameAmount()
    {
        return games.length;
    }

    /**
     * This method returns the game at a specific index from the alphabetically sorted game array
     * @param index game position (start: 0)
     * @return game object at that position
     */
    public Game getGameAtIndex(int index)
    {
        if(index < getGameAmount())
        {
            return games[index];
        }

        return null;
    }

    /**
     * This method creates a copy of the array
     * @return New game array (opPlayerCount is bound do not change)
     */
    public Game[] getArray()
    {
        return games;
    }

}
