package cl.lay.azurevmoff.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import cl.lay.azurevmoff.R;
import cl.lay.azurevmoff.adapters.AccountAdapter;
import cl.lay.azurevmoff.models.AccountModel;
import cl.lay.azurevmoff.repositories.AccountRepository;


public class AccountListActivity extends ListActivity {

    private AccountRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        repo = new AccountRepository(this);

        refreshAccounts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAccounts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                handleClickAddAccount();
                break;

            default:
                break;
        }

        return true;
    }

    private void handleClickAddAccount() {
        Intent i = new Intent(this, RegisterAccountActivity.class);
        startActivity(i);
    }

    private void refreshAccounts() {
        List<AccountModel> accounts = repo.listAccounts();
        if (accounts.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = "No accounts registered, Please create an account";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            handleClickAddAccount();
        } else {
            setListAdapter(new AccountAdapter(this, accounts));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_list, menu);
        return true;
    }

}
