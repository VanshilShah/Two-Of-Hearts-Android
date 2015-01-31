package com.example.twoofhearts;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

public class MainActivity extends Activity {
	ToggleButton update;
	public Heart me, mate;
	public static final String 	APPLICATION_ID = "6aeac2ca-d271-45fd-9e8e-479f887fc9ba", 
			APPLICATION_SECRET = "ac068179f3cf2e41da78b1c9a1f87108fde1577c", 
			APPLICATION_ROUTE = "http://two-of-hearts.mybluemix.net";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        me = new Heart();
        update = (ToggleButton) findViewById(R.id.update);
    }
    
	    private void initBluemix() {
	        IBMBluemix.initialize(this, APPLICATION_ID, APPLICATION_SECRET, APPLICATION_ROUTE);
	        IBMData.initializeService();
	        Heart.registerSpecialization(Heart.class); //Registering a specialization   
	    }
	    
    @Override
	protected void onStart(){
		super.onStart();
		initBluemix();
	}

	public void saveHeart(){
        Log.d("BLUEMIX", "save initialized");
        me.save().continueWith(new Continuation<IBMDataObject, Void>() {
	            @Override
	            public Void then(Task<IBMDataObject> task) throws Exception {
	                if (task.isFaulted()) {
	                    // Handle errors
	                    //status.setText("no work");
		               Log.d("BLUEMIX", "no work");
	                } else {
	                   Log.d("BLUEMIX", "job saved");
	                    //status.setText("player created!");
	                    // Do more work
	                }
	                return null;
	            }
	        });
        me = new Heart();
	}

	public void downloadMate(View view){
        IBMQuery<Heart> query = null;        
        try {
            query = IBMQuery.queryForClass(Heart.class);
        } catch (IBMDataException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // Query all the Jobs objects from the server
        query.find().continueWith(new Continuation<List<Heart>, Void>() {
        	
                @Override
                public Void then(Task<List<Heart>> task) throws Exception {
                    Log.v("download", "before");                    
                    List<Heart> objectsList = task.getResult();
                    for(Heart j: objectsList){                       
                    	mate = j;                         
                    }                   
                    Log.v("download", "after");  
					return null;
                }
        });
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
