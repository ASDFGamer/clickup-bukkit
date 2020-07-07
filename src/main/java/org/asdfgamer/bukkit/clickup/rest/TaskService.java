package org.asdfgamer.bukkit.clickup.rest;

import okhttp3.RequestBody;
import org.asdfgamer.bukkit.clickup.model.StatusTask;
import org.asdfgamer.bukkit.clickup.model.TaskList;
import org.asdfgamer.bukkit.clickup.model.Task;
import retrofit2.Call;
import retrofit2.http.*;

public interface TaskService
{
    @GET("api/v2/list/{list_id}/task")
    Call<TaskList> listTasks(@Path("list_id") String list_id);

    @GET("api/v2/task/{task_id}")
    Call<Task> getTask(@Path("task_id") String task_id);

    @PUT("api/v2/task/{task_id}")
    Call<Task> updateStatus(@Path("task_id") String task_id, @Body StatusTask task);

    @POST("api/v2/list/{list_id}/task")
    Call<Task> createTask(@Path("list_id") String list_id, @Body Task task);
}
