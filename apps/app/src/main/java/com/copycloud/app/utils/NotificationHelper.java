package com.copycloud.app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.copycloud.app.MainActivity;
import com.copycloud.app.R;

public class NotificationHelper {
    
    private static final String CHANNEL_ID = "copy_cloud_channel";
    private static final String CHANNEL_NAME = "Copy Cloud Notifications";
    private static final String CHANNEL_DESC = "Notifications for received content";
    private static final int NOTIFICATION_ID_BASE = 1000;
    
    /**
     * Create notification channel (Android 8.0+)
     */
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    
    /**
     * Show notification for received text
     */
    public static void showTextReceivedNotification(Context context, String code, String textPreview) {
        String title = "📝 Text Received";
        String content = "Code: " + code;
        
        // Truncate preview if too long
        if (textPreview != null && !textPreview.isEmpty()) {
            String preview = textPreview.length() > 100 
                ? textPreview.substring(0, 100) + "..." 
                : textPreview;
            content += "\n" + preview;
        }
        
        showNotification(context, title, content, code);
    }
    
    /**
     * Show notification for received file(s)
     */
    public static void showFileReceivedNotification(Context context, String code, String fileName, int fileCount) {
        String title = "📁 File" + (fileCount > 1 ? "s" : "") + " Received";
        String content = "Code: " + code;
        
        if (fileName != null && !fileName.isEmpty()) {
            if (fileCount > 1) {
                content += "\n" + fileName + " and " + (fileCount - 1) + " more";
            } else {
                content += "\n" + fileName;
            }
        } else {
            content += "\n" + fileCount + " file" + (fileCount > 1 ? "s" : "");
        }
        
        showNotification(context, title, content, code);
    }
    
    /**
     * Show notification for successful upload
     */
    public static void showUploadSuccessNotification(Context context, String code, String type) {
        String title = "✅ Upload Complete";
        String content = "Code: " + code + "\nType: " + type.toUpperCase();
        showNotification(context, title, content, code);
    }
    
    /**
     * Show generic notification
     */
    private static void showNotification(Context context, String title, String content, String code) {
        // Create intent to open app when notification is tapped
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("code", code);
        intent.putExtra("open_retrieve", true);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{0, 250, 250, 250});
        
        // Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        
        // Check notification permission (Android 13+)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || 
            hasNotificationPermission(context)) {
            try {
                int notificationId = NOTIFICATION_ID_BASE + (int) System.currentTimeMillis() % 1000;
                notificationManager.notify(notificationId, builder.build());
            } catch (SecurityException e) {
                // Permission denied
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Check if notification permission is granted
     */
    public static boolean hasNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
        return true; // Pre-Android 13 doesn't need runtime permission
    }
    
    /**
     * Request notification permission (Android 13+)
     */
    public static boolean needsNotificationPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }
}
