package appu26j.musicplayer.gui;

import static appu26j.musicplayer.utils.RenderUtil.*;

import java.awt.Color;
import java.io.File;

import appu26j.aguiu.AgWindow;
import appu26j.aguiu.gui.GuiScreen;
import appu26j.musicplayer.utils.AudioUtil;

public class HomeScreen extends GuiScreen
{
    @Override
    public void drawScreen(float cursorX, float cursorY)
    {
        bigText.drawString("Welcome", 12, 0, Color.BLACK);
        float maxWidth = width - 48;
        int offset = 0;
        
        for (File file : AudioUtil.DIRECTORY.listFiles())
        {
            if (AudioUtil.isMusic(file))
            {
                String name = file.getName(), extension = name.substring(name.indexOf("."));
                float textWidth = mediumText.getWidth(name) + 48;
                
                if (textWidth > maxWidth)
                {
                    maxWidth = textWidth;
                }
                
                drawRect(48, 120 + offset, maxWidth, 180 + offset, new Color(0, 128, 255, ICIB(cursorX, cursorY, 48, 120 + offset, maxWidth, 180 + offset) ? 50 : 25));
                drawGradient(56, 133 + offset, 113, 164 + offset, Color.MAGENTA, Color.MAGENTA.darker());
                smallText.drawString(extension.substring(1), 85 - (smallText.getWidth(extension.substring(1)) / 2), 130 + offset, Color.WHITE);
                mediumText.drawString(name.replace(extension, ""), 123, 120 + offset, Color.DARK_GRAY);
                offset += 60;
            }
        }
        
        if (offset != 0)
        {
            smallText.drawString("You may click one to play or choose", 48, 64, Color.BLACK);
            smallText.drawString("here", 436, 64, Color.BLUE);
        }
        
        else
        {
            smallText.drawString("You may choose one to play", 48, 64, Color.BLACK);
            smallText.drawString("here", 355, 64, Color.BLUE);
        }
        
        drawRect(0, height - 90, width, height, new Color(225, 235, 250));
        drawGradient(0, height - 110, width, height - 90, new Color(225, 235, 250, 0), new Color(225, 235, 250));
        drawPlayButton(false, false, cursorX, cursorY, width, height);
        drawArrows(false, cursorX, cursorY, width, height);
    }
    
    @Override
    public void mouseClicked(int button, float cursorX, float cursorY)
    {
        float maxWidth = width - 48;
        int offset = 0;
        
        for (File file : AudioUtil.DIRECTORY.listFiles())
        {
            if (AudioUtil.isMusic(file))
            {
                float textWidth = mediumText.getWidth(file.getName()) + 48;
                
                if (textWidth > maxWidth)
                {
                    maxWidth = textWidth;
                }
                
                if (ICIB(cursorX, cursorY, 48, 120 + offset, maxWidth, 180 + offset) && (height - 90) > cursorY)
                {
                    AgWindow.displayScreen(new PlayScreen(file));
                }
                
                offset += 60;
            }
        }
    }
}
