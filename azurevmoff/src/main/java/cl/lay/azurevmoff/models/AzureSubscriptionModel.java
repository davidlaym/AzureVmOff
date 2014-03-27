package cl.lay.azurevmoff.models;

public class AzureSubscriptionModel {
    String _subscriptionName;
    String _subscriptionStatus;
    String _accountAdminLiveEmail;

    public String getSubscriptionName() {
        return _subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this._subscriptionName = subscriptionName;
    }

    public String getSubscriptionStatus() {
        return _subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this._subscriptionStatus = subscriptionStatus;
    }

    public String getAccountAdminLiveEmail() {
        return _accountAdminLiveEmail;
    }

    public void setAccountAdminLiveEmail(String accountAdminLiveEmail) {
        this._accountAdminLiveEmail = accountAdminLiveEmail;
    }
}
