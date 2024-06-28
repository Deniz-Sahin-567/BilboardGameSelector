

/**
 * This is the main class of the Bilboard Game Selector project
 * @author Deniz Sahin
 */
 
import java.awt.GraphicsEnvironment;

public class App {

    private static int screenHeight;
    private static int screenWidth;
    public static void main(String[] args) throws Exception {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        screenHeight = ge.getMaximumWindowBounds().height;
        screenWidth = ge.getMaximumWindowBounds().width;

        GameArray ga = new GameArray("GameList.txt");
        
        new UIManager(ga);
    }

    public static int getScreenWidth()
    {
        return screenWidth;
    }    
    public static int getScreenHeight()
    {
        return screenHeight;
    }
}
