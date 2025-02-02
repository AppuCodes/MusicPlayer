package appu26j.musicplayer.utils;

import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import javafx.util.Duration;

public class MidiUtil
{
    private static boolean playing = false, finished = false;
    private static File previousMusicFile = null;
    private static double previousVolume = 1;
    private static Sequencer sequencer;
    
    public static void play(File file)
    {
        try
        {
            Sequence sequence = MidiSystem.getSequence(file);
            
            if (sequencer == null)
            {
                sequencer = MidiSystem.getSequencer();
                sequencer.open();
            }
            
            sequencer.setSequence(sequence);
            sequencer.start();
        } catch (Exception e) { e.printStackTrace(); }
        
        previousMusicFile = file;
        finished = false;
        playing = true;
    }
    
    public static void pause()
    {
        if (playing)
        {
            sequencer.stop();
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
            sequencer.start();
            playing = true;
        }
    }
    
    public static void stop()
    {
        if (playing)
        {
            sequencer.stop();
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
            
            sequencer.setMicrosecondPosition((long) (time.toMillis() * 1000L));
            resume();
        }
    }
    
    public static void changeVolume(double volume)
    {
//        for (MidiChannel channel : synthesizer.getChannels())
//            channel.controlChange(7, (int) (volume * 127));
        previousVolume = volume;
    }
    
    public static double getVolume()
    {
        return previousVolume;
    }
    
    public static Duration getCurrentTime()
    {
        return Duration.millis(sequencer.getMicrosecondPosition() / 1000L);
    }
    
    public static Duration getStopTime()
    {
        return Duration.millis(sequencer.getMicrosecondLength() / 1000L);
    }
    
    public static boolean isMusic(File file)
    {
        return file.getName().endsWith(".mid") || file.getName().endsWith(".midi");
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
