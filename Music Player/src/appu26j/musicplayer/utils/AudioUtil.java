package appu26j.musicplayer.utils;

import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioUtil
{
    public static File DIRECTORY = new File(System.getProperty("user.home"));
    private static boolean playing = false, finished = false;
    private static File previousMusicFile = null;
    private static double previousVolume = 1;
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
        mediaPlayer.setVolume(previousVolume);
        previousMusicFile = file;
        mediaPlayer.play();
        finished = false;
        playing = true;
        
        new Thread(() ->
        {
            while (true)
            {
                if (getStopTime().subtract(getCurrentTime()).toSeconds() < 0.5)
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
        if (Math.abs(getCurrentTime().subtract(time).toSeconds()) > 1)
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
    }
    
    public static void changeVolume(double volume)
    {
        mediaPlayer.setVolume(volume);
        previousVolume = volume;
    }
    
    public static double getVolume()
    {
        return mediaPlayer.getVolume();
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
        return file.getName().endsWith(".mp3") || file.getName().endsWith(".wav") || file.getName().endsWith(".ogg") || file.getName().endsWith(".aac") || file.getName().endsWith(".aiff") || file.getName().endsWith(".mid") || file.getName().endsWith(".midi");
    }
    
    public static boolean isPlaying()
    {
        return playing;
    }
    
    public static boolean hasFinished()
    {
        return finished;
    }
    
    public static void init() {}
}
