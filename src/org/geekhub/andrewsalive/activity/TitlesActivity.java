package org.geekhub.andrewsalive.activity;

import org.geekhub.andrewsalive.BaseActivity;
import org.geekhub.andrewsalive.R;
import org.geekhub.andrewsalive.fragments.TitlesFragment;
import org.geekhub.andrewsalive.helper.AlertDialogManager;
import org.geekhub.andrewsalive.helper.ConnectionDetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;


public class TitlesActivity extends BaseActivity {
	
    ConnectionDetector cd;
    
    AlertDialogManager alert = new AlertDialogManager();
    
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.titles_activity);
	        
	        cd = new ConnectionDetector(getApplicationContext());
	        
	        
	        
	        if (!cd.isConnectingToInternet()) {
	            alert.showAlertDialog(TitlesActivity.this, "Internet Connection Error",
	                    "Please connect to working Internet connection", false);
	            return;
	        }

	        getSupportActionBar().setTitle("TweetReader");
	        getSupportActionBar().setHomeButtonEnabled(false);
	        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

	        if (savedInstanceState == null) {
	            handleIntentExtras(getIntent());
	        }
	    }

	 private void handleIntentExtras(Intent intent) {
	        TitlesFragment fragment = new TitlesFragment();
	        fragment.setArguments(intent.getExtras());
	        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	        transaction.replace(R.id.titles_frag, fragment).commit();
	    }	 
}
