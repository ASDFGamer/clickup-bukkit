package org.asdfgamer.bukkit.clickup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.asdfgamer.bukkit.clickup.Settings;
import org.asdfgamer.bukkit.clickup.TaskIDs;
import org.asdfgamer.bukkit.clickup.rest.Executor;
import org.asdfgamer.bukkit.clickup.rest.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Task
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("orderindex")
    @Expose
    private String orderindex;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("date_updated")
    @Expose
    private String dateUpdated;
    @SerializedName("date_closed")
    @Expose
    private Object dateClosed;
    @SerializedName("creator")
    @Expose
    private User creator;
    @SerializedName("assignees")
    @Expose
    private List<User> assignees = null;
    @SerializedName("checklists")
    @Expose
    private List<Object> checklists = null;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("parent")
    @Expose
    private Object parent;
    @SerializedName("priority")
    @Expose
    private Object priority;
    @SerializedName("due_date")
    @Expose
    private Object dueDate;
    @SerializedName("start_date")
    @Expose
    private Object startDate;
    @SerializedName("time_estimate")
    @Expose
    private Object timeEstimate;
    @SerializedName("time_spent")
    @Expose
    private Object timeSpent;
    @SerializedName("url")
    @Expose
    private String url;

    private List<Task> subtasks = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOrderindex() {
        return orderindex;
    }

    public void setOrderindex(String orderindex) {
        this.orderindex = orderindex;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Object getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Object dateClosed) {
        this.dateClosed = dateClosed;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<User> assignees) {
        this.assignees = assignees;
    }

    public List<Object> getChecklists() {
        return checklists;
    }

    public void setChecklists(List<Object> checklists) {
        this.checklists = checklists;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public Object getPriority() {
        return priority;
    }

    public void setPriority(Object priority) {
        this.priority = priority;
    }

    public Object getDueDate() {
        return dueDate;
    }

    public void setDueDate(Object dueDate) {
        this.dueDate = dueDate;
    }

    public Object getStartDate() {
        return startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }

    public Object getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(Object timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public Object getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Object timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Task)){
            return false;
        }
        return ((Task)object).getId().equals(id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public List<Task> getSubtasks(TaskService taskService)
    {
        if (subtasks == null){

            TaskList taskList = Executor.execute(taskService.listTasks(Settings.listID,false,id));
            if (taskList != null){
                subtasks = taskList.getTasks();
            }else{
               subtasks = new ArrayList<>();
            }
        }
        return subtasks;
    }
}
