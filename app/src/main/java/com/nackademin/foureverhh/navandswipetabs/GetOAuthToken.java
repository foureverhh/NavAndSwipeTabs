package com.nackademin.foureverhh.navandswipetabs;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;



import java.io.IOException;

/*
// https://medium.com/iquii/quick-look-at-google-cloud-vision-api-on-android-66d87d08943
public class GetOAuthToken extends AsyncTask {

    Activity mActivity;
    Account mAccount;
    int mRequestCode;
    String mScope;

    GetOAuthToken(Activity activity, Account account, String scope, int requestCode) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mAccount = account;
        this.mRequestCode = requestCode;
    }


    protected String fetchToken() throws IOException {
        String accessToken;
        try {
            accessToken = GoogleAuthUtil.getToken(mActivity, mAccount, mScope);
            GoogleAuthUtil.clearToken (mActivity, accessToken);
            accessToken = GoogleAuthUtil.getToken(mActivity, mAccount, mScope);
            return accessToken;
        } catch (UserRecoverableAuthException userRecoverableException) {
            mActivity.startActivityForResult(userRecoverableException.getIntent(), mRequestCode);
        } catch (GoogleAuthException fatalException) {
            fatalException.printStackTrace();
        }
        return null;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String token = fetchToken();
            if (token != null) {
                ((MainActivity)mActivity).onTokenReceived(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
*/