package com.paveynganpi.applestorefeed;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;


public class MainListActivity extends ActionBarActivity {

    protected ListView mListView;//reference a listView
    protected String[] mFootballTeams = {

            "Chelsea Fc",
            "Real Madrid Fc",
            "Arsenal Fc",
            "Manchester United",
            "Liverpool Fc",
            "Barcelona Fc",
            "Athletico de Madrid",
            "Sevilla Fc",
            "Totenham United",
            "Paris Saint Germain",
            "As Monaco",
            "Southampton",
            "Ajax",
            "Mancester City"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mFootballTeams);
        getListView().setAdapter(adapter);

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
