package vajnatimi.unicoin;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class NotificationService extends IntentService{
    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("Notification Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String s = intent.getExtras().getString("type");
        NotificationCompat.Builder mBuilder;
        int mNotificationId = 001;
        NotificationManager mNotifyMgr;
        switch (s){
            case "daily":
                Log.i("notif", "========> DAILY NOTIFICATION");
                mBuilder =
                        new NotificationCompat.Builder(this)
                                .setContentTitle("Unicoin reminder")
                                .setSmallIcon(R.drawable.rc_notif)
                                .setContentText("Don't forget to add today's transactions!");
                mNotificationId = 001;
                mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                break;
            case "weekly":
                Log.i("notif", "========> WEEKLY NOTIFICATION");
                mBuilder =
                        new NotificationCompat.Builder(this)
                                .setContentTitle("Unicoin reminder")
                                .setSmallIcon(R.drawable.rc_notif)
                                .setContentText("Don't forget to add today's transactions!");
                mNotificationId = 002;
                mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                break;
        }
    }
}
