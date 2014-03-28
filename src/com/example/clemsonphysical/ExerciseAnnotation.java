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
public class ExerciseAnnotation extends DatabaseObject {

	/**
	 * 
	 */
	public ExerciseAnnotation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 */
	public ExerciseAnnotation(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.example.clemsonphysical.DatabaseObject#getTableName()
	 */
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
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
