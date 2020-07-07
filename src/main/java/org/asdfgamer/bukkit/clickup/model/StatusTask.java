package org.asdfgamer.bukkit.clickup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatusTask
{
    @SerializedName("status")
    @Expose
    private String status = null;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
