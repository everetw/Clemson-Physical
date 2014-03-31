package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ExercisePlan extends DatabaseObject {
	
	
	public static final String KEY_ID = "idexercise_plan";
	public static final String KEY_EXERCISE_PLAN_NAME =  "exercise_plan_name";
	public static final String KEY_EXERCISE_PLAN_DESCRIPTION = "exercise_plan_description";
	
//	"CREATE TABLE \"exercise_plan\"(\n"+
//	"  \"idexercise_plan\" INTEGER PRIMARY KEY NOT NULL,\n"+
//	"  \"exercise_plan_name\" VARCHAR(45) NOT NULL,\n"+
//	"  \"exercise_plan_description\" VARCHAR(255)\n"+
//	");\n"

	private String exercise_plan_name;
	private String exercise_plan_description;
	
	
	
	public ExercisePlan() {
		this(0);
	}

	public ExercisePlan(int id) {
		this(id,"","");
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
	
	public void SetDescription(String plan_description)
	{
		this.exercise_plan_description = plan_description;
	}
	
	@Override
	public String getTableName() {
		return "exercise_plan";
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
        values.put(KEY_EXERCISE_PLAN_NAME, this.getName());
        values.put(KEY_EXERCISE_PLAN_DESCRIPTION, this.getDescription());

 
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
        values.put(KEY_EXERCISE_PLAN_NAME, this.getName());
        values.put(KEY_EXERCISE_PLAN_DESCRIPTION, this.getDescription());

		
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
