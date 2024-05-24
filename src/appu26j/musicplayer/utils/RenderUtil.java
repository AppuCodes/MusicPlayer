package appu26j.musicplayer.utils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import appu26j.aguiu.gui.GuiScreen;
import appu26j.aguiu.gui.font.FontRenderer;

public class RenderUtil extends GuiScreen
{
    private static String font = "appu26j/musicplayer/assets/SegoeUI.ttf";
    public static FontRenderer bigText = new FontRenderer(font, 64), smallText = new FontRenderer(font, 32), mediumText = new FontRenderer(font, 48);
    
    public static void drawPlayButton(boolean enabled, boolean playing, float cursorX, float cursorY, float width, float height)
    {
        float w = width / 2;
        
        if (hoveringPlayButton(cursorX, cursorY, width, height) && enabled)
        {
            drawCircle(w, height - 56, 40, new Color(0, 175, 255), new Color(0, 110, 200));
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
            
            if (enabled)
            {
                glColor4f(1, 1, 1, 1);
            }
            
            else
            {
                glColor4f(0.6F, 0.7F, 0.8F, 1);
            }
            
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
    
    public static void drawArrows(boolean enabled, float cursorX, float cursorY, float width, float height)
    {
        float w = width / 2;
        
        /**
         * Left
         */
        
        if (enabled)
        {
            drawRect(w - 110, height - 75, w - 55, height - 35, new Color(235, 245, 250, hoveringLeftButton(cursorX, cursorY, width, height) ? 125 : 75));
        }
        
        else
        {
            drawRect(w - 110, height - 75, w - 55, height - 35, new Color(0, 128, 255, 25));
        }
        
        mediumText.drawString("<", w - 105, height - 82, enabled ? Color.DARK_GRAY : new Color(153, 179, 204));
        mediumText.drawString("<", w - 89, height - 82, enabled ? Color.DARK_GRAY : new Color(153, 179, 204));
        
        /**
         * Right
         */
        
        if (enabled)
        {
            drawRect(w + 55, height - 75, w + 110, height - 35, new Color(235, 245, 250, hoveringRightButton(cursorX, cursorY, width, height) ? 125 : 75));
        }
        
        else
        {
            drawRect(w + 55, height - 75, w + 110, height - 35, new Color(0, 128, 255, 25));
        }
        
        mediumText.drawString(">", w + 79, height - 82, enabled ? Color.DARK_GRAY : new Color(153, 179, 204));
        mediumText.drawString(">", w + 62, height - 82, enabled ? Color.DARK_GRAY : new Color(153, 179, 204));
    }
    
    public static boolean hoveringPlayButton(float cursorX, float cursorY, float width, float height)
    {
        float w = width / 2;
        return ICIB(cursorX, cursorY, w - 40, height - 96, w + 40, height - 16);
    }
    
    public static boolean hoveringLeftButton(float cursorX, float cursorY, float width, float height)
    {
        float w = width / 2;
        return ICIB(cursorX, cursorY, w - 110, height - 75, w - 55, height - 35);
    }
    
    public static boolean hoveringRightButton(float cursorX, float cursorY, float width, float height)
    {
        float w = width / 2;
        return ICIB(cursorX, cursorY, w + 55, height - 75, w + 110, height - 35);
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
    
    @Override
    public void drawScreen(float cursorX, float cursorY) {}
}
