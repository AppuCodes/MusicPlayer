package appu26j.musicplayer.gui;

import static appu26j.musicplayer.utils.RenderUtil.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.File;

import appu26j.aguiu.AgWindow;
import appu26j.aguiu.gui.GuiScreen;
import appu26j.musicplayer.utils.AudioUtil;
import javafx.util.Duration;

public class PlayScreen extends GuiScreen
{
    private boolean volumePopUp = false, draggingVolumeSlider = false, draggingPlaybackSlider = false;
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
        drawStopButton(true, cursorX, cursorY, width, height);
        drawVolumeButton(true, cursorX, cursorY, width, height);
        
        if (volumePopUp)
        {
            float w = width / 2;
            drawRect(w + 125, height - 250, w + 165, height - 90, new Color(235, 245, 250, ICIB(cursorX, cursorY, w + 125, height - 250, w + 165, height - 90) ? 125 : 75));
            progress = (float) AudioUtil.getVolume();
            drawGradient(w + 141, height - (100 + (140 * progress)), w + 149, height - 100, new Color(0, 128, 255), new Color(0, 128, 255).darker());
            smallText.drawString((int) (progress * 100) + "%", w + 175, height - (122 + (128 * progress)), Color.DARK_GRAY);
            
            if (draggingVolumeSlider)
            {
                progress = clamp(0, 1, (1 - ((cursorY - (height - 240)) / 140)));
                AudioUtil.changeVolume(progress);
            }
        }
        
        if (draggingPlaybackSlider)
        {
            progress = clamp(0, 1, (cursorX - 16) / (width - 32));
            AudioUtil.changePosition(stopTime.multiply(progress));
        }
    }
    
    @Override
    public void mouseClicked(int button, float cursorX, float cursorY)
    {
        boolean madeFalse = false;
        float w = width / 2;
        
        if (volumePopUp)
        {
            if (ICIB(cursorX, cursorY, w + 125, height - 250, w + 165, height - 90))
            {
                draggingVolumeSlider = true;
            }
            
            else
            {
                volumePopUp = false;
                madeFalse = true;
            }
        }
        
        if (!volumePopUp && ICIB(cursorX, cursorY, 0, height - 137, width, height - 100))
        {
            draggingPlaybackSlider = true;
        }
        
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
            
            else if (hoveringRightButton(cursorX, cursorY, width, height))
            {
                Duration currentTime = AudioUtil.getCurrentTime(), position = currentTime.add(Duration.seconds(5));
                AudioUtil.changePosition(position);
            }
            
            else if (hoveringLeftButton(cursorX, cursorY, width, height))
            {
                Duration currentTime = AudioUtil.getCurrentTime(), position = currentTime.subtract(Duration.seconds(5));
                AudioUtil.changePosition(position);
            }
            
            else if (hoveringStopButton(cursorX, cursorY, width, height))
            {
                AudioUtil.stop();
                AgWindow.displayScreen(new HomeScreen());
            }
            
            else if (!madeFalse && hoveringVolumeButton(cursorX, cursorY, width, height))
            {
                volumePopUp = true;
            }
        }
    }
    
    @Override
    public void mouseReleased(int button, float cursorX, float cursorY)
    {
        if (draggingVolumeSlider)
        {
            draggingVolumeSlider = false;
        }
        
        if (draggingPlaybackSlider)
        {
            draggingPlaybackSlider = false;
        }
    }
    
    @Override
    public void keyPressed(int key)
    {
        if (key == 256) // ESC Key
        {
            if (volumePopUp)
            {
                volumePopUp = false;
            }
            
            else
            {
                AudioUtil.stop();
                AgWindow.displayScreen(new HomeScreen());
            }
        }
        
        else if (key == 32) // Space Key
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
        
        else if (key == 262) // Right Arrow
        {
            Duration currentTime = AudioUtil.getCurrentTime(), position = currentTime.add(Duration.seconds(5));
            AudioUtil.changePosition(position);
        }
        
        else if (key == 263) // Left Arrow
        {
            Duration currentTime = AudioUtil.getCurrentTime(), position = currentTime.subtract(Duration.seconds(5));
            AudioUtil.changePosition(position);
        }
        
        else if (key == 264) // Down Arrow
        {
            volumePopUp = true;
            double volume = AudioUtil.getVolume() - 0.1;
            volume = volume < 0 ? 0 : volume;
            AudioUtil.changeVolume(volume);
        }
        
        else if (key == 265) // Up Arrow
        {
            volumePopUp = true;
            double volume = AudioUtil.getVolume() + 0.1;
            volume = volume > 1 ? 1 : volume;
            AudioUtil.changeVolume(volume);
        }
    }
    
    private float clamp(float minimum, float maximum, float value)
    {
        return value < minimum ? minimum : value > maximum ? maximum : value;
    }
}
