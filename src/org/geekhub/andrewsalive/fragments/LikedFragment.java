package org.geekhub.andrewsalive.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;


import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.geekhub.andrewsalive.BaseFragment;
import org.geekhub.andrewsalive.R;
import org.geekhub.andrewsalive.database.ArticlesContentProvider;
import org.geekhub.andrewsalive.database.ArticlesTable;
import org.geekhub.andrewsalive.database.DatabaseHelper;
import org.geekhub.andrewsalive.helper.Intents;
import org.geekhub.andrewsalive.helper.TweetListAdapter;
import org.holoeverywhere.LayoutInflater;

public class LikedFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	
	ListView lv;
	
	private View view;
//    private SimpleCursorAdapter mAdapter;
    private SQLiteDatabase database;	
    TweetListAdapter scAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.titles_fragment, container, false);
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListFromDB();
        lvOnClick();
     }

    public void lvOnClick () {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);
                int colId = cursor.getColumnIndex(ArticlesTable.COLUMN_ID);
                int colAvatar = cursor.getColumnIndex(ArticlesTable.COLUMN_AVATAR);
                int colTitle = cursor.getColumnIndex(ArticlesTable.COLUMN_NICKNAME);
                int colContent = cursor.getColumnIndex(ArticlesTable.COLUMN_TWEETTEXT);

                startActivity(Intents.getDetailsIntent(getSupportActivity(),
                        cursor.getLong(colId), cursor.getString(colAvatar),cursor.getString(colTitle), cursor.getString(colContent)));
                }
        });
    }
    public void getListFromDB() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(ArticlesTable.TABLE_ARTICLES, null, null, null,null,null,null);

        getActivity().startManagingCursor(cursor);
        String[] from = new String[] {ArticlesTable.COLUMN_TWEETTEXT, ArticlesTable.COLUMN_NICKNAME, ArticlesTable.COLUMN_AVATAR };
        int[] to = new int[] { R.id.text, R.id.username, R.id.avatar };
        getLoaderManager().initLoader(0, null, this);
        scAdapter = new TweetListAdapter(getActivity(), R.layout.list_item, cursor, from, to);
        lv = (ListView) getView().findViewById(R.id.list);
        lv.setAdapter(scAdapter);
    }

    
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ArticlesContentProvider.CONTENT_URI,
                ArticlesTable.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
//        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//        mAdapter.swapCursor(null);
    }
}