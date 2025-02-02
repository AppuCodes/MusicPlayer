package appu26j.musicplayer;

import java.awt.Color;

import appu26j.aguiu.AgWindow;
import appu26j.musicplayer.gui.HomeScreen;
import appu26j.musicplayer.utils.AudioUtil;
import appu26j.musicplayer.utils.SystemTheme;

public class MusicPlayer
{
    public static final Color DARK_MODE = SystemTheme.isDarkMode() ? new Color(32, 32, 32) : null;
    
    public static void main(String[] args)
    {
        AgWindow.create("", 500, 500, true);
        AgWindow.setIcon("appu26j/musicplayer/assets/icon.png");
        
        if (DARK_MODE != null)
            AgWindow.setBackgroundColor(DARK_MODE.getRed(), DARK_MODE.getGreen(), DARK_MODE.getBlue(), 255);
        else
            AgWindow.setBackgroundColor(235, 245, 250, 255);
        
        AudioUtil.init();
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
