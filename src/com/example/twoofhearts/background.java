/*package com.example.twoofhearts;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;


public class background extends IntentService{

	public background(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub Intent localIntent =
        new Intent(Constants.BROADCAST_ACTION)
        // Puts the status into the Intent
        .putExtra(Constants.EXTENDED_DATA_STATUS, status);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
		
	}

}
final class Constants {
    // Defines a custom Intent action
    public static final String BROADCAST_ACTION =
        "com.example.twoofhearts.BROADCAST";
    // Defines the key for the status "extra" in an Intent
    public static final String EXTENDED_DATA_STATUS =
        "com.example.android.STATUS";
}*/