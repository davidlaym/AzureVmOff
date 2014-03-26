package cl.lay.azurevmoff.models;

public class AccountModel {
    int mRecordId;
    String mAccountId;
    String mCertLocation;
    String mCertPassword;

    public int getmRecordId() {
        return mRecordId;
    }

    public void setRecordId(int mRecordId) {
        this.mRecordId = mRecordId;
    }

    public String getmAccountId() {
        return mAccountId;
    }

    public void setAccountId(String mAccountId) {
        this.mAccountId = mAccountId;
    }

    public String getmCertLocation() {
        return mCertLocation;
    }

    public void setCertLocation(String mCertLocation) {
        this.mCertLocation = mCertLocation;
    }

    public String getmCertPassword() {
        return mCertPassword;
    }

    public void setCertPassword(String mCertPassword) {
        this.mCertPassword = mCertPassword;
    }
}
