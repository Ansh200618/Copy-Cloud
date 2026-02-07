package com.copycloud.app.api;

import android.util.Log;

import com.copycloud.app.models.ClipData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseClient {
    
    private static final String TAG = "SupabaseClient";
    
    // Supabase credentials - These should match the web app
    private static final String SUPABASE_URL = "https://your-project.supabase.co";
    private static final String SUPABASE_KEY = "your-anon-key";
    private static final String TABLE_NAME = "clips";
    
    private final OkHttpClient client;
    private final Gson gson;
    
    public SupabaseClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
    }
    
    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
    
    public void insertClip(ClipData clipData, ApiCallback<ClipData> callback) {
        String url = SUPABASE_URL + "/rest/v1/" + TABLE_NAME;
        String json = gson.toJson(clipData);
        
        RequestBody body = RequestBody.create(
                json, 
                MediaType.parse("application/json")
        );
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .post(body)
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Insert failed", e);
                callback.onError("Network error: " + e.getMessage());
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<ClipData>>(){}.getType();
                    List<ClipData> clips = gson.fromJson(responseBody, listType);
                    if (clips != null && !clips.isEmpty()) {
                        callback.onSuccess(clips.get(0));
                    } else {
                        callback.onError("No data returned");
                    }
                } else {
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }
    
    public void getClipByCode(String code, ApiCallback<ClipData> callback) {
        String url = SUPABASE_URL + "/rest/v1/" + TABLE_NAME + "?code=eq." + code;
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .get()
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Get failed", e);
                callback.onError("Network error: " + e.getMessage());
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<ClipData>>(){}.getType();
                    List<ClipData> clips = gson.fromJson(responseBody, listType);
                    if (clips != null && !clips.isEmpty()) {
                        callback.onSuccess(clips.get(0));
                    } else {
                        callback.onError("Code not found");
                    }
                } else {
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }
}
