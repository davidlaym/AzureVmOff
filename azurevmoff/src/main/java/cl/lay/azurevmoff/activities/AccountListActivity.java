package cl.lay.azurevmoff.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cl.lay.azurevmoff.ApplicationState;
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
        ListView list = getListView();
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new CustomMultiChoiceModeListener(list));

        repo = new AccountRepository(this);

        refreshAccounts();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            setListAdapter(null);
            setListAdapter(new AccountAdapter(this, accounts));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_list, menu);
        return true;
    }

    public class CustomMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

        private ListView parentList;

        public CustomMultiChoiceModeListener(ListView parentList) {
            this.parentList = parentList;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
            final int checkedCount = parentList.getCheckedItemCount();
            actionMode.setTitle(checkedCount + " Selected");
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.account_list_edit, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    SparseBooleanArray checked = parentList.getCheckedItemPositions();
                    ArrayList<AccountModel> selectedItems = new ArrayList<AccountModel>();
                    for (int i = 0; i < checked.size(); i++) {
                        // Item position in adapter
                        int position = checked.keyAt(i);
                        // Add sport if it is checked i.e.) == TRUE!
                        if (checked.valueAt(i)) {
                            AccountModel selectedAccount = (AccountModel) parentList.getAdapter().getItem(position);
                            AccountRepository accountRepository = ApplicationState.getInstance().getAccountRepository(parentList.getContext());
                            accountRepository.deleteAccount(selectedAccount);
                            refreshAccounts();
                        }
                    }
                    break;
                case R.id.action_refresh:

                    break;
            }
            actionMode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }
}
