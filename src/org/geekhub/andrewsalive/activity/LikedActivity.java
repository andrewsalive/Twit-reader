package org.geekhub.andrewsalive.activity;

import org.geekhub.andrewsalive.BaseActivity;
import org.geekhub.andrewsalive.R;
import org.geekhub.andrewsalive.fragments.LikedFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;


public class LikedActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liked_articles_activity);

        getSupportActionBar().setTitle("Liked Tweets");

        if (savedInstanceState == null) {
            handleIntentExtras(getIntent());
        }
    }

    private void handleIntentExtras(Intent intent) {
        LikedFragment fragment = new LikedFragment();
        fragment.setArguments(intent.getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag, fragment).commit();
    }
}