

/**
 * This is the main class of the Bilboard Game Selector project
 * @author Deniz Sahin
 */

public class App {

    public static void main(String[] args) throws Exception {
        GameArray ga = new GameArray("GameList.txt");
        
        new UIManager(ga);
    }
}
