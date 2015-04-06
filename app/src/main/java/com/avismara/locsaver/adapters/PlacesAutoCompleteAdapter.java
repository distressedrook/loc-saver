package com.avismara.locsaver.adapters;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.avismara.locsaver.entities.LocationInfoEntity;
import com.avismara.locsaver.utils.GlobalConstants;
import com.google.android.gms.location.places.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class PlacesAutoCompleteAdapter extends ArrayAdapter<LocationInfoEntity> implements Filterable {

	private static final String LOG_TAG = "PlacesAutoCompleteAdapter";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";



	private ArrayList<LocationInfoEntity> resultList;

    private Context mContext;

	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
        mContext = context;

	}

    @Override
	public int getCount() {

		return resultList.size();
	}

	@Override
	public LocationInfoEntity getItem(int index) {

		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {

		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.
					resultList = getAutoCompleteResults(constraint.toString());
					// Assign the data to the FilterResults

					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {

					notifyDataSetChanged();
				}
				else {

					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
       if(convertView == null) {
           LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line,parent,false);
           holder = new ViewHolder();
           holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
           convertView.setTag(holder);
       } else {
           holder = (ViewHolder) convertView.getTag();
       }

        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(),GlobalConstants.OPEN_SANS_REGULAR);
        holder.textView.setTypeface(typeFace);
        if(resultList.size() > position) {
            holder.textView.setText(resultList.get(position).getLocationDescription());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }

    private ArrayList<LocationInfoEntity> getAutoCompleteResults(String input) {



		ArrayList<LocationInfoEntity> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?key=" + GlobalConstants.GOOGLE_MAPS_KEY);
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));
			sb.append("&types=geocode");

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predictions = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<LocationInfoEntity>(predictions.length());
            for (int i = 0; i < predictions.length(); i++) {

                String description = predictions.getJSONObject(i).getString("description");
                String placeID = predictions.getJSONObject(i).getString("place_id");

                LocationInfoEntity locationInfoEntity = new LocationInfoEntity(description,placeID);
                resultList.add(locationInfoEntity);
            }
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}
		return resultList;
	}

}

