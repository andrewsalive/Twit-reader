package org.geekhub.andrewsalive.activity;

import org.geekhub.andrewsalive.BaseActivity;
import org.geekhub.andrewsalive.R;
import org.geekhub.andrewsalive.fragments.TitlesFragment;
import org.geekhub.andrewsalive.helper.ConnectionDetector;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;


public class TitlesActivity extends BaseActivity {
		
    ConnectionDetector cd;
    
    
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.titles_activity);
	        
	        cd = new ConnectionDetector(getApplicationContext());
	        	        
	        
	        if (!cd.isConnectingToInternet()) {	
	        	pushToFinish();
	        }

	        getSupportActionBar().setTitle("TweetReader");
	        getSupportActionBar().setHomeButtonEnabled(false);
	        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

	        if (savedInstanceState == null) {
	            handleIntentExtras(getIntent());
	        }
	    }	 
	 
	 private void pushToFinish(){
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	 
				// Setting Dialog Title
				alertDialogBuilder.setTitle("Internet Connection Error");
	 
				// Setting Dialog Message
				alertDialogBuilder
					.setMessage("Please connect to working Internet connection")
					.setCancelable(false)
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							TitlesActivity.this.finish();
						}
					 });	
				// Create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
	 
				// Showing Alert Message
				alertDialog.show();		 
	 }


	 private void handleIntentExtras(Intent intent) {
	        TitlesFragment fragment = new TitlesFragment();
	        fragment.setArguments(intent.getExtras());
	        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	        transaction.replace(R.id.titles_frag, fragment).commit();
	    }	 
}
