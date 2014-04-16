package edu.clemson.physicaltherapy.datamodel;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.clemson.physicaltherapy.database.DatabaseHandler;



/// Class Must be serializable to pass in intent. 
/// http://www.tutorialspoint.com/java/java_serialization.htm



public abstract class DatabaseObject implements java.io.Serializable
{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4847728748712633666L;
	public final String URL_INSERT = "";
	//public final String KEY_ID = "id";
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
	

	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public abstract String getTableName();
	
	public abstract List<NameValuePair> getParams();
	public abstract void setObjectFromJSON(JSONObject j) throws JSONException;
	public abstract int update(DatabaseHandler dbh);
	public abstract void add(DatabaseHandler dbh) throws Exception;
	public abstract String getIdKeyName();

	public void delete(DatabaseHandler dbh) {
		//System.err.println("Deleting object from "+this.getTableName()+ " where " + this.getIdKeyName()+" = "+this.getId());
        SQLiteDatabase db = dbh.getWritableDatabase();
        int rc = db.delete(this.getTableName(), this.getIdKeyName() + " = ?",
                new String[] { String.valueOf(this.getId()) });
        //System.err.println("Delete returned "+rc);

	}
	
	public int getCount(DatabaseHandler dbh)
	{
        String countQuery = "SELECT  * FROM " + this.getTableName();
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        int count =  cursor.getCount();
        
        
        return count;
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
