package appu26j.musicplayer;

import appu26j.aguiu.AgWindow;
import appu26j.musicplayer.gui.HomeScreen;

public class MusicPlayer
{
    public static void main(String[] args)
    {
        AgWindow.create("", 500, 500, true);
        AgWindow.setBackgroundColor(235, 245, 250, 255);
        AgWindow.displayScreen(new HomeScreen());
        
        while (AgWindow.isOpen())
        {
            AgWindow.beginLoop();
            AgWindow.draw();
            AgWindow.endLoop();
        }
        
        System.exit(0); // Fix JavaFX keeping the application open
    }
}
