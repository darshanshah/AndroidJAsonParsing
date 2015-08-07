package info.androidhive.jsonparsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import info.androidhive.jsonparsing.R;

public class SingleContactActivity extends Activity {

	private ProgressDialog pDialog;
	// JSON node keys
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "id";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_PHONE_MOBILE = "mobile";
	private static final String TAG_OFFICE = "office";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_FNAME = "firstName";
	private static final String TAG_LNAME = "lastName";
	// private static final String TAG_PHONE = "phone";
	// private static final String TAG_PHONE_MOBILE = "mobile";
	// private static final String TAG_EMAIL = "email";
	private static final String TAG_RATING = "rating";
	private static final String TAG_AVGRATING = "average";
	private static final String TAG_TOTALRATING = "totalRatings";
	private static final String TAG_COMMENT = "comment";
	private static String url, urlCommnet;
	private static String id;
	JSONArray contacts = null;
	List<String> listofCommet= new ArrayList<String>() ;
	HashMap<String, String> contact = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_contact);
	
		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		id = in.getStringExtra(TAG_ID);
	
		url = "http://bismarck.sdsu.edu/rateme/instructor/"
				+ String.valueOf(id);

		urlCommnet = "http://bismarck.sdsu.edu/rateme/comments/"
				+ String.valueOf(id);
		String email = in.getStringExtra(TAG_EMAIL);
		String mobile = in.getStringExtra(TAG_PHONE_MOBILE);
		ArrayList<HashMap<String, String>> contactList;

		contactList = new ArrayList<HashMap<String, String>>();
		new GetContacts().execute();
		// Displaying all values on the screen

	}

	public void writeReview(View view) {
		 Intent intent = new Intent(this, ReviewAndComment.class);
		 intent.putExtra(TAG_ID, id);
		 startActivity(intent);
	 }
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(SingleContactActivity.this);
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
			String jsonStrComment = sh.makeServiceCall(urlCommnet,
					ServiceHandler.GET);
			Log.d("Response: ", "> " + jsonStrComment);

			if (jsonStr != null) {
				try {
					if (jsonStrComment != null || jsonStrComment != "") {
						JSONArray jsonObjComment = new JSONArray(jsonStrComment);
						contacts = jsonObjComment;

						// looping through All Contacts
						for (int i = 0; i < contacts.length(); i++) {
							JSONObject c = contacts.getJSONObject(i);
							
							String comment = c.getString("text");
							String date = c.getString("date");
							listofCommet.add(date + "  "+ comment);
						}
					}
					JSONObject jsonObj = new JSONObject(jsonStr);
					String id = jsonObj.getString(TAG_ID);
					String office = jsonObj.getString(TAG_OFFICE);
					String phone = jsonObj.getString(TAG_PHONE);
					String email = jsonObj.getString(TAG_EMAIL);
					String rating = jsonObj.getString(TAG_RATING);
					JSONObject jsonRating = new JSONObject(rating);
					String totalRating = jsonRating.getString(TAG_TOTALRATING);
					String averageRating = jsonRating.getString(TAG_AVGRATING);
					String fName = jsonObj.getString(TAG_FNAME);
					String lName = jsonObj.getString(TAG_LNAME);
					
					contact.put(TAG_ID, id);
					contact.put(TAG_OFFICE, office);
					contact.put(TAG_PHONE, phone);
					contact.put(TAG_EMAIL, email);
					contact.put(TAG_RATING, rating);
					contact.put(TAG_FNAME, fName);
					contact.put(TAG_LNAME, lName);
					contact.put(TAG_TOTALRATING, totalRating);
					contact.put(TAG_AVGRATING, averageRating);
					
					// contact.put(TAG_COMMENT, comment);
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

			if (pDialog.isShowing())
				pDialog.dismiss();
			TextView lblid = (TextView) findViewById(R.id.id_label);
			TextView lblFname = (TextView) findViewById(R.id.fName_label);
			TextView lblLName = (TextView) findViewById(R.id.lName_label);
			TextView lblOffice = (TextView) findViewById(R.id.office_label);
			TextView lblphone = (TextView) findViewById(R.id.phone_label);
			TextView lblEmail = (TextView) findViewById(R.id.email_label);
			TextView txtAvgRating = (TextView) findViewById(R.id.txtRatingValue);
			TextView txtTotalRating = (TextView) findViewById(R.id.txtTotalRAting);

			// TextView lblMobile = (TextView) findViewById(R.id.mobile_label);
			lblid.setText(contact.get(TAG_ID));
			lblFname.setText(contact.get(TAG_FNAME));
			lblLName.setText(contact.get(TAG_LNAME));
			lblOffice.setText(contact.get(TAG_OFFICE));
			lblphone.setText(contact.get(TAG_PHONE));
			lblEmail.setText(contact.get(TAG_EMAIL));
			txtAvgRating.setText(contact.get(TAG_AVGRATING));
			txtTotalRating.setText(contact.get(TAG_TOTALRATING));
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(SingleContactActivity.this,
					android.R.layout.simple_expandable_list_item_1, listofCommet);
			ListView lv = (ListView) findViewById(R.id.commentList);
			lv.setAdapter(adapter);
//			setListAdapter(adapter);

		}

	}

}
