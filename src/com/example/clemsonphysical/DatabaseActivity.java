package com.example.clemsonphysical;

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



public abstract class DatabaseActivity extends DisplayActivity {
	
	private static final String TAG_SUCCESS = "success";


	protected DatabaseHandler dbSQLite = new DatabaseHandler(this);
	//protected DatabaseMySQLHandler mySQLDB = new DatabaseMySQLHandler(this);
	
	protected DatabaseObject myDatabaseObject;
	protected List<DatabaseObject> databaseObjectList = new ArrayList<DatabaseObject>();
	

	
	
	

 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

	public DatabaseActivity() {
		// TODO Auto-generated constructor stub
	}

	
	
	
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
     * Background Async Task to Get complete product details
     * */
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
                        params.add(new BasicNameValuePair(myDatabaseObject.KEY_ID, Integer.toString(myDatabaseObject.getID())));
                        
                        
 
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
     * Background Async Task to  Save product Details
     * */
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
 
    /*****************************************************************
     * Background Async Task to Delete Product
     * */
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
                params.add(new BasicNameValuePair(myDatabaseObject.KEY_ID, Integer.toString(myDatabaseObject.getID())));
 
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
    
    /*****************************************************************
     * Background Async Task to Delete Product
     * */
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
         * Deleting product
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
                        myDatabaseObject.getDeleteAllUrl(), "POST", params);
 
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
     * Background Async Task to Load all product by making HTTP Request
     * */
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
    
    private void addToListFromJSON(JSONObject c) throws JSONException
    {
    	
    	DatabaseObject newObj = null;
//	    if(myDatabaseObject.getTableName().equals(Work.TABLE_NAME))
//	    {
//	    	
//	    	newObj = new Work();
//	    }
//	    else if(myDatabaseObject.getTableName().equals(Receipt.TABLE_NAME))
//	    {
//	    	newObj = new Receipt();
//	    }
//	    else if(myDatabaseObject.getTableName().equals(Item.TABLE_NAME))
//	    {
//	    	newObj = new Item();
//	    }
//	
//	    else if(myDatabaseObject.getTableName().equals(Vehicle.TABLE_NAME))
//	    {
//	    	newObj = new Vehicle();
//	    }
//	
//	    else if(myDatabaseObject.getTableName().equals(Location.TABLE_NAME))
//	    {
//	    	newObj = new Location();
//	    }
	    System.err.println("Adding new "+newObj.getTableName()+" object to list");
	    newObj.setObjectFromJSON(c);
	    

	    databaseObjectList.add(newObj);
    }
    
    protected void updateInDatabase(DatabaseObject dbo)
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
    
    protected void addToDatabase(DatabaseObject dbo)
    {
		myDatabaseObject = dbo;
		AsyncTask<String,String,String> addTask = new AddDatabaseObject();
		String output = null;
		try {
			output = addTask.execute().get();
			if (output != null && output.equals(TAG_SUCCESS))
			{
				// The global myDatabaseObject does not contain the newly index or any MySQL formatting adjustments. 
				// The new object with the index is stored in the list.
			    databaseObjectList.get(0).add(dbSQLite);
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
    
    protected void deleteFromDatabase(DatabaseObject dbo)
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
    
    protected void deleteAllRecordsFromTable(String tableName)
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
    
    protected void rebuildTableFromRemote(DatabaseObject dbo)
    {
    	myDatabaseObject = dbo;
    	AsyncTask<String,String,String> getAllTask = new GetAllDatabaseObjects();
    	
		String output = null;
		try {
			System.err.println("executing");
			output = getAllTask.execute().get();
			System.err.println("done executing");

		    for (int index = 0; index < databaseObjectList.size(); index++)
		    {
		    	System.err.println("Adding id"+databaseObjectList.get(index).getID()+" for table "+databaseObjectList.get(index).getTableName());
		    	databaseObjectList.get(index).add(dbSQLite);
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
    
    protected void deleteAndRebuildAllFromRemote()
    {

		//DatabaseObject db = new Vehicle();
		//System.err.println("Inheritance test: "+db.getTableName());

    	dbSQLite.deleteAndRebuild();
//		rebuildTableFromRemote(new Vehicle());
//		
//		rebuildTableFromRemote(new Location());
//		rebuildTableFromRemote(new Item());
//		rebuildTableFromRemote(new Receipt());
//		rebuildTableFromRemote(new Work());
		
    }
    
    @Override
	protected void onStop()
	{
		super.onStop();
		dbSQLite.close();
	}
}
