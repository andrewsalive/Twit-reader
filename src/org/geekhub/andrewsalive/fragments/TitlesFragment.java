package org.geekhub.andrewsalive.fragments;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import org.geekhub.andrewsalive.BaseFragment;
import org.geekhub.andrewsalive.Constants;
import org.geekhub.andrewsalive.R;
import org.geekhub.andrewsalive.helper.Article;
import org.geekhub.andrewsalive.helper.ArticleCollection;
import org.geekhub.andrewsalive.helper.ConnectionHelper;
import org.geekhub.andrewsalive.helper.Intents;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.Toast;
import org.json.JSONException;

import java.io.IOException;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class TitlesFragment extends BaseFragment{

	private View view;
	private ArticleCollection articles;
	// Progress Dialog
    private ProgressDialog pDialog;
    
    public static TitlesFragment instance;
    Intent intent;
    Context context;

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.titles_fragment, container, false);
        return view;
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intent = getActivity().getIntent();
        context = getActivity().getApplicationContext();

        instance = this;
//        pDialog = new ProgressDialog(TitlesActivity.this);
//        pDialog.setMessage("Listing Tweets ...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(false);
//        pDialog.show();
        loadData();
    }
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.all_likes).setVisible(true);
    }
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.all_likes:
	                startActivity(Intents.getLikedArticlesIntent(getSupportActivity()));
	                
	                break;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	
	 private void loadData() {
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	            	
	            	
	                try {
	                    articles = ConnectionHelper.getArticles(Constants.TWEET_NICK_NAME);

	                    updateUi();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	            }
	        }).start();
	    }
	
	  
	    public void updateUi() {
	        final ListView list = (ListView) view.findViewById(R.id.list);

	        getSupportActivity().runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	                ArticlesAdapter adapter = new ArticlesAdapter(articles);

	                if (list.getAdapter() == null) {
	                    list.setAdapter(adapter);
	                } else {
	                    adapter.notifyDataSetChanged();
	                }
	            }
	        });

	        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
	            	Article article = (Article) adapterView.getItemAtPosition(position);
	                startActivity(Intents.getDetailsIntent(
	                        getSupportActivity(), article.getId(), article.getAvatar(), article.getTitle(), article.getContent()));
	            }
	        });

	        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	            @Override
	            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
	                final Article article = (Article) adapterView.getItemAtPosition(position);

	                CharSequence[] items = {"Share on ..."};
	                AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
	                builder.setTitle(article.getTitle())
	                        .setItems(items, new DialogInterface.OnClickListener() {
	                            @Override
	                            public void onClick(DialogInterface dialogInterface, int which) {
	                    	        Intent sendIntent = new Intent();
	                    	        sendIntent.setAction(Intent.ACTION_SEND);
	                    	        sendIntent.putExtra(Intent.EXTRA_TITLE, article.getTitle());
	                    	        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getContent());
	                    	        sendIntent.setType("text/plain");
	                    	        getActivity().startActivity(sendIntent);
	                            }
	                        }).show();

	                return true;
	            }
	        });
	    }
	    
	    public class ArticlesAdapter extends BaseAdapter {

	        private ArticleCollection articles;

	        private ArticlesAdapter(ArticleCollection articles) {
	            this.articles = articles;
	        }

	        @Override
	        public int getCount() {
	            return articles.asVector().size();
	        }

	        @Override
	        public Object getItem(int i) {
	            return articles.get(i);
	        }

	        @Override
	        public long getItemId(int i) {
	            return 0;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup viewGroup) {
	        	View v = convertView;
	        	
	        	if (v == null) {
	        		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.list_item, null);	            
				}

	        	Article article = (Article) getItem(position);

	            TextView username = (TextView) v.findViewById(R.id.username);
	            username.setText(article.getTitle());
	            
	            
	            TextView title = (TextView) v.findViewById(R.id.text);
	            title.setText(article.getContent());
	            
	            ImageView imageView = (ImageView) v.findViewById(R.id.avatar);
	            String imageUrl = article.getAvatar();
	            if (imageView != null) {
	            		            	
	            	ImageLoader imageLoader = ImageLoader.getInstance();
	            	imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
	            	imageLoader.displayImage(imageUrl, imageView);
	            }            		         
	            return v;
	        }
	        
	    }
	    
		 public static class BroadcastListener extends BroadcastReceiver {
			 
	    
	    		 @Override
	    		  public void onReceive( Context context, Intent intent )
	    		  {
	    		    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
	    		    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
	    		    if(activeNetInfo != null && activeNetInfo.isConnected())
	    		    {
//	    		    	instance.loadData();
	    		    	// WE ARE CONNECTED: DO SOMETHING
	    		    	Toast.makeText( context, "Connected to : " + activeNetInfo.getTypeName(), Toast.LENGTH_LONG ).show();
						
	    		    	
	    		    }
	    		    else {
	    		        // WE ARE NOT: DO SOMETHING ELSE    
	    		    	Toast.makeText( context, "NO CONNECTION " , Toast.LENGTH_LONG ).show();
	    		    }
	    		    
	    		  }
	    		}
	}