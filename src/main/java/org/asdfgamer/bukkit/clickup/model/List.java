package org.asdfgamer.bukkit.clickup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("orderindex")
    @Expose
    private Integer orderindex;
    @SerializedName("priority")
    @Expose
    private Object priority;
    @SerializedName("assignee")
    @Expose
    private Object assignee;
    @SerializedName("due_date")
    @Expose
    private Object dueDate;
    @SerializedName("start_date")
    @Expose
    private Object startDate;
    @SerializedName("inbound_address")
    @Expose
    private String inboundAddress;
    @SerializedName("archived")
    @Expose
    private Boolean archived;
    @SerializedName("override_statuses")
    @Expose
    private Boolean overrideStatuses;
    @SerializedName("statuses")
    @Expose
    private java.util.List<Status> statuses = null;
    @SerializedName("permission_level")
    @Expose
    private String permissionLevel;

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

    public Integer getOrderindex() {
        return orderindex;
    }

    public void setOrderindex(Integer orderindex) {
        this.orderindex = orderindex;
    }

    public Object getPriority() {
        return priority;
    }

    public void setPriority(Object priority) {
        this.priority = priority;
    }

    public Object getAssignee() {
        return assignee;
    }

    public void setAssignee(Object assignee) {
        this.assignee = assignee;
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

    public String getInboundAddress() {
        return inboundAddress;
    }

    public void setInboundAddress(String inboundAddress) {
        this.inboundAddress = inboundAddress;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getOverrideStatuses() {
        return overrideStatuses;
    }

    public void setOverrideStatuses(Boolean overrideStatuses) {
        this.overrideStatuses = overrideStatuses;
    }

    public java.util.List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(java.util.List<Status> statuses) {
        this.statuses = statuses;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
