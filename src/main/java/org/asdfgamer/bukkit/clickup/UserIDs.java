package org.asdfgamer.bukkit.clickup;

import org.asdfgamer.bukkit.clickup.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserIDs
{
    private final static Map<Integer, User> userIDs = new HashMap<>();
    private final static Map<String, Integer> usernames = new HashMap<>();
    private final static Map<String, Integer> minecraftNames = new HashMap<>();
    private final static Map<Integer, String> minecraftNamesRev = new HashMap<>();

    public static void addUser(User user){
        userIDs.put(user.getId(),user);
        usernames.put(user.getUsername(),user.getId());
    }

    public static boolean attachMCName(String username, String mcName){
        Integer id = usernames.get(username);
        if (id == null){
            return false;
        }
        return attachMCName(id,mcName);
    }
    public static boolean attachMCName(Integer id, String mcName){
        minecraftNames.put(mcName,id);
        minecraftNamesRev.put(id,mcName);
        return true;
    }

    public static User getUser(Integer id){
        return userIDs.get(id);
    }

    public static Integer getIdFromUsername(String username){
        return usernames.get(username);
    }

    public static Integer getIdFromMCName(String mcName){
        return minecraftNames.get(mcName);
    }

    public static String getMCName(Integer id){
        return minecraftNamesRev.get(id);
    }

    public static Map<String,Integer> getAllMCNames(){
        return minecraftNames;
    }
}
