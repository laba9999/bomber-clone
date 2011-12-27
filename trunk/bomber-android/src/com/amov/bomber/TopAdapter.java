package com.amov.bomber;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopAdapter extends BaseAdapter {
    
    private Activity activity;
    private TopRow[] data;
    private static LayoutInflater inflater=null;

    
    public TopAdapter(Activity a, TopRow[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.top_item, null);

        TextView text=(TextView)vi.findViewById(R.id.tvTopCountry);
        text.setText(data[position].mCountry);
        
        text.setText("item "+position);
        return vi;
    }
    
    public class TopRow{
    	public String mCountry;
		public String mRank;
    	public String mName;
    	public String mScore;
    	
    	public TopRow(String _country, String _rank, String _name, String _score)
		{
			mCountry = _country;
			mRank = _rank;
			mName = _name;
			mScore = _score;
		}
    }
}