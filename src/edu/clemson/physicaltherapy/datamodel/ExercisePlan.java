package edu.clemson.physicaltherapy.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.clemson.physicaltherapy.database.DatabaseHandler;

public class ExercisePlan extends DatabaseObject
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5806522798915995517L;

	public enum DbKeys
	{

		KEY_ID("idexercise_plan", "Exercise Plan ID"),
		KEY_EXERCISE_PLAN_NAME("exercise_plan_name", "Plan Name"),
		KEY_EXERCISE_PLAN_DESCRIPTION("exercise_plan_description", "Description");

		private String	key_name;
		private String	key_label;

		DbKeys(String name, String label)
		{
			key_name = name;
			key_label = label;
		}

		public String getKeyName()
		{
			return key_name;
		}

		public String getKeyLabel()
		{
			return key_label;
		}
	};

	// public static final String KEY_ID = "idexercise_plan";
	// public static final String KEY_EXERCISE_PLAN_NAME = "exercise_plan_name";
	// public static final String KEY_EXERCISE_PLAN_DESCRIPTION =
	// "exercise_plan_description";

	// "CREATE TABLE \"exercise_plan\"(\n"+
	// "  \"idexercise_plan\" INTEGER PRIMARY KEY NOT NULL,\n"+
	// "  \"exercise_plan_name\" VARCHAR(45) NOT NULL,\n"+
	// "  \"exercise_plan_description\" VARCHAR(255)\n"+
	// ");\n"

	public static final String	TABLE_NAME	= "exercise_plan";

	private String				exercise_plan_name;
	private String				exercise_plan_description;

	public ExercisePlan()
	{
		this(0);
	}

	public ExercisePlan(int id)
	{
		this(id, "", "");
	}

	public ExercisePlan(int id, String plan_name, String plan_description)
	{
		super(id);
		this.exercise_plan_name = plan_name;
		this.exercise_plan_description = plan_description;
	}

	public String getName()
	{
		return this.exercise_plan_name;
	}

	public void setName(String plan_name)
	{
		this.exercise_plan_name = plan_name;
	}

	public String getDescription()
	{
		return this.exercise_plan_description;
	}

	public void setDescription(String plan_description)
	{
		this.exercise_plan_description = plan_description;
	}

	@Override
	public String getTableName()
	{
		return TABLE_NAME;
	}

	@Override
	public List<NameValuePair> getParams()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObjectFromJSON(JSONObject j) throws JSONException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int update(DatabaseHandler dbh)
	{
		SQLiteDatabase db = dbh.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DbKeys.KEY_ID.getKeyName(), this.getId());
		values.put(DbKeys.KEY_EXERCISE_PLAN_NAME.getKeyName(), this.getName());
		values.put(DbKeys.KEY_EXERCISE_PLAN_DESCRIPTION.getKeyName(), this.getDescription());

		// updating row
		int rc = db.update(getTableName(), values, getIdKeyName() + " = ?", new String[] { String.valueOf(getId()) });

		return rc;

	}

	@Override
	public void add(DatabaseHandler dbh) throws Exception
	{
		SQLiteDatabase db = dbh.getWritableDatabase();

		ContentValues values = new ContentValues();
		// values.put(KEY_ID, this.getId());
		values.put(DbKeys.KEY_EXERCISE_PLAN_NAME.getKeyName(), this.getName());
		values.put(DbKeys.KEY_EXERCISE_PLAN_DESCRIPTION.getKeyName(), this.getDescription());

		// Inserting Row
		db.insertOrThrow(this.getTableName(), null, values);
	}

	// Should be in superclass, but Java won't let you override static methods.
	public static String[] getDbKeyNames()
	{
		String[] key_names = new String[DbKeys.values().length];

		for (int i = 0; i < DbKeys.values().length; i++)
		{
			key_names[i] = DbKeys.values()[i].getKeyName();
		}

		return key_names;
	}

	// Should be in superclass, but Java won't let you override static methods.

	public static DatabaseObject getById(DatabaseHandler dbh, int id)
			throws Exception
	{

		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, getDbKeyNames(), DbKeys.KEY_ID.getKeyName()
				+ "=?", new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			DatabaseObject object = createObjectFromCursor(cursor);
			cursor.close();

			return object;
		}
		else
		{
			cursor.close();

			throw new java.lang.Exception("Cannot find " + TABLE_NAME
					+ " matching id " + id);
		}

	}

	// Should be in superclass, but Java won't let you override static methods.

	public static List<DatabaseObject> getAll(DatabaseHandler dbh)
	{
		List<DatabaseObject> objectList = new ArrayList<DatabaseObject>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = dbh.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{

				DatabaseObject object = createObjectFromCursor(cursor);

				// Adding object to list
				objectList.add(object);

			} while (cursor.moveToNext());
		}

		cursor.close();

		return objectList;
	}

	protected static ExercisePlan createObjectFromCursor(Cursor cursor)
	{
		ExercisePlan exercise_plan = new ExercisePlan();

		exercise_plan.setId(Integer.parseInt(cursor.getString(DbKeys.KEY_ID.ordinal())));
		exercise_plan.setName(cursor.getString(DbKeys.KEY_EXERCISE_PLAN_NAME.ordinal()));
		exercise_plan.setDescription(cursor.getString(DbKeys.KEY_EXERCISE_PLAN_DESCRIPTION.ordinal()));

		return exercise_plan;
	}

	@Override
	public String getIdKeyName()
	{
		return DbKeys.KEY_ID.getKeyName();
	}

	public static void deleteAll(DatabaseHandler dbh)
	{
		dbh.deleteAllRecordsFromTable(TABLE_NAME);
	}

}
