package com.paveynganpi.applestorefeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by paveynganpi on 1/11/15.
 */

//adapter class for loading thr data into listview_layout.xml
public class FeedAdapter extends ArrayAdapter{

    protected static final String TAG = FeedAdapter.class.getSimpleName();
    protected Context mContext;
    protected ArrayList<MainListActivity.feedData> mTitle_authors;

    public FeedAdapter(Context context, ArrayList<MainListActivity.feedData> title_authors){
        super(context, R.layout.listview_layout,title_authors);
        mContext = context;
        mTitle_authors = title_authors;

        //hhhh

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_layout,null);
            holder = new ViewHolder();
            holder.descriptionTextView = (TextView)convertView.findViewById(R.id.description);
            holder.authorTextView = (TextView)convertView.findViewById(R.id.author);
            holder.authorImageView =(ImageView)convertView.findViewById(R.id.authorPhoto);
            convertView.setTag(holder);//makes us return to initial state of listview after viewing the content

        }
        else{
            holder =(ViewHolder)convertView.getTag();//gets the view holder that was already created
            //if tag is no set as above, error will result due to the fact we are trying to retrieve a tag
            //that is no longer available


        }

        holder.descriptionTextView.setText(mTitle_authors.get(position).getTitle());
        holder.authorTextView.setText(mTitle_authors.get(position).getAuthor());
        //holder.authorImageView.setI(mTitle_authors.get(position).getAuthor());
        Picasso.with(mContext).load(mTitle_authors.get(position).getAuthorThumnail()).into(holder.authorImageView);
        //Log.d(TAG,"title is ....." + mTitle_authors.get(0).getTitle());

        return convertView;

    }

    public static class ViewHolder{

        TextView descriptionTextView;
        TextView authorTextView;
        ImageView authorImageView;

    }

}
