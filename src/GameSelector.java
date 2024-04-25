

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


/**
 * This is a game selector class representing a game selector object.
 * This object is created whenever a game is needed to be selected according to preferences.
 * This object manages the UI of the selection process.
 * @author Deniz Sahin
 */

public class GameSelector {
    
    //Defining the coefficients
    private static final double optPCoeff = 1.2;
    private static final double playTimeCoeff = 1.7;
    private static final int startingCoeff = 100;

    //Defining the variables
    private static GameArray ga;
    private static UIManager parent;
    private static Node firstNode;
    
    //UI Elements
    private static JFrame[] selectionFrames;
    private static int curSelFrame;

    private static Font questionFont;
    private static Font selButtFont;

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

        selectionFrames = new JFrame[5];
        curSelFrame = 0;
        selButtFont = new Font("Arial", 0, 20);

        questionFont = new Font("Times New Roman", 1, 40);

        firstNode = createNodes();

        createNextQuestion();
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

    /**
     * This function creates the question frame acording to the prameters provided
     */
    private void createNextQuestion()
    {

        //starting frame
        selectionFrames[curSelFrame] = new JFrame();
        selectionFrames[curSelFrame].setTitle("Bilboard Game Selector");
        selectionFrames[curSelFrame].setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        selectionFrames[curSelFrame].setSize(2560, 1440);

        Container selContainer = new Container();

        //Defining the question variables
        String questionString;
        int answerCount, answerMultiplier;
        switch (curSelFrame) {
            case 0:
                questionString = "How many players are going to be playing the game?";
                answerCount = 12;
                answerMultiplier = 1;
                break;
            case 1:
                questionString = "Approximately how long do you want the game to last?";
                answerCount = 12;
                answerMultiplier = 10;
                break;
            case 2://special case
                questionString = "What kind of game do you want?";
                answerCount = 3;
                answerMultiplier = 1;
                break;
            case 3:
                questionString = "How difficult of a game do you want? (1: Easy, 10: Hard)";
                answerCount = 10;
                answerMultiplier = 1;
                break;
        
            default:
                questionString = "";
                answerCount = 0;
                answerMultiplier = 0;    
                break;
        }

        JLabel questionLabel = new JLabel(questionString, SwingConstants.CENTER);
        questionLabel.setFont(questionFont);
        questionLabel.setBounds(0, 0, 1600, 200);
        selContainer.add(questionLabel);

        Container selButContainer = new Container();
        selButContainer.setBounds(700, 250, 300, 300);
        selButContainer.setLayout(new GridLayout(4, 3));

        selectionMadeListener plListener = new selectionMadeListener();

        //Selection Buttons
        if(curSelFrame != 2)
        {
            for(int i = 1; i < answerCount + 1; i++)
            {
                JButton button = new JButton(String.valueOf(i * answerMultiplier));
                button.setFont(selButtFont);
                button.addActionListener(plListener);
                selButContainer.add(button);
            }
        } else {
            selButContainer.setLayout(new FlowLayout());
            for(int i = 0; i < answerCount; i++)
            {
                JButton button = new JButton(Game.GameType.enumStringAtPos(i));
                button.setFont(selButtFont);
                button.addActionListener(plListener);
                selButContainer.add(button);
            }
        }
        
        selContainer.add(selButContainer);
        
        JButton dNMButton = new JButton("Does Not Matter");
        dNMButton.setFont(selButtFont);
        dNMButton.addActionListener(new doesNotMatterListener());
        dNMButton.setBounds(700, 650, 300, 100);
        selContainer.add(dNMButton);

        selectionFrames[curSelFrame].add(selContainer);

        //gs[0].setFullScreenWindow(startFrame);
        selectionFrames[curSelFrame].setVisible(true);

    }

    private class doesNotMatterListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            selectionFrames[curSelFrame].setVisible(false);

            curSelFrame++;
        if(curSelFrame != 4)
        {
            createNextQuestion();
        } else {
            createSortedArray();
        }
        }
    }

    private class selectionMadeListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            selectionFrames[curSelFrame].setVisible(false);

            String buttonValue = JButton.class.cast(e.getSource()).getText();

            //System.out.println("Button pressed at selection frame " + curSelFrame + " with button value " + buttonValue);

            if(curSelFrame != 2)
            {
                int buttonPressed = Integer.parseInt(buttonValue);
                buttonAction(buttonPressed);
            } else {
                //System.out.println("Selection no 2 accnowledged.");
                int buttonPressed = Game.GameType.enumIntFromString(buttonValue);
                //System.out.println("Button press value recognized as " + buttonPressed);
                buttonAction(buttonPressed);
            }
        }
    }

    /**
     * This method is called whenever a selection button is pressed during the selection process
     * @param buttonValue
     */
    private void buttonAction(int buttonValue)
    {
        curSelFrame++;
        if(curSelFrame != 4)
        {
            createNextQuestion();
        }

        Node curNode = firstNode;
        Node prevNode = firstNode;

        while(curNode != null)
        {
            //if the question was player count
            if(curSelFrame == 1)
            {
                if(buttonValue > curNode.game.getMaxPlayerCount() || buttonValue < curNode.game.getMinPlayerCount() || 
                            (buttonValue % 2 != 0 && curNode.game.isEvenPlayerCount())) 
                {
                    System.out.print("Due to player count ");
                    curNode.removeNode(prevNode);
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
        
            //if the question was play time
            else if(curSelFrame == 2)
            {
                if(buttonValue * playTimeCoeff < curNode.game.getMaxPlayTime() || buttonValue / playTimeCoeff> curNode.game.getMinPlayTime()) 
                {
                    System.out.print("Due to game length ");
                    curNode.removeNode(prevNode);
                } 
            }
    
            //if the question was type
            else if(curSelFrame == 3)
            {
                if(!Game.GameType.includesGame(buttonValue, curNode.game)) 
                {
                    System.out.print("Due to its type ");
                    curNode.removeNode(prevNode);
                } 
            }

            else if(curSelFrame == 4)
            {
                System.out.print("Due to optimal player selection score of " + curNode.game.getName() + " changed from " + curNode.score);
                curNode.score = curNode.score * ((500 - Math.abs(curNode.game.getDifficulty() - (buttonValue * 50))));
                System.out.println(" to " + curNode.score);
            }

            if(prevNode.next == curNode)
            {    
                prevNode = curNode;   
            }
            curNode = curNode.next;
        }

        System.out.println();

        if(curSelFrame == 4)
        {
            createSortedArray();
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
