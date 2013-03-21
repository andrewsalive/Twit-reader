package org.geekhub.andrewsalive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import org.geekhub.andrewsalive.BaseActivity;
import org.geekhub.andrewsalive.R;
import org.geekhub.andrewsalive.fragments.DetailsFragment;
import org.geekhub.andrewsalive.helper.Intents;

public class DetailsActivity extends BaseActivity {

    private String nickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        if (savedInstanceState == null) {
            handleIntentExtras(getIntent());
        } else {
        	nickname = savedInstanceState.getString(Intents.EXTRA_TITLE);
            getSupportActionBar().setTitle(nickname);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Intents.EXTRA_TITLE, nickname);
    }

    private void handleIntentExtras(Intent intent) {
        if (intent.hasExtra(Intents.EXTRA_TITLE)) {
        	nickname = intent.getStringExtra(Intents.EXTRA_TITLE);
            getSupportActionBar().setTitle(nickname);
        }

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(intent.getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.details_frag, fragment).commit();
    }
}