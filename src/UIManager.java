/**
 * This is the class managing the UI dependancies of the Bilboard Game Selector project
 * @author Deniz Sahin
*/
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class UIManager {

    //#region Variables

    //screen size variables
    private static int screenWidth;
    private static int screenHeight;
    

    //defining private variables
    private static JFrame startFrame;
    private static JFrame gameViewFrame;

    //Fonts
    public final static Font TITLE_FONT = new Font("Cooper Black", 1, 90); //Britannic Bold, Cooper Black
    public final static Font GAME_FONT = new Font(TITLE_FONT.getFontName(), 1, 40);
    public final static Font TO_START_FONT = new Font(TITLE_FONT.getFontName(), 0, 25);
    public final static Font SMALLER_GAME_FONT = new Font(GAME_FONT.getFontName(), 0, 20);


    public final static Color BUTTON_COLOR = new Color(0x38225b);
    public final static Color BACKGROUND_COLOR = new Color(0xe3acbc);
    public final static Color BUTTON_TEXT_COLOR = new Color(0xe3acbc);
    public final static Color TITLE_COLOR = new Color(0x38225b);
    public final static Color GAME_FONT_COLOR = new Color(0x38225b);

    private static GameArray ga;
    private static Game[] curGames;


    //#endregion Variables

    UIManager(GameArray gameArray)
    {
        ga = gameArray;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        for(String str : ge.getAvailableFontFamilyNames())
        {
            System.out.println(str);
        }

        gameViewFrame = null;

        screenHeight = ge.getMaximumWindowBounds().height;
        screenWidth = ge.getMaximumWindowBounds().width;

        createStartFrame();
    }

    /**
     * Creates the starting frame for the selector.
     */
    public void createStartFrame()
    {
        //starting frame
        startFrame = new JFrame();
        startFrame.setTitle("Bilboard Game Selector");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setUndecorated(true);
        startFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        startFrame.getContentPane().setBackground(BACKGROUND_COLOR);

        Container startSelBtContainer = new Container();

        int leftBorder = screenWidth * 57 / 1000;

        JLabel titleLabel = new JLabel("Bilboard Game Selector v1.1", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        startSelBtContainer.add(titleLabel);

        Font selButtFont = new Font(GAME_FONT.getFontName(), 1, 25);
        int selButtWidth = screenWidth * 42 / 100;
        int selButtHeight = screenHeight * 27 / 100;

        // Algorithm selection start button
        JButton prefSelButton = new JButton("Find a Game");
        prefSelButton.setBackground(BUTTON_COLOR);
        prefSelButton.setForeground(BUTTON_TEXT_COLOR);
        prefSelButton.setBounds(leftBorder, 220, selButtWidth, selButtHeight);
        prefSelButton.addActionListener(new prefSelListener());
        prefSelButton.setFont(selButtFont);
        startSelBtContainer.add(prefSelButton);

        // Random game button
        JButton randGameButton = new JButton("Random Game List");
        randGameButton.setBackground(BUTTON_COLOR);
        randGameButton.setForeground(BUTTON_TEXT_COLOR);
        randGameButton.addActionListener(new randGameListener());
        randGameButton.setBounds(screenWidth - selButtWidth - leftBorder, 220, selButtWidth, selButtHeight);
        randGameButton.setFont(selButtFont);
        startSelBtContainer.add(randGameButton);

        // Entire game list button
        JButton gameListButton = new JButton("See Entire Game List");
        gameListButton.setBackground(BUTTON_COLOR);
        gameListButton.setForeground(BUTTON_TEXT_COLOR);
        gameListButton.addActionListener(new gameListListener());
        gameListButton.setBounds(leftBorder, 290 + selButtHeight, selButtWidth, selButtHeight);
        gameListButton.setFont(selButtFont);
        startSelBtContainer.add(gameListButton);
        
        // How to use button
        JButton howToButton = new JButton("How To Use?");
        howToButton.setBackground(BUTTON_COLOR);
        howToButton.setForeground(BUTTON_TEXT_COLOR);
        howToButton.addActionListener(new howToListener());
        howToButton.setBounds(screenWidth - selButtWidth - leftBorder,  290 + selButtHeight, selButtWidth, selButtHeight);
        howToButton.setFont(selButtFont);
        startSelBtContainer.add(howToButton);

        // Go back to game view button
        JButton goBackButton = new JButton("<-- Go Back");
        goBackButton.setBackground(BUTTON_COLOR);
        goBackButton.setForeground(BUTTON_TEXT_COLOR);
        goBackButton.setFont(TO_START_FONT);
        goBackButton.addActionListener(new goBackListener());
        goBackButton.setBounds(leftBorder, 340 + (2 * selButtHeight), 190, 30);
        startSelBtContainer.add(goBackButton);

        // Source code labels
        JLabel sourceLabel = new JLabel("Source code for this project is available at:", SwingConstants.RIGHT);
        sourceLabel.setFont(SMALLER_GAME_FONT);
        sourceLabel.setForeground(TITLE_COLOR);
        sourceLabel.setBounds(screenWidth - leftBorder - 600, 340 + (2 * selButtHeight), 600, 40);
        startSelBtContainer.add(sourceLabel);

        JLabel repoLabel = new JLabel("github.com/Deniz-Sahin-567/BilboardGameSelector", SwingConstants.RIGHT);
        repoLabel.setFont(SMALLER_GAME_FONT);
        repoLabel.setForeground(TITLE_COLOR);
        repoLabel.setBounds(screenWidth - leftBorder - 600, 380 + (2 * selButtHeight), 600, 40);
        startSelBtContainer.add(repoLabel);
        
        startFrame.add(startSelBtContainer);

        startFrame.setVisible(true);

    }

    /**
     * This method initializes the main frame where the games are visible
     */
    private void initializeGameViewFrame()
    {
        if(gameViewFrame != null)
        {    
            gameViewFrame.dispose();
        }

        //mainFrame
        gameViewFrame = new JFrame();
        gameViewFrame.setTitle("Bilboard Game Selector");
        gameViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameViewFrame.setUndecorated(true);
        gameViewFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameViewFrame.getContentPane().setBackground(BACKGROUND_COLOR);
    }

    /**
     * This method creates the view all games frame and makes it visible.
     */
    private void createGameListFrame()
    {
        curGames = ga.copyArray();

        initializeGameViewFrame();

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("Our Full Game List", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, 1600, 200);
        mainContainer.add(titleLabel);

        createGameViewObjects(mainContainer);
        
        gameViewFrame.add(mainContainer);

        gameViewFrame.setVisible(true);


    }

    /**
     * This method creates a randomized list of games whenever the random game button is pressed
     */
    private void createRandomGameFrame()
    {
        //Randomizing the array
        curGames = new Game[15];

        Random rand = new Random();
        boolean[] alrSellected = new boolean[ga.getGameAmount()];
        for(int i = 0; i < 15; i++)
        {
            alrSellected[i] = false;
        }
            
        for(int i = 0; i <15; i++)
        {
            int randInt = rand.nextInt(ga.getGameAmount());
            if(alrSellected[randInt] == false)
            {
                curGames[i] = ga.getGameAtIndex(randInt);
                alrSellected[randInt] = true;
            } else {
                i--;
            }
        }

        initializeGameViewFrame();

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("A List Of Random Games", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        mainContainer.add(titleLabel);

        createGameViewObjects(mainContainer);
        
        gameViewFrame.add(mainContainer);

        //gs[0].setFullScreenWindow(startFrame);
        gameViewFrame.setVisible(true);


    }

    private void createGameSelectionFrame()
    {
        GameSelector gameSelector = new GameSelector(ga, this);
    }

    private void createHowToFrame()
    {
        int leftBorder = 60;
        int curHeight = 0;
        
        initializeGameViewFrame();

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("How To Use Bilboard Game Selector?", SwingConstants.CENTER);
        titleLabel.setFont(new Font(TITLE_FONT.getFontName(), 1, 70));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, curHeight, screenWidth, 200);
        mainContainer.add(titleLabel);

        curHeight += titleLabel.getHeight();

        JLabel step1Label = new JLabel("1) Go back to the start screen and press on the find a game button.", SwingConstants.LEFT);
        step1Label.setFont(GAME_FONT);
        step1Label.setForeground(GAME_FONT_COLOR);
        step1Label.setBounds(leftBorder, curHeight, screenWidth, 70);
        mainContainer.add(step1Label);

        curHeight += step1Label.getHeight();

        JLabel step2Label = new JLabel("2) Answer the questions based on your preferences.", SwingConstants.LEFT);
        step2Label.setFont(GAME_FONT);
        step2Label.setForeground(GAME_FONT_COLOR);
        step2Label.setBounds(leftBorder, curHeight, screenWidth, 70);
        mainContainer.add(step2Label);

        curHeight += step2Label.getHeight();

        JLabel step3Label = new JLabel("3) If you don't know how to play the games ask for help", SwingConstants.LEFT);
        step3Label.setFont(GAME_FONT);
        step3Label.setForeground(GAME_FONT_COLOR);
        step3Label.setBounds(leftBorder, curHeight + 10, screenWidth,50);
        mainContainer.add(step3Label);

        curHeight += step3Label.getHeight();

        JLabel step3_2Label = new JLabel("from an organisation team member.", SwingConstants.LEFT);
        step3_2Label.setFont(GAME_FONT);
        step3_2Label.setForeground(GAME_FONT_COLOR);
        step3_2Label.setBounds(leftBorder + 50, curHeight, screenWidth,50);
        mainContainer.add(step3_2Label);

        curHeight += step3_2Label.getHeight();

        JLabel step3ExLabel = new JLabel("If you don't know who is an OT member, ask around. There should be at least one of us around here somewhere?", SwingConstants.LEFT);
        step3ExLabel.setFont(SMALLER_GAME_FONT);
        step3ExLabel.setForeground(GAME_FONT_COLOR);
        step3ExLabel.setBounds(leftBorder + 100, curHeight - 5, screenWidth, 30);
        mainContainer.add(step3ExLabel);

        curHeight += step3ExLabel.getHeight();

        JLabel step3Ex2Label = new JLabel("Also, we don't know some of the games either so don't get your hopes too high. :)", SwingConstants.LEFT);
        step3Ex2Label.setFont(SMALLER_GAME_FONT);
        step3Ex2Label.setForeground(GAME_FONT_COLOR);
        step3Ex2Label.setBounds(leftBorder + 100, curHeight - 10, screenWidth, 30);
        mainContainer.add(step3Ex2Label);

        curHeight += step3Ex2Label.getHeight();

        JLabel step4Label = new JLabel("4) Now that you have hopefully found a game that's for your liking,", SwingConstants.LEFT);
        step4Label.setFont(GAME_FONT);
        step4Label.setForeground(GAME_FONT_COLOR);
        step4Label.setBounds(leftBorder, curHeight + 10, screenWidth, 50);
        mainContainer.add(step4Label);

        curHeight += step4Label.getHeight();

        JLabel step4_2Label = new JLabel("have fun playing it!", SwingConstants.LEFT);
        step4_2Label.setFont(GAME_FONT);
        step4_2Label.setForeground(GAME_FONT_COLOR);
        step4_2Label.setBounds(leftBorder + 50, curHeight, screenWidth, 50);
        mainContainer.add(step4_2Label);

        curHeight += step4_2Label.getHeight();

        JButton backToStartButton = new JButton("<-- Selection");
        backToStartButton.addActionListener(new toStartListener());
        backToStartButton.setBounds(leftBorder, curHeight + 40, 200, 50);
        backToStartButton.setFont(TO_START_FONT);
        backToStartButton.setBackground(BUTTON_COLOR);
        backToStartButton.setForeground(BUTTON_TEXT_COLOR);
        mainContainer.add(backToStartButton);

        gameViewFrame.add(mainContainer);

        //gs[0].setFullScreenWindow(startFrame);
        gameViewFrame.setVisible(true);
    }

    //#region Listeners

    /**
     * This is an inner class.
     * Change Listener class for changing pages.
     * This class is invoked to change the pages of the games list.
     */
    private class changePageListener implements ActionListener{

        private int page;
        private Container mainContainer;
        private int nextButtonHash;
        private int maxPages, gamesPerPage;

        public changePageListener(int page, Container mContainer, int nextHash, int gamesPerPage)
        {
            this.page = page;
            this.mainContainer = mContainer;
            this.nextButtonHash = nextHash;
            this.maxPages = ((curGames.length-1) /gamesPerPage);
            this.gamesPerPage = gamesPerPage;
        }

        public void actionPerformed(ActionEvent e) {
            mainContainer.getComponent(1).setVisible(false);
            mainContainer.remove(1);
            if(e.getSource().hashCode() == nextButtonHash)
            {
                page += 1;
                if(page == maxPages)
                {
                    mainContainer.getComponent(1).setVisible(false);
                }
                
                if(page == 1)
                {
                    mainContainer.getComponent(2).setVisible(true);
                }
            }
            else{
                page -= 1;
                if(page == 0)
                {
                    mainContainer.getComponent(2).setVisible(false);
                }
                
                if(page == maxPages-1)
                {
                    mainContainer.getComponent(1).setVisible(true);
                }
            }

            //System.out.println("Page is at: " + page);
            mainContainer.add(createGameListContainer(page, gamesPerPage), 1);

            mainContainer.validate();
        }
    }

    private class gameListListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            createGameListFrame();
        }
    }

    private class prefSelListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            createGameSelectionFrame();
        }
    }

    private class toStartListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            gameViewFrame.setVisible(false);
        }
    }

    private class goBackListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(gameViewFrame != null)
            {    
                gameViewFrame.setVisible(true);
            }
        }
    }

    private class randGameListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            createRandomGameFrame();
        }
    }

    private class howToListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            createHowToFrame();
        }
    }

    //#endregion Listeners


    //#region Game View Methods
    
    /**
     * This method makes the game list visible from the curGames object
     * @param page What page number of the list to make visible
     * @param gamesPerPage How many games per page to use
     * @return The container containing the visible games
     */
    private Container createGameListContainer(int page, int gamesPerPage)
    {   
        int gameNameHeight = 80;
        int leftBorder = 50;
        int gameNameWidth = ((screenWidth - (2*leftBorder))/3);
     
        Container gameListContainer = new Container();
        gameListContainer.setBounds(leftBorder, 200, screenWidth - (2 *leftBorder) , 950);

        if(curGames == null)
        {
            JLabel gameLabel = new JLabel( "No games in our list fit your preference.", SwingConstants.CENTER);
            gameLabel.setFont(GAME_FONT);
            gameLabel.setForeground(GAME_FONT_COLOR);
            gameLabel.setBounds( 0, 250, screenWidth - (2*leftBorder), gameNameHeight);
            gameListContainer.add(gameLabel);
            return gameListContainer;
        }

        //finding out how many games to print on this page
        int listLength = (curGames.length / (gamesPerPage*(page+1))); 
        listLength = ((listLength) != 0) ? gamesPerPage : (curGames.length % gamesPerPage);

        //System.out.println("List length is: " + listLength);

        for(int i = 0; i < listLength; i++)
        {
            Game curGame = curGames[i + (gamesPerPage*page)];
            String gameName = curGame.getName();
            int gameNum = (page*gamesPerPage) + i;

            //System.out.println("Added game " + i + gameName);

            JLabel gameLabel = new JLabel( (gameNum+1) + ". " + gameName);
            gameLabel.setFont(GAME_FONT);
            gameLabel.setForeground(GAME_FONT_COLOR);
            gameLabel.setBounds((gameNameWidth) * (i / (gamesPerPage/3)), (gameNameHeight+5) *(i % (gamesPerPage/3)), gameNameWidth - 10, gameNameHeight);
            gameListContainer.add(gameLabel);
        }

        return gameListContainer;
    }
    
    /**
     * This method creates the objects nessesary to view the game list:
     * The container showing the games, the page chage buttons, back to start button.
     * @param mainContainer the container these objects are added to.
     */
    private void createGameViewObjects(Container mainContainer)
    {
        int gamesPerPage = 15;
        
        int leftBorder = 50;

        mainContainer.add(createGameListContainer(0, gamesPerPage));
        
        Font listButtonFont = new Font(TITLE_FONT.getFontName(), 1, 40);

        if(curGames != null)
        {
            //Creating the next and previous page controllers using the same listener
            JButton nextPageButton = new JButton(">");
            changePageListener cpListener = new changePageListener(0, mainContainer, nextPageButton.hashCode(), gamesPerPage);
            nextPageButton.addActionListener(cpListener);
            nextPageButton.setBounds((screenWidth / 2) + 10, 670, 100, 50);
            nextPageButton.setFont(listButtonFont);
            nextPageButton.setBackground(BUTTON_COLOR);
            nextPageButton.setForeground(BUTTON_TEXT_COLOR);
            mainContainer.add(nextPageButton);

            JButton prevPageButton = new JButton("<");
            prevPageButton.addActionListener(cpListener);
            prevPageButton.setBounds((screenWidth / 2) - 110, 670, 100, 50);
            prevPageButton.setFont(listButtonFont);
            prevPageButton.setBackground(BUTTON_COLOR);
            prevPageButton.setForeground(BUTTON_TEXT_COLOR);
            mainContainer.add(prevPageButton);

            //setting the buttons invisible if no other pages are available        
            prevPageButton.setVisible(false);
            if(curGames.length <= gamesPerPage)
            {
                nextPageButton.setVisible(false);
            }
            
        }

        JButton backToStartButton = new JButton("<-- Selection");
        backToStartButton.addActionListener(new toStartListener());
        backToStartButton.setBounds(leftBorder, 670, 200, 50);
        backToStartButton.setFont(TO_START_FONT);
        backToStartButton.setBackground(BUTTON_COLOR);
        backToStartButton.setForeground(BUTTON_TEXT_COLOR);
        mainContainer.add(backToStartButton);

    }
    
    /**
     * This method is called from the game selector class.
     * This method creates a game list
     * @param sortedGames the games sorted according to preferences
     */
    public void gameSelectionDone(Game[] sortedGames)
    {
        curGames = sortedGames;

        initializeGameViewFrame();

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("Games Best Fitting Your Preferences", SwingConstants.CENTER);
        titleLabel.setFont(new Font(TITLE_FONT.getFontName(), 1, 70));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        mainContainer.add(titleLabel);

        createGameViewObjects(mainContainer);
        
        gameViewFrame.add(mainContainer);

        gameViewFrame.setVisible(true);
    }

    //#endregion Game View Methods

}
