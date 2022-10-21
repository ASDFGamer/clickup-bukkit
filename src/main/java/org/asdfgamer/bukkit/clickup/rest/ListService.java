package org.asdfgamer.bukkit.clickup.rest;

import org.asdfgamer.bukkit.clickup.model.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ListService
{
    @GET("api/v2/list/{list_id}")
    Call<List> getList(@Path("list_id") String list_id);
}
