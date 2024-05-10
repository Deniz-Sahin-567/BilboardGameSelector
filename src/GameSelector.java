
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GraphicsEnvironment;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * This is a game selector class representing a game selector object.
 * This object is created whenever a game is needed to be selected according to preferences.
 * This object manages the UI of the selection process.
 * @author Deniz Sahin
 */

public class GameSelector {
    
    //#region Variables

    //Defining the coefficients
    private static final double optPCoeff = 1.2;
    private static final double playTimeCoeff = 1.7;
    private static final int startingCoeff = 100;

    //Defining the variables
    private static GameArray ga;
    private static UIManager parent;
    private static Node firstNode;
    
    //UI Elements
    private static JFrame selectionFrame;
    private static int curSelFrame;
    private static JTabbedPane questionPane;
    private static ButtonGroup[] answers;
    private static JButton doneButton;

    private static Font questionFont;
    private static Font selButtFont;
    private static Font pageChangeFont;

    private static Color buttonColor;
    private static Color questionColor;
    private static Color backgroundColor;
    private static Color buttonTextColor;

    private static int screenWidth;

    //#endregion Variables

    private class Node
    {
        private Game game;
        private Node next;
        private int score;

        public Node removeNode(Node prevNode)
        {
            System.out.println("Node was removed: " + game.getName());
         
            if(firstNode == this)
            {
                firstNode = this.next;
                return firstNode;
            }

            prevNode.next = this.next;

            return prevNode.next;
        }
    }

    public GameSelector(GameArray gameArray, UIManager uiManager)
    {
        ga = gameArray;
        parent = uiManager;

        selectionFrame = new JFrame();
        answers = new ButtonGroup[4];
        curSelFrame = 0;

        //Fonts and colors
        selButtFont = UIManager.GAME_FONT;
        questionFont = new Font(UIManager.TITLE_FONT.getFontName(), 1, 50);
        pageChangeFont = new Font(selButtFont.getFontName(), 0, 30);

        buttonColor = UIManager.BUTTON_COLOR;
        backgroundColor = UIManager.BACKGROUND_COLOR;
        questionColor = UIManager.TITLE_COLOR;
        buttonTextColor = UIManager.BUTTON_TEXT_COLOR;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        screenWidth = ge.getMaximumWindowBounds().width;
        
        firstNode = createNodes();

        createAllQuestions();
    }

    /**
     * This method creates a new linked list of game containing nodes
     * @return  The first node in the list
     */
    private Node createNodes()
    {
        Node startNode = null, curNode = null;

        for(int i = 0; i < ga.getGameAmount(); i++)
        {
            if(startNode == null)
            {
                startNode = new Node();
                startNode.game = ga.getGameAtIndex(i);
                startNode.score = startNode.game.getRating() * startingCoeff;
                curNode = startNode;
            } else {
                curNode.next = new Node();
                curNode = curNode.next;
                curNode.game = ga.getGameAtIndex(i);
                curNode.score = curNode.game.getRating() * startingCoeff;
            }
        }

        curNode.next = null;

        return startNode;
    }

    //#region UI Methods

    /**
     * This method creates and makes visible the question selection tabbed pane.
     */
    private void createAllQuestions()
    {
        //starting frame
        selectionFrame = new JFrame();
        selectionFrame.setTitle("Bilboard Game Selector");
        selectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectionFrame.setUndecorated(true);
        selectionFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        selectionFrame.getContentPane().setBackground(backgroundColor);

        questionPane = new JTabbedPane();
        ImageIcon paneIcon = null;

        for(int i = 1; i < 5; i++)
        {
            JComponent questionPanel = createNextQuestion();

            String qName;
            switch (i) {
                case 1:
                    qName = "Player Count";
                    break;
                case 2:
                    qName = "Game Length";
                    break;
                case 3:
                    qName = "Game Type";
                    break;
                case 4:
                    qName = "Game Difficulty";
                    break;
                default:
                    qName = "Question " + i;
                    break;
            }
            questionPane.addTab(qName, paneIcon, questionPanel, null);
        }

        selectionFrame.add(questionPane);
        selectionFrame.setVisible(true);
    }

    /**
     * This function creates the question frame acording to the prameters provided
     */
    private JComponent createNextQuestion()
    {
        JPanel questionPanel = new JPanel(false);
        questionPanel.setLayout(new GridLayout(1, 1));
        questionPanel.setBackground(backgroundColor);

        Container selContainer = new Container();

        //Defining the question variables
        String questionString, questionString2;
        int answerCount, answerMultiplier;
        switch (curSelFrame) {
            case 0:
                questionString = "How many players are going to be playing the game?";
                questionString2 = "";
                answerCount = 12;
                answerMultiplier = 1;
                break;
            case 1:
                questionString = "Approximately how long do you want the game";
                questionString2 = "to last in minutes?";
                answerCount = 12;
                answerMultiplier = 10;
                break;
            case 2://special case
                questionString = "What kind of game do you want?";
                questionString2 = "";
                answerCount = 3;
                answerMultiplier = 1;
                break;
            case 3:
                questionString = "How difficult of a game do you want?";
                questionString2 = "(1: Easy, 10: Hard)";
                answerCount = 10;
                answerMultiplier = 1;
                break;
        
            default:
                questionString = "";
                answerCount = 0;
                questionString2 = "";
                answerMultiplier = 0;    
                break;
        }

        JLabel questionLabel = new JLabel(questionString, SwingConstants.CENTER);
        questionLabel.setFont(questionFont);
        questionLabel.setForeground(questionColor);
        questionLabel.setBounds(0, 0, screenWidth, 200);
        selContainer.add(questionLabel);

        JLabel questionLabel2 = new JLabel(questionString2, SwingConstants.CENTER);
        questionLabel2.setFont(questionFont);
        questionLabel2.setForeground(questionColor);
        questionLabel2.setBounds(0, 50, screenWidth, 200);
        selContainer.add(questionLabel2);

        Container selButContainer = new Container();
        selButContainer.setBounds(screenWidth / 3, 300, screenWidth / 3, 300);
        selButContainer.setLayout(new GridLayout(4, 3));

        answers[curSelFrame] = new ButtonGroup();

        //Selection Buttons
        if(curSelFrame != 2)
        {
            for(int i = 1; i < answerCount + 1; i++)
            {
                JRadioButton button = new JRadioButton(String.valueOf(i * answerMultiplier));
                button.setActionCommand(String.valueOf(i * answerMultiplier));
                button.setFont(selButtFont);
                button.setBackground(backgroundColor);
                button.setForeground(questionColor);
                answers[curSelFrame].add(button);
                selButContainer.add(button);
            }

            JRadioButton dNMButton = new JRadioButton("Does Not Matter");
            dNMButton.setActionCommand("DNM");
            dNMButton.setFont(selButtFont);
            dNMButton.setBackground(backgroundColor);
            dNMButton.setForeground(questionColor);
            dNMButton.setBounds(screenWidth / 3, 600, screenWidth / 3, 75);
            answers[curSelFrame].add(dNMButton);
            selContainer.add(dNMButton);

            // auto selects dnm
            answers[curSelFrame].setSelected(dNMButton.getModel(), true);

        } else {
            selButContainer.setLayout(new FlowLayout());

            JRadioButton button = null;

            for(int i = 0; i < answerCount; i++)
            {
                button = new JRadioButton(Game.GameType.enumStringAtPos(i));
                button.setActionCommand("" + i);
                button.setFont(selButtFont);
                button.setBackground(backgroundColor);
                button.setForeground(questionColor);
                answers[curSelFrame].add(button);
                selButContainer.add(button);
            }

            //auto selects either
            if(button != null)
            {
                answers[curSelFrame].setSelected(button.getModel(), true);
            }
        }

        
        selContainer.add(selButContainer);

        if(curSelFrame != 3)
        {
            JButton nextPageButton = new JButton("Next Question -->");
            nextPageButton.setActionCommand(">");
            nextPageButton.addActionListener(new changePageListener());
            nextPageButton.setBounds(screenWidth * 6 / 10, 750, screenWidth * 3 / 10, 70);
            nextPageButton.setFont(pageChangeFont);
            nextPageButton.setBackground(buttonColor);
            nextPageButton.setForeground(buttonTextColor);
            selContainer.add(nextPageButton);
        } else {
            doneButton = new JButton("Compute Acceptable Games");
            doneButton.addActionListener(new selectionMadeListener());
            doneButton.setBounds(screenWidth * 6 / 10, 750, screenWidth * 3 / 10, 70);
            doneButton.setFont(pageChangeFont);
            doneButton.setBackground(buttonColor);
            doneButton.setForeground(buttonTextColor);
            selContainer.add(doneButton);
        }

        if(curSelFrame != 0)
        {
            JButton prevPageButton = new JButton("<-- Previous Question");
            prevPageButton.setActionCommand("<");
            prevPageButton.addActionListener(new changePageListener());
            prevPageButton.setBounds(screenWidth * 1 / 10, 750, screenWidth * 3 / 10, 70);
            prevPageButton.setFont(pageChangeFont);
            prevPageButton.setBackground(buttonColor);
            prevPageButton.setForeground(buttonTextColor);
            selContainer.add(prevPageButton);
        }
        

        questionPanel.add(selContainer);

        curSelFrame++;

        return questionPanel;
    }

    /**
     * This action listener changes the question page
     */
    private class changePageListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {

            if(e.getActionCommand().equals("<"))
            {
                questionPane.setSelectedIndex(questionPane.getSelectedIndex() - 1);
            } else if(e.getActionCommand().equals(">"))
            {
                questionPane.setSelectedIndex(questionPane.getSelectedIndex() + 1);
            }
        }
    }

    private class selectionMadeListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            doneButton.setVisible(false); // removes the possibility of double tapping
            selectionDone();
        }
    }

    //#endregion UI Methods

    /**
     * This method is called whenever a selection button is pressed during the selection process
     * @param buttonValue
     */
    private void selectionDone()
    {   
        Node curNode = firstNode;
        Node prevNode = firstNode;

        while(curNode != null)
        {
            computeNodeScore(curNode, prevNode);

            if(prevNode.next == curNode)
            {    
                prevNode = curNode;   
            }
            curNode = curNode.next;
        }

        System.out.println();

        createSortedArray();
        
    }

    /**
     * This method computes the nodes score based on the given answers and stores it inside the node itself
     * @param curNode   Node being examined
     * @param prevNode  The node pointing to curNode
     */
    private void computeNodeScore(Node curNode, Node prevNode)
    {
        //player count
        if(!answers[0].getSelection().getActionCommand().equals("DNM"))
        {
            int buttonValue = Integer.parseInt(answers[0].getSelection().getActionCommand());

            if(buttonValue > curNode.game.getMaxPlayerCount() || buttonValue < curNode.game.getMinPlayerCount() || 
                        (buttonValue % 2 != 0 && curNode.game.isEvenPlayerCount())) 
            {
                System.out.print("Due to player count ");
                curNode.removeNode(prevNode);
                return;
            } 

            for(int opPlC : curNode.game.getOpPlayerCount())
            {
                if(opPlC == buttonValue)
                {
                    System.out.print("Due to optimal player selection score of " + curNode.game.getName() + " changed from " + curNode.score);
                    curNode.score = (int) (curNode.score * optPCoeff);
                    System.out.println(" to " + curNode.score);
                }
            }
        }
    
        //play time
        if(!answers[1].getSelection().getActionCommand().equals("DNM"))
        {
            int buttonValue = Integer.parseInt(answers[1].getSelection().getActionCommand());
            
            if(buttonValue * playTimeCoeff < curNode.game.getMaxPlayTime() || buttonValue / playTimeCoeff> curNode.game.getMinPlayTime()) 
            {
                System.out.print("Due to game length of " + buttonValue);
                curNode.removeNode(prevNode);
                return;
            } 
        }

        //type
        if(!answers[2].getSelection().getActionCommand().equals("2"))
        {
            int buttonValue = Integer.parseInt(answers[2].getSelection().getActionCommand());

            if(!Game.GameType.includesGame(buttonValue, curNode.game)) 
            {
                System.out.print("Due to its type ");
                curNode.removeNode(prevNode);
                return;
            } 
        }

        //difficulty
        if(!answers[3].getSelection().getActionCommand().equals("DNM"))
        {
            int buttonValue = Integer.parseInt(answers[3].getSelection().getActionCommand());

            System.out.print("Due to optimal player selection score of " + curNode.game.getName() + " changed from " + curNode.score);
            curNode.score = curNode.score * ((500 - Math.abs(curNode.game.getDifficulty() - (buttonValue * 50))));
            System.out.println(" to " + curNode.score);
        }
    }

    /**
     * This method creates the final sorted array and sends it to the UIManager
     */
    private void createSortedArray()
    {
        Node curNode = firstNode;
        
        int remGCount;
        for(remGCount = 0; curNode != null; remGCount++)
        {
            curNode = curNode.next;
        }

        if(remGCount == 0)
        {
            parent.gameSelectionDone(null);
            
            selectionFrame.setVisible(false);
            return;
        }

        sortNodes();

        curNode = firstNode;
        Game[] sortedGames = new Game[remGCount];

        for(int i = 0; i < remGCount; i++)
        {
            sortedGames[i] = curNode.game;

            System.out.println("Game: " + curNode.game.getName() + " ended up with score of " + curNode.score);

            curNode = curNode.next;
        }

        parent.gameSelectionDone(sortedGames);
        
        

        selectionFrame.setVisible(false);
    }

    /**
     * Sorts the nodes in ascending order using bubble sort.
     * Starts from the firstNode.
     */
    private void sortNodes()
    {
        boolean sorted = false;

        if(firstNode == null || firstNode.next == null)
        {
            return;
        }

        while(!sorted)
        {
            sorted = true;
            
            for(Node curNode = firstNode, prevNode = null; curNode != null; curNode = curNode.next)
            {
                //it the first node is being checked
                if(prevNode == null)
                {
                    if(curNode.next.score > curNode.score)
                    {
                        firstNode = curNode.next;
                        curNode.next = firstNode.next;
                        firstNode.next = curNode;
                        sorted = false;
                    }
                } else if (curNode.next == null) {
                    //Just skip this one
                } else {
                    if(curNode.next.score > curNode.score)
                    {
                        prevNode.next = curNode.next;
                        curNode.next = prevNode.next.next;
                        prevNode.next.next = curNode;
                        sorted = false;
                    }
                }

                prevNode = curNode;
            }
        }
    }

}
