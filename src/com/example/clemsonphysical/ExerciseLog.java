package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

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
	
	public static final String KEY_ID = "id_exercise_log";
	public static final String KEY_EXERCISE_LOG_VIDEO_LOCATION = "exercise_log_video_location";
	public static final String KEY_EXERCISE_LOG_VIDEO_NOTES = "exercise_log_video_notes";
	public static final String KEY_CREATE_TIME = "create_time";
	public static final String KEY_EXERCISE_IDEXERCISE = "exercise_idexercise";
	
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
		// TODO Auto-generated method stub
		return null;
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
        values.put(KEY_ID, this.getId());
    	values.put(KEY_EXERCISE_LOG_VIDEO_LOCATION, this.getVideoLocation());
    	values.put(KEY_EXERCISE_LOG_VIDEO_NOTES, this.getVideoNotes());
    	// CREATE_TIME does not get updated.  	
    	values.put(KEY_EXERCISE_IDEXERCISE, this.getExerciseId());
 
        // updating row
        int rc = db.update(getTableName(), values,KEY_ID + " = ?",
                new String[] { String.valueOf(getId()) });
        
        
        return rc;


	}

	@Override
	public void add(DatabaseHandler dbh) throws Exception {
		SQLiteDatabase db = dbh.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, this.getId());
    	values.put(KEY_EXERCISE_LOG_VIDEO_LOCATION, this.getVideoLocation());
    	values.put(KEY_EXERCISE_LOG_VIDEO_NOTES, this.getVideoNotes());
    	// CREATE_TIME does not get updated.  	
    	values.put(KEY_EXERCISE_IDEXERCISE, this.getExerciseId());
    	
        // Inserting Row
        db.insertOrThrow(this.getTableName(), null, values);

	}

	@Override
	public void delete(DatabaseHandler dbh) {
		//TODO Delete video when record is deleted.
		//this.getVideoLocation();
		super.delete(dbh);
	
	}
	
	@Override
	public void deleteAll(DatabaseHandler dbh) 
	{
		super.deleteAll(dbh);
	}

	@Override
	public DatabaseObject selectById(DatabaseHandler db) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DatabaseObject> selectAll(DatabaseHandler db) {
		// TODO Auto-generated method stub
		return null;
	}

}
