package edu.clemson.physicaltherapy.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import edu.clemson.physicaltherapy.database.DatabaseHandler;
import edu.clemson.physicaltherapy.datamodel.DatabaseObject;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseAnnotation;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;
import edu.clemson.physicaltherapy.datamodel.ExerciseLogAnnotation;
import edu.clemson.physicaltherapy.datamodel.ExercisePlan;
import edu.clemson.physicaltherapy.datamodel.ExercisePlanItem;
import edu.clemson.physicaltherapy.web.JSONParser;

/**
 * 
 * @author jburton
 *
 * @class DatabaseActivity
 * 
 * @brief This abstract class provides database connectivity (internal and external) to activities in the app.
 * @brief Activities that want to connect to the database should inherit from this class.
 */



public abstract class DatabaseActivity extends DisplayActivity {
	
	private static final String TAG_SUCCESS = "success";

	/**
	 * @var protected DatabaseHandler dbSQLite
	 * @brief This is the SQLite handler. Every app that connects to the internal database will need
	 * a database handler to access it
	 */
	protected DatabaseHandler dbSQLite = new DatabaseHandler(this);
	
	/**
	 * @var private DatabaseObject myDatabaseObject
	 * @brief Global variable pointing to a database object. This is the object that is used for querying the external database.
	 */
	private DatabaseObject myDatabaseObject;
	
	/**
	 * @var private List<DatabaseObject> databaseObjectList
	 * @brief Global variable pointing to a list of database objects 
	 */
	private List<DatabaseObject> databaseObjectList = new ArrayList<DatabaseObject>();

	/**
	 * @var private JSONParser jsonParser
	 * @brief The JSON Parser
	 */

    private JSONParser jsonParser = new JSONParser();

	public DatabaseActivity() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @class AddDatabaseObject
	 * @brief This is an async task that adds the database object stored in myDatabaseObject to the external database.
	 * This task is started by the this.addToExternalDatabase() method.
	 *
	 */
	
	
    class AddDatabaseObject extends AsyncTask<String, String, String> {
   	 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(DatabaseActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
        	
        	List<NameValuePair> params = myDatabaseObject.getParams();
        	for (int index = 0; index < params.size(); index++)
        	{
        		System.err.print(params.get(index).getName()+"="+params.get(index).getValue()+"&");
        	}
        	System.err.println("");
        	try {
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(myDatabaseObject.getAddUrl(),
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                	 // add returns the object created. This is to let MySQL take care of the autogeneration of indices.
                    JSONArray productObj = json
                            .getJSONArray(myDatabaseObject.getTableName()); // JSON Array
                    		
                            
                            // get first product object from JSON Array
                            JSONObject dbObject = productObj.getJSONObject(0);
                            
                            databaseObjectList.clear();
                            addToListFromJSON(dbObject);
                            
                	
                	
                	
                	return TAG_SUCCESS;
                } else {
                    // failed to create product
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
    

 
	/**
	 * 
	 * @class UpdateDatabaseObject
	 * @brief This is an async task that updates the database object stored in myDatabaseObject in the external database.
	 * This task is started by the this.updateInExternalDatabase() method.
	 * 
	 *
	 */
    class UpdateDatabaseObject extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(DatabaseActivity.this);
            pDialog.setMessage("Saving record ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 


 
            List<NameValuePair> params = myDatabaseObject.getParams();
            try {
            // sending modified data through http request
            // Notice that update product url accepts POST method
            	System.err.println("Updating via "+myDatabaseObject.getUpdateUrl());
            JSONObject json = jsonParser.makeHttpRequest(myDatabaseObject.getUpdateUrl(),
                    "POST", params);
 
            // check json success tag
            
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully updated
                    
                    return TAG_SUCCESS;
                } else {
                    // failed to update product. May not be update
                	
                }
            } 
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
    
	/**
	 * 
	 * @class DeleteDatabaseObject
	 * @brief This is an async task that deletes the database object stored in myDatabaseObject from the external database.
	 * This task is started by the this.DeleteInExternalDatabase() method.
	 * 
	 *
	 */

    
    class DeleteDatabaseObject extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(DatabaseActivity.this);
            pDialog.setMessage("Deleting...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {
 
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(myDatabaseObject.getIdKeyName(), Integer.toString(myDatabaseObject.getId())));
 
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        myDatabaseObject.getDeleteUrl(), "POST", params);
 
                // check your log for json response
                Log.d("Delete ", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    
                    return TAG_SUCCESS;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
	/**
	 * 
	 * @class DeleteAllDatabaseObjects
	 * @brief This is an async task that deletes all database objects for a given table.
	 * This does NOT use myDatabaseObject, but takes a table name as a parameter to doInBackground.
	 * This task is started by the this.DeleteAllRecordsFromExternalDatabase() method. 
	 * 
	 *
	 */   

    class DeleteAllDatabaseObjects extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(DatabaseActivity.this);
            pDialog.setMessage("Deleting All Records...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Deleting table. Table name is the first parameter.
         * */
        protected String doInBackground(String... args) {
 
        	String tableName = args[0];
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("table_name", tableName));
 
                System.err.println("deleteAllUrl="+DatabaseObject.getDeleteAllUrl());
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        DatabaseObject.getDeleteAllUrl(), "POST", params);
 
                // check your log for json response
                Log.d("Delete All", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    
                    return TAG_SUCCESS;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
    
	/**
	 * 
	 * @class GetDatabaseObjectById
	 * @brief This is an async task that gets an object from the external database from the id of the object stored in myDatabaseObject and stores it in the databaseObjectList.
	 * myDatabaseObjectList should have one object in it. 
	 *
	 */
    class GetDatabaseObjectById extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(DatabaseActivity.this);
            pDialog.setMessage("Loading record from remote DB. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... args) {
 

                // Check for success tag
            int success;
            try {
                // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(myDatabaseObject.getIdKeyName(), Integer.toString(myDatabaseObject.getId())));
                        
                        
 
                        // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(
                        myDatabaseObject.getByIdUrl(), "GET", params);
 
                        // check your log for json response
                Log.d("Single Product Details", json.toString());
 
                        // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray(myDatabaseObject.getTableName()); // JSON Array
                    		
                            
                            // get first product object from JSON Array
                            JSONObject dbObject = productObj.getJSONObject(0);
                            
                            databaseObjectList.clear();
                            addToListFromJSON(dbObject);
                            

                        }
                else{
                            // product with pid not found
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
    
	/**
	 * 
	 * @class GetAllDatabaseObjects
	 * @brief This is an async task that gets all objects from the external database for a given table and stores them in the databaseObjectList.
	 * The table information will be contained in myDatabaseObject. This can be a "dummy" object of the appropriate class with no data in it. 
	 *
	 */

    class GetAllDatabaseObjects extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(DatabaseActivity.this);
            pDialog.setMessage("Loading records from remote DB. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
            // getting JSON string from URL
            System.err.println("URL: "+myDatabaseObject.getAllUrl());
            try {
	            JSONObject json = jsonParser.makeHttpRequest(myDatabaseObject.getAllUrl(), "GET", params);
	 
	            // Check your log cat for JSON reponse
	            Log.d("All Products: ", json.toString());
	            databaseObjectList.clear();
	            
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) 
                {
                    // products found
                    // Getting Array of Products
                	
                    JSONArray products = json.getJSONArray(myDatabaseObject.getTableName());
                    System.err.println("Found "+products.length()+" objects for "+myDatabaseObject.getTableName());
                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) 
                    {
                        JSONObject c = products.getJSONObject(i);
                        
                        addToListFromJSON(c);
                    }
                } 
                else {
                    // no products found
                	System.err.println("Nothing found!");
                	
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	
        	System.err.println("onpostexecute");
            // dismiss the dialog once done
        	if (pDialog.isShowing())
        		pDialog.dismiss();
            super.onPostExecute(result);
        }
 
    }
    
    /**
     * @fn private void addToListFromJSON(JSONObject c)
     * @brief Adds a databaseObject to the list from a JSON Object. The type of database object should match the type of myDatabaseObject.
     * @param c
     * @throws JSONException
     */
    
    private void addToListFromJSON(JSONObject c) throws JSONException
    {
    	
    	DatabaseObject newObj = null;
    	
    	if (myDatabaseObject instanceof Exercise)
    	{
    		newObj = new Exercise();
    	}
    	else if (myDatabaseObject instanceof ExerciseLog)
    	{
    		newObj = new ExerciseLog();
    	}
    	else if (myDatabaseObject instanceof ExerciseAnnotation)
    	{
    		newObj = new ExerciseAnnotation();
    	}
    	else if (myDatabaseObject instanceof ExerciseLogAnnotation)
    	{
    		newObj = new ExerciseLogAnnotation();
    	}
    	else if (myDatabaseObject instanceof ExercisePlan)
    	{
    		newObj = new ExercisePlan();
    	}
    	else if (myDatabaseObject instanceof ExercisePlanItem)
    	{
    		newObj = new ExercisePlanItem();
    	}
    	
	    System.err.println("Adding new "+newObj.getTableName()+" object to list");
	    newObj.setObjectFromJSON(c);
	    

	    databaseObjectList.add(newObj);
    }
    
    /**
     * @fn protected void updateInExternalandInternalDatabase(DatabaseObject dbo)
     * @brief Update the given DatabaseObject dbo in external and internal database.
     * @param dbo
     */
    
    protected void updateInExternalAndInternalDatabase(DatabaseObject dbo)
    {
		myDatabaseObject = dbo;
		AsyncTask<String,String,String> updateTask = new UpdateDatabaseObject();
		String output = null;
		try {
			output = updateTask.execute().get();
			if (output.equals(TAG_SUCCESS))
			{
			    // if update was successful update. If not, do nothing.
				dbo.update(dbSQLite);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

    }
    
    /**
     * @fn protected void addToExternalandInternalDatabase(DatabaseObject dbo)
     * @brief Add the given DatabaseObject dbo to the external and internal database.
     * @param dbo
     */
    
    protected void addToExternalAndInternalDatabase(DatabaseObject dbo)
    {
		myDatabaseObject = dbo;
		AsyncTask<String,String,String> addTask = new AddDatabaseObject();
		String output = null;
		try {
			//External database add.
			output = addTask.execute().get();
			if (output != null && output.equals(TAG_SUCCESS))
			{
				// The global myDatabaseObject does not contain the newly index or any MySQL formatting adjustments. 
				// The new object with the index is stored in the list.
			    databaseObjectList.get(0).add(dbSQLite);
			}
			dbo.add(dbSQLite);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

    }
    
    /**
     * @fn protected void deleteFromExternalAndInternalDatabase(DatabaseObject dbo)
     * @brief Delete the given DatabaseObject dbo from the external and internal database.
     * @param dbo
     */
    
    protected void deleteFromExternalAndInternalDatabase(DatabaseObject dbo)
    {
		myDatabaseObject = dbo;
		AsyncTask<String,String,String> deleteTask = new DeleteDatabaseObject();
		String output = null;
		try {
			output = deleteTask.execute().get();
			if (output.equals(TAG_SUCCESS))
			{
			    dbo.delete(dbSQLite);
			}

				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * @fn protected void deleteAllRecordsFromExternalAndInternalTable(String tableName)
     * @brief delete all the records from the internal and external database for table tableName.
     * @param tableName
     */
    
    protected void deleteAllRecordsFromExternalAndInternalTable(String tableName)
    {
		
		AsyncTask<String,String,String> deleteAllTask = new DeleteAllDatabaseObjects();
		String output = null;
		try {
			output = deleteAllTask.execute(tableName).get();
			if (output != null && output.equals(TAG_SUCCESS))
			{
			    dbSQLite.deleteAllRecordsFromTable(tableName);
			}

				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * @fn protected void rebuildInternalTableFromExternal(DatabaseObject dbo)
     * @brief This method rebuilds the internal table from the external. 
     * This method should do the following
     * 1. Get all objects from the external database
     * 2. Add new objects that have been created in the external database.
     * 3. Update existing objects in internal database that have been changed in external database.
     * 4. Delete objects that have been removed from external database.
     * 5. Maintain data integrity.
     * 
     * Note: External and internal autogenerated indices will not match if both are autogenerated. External and internal data records should either have matching ids or a way to map external and internal ids. 
     * @param dbo
     * 
     */
    
    
    protected void rebuildInternalTableFromExternal(DatabaseObject dbo)
    {
    	// Set the global database object. This can be a "dummy" object. 
    	myDatabaseObject = dbo;
    	AsyncTask<String,String,String> getAllTask = new GetAllDatabaseObjects();
    	
		String output = null;
		try {
			System.err.println("executing");
			output = getAllTask.execute().get();
			System.err.println("done executing");

		    for (int index = 0; index < databaseObjectList.size(); index++)
		    {
		    	System.err.println("Adding id"+databaseObjectList.get(index).getId()+" for table "+databaseObjectList.get(index).getTableName());
		    	//TODO: Intelligently add/update/delete object from the database.
		    	// Can add/update/delete objects through the datamodel classes.
		    	// e.g. databaseObjectList.get(index).add(dbSQLite);
		    }

			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    /**
     * @fn protected void resynchronizeInternalDbFromExternal()
     * @brief Resynchronize the internal and external database.
     * 
     * For this application, we only need to worry about getting data 
     * from the external database and storing it in the internal database.
     */
    
    protected void resynchronizeInternalDbFromExternal()
    {

		rebuildInternalTableFromExternal(new Exercise());
		
		// Exercise log data is not in the external database.
		rebuildInternalTableFromExternal(new ExerciseAnnotation());
		rebuildInternalTableFromExternal(new ExercisePlan());
    	rebuildInternalTableFromExternal(new ExercisePlanItem());
		
    }
    
    public void setDatabaseObject(DatabaseObject dbo)
    {
    	myDatabaseObject = dbo;
    }
    
    public List<DatabaseObject> getDatabaseObjectList()
    {
    	return databaseObjectList;
    }

    
    @Override
	protected void onStop()
	{
		super.onStop();
		dbSQLite.close();
	}
}
