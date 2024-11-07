# BilboardGameSelector
Board Game Selection Algorithm and Java Program


## How to use this program
In order to use this program: 
- Compile the files in the src directory into a .jar file. (Or download the executable from the release files)
- Add the file named GameList.txt into the same directory as your executable.
- Add a folder named resources in the directory.
  - Add a folder named backgrounds and add the three backgrounds in there.
  - Add a folder named images and add the game images in there.
- Run the executable.

## How to change the game list
In order to run the program with a different game list:
- Follow the steps in the how to use this program.
- Change the GameList.txt file's contents as follows:
  - On each line seperated by tabs include the following information:
    - Game Name: Preferably use only english letters or the outputs may be unreadable
    - Game copy count: A random number will do, currently not used in code
    - Minimum player count: integer
    - Maximum player count: integer
    - Is an even number of players mandatory: Yes/No
    - Optimal player count: integer OR integer-integer OR integer,integer
    - Play time: integer OR integer-integer
    - Difficulty: decimal point number(.) 1-5 with at most 2 points of precision
    - Rating: decimal point number(.) 1-10 with at most 2 points of precision
    - Type: Strategy OR Party OR Both
    - Subtype: Any strings divided by forward slashes '/'
    - Explanation: A short explanation of what the game is about.
> [!TIP]                
> The formatting can be obtained by using a google-sheets with each piece of information seperated to a different cell and then the entire document copy-pasted. (Do not copy the tag row.)

## Copying of the code
This code is licensed under the GNU GENERAL PUBLIC LICENSE VERSION 3.
Before copying this code refer to said license.

