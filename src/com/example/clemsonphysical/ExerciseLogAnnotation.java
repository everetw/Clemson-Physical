package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ExerciseLogAnnotation extends DatabaseObject {
	
	public static final String KEY_ID = "idexercise_annotation";
	public static final String KEY_IDEXERCISE_LOG = "exercise_log_idexercise_log";
	public static final String KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME = "exercise_log_annotation_video_time";
	public static final String KEY_EXERCISE_LOG_ANNOTATION_ANNOTATION = "exercise_log_annotation_annotation";
	public static final String KEY_CREATE_TIME = "create_time";
	
//	"CREATE TABLE \"exercise_log_annotation\"(\n"+
//	"  \"idexercise_log_annotation\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"+
//	"  \"exercise_log_idexercise_log\" INTEGER NOT NULL,\n"+
//	"  \"exercise_log_annotation_video_time\" INTEGER NOT NULL,\n"+
//	"  \"exercise_log_annotation_annotation\" VARCHAR(255) NOT NULL,\n"+
//	"  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
//	"  CONSTRAINT \"fk_exercise_log_annotation_exercise_log1\"\n"+
//	"    FOREIGN KEY(\"exercise_log_idexercise_log\")\n"+
//	"    REFERENCES \"exercise_log\"(\"idexercise_log\")\n"+
//	");\n",
	
	private int exercise_log_idexercise_log;
	private int exercise_log_annotation_video_time;
	private String exercise_log_annotation_annotation;
	private String create_time;

	public ExerciseLogAnnotation() {
		
		this(0);
	}

	public ExerciseLogAnnotation(int id) {
		this(0,0,0,"","");

	}
	public ExerciseLogAnnotation(int id, int exercise_log_id, int video_time, String annotation)
	{
		this(id, exercise_log_id, video_time, annotation, "");
	}
	
	public ExerciseLogAnnotation(int id, int exercise_log_id, int video_time, String annotation, String create_time)
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
	public String getTableName() {
		
		return "exercise_log_annotation";
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
    	values.put(KEY_IDEXERCISE_LOG, this.getExerciseLogId());
    	values.put(KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME, this.getVideoTime());
    	values.put(KEY_EXERCISE_LOG_ANNOTATION_ANNOTATION, this.getAnnotation());
    	//CREATE_TIME does not get updated.

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
    	values.put(KEY_IDEXERCISE_LOG, this.getExerciseLogId());
    	values.put(KEY_EXERCISE_LOG_ANNOTATION_VIDEO_TIME, this.getVideoTime());
    	values.put(KEY_EXERCISE_LOG_ANNOTATION_ANNOTATION, this.getAnnotation());
    	//CREATE_TIME does not get updated.
        // Inserting Row
        db.insertOrThrow(this.getTableName(), null, values);

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
