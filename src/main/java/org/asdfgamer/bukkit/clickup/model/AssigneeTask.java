package org.asdfgamer.bukkit.clickup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssigneeTask
{
    @SerializedName("assignees")
    @Expose
    private AssigneeUpdate assignees = null;

    public AssigneeUpdate getAssignees()
    {
        return assignees;
    }

    public void setAssignees(AssigneeUpdate assignees)
    {
        this.assignees = assignees;
    }
}
