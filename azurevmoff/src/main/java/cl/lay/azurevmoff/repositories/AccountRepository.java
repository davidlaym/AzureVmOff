package cl.lay.azurevmoff.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cl.lay.azurevmoff.AzureVmOffDB;
import cl.lay.azurevmoff.models.AccountModel;

public class AccountRepository {

    private final SQLiteDatabase db;

    public AccountRepository(Context context) {
        AzureVmOffDB driver = new AzureVmOffDB(context);
        db = driver.getWritableDatabase();
    }

    public List<AccountModel> listAccounts() {
        List<AccountModel> accounts = new ArrayList<AccountModel>();
        String [] columns = new String[] {"id", "account_id","cert_location","cert_password"};

        Cursor cursor = db.query(AzureVmOffDB.TABLE_ACCOUNTS,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AccountModel account = cursorToAccountModel(cursor);
            accounts.add(account);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return accounts;
    }

    public void createAccount(AccountModel newAccount){

        ContentValues newValues = accountToContentValues(newAccount);
        db.insert(AzureVmOffDB.TABLE_ACCOUNTS, null, newValues);

    }

    private ContentValues accountToContentValues(AccountModel model){
        ContentValues newValues = new ContentValues();

        newValues.put("account_id", model.getmAccountId());
        newValues.put("cert_password",model.getmCertPassword());
        newValues.put("cert_location",model.getmCertLocation());
        return newValues;
    }

    private AccountModel cursorToAccountModel(Cursor cursor) {
        AccountModel comment = new AccountModel();

        comment.setRecordId(cursor.getInt(0));
        comment.setAccountId(cursor.getString(1));
        comment.setCertLocation(cursor.getString(2));
        comment.setCertPassword(cursor.getString(3));

        return comment;
    }
}
