package edu.clemson.physicaltherapy.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import edu.clemson.physicaltherapy.database.DatabaseHandler;
import edu.clemson.physicaltherapy.database.DatabaseObject;
import edu.clemson.physicaltherapy.datamodel.Exercise.DbKeys;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	
	public enum DbKeys
	{
		// order must match SQL create statement.
		KEY_ID ("exercise_plan_item_id","Exercise Plan Item ID"),
		KEY_EXERCISE_PLAN_IDEXERCISE_PLAN ("exercise_plan_idexercise_plan","Exercise Plan ID"),
		KEY_EXERCISE_IDEXERCISE ("exercise_idexercise","Exercise ID"),
		KEY_EXERCISE_PLAN_ITEM_SEQUENCE ("exercise_plan_item_sequence","Sequence"),
		KEY_EXERCISE_PLAN_ITEM_QUANTITY ("exercise_plan_item_quantity","Number Reps."),
		KEY_EXERCISE_PLAN_ITEM_DESCRIPTION ("exercise_plan_item_description","Description");

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
	
	public static final String TABLE_NAME = "exercise_plan_item";
	
	private int exercise_plan_idexercise_plan;
	private int exercise_idexercise;
	private int exercise_plan_item_sequence;
	private int exercise_plan_item_quantity;
	private String exercise_plan_item_description;
	
	
	public ExercisePlanItem() {
		this(0);
	}

	public ExercisePlanItem(int id) {
		this(id,0,0,0,0,"");
		
	}
	
	public ExercisePlanItem(int id, int exercise_id, int exercise_plan_id, int sequence, int quantity, String description) 
	{
		super(id);
		this.exercise_plan_idexercise_plan = exercise_plan_id;
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
	
	public int getExercisePlanId()
	{
		return this.exercise_plan_idexercise_plan;
	}
	
	public void setExercisePlanId(int exercise_plan_id)
	{
		this.exercise_plan_idexercise_plan = exercise_plan_id;
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
		return TABLE_NAME;
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
        
        //values.put(KEY_ID, this.getId());
        values.put(DbKeys.KEY_EXERCISE_PLAN_IDEXERCISE_PLAN.getKeyName(), this.getExercisePlanId());
    	values.put(DbKeys.KEY_EXERCISE_IDEXERCISE.getKeyName(), this.getExerciseId());
    	values.put(DbKeys.KEY_EXERCISE_PLAN_ITEM_SEQUENCE.getKeyName(), this.getSequence());
    	values.put(DbKeys.KEY_EXERCISE_PLAN_ITEM_QUANTITY.getKeyName(), this.getQuantity());
    	values.put(DbKeys.KEY_EXERCISE_PLAN_ITEM_DESCRIPTION.getKeyName(), this.getDescription());
 
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
        values.put(DbKeys.KEY_EXERCISE_PLAN_IDEXERCISE_PLAN.getKeyName(), this.getExercisePlanId());
    	values.put(DbKeys.KEY_EXERCISE_IDEXERCISE.getKeyName(), this.getExerciseId());
    	values.put(DbKeys.KEY_EXERCISE_PLAN_ITEM_SEQUENCE.getKeyName(), this.getSequence());
    	values.put(DbKeys.KEY_EXERCISE_PLAN_ITEM_QUANTITY.getKeyName(), this.getQuantity());
    	values.put(DbKeys.KEY_EXERCISE_PLAN_ITEM_DESCRIPTION.getKeyName(), this.getDescription());
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
	
	
	protected static ExercisePlanItem createObjectFromCursor(Cursor cursor)
	{
		ExercisePlanItem exercise_plan_item = new ExercisePlanItem();
        
        exercise_plan_item.setId(Integer.parseInt(cursor.getString(DbKeys.KEY_ID.ordinal())));
        exercise_plan_item.setDescription(cursor.getString(DbKeys.KEY_EXERCISE_PLAN_ITEM_DESCRIPTION.ordinal()));
        exercise_plan_item.setExerciseId(Integer.parseInt(cursor.getString(DbKeys.KEY_EXERCISE_IDEXERCISE.ordinal())));
        exercise_plan_item.setExercisePlanId(Integer.parseInt(cursor.getString(DbKeys.KEY_EXERCISE_PLAN_IDEXERCISE_PLAN.ordinal())));
        exercise_plan_item.setSequence(Integer.parseInt(cursor.getString(DbKeys.KEY_EXERCISE_PLAN_ITEM_SEQUENCE.ordinal())));
        exercise_plan_item.setQuantity(Integer.parseInt(cursor.getString(DbKeys.KEY_EXERCISE_PLAN_ITEM_QUANTITY.ordinal())));
        
        return exercise_plan_item;
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
