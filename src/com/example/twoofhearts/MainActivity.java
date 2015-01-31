package com.example.twoofhearts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
	public static String meId = null, mateId = null;
	public static List<Heart> allHearts;
	public static MainActivityPanel panel;
	public static final String 	APPLICATION_ID = "6aeac2ca-d271-45fd-9e8e-479f887fc9ba", 
			APPLICATION_SECRET = "ac068179f3cf2e41da78b1c9a1f87108fde1577c", 
			APPLICATION_ROUTE = "http://two-of-hearts.mybluemix.net";


    LinearLayout ll;
	private static final int UPDATE_DELAY = 2500;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ll = new LinearLayout(this);
        panel = new MainActivityPanel(this);
        ll.addView(panel);
        setContentView(ll);
		me = new Heart();
		loadLocalData();
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
		allHearts = new ArrayList<Heart>();
		initBluemix();
		loadLocalData();
		panel.up = true;	
		
		/*me.setName("Vanshil");
		me.setFormAnswers("asdsd34 234nwemsfd");
		me.setLocation("Canada");
*/
		/*me.setName("Ben");
		me.setFormAnswers("beasdasda");
		me.setLocation("China");*/
		
		
		
		/*
        initHeart(me);
        initHeart(mate);*/
		//initBackground();
	}
     public void loadLocalData(){
    	String FILENAME = "id_file";
    	
    	try {
			FileInputStream fis = openFileInput(FILENAME);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			meId = new String(buffer);
			Log.v("file input", meId);
			new RegularUpdate().execute();
			fis.close();
			
		} catch (IOException e) {
			
			//if the file isn't found, create a new one.
	    	try {
		    	FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
		    	fos.write(me.getObjectId().getBytes());
				Log.v("file output", me.getObjectId());
				saveHeart();
		    	fos.close();
	    	}
	    	catch(IOException f){
	    		f.printStackTrace();
	    	}
			
			e.printStackTrace();
		}
    }
	
    	public static Heart getBestMate(){
    		Heart bestMate = null;
    		int bestScore = 0;
    		for(Heart h : allHearts){
    			if(scoreMate(h) > bestScore){
    				bestMate = h;
    			}
    		}
    		return bestMate;
    	}
    		public static int scoreMate(Heart mate){
				return 5;    			
    		}
    
    public static void regularUpdate(){
	/*	long lastTime = 0;
		long currTime = System.currentTimeMillis();
		while(true){
			currTime = System.currentTimeMillis();
			if(currTime - lastTime > UPDATE_DELAY){*/
				
				new RegularUpdate().execute();
			/*	lastTime = System.currentTimeMillis();
			}
			currTime = System.currentTimeMillis();
			
		}*/
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
	
    public void openSignUp() {
	    Intent intent = new Intent(this, SignUpActivity.class);
	    this.startActivity(intent);
	}
	public void openLogin() {
	    Intent intent = new Intent(this, LoginActivity.class);
	    this.startActivity(intent);
	}
	public void openAccount() {
	    Intent intent = new Intent(this, AccountActivity.class);
	    this.startActivity(intent);
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

class RegularUpdate extends AsyncTask<URL, Integer, Long> {
	
	public void downloadMe(String personId){
	 Log.v("downloadMe", "started"); 
     IBMQuery<Heart> query = null;        
     query = IBMQuery.queryForObjectId(personId);
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
                 
                 List<Heart> objectsList = task.getResult();
                 
                 Log.v("Me : queries retrieved = ", ""+objectsList.size());             
             	 
                 Log.d("Me : BLUEMIX name = ", MainActivity.me.getName());
                 
                 for(Heart j : objectsList){
             		 MainActivity.me = j;
             	 }		   		 
		   	     
                 long testing = 0;
          	     onPostExecute(testing);	   	 
			   	  return null;
             }
     });
	}
	public void downloadMate(String personId){
		 Log.v("downloadMate","started"); 
	     IBMQuery<Heart> query = null;        
	     query = IBMQuery.queryForObjectId(personId);
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
	               
	                 List<Heart> objectsList = task.getResult();
	                 
	                 Log.v("Mate : queries retrieved = ", ""+objectsList.size());
	                 
	                 
	             	 for(Heart j : objectsList){
	             		 MainActivity.mate = j;
	             	 }
	             	 
	                 Log.d("Mate : BLUEMIX name = ", MainActivity.mate.getName());   			   	  
	                 long testing = 0;
	          	     onPostExecute(testing);
				   	  return null;
	             }
	     });
		}
	public void downloadAll(){
		 Log.v("downloadAll", "started"); 
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
	                 
	                 List<Heart> objectsList = task.getResult();
	                 
	                 Log.v("All : queries retrieved = ", ""+objectsList.size());
	                 
	             	 for(Heart j : objectsList){
	             		 if(!j.equals(MainActivity.me)){
	             			MainActivity.allHearts.add(j);
	             		 }	             		 
	             	 }                            
						   		 			   
			   	     
			   	  Log.v("download", "end"); 
			   	long testing = 0;
		  	     onPostExecute(testing);
			   	  
				   	  return null;
	             }
	     });
	}
	protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    	
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    	if(MainActivity.mateId == null){
    		if(MainActivity.allHearts.size()>0){
    	    		MainActivity.mateId = MainActivity.getBestMate().getObjectId();
    	    		MainActivity.me.setMateName(MainActivity.mateId);
    	    		Log.v("MATE", "Added : " +  MainActivity.mateId);
    		}	
    		else{
        		downloadAll();
    		}
    	}
  	    MainActivity.saveHeart();
    }

	@Override
	protected Long doInBackground(URL... params) {
		// TODO Auto-generated method stub
		downloadMe(MainActivity.meId);
		downloadMate(MainActivity.mateId);	     
  	     
		return null;
	}
}

