package org.geekhub.andrewsalive.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ArticlesContentProvider extends ContentProvider {

	private static final String AUTHORITY = "org.geekhub.andrewsalive.database";
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int ARTICLE_LIST = 1;
    private static final int ARTICLE_ITEM = 2;
    
	 public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	            + "/" + "articles");

	    private DatabaseHelper mDbHelper;

	    static {
	        sUriMatcher.addURI(AUTHORITY, "articles", ARTICLE_LIST);
	        sUriMatcher.addURI(AUTHORITY, "articles/#", ARTICLE_ITEM);
	    }

	    @Override
	    public boolean onCreate() {
	        mDbHelper = new DatabaseHelper(getContext());
	        return true;
	    }
	    @Override
	    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
	        // Constructs a new query builder and sets its table name
	        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	        qb.setTables(ArticlesTable.TABLE_ARTICLES);

	        // Choose the projection and adjust the "where" clause based on URI pattern-matching
	        switch (sUriMatcher.match(uri)) {
	            case ARTICLE_LIST:
	                break;
	            case ARTICLE_ITEM:
	                qb.appendWhere(ArticlesTable.COLUMN_ID + "=" + uri.getLastPathSegment());
	                break;
	            default:
	                // If the URI doesn't match any of the known patterns, throw an exception.
	                throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        SQLiteDatabase db = mDbHelper.getReadableDatabase();
	        Cursor c = qb.query(
	                db,            // The database to query
	                projection,    // The columns to return from the query
	                selection,     // The columns for the where clause
	                selectionArgs, // The values for the where clause
	                null,          // don't group the rows
	                null,          // don't filter by row groups
	                sortOrder      // The sort order
	        );

	        // Tells the Cursor what URI to watch, so it knows when its source data changes
	        c.setNotificationUri(getContext().getContentResolver(), uri);
	        return c;
	    }

	    @Override
	    public String getType(Uri uri) {
	        return null;
	    }

	    @Override
	    public synchronized Uri insert(Uri uri, ContentValues values) {
	        SQLiteDatabase db = mDbHelper.getWritableDatabase();
	        long id;

	        switch (sUriMatcher.match(uri)) {
	            case ARTICLE_LIST:
	                id = db.insertOrThrow(ArticlesTable.TABLE_ARTICLES, null, values);
	                break;
	            default:
	                // If the incoming pattern is invalid, throws an exception.
	                throw new IllegalArgumentException("Unsupported URI for insertion: " + uri);
	        }

	        Uri itemUri = ContentUris.withAppendedId(uri, id);
	        getContext().getContentResolver().notifyChange(itemUri, null);
	        return itemUri;
	    }

	    @Override
	    public int delete(Uri uri, String where, String[] whereArgs) {
	        SQLiteDatabase db = mDbHelper.getWritableDatabase();
	        int count;

	        switch (sUriMatcher.match(uri)) {
	            case ARTICLE_ITEM:
	                count = db.delete(ArticlesTable.TABLE_ARTICLES, where, whereArgs);
	                break;
	            default:
	                // If the incoming pattern is invalid, throws an exception.
	                throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	    }

	    @Override
	    public synchronized int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
	        return 0;
	    }
}
