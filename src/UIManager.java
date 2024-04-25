/**
 * This is the class managing the UI dependancies of the Bilboard Game Selector project
 * @author Deniz Sahin
*/
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class UIManager {

    //screen size variables
    private static int screenWidth;
    private static int screenHeight;
    

    //defining private variables
    private static JFrame startFrame;
    private static JFrame gameViewFrame;

    //Fonts
    private static Font titleFont;
    private static Font gameFont;
    private static Font toStartFont;
    private static Font smallerGameFont;

    private static GameArray ga;
    private static Game[] curGames;

    //screen variables
    private static GraphicsEnvironment ge;
    private static GraphicsDevice[] gs;

    UIManager(GameArray gameArray)
    {
        ga = gameArray;
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gs = ge.getScreenDevices();

        screenHeight = ge.getMaximumWindowBounds().height;
        screenWidth = ge.getMaximumWindowBounds().width;

        //Font definitions
        titleFont = new Font("Times New Roman", 1, 90);
        gameFont = new Font("Arial", 0, 40);
        toStartFont = new Font("Times New Roman", 0, 20);
        smallerGameFont = new Font("Arial", 0, 20);

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
        startFrame.setSize(screenWidth, screenHeight);
        //startFrame.setLayout(new GridLayout(3,1));s

        Container startSelBtContainer = new Container();

        int leftBorder = 90;

        JLabel titleLabel = new JLabel("Bilboard Game Selector v1.0", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        //titleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        startSelBtContainer.add(titleLabel);
        //startFrame.add(titleLabel);
        //startSelBtContainer.setLayout(new GridLayout(2,2));

        Font selButtFont = new Font("Arial", 1, 25);

        JButton prefSelButton = new JButton("Find a Game");
        //prefSelButton.setSize(200, 50);
        prefSelButton.setBounds(leftBorder, 220, 700, 250);
        prefSelButton.addActionListener(new prefSelListener());
        prefSelButton.setFont(selButtFont);
        startSelBtContainer.add(prefSelButton);

        JButton randGameButton = new JButton("Random Game");
        randGameButton.addActionListener(new randGameListener());
        randGameButton.setBounds(leftBorder + 800, 220, 700, 250);
        randGameButton.setFont(selButtFont);
        startSelBtContainer.add(randGameButton);

        JButton gameListButton = new JButton("See Entire Game List");
        gameListButton.addActionListener(new gameListListener());
        gameListButton.setBounds(leftBorder, 520, 700, 250);
        gameListButton.setFont(selButtFont);
        startSelBtContainer.add(gameListButton);
        
        JButton howToButton = new JButton("How To Use?");
        howToButton.addActionListener(new howToListener());
        howToButton.setBounds(leftBorder + 800, 520, 700, 250);
        howToButton.setFont(selButtFont);
        startSelBtContainer.add(howToButton);

        JButton goBackButton = new JButton("<-- Go Back");
        goBackButton.addActionListener(new goBackListener());
        goBackButton.setBounds(leftBorder, 800, 120, 30);
        startSelBtContainer.add(goBackButton);
        
        startFrame.add(startSelBtContainer);

        startFrame.setVisible(true);
        //goBackButton.setVisible(false);
    }

    /**
     * This method creates the view all games frame and makes it visible.
     */
    private void createGameListFrame()
    {
        curGames = ga.copyArray();

        //mainFrame
        gameViewFrame = new JFrame();
        gameViewFrame.setTitle("Bilboard Game Selector");
        gameViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameViewFrame.setSize(screenWidth, screenHeight);
        //startFrame.setLayout(new GridLayout(3,1));

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("Our Full Game List", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(0, 0, 1600, 200);
        mainContainer.add(titleLabel);

        createGameViewObjects(mainContainer);
        
        gameViewFrame.add(mainContainer);

        //gs[0].setFullScreenWindow(startFrame);
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

        //mainFrame
        gameViewFrame = new JFrame();
        gameViewFrame.setTitle("Bilboard Game Selector");
        gameViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameViewFrame.setSize(screenWidth, screenHeight);

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("A List Of Random Games", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
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
        //mainFrame
        gameViewFrame = new JFrame();
        gameViewFrame.setTitle("Bilboard Game Selector");
        gameViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameViewFrame.setSize(screenWidth, screenHeight);

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("How To Use Bilboard Game Selector?", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(0, 0, screenWidth, 200);
        mainContainer.add(titleLabel);

        JLabel step1Label = new JLabel("1) Go back to the start screen and press on the find a game button.", SwingConstants.LEFT);
        step1Label.setFont(gameFont);
        step1Label.setBounds(leftBorder, 0, screenWidth, 400);
        mainContainer.add(step1Label);

        JLabel step2Label = new JLabel("2) Answer the questions based on your preferences.", SwingConstants.LEFT);
        step2Label.setFont(gameFont);
        step2Label.setBounds(leftBorder, 0, screenWidth, 550);
        mainContainer.add(step2Label);

        JLabel step3Label = new JLabel("3) If you don't know how to play the games ask for help from an organisation team member.", SwingConstants.LEFT);
        step3Label.setFont(gameFont);
        step3Label.setBounds(leftBorder, 0, screenWidth,700);
        mainContainer.add(step3Label);

        JLabel step3ExLabel = new JLabel("If you don't know who is an OT member ask around, there should be at least one of us around here somewhere?", SwingConstants.LEFT);
        step3ExLabel.setFont(smallerGameFont);
        step3ExLabel.setBounds(leftBorder + 60, 0, screenWidth, 780);
        mainContainer.add(step3ExLabel);

        JLabel step3Ex2Label = new JLabel("Also, we don't know some of the games eighter so don't get your hopes too high. :)", SwingConstants.LEFT);
        step3Ex2Label.setFont(smallerGameFont);
        step3Ex2Label.setBounds(leftBorder + 60, 0, screenWidth, 840);
        mainContainer.add(step3Ex2Label);

        JLabel step4Label = new JLabel("4) Now that you have hopefully found a game thats for your liking, have fun playing it!", SwingConstants.LEFT);
        step4Label.setFont(gameFont);
        step4Label.setBounds(leftBorder, 0, screenWidth, 990);
        mainContainer.add(step4Label);

        JButton backToStartButton = new JButton("<-- Selection");
        backToStartButton.addActionListener(new toStartListener());
        backToStartButton.setBounds(leftBorder, 620, 200, 50);
        backToStartButton.setFont(toStartFont);
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
            startFrame.setVisible(false);
            createGameListFrame();
        }
    }

    private class prefSelListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            startFrame.setVisible(false);
            createGameSelectionFrame();
        }
    }

    private class toStartListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            gameViewFrame.setVisible(false);
            startFrame.setVisible(true);
            //startFrame.getComponent(0).get.setVisible(true);
        }
    }

    private class goBackListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(gameViewFrame.isDisplayable())
            {    
                startFrame.setVisible(false);
                gameViewFrame.setVisible(true);
            }
        }
    }

    private class randGameListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            startFrame.setVisible(false);
            createRandomGameFrame();
        }
    }

    private class howToListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            startFrame.setVisible(false);
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
        int gameNameWidth = 520;
        int gameNameHeight = 80;
        int leftBorder = 50;
     
        Container gameListContainer = new Container();
        gameListContainer.setBounds(leftBorder, 200, 2460, 950);

        if(curGames == null)
        {
            JLabel gameLabel = new JLabel( "No games in our list fit your preference.");
            gameLabel.setFont(gameFont);
            gameLabel.setBounds( 100, 250, 1000, gameNameHeight);
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
            gameLabel.setFont(gameFont);
            gameLabel.setBounds( 10+ (gameNameWidth+10) * (i / (gamesPerPage/3)), (gameNameHeight+5) *(i % (gamesPerPage/3)), gameNameWidth, gameNameHeight);
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
        
        if(curGames != null)
        {
            //Creating the next and previous page controllers using the same listener
            JButton nextPageButton = new JButton(">");
            changePageListener cpListener = new changePageListener(0, mainContainer, nextPageButton.hashCode(), gamesPerPage);
            nextPageButton.addActionListener(cpListener);
            nextPageButton.setBounds(leftBorder + 800, 620, 100, 50);
            nextPageButton.setFont(gameFont);
            mainContainer.add(nextPageButton);

            JButton prevPageButton = new JButton("<");
            prevPageButton.addActionListener(cpListener);
            prevPageButton.setBounds(leftBorder + 700, 620, 100, 50);
            prevPageButton.setFont(gameFont);
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
        backToStartButton.setBounds(leftBorder, 620, 200, 50);
        backToStartButton.setFont(toStartFont);
        mainContainer.add(backToStartButton);

    }
    //#endregion Game View Methods
    
    /**
     * This method is called from the game selector class.
     * This method creates a game list
     * @param sortedGames the games sorted according to preferences
     */
    public void gameSelectionDone(Game[] sortedGames)
    {
        curGames = sortedGames;

        //mainFrame
        gameViewFrame = new JFrame();
        gameViewFrame.setTitle("Bilboard Game Selector");
        gameViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameViewFrame.setSize(screenWidth, screenHeight);

        Container mainContainer = new Container();

        JLabel titleLabel = new JLabel("Games Best Fitting Your Preferences", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(0, 0, 1600, 200);
        mainContainer.add(titleLabel);

        createGameViewObjects(mainContainer);
        
        gameViewFrame.add(mainContainer);

        //gs[0].setFullScreenWindow(startFrame);
        gameViewFrame.setVisible(true);
    }

}
