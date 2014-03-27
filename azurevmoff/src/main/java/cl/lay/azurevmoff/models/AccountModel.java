package cl.lay.azurevmoff.models;

public class AccountModel {
    int recordId;
    String accountId;
    String certLocation;
    String certPassword;
    String subscriptionName;
    String subscriptionState;
    String administratorEmail;

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public String getSubscriptionState() {
        return subscriptionState;
    }

    public void setSubscriptionStatus(String subscriptionState) {
        this.subscriptionState = subscriptionState;
    }

    public String getAdministratorEmail() {
        return administratorEmail;
    }

    public void setAdministratorEmail(String administratorEmail) {
        this.administratorEmail = administratorEmail;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int mRecordId) {
        this.recordId = mRecordId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String mAccountId) {
        this.accountId = mAccountId;
    }

    public String getCertLocation() {
        return certLocation;
    }

    public void setCertLocation(String mCertLocation) {
        this.certLocation = mCertLocation;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String mCertPassword) {
        this.certPassword = mCertPassword;
    }
}
