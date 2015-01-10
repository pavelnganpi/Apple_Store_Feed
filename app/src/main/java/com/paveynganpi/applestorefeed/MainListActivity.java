package com.paveynganpi.applestorefeed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainListActivity extends ActionBarActivity {

    protected ListView mListView;//reference a listView
    protected String[] mAppleFeedTitle;
    protected static final String TAG = MainListActivity.class.getSimpleName();
    protected static final int NUMBER_OF_POSTS = 20; // the top 15 posts of the news feed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        //if device is connected to a network, connect to the appleFeedUrl
        if(isAvailableNetwork()) {
            GetAppleFeedTasks getAppleFeedTasks = new GetAppleFeedTasks();
            getAppleFeedTasks.execute();
        }
        else{
            //no network available
            Toast.makeText(this,"Network is Unavailable",Toast.LENGTH_LONG).show();
        }


    }

    //checks is device is connected to a network
    private boolean isAvailableNetwork() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if(networkInfo!=null && networkInfo.isConnected() ){
            isAvailable = true;
        }
        return isAvailable;

    }

    //class to run in background as we connect to apple feed url
    private class GetAppleFeedTasks extends AsyncTask<Object,Void,String>{

        @Override
        protected String doInBackground(Object[] params) {
            int responseCode = -1;

            try {
                URL appleFeedUrl = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
                HttpURLConnection connection = (HttpURLConnection) appleFeedUrl.openConnection();
                connection.connect();


                responseCode = connection.getResponseCode();

                //if responsecode is 200
                if(responseCode == HttpURLConnection.HTTP_OK){
                    //success
                    //create inputstream to get data from url in bytes
                    InputStream inputStream = connection.getInputStream();

                    //create a reader to read the data in the input stream
                    Reader reader = new InputStreamReader(inputStream);
                   int contentLength = connection.getContentLength();//get the length of the data. /// bugy with this url
                   // int contentLength = 10000;
                    Log.d(TAG,"content length "+contentLength);
                   // int contentLength = 9000000;
                    //create a char array to store the data in it
                    char[] charArray = new char[contentLength];
                    int[] now = new int[contentLength];

                    //store the data into the charArray
                    reader.read(charArray);

                    String responseData = new String(charArray);
                    //String responseData = charArray.toString();//convert the charArray to String
                    Log.v(TAG,responseData);


                }
                else {
                    Log.i(TAG, "Unsuccessfull HTTP response Code: " + responseCode);
                }
            } catch (IOException | NetworkOnMainThreadException e){
                Log.e(TAG,"Exception caught",e);

            }

            return "Code: "+responseCode;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //gets an instance of the listview, returns a list view which is used to be able to call
    protected ListView getListView() {
        if (mListView == null) {
            mListView = (ListView) findViewById(android.R.id.list);
        }
        //set the listview to be empty if no data is present
        TextView emptyText = (TextView)findViewById(android.R.id.empty);//
        mListView.setEmptyView(emptyText);

        return mListView;
    }

}
