package org.asdfgamer.bukkit.clickup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.*;
import java.util.List;

public class Status {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("orderindex")
    @Expose
    private Integer orderindex;
    @SerializedName("type")
    @Expose
    private String type;

    private final Set<Status> allStatuses = new HashSet<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        allStatuses.add(this);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getOrderindex() {
        return orderindex;
    }

    public void setOrderindex(Integer orderindex) {
        this.orderindex = orderindex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Status> getAllStatuses(){
        return Collections.unmodifiableSet(allStatuses);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Status){
            return ((Status)obj).getStatus().equals(status);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(status);
    }
}
