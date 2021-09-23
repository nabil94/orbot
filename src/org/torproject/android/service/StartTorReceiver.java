
package org.torproject.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.torproject.android.Prefs;

public class StartTorReceiver extends BroadcastReceiver implements TorServiceConstants {

    @Override
    public void onReceive(Context context, Intent intent) {
        /* sanitize the Intent before forwarding it to TorService */
        Prefs.setContext(context);
        String action = intent.getAction();
        if (TextUtils.equals(action, ACTION_START)) {
            String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
            if (Prefs.allowBackgroundStarts()) {
                Intent startTorIntent = new Intent(context, TorService.class);
                startTorIntent.setAction(action);
                if (packageName != null)
                    startTorIntent.putExtra(EXTRA_PACKAGE_NAME, packageName);
                context.startService(startTorIntent);
            } else if (!TextUtils.isEmpty(packageName)) {
                // let the requesting app know that the user has disabled
                // starting via Intent
                Intent startsDisabledIntent = new Intent(ACTION_STATUS);
                startsDisabledIntent.putExtra(EXTRA_STATUS, STATUS_STARTS_DISABLED);
                startsDisabledIntent.setPackage(packageName);
                /* ********OpenRefactory Warning********
				 Broadcasting message should specify receiver permission.
				*/
				context.sendBroadcast(startsDisabledIntent);
            }
        }
    }
}
