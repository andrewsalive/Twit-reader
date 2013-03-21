package org.geekhub.andrewsalive.helper;

import java.io.File;

import org.geekhub.andrewsalive.R;
import org.geekhub.andrewsalive.database.ArticlesTable;


import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.graphics.Bitmap;

public class TweetListAdapter extends SimpleCursorAdapter {

	
	private Cursor c;
    private Context context;
    
    public TweetListAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }
    
    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        this.c.moveToPosition(pos);
        String title = this.c.getString(this.c.getColumnIndex(ArticlesTable.COLUMN_TWEETTEXT));
        String date = this.c.getString(this.c.getColumnIndex(ArticlesTable.COLUMN_NICKNAME));
        String imgLink = this.c.getString(this.c.getColumnIndex(ArticlesTable.COLUMN_AVATAR));

        ImageView iv = (ImageView) v.findViewById(R.id.avatar);
        if (imgLink != null) {

            File cacheDir = StorageUtils.getCacheDirectory(context);

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(120, 80) // width, height
                    .discCacheExtraOptions(120, 80, Bitmap.CompressFormat.JPEG, 75) // width, height, compress format, quality
                    .threadPoolSize(4)
                    .threadPriority(6)
                    .imageDownloader(new BaseImageDownloader(context))
                    .denyCacheImageMultipleSizesInMemory()
                    .offOutOfMemoryHandling()
                    .memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024)) // 2 Mb
                    .discCache(new UnlimitedDiscCache(cacheDir))
                    .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .enableLogging()
                    .build();

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.icon)
                    .showImageForEmptyUri(R.drawable.icon)
                    .showImageOnFail(R.drawable.icon)
                    .cacheInMemory()
                    .cacheOnDisc()
                    .build();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
            imageLoader.displayImage(imgLink, iv,options);

        }

        TextView tvdate = (TextView) v.findViewById(R.id.username);
        tvdate.setText(date);

        TextView tvtitle = (TextView) v.findViewById(R.id.text);
        tvtitle.setText(title);
        return(v);
    }
}
