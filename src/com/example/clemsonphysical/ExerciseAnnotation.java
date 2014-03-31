/**
 * 
 */
package com.example.clemsonphysical;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.clemsonphysical.Exercise.DbKeys;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author jburton
 *
 */
public class ExerciseAnnotation extends DatabaseObject {

	/**
	 * 
	 */
	
//	"CREATE TABLE \"exercise_annotation\"(\n"+
//	"  \"idexercise_annotation\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"+
//	"  \"exercise_idexercise\" INTEGER NOT NULL,\n"+
//	"  \"exercise_annotation_video_time\" INTEGER NOT NULL,\n"+
//	"  \"exercise_annotation_annotation\" VARCHAR(255) NOT NULL,\n"+
//	"  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
//	"  CONSTRAINT \"fk_exercise_annotation_exercise1\"\n"+
//	"    FOREIGN KEY(\"exercise_idexercise\")\n"+
//	"    REFERENCES \"exercise\"(\"idexercise\")\n"+
//	");\n",
	
	public enum DbKeys
	{
		// Order of columns must match order in SQL create statement.
		KEY_ID ("idexercise_annotation"),
		KEY_IDEXERCISE ("exercise_idexercise"),
		KEY_EXERCISE_ANNOTATION_VIDEO_TIME ("exercise_annotation_video_time"),
		KEY_EXERCISE_ANNOTATION_ANNOTATION ("exercise_annotation_annotation"),
		KEY_CREATE_TIME ("create_time");
		
		private String field_name;
		DbKeys(String name)
		{
			field_name = name;
		}
		public String getKeyName()
		{
			return field_name;
		}
	};
	
	public static final String TABLE_NAME = "exercise_annotation";
	
	private int exercise_idexercise;
	private int exercise_annotation_video_time;
	private String exercise_annotation_annotation;
	private String create_time;

	public ExerciseAnnotation() {
		
		this(0);
	}

	public ExerciseAnnotation(int id) {
		this(0,0,0,"","");

	}
	public ExerciseAnnotation(int id, int exercise_id, int video_time, String annotation)
	{
		this(id, exercise_id, video_time, annotation, "");
	}
	
	public ExerciseAnnotation(int id, int exercise_id, int video_time, String annotation, String create_time)
	{
		super(id);
		this.exercise_idexercise = exercise_id;
		this.exercise_annotation_video_time = video_time;
		this.exercise_annotation_annotation = annotation;
		this.create_time = create_time;
	}
	
	
	public int getExerciseId()
	{
		return this.exercise_idexercise;
	}
	
	public void setExerciseId(int exercise_id)
	{
		this.exercise_idexercise = exercise_id;
	}
		

	public int getVideoTime()
	{
		return this.exercise_annotation_video_time;
	}
	
	public void setVideoTime(int video_time)
	{
		this.exercise_annotation_video_time = video_time;
	}
	
	public String getAnnotation()
	{
		return this.exercise_annotation_annotation;
	}
	
	public void setAnnotation(String annotation)
	{
		this.exercise_annotation_annotation = annotation;
	}
	
	public String getCreateTime()
	{
		return this.create_time;
	}
	
	public void setCreateTime(String create_time)
	{
		this.create_time = create_time;
	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#getTableName()
	 */
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#getParams()
	 */
	@Override
	public List<NameValuePair> getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#setObjectFromJSON(org.json.JSONObject)
	 */
	@Override
	public void setObjectFromJSON(JSONObject j) throws JSONException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#update(com.example.clemsonphysical.DatabaseHandler)
	 */
	@Override
	public int update(DatabaseHandler dbh) {
		SQLiteDatabase db = dbh.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        values.put(DbKeys.KEY_ID.getKeyName(), this.getId());
    	values.put(DbKeys.KEY_IDEXERCISE.getKeyName(), this.getExerciseId());
    	values.put(DbKeys.KEY_EXERCISE_ANNOTATION_VIDEO_TIME.getKeyName(), this.getVideoTime());
    	values.put(DbKeys.KEY_EXERCISE_ANNOTATION_ANNOTATION.getKeyName(), this.getAnnotation());
    	//values.put(KEY_CREATE_TIME, this.getCreateTime());

        // updating row
        int rc = db.update(getTableName(), values,DbKeys.KEY_ID.getKeyName() + " = ?",
                new String[] { String.valueOf(getId()) });
        
        
        return rc;

	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#add(com.example.clemsonphysical.DatabaseHandler)
	 */
	@Override
	public void add(DatabaseHandler dbh) throws Exception 
	
	{
		
		SQLiteDatabase db = dbh.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, this.getId());
    	values.put(DbKeys.KEY_IDEXERCISE.getKeyName(), this.getExerciseId());
    	values.put(DbKeys.KEY_EXERCISE_ANNOTATION_VIDEO_TIME.getKeyName(), this.getVideoTime());
    	values.put(DbKeys.KEY_EXERCISE_ANNOTATION_ANNOTATION.getKeyName(), this.getAnnotation());
    	//values.put(KEY_CREATE_TIME, this.getCreateTime());

        // Inserting Row
        db.insertOrThrow(this.getTableName(), null, values);
        
    }


	// Should be in superclass, but Java won't let you override static methods.
	
	public static String [] getDbKeyNames()
	{
		String [] key_names = new String[DbKeys.values().length];
	    
	    for (int i = 0; i < DbKeys.values().length; i++)
	    {
	   	 key_names[i] = DbKeys.values()[i].getKeyName();
	    }
	    
	    return key_names;
	}



	// Should be in superclass, but Java won't let you override static methods. 
	
	public static DatabaseObject getById(DatabaseHandler dbh, int id) throws Exception {
	    
		SQLiteDatabase db = dbh.getReadableDatabase();
	     
	     			
        Cursor cursor = db.query(TABLE_NAME,getDbKeyNames(), DbKeys.KEY_ID.getKeyName() + "=?",
                new String[] { Integer.toString(id) }, null, null, null, null);
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
	        
        	throw new java.lang.Exception("Cannot find "+TABLE_NAME+" matching id "+ id);
        }

	}

	// Should be in superclass, but Java won't let you override static methods.
	
	public static List<DatabaseObject> getAll(DatabaseHandler dbh) {
        List<DatabaseObject> objectList = new ArrayList<DatabaseObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                
            	DatabaseObject object = createObjectFromCursor(cursor);
               
                // Adding object to list
                objectList.add(object);
                
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        
        
        return objectList;
	}
	
	
	protected static ExerciseAnnotation createObjectFromCursor(Cursor cursor)
	{
		ExerciseAnnotation exercise_annotation = new ExerciseAnnotation();
        
        exercise_annotation.setId(Integer.parseInt(cursor.getString(DbKeys.KEY_ID.ordinal())));
        exercise_annotation.setAnnotation(cursor.getString(DbKeys.KEY_EXERCISE_ANNOTATION_ANNOTATION.ordinal()));
        exercise_annotation.setVideoTime(Integer.parseInt(cursor.getString(DbKeys.KEY_EXERCISE_ANNOTATION_VIDEO_TIME.ordinal())));
        exercise_annotation.setCreateTime(cursor.getString(DbKeys.KEY_CREATE_TIME.ordinal()));
        exercise_annotation.setExerciseId(Integer.parseInt(cursor.getString(DbKeys.KEY_IDEXERCISE.ordinal())));
        
        return exercise_annotation;
	}
	
	@Override
	public String getIdKeyName() {
		return DbKeys.KEY_ID.getKeyName();
	}
	
	public static void deleteAll(DatabaseHandler dbh) 
	{
		dbh.deleteAllRecordsFromTable(TABLE_NAME);
	}

}
