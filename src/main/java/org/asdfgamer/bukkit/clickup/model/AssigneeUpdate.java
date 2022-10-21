package org.asdfgamer.bukkit.clickup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.*;
import java.util.List;

public class AssigneeUpdate
{
    @SerializedName("add")
    @Expose
    private List<Integer> assignees = null;

    public List<Integer> getAssignees()
    {
        if (assignees == null){
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(assignees);
    }

    public void addAssignee(Integer id)
    {
        if (assignees == null){
            assignees = new LinkedList<>();
        }
        assignees.add(id);
    }
}
