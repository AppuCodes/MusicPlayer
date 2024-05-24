package appu26j.musicplayer.utils;

import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.*;
import javafx.util.Duration;

public class AudioUtil
{
    public static File DIRECTORY = new File(System.getProperty("user.home"));
    private static boolean playing = false, finished = false;
    private static File previousMusicFile = null;
    private static MediaPlayer mediaPlayer;
    
    static
    {
        File musicFolder = new File(DIRECTORY, "Music");
        
        if (musicFolder.exists())
        {
            DIRECTORY = musicFolder;
        }
        
        new JFXPanel();
    }
    
    public static void play(File file)
    {
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        previousMusicFile = file;
        mediaPlayer.play();
        playing = true;
        
        new Thread(() ->
        {
            while (true)
            {
                if ((getStopTime().toSeconds() - getCurrentTime().toSeconds()) < 0.5)
                {
                    stop();
                    break;
                }
            }
        }).start();
    }
    
    public static void pause()
    {
        if (playing)
        {
            mediaPlayer.pause();
            playing = false;
        }
    }
    
    public static void resume()
    {
        if (finished)
        {
            play(previousMusicFile);
            finished = false;
        }
        
        else if (!playing)
        {
            mediaPlayer.play();
            playing = true;
        }
    }
    
    public static void stop()
    {
        if (playing)
        {
            mediaPlayer.stop();
            playing = false;
            finished = true;
        }
    }
    
    public static void changePosition(Duration time)
    {
        pause();
        
        try
        {
            Thread.sleep(5);
        }
        
        catch (Exception e)
        {
        }
        
        mediaPlayer.setStartTime(time);
        resume();
    }
    
    public static Duration getCurrentTime()
    {
        return mediaPlayer.getCurrentTime();
    }
    
    public static Duration getStopTime()
    {
        return mediaPlayer.getStopTime();
    }
    
    public static boolean isMusic(File file)
    {
        return file.getName().endsWith(".mp3") || file.getName().endsWith(".wav") || file.getName().endsWith(".ogg");
    }
    
    public static boolean isPlaying()
    {
        return playing;
    }
    
    public static boolean hasFinished()
    {
        return finished;
    }
}
