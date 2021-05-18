package com.galab_rotemle.ex3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmTodoReceiver  extends BroadcastReceiver {
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "channel_todo";
    private static final CharSequence CHANNEL_NAME = "Todo Channel";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String username = intent.getStringExtra("username");
        String title = intent.getStringExtra("title");
        int todoId = intent.getIntExtra("todoId", -1);
        notificationsSetup(context);
        notifyNewTodo(username, title,todoId, context);
    }

    public void notifyNewTodo(String username,String title, int todoId, Context context)
    {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_todo)
                .setContentTitle(username)
                .setContentText(title)
                .build();
        notificationManager.notify(todoId, notification);
    }

    private void notificationsSetup(Context context)
    {
        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
