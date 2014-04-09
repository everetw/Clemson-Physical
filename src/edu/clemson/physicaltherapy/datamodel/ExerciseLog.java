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

public class ExerciseLog extends DatabaseObject {
	
//	"CREATE TABLE \"exercise_log\"(\n"+
//	"  \"idexercise_log\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"+
//	"  \"exercise_log_video_location\" VARCHAR(255) NOT NULL,\n"+
//	"  \"exercise_log_video_notes\" VARCHAR(255),\n"+
//	"  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
//	"  \"exercise_idexercise\" INTEGER NOT NULL,\n"+
//	"  CONSTRAINT \"fk_exercise_log_exercise1\"\n"+
//	"    FOREIGN KEY(\"exercise_idexercise\")\n"+
//	"    REFERENCES \"exercise\"(\"idexercise\")\n"+
//	");\n",
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6497905965355954481L;

	public enum DbKeys
	{
	
		// Order of columns must match order in SQL create statement. 
		KEY_ID ("idexercise_log","Exercise Log ID"),
		KEY_EXERCISE_LOG_VIDEO_LOCATION ("exercise_log_video_location","Location of Video"),
		KEY_EXERCISE_LOG_VIDEO_NOTES ("exercise_log_video_notes","Video Notes"),
		KEY_CREATE_TIME ("create_time","Time Created"),
		KEY_EXERCISE_IDEXERCISE ("exercise_idexercise","Exercise ID");
		
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
	
	public static final String TABLE_NAME = "exercise_log";
	
	private String exercise_log_video_location;
	private String exercise_log_video_notes;
	private String create_time;
	private int exercise_idexercise;

	public ExerciseLog() {
		this(0);
	}

	public ExerciseLog(int id) {
		this(0,0,"","","");
	}
	
	public ExerciseLog(int id, int exercise_id, String video_location, String video_notes)
	{
		this(id,exercise_id,video_location,video_notes,"");
	}
	
	public ExerciseLog(int id, int exercise_id, String video_location, String video_notes, String create_time)
	{
		super(id);
		this.exercise_log_video_location = video_location;
		this.exercise_log_video_notes = video_notes;
		this.create_time = create_time;
		this.exercise_idexercise = exercise_id;
	}
	
	public int getExerciseId()
	{
		return this.exercise_idexercise;
	}
	
	public void setExerciseId(int exercise_id)
	{
		this.exercise_idexercise = exercise_id;
	}
	
	public String getVideoLocation()
	{
		return this.exercise_log_video_location;
	}
	
	public void setVideoLocation(String video_location)
	{
		this.exercise_log_video_location = video_location;
	}
	
	public String getVideoNotes()
	{
		return this.exercise_log_video_notes;
	}
	
	public void setVideoNotes(String video_notes)
	{
		this.exercise_log_video_notes = video_notes;
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
	public String getTableName() {

		return "exercise_log";
	}

	@Override
	public List<NameValuePair> getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObjectFromJSON(JSONObject j) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public int update(DatabaseHandler dbh) {
		
		SQLiteDatabase db = dbh.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        values.put(DbKeys.KEY_ID.getKeyName(), this.getId());
    	values.put(DbKeys.KEY_EXERCISE_LOG_VIDEO_LOCATION.getKeyName(), this.getVideoLocation());
    	values.put(DbKeys.KEY_EXERCISE_LOG_VIDEO_NOTES.getKeyName(), this.getVideoNotes());
    	// CREATE_TIME does not get updated.  	
    	values.put(DbKeys.KEY_EXERCISE_IDEXERCISE.getKeyName(), this.getExerciseId());
 
        // updating row
        int rc = db.update(getTableName(), values,getIdKeyName() + " = ?",
                new String[] { String.valueOf(getId()) });
        
        
        return rc;


	}

	@Override
	public void add(DatabaseHandler dbh) throws Exception {
		SQLiteDatabase db = dbh.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, this.getId());
    	values.put(DbKeys.KEY_EXERCISE_LOG_VIDEO_LOCATION.getKeyName(), this.getVideoLocation());
    	values.put(DbKeys.KEY_EXERCISE_LOG_VIDEO_NOTES.getKeyName(), this.getVideoNotes());
    	// CREATE_TIME does not get updated.  	
    	values.put(DbKeys.KEY_EXERCISE_IDEXERCISE.getKeyName(), this.getExerciseId());
    	
        // Inserting Row
        db.insertOrThrow(this.getTableName(), null, values);

	}

	@Override
	public void delete(DatabaseHandler dbh) {
		
		super.delete(dbh);
		DatabaseHandler.deleteFile(this.getVideoLocation());
	
	}
	

	public static void deleteAll(DatabaseHandler dbh) 
	{

		dbh.deleteAllRecordsFromTable(TABLE_NAME);
    	List<DatabaseObject> exerciseList = getAll(dbh);
    	for (int index = 0; index < exerciseList.size(); index++)
    	{
    		DatabaseHandler.deleteFile(((ExerciseLog)(exerciseList.get(index))).getVideoLocation());
    	}
		
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
	
	public static ExerciseLog getById(DatabaseHandler dbh, int id) throws Exception {
	    
		SQLiteDatabase db = dbh.getReadableDatabase();
	     
	     			
        Cursor cursor = db.query(TABLE_NAME,getDbKeyNames(), DbKeys.KEY_ID.getKeyName() + "=?",
                new String[] { Integer.toString(id) }, null, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            ExerciseLog object = createObjectFromCursor(cursor);
	        cursor.close();
	        
            return object;
        }
        else
        {
	        cursor.close();
	        
        	throw new java.lang.Exception("Cannot find "+TABLE_NAME+" matching id "+ id);
        }

	}
	
	
	public static ExerciseLog getByVideoLocation(DatabaseHandler dbh, String video_location) throws Exception {
	    
		SQLiteDatabase db = dbh.getReadableDatabase();
	     
	     			
        Cursor cursor = db.query(TABLE_NAME,getDbKeyNames(), DbKeys.KEY_EXERCISE_LOG_VIDEO_LOCATION.getKeyName() + "=?",
                new String[] { video_location }, null, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            ExerciseLog object = createObjectFromCursor(cursor);
	        cursor.close();
	        
            return object;
        }
        else
        {
	        cursor.close();
	        
        	throw new java.lang.Exception("Cannot find "+TABLE_NAME+" matching name "+video_location);
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
	
	
	protected static ExerciseLog createObjectFromCursor(Cursor cursor)
	{
		ExerciseLog exercise_log = new ExerciseLog();
        
        exercise_log.setId(Integer.parseInt(cursor.getString(DbKeys.KEY_ID.ordinal())));
        exercise_log.setExerciseId(Integer.parseInt(cursor.getString(DbKeys.KEY_EXERCISE_IDEXERCISE.ordinal())));
        exercise_log.setVideoLocation(cursor.getString(DbKeys.KEY_EXERCISE_LOG_VIDEO_LOCATION.ordinal()));
        exercise_log.setVideoNotes(cursor.getString(DbKeys.KEY_EXERCISE_LOG_VIDEO_NOTES.ordinal()));
        exercise_log.setCreateTime(cursor.getString(DbKeys.KEY_CREATE_TIME.ordinal()));
        
        return exercise_log;
	}
	
	@Override
	public String getIdKeyName() 
	{
		return DbKeys.KEY_ID.getKeyName();
	}

	public static List<DatabaseObject> getAllByExerciseId(
			DatabaseHandler dbh, int exercise_id) {
        List<DatabaseObject> objectList = new ArrayList<DatabaseObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "+DbKeys.KEY_EXERCISE_IDEXERCISE.getKeyName()+"="+Integer.toString(exercise_id);
 
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

}
