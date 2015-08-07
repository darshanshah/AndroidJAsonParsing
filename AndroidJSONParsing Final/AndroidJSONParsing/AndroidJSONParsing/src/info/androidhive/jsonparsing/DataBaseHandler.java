package info.androidhive.jsonparsing;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBaseHandler extends SQLiteOpenHelper {

	private static final String DB_NAME = "Instructor_database";
	private static final String TB_NAME = "instructor_table";

	public static final String ID_DEFAULT = "id";
	public static final String ID_INSTRUCTOR = "idInstructor";
	public static final String FIRSTNAME = "firstName";
	public static final String LASTNAME = "lastName";
	
	private static final String TAG_ID = "id";
	private static final String TAG_FNAME = "firstName";
	private static final String TAG_LNAME = "lastName";
	
	ArrayList<HashMap<String, String>> contactList;
	
	private static final int versions = 1;

	public DataBaseHandler(Context context) {
		super(context, DB_NAME, null, versions);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		//To create table in database.
		
		String query = "CREATE TABLE " + TB_NAME + "(" + ID_DEFAULT
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + ID_INSTRUCTOR + " TEXT,"
				+ FIRSTNAME + " TEXT," + LASTNAME + " TEXT)";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}

	// Inserting Data into database for storing
	public void addInstructorDetail(ArrayList<HashMap<String, String>> contactList) {
		SQLiteDatabase db = this.getWritableDatabase();

		for(int i = 0; i < contactList.size(); i++)
		{
			HashMap<String, String> contact = new HashMap<String, String>();
			contact = contactList.get(i);
			
			Log.i("TAG", "data inserted successfully :: " + contact.get(TAG_FNAME));
		
			ContentValues values = new ContentValues();
			values.put(ID_INSTRUCTOR, contact.get(TAG_ID));
			values.put(FIRSTNAME, contact.get(TAG_FNAME));
			values.put(LASTNAME, contact.get(TAG_LNAME));
			// Inserting Row
			db.insert(TB_NAME, null, values);
		}
		db.close(); // Closing database connection
	}

	//Fetching all the data from the database to display in the listview.

	public ArrayList<HashMap<String, String>> RetriveAll() 
	{
		SQLiteDatabase sqdb = this.getReadableDatabase();

		Cursor cur = sqdb.query(TB_NAME, new String[] { ID_DEFAULT, ID_INSTRUCTOR, FIRSTNAME,
				LASTNAME }, null, null, null, null, null);

		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		if (cur.moveToNext()) 
		{
			do 
			{
				HashMap<String, String> contact = new HashMap<String, String>();
				
				Log.i("TAG", "first name is :: " + cur.getInt(2));
				
				contact.put(ID_DEFAULT, "" + cur.getInt(0));
				contact.put(ID_INSTRUCTOR, "" + cur.getString(1));
				contact.put(FIRSTNAME, "" + cur.getString(2));
				contact.put(LASTNAME, "" + cur.getString(3));

				contactList.add(contact);
			}
			while (cur.moveToNext());
			return contactList;
		}
		else 
		{
			return null;
		}
	}


	//To delete all the record from the database.
	
	public boolean delete() {
		SQLiteDatabase sqdb = this.getWritableDatabase();
		//String where = ID_DATA + "=" + id;
		return sqdb.delete(TB_NAME, null, null) != 0;
	}
}
