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
    
    // Supabase credentials - Matching web app configuration
    private static final String SUPABASE_URL = "https://luunzeonlmzvmewaucqj.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx1dW56ZW9ubG16dm1ld2F1Y3FqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzAxMzAyNzEsImV4cCI6MjA4NTcwNjI3MX0.qQpWEGFLg6Weof0NO_ApntTrGGYVsrsNB2zaujRMuFY";
    private static final String TABLE_NAME = "clips";
    private static final String STORAGE_BUCKET = "uploads";
    
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
    
    /**
     * Upload file to Supabase Storage
     */
    public void uploadFile(byte[] fileData, String fileName, String mimeType, ApiCallback<String> callback) {
        String url = SUPABASE_URL + "/storage/v1/object/" + STORAGE_BUCKET + "/" + fileName;
        
        RequestBody body = RequestBody.create(fileData, MediaType.parse(mimeType));
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Content-Type", mimeType)
                .post(body)
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "File upload failed", e);
                callback.onError("Upload error: " + e.getMessage());
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Return the file path
                    callback.onSuccess(fileName);
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    Log.e(TAG, "Upload failed: " + errorBody);
                    callback.onError("Upload failed: " + response.code());
                }
            }
        });
    }
    
    /**
     * Get public URL for uploaded file
     */
    public String getFilePublicUrl(String fileName) {
        return SUPABASE_URL + "/storage/v1/object/public/" + STORAGE_BUCKET + "/" + fileName;
    }
    
    /**
     * Download file from Supabase Storage
     */
    public void downloadFile(String fileName, ApiCallback<byte[]> callback) {
        String url = getFilePublicUrl(fileName);
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "File download failed", e);
                callback.onError("Download error: " + e.getMessage());
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    byte[] fileData = response.body().bytes();
                    callback.onSuccess(fileData);
                } else {
                    callback.onError("Download failed: " + response.code());
                }
            }
        });
    }
}
