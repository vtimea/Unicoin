package vajnatimi.unicoin;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
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
        Log.i("notif", "------->" + s);
        Log.i("notif", "=========> NOTIFICATION!!!");
    }
}
