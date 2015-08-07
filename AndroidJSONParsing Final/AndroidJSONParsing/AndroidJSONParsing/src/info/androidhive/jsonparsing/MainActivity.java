package info.androidhive.jsonparsing;

import info.androidhive.jsonparsing.R;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url = "http://bismarck.sdsu.edu/rateme/list";	
	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_FNAME = "firstName";
	private static final String TAG_LNAME = "lastName";
		
	// contacts JSONArray
	JSONArray contacts = null;
	
	// Hashmap for ListView
	ArrayList<HashMap<String, String>> contactList;
	DataBaseHandler databaseObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		contactList = new ArrayList<HashMap<String, String>>();

		databaseObject = new DataBaseHandler(getApplicationContext());
		
		//databaseObject.delete();
		
		contactList = databaseObject.RetriveAll();
		
		ListView lv = getListView();

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem	
				
				String idToPass = ((TextView) view.findViewById(R.id.id))
					.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SingleContactActivity.class);
				in.putExtra(TAG_ID, idToPass);
				
				startActivity(in);

			}
		});

		//To check if data exits or not in database
		
		if(contactList != null)
		{
			showTheDataInListView();
		}
		else
		{
			contactList = new ArrayList<HashMap<String, String>>();
			// Calling async task to get json
			new GetContacts().execute();
		}
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONArray jsonObj = new JSONArray(jsonStr);
					
					// Getting JSON Array node
					contacts = jsonObj;

					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						
						String id = c.getString(TAG_ID);
						String name = c.getString(TAG_FNAME);
						String email = c.getString(TAG_LNAME);

*/
						// tmp hashmap for single contact
						HashMap<String, String> contact = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						contact.put(TAG_ID, id);
						contact.put(TAG_FNAME, name);
						contact.put(TAG_LNAME, email);
					//	contact.put(TAG_PHONE_MOBILE, mobile);

						// adding contact to contact list
						contactList.add(contact);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			
			databaseObject.addInstructorDetail(contactList);
			
			showTheDataInListView();
		}

	}
	
	public void showTheDataInListView() {
		
		ListAdapter adapter = new SimpleAdapter(
				MainActivity.this, contactList,
				R.layout.list_item, new String[] {TAG_ID, TAG_FNAME, TAG_LNAME
						 }, new int[] { R.id.id,
						R.id.firstName, R.id.lastName });
	
		setListAdapter(adapter);
		
	}

}