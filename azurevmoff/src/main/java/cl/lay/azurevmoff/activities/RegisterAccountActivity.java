package cl.lay.azurevmoff.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import javax.net.ssl.SSLContext;

import cl.lay.azurevmoff.ApplicationState;
import cl.lay.azurevmoff.R;
import cl.lay.azurevmoff.models.AccountModel;
import cl.lay.azurevmoff.models.AzureSubscriptionModel;
import cl.lay.azurevmoff.repositories.AccountRepository;
import cl.lay.azurevmoff.services.AzureManagementApiService;
import cl.lay.azurevmoff.services.KeyStoreService;

public class RegisterAccountActivity extends Activity {

    private ValidateCertPasswordTask mCertPasswordTask = null;
    private ValidateAccountTask mAccountTask = null;
    protected RegisterAccountActivity activityReference = null;

    // UI references.
    private EditText mCertPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText mCertLocationView;
    private EditText mAccountIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityReference = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);


        mCertPasswordView = (EditText) findViewById(R.id.account_cert_password);
        mCertLocationView = (EditText) findViewById(R.id.account_cert_location);
        mAccountIdView = (EditText) findViewById(R.id.account_id);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAccount();
            }
        });

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void registerAccount() {
        if (mCertPasswordTask != null) {
            return;
        }

        // Reset errors.
        mAccountIdView.setError(null);
        mCertLocationView.setError(null);
        mCertPasswordView.setError(null);

        // Store values at the time of the login attempt.

        String accountId = mAccountIdView.getText().toString();
        String certLocation = mCertLocationView.getText().toString();
        String certPassword = mCertPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid accountId, if the user entered one.
        if (!isAccountIdFormatValid(accountId)) {
            mAccountIdView.setError("The account id is invalid, please write it with dashes and lowercase letters");
            focusView = mAccountIdView;
            cancel = true;
        }
        if (!isCertLocationValid(certLocation)) {
            mCertLocationView.setError("The file location provided does not exist. please check the path and try again");
            focusView = mCertLocationView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            String text = "Validating certificate and accountId";
            Toast toast = Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();

            // Show a progress spinner, and kick off a background task to
            // perform the account validation
            showProgress(true);
            mCertPasswordTask = new ValidateCertPasswordTask(certPassword, certLocation, accountId);
            mCertPasswordTask.execute((Void) null);
        }
    }

    private boolean isCertLocationValid(String certLocation) {
        try {
            if (TextUtils.isEmpty(certLocation))
                return false;
            File file = new File(certLocation);
            return file.exists();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isAccountIdFormatValid(String accountId) {
        if (TextUtils.isEmpty(accountId))
            return false;

        String validAccountIdRegex = "^[0-9a-z]{8}\\-[0-9a-z]{4}\\-[0-9a-z]{4}\\-[0-9a-z]{4}\\-[0-9a-z]{12}$";
        return accountId.matches(validAccountIdRegex);
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ValidateCertPasswordTask extends AsyncTask<Void, Void, Boolean> {


        private String mCertLocation;
        private String mCertPassword;
        private String accountId;
        private KeyStoreService keyStoreService;

        ValidateCertPasswordTask(String certLocation, String certPassword, String accountId) {
            mCertLocation = certLocation;
            mCertPassword = certPassword;
            this.accountId = accountId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            keyStoreService = ApplicationState.getInstance().getKeyStoreService();
            return keyStoreService.loadCert(mCertPassword, mCertLocation);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCertPasswordTask = null;

            if (success) {
                mAccountTask = new ValidateAccountTask(keyStoreService, mCertLocation, mCertPassword, accountId);
                mAccountTask.execute((Void) null);
                finish();
            } else {
                showProgress(false);
                mCertPasswordView.setError("The password has been rejected by the certificate, please try again");
                mCertPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mCertPasswordTask = null;
            showProgress(false);
        }
    }

    public class ValidateAccountTask extends AsyncTask<Void, Void, AzureSubscriptionModel> {

        private final AzureManagementApiService azureService;
        private KeyStoreService keyStoreService;
        private String certLocation;
        private String certPassword;
        private String accountId;

        public ValidateAccountTask(KeyStoreService keyStoreService, String certLocation, String certPassword, String accountId) {

            this.keyStoreService = keyStoreService;
            this.certLocation = certLocation;
            this.certPassword = certPassword;
            this.accountId = accountId;
            this.azureService = ApplicationState.getInstance().getAzureManagmentApiService();
        }

        @Override
        protected AzureSubscriptionModel doInBackground(Void... voids) {

            SSLContext sslContext = keyStoreService.generateSSLContext(certPassword);
            AzureSubscriptionModel subscriptionDetails =  azureService.fetchSubscriptionDetails(sslContext, accountId);

            return subscriptionDetails;
        }

        @Override
        protected void onPostExecute(final AzureSubscriptionModel subscrtiptionDetails) {
            mAccountTask = null;
            showProgress(false);

            if (subscrtiptionDetails!=null) {
                Context context = activityReference.getApplicationContext();
                AccountRepository accountRepository = ApplicationState.getInstance().getAccountRepository(context);
                AccountModel accountModel = new AccountModel();
                accountModel.setAccountId(accountId);
                accountModel.setCertLocation(certLocation);
                accountModel.setCertLocation(certPassword);
                accountModel.setSubscriptionName(subscrtiptionDetails.getSubscriptionName());
                accountModel.setSubscriptionStatus(subscrtiptionDetails.getSubscriptionStatus());
                accountModel.setAdministratorEmail(subscrtiptionDetails.getAccountAdminLiveEmail());
                accountRepository.createAccount(accountModel);

                String text = "Account registered.";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();

                Intent i = new Intent(context, AccountListActivity.class);
                startActivity(i);
                finish();
            } else {
                mAccountIdView.setError("The account id is wrong, invalid or the certificate has not been associated with the account");
                mAccountIdView.requestFocus();
            }
        }

    }
}



