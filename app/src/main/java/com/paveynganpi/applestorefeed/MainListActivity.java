package com.paveynganpi.applestorefeed;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainListActivity extends ActionBarActivity {

    protected ListView mListView;//reference a listView

    protected static final String TAG = MainListActivity.class.getSimpleName();
    protected static final int NUMBER_OF_POSTS = 20; // the top 15 posts of the news feed
    protected JSONObject mAppleFeedData;
    protected ProgressBar mProgressBar;

    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_THUMBNAIL = "thumbnail";

    class feedData {

        String title;
        String author;
        String authorThumnail;
        public feedData(String title, String author, String thumbnail){

            this.title = title;
            this.author = author;
            this.authorThumnail = thumbnail;
        }

        String getTitle(){
            return  title;
        }
        String getAuthor(){
            return author;
        }
        String getAuthorThumnail(){
            return authorThumnail;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        //if device is connected to a network, connect to the appleFeedUrl
        if(isAvailableNetwork()) {
            GetAppleFeedTasks getAppleFeedTasks = new GetAppleFeedTasks();
            getAppleFeedTasks.execute();
        }
        else{
            //no network available
            Toast.makeText(this,"Network is Unavailable",Toast.LENGTH_LONG).show();
        }

        //when an item in listview is clicked, open a webview
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    JSONArray jsonPosts = mAppleFeedData.getJSONArray("posts");
                    JSONObject jsonPost = jsonPosts.getJSONObject(position);
                    String appleFeedUrl = jsonPost.getString("url");

                    Intent intent = new Intent(MainListActivity.this,AppleFeedWebViewActivity.class);
                    intent.setData(Uri.parse(appleFeedUrl));
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


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
    private class GetAppleFeedTasks extends AsyncTask<Object,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(Object[] params) {
            int responseCode = -1;
            JSONObject jsonResponse = null;

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
                   // Log.d(TAG,"content length "+contentLength);
                   // int contentLength = 9000000;
                    //create a char array to store the data in it
                    char[] charArray = new char[contentLength];

                    //store the data into the charArray
                    reader.read(charArray);

                    String responseData = new String(charArray);

                    //create a JSON object
                    jsonResponse = new JSONObject(responseData);

                }
                else {
                    Log.i(TAG, "Unsuccessfull HTTP response Code: " + responseCode);
                }
            } catch (IOException | NetworkOnMainThreadException e){
                Log.e(TAG,"Exception caught",e);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            mAppleFeedData = result;
            handleAppleFeedResponse();

        }


    }

    //update the list on the listview
    private void handleAppleFeedResponse() {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(mAppleFeedData == null){

            updateDisplayForError();
            mProgressBar.setVisibility(View.INVISIBLE);


        }
        else{

            try {
                JSONArray jsonPosts = mAppleFeedData.getJSONArray("entry");//get all the posts
                //ArrayList<HashMap<String,String>> blogPosts = new ArrayList<HashMap<String,String>>();
                ArrayList<feedData> title_author = new ArrayList<feedData>();


                //loop over all the JSON objects in posts and get their titles
                for(int i =0;i<jsonPosts.length();i++){

                    JSONObject post = jsonPosts.getJSONObject(i);
                    String title = post.getString(KEY_TITLE);
                    title = Html.fromHtml(title).toString();//convert html to strings
                    String author = post.getString(KEY_AUTHOR);
                    author = Html.fromHtml(author).toString();//convert html to strings
                    String authorPhotoUrl = post.getString(KEY_THUMBNAIL);



                    title_author.add(new feedData(title,author,authorPhotoUrl));
                    //Log.d(TAG,"title " + title_author.get(i).getTitle() + " Author " + title_author.get(i).getAuthor());


                }


                  FeedAdapter adapter = new FeedAdapter(this,title_author);
                  getListView().setAdapter(adapter);


            } catch (JSONException e) {
                Log.e(TAG,"Exception caught "+e);
            }

        }

    }

    private void updateDisplayForError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.error_message));
        builder.setTitle(getString(R.string.title));
        builder.setPositiveButton(android.R.string.ok,null);
        AlertDialog dialog = builder.create();
        dialog.show();

        //set the listview to be empty if no data is present
//        TextView emptyText = (TextView) getListView().getEmptyView();//
//        emptyText.setText(getString(R.string.no_items));
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

        return mListView;
    }

}
