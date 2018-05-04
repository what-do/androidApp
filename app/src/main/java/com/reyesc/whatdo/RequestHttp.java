package com.reyesc.whatdo;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Michael on 4/16/18.
 */

public class RequestHttp {
    private static RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String userUrl = "http://civil-ivy-200504.appspot.com/users/";
    private String baseUrl = "http://civil-ivy-200504.appspot.com/";
    private String friendReqUrl = "http://civil-ivy-200504.appspot.com/friendrequest/";
    private String reqRespondUrl = "http://civil-ivy-200504.appspot.com/friendrequest/respond/";

    private RequestHttp() { }

    private static RequestHttp requestHttp;

    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }

    public static RequestHttp getRequestHttp() {
        if (requestHttp == null){
            requestHttp = new RequestHttp();
        }
        return requestHttp;
    }

    public static RequestQueue getmRequestQueue(Context context) {
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }


    public void getRequest(Context context, String type, String task, String id, final VolleyCallback callback) {
        mRequestQueue = getmRequestQueue(context);

        String getUrl = baseUrl + type + "/";

        if (task != "") {
            getUrl += task + "/";
        }
        getUrl += id;

        Log.i(TAG, "sending to: " + getUrl);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: "  + response.toString());
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    public void getJointActivityRequest(Context context, String id, String friendId, final VolleyCallback callback) {
        mRequestQueue = getmRequestQueue(context);

        String getUrl = userUrl + "jointactivities/" + id + "/friendId/123";

        Log.i(TAG, "sending to: " + getUrl);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: "  + response.toString());
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }


    public void putStringRequest(Context context, String id, final String task, final JSONArray jsonArray) {
        mRequestQueue = Volley.newRequestQueue(context);
        String requestUrl = userUrl + task + "/" + id;
        Log.i(TAG, "sending to " + requestUrl);
        mStringRequest = new StringRequest(Request.Method.PUT, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Put Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String paramType = "interests";
                String param = jsonArray.toString();
                if(task.contains("like")){
                    paramType = "like";
                    try {
                        param = jsonArray.getString(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                params.put(paramType, param);
                Log.i(TAG, "Did it send this?: " + params.toString());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    public void postRequest(Context context, final String id, final String email, final String username) {
        mRequestQueue = Volley.newRequestQueue(context);
        mStringRequest = new StringRequest(Request.Method.POST, userUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("id", id);
                params.put("email", email);
                Log.i(TAG, params.toString());
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    //overloaded postRequest for friend requests/responses
    public void postRequest(Context context, final String senderId, final String recipId, final Integer task, final Boolean response) {
        mRequestQueue = Volley.newRequestQueue(context);
        String url;
        switch(task) {
            case 0: //friend request
                url = friendReqUrl + senderId;
                mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error: " + error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                        return headers;
                    }
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("recipient", recipId);
                        Log.i(TAG, params.toString());
                        return params;
                    }
                };
                mRequestQueue.add(mStringRequest);
                break;
            case 1: //friend response
                url = reqRespondUrl + senderId;
                final String value = (response) ? "ACCEPT" : "DECLINE";
                mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error: " + error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                        return headers;
                    }
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("sender", senderId);
                        params.put("value", value);
                        Log.i(TAG, params.toString());
                        return params;
                    }
                };
                mRequestQueue.add(mStringRequest);
                break;
            default:
                Log.d(TAG, task.toString());
                break;
        }
    }
}
