package hznu.edu.cn.activity;

/**
 * Created by Ssumday on 2017/11/26.
 */
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final int PHONE_DIR=0;

    public static final int PHONE_ITEM=1;

    public static final String AUTHORITY="cn.edu.hznu.contacts.provider";

    private static UriMatcher uriMatcher;

    private MyDatabaseHelper dbHelper;

    SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"phone",PHONE_DIR);
        uriMatcher.addURI(AUTHORITY,"phone/#",PHONE_ITEM);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int deletedRows=0;
        switch (uriMatcher.match(uri)){
            case PHONE_DIR:
                deletedRows = db.delete("phone", selection, selectionArgs);
                break;
            case PHONE_ITEM:
                String contactId = uri.getPathSegments().get(1);
                deletedRows = db.delete("phone","id= ?",new String[]{contactId});
                break;
            default:
                break;
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case PHONE_DIR:
                return "vnd.android.cursor.dir/vnd.cn.edu.hznu.contacts.provider.phone";
            case PHONE_ITEM:
                return "vnd.android.cursor.item/vnd.cn.edu.hznu.contacts.provider.phone";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case PHONE_DIR:
            case PHONE_ITEM:
                long newphoneId = db.insert("phone",null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/phone/"+newphoneId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(),"Phone.db",null,1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case PHONE_DIR:
                cursor=db.query("phone",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PHONE_ITEM:
                String contactId = uri.getPathSegments().get(1);
                cursor=db.query("phone",projection,"id= ?",new String[]{contactId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case PHONE_DIR:
                updateRows = db.update("phone",values,selection,selectionArgs);
                break;
            case PHONE_ITEM:
                String contactId = uri.getPathSegments().get(1);
                updateRows = db.update("phone",values,"id= ?",new String[]{contactId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}

