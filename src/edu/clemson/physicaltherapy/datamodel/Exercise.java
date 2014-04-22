/**
 * 
 */
package edu.clemson.physicaltherapy.datamodel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import edu.clemson.physicaltherapy.database.DatabaseHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * @author jburton
 *
 */
public class Exercise extends DatabaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3626262735123169717L;
	private String exercise_name;
	private String exercise_video_url;
	private String exercise_instructions;
	private String exercise_file_location;
	
	/**
	 * 
	 * @author jburton
	 *
	 * This enum represents the fields in the internal database. 
	 * 
	 */
	public static enum DbKeys
	{
		KEY_ID ("idexercise","Exercise ID"),
		KEY_EXERCISE_NAME ("exercise_name","Exercise Name"),
		KEY_EXERCISE_VIDEO_URL ("exercise_video_url","Video URL"),
		KEY_EXERCISE_INSTRUCTIONS ("exercise_instruction_url","Instructions"),
		KEY_EXERCISE_FILE_LOCATION ("exercise_file_location","File Location");
		
		
		private String key_name;
		private String key_label;
		
		DbKeys(String name,String label)
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
	
	public static final String TABLE_NAME = "exercise";
	
	
	/**
	 * @fn Exercise()
	 * @brief Create an empty exercise object.
	 */
	public Exercise() {
		this(0,"","","","");
	}
	
	/**
	 * @fn public Exercise(int id, String name, String video_url, String instructions, String file_location)
	 * @brief Create a new exercise object. 
	 * @param id
	 * @param name
	 * @param video_url
	 * @param instructions
	 * @param file_location
	 */
	
	public Exercise(int id, String name, String video_url, String instructions, String file_location)
	{
		this.id = id;
		this.exercise_name = name;
		this.exercise_file_location = file_location; 
		this.exercise_instructions = instructions;
		this.exercise_video_url = video_url;
	}

	/**
	 * @fn public Exercise(int id)
	 * @brief create an empty exercise with a specific id.
	 * @param id
	 */
	public Exercise(int id) {
		this(id,"","","","");
	}

	public String getName()
	{
		return this.exercise_name;
	}
	
	public void setName(String name)
	{
		this.exercise_name = name;	
	}
	
	public String getFileLocation()
	{
		return this.exercise_file_location; 	
	}
	
	public void setFileLocation(String file_location)
	{
		this.exercise_file_location = file_location;
	}
	
	public String getVideoUrl()
	{
		return this.exercise_video_url;
	}
	
	public void setVideoUrl(String video_url)
	{
		this.exercise_video_url = video_url;
	}
	
	
	public String getInstructions()
	{
		return this.exercise_instructions;
	}
	
	public void setInstructions(String instructions)
	{
		this.exercise_instructions = instructions;
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
	 * This method sets the value of an exercise object from a JSONObject. 
	 * Once the exercise object is created, the object can be stored in the internal database.
	 * This method should handle the "housekeeping" tasks that allows
	 * the external data to merge with the data in the internal database.
	 */
	@Override
	public void setObjectFromJSON(JSONObject j) throws JSONException {
		setId(Integer.parseInt(j.getString("idexercise")));
		setName(j.getString("exercise_name"));
		setVideoUrl(j.getString("exercise_video_url"));
		setInstructions(j.getString("exercise_instruction"));
		setFileLocation("online video");
		Log.d("VIDEO", j.getString("exercise_video_url"));
	}
	
		

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#update(com.example.clemsonphysical.DatabaseHandler)
	 * 
	 */
	@Override
	public int update(DatabaseHandler dbh) {
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		Exercise oldExercise;
		
		try {
			oldExercise = Exercise.getById(dbh, this.getId());
		} catch (Exception e) {
			// Can't find the log. Return 0.  
			return 0;
		}
		
 
        ContentValues values = new ContentValues();
        values.put(DbKeys.KEY_ID.getKeyName(), this.getId());
        values.put(DbKeys.KEY_EXERCISE_NAME.getKeyName(), this.getName());
        values.put(DbKeys.KEY_EXERCISE_VIDEO_URL.getKeyName(), this.getVideoUrl());
        values.put(DbKeys.KEY_EXERCISE_INSTRUCTIONS.getKeyName(), this.getInstructions());
        values.put(DbKeys.KEY_EXERCISE_FILE_LOCATION.getKeyName(), this.getFileLocation());
 
        // updating row
        int rc = db.update(getTableName(), values,DbKeys.KEY_ID.getKeyName() + " = ?",
                new String[] { String.valueOf(getId()) });
        
        
        // If the video has changed, delete the old video and the annotations
        if (oldExercise != null && !oldExercise.getFileLocation().equals("online video") && !oldExercise.getFileLocation().equals(this.getFileLocation()))
        {
        	ExerciseAnnotation.deleteAllByExerciseId(dbh, this.getId());
        	DatabaseHandler.deleteFile(oldExercise.getFileLocation());
        }
        
        
        
        return rc;

	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#add(com.example.clemsonphysical.DatabaseHandler)
	 */
	@Override
	public void add(DatabaseHandler dbh) throws Exception {

		SQLiteDatabase db = dbh.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, this.getId());
        values.put(DbKeys.KEY_EXERCISE_NAME.getKeyName(), this.getName());
        values.put(DbKeys.KEY_EXERCISE_VIDEO_URL.getKeyName(), this.getVideoUrl());
        values.put(DbKeys.KEY_EXERCISE_INSTRUCTIONS.getKeyName(), this.getInstructions());
        values.put(DbKeys.KEY_EXERCISE_FILE_LOCATION.getKeyName(), this.getFileLocation());

 
        // Inserting Row
        db.insertOrThrow(this.getTableName(), null, values);

	}

	@Override
	public void delete(DatabaseHandler dbh) {
		
		super.delete(dbh);
		DatabaseHandler.deleteFile(this.getFileLocation());
	
	}
	

	public static void deleteAll(DatabaseHandler dbh) 
	{
		//TODO Delete videos when records are is deleted.
		dbh.deleteAllRecordsFromTable(TABLE_NAME);
    	List<Exercise> exerciseList = getAll(dbh);
    	for (int index = 0; index < exerciseList.size(); index++)
    	{
    		DatabaseHandler.deleteFile(exerciseList.get(index).getFileLocation());
    	}
		
	}

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
	
	public static Exercise getById(DatabaseHandler dbh, int id) throws Exception {
	    
		SQLiteDatabase db = dbh.getReadableDatabase();
	     
	     			
        Cursor cursor = db.query(TABLE_NAME,getDbKeyNames(), DbKeys.KEY_ID.getKeyName() + "=?",
                new String[] { Integer.toString(id) }, null, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            Exercise object = createObjectFromCursor(cursor);
	        cursor.close();
	        
            return object;
        }
        else
        {
	        cursor.close();
	        
        	throw new java.lang.Exception("Cannot find "+TABLE_NAME+" matching id "+ id);
        }

	}


	public static List<Exercise> getAll(DatabaseHandler dbh) {
        List<Exercise> objectList = new ArrayList<Exercise>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                
            	Exercise object = createObjectFromCursor(cursor);
               
                // Adding object to list
                objectList.add(object);
                
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        
        
        return objectList;
	}
	
	protected static Exercise createObjectFromCursor(Cursor cursor)
	{
		Exercise exercise = new Exercise();
        
        exercise.setId(Integer.parseInt(cursor.getString(DbKeys.KEY_ID.ordinal())));
        exercise.setFileLocation(cursor.getString(DbKeys.KEY_EXERCISE_FILE_LOCATION.ordinal()));
        exercise.setInstructions(cursor.getString(DbKeys.KEY_EXERCISE_INSTRUCTIONS.ordinal()));
        exercise.setName(cursor.getString(DbKeys.KEY_EXERCISE_NAME.ordinal()));
        exercise.setVideoUrl(cursor.getString(DbKeys.KEY_EXERCISE_VIDEO_URL.ordinal()));
        
        return exercise;
	}

	@Override
	public String getIdKeyName() {
		return DbKeys.KEY_ID.getKeyName();
	}

	public static Exercise getByName(DatabaseHandler dbh, String exercise_name) throws Exception {
		SQLiteDatabase db = dbh.getReadableDatabase();
	     
			
        Cursor cursor = db.query(TABLE_NAME,getDbKeyNames(), DbKeys.KEY_EXERCISE_NAME.getKeyName() + "=?",
                new String[] { exercise_name }, null, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            Exercise object = createObjectFromCursor(cursor);
	        cursor.close();
	        
            return object;
        }
        else
        {
	        cursor.close();
	        
        	throw new java.lang.Exception("Cannot find "+TABLE_NAME+" matching name "+exercise_name);
        }
	}

}
