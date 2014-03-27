package cl.lay.azurevmoff;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AzureVmOffDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;


    public static final String TABLE_ACCOUNTS = "Accounts";

    public AzureVmOffDB(Context context) {
        super(context, "azurevmoff", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + " (id integer primary key autoincrement, account_id char(36), cert_location varchar(255), cert_password varchar(255) );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int vOld, int vNew) {
        if (vOld > 1) {
            if(vNew <=2){
                db.execSQL("ALTER TABLE "+TABLE_ACCOUNTS+" ADD COLUMN subscription_name varchar(100);");
                db.execSQL("ALTER TABLE "+TABLE_ACCOUNTS+" ADD COLUMN subscription_status varchar(100);");
                db.execSQL("ALTER TABLE "+TABLE_ACCOUNTS+" ADD COLUMN account_admin_email varchar(255);");
            }
        }
    }

}
