package org.asdfgamer.bukkit.clickup;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.asdfgamer.bukkit.clickup.model.StatusTask;
import org.asdfgamer.bukkit.clickup.model.Task;
import org.asdfgamer.bukkit.clickup.model.TaskList;
import org.asdfgamer.bukkit.clickup.model.User;
import org.asdfgamer.bukkit.clickup.rest.Executor;
import org.asdfgamer.bukkit.clickup.rest.TaskService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ClickUpPlugin extends JavaPlugin
{

    private Retrofit retrofit;

    private TaskService taskService;


    @Override
    public void onEnable()
    {
        super.onEnable();
        retrofit = createRetrofit();
        taskService = retrofit.create(TaskService.class);
//        getConfig().
    }

    private Retrofit createRetrofit()
    {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            Request request = chain.
                    request().
                    newBuilder().
                    addHeader("Authorization", Settings.clickupToken).
                    build();
            return chain.proceed(request);
        });
        OkHttpClient client = builder.build();
        return new Retrofit.Builder().
                baseUrl("https://api.clickup.com/").
                client(client).
                addConverterFactory(GsonConverterFactory.create()).
                build();
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        switch (command.getName().toLowerCase()){
            case "tasks":
                return outputList(sender);
            case "task":
                return outputInfo(sender,args);
            case "new":
                return createTask(sender,args);
            case "wip":
            case "inProgress":
                return setStatus(sender,args,"in progress");
            case "review":
                return setStatus(sender,args,"review");
            case "close":
                return setStatus(sender,args,"Closed");
            default:
                getLogger().warning("The command '" + command.getName() + "' is not yet implemented." );
                return false;
        }
    }

    private boolean createTask(CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player)){
            getLogger().warning("Only players can output infos for a task.");
            return false;
        }
        Player player = (Player)sender;
        if (args.length != 1){
            return false;
        }
        Task newTask = new Task();
        newTask.setName(args[0]);
        Task task = Executor.execute(taskService.createTask(Settings.listID,newTask));
        if (task == null){
            player.sendMessage("There was a Problem while creating the status");
        }else{
            player.sendMessage("The Task was created");
        }
        return true;
    }

    private boolean setStatus(CommandSender sender, String[] args, String newStatus)
    {
        if (!(sender instanceof Player)){
            getLogger().warning("Only players can output infos for a task.");
            return false;
        }
        Player player = (Player)sender;
        String taskID = parseID(args);
        if (taskID == null){
            return false;
        }
        StatusTask taskUpdate = new StatusTask();
        taskUpdate.setStatus(newStatus);
        Task task = Executor.execute(taskService.updateStatus(taskID,taskUpdate));
        if (task == null){
            player.sendMessage("There was a Problem while updating the status");
        }else{
             player.sendMessage("The Status of the Task was updated");
        }
        return true;
    }

    private boolean outputInfo(CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player)){
            getLogger().warning("Only players can output infos for a task.");
            return false;
        }
        Player player = (Player)sender;
        String taskID = parseID(args);
        if (taskID == null){
            return false;
        }
        Task task = Executor.execute(taskService.getTask(taskID));
        if (task == null){
            player.sendMessage("There was a problem while loading information about the task");
            return true;
        }
        player.sendMessage("Name:      " + task.getName());
        player.sendMessage("Creator:   " + task.getCreator().getUsername());
        if (task.getAssignees().isEmpty()){
            player.sendMessage("Assignees: " + "Not yet Assigned");
        }else{
            if (task.getAssignees().size() == 1){
                player.sendMessage("Assignees: " + task.getAssignees().get(0).getUsername());
            }
            else{
                player.sendMessage("Assignees:");
                for (User assignee: task.getAssignees()){
                    player.sendMessage("\t\t" + assignee.getUsername());
                }
            }
        }
        player.sendMessage("State:     " + task.getStatus().getStatus());
        return true;
    }

    private String parseID(String[] args)
    {
        if (args.length != 1){
            getLogger().warning("Number of arguments is " + args.length + " and not 1.");
            return null;
        }
        if (!Utils.isInteger(args[0])){
            getLogger().warning("The Argument " + args[0] + " is no valid number.");
            return null;
        }
        int internID = Integer.parseInt(args[0]);
        try
        {
            return TaskIDs.getTask(internID).getId();
        }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
            return null;
        }
    }

    private boolean outputList(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            getLogger().warning("Only players can output infos for tasks.");
            return false;
        }
        Player player = (Player)sender;
        TaskList taskList = Executor.execute(taskService.listTasks(Settings.listID));
        if (taskList == null)
        {
            player.sendMessage("There was a Problem while loading the Tasks");
            return true;
        }
        player.sendMessage("Open Tasks:");
        for (Task task: taskList.getTasks()){
            TaskIDs.addTask(task);
            player.sendMessage(TaskIDs.getID(task) + " :" + task.getName() + " "+ task.getId());
        }
        return true;
    }
}
