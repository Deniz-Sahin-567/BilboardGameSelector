/**
 * This is the class managing the UI dependancies of the Bilboard Game Selector project
 * @author Deniz Sahin
*/
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class UIManager {

    //#region Variables

    //screen size variables
    private static int screenWidth;
    private static int screenHeight;

    //defining private variables
    private static JFrame startFrame;
    private static JFrame gameViewFrame;
    public static listTimerListener timerAction;
    private static boolean selectionBeingMade;
    private static boolean singleGameView;
    private static int randomizedGame;
    private static int gamesInList;

    //Fonts
    public final static Font TITLE_FONT = new Font("Cooper Black", 1, 90); //Britannic Bold, Cooper Black
    public final static Font GAME_FONT = new Font(TITLE_FONT.getFontName(), 1, 38);
    public final static Font TO_START_FONT = new Font(TITLE_FONT.getFontName(), 0, 25);
    public final static Font SMALLER_GAME_FONT = new Font(GAME_FONT.getFontName(), 0, 20);

    public final static Color BUTTON_COLOR = new Color(0x38225b);
    public final static Color BACKGROUND_COLOR = new Color(0xe3acbc);
    public final static Color BUTTON_TEXT_COLOR = new Color(0xe3acbc);
    public final static Color TITLE_COLOR = new Color(0x38225b);
    public final static Color GAME_FONT_COLOR = new Color(0x38225b);

    //Functional variables
    private static GameArray ga;
    private static Game[] curGames;
    private static int timerDelay;


    //#endregion Variables

    UIManager(GameArray gameArray)
    {
        ga = gameArray;
        
        gameViewFrame = null;

        timerDelay = 10;
        timerAction = new listTimerListener();
        new Timer(timerDelay * 1000, timerAction).start();

        selectionBeingMade = false;
        singleGameView = false;
        randomizedGame = -1;
        gamesInList = 15;

        screenHeight = App.getScreenHeight();
        screenWidth = App.getScreenWidth();

        System.out.println("Screen height is: " + screenHeight + " and screen width is: " + screenWidth);

        createStartFrame();
    }

    //#region Frame Creation

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

        JLabel titleLabel = new JLabel("Bilboard Game Selector v2.0-a", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        startSelBtContainer.add(titleLabel);

        Font selButtFont = new Font(GAME_FONT.getFontName(), 1, 40);
        int selButtWidth = screenWidth * 42 / 100;
        int selButtHeight = screenHeight * 27 / 100;

        // Algorithm selection start button
        JButton prefSelButton = createButton("Find a Game", new prefSelListener(), selButtFont, BUTTON_COLOR, BUTTON_TEXT_COLOR);
        prefSelButton.setBounds(leftBorder, 220, selButtWidth, selButtHeight);
        startSelBtContainer.add(prefSelButton);

        // Random game button
        JButton randGameButton = createButton("A Random Game", new randGameListener(), selButtFont, BUTTON_COLOR, BUTTON_TEXT_COLOR);
        randGameButton.setBounds(screenWidth - selButtWidth - leftBorder, 220, selButtWidth, selButtHeight);
        startSelBtContainer.add(randGameButton);

        // Entire game list button
        JButton gameListButton = createButton("See Entire Game List", new gameListListener(), selButtFont, BUTTON_COLOR, BUTTON_TEXT_COLOR);
        gameListButton.setBounds(leftBorder, 290 + selButtHeight, selButtWidth, selButtHeight);
        startSelBtContainer.add(gameListButton);
        
        // How to use button
        JButton howToButton = createButton("How to Use?", new howToListener(), selButtFont, BUTTON_COLOR, BUTTON_TEXT_COLOR);
        howToButton.setBounds(screenWidth - selButtWidth - leftBorder,  290 + selButtHeight, selButtWidth, selButtHeight);
        startSelBtContainer.add(howToButton);

        // Go back to game view button
        JButton goBackButton = createButton("<-- Go Back", new goBackListener(), TO_START_FONT, BUTTON_COLOR, BUTTON_TEXT_COLOR);
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
        randomizedGame = -1;
        singleGameView = false;
        curGames = ga.getArray();

        initializeGameViewFrame();

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("Our Full Game List", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, 1600, 200);
        mainContainer.add(titleLabel);

        mainContainer.add(createGameViewObjects());
        
        gameViewFrame.add(mainContainer);

        gameViewFrame.setVisible(true);


    }

    /**
     * This method creates a randomized list of games whenever the random game button is pressed
     */
    private void createRandomGameFrame()
    {
        singleGameView = true;
        randomizedGame = 0;
        int gameCount = ga.getGameAmount();
        //Randomizing the array
        curGames = new Game[15];

        Random rand = new Random();
        boolean[] alrSellected = new boolean[gameCount];
        for(int i = 0; i < gameCount; i++)
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

        JLabel titleLabel = new JLabel("A Random Game", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        mainContainer.add(titleLabel);

        mainContainer.add(createGameViewObjects());
        
        gameViewFrame.add(mainContainer);

        //gs[0].setFullScreenWindow(startFrame);
        gameViewFrame.setVisible(true);


    }

    private void createGameSelectionFrame()
    {
        new GameSelector(ga, this);
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

    //#endregion Frame Creation

    //#region Listeners

    /**
     * This is an inner class.
     * Change Listener class for changing pages.
     * This class is invoked to change the pages of the games list.
     */
    private class changePageListener implements ActionListener{

        public static int page;
        private Container mainContainer;
        private int nextButtonHash;
        private int maxPages, gamesPerPage;

        public changePageListener(int startPage, Container mContainer, int nextHash, int gamesPerPage)
        {
            page = startPage;
            this.mainContainer = mContainer;
            this.nextButtonHash = nextHash;
            this.maxPages = ((curGames.length-1) /gamesPerPage);
            this.gamesPerPage = gamesPerPage;
        }

        public void actionPerformed(ActionEvent e) {

            timerAction.resetTimer();

            //if randomizer is used
            if(randomizedGame != -1)
            {
                mainContainer.getComponent(0).setVisible(false);
                mainContainer.remove(0);

                if(page < maxPages)
                {
                    page++;
                    mainContainer.add(createSingleGameView(page), 0);
                } else {
                    Random rand = new Random();
                    boolean selected = false;
                    while(!selected)
                    {
                        selected = true;
                        int gameNum = rand.nextInt(ga.getGameAmount());
                        Game game = ga.getGameAtIndex(gameNum);
                        for(int i = 0; i < 15; i++)
                        {
                            if(game == curGames[i])
                            {
                                selected = false;
                            }
                        }

                        if(selected)
                        {
                            curGames[randomizedGame] = game;
                        }
                    }

                    mainContainer.add(createSingleGameView(randomizedGame), 0);
                    
                    randomizedGame = (randomizedGame + 1) % 15;
                }

                mainContainer.validate();
                return;
            }

            mainContainer.getComponent(0).setVisible(false);
            mainContainer.remove(0);
            if(e.getSource().hashCode() == nextButtonHash)
            {
                page += 1;
                if(page == maxPages)
                {
                    mainContainer.getComponent(0).setVisible(false);
                }
                
                if(page == 1)
                {
                    mainContainer.getComponent(1).setVisible(true);
                }
            }
            else{
                page -= 1;
                if(page == 0)
                {
                    mainContainer.getComponent(1).setVisible(false);
                }
                
                if(page == maxPages-1)
                {
                    mainContainer.getComponent(0).setVisible(true);
                }
            }

            //Changing the page num label
            JLabel pageNumLabel = JLabel.class.cast(mainContainer.getComponent(2));
            pageNumLabel.setText((page + 1) + " / " + (maxPages + 1));

            Container pageContainer = singleGameView ? createSingleGameView(page) : createGameListContainer(page, gamesPerPage);

            mainContainer.add(pageContainer, 0);

            mainContainer.validate();
        }
    }

    private class gameListListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            createGameListFrame();
        }
    }

    /**
     * This is an inner class.
     * List Timer Listener class for the game list timer.
     * This class is invoked every time the timer goes off and checks if the game list needs to be removed from the screen.
     */
    private class listTimerListener implements ActionListener{
        
        private int secs;

        public listTimerListener()
        {
            secs = 0;
        }
        
        public void actionPerformed(ActionEvent e) {
            
            //System.out.println("Timer tick " + secs);

            if(selectionBeingMade || (gameViewFrame != null && gameViewFrame.isVisible()))
            {
                secs += timerDelay;
                
                if(secs > 180)
                {
                    if(gameViewFrame != null)
                    {
                        gameViewFrame.setVisible(false);
                    }

                    //if the selection frame is active
                    if(selectionBeingMade)
                    {
                        GameSelector.selectionFrame.setVisible(false);
                    }

                    secs = 0;
                }
            }
            else {
                secs = 0;
            }
        }

        public void resetTimer()
        {
            secs = 0;
        }
    }

    private class prefSelListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            selectionBeingMade = true;
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

    private class listViewListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            singleGameView = false;

            Container outerContainer = JButton.class.cast(e.getSource()).getParent().getParent();

            outerContainer.remove(1);
            outerContainer.add(createGameViewObjects(changePageListener.page / gamesInList));

            gameViewFrame.repaint();
            
        }
    }

    private class singleViewListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            singleGameView = true;

            JButton button = JButton.class.cast(e.getSource());
            String num = button.getText();
            num = num.replace(".", "");

            int pos = Integer.parseInt(num) - 1;

            Container outerContainer = button.getParent().getParent().getParent();

            outerContainer.remove(1);
            outerContainer.add(createGameViewObjects(pos));

            gameViewFrame.repaint();
            
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

        for(int i = 0; i < listLength; i++)
        {
            Game curGame = curGames[i + (gamesPerPage*page)];
            String gameName = curGame.getName();
            int gameNum = (page*gamesPerPage) + i;

            int buttonWidth = 100;
            JButton viewGameButton = createButton((gameNum+1) + ".", new singleViewListener(), GAME_FONT, BUTTON_COLOR, BUTTON_TEXT_COLOR);
            viewGameButton.setBounds((gameNameWidth + 10) * (i / (gamesPerPage/3)), (gameNameHeight+5) *(i % (gamesPerPage/3)), buttonWidth, gameNameHeight);
            gameListContainer.add(viewGameButton);

            JLabel gameLabel = new JLabel( " " + gameName);
            gameLabel.setFont(GAME_FONT);
            gameLabel.setForeground(GAME_FONT_COLOR);
            gameLabel.setBounds((gameNameWidth + 10) * (i / (gamesPerPage/3)) + buttonWidth, (gameNameHeight+5) *(i % (gamesPerPage/3)), gameNameWidth - buttonWidth, gameNameHeight);
            gameListContainer.add(gameLabel);
        }

        return gameListContainer;
    }
    
    /**
     * This method creates the objects nessesary to view the game list:
     * The container showing the games, the page chage buttons, back to start button.
     * @return the container these objects are added to.
     */
    private Container createGameViewObjects(int... page)
    {
        int pageNum = 0;
        if(page.length != 0)
        {
            pageNum = page[0];
        }

        Container mainContainer = new Container();
        mainContainer.setBounds(0, 0, screenWidth, screenHeight);

        int gamesPerPage = singleGameView ? 1 : gamesInList;
        int heightOffset = singleGameView ? 800 : 670;
        
        int leftBorder = 50;
        
        Container pageContainer = singleGameView ? createSingleGameView(pageNum) : createGameListContainer(pageNum, gamesPerPage);

        mainContainer.add(pageContainer);

        Font listButtonFont = new Font(TITLE_FONT.getFontName(), 1, 40);

        if(curGames != null)
        {
            int pageNumWidth = 160;

            //Creating the next and previous page controllers using the same listener
            JButton nextPageButton = createButton(">", null, listButtonFont, BUTTON_COLOR, BUTTON_TEXT_COLOR);
            changePageListener cpListener = new changePageListener(pageNum, mainContainer, nextPageButton.hashCode(), gamesPerPage);
            nextPageButton.addActionListener(cpListener);
            nextPageButton.setBounds((screenWidth / 2) + (pageNumWidth / 2), heightOffset, 100, 50);
            mainContainer.add(nextPageButton);

            JButton prevPageButton = createButton("<", cpListener, listButtonFont, BUTTON_COLOR, BUTTON_TEXT_COLOR);
            prevPageButton.setBounds((screenWidth / 2) - (100 + (pageNumWidth / 2)), heightOffset, 100, 50);
            mainContainer.add(prevPageButton);

            //setting the buttons invisible if no other pages are available    
            if(pageNum == 0)
            {
                prevPageButton.setVisible(false);
            }    

            if( pageNum == (curGames.length - 1) / gamesPerPage)
            {
                nextPageButton.setVisible(false);
            }

            //Page number string
            String pageNumString = (randomizedGame == -1) ? ((pageNum + 1) + " / " + (((curGames.length - 1) / gamesPerPage) + 1)) : "";
            JLabel pageNumLabel = new JLabel( pageNumString, SwingConstants.CENTER);
            pageNumLabel.setFont(GAME_FONT);
            pageNumLabel.setForeground(GAME_FONT_COLOR);
            pageNumLabel.setBounds((screenWidth / 2) - (pageNumWidth / 2), heightOffset, pageNumWidth, 50);
            mainContainer.add(pageNumLabel);

            //Listed view button
            if(singleGameView && randomizedGame == -1)
            {
                JButton listViewButton = createButton("See List", new listViewListener(), listButtonFont, BUTTON_COLOR, BUTTON_TEXT_COLOR);
                listViewButton.setBounds((screenWidth / 2) - (pageNumWidth * 2 / 3), heightOffset + 60, pageNumWidth * 4 / 3, 50);
                mainContainer.add(listViewButton);
            }
            
        }

        //Back to the selection screen button
        JButton backToStartButton = createButton("<-- Selection", new toStartListener(), TO_START_FONT, BUTTON_COLOR, BUTTON_TEXT_COLOR);
        backToStartButton.setBounds(leftBorder, heightOffset, 200, 50);
        mainContainer.add(backToStartButton);

        return mainContainer;

    }
    
    /**
     * This method is called from the game selector class.
     * This method creates a game list
     * @param sortedGames the games sorted according to preferences
     */
    public void gameSelectionDone(Game[] sortedGames)
    {
        selectionBeingMade = false;
        randomizedGame = -1;
        timerAction.resetTimer();

        singleGameView = true;
        curGames = sortedGames;

        initializeGameViewFrame();

        Container mainContainer = new Container();
        mainContainer.setBounds(0, 0, screenWidth, screenHeight);

        JLabel titleLabel = new JLabel("Games Best Fitting Your Preferences", SwingConstants.CENTER);
        titleLabel.setFont(new Font(TITLE_FONT.getFontName(), 1, 70));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        mainContainer.add(titleLabel);

        mainContainer.add(createGameViewObjects());
        
        gameViewFrame.add(mainContainer);

        gameViewFrame.setVisible(true);
    }

    /**
     * This method creates a game view container containing the information of a single game.
     * @param gameNum the array position of the game from curGames
     * @return the container with the information
     */
    public Container createSingleGameView(int gameNum)
    {
        int gameNameHeight = 80;
        int leftBorder = 20;
        int gameNameWidth = ((screenWidth - (2*leftBorder))/3);
     
        Container gameListContainer = new Container();
        gameListContainer.setBounds(0, 100, screenWidth - (leftBorder * 2) , 950);

        Game curGame = curGames[gameNum];

        String titleString = "";
        if(randomizedGame == -1)
        {
            titleString = (gameNum + 1) + ". ";
        }
        titleString += curGame.getName();
        JLabel titleLabel = new JLabel(titleString, SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        gameListContainer.add(titleLabel);

        // Image of the game
        JLabel imageDisplay = curGame.getImage();
        if(imageDisplay != null)
        {   
            imageDisplay.setLocation(leftBorder * 3 + ((gameNameWidth - imageDisplay.getWidth()) / 2), 170);
            gameListContainer.add(imageDisplay);
        }

        //Game Variables
        Container gameVarContainer = new Container();
        gameVarContainer.setBounds((leftBorder * 4) + gameNameWidth, 200, gameNameWidth * 2, 600);

        JLabel ratingLabel = new JLabel("Rating: " + curGame.getRating() + " / 100");
        ratingLabel.setFont(GAME_FONT);
        ratingLabel.setForeground(GAME_FONT_COLOR);
        ratingLabel.setBounds((gameNameWidth / 2), 0, gameNameWidth - 10, gameNameHeight);
        gameVarContainer.add(ratingLabel);

        JLabel typeLabel = new JLabel("Type: " + curGame.getType().name());
        typeLabel.setFont(GAME_FONT);
        typeLabel.setForeground(GAME_FONT_COLOR);
        typeLabel.setBounds(0, (gameNameHeight+5), gameNameWidth - 10, gameNameHeight);
        gameVarContainer.add(typeLabel);
        
        String plCntString = "Player Count: ";
        plCntString += (curGame.getMaxPlayerCount() == curGame.getMinPlayerCount()) ? curGame.getMinPlayerCount() 
                                : (curGame.getMinPlayerCount() + "-" + curGame.getMaxPlayerCount());

        JLabel playerCountLabel = new JLabel(plCntString);
        playerCountLabel.setFont(GAME_FONT);
        playerCountLabel.setForeground(GAME_FONT_COLOR);
        playerCountLabel.setBounds(0, ((gameNameHeight+5) * 2), gameNameWidth - (leftBorder * 4), gameNameHeight);
        gameVarContainer.add(playerCountLabel);

        String timeString = "Play Time: ";
        timeString += (curGame.getMaxPlayTime() == curGame.getMinPlayTime()) ? curGame.getMinPlayTime() 
                                : (curGame.getMinPlayTime() + "-" + curGame.getMaxPlayTime());

        timeString += " min";

        JLabel playTimeLabel = new JLabel(timeString);
        playTimeLabel.setFont(GAME_FONT);
        playTimeLabel.setForeground(GAME_FONT_COLOR);
        playTimeLabel.setBounds(gameNameWidth, ((gameNameHeight+5)), gameNameWidth, gameNameHeight);
        gameVarContainer.add(playTimeLabel);

        JLabel difficultyLabel = new JLabel("Difficulty: " + curGame.getDifficulty() / 100 + "." + curGame.getDifficulty() % 100 + " / 5");
        difficultyLabel.setFont(GAME_FONT);
        difficultyLabel.setForeground(GAME_FONT_COLOR);
        difficultyLabel.setBounds(gameNameWidth, ((gameNameHeight+5) * 2), gameNameWidth, gameNameHeight);
        gameVarContainer.add(difficultyLabel);
        
        gameListContainer.add(gameVarContainer);

        return gameListContainer;
    }

    //#endregion Game View Methods

    //#region Helpers

    public JButton createButton(String text, ActionListener listener, Font font, Color background, Color foreground)
    {
        JButton button = new JButton(text);
        if(listener != null)
        {
            button.addActionListener(listener);
        }
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        
        return button; 
    }

    //#endregion Helpers

}
