/**
 * 
 */
package com.example.clemsonphysical;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * @author jburton
 *
 * Database handler from  http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "carMaintenance";
 
    //table names
    
    private static final String TABLE_ITEMS = "item";
    private static final String TABLE_WORKS = "work";
    private static final String TABLE_RECEIPTS = "receipt";
    private static final String TABLE_LOCATIONS = "location";
 
    private static final String SQL_FILE = "file:////android_asset/CarMaintenance.sql";
    
   // private static final String SQL_COMMANDS = 
    private Context myContext;
    
    public DatabaseHandler(Context context) 
    {
    	
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	// Save the context! http://stackoverflow.com/questions/15586072/using-getassets-gives-the-error-the-method-getassets-is-undefined-for-the-t
    	myContext = context; //save the context!
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) 
    {

    	// Run all the SQL statements
    	for (int i = 0; i < DatabaseSQL.CREATE_DATABASE.length; i++)
    		db.execSQL(DatabaseSQL.CREATE_DATABASE[i]);

        
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
//        // Drop older table if existed
    	// Run all the SQL statements
    	for (int i = 0; i < DatabaseSQL.DROP_DATABASE.length; i++)
    		db.execSQL(DatabaseSQL.DROP_DATABASE[i]);

//        // Create tables again
//        onCreate(db);
        
        
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    public void deleteAndRebuild()
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	onUpgrade(db,0,0);
    }
    
    
    public void deleteAllRecordsFromTable(String table_name) {
    	
//    	if (table_name.equals(Receipt.TABLE_NAME))
//    	{
//    		deleteAllReceiptImages();
//    	}
//    	
    	try {
	        SQLiteDatabase db = this.getWritableDatabase();
	        db.delete(table_name, null, null);
	        
    	} catch (NullPointerException npe)
    	{
    		
    	}
    	
    }
    
	public static void deleteFile(String filename)
	{
		try 
		{
			
			File file = new File(filename);
			file.delete();
		}
		catch (Exception e)
		{
			System.err.println("Could not delete "+filename);
			System.err.println(e.getMessage());
		}
	}
	
    

	
    
}
