package com.paveynganpi.applestorefeed;

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

import org.apache.http.HttpConnection;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainListActivity extends ActionBarActivity {

    protected ListView mListView;//reference a listView
    protected String[] mAppleFeedTitle;
    protected static final String TAG = MainListActivity.class.getSimpleName();
    protected static final int NUMBER_OF_POSTS = 15; // the top 15 posts of the news feed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        GetAppleFeedTasks getAppleFeedTasks = new GetAppleFeedTasks();
        getAppleFeedTasks.execute();


    }

    //class to run in background as we connect to apple feed url
    private class GetAppleFeedTasks extends AsyncTask<Object,Void,String>{

        @Override
        protected String doInBackground(Object[] params) {
            int responseCode = -1;

            try {
                URL appleFeedUrl = new URL("https://itunes.apple.com/us/rss/topaudiobooks/limit=10/xml");
                HttpURLConnection connection = (HttpURLConnection) appleFeedUrl.openConnection();
                connection.connect();

                responseCode = connection.getResponseCode();
                Log.i(TAG,"Code: "+ responseCode);

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
