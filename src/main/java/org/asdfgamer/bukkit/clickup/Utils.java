package org.asdfgamer.bukkit.clickup;

public class Utils
{
    public static boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

}
