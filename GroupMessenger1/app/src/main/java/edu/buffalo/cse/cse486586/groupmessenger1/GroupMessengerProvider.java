package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {

    private static final String KEY = "key";
    private static final String TABLE_NAME = "messages";
    /*
    *Reference: https://developer.android.com/guide/topics/providers/content-provider-creating.html
    */

    private GroupMessengerDatabaseHelper dbHelper;

    // Holds the database object
    private SQLiteDatabase db;


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /*
         * TODO: You need to implement this method. Note that values will have two columns (a key
         * column and a value column) and one row that contains the actual (key, value) pair to be
         * inserted.
         * 
         * For actual storage, you can use any option. If you know how to use SQL, then you can use
         * SQLite. But this is not a requirement. You can use other storage options, such as the
         * internal storage option that we used in PA1. If you want to use that option, please
         * take a look at the code for PA1.
         */

        /*
        Reference 1: https://developer.android.com/reference/android/database/sqlite/SQLiteQueryBuilder.html
        Reference 2: https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
         */
        db = dbHelper.getWritableDatabase();

        SQLiteQueryBuilder queryBuilder= new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);
        Cursor cursor = queryBuilder.query(db,null,null,null,null,null,null);
        boolean updateFlag=false;
        while(cursor.moveToNext()){
           String s= cursor.getString(0);
            if(s.equals(values.getAsString(KEY))){
                updateFlag=true;
                break;
            }
        }

        cursor.close();
        if(updateFlag)
        {
            int updaterow = db.update(TABLE_NAME,values,KEY+"="+values.getAsString(KEY),null);
            Log.v("update",Integer.toString(updaterow));
        }
        else {
            long id = db.insert(TABLE_NAME, null, values);
            Log.v("insert",Long.toString(id));
        }
        return uri;
    }

    @Override
    public boolean onCreate() {
        // If you need to perform any one-time initialization task, please do it here.


        //Reference: https://developer.android.com/guide/topics/providers/content-provider-creating.html
        dbHelper = new GroupMessengerDatabaseHelper(
                getContext()       // the application context
        );


        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         *
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         *
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */

        /*
        Reference: https://developer.android.com/reference/android/database/sqlite/SQLiteQueryBuilder.html
         */

        db=dbHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder= new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);
        queryBuilder.appendWhere(KEY+"='"+selection+"'");
        Cursor cursor = queryBuilder.query(db,projection,null,selectionArgs,null,null,null);
        return cursor;
    }
}
