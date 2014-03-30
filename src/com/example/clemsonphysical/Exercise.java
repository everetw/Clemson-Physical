/**
 * 
 */
package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
	private String exercise_instruction_url;
	private String exercise_file_location;
	
	/**
	 * 		"CREATE TABLE \"exercise\"(\n" +
		"  \"idexercise\" INTEGER PRIMARY KEY NOT NULL,\n"+
		"  \"exercise_name\" VARCHAR(45) NOT NULL,\n"+
		"  \"exercise_video_url\" VARCHAR(127),\n"+
		"  \"exercise_instruction_url\" VARCHAR(127),\n"+
		"  \"exercise_file_location\" VARCHAR(127),\n"+
		"  \"create_time\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"+
		"  \"update_time\" TIMESTAMP\n"+
		");",
	 */
	public Exercise() {
		this(0,"","","","");
	}
	
	public Exercise(int id, String name, String video_url, String instruction_url, String file_location)
	{
		this.id = id;
		this.exercise_name = name;
		this.exercise_file_location = file_location; 
		this.exercise_instruction_url = instruction_url;
		this.exercise_video_url = video_url;
	}

	/**
	 * @param id
	 */
	public Exercise(int id) {
		this(id,"","","","");
	}

	public String getExerciseName()
	{
		return this.exercise_name;
	}
	
	public void setExerciseName(String name)
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
	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#getTableName()
	 */
	@Override
	public String getTableName() {

		return "exercise";
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
	public void update(DatabaseHandler db) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#add(com.example.clemsonphysical.DatabaseHandler)
	 */
	@Override
	public void add(DatabaseHandler db) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#delete(com.example.clemsonphysical.DatabaseHandler)
	 */
	@Override
	public void delete(DatabaseHandler db) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#selectByID(com.example.clemsonphysical.DatabaseHandler)
	 */
	@Override
	public DatabaseObject selectByID(DatabaseHandler db) throws Exception {
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
