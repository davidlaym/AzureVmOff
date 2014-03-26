package cl.lay.azurevmoff.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;

import cl.lay.azurevmoff.R;

public class VmList extends Activity {
    // Initialize the array
    String[] monthsArray = {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY",
            "AUG", "SEPT", "OCT", "NOV", "DEC"};

    // Declare the UI components
    private ListView vmListView;
    private ArrayAdapter arrayAdapter;
    private LinearLayout mProgressLayout;


    private FetchVMTask fetchTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vm_list);


        vmListView = (ListView) findViewById(R.id.vmlist);
        mProgressLayout = (LinearLayout) findViewById(R.id.vmlist_progressLayout);

        showProgress(true);
        fetchTask = new FetchVMTask();
        fetchTask.execute((Void) null);

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            vmListView.setVisibility(show ? View.GONE : View.VISIBLE);

            vmListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    vmListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            vmListView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
            vmListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setContentView(R.layout.activity_vm_list);

        // Initialize the UI components
        vmListView = (ListView) findViewById(R.id.vmlist);
        // For this moment, you have ListView where you can display a list.
        // But how can we put this data set to the list?
        // This is where you need an Adapter

        // context - The current context.
        // resource - The resource ID for a layout file containing a layout
        // to use when instantiating views.
        // From the third parameter, you plugged the data set to adapter
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, monthsArray);

        // By using setAdapter method, you plugged the ListView with adapter
        vmListView.setAdapter(arrayAdapter);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vm_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class FetchVMTask extends AsyncTask<Void, Void, String> {

        FetchVMTask() {
        }

        @Override
        protected String doInBackground(Void... params) {

            String result = null;
            int responseCode = 0;
            String contentType = null;
            KeyStore keyStore = null;


            // generate keystore and load certificate
            // create ssl context with certificate
            // make the request https://management.core.windows.net/2542916f-8712-43b7-a767-f8e772ff3665/services/hostedservices
            // pass result to post-execute method

            return result;


        }

        @Override
        protected void onPostExecute(final String success) {
            fetchTask = null;
            showProgress(false);

            if (success != null && !success.isEmpty()) {
                // display results in UI
                finish();
            } else {
                vmListView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            fetchTask = null;
            showProgress(false);
        }
    }

}
