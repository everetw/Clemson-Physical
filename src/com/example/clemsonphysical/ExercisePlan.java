package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class ExercisePlan extends DatabaseObject {
	
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
	
	public String getPlanName()
	{
		return this.exercise_plan_name;
	}
	
	public void setPlanName(String plan_name)
	{
		this.exercise_plan_name = plan_name;
	}
	
	public String getPlanDescription()
	{
		return this.exercise_plan_description;
	}
	
	public void SetPlanDescription(String plan_description)
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
