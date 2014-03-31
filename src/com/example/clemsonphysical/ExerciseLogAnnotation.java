package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class ExerciseLogAnnotation extends DatabaseObject {
	
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
	
	public void setCreateTime()
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
	public void update(DatabaseHandler db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(DatabaseHandler db) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(DatabaseHandler db) {
		// TODO Auto-generated method stub

	}

	@Override
	public DatabaseObject selectByID(DatabaseHandler db) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DatabaseObject> selectAll(DatabaseHandler db) {
		// TODO Auto-generated method stub
		return null;
	}

}
