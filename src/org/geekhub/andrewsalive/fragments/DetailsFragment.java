package org.geekhub.andrewsalive.fragments;

import org.geekhub.andrewsalive.BaseFragment;
import org.geekhub.andrewsalive.R;
import org.geekhub.andrewsalive.database.ArticlesContentProvider;
import org.geekhub.andrewsalive.database.ArticlesTable;
import org.geekhub.andrewsalive.helper.Intents;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class DetailsFragment extends BaseFragment{
	
	private View mRootView;
	private Long mId;
	private String avatar;
    private String mTitle;
    private String mContent;
    private Menu mOptionsMenu;
    private boolean isLiked;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mId = args.getLong(Intents.EXTRA_ID);
            avatar = args.getString(Intents.EXTRA_AVATAR);
            mTitle = args.getString(Intents.EXTRA_TITLE);
            mContent = args.getString(Intents.EXTRA_CONTENT);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        mRootView = inflater.inflate(R.layout.details_fragment, container, false);
	        
	        TextView username = (TextView) mRootView.findViewById(R.id.d_username);
            username.setText(mTitle);
            
            
            TextView title = (TextView) mRootView.findViewById(R.id.d_text);
            title.setText(mContent);
            
            ImageView imageView = (ImageView) mRootView.findViewById(R.id.d_avatar);
            String imageUrl = avatar;
            if (imageView != null) {
            		            	
            	ImageLoader imageLoader = ImageLoader.getInstance();
            	imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            	imageLoader.displayImage(imageUrl, imageView);
            }  
	        return mRootView;
	    }	 
	 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
	        loadFromDb();
	    }
	 
	 
    private void loadFromDb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setLikeActionButtonProgress(true);

                Uri uri = Uri.parse(ArticlesContentProvider.CONTENT_URI + "/" + mId);
                Cursor cursor = getSupportActivity().getContentResolver()
                        .query(uri, ArticlesTable.PROJECTION, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToNext()) {
                        int colId = cursor.getColumnIndex(ArticlesTable.COLUMN_ID);
                        long id = cursor.getLong(colId);

                        isLiked = id == mId;
                        setLikeActionButtonState(isLiked);
                    } else {
                        setLikeActionButtonState(false);
                    }

                    cursor.close();
                }

                setLikeActionButtonProgress(false);
            }
        }).start();
    }
	 
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mOptionsMenu = menu;
        menu.findItem(R.id.menu_likes).setVisible(true);
        menu.findItem(R.id.menu_share).setVisible(true);
    }
	 
	 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	            case R.id.menu_likes:
	            	 setLikeActionButtonProgress(true);
	                 articleAddOrRemove(isLiked);
	                 break;
	            case R.id.menu_share:
	                    // TODO add callback to fire the sharing activity after successful oauth
//	                    startActivity(Intents.getSharingIntent(getSupportActivity()));
	                    
	                    Intent sendIntent = new Intent();
	                    sendIntent.setAction(Intent.ACTION_SEND);
	                    sendIntent.putExtra(Intent.EXTRA_TITLE, mTitle);
	                    sendIntent.putExtra(Intent.EXTRA_TEXT, mContent);
	                    sendIntent.setType("text/plain");
	                    getActivity().startActivity(sendIntent);
	                 break;
	        }
	        return super.onOptionsItemSelected(item);
	    }


    private void articleAddOrRemove(final boolean liked) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (liked) {
	                	
	                    Uri uri = Uri.parse(ArticlesContentProvider.CONTENT_URI + "/" + mId);
	                    getSupportActivity().getContentResolver().delete(uri,
	                            ArticlesTable.COLUMN_ID + "= ?", new String[]{String.valueOf(mId)});
	                } else {
	                	ContentValues values = new ContentValues();
	                    values.put(ArticlesTable.COLUMN_ID, mId);
	                    values.put(ArticlesTable.COLUMN_AVATAR, avatar);
	                    values.put(ArticlesTable.COLUMN_NICKNAME, mTitle);
	                    values.put(ArticlesTable.COLUMN_TWEETTEXT, mContent);

	                    getSupportActivity().getContentResolver().insert(ArticlesContentProvider.CONTENT_URI, values);
	                }

	                loadFromDb();
	            }
	        }).start();
	    }


    private void setLikeActionButtonState(final boolean isLiked) {
        if (mOptionsMenu == null) return;

        this.isLiked = isLiked;

        getSupportActivity().runOnUiThread(new Runnable() {
            public void run() {
                MenuItem likeItem = mOptionsMenu.findItem(R.id.menu_likes);
                if (likeItem != null) {
                    likeItem.setIcon(isLiked ? R.drawable.ic_action_like : R.drawable.ic_action_not_like);
                }
            }
        });
    }

    private void setLikeActionButtonProgress(final boolean loading) {
        if (mOptionsMenu == null) return;

        getSupportActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MenuItem likeItem = mOptionsMenu.findItem(R.id.menu_likes);
                if (likeItem != null) {
                    if (loading) {
                        likeItem.setActionView(R.layout.actionbar_indeterminate_progress);
                    } else {
                        likeItem.setActionView(null);
                    }
                }
            }
        });
    }
	    
}