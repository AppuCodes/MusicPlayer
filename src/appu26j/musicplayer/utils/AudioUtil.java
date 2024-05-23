package appu26j.musicplayer.utils;

import java.io.*;

import javazoom.jl.player.Player;

public class AudioUtil
{
    public static File DIRECTORY = new File(System.getProperty("user.home"));
    private static int previousPosition = 0;
    private static File previousFile = null;
    private static Player player = null;
    
    static
    {
        File musicFolder = new File(DIRECTORY, "Music");
        
        if (musicFolder.exists())
        {
            DIRECTORY = musicFolder;
        }
    }
    
    public static void play(File file)
    {
        play(file, 0);
    }
    
    public static void play(File file, int position)
    {
        try (FileInputStream fileInputStream = new FileInputStream(file))
        {
            player = new Player(fileInputStream);
            player.play(position);
        }
        
        catch (Exception e)
        {
        }
        
        previousFile = file;
    }
    
    public static void pause()
    {
        previousPosition = player.getPosition();
        stop();
    }
    
    public static void resume()
    {
        play(previousFile, previousPosition);
    }
    
    public static void stop()
    {
        player.close();
    }
    
    public static boolean isMusic(File file)
    {
        return file.getName().endsWith(".mp3") || file.getName().endsWith(".wav") || file.getName().endsWith(".ogg");
    }
}
