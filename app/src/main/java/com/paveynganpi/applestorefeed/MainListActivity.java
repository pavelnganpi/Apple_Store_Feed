package com.paveynganpi.applestorefeed;

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

        try {
            URL appleFeedUrl = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
            HttpURLConnection connection = (HttpURLConnection) appleFeedUrl.openConnection();
            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.i(TAG,"Code: "+ responseCode);

        } catch (MalformedURLException e) {

            Log.e(TAG,"Exception caught",e);
        }
        catch (IOException e){
            Log.e(TAG,"Exception caught",e);

        }
        catch (NetworkOnMainThreadException e){
            Log.e(TAG,"Exception caught",e);
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
