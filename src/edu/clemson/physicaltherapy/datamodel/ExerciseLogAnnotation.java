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
import edu.clemson.physicaltherapy.datamodel.ExerciseLogAnnotation.DbKeys;

public class ExerciseLogAnnotation extends DatabaseObject
{

	// public static final String KEY_ID = "idexercise_annotation";
	// public static final String KEY_IDEXERCISE_LOG =
	// "exercise_log_idexercise_log";
	// public static final String KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME =
	// "exercise_log_annotation_video_time";
	// public static final String KEY_EXERCISE_LOG_ANNOTATION_ANNOTATION =
	// "exercise_log_annotation_annotation";
	// public static final String KEY_CREATE_TIME = "create_time";

	// "CREATE TABLE \"exercise_log_annotation\"(\n"+
	// "  \"idexercise_log_annotation\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"+
	// "  \"exercise_log_idexercise_log\" INTEGER NOT NULL,\n"+
	// "  \"exercise_log_annotation_video_time\" INTEGER NOT NULL,\n"+
	// "  \"exercise_log_annotation_annotation\" VARCHAR(255) NOT NULL,\n"+
	// "  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
	// "  CONSTRAINT \"fk_exercise_log_annotation_exercise_log1\"\n"+
	// "    FOREIGN KEY(\"exercise_log_idexercise_log\")\n"+
	// "    REFERENCES \"exercise_log\"(\"idexercise_log\")\n"+
	// ");\n",

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 697758881842627158L;

	public enum DbKeys
	{

		KEY_ID("idexercise_log_annotation", "Exercise Log Annotation ID"),
		KEY_IDEXERCISE_LOG("exercise_log_idexercise_log", "Exercise Log ID"),
		KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME("exercise_log_annotation_video_time", "Video Time (ms)"),
		KEY_EXERCISE_LOG_ANNOTATION_ANNOTATION("exercise_log_annotation_annotation", "Annotation"),
		KEY_CREATE_TIME("create_time", "Time Created");

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

	public static final String	TABLE_NAME	= "exercise_log_annotation";

	private int					exercise_log_idexercise_log;
	private int					exercise_log_annotation_video_time;
	private String				exercise_log_annotation_annotation;
	private String				create_time;

	public ExerciseLogAnnotation()
	{

		this(0);
	}

	public ExerciseLogAnnotation(int id)
	{
		this(0, 0, 0, "", "");

	}

	public ExerciseLogAnnotation(int id, int exercise_log_id, int video_time,
			String annotation)
	{
		this(id, exercise_log_id, video_time, annotation, "");
	}

	public ExerciseLogAnnotation(int id, int exercise_log_id, int video_time,
			String annotation, String create_time)
	{
		super(id);
		this.exercise_log_idexercise_log = exercise_log_id;
		this.exercise_log_annotation_video_time = video_time;
		this.exercise_log_annotation_annotation = annotation;
		this.create_time = create_time;
	}

	public int getExerciseLogId()
	{
		return this.exercise_log_idexercise_log;
	}

	public void setExerciseLogId(int exercise_log_id)
	{
		this.exercise_log_idexercise_log = exercise_log_id;
	}

	public int getVideoTime()
	{
		return this.exercise_log_annotation_video_time;
	}

	public void setVideoTime(int video_time)
	{
		this.exercise_log_annotation_video_time = video_time;
	}

	public String getAnnotation()
	{
		return this.exercise_log_annotation_annotation;
	}

	public void setAnnotation(String annotation)
	{
		this.exercise_log_annotation_annotation = annotation;
	}

	public String getCreateTime()
	{
		return this.create_time;
	}

	public void setCreateTime(String create_time)
	{
		this.create_time = create_time;
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
		values.put(DbKeys.KEY_IDEXERCISE_LOG.getKeyName(), this.getExerciseLogId());
		values.put(DbKeys.KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME.getKeyName(), this.getVideoTime());
		values.put(DbKeys.KEY_EXERCISE_LOG_ANNOTATION_ANNOTATION.getKeyName(), this.getAnnotation());
		// CREATE_TIME does not get updated.

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
		values.put(DbKeys.KEY_IDEXERCISE_LOG.getKeyName(), this.getExerciseLogId());
		values.put(DbKeys.KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME.getKeyName(), this.getVideoTime());
		values.put(DbKeys.KEY_EXERCISE_LOG_ANNOTATION_ANNOTATION.getKeyName(), this.getAnnotation());
		// CREATE_TIME does not get updated.
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

	public static ExerciseLogAnnotation getById(DatabaseHandler dbh, int id)
			throws Exception
	{

		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, getDbKeyNames(), DbKeys.KEY_ID.getKeyName()
				+ "=?", new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			ExerciseLogAnnotation object = createObjectFromCursor(cursor);
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

	public static ExerciseLogAnnotation getNextAnnotationByTime(
			DatabaseHandler dbh, int log_id, int time, int interval)
	{

		SQLiteDatabase db = dbh.getReadableDatabase();
		int max_time = time + interval;

		Cursor cursor = db.query(TABLE_NAME, getDbKeyNames(), DbKeys.KEY_IDEXERCISE_LOG.getKeyName()
				+ "=? and "
				+ DbKeys.KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME.getKeyName()
				+ " between ? and ?", new String[] { Integer.toString(log_id),
				Integer.toString(time), Integer.toString(max_time) }, null, null, DbKeys.KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME.getKeyName()
				+ " ASC", null);

		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			ExerciseLogAnnotation object = createObjectFromCursor(cursor);
			cursor.close();

			return object;
		}
		else
		{
			cursor.close();

			// throw new
			// java.lang.Exception("Cannot find "+TABLE_NAME+" matching id "+ );
		}
		return null;

	}

	public static ExerciseLogAnnotation getPreviousAnnotationByTime(
			DatabaseHandler dbh, int log_id, int time, int interval)
	{

		SQLiteDatabase db = dbh.getReadableDatabase();
		int min_time = time - interval;

		Cursor cursor = db.query(TABLE_NAME, getDbKeyNames(), DbKeys.KEY_IDEXERCISE_LOG.getKeyName()
				+ "=? and "
				+ DbKeys.KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME.getKeyName()
				+ " between ? and ?", new String[] { Integer.toString(log_id),
				Integer.toString(min_time), Integer.toString(time) }, null, null, DbKeys.KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME.getKeyName()
				+ " DESC", null);

		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			ExerciseLogAnnotation object = createObjectFromCursor(cursor);
			cursor.close();

			return object;
		}
		else
		{
			cursor.close();

			// throw new
			// java.lang.Exception("Cannot find "+TABLE_NAME+" matching id "+ );
		}
		return null;

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

	protected static ExerciseLogAnnotation createObjectFromCursor(Cursor cursor)
	{
		ExerciseLogAnnotation exercise_log_annotation = new ExerciseLogAnnotation();

		exercise_log_annotation.setId(Integer.parseInt(cursor.getString(DbKeys.KEY_ID.ordinal())));
		exercise_log_annotation.setAnnotation(cursor.getString(DbKeys.KEY_EXERCISE_LOG_ANNOTATION_ANNOTATION.ordinal()));
		exercise_log_annotation.setVideoTime(Integer.parseInt(cursor.getString(DbKeys.KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME.ordinal())));
		exercise_log_annotation.setCreateTime(cursor.getString(DbKeys.KEY_CREATE_TIME.ordinal()));
		exercise_log_annotation.setExerciseLogId(Integer.parseInt(cursor.getString(DbKeys.KEY_IDEXERCISE_LOG.ordinal())));

		return exercise_log_annotation;
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

	public static List<ExerciseLogAnnotation> getAllByExerciseLogId(
			DatabaseHandler dbh, int exercise_log_id)
	{
		List<ExerciseLogAnnotation> objectList = new ArrayList<ExerciseLogAnnotation>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "
				+ DbKeys.KEY_IDEXERCISE_LOG.getKeyName() + "="
				+ Integer.toString(exercise_log_id);

		SQLiteDatabase db = dbh.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{

				ExerciseLogAnnotation object = createObjectFromCursor(cursor);

				// Adding object to list
				objectList.add(object);

			} while (cursor.moveToNext());
		}

		cursor.close();

		return objectList;
	}

	public static void deleteAllByExerciseLogId(DatabaseHandler dbh,
			int exerciseLogId)
	{
		SQLiteDatabase db = dbh.getWritableDatabase();
		List<ExerciseLogAnnotation> exerciseLogAnnotationList = ExerciseLogAnnotation.getAllByExerciseLogId(dbh, exerciseLogId);

		for (int i = 0; i < exerciseLogAnnotationList.size(); i++)
		{
			exerciseLogAnnotationList.get(i).delete(dbh);
		}

	}

}
