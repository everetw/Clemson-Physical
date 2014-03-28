package com.example.clemsonphysical;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



/// Class Must be serializable to pass in intent. 
/// http://www.tutorialspoint.com/java/java_serialization.htm



public abstract class DatabaseObject implements java.io.Serializable
{
	
	
	public final String URL_INSERT = "";
	public final String KEY_ID = "id";
	//public final String TABLE_NAME = "table";
	
	private static String baseUrl="http://people.cs.clemson.edu/~jburto2/CarMaintenance/";
	
	protected int	id;

	public String getAllUrl()
	{
		return getBaseUrl()+"getAll"+getTableName()+"s.php";
	}
	
	public String getUpdateUrl()
	{
		return getBaseUrl()+"update"+getTableName()+".php";
	}
	
	public String getByIdUrl()
	{
		return getBaseUrl()+"get"+getTableName()+"ById.php";
	}
	
	public String getDeleteUrl()
	{
		return getBaseUrl()+"delete"+getTableName()+".php";
	}
	
	public String getAddUrl()
	{
		return getBaseUrl()+"add"+getTableName()+".php";
	}
	
	public static String getDeleteAllUrl()
	{
		return getBaseUrl()+"deleteAll.php";
	}
	
	public DatabaseObject()
	{
	
		this(0);
				
	}
	
	public DatabaseObject(int id)
	{
		this.id = id;

				
	}
	

	
	public void setID(int id)
	{
		this.id=id;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public abstract String getTableName();
	
	public abstract List<NameValuePair> getParams();
	public abstract void setObjectFromJSON(JSONObject j) throws JSONException;
	public abstract void update(DatabaseHandler db);
	public abstract void add(DatabaseHandler db) throws Exception;
	public abstract void delete(DatabaseHandler db);
	public abstract DatabaseObject selectByID(DatabaseHandler db) throws Exception;
	public abstract List<DatabaseObject> selectAll(DatabaseHandler db);
	
	public void deleteAll(DatabaseHandler db) {
		// TODO Auto-generated method stub
		db.deleteAllRecordsFromTable(getTableName());
	}
		
	
	
    public static void setBaseUrl(String url)
    {
    	baseUrl = url;
    }
    
    public static String getBaseUrl()
    {

    	return baseUrl;
    }

}
