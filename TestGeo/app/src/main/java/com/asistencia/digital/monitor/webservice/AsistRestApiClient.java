package com.asistencia.digital.monitor.webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.asistencia.digital.monitor.activities.LoginActivity;
import com.asistencia.digital.monitor.geoutils.GeoItem;
import com.loopj.android.http.*;

import com.asistencia.digital.monitor.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class AsistRestApiClient {

    private final String CONTENT_TYPE_WWW_FORM = "application/x-www-form-urlencoded";
    public static final String SHARED_PREFS = "testgeo_shared_prefs";
    private static final AsyncHttpClient client = new AsyncHttpClient();
    private Context context;

    public AsistRestApiClient(Context context) {
        this.context = context;
    }

    /**
     * //////////////////////////
     * CREATE ISSUE - WEB SERVICE
     * //////////////////////////
     * */
    public void createIssue(int type) {
        final String url = Requests.enpointStr(context, R.string.const_endpoint_new_issue);
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context, new String(responseBody), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        };

        /** create parameters to send */
        Map<String, String> params = new HashMap<>();
        params.put("id_patient", "1");
        params.put("issue_type", String.valueOf(type));

        /** do the request */
        Requests.post(context, url, getSessionHeaders(), new RequestParams(params), CONTENT_TYPE_WWW_FORM, handler);
    }

    /**
     * //////////////////////
     * DO LOGIN - WEB SERVICE
     * //////////////////////
     * */
    public void doLoginGUI(int editUserResId, int editPwordResId, final LoginActivity loginMain) {
        doLoginGUI(
                (EditText)loginMain.findViewById(editUserResId),
                (EditText)loginMain.findViewById(editPwordResId),
                loginMain
        );
    }

    public void doLoginGUI(EditText editUser, final EditText editPword, final LoginActivity loginMain) {
        if (editUser != null && editPword != null && loginMain != null) {
            final String user = editUser.getText().toString().trim();
            final String pword = editPword.getText().toString().trim();
            if (pword.equals("") || user.equals("")) {
                loginMain.onLoginFailed("You must fill all form parameters. ", false);
                return;
            }

            final String url = Requests.enpointStr(context, R.string.const_endpoint_login);
            AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (Requests.isStatusOK(responseBody)) {

                        /* add the cookie to the preferences */
                        setSessionHeaders( Requests.getStrOfResponse(responseBody, "cookie") );

                        /* add credentials to preferences (hopefully safe without root) */
                        editPreferencesValue("user", user);
                        editPreferencesValue("password", pword);
                        editPreferencesValue("id_user", Requests.getStrOfResponse(responseBody, "id_user"));

                        /* callback for correct login */
                        loginMain.onLoginSuccess();

                    } else {

                        /* incorrect user or password */
                        loginMain.onLoginFailed("Sign in failed, incorrect user or password! ", false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    loginMain.onLoginFailed("We are sorry, an unknown error ocurred", true);
                }
            };

            /* create login parameters */
            Map<String, String> params = new HashMap<>();
            params.put("user_name", user);
            params.put("password", pword);
            params.put("group", "family");
            params.put("type", "cookie");

            /* do the post request */
            Requests.post(context, url, null, new RequestParams(params), CONTENT_TYPE_WWW_FORM, handler);
        }
    }

    public void doLogin(String user, String password) {
        doLogin(user, password, "family");
    }

    private void doLogin(String user, String password, String userGroup) {
        if (sharedContainsKey("session_cookie"))
            return;

        final String url = Requests.enpointStr(context, R.string.const_endpoint_login);
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (Requests.isStatusOK(responseBody)) {
                    /* add the cookie to the preferences */
                    setSessionHeaders( Requests.getStrOfResponse(responseBody, "cookie") );
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, new String(responseBody), Toast.LENGTH_SHORT).show();
            }
        };

        /* create login parameters */
        Map<String, String> params = new HashMap<>();
        params.put("user_name", user);
        params.put("password", password);
        params.put("group", userGroup);
        params.put("type", "cookie");

        /** do the post request */
        Requests.post(context, url, null, new RequestParams(params), CONTENT_TYPE_WWW_FORM, handler);
    }

    public void insertLocation(GeoItem item) {
        if (item != null) {
            final String url = Requests.enpointStr(context, R.string.const_endpoint_new_location);
            AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.e("test_geo_log", "Location inserted: "+new String(responseBody));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
            };

            /* declare the params */
            Map<String, String> params = new HashMap<>();
            params.put("id_patient", getSharedPrefValue("id_user"));
            params.put("latitude", item.latitude);
            params.put("longitude", item.longitude);

            /* do the insert request to the api */
            Requests.post(context, url, getSessionHeaders(), new RequestParams(params), CONTENT_TYPE_WWW_FORM, handler);
        }
    }

    private String getSharedPrefValue(String key) {
        return context.getSharedPreferences(SHARED_PREFS, 1).getString(key, null);
    }

    private Header[] getSessionHeaders() {
        String value = getSharedPrefValue("session_cookie");
        if (value != null)
            return new Header[] {
                    new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0"),
                    new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                    new BasicHeader("Accept-Language", "en-US,en;q=0.5"),
                    new BasicHeader("Accept-Encoding", "gzip, deflate"),
                    new BasicHeader("DNT", "1"),
                    new BasicHeader("Connection", "keep-alive"),
                    new BasicHeader("Cookie", value),
                    new BasicHeader("Pragma", "no-cache"),
                    new BasicHeader("Cache-Control", "no-cache")
            };
        return null;
    }

    private boolean sharedContainsKey(String key) {
        return context.getSharedPreferences(SHARED_PREFS, 1).contains(key);
    }

    private void setSessionHeaders(String cookiePHP) {
        this.editPreferencesValue("session_cookie", cookiePHP);
    }

    private void editPreferencesValue(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFS, 1).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static class Requests {

        public static boolean isStatusOK(byte[] response) {
            try {
                return Requests.getStrOfResponse(response, "status").equals("OK");
            } catch (Exception e) {
                Log.e(e.getClass().toString(), "ERROR: "+e.getMessage());
                return false;
            }
        }

        public static String getStrOfResponse(byte[] response, String key) {
            try {
                return new JSONObject(new String(response)).getString(key);
            } catch (Exception e) {
                Log.e(e.getClass().toString(), "ERROR: "+e.getMessage());
                return null;
            }
        }

        public static void get(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.get(context, getAbsoluteUrl(context, url), headers, params, responseHandler);
        }

        public static void post(Context context, String url, Header[] headers, RequestParams params, String contentType, AsyncHttpResponseHandler responseHandler) {
            client.post(context, getAbsoluteUrl(context, url), headers, params, contentType, responseHandler);
        }

        public static String getAbsoluteUrl(Context c, String relativeUrl) {
            return c.getResources().getString(R.string.const_endpoint_base) + relativeUrl;
        }

        public static String enpointStr(Context c, int resId) {
            return c.getResources().getString(resId);
        }
    }
}
