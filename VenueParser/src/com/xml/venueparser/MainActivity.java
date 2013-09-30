package com.xml.venueparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	String dataURL = "http://192.168.1.122/getVenues.xml";
	String xmlData;
    Boolean dataFetched;
	ArrayList<Venue> venues;
	ListView venueListView;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		venueListView = (ListView) findViewById(R.id.venueNameListView);
		xmlData = "";
		dataFetched = false;
		venues = new ArrayList<Venue>();
		GetXMLData getXMLData = new GetXMLData();
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			getXMLData.execute();
		} 
		else {
			// display error dialog
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class GetXMLData extends AsyncTask<Void, Void, String> {

		String response = "";
		@Override
		protected String doInBackground(Void... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(dataURL);
			try {
				HttpResponse execute = client.execute(httpGet);
				InputStream content = execute.getEntity().getContent();

				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(content));
				String s = "";
				while ((s = buffer.readLine()) != null) {
					response += s;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			xmlData = result;
		   ArrayList<String> venueNames = new ArrayList<String>();
			XmlPullParserFactory factory;
			try {
				Venue venue = null;
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(new StringReader(xmlData));
				int eventType = parser.getEventType();
				String text = "";
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String tagName = parser.getName();
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if (tagName.equalsIgnoreCase("venue")) {
							venue = new Venue();
						}
						break;
					case XmlPullParser.TEXT:
						text = parser.getText();
						break;
					case XmlPullParser.END_TAG:
						if (tagName.equalsIgnoreCase("venue")) {
							venues.add(venue);
						} else if (tagName.equalsIgnoreCase("venueName")) {
							venue.setVenueName(text);
						}
						else if(tagName.equalsIgnoreCase("small"))
						{
							venue.setImage(text);
						}
							
						break;
					}  

					eventType = parser.next();
				}
 
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String names=""; 
			for(Venue venue :venues)
			{
				names = names + venue.getVenueName();
				venueNames.add(venue.getVenueName());
			}
			
			VenueAdapter adapter =new VenueAdapter(context, venues);
			venueListView.setAdapter(adapter);
			
//         Log.d("Venue Name array", names);
//         System.out.println(names);
		}

	}

}
