package com.pj.app.youragewidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.Leadbolt.AdController;
import com.airpush.android.Airpush;

/**
 * Created with IntelliJ IDEA.
 * User: pawel
 * Date: 18.04.12
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1) {
        if(context.getResources().getBoolean(R.bool.air_push) && Integer.parseInt(Build.VERSION.SDK) > 3){
            new Airpush(context,context.getResources().getText(R.string.app_id).toString(),context.getResources().getText(R.string.api_key).toString(),false,true,false);
        }
        if(context.getResources().getBoolean(R.bool.leadbolt)) {
            AdController mycontroller = new AdController(context,
                    context.getResources().getString(R.string.leadbolt_section_id));
            mycontroller.loadNotification();
        }
    }
}
