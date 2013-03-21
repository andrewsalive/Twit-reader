package org.geekhub.andrewsalive.helper;

import org.geekhub.andrewsalive.activity.DetailsActivity;
import org.geekhub.andrewsalive.activity.LikedActivity;

import android.content.Context;
import android.content.Intent;

public class Intents {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_AVATAR = "extra_avatar";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_CONTENT = "extra_content";
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_SOCIAL = "extra_social";

    public static Intent getDetailsIntent(Context context, long id, String avatar, String title, String content) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_AVATAR, avatar);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CONTENT, content);
        return intent;
    }


//    public static Intent getSharingIntent(Context context, String message, String social) {
//        Intent intent = new Intent(context, SharingActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent.putExtra(EXTRA_MESSAGE, message);
//        intent.putExtra(EXTRA_TITLE, title);
//        return intent;
//        }

    public static Intent getLikedArticlesIntent(Context context) {
        Intent intent = new Intent(context, LikedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }
}
