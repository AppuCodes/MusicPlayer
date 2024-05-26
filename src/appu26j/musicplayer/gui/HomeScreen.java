package appu26j.musicplayer.gui;

import static appu26j.musicplayer.utils.RenderUtil.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.io.File;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.nfd.NativeFileDialog;

import appu26j.aguiu.AgWindow;
import appu26j.aguiu.gui.GuiScreen;
import appu26j.musicplayer.utils.*;

public class HomeScreen extends GuiScreen
{
    private int scroll = 0, refreshRate = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
    private boolean allVisible = false, scrollDown = false, scrollUp = false;
    
    @Override
    public void drawScreen(float cursorX, float cursorY)
    {
        if (scrollDown)
        {
            mouseScrolled(0, -0.25 / (refreshRate / 60D));
        }
        
        if (scrollUp)
        {
            mouseScrolled(0, 0.25 / (refreshRate / 60D));
        }
        
        bigText.drawString("Welcome", 12, 0, Color.BLACK);
        float maxWidth = width - 48;
        int offset = -scroll;
        glEnable(GL_SCISSOR_TEST);
        RenderUtil.scissor(0, 120, width, height - 110);
        allVisible = true;
        
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
                
                if ((180 + offset) > (height - 110))
                {
                    allVisible = false;
                }
                
                drawRect(48, 120 + offset, maxWidth, 180 + offset, new Color(0, 128, 255, ICIB(cursorX, cursorY, 48, 120 + offset, maxWidth, 180 + offset) ? 50 : 25));
                drawGradient(56, 133 + offset, 113, 164 + offset, Color.MAGENTA, Color.MAGENTA.darker());
                smallText.drawString(extension.substring(1), 85 - (smallText.getWidth(extension.substring(1)) / 2), 130 + offset, Color.WHITE);
                mediumText.drawString(name.replace(extension, ""), 123, 120 + offset, Color.DARK_GRAY);
                offset += 60;
            }
        }
        
        glDisable(GL_SCISSOR_TEST);
        
        if (offset != 0)
        {
            float x = smallText.getWidth("You may click one to play or choose") + 48, textWidth = smallText.getWidth("here");
            smallText.drawString("You may click one to play or choose", 48, 64, Color.BLACK);
            smallText.drawString("here", 436, 64, Color.BLUE);
            
            if (ICIB(cursorX, cursorY, x, 65, x + textWidth + 15, 100))
            {
                drawRect(x + 7, 94, x + textWidth + 7, 96, Color.BLUE);
            }
        }
        
        else
        {
            float x = smallText.getWidth("You may choose one to play") + 48, textWidth = smallText.getWidth("here");
            smallText.drawString("You may choose one to play", 48, 64, Color.BLACK);
            smallText.drawString("here", 355, 64, Color.BLUE);
            
            if (ICIB(cursorX, cursorY, x, 65, x + textWidth + 15, 100))
            {
                drawRect(x + 7, 94, x + textWidth + 7, 96, Color.BLUE);
            }
        }
        
        drawPlayButton(false, false, cursorX, cursorY, width, height);
        drawArrows(false, cursorX, cursorY, width, height);
        drawStopButton(false, cursorX, cursorY, width, height);
        drawVolumeButton(false, cursorX, cursorY, width, height);
    }
    
    @Override
    public void mouseScrolled(double xOffset, double yOffset)
    {
        if (yOffset < 0)
        {
            if (!allVisible)
            {
                scroll -= (yOffset * 30);
            }
        }
        
        else if (scroll != 0)
        {
            scroll -= (yOffset *= 30);
            scroll = scroll < 0 ? 0 : scroll;
        }
    }
    
    @Override
    public void mouseClicked(int button, float cursorX, float cursorY)
    {
        float maxWidth = width - 48;
        int offset = -scroll;
        
        for (File file : AudioUtil.DIRECTORY.listFiles())
        {
            if (AudioUtil.isMusic(file))
            {
                float textWidth = mediumText.getWidth(file.getName()) + 48;
                
                if (textWidth > maxWidth)
                {
                    maxWidth = textWidth;
                }
                
                if (ICIB(cursorX, cursorY, 48, 120 + offset, maxWidth, 180 + offset) && ICIB(cursorX, cursorY, 0, 120, width, height - 110))
                {
                    AgWindow.displayScreen(new PlayScreen(file));
                }
                
                offset += 60;
            }
        }
        
        if (offset != 0)
        {
            float x = smallText.getWidth("You may click one to play or choose") + 48, textWidth = smallText.getWidth("here");
            
            if (ICIB(cursorX, cursorY, x, 65, x + textWidth + 15, 100))
            {
                openFileChooser();
            }
        }
        
        else
        {
            float x = smallText.getWidth("You may choose one to play") + 48, textWidth = smallText.getWidth("here");
            
            if (ICIB(cursorX, cursorY, x, 65, x + textWidth + 15, 100))
            {
                openFileChooser();
            }
        }
    }
    
    @Override
    public void initGui(float width, float height)
    {
        super.initGui(width, height);
        scroll = 0;
    }
    
    @Override
    public void keyPressed(int key)
    {
        if (key == 264) // Down Arrow
        {
            scrollDown = true;
        }
        
        else if (key == 265) // Up Arrow
        {
            scrollUp = true;
        }
    }
    
    @Override
    public void keyReleased(int key)
    {
        if (key == 264) // Down Arrow
        {
            scrollDown = false;
        }
        
        else if (key == 265) // Up Arrow
        {
            scrollUp = false;
        }
    }
    
    private void openFileChooser()
    {
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            PointerBuffer buffer = stack.mallocPointer(1);
            int result = NativeFileDialog.NFD_OpenDialog(buffer, null, AudioUtil.DIRECTORY.getAbsolutePath());
            
            if (result == NativeFileDialog.NFD_OKAY)
            {
                File file = new File(buffer.getStringASCII());
                
                if (AudioUtil.isMusic(file))
                {
                    AgWindow.displayScreen(new PlayScreen(file));
                }
            }
        }
    }
}
