package appu26j.musicplayer.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.File;

import appu26j.aguiu.gui.GuiScreen;
import appu26j.aguiu.gui.font.FontRenderer;
import appu26j.musicplayer.utils.AudioUtil;

public class HomeScreen extends GuiScreen
{
    private String font = "appu26j/musicplayer/assets/SegoeUI.ttf";
    private FontRenderer bigText = new FontRenderer(font, 64), smallText = new FontRenderer(font, 32), mediumText = new FontRenderer(font, 48);
    
    @Override
    public void drawScreen(float cursorX, float cursorY)
    {
        bigText.drawString("Welcome", 12, 0, Color.BLACK);
        int offset = 0;
        
        for (File file : AudioUtil.DIRECTORY.listFiles())
        {
            if (AudioUtil.isMusic(file))
            {
                String name = file.getName(), extension = name.substring(name.indexOf("."));
                drawRect(48, 120 + offset, width - 48, 180 + offset, new Color(0, 128, 255, ICIB(cursorX, cursorY, 48, 120 + offset, width - 48, 180 + offset) ? 50 : 12));
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
        
        drawPlayButton(false, false, cursorX, cursorY, width, height);
    }
    
    public static void drawPlayButton(boolean enabled, boolean playing, float cursorX, float cursorY, float width, float height)
    {
        float w = width / 2;
        
        if (ICIB(cursorX, cursorY, w - 40, height - 96, w + 40, height - 16))
        {
            drawCircle(w, height - 56, 40, new Color(0, 175, 255), new Color(0, 122, 178));
        }
        
        else
        {
            drawCircle(w, height - 56, 40, new Color(0, 128, 255), new Color(0, 91, 182));
        }
        
        if (playing)
        {
            drawRect(w - 12, height - 72, w - 5, height - 36, Color.WHITE);
            drawRect(w + 5, height - 72, w + 12, height - 36, Color.WHITE);
        }
        
        else
        {
            float h = height - 70;
            glColor4f(1, 1, 1, 1);
            glBegin(GL_TRIANGLES);
            glVertex2f(w - 12, h);
            glVertex2f(w - 12, h + 32);
            glVertex2f(w + 16, h + 16);
            glEnd();
            glEnable(GL_LINE_SMOOTH);
            glBegin(GL_LINE_LOOP);
            glVertex2f(w - 12, h);
            glVertex2f(w - 12, h + 32);
            glVertex2f(w + 16, h + 16);
            glEnd();
        }
    }
    
    private static void drawCircle(float x, float y, float radius, Color baseColor, Color darkColor)
    {
//        Color temp = baseColor;
//        baseColor = darkColor;
//        darkColor = temp;
        float baseRed = baseColor.getRed() / 255F, baseGreen = baseColor.getGreen() / 255F, baseBlue = baseColor.getBlue() / 255F, baseAlpha = baseColor.getAlpha() / 255F;
        float darkRed = darkColor.getRed() / 255F, darkGreen = darkColor.getGreen() / 255F, darkBlue = darkColor.getBlue() / 255F, darkAlpha = darkColor.getAlpha() / 255F;
        double angle = Math.PI / 45;
        glColor4f(baseRed, baseGreen, baseBlue, baseAlpha);
        glBegin(GL_TRIANGLE_FAN);
        
        for (int i = 0; i <= 90; i++)
        {
            /**
             * Accidentally discovered this cool gradient effect LOL
             */
            if (i == 1)
            {
                glColor4f(darkRed, darkGreen, darkBlue, darkAlpha);
            }
            
            glVertex2d(x + radius * Math.sin(angle * i), y + radius * Math.cos(angle * i));
        }
        
        glEnd();
        glEnable(GL_LINE_SMOOTH);
        glBegin(GL_LINE_LOOP);
        
        for (int i = 0; i <= 90; i++)
        {
            glVertex2d(x + radius * Math.sin(angle * i), y + radius * Math.cos(angle * i));
        }
        
        glEnd();
        glDisable(GL_LINE_SMOOTH);
    }
}
