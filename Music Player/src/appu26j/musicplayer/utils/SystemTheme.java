package appu26j.musicplayer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemTheme
{
    private static final String DARK_THEME_CMD = "reg query \"HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize\" /v AppsUseLightTheme";
    
    public static final boolean isDarkMode()
    {
        switch (getOperatingSystem())
        {
            case "Windows":
                return checkWindowsTheme();
            case "Mac":
                return checkMacTheme();
            default:
                return false;
        }
    }
    
    private static boolean checkMacTheme()
    {
        try
        {
            Process process = Runtime.getRuntime().exec("defaults read -g AppleInterfaceStyle");
            
            try (InputStreamReader streamReader = new InputStreamReader(process.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(streamReader))
            {
                String line;
                
                while ((line = bufferedReader.readLine()) != null)
                {
                    if (line.equals("Dark"))
                        return true;
                }
                
                return false;
            }
        }
        
        catch (IOException e) { return false; }
    }
    
    private static boolean checkWindowsTheme()
    {
        try
        {
            Process process = Runtime.getRuntime().exec(DARK_THEME_CMD);
            
            try (InputStreamReader streamReader = new InputStreamReader(process.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(streamReader))
            {
                StringBuilder output = new StringBuilder();
                String line;
                
                while ((line = bufferedReader.readLine()) != null)
                    output.append(line);
                
                int index = output.indexOf("REG_DWORD");
                if (index == -1) return false;
                
                String text = output.substring(index + "REG_DWORD".length()).trim();
                return Integer.parseInt(text.substring("0x".length()), 16) == 0;
            }
        }
        
        catch (IOException e) { return false; }
    }
    
    public static String getOperatingSystem()
    {
        String system = System.getProperty("os.name").toLowerCase();
        return system.indexOf("win") >= 0 ? "Windows" : (system.indexOf("mac") >= 0 ? "Mac" : "Other");
    }
}
