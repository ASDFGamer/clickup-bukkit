package org.asdfgamer.bukkit.clickup.rest;

import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class Executor
{
    public static <T> T execute(Call<T> call){
        try
        {
            Response<T> response = call.execute();
            if (response.isSuccessful()){
                return response.body();
            }else{
                return null;
            }
        } catch (IOException e)
        {
            return null;
        }
    }
}
