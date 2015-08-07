package info.androidhive.jsonparsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewAndComment extends Activity {

	private ProgressDialog pDialog;
	private String url, urlCommnet, id, ratingValue, comment;
	private RatingBar ratingBar;
	private EditText edittext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviewcomment);
		Intent in = getIntent();
		id = in.getStringExtra("id");

	}

	public void updateReview(View view) {
		Intent intent = new Intent(this, SingleContactActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingValue = String.valueOf(ratingBar.getRating());
		edittext = (EditText) findViewById(R.id.comment);
		comment = String.valueOf(edittext.getText());
		new GetContacts().execute();
	}

	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance

			ServiceHandler sh = new ServiceHandler();

			// // Making a request to url and getting response
			url = "http://bismarck.sdsu.edu/rateme/rating/" + id + "/"
					+ ratingValue;
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST);
			Log.d("Response: ", "> " + jsonStr);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(comment, "commeasdasdfnt"));
			urlCommnet = "http://bismarck.sdsu.edu/rateme/comment/" + id;
			String jsonStrComment = sh.makeServiceCall(urlCommnet,
					ServiceHandler.POST,params);
			Log.d("Response: ", "> " + jsonStrComment);
			//
			// if (jsonStr != null) {
			// try {
			// if (jsonStrComment != null || jsonStrComment != "") {
			// JSONArray jsonObjComment = new JSONArray(jsonStrComment);
			// contacts = jsonObjComment;
			//
			// // looping through All Contacts
			// for (int i = 0; i < contacts.length(); i++) {
			// JSONObject c = contacts.getJSONObject(i);
			//
			// String comment = c.getString("text");
			// String date = c.getString("date");
			// listofCommet.add(date + "  "+ comment);
			// }
			// }
			// JSONObject jsonObj = new JSONObject(jsonStr);
			// String id = jsonObj.getString(TAG_ID);
			// String office = jsonObj.getString(TAG_OFFICE);
			// String phone = jsonObj.getString(TAG_PHONE);
			// String email = jsonObj.getString(TAG_EMAIL);
			// String rating = jsonObj.getString(TAG_RATING);
			// String fName = jsonObj.getString(TAG_FNAME);
			// String lName = jsonObj.getString(TAG_LNAME);
			// // String comment = jsonObj.getString(TAG_COMMENT);
			// // Getting JSON Array node
			// // contacts = jsonObj;
			//
			// // looping through All Contacts
			// // for (int i = 0; i < contacts.length(); i++) {
			// // JSONObject c = contacts.getJSONObject(i);
			//
			// // String id = c.getString(TAG_ID);
			// // String name = c.getString(TAG_FNAME);
			// // String email = c.getString(TAG_LNAME);
			// /*
			// * String address = c.getString(TAG_OFFICE); String gender =
			// * c.getString(TAG_PHONE);
			// *
			// * // Phone node is JSON Object JSONObject phone =
			// * c.getJSONObject(TAG_PHONE); String mobile =
			// * phone.getString(TAG_PHONE_MOBILE); String home =
			// * phone.getString(TAG_EMAIL); String office =
			// * phone.getString(TAG_RATING);
			// */
			// // tmp hashmap for single contact
			//
			// // adding each child node to HashMap key => value
			//
			// // contact.put(TAG_PHONE_MOBILE, mobile);
			//
			// // adding contact to contact list
			// // contactList.add(contact);
			// // }
			// contact.put(TAG_ID, id);
			// contact.put(TAG_OFFICE, office);
			// contact.put(TAG_PHONE, phone);
			// contact.put(TAG_EMAIL, email);
			// contact.put(TAG_RATING, rating);
			// contact.put(TAG_FNAME, fName);
			// contact.put(TAG_LNAME, lName);
			// // contact.put(TAG_COMMENT, comment);
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// } else {
			// Log.e("ServiceHandler", "Couldn't get any data from the url");
			// }
			//
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}

	}

}
