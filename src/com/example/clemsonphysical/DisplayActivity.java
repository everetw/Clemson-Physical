/**
 * 
 */
package com.example.clemsonphysical;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * @author jburton
 *
 */
public abstract class DisplayActivity extends Activity  {
	

	private static final String TAG_SUCCESS = "success";

	protected boolean autoSave = false;
	protected int colorTheme = LayoutUtils.HIGHLIGHT_COLOR;
	protected DatabaseHandler dbSQLite = new DatabaseHandler(this);
	//protected DatabaseMySQLHandler mySQLDB = new DatabaseMySQLHandler(this);
	
	protected DatabaseObject myDatabaseObject;
	protected List<DatabaseObject> databaseObjectList = new ArrayList<DatabaseObject>();
	

	
	
	
    // Progress Dialog
    protected ProgressDialog pDialog = null;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
	
	@Override

	/**
	 * @fn protected void onCreate(Bundle savedInstanceState)
	 * @brief Method called when activity is created. Sets the content view to activity_main. 
	 * 
	 * @param savedInstanceState
	 */
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    /// Preferences from http://developer.android.com/guide/topics/data/data-storage.html#pref
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//	    autoSave = settings.getBoolean(SettingsActivity.KEY_AUTOSAVE, false);
//	    LayoutUtils.setHighlightColor(Integer.parseInt(settings.getString(SettingsActivity.KEY_HIGHLIGHT_COLOR, "2")));
//	    colorTheme = LayoutUtils.HIGHLIGHT_COLOR;
//	    String urlBase = settings.getString(SettingsActivity.KEY_BASEURL, null);
//	    if (urlBase == null)
//	    {
//	    	displayMessageDialog("URL not set", "Database URL is not set! Please set this in the preferences.");
//	    	
//	    }
//	    else
//	    {
//	    	DatabaseObject.setBaseUrl(urlBase);
//	    }


	}

	
    /**
     * @fn public void displayToast(String message)
     * @brief Displays a popup "Toast" message to the user.
     * Displaying toasts from http://developer.android.com/guide/topics/ui/notifiers/toasts.html
     * @param message Message to display
     */
    public void displayToast(String message)
    {
    	Context context = this.getApplicationContext();
    	LayoutUtils.displayToast(context,message);
    }
    
    /**
     * @fn public void displayMessageDialog(String title, String message)
     * @brief Displays a message dialog to the user.
     * Displaying message dialogs from http://www.mkyong.com/android/android-alert-dialog-example/
     * @param message Message to display
     * @param title Title of dialog
     * 
     */
    
    public void displayMessageDialog(String title, String message )
    {
    	
		LayoutUtils.displayMessageDialog(this,title,message);
    }
    

	/**
	 * @fn public boolean onOptionsItemSelected(MenuItem item)
	 * @brief Handles menu item selection. 
	 * Only menu item here is the "action_about" for the info activity.
	 * @param item MenuItem that was selected
	 * @return true  
	 */
	public void onAboutButtonClick(View v) {
	

//	    Intent intent = new Intent(this, InfoView.class);
//	    startActivity(intent);

	}
//	public void onVehicleButtonClick(View v) {
//		
//
//	    Intent intent = new Intent(this, DisplayVehicleActivity.class);
//	    startActivity(intent);
//
//	}
//	public void onLocationButtonClick(View v) {
//		
//	    Intent intent = new Intent(this, DisplayLocationActivity.class);
//	    startActivity(intent);
//
//	}
//	public void onWorkButtonClick(View v) {
//		
//
//	    Intent intent = new Intent(this, DisplayWorkActivity.class);
//	    startActivity(intent);
//
//	}
//
//	
//	public void onReceiptButtonClick(View v) {
//		
//
//	    Intent intent = new Intent(this, DisplayReceiptActivity.class);
//	    startActivity(intent);
//
//	}
//	
//	public void onItemButtonClick(View v) {
//		
//
//	    Intent intent = new Intent(this, DisplayItemActivity.class);
//	    startActivity(intent);
//
//	}
	

	public void onDateFieldClick(View v) {
		
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		LayoutUtils.displayDatePickerDialog(this, v, year, month, day);


	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		dbSQLite.close();
	}
	
    class AddDatabaseObject extends AsyncTask<String, String, String> {
   	 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(DisplayActivity.this);
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
            	pDialog = new ProgressDialog(DisplayActivity.this);
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
            	pDialog = new ProgressDialog(DisplayActivity.this);
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
            	pDialog = new ProgressDialog(DisplayActivity.this);
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
            	pDialog = new ProgressDialog(DisplayActivity.this);
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
            	pDialog = new ProgressDialog(DisplayActivity.this);
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

}






