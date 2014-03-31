package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class ExercisePlanItem extends DatabaseObject {


//	"CREATE TABLE \"exercise_plan_item\"(\n"+
//	"  \"exercise_plan_item_id\" INTEGER PRIMARY KEY NOT NULL,\n"+
//	"  \"exercise_plan_idexercise_plan\" INTEGER NOT NULL,\n"+
//	"  \"exercise_plan_item_sequence\" INTEGER NOT NULL,\n"+
//	"  \"exercise_plan_item_quantity\" INTEGER NOT NULL,\n"+
//	"  \"exercise_plan_item_description\" VARCHAR(255),\n"+
//	"  \"exercise_idexercise\" INTEGER NOT NULL,\n"+
//	"  CONSTRAINT \"fk_exercise_plan_item_exercise_plan1\"\n"+
//	"    FOREIGN KEY(\"exercise_plan_idexercise_plan\")\n"+
//	"    REFERENCES \"exercise_plan\"(\"idexercise_plan\"),\n"+
//	"  CONSTRAINT \"fk_exercise_plan_item_exercise1\"\n"+
//	"    FOREIGN KEY(\"exercise_idexercise\")\n"+
//	"    REFERENCES \"exercise\"(\"idexercise\")\n"+
//	");\n",
	
	
	private int exercise_plan_idexercise_plan;
	private int exercise_idexercise;
	private int exercise_plan_item_sequence;
	private int exercise_plan_item_quantity;
	private String exercise_plan_item_description;
	
	
	public ExercisePlanItem() {
		this(0);
	}

	public ExercisePlanItem(int id) {
		this(id,0,0,0,"");
		
	}
	
	public ExercisePlanItem(int id, int exercise_id, int sequence, int quantity, String description) 
	{
		super(id);
		this.exercise_idexercise = exercise_id;
		this.exercise_plan_item_sequence = sequence;
		this.exercise_plan_item_quantity = quantity;
		this.exercise_plan_item_description = description;
		
	}
	
	public int getExerciseId()
	{
		return this.exercise_idexercise;
	}
	
	public void setExerciseId(int exercise_id)
	{
		this.exercise_idexercise = exercise_id;
	}
	
	public int getSequence()
	{
		return this.exercise_plan_item_sequence;
	}
	
	public void setSequence(int sequence)
	{
		this.exercise_plan_item_sequence = sequence;
	}
	
	public int getQuantity()
	{
		return this.exercise_plan_item_quantity;
	}
	
	public void setQuantity(int quantity)
	{
		this.exercise_plan_item_quantity = quantity;
	}
	
	public String getDescription()
	{
		return this.exercise_plan_item_description;
		
	}
	
	public void setDescription(String description)
	{
		this.exercise_plan_item_description = description;
	}

	@Override
	public String getTableName() {
		return "exercise_plan_item";
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
