package com.example.twoofhearts;

import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
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
	public static Heart me, mate;
	Intent mServiceIntent;
	
	public static final String 	APPLICATION_ID = "6aeac2ca-d271-45fd-9e8e-479f887fc9ba", 
			APPLICATION_SECRET = "ac068179f3cf2e41da78b1c9a1f87108fde1577c", 
			APPLICATION_ROUTE = "http://two-of-hearts.mybluemix.net";


    LinearLayout ll;
	private static final int UPDATE_DELAY = 2500;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ll = new LinearLayout(this);
        ll.addView(new MainActivityPanel(this));
        setContentView(ll);
        update = (ToggleButton) findViewById(R.id.update);
    }
    
	    private void initBluemix() {
	        IBMBluemix.initialize(this, APPLICATION_ID, APPLICATION_SECRET, APPLICATION_ROUTE);
	        IBMData.initializeService();
	        Heart.registerSpecialization(Heart.class); //Registering a specialization   
	    }
	    /*private void initBackground(){
	    	mServiceIntent = new Intent(this, background.class);
	    	this.startService(mServiceIntent);
	    }
	    */
    @Override
	protected void onStart(){
		super.onStart();
		initBluemix();
		
		
		long lastTime = 0;
		long currTime = System.currentTimeMillis();
		while(true){
			currTime = System.currentTimeMillis();
			if(currTime - lastTime > UPDATE_DELAY){
				
				new DownloadFilesTask().execute();
				lastTime = System.currentTimeMillis();
			}
			currTime = System.currentTimeMillis();
			
		}
		
       
		//initBackground();
	}
    
	public static void saveHeart(){
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
// end main activity *********************************************************








class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
	
	public void downloadMate(Heart heart){
	 Log.v("download", "before1"); 
     IBMQuery<Heart> query = null;        
     query = IBMQuery.queryForObjectId("e90f349c-dec1-40b4-a5bd-d69677403ec1");
     /*
     try {
			query = IBMQuery.queryForClass(Heart.class);
		} catch (IBMDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
     // Query all the Jobs objects from the server
     query.find().continueWith(new Continuation<List<Heart>, Void>() {
     	
             @Override
             public Void then(Task<List<Heart>> task) throws Exception {
                 Log.v("download", "before");
                 
                 List<Heart> objectsList = task.getResult();
                 
                 Log.v("queries retrieved = ", ""+objectsList.size());
                 
                 
                 for(Heart j: objectsList){                       
                	 MainActivity.me = j;                         
                 }    
                                 
                 Log.d("BLUEMIX name = ", MainActivity.me.getName());
                 
                  
			
		   		 
		   	     
		   	     long testing = 0;
		   	     onPostExecute(testing);
		   	     
		   	  Log.v("download", "end"); 
		   	  
		   	  
			   	  return null;
             }
     });
	}
	
    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    	
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    	MainActivity.me.setMateName("CHELSEA");
  	    MainActivity.saveHeart();
    	
    }

	@Override
	protected Long doInBackground(URL... params) {
		// TODO Auto-generated method stub
		downloadMate(MainActivity.me);
		return null;
	}
}

