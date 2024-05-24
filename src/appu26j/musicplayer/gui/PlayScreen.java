package appu26j.musicplayer.gui;

import static appu26j.musicplayer.utils.RenderUtil.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.File;

import appu26j.aguiu.gui.GuiScreen;
import appu26j.musicplayer.utils.*;
import javafx.util.Duration;

public class PlayScreen extends GuiScreen
{
    private File music;
    
    public PlayScreen(File music)
    {
        this.music = music;
        AudioUtil.play(music);
    }
    
    @Override
    public void drawScreen(float cursorX, float cursorY)
    {
        drawGradient(0, 0, width, height, new Color(235, 245, 250), new Color(120, 185, 250));
        // drawGradient(0, height / 2, width, height, new Color(0, 128, 255, 128), new Color(235, 245, 250));
        Duration currentTime = AudioUtil.getCurrentTime(), stopTime = AudioUtil.getStopTime();
        float progress = AudioUtil.hasFinished() ? 1 : (float) (currentTime.toMillis() / stopTime.toMillis());
        drawRect(16, height - 122, width - 16, height - 115, new Color(128, 128, 200, 128));
        drawHorzGradient(16, height - 122, ((width - 32) * progress) + 16, height - 115, Color.RED.darker(), Color.RED);
        drawPlayButton(true, AudioUtil.isPlaying(), cursorX, cursorY, width, height);
        String name = music.getName().substring(0, music.getName().indexOf("."));
        float textWidth = bigText.getWidth(name);
        float scale = 1;
        
        if (textWidth > (width - 32))
        {
            scale = (width - 32) / textWidth;
        }
        
        glPushMatrix();
        glScalef(scale, scale, scale);
        bigText.drawString(name, (width / scale / 2) - (textWidth / 2), (height - 229) / scale / 2, Color.BLACK);
        glPopMatrix();
        String endDuration = (int) stopTime.toMinutes() + ":" + (((int) stopTime.toSeconds() % 60) < 10 ? "0" + ((int) stopTime.toSeconds() % 60) : ((int) stopTime.toSeconds() % 60));
        String currentDuration = AudioUtil.hasFinished() ? endDuration : ((int) currentTime.toMinutes() + ":" + (((int) currentTime.toSeconds() % 60) < 10 ? "0" + ((int) currentTime.toSeconds() % 60) : ((int) currentTime.toSeconds() % 60)));
        smallText.drawString(currentDuration + " / " + endDuration, 16, height - 165, Color.DARK_GRAY);
        drawArrows(true, cursorX, cursorY, width, height);
    }
    
    @Override
    public void mouseClicked(int button, float cursorX, float cursorY)
    {
        if (button == 0)
        {
            if (hoveringPlayButton(cursorX, cursorY, width, height))
            {
                if (AudioUtil.isPlaying())
                {
                    AudioUtil.pause();
                }
                
                else
                {
                    AudioUtil.resume();
                }
            }
            
            else if (hoveringLeftButton(cursorX, cursorY, width, height))
            {
                Duration currentTime = AudioUtil.getCurrentTime(), position = currentTime.subtract(Duration.seconds(5));
                AudioUtil.changePosition(position);
            }
            
            else if (hoveringRightButton(cursorX, cursorY, width, height))
            {
                Duration currentTime = AudioUtil.getCurrentTime(), position = currentTime.add(Duration.seconds(5));
                AudioUtil.changePosition(position);
            }
        }
    }
}
