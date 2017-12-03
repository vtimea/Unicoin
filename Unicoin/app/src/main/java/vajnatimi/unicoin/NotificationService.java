package vajnatimi.unicoin;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


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
        Uri alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        switch (s){
            case "daily":
                mBuilder =
                        new NotificationCompat.Builder(this)
                                .setContentTitle(getString(R.string.title_unicoin_reminder))
                                .setSmallIcon(R.drawable.rc_notif)
                                .setContentText(getString(R.string.msg_today))
                                .setSound(alertSound);
                mNotificationId = 001;
                mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                break;
            case "weekly":
                Log.i("notif", "====> NOTIFICATION");
                mBuilder =
                        new NotificationCompat.Builder(this)
                                .setContentTitle(getString(R.string.title_unicoin_reminder))
                                .setSmallIcon(R.drawable.rc_notif)
                                .setContentText(getString(R.string.msg_week))
                                .setSound(alertSound);
                mNotificationId = 002;
                mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                break;
        }
    }
}
