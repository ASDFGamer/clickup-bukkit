package org.asdfgamer.bukkit.clickup;

import java.util.ArrayList;
import java.util.List;
import org.asdfgamer.bukkit.clickup.model.Task;

public class TaskIDs {
    
    private static final List<Task> tasks = new ArrayList<>();
    
    public static int getID(Task task){
        return tasks.indexOf(task)+1;
    }
    
    public static Task getTask(int id){
        return tasks.get(id-1);
    }
    
    public static void addTask(Task task){
        if (!tasks.contains(task)){
            tasks.add(task);
        }
    }
    
}
