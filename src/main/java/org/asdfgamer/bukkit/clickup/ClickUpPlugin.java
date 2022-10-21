package org.asdfgamer.bukkit.clickup;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.asdfgamer.bukkit.clickup.model.*;
import org.asdfgamer.bukkit.clickup.rest.Executor;
import org.asdfgamer.bukkit.clickup.rest.TaskService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.asdfgamer.bukkit.clickup.Settings.*;

public class ClickUpPlugin extends JavaPlugin
{

    private Retrofit retrofit;

    private TaskService taskService;

    private boolean configLoaded;


    @Override
    public void onEnable()
    {
        super.onEnable();
        retrofit = createRetrofit();
        taskService = retrofit.create(TaskService.class);
        loadConfig();
        initLoadLists();
    }

    private void initLoadLists()
    {
        TaskList taskList = Executor.execute(taskService.listTasks(listID,false));
        if (taskList == null){
            return;
        }
        for (Task task: taskList.getTasks()){
            TaskIDs.addTask(task);
            UserIDs.addUser(task.getCreator());
            for (User user: task.getAssignees()){
                UserIDs.addUser(user);
            }
        }
    }

    private void loadConfig()
    {
        Map<String,Object> configValues = getConfig().getValues(false);
        if (!configValues.containsKey("personalToken")){
            saveDefaultConfig();
        }
        clickupToken = configValues.get("personalToken").toString();
        listID = configValues.get("listID").toString();
        configLoaded = true;
        if ((clickupToken == null) || (clickupToken.isEmpty())||(clickupToken.equalsIgnoreCase("To be filled"))){
            getLogger().severe("No PersonalToken is given in the config. This Plugins requires a token to work.");
            configLoaded = false;
        }
        if ((listID == null) || (listID.isEmpty())||(listID.equalsIgnoreCase("To be filled"))){
            getLogger().severe("No ListID is given in the config. This Plugins requires a listID to work.");
            configLoaded = false;
        }
        ConfigurationSection userSection = getConfig().getConfigurationSection("users");
        if (userSection != null)
        {
            Map<String, Object> users = userSection.getValues(false);
            for (Map.Entry<String, Object> entry : users.entrySet())
            {
                if (entry.getValue() instanceof Integer)
                {
                    UserIDs.attachMCName((Integer) entry.getValue(), entry.getKey());
                }
            }
        }
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
                    addHeader("Authorization", clickupToken).
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
        for (Map.Entry<String,Integer> entry: UserIDs.getAllMCNames().entrySet())
        {
            getConfig().set("users." + entry.getKey(), entry.getValue());
        }
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!configLoaded){
            sender.sendMessage("The Plugin is not configured, this has to be done first.");
            return true;
        }
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
            case "setclickupname":
                return setName(sender,args);
            case "assign":
                return assignTask(sender,args);
            default:
                getLogger().warning("The command '" + command.getName() + "' is not yet implemented." );
                return false;
        }
    }

    private boolean assignTask(CommandSender sender, String[] args)
    {
        Integer id;
        if (args.length == 2){
            id = UserIDs.getIdFromMCName(args[1]);
        }else if(sender instanceof Player) {
            if (args.length != 1){
                return false;
            }
            id = UserIDs.getIdFromMCName(sender.getName());
        }else {
            return false;
        }
        if(id == null){
            if (sender instanceof Player){
                sender.sendMessage("You haven't set your ClickUp Username yet");
                sender.sendMessage("This can be done per /setClickUpName");
            }else{
                sender.sendMessage("The given Player does not exists or hasn't set his username yet.");
            }
        }
        String taskID = parseID(args);
        if (taskID == null){
            return false;
        }
        AssigneeUpdate update = new AssigneeUpdate();
        update.addAssignee(id);
        AssigneeTask updateTask = new AssigneeTask();
        updateTask.setAssignees(update);
        Task task = Executor.execute(taskService.updateStatus(taskID,updateTask));
        if (task != null){
            sender.sendMessage("Added assignee");
        }else{
            sender.sendMessage("There was a problem while adding the assignee");
        }
        return true;
    }

    private boolean setName(CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can attach their username to their ClickUp name");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0){
            return false;
        }
        StringBuilder name = new StringBuilder();
        for (String arg: args){
            name.append(arg).append(" ");
        }
        if(UserIDs.attachMCName(name.toString().trim(),player.getName())){
            player.sendMessage("Your Username was successfully set");
        }else{
            player.sendMessage("There was a Problem while setting your Username.");
            player.sendMessage("Are you sure it is the correct username and you have already?");
            player.sendMessage("You also have to be assigned to a existing task or created a task.");
        }
        return true;
    }

    private boolean createTask(CommandSender sender, String[] args)
    {
        if (args.length != 1){
            return false;
        }
        Task newTask = new Task();
        newTask.setName(args[0]);
        Task task = Executor.execute(taskService.createTask(Settings.listID,newTask));
        if (task == null){
            sender.sendMessage("There was a Problem while creating the status");
        }else{
            sender.sendMessage("The Task was created");
            TaskIDs.addTask(task);
        }
        return true;
    }

    private boolean setStatus(CommandSender sender, String[] args, String newStatus)
    {
        String taskID = parseID(args);
        if (taskID == null){
            return false;
        }
        StatusTask taskUpdate = new StatusTask();
        taskUpdate.setStatus(newStatus);
        Task task = Executor.execute(taskService.updateStatus(taskID,taskUpdate));
        if (task == null){
            sender.sendMessage("There was a Problem while updating the status");
        }else{
            sender.sendMessage("The Status of the Task was updated");
        }
        return true;
    }

    private boolean outputInfo(CommandSender sender, String[] args)
    {
        String taskID = parseID(args);
        if (taskID == null){
            return false;
        }
        Task task = Executor.execute(taskService.getTask(taskID));
        if (task == null){
            sender.sendMessage("There was a problem while loading information about the task");
            return true;
        }
        sender.sendMessage("Name:      " + task.getName());
        sender.sendMessage("Creator:   " + outputUser(task.getCreator()));
        if (task.getAssignees().isEmpty()){
            sender.sendMessage("Assignees: " + "Not yet Assigned");
        }else{
            if (task.getAssignees().size() == 1){
                sender.sendMessage("Assignees: " + outputUser(task.getAssignees().get(0)));
            }
            else{
                sender.sendMessage("Assignees:");
                for (User assignee: task.getAssignees()){
                    sender.sendMessage("      " + outputUser(assignee));
                }
            }
        }
        sender.sendMessage("State:     " + task.getStatus().getStatus());
        List<Task> subtasks = task.getSubtasks(taskService);
        if (!subtasks.isEmpty()){
            if (subtasks.size() == 1){
                sender.sendMessage("Subtask:  " + subtasks.get(0).getName());
            }else{
                sender.sendMessage("Subtask:");
                for (Task subTask: subtasks){
                    sender.sendMessage("      " + subTask.getName());
                }
            }
        }else {
            sender.sendMessage("Subtasks:  This task has no subtasks");
        }
        return true;
    }

    private String outputUser(User user)
    {
        UserIDs.addUser(user);
        String mcName = UserIDs.getMCName(user.getId());
        if (mcName == null){
            return user.getUsername();
        }else {
            return user.getUsername() + " (" + mcName+ ")";
        }
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
        }catch (IndexOutOfBoundsException | NullPointerException e){
            return null;
        }
    }

    private boolean outputList(CommandSender sender)
    {
        TaskList taskList = Executor.execute(taskService.listTasks(Settings.listID,false));
        if (taskList == null)
        {
            sender.sendMessage("There was a Problem while loading the Tasks");
            return true;
        }
        sender.sendMessage("Open Tasks:");
        for (Task task: taskList.getTasks()){
            TaskIDs.addTask(task);
            sender.sendMessage(TaskIDs.getID(task) + " :" + task.getName());
        }
        return true;
    }
}
