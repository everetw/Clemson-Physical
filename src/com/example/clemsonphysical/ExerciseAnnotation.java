/**
 * 
 */
package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author jburton
 *
 */
public class ExerciseAnnotation extends DatabaseObject {

	/**
	 * 
	 */
	
	public static final String KEY_ID = "idexercise_annotation";
	public static final String KEY_IDEXERCISE = "exercise_idexercise";
	public static final String KEY_EXERCISE_ANNOTATION_VIDEO_TIME = "exercise_annotation_video_time";
	public static final String KEY_EXERCISE_ANNOTATION_ANNOTATION = "exercise_annotation_annotation";
	public static final String KEY_CREATE_TIME = "create_time";

	
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
		return "exercise_idexercise";
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
        values.put(KEY_ID, this.getId());
    	values.put(KEY_IDEXERCISE, this.getExerciseId());
    	values.put(KEY_EXERCISE_ANNOTATION_VIDEO_TIME, this.getVideoTime());
    	values.put(KEY_EXERCISE_ANNOTATION_ANNOTATION, this.getAnnotation());
    	//values.put(KEY_CREATE_TIME, this.getCreateTime());

        // updating row
        int rc = db.update(getTableName(), values,KEY_ID + " = ?",
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
    	values.put(KEY_IDEXERCISE, this.getExerciseId());
    	values.put(KEY_EXERCISE_ANNOTATION_VIDEO_TIME, this.getVideoTime());
    	values.put(KEY_EXERCISE_ANNOTATION_ANNOTATION, this.getAnnotation());
    	//values.put(KEY_CREATE_TIME, this.getCreateTime());

        // Inserting Row
        db.insertOrThrow(this.getTableName(), null, values);
        
    }



	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#selectByID(com.example.clemsonphysical.DatabaseHandler)
	 */
	@Override
	public DatabaseObject selectById(DatabaseHandler db) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#selectAll(com.example.clemsonphysical.DatabaseHandler)
	 */
	@Override
	public List<DatabaseObject> selectAll(DatabaseHandler db) {
		// TODO Auto-generated method stub
		return null;
	}

}
