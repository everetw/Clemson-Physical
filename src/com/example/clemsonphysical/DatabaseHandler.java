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
    
//    // Adding new vehicle
//    public void addVehicle(Vehicle vehicle) throws Exception {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//       
//        values.put(Vehicle.KEY_ID,vehicle.getID());
//        values.put(Vehicle.KEY_VEHICLEDESCRIPTION, vehicle.getVehicleDescription()); // Vehicle description
// 
//        // Inserting Row
//        db.insertOrThrow(Vehicle.TABLE_NAME, null, values);
//         // Closing database connection
//    }
//
//    // Getting single vehicle
//    public Vehicle getVehicleById(int id) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
// 
//        Cursor cursor = db.query(Vehicle.TABLE_NAME, new String[] { Vehicle.KEY_ID, Vehicle.KEY_VEHICLEDESCRIPTION }, Vehicle.KEY_ID + "=?",
//                new String[] { Integer.toString(id) }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//        	Vehicle vehicle = new Vehicle(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
//	        cursor.close();
//	        
//            return vehicle;
//        }
//        else
//        {
//	        cursor.close();
//	        
//        	throw new java.lang.Exception("Cannot find vehicle matching id "+id);
//        }
//
//    }
//    // Getting single vehicle
//    public Vehicle getVehicle(String name) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
// 
//        Cursor cursor = db.query(Vehicle.TABLE_NAME, new String[] { Vehicle.KEY_ID, Vehicle.KEY_VEHICLEDESCRIPTION }, Vehicle.KEY_VEHICLEDESCRIPTION + "=?",
//                new String[] { name }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//        	Vehicle vehicle = new Vehicle(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
//	        cursor.close();
//	        
//            return vehicle;
//        }
//        else
//        {
//	        cursor.close();
//	        
//        	throw new java.lang.Exception("Cannot find vehicle matching name "+name);
//        }
//
//    }
//     
//    // Getting All Vehicles
//    public List<DatabaseObject> getAllVehicles() {
//        List<DatabaseObject> vehicleList = new ArrayList<DatabaseObject>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Vehicle.TABLE_NAME;
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Vehicle vehicle = new Vehicle();
//                
//                vehicle.setID(Integer.parseInt(cursor.getString(0)));
//                vehicle.setVehicleDescription(cursor.getString(1));
//               
//                // Adding vehicle to list
//                vehicleList.add(vehicle);
//            } while (cursor.moveToNext());
//        }
// 
//        cursor.close();
//        
//        
//        return vehicleList;
//    }
// 
//    // Updating single vehicle
//    public int updateVehicle(Vehicle vehicle) {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//        values.put(Vehicle.KEY_ID, vehicle.getID());
//        values.put(Vehicle.KEY_VEHICLEDESCRIPTION, vehicle.getVehicleDescription());
// 
//        // updating row
//        int rc = db.update(Vehicle.TABLE_NAME, values, Vehicle.KEY_ID + " = ?",
//                new String[] { String.valueOf(vehicle.getID()) });
//        
//        
//        return rc;
//    }
// 
//    // Deleting single vehicle
//    public void deleteVehicle(Vehicle vehicle) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Vehicle.TABLE_NAME, Vehicle.KEY_ID + " = ?",
//                new String[] { String.valueOf(vehicle.getID()) });
//        
//    }
// 
// 
//    // Getting vehicles Count
//    public int getVehiclesCount() {
//        String countQuery = "SELECT  * FROM " + Vehicle.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
// 
//        // return count
//        int count =  cursor.getCount();
//        
//        
//        return count;
//    }
//    
//    /**
//     * All CRUD(Create, Read, Update, Delete) Operations
//     */
// 
//    // Adding new item
//    void addItem(Item item) throws Exception {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//       
//        values.put(Item.KEY_ID,item.getID());
//        values.put(Item.KEY_ITEMDESCRIPTION, item.getItemDescription());
//        values.put(Item.KEY_ITEMMILEAGEINTERVAL, item.getMileageInterval());
//        values.put(Item.KEY_ITEMTIMEINTERVAL, item.getTimeInterval());
// 
//        // Inserting Row
//        db.insertOrThrow(Item.TABLE_NAME, null, values);
//         // Closing database connection
//    }
// 
// 
//    // Getting single item
//    Item getItemById(int id) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
//        
// 
//        Cursor cursor = db.query(Item.TABLE_NAME, new String[] { Item.KEY_ID, Item.KEY_ITEMDESCRIPTION, Item.KEY_ITEMMILEAGEINTERVAL, Item.KEY_ITEMTIMEINTERVAL }, Item.KEY_ID + "=?",
//                new String[] { Integer.toString(id) }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//
//	        Item item = new Item(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)));
//	        cursor.close();
//	        
//	        return item;
//        }
//        else
//        {
//
//	        cursor.close();
//	        
//        	throw new java.lang.Exception("Cannot find item matching id "+id);
//        }
//
//    }
//    
//    // Getting single item
//    Item getItem(String name) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
//        
// 
//        Cursor cursor = db.query(Item.TABLE_NAME, new String[] { Item.KEY_ID, Item.KEY_ITEMDESCRIPTION, Item.KEY_ITEMMILEAGEINTERVAL, Item.KEY_ITEMTIMEINTERVAL }, Item.KEY_ITEMDESCRIPTION + "=?",
//                new String[] { name }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//	
//	        Item item = new Item(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)));
//	        
//	        cursor.close();
//	        
//
//	        return item;
//        }
//        else
//        {
//            cursor.close();
//            
//        	throw new java.lang.Exception("Cannot find item matching "+name);
//        }
//
//    }
//     
//    // Getting All Items
//    public List<DatabaseObject> getAllItems() {
//        List<DatabaseObject> itemList = new ArrayList<DatabaseObject>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Item.TABLE_NAME;
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Item item = new Item();
//                
//                item.setID(Integer.parseInt(cursor.getString(0)));
//                item.setItemDescription(cursor.getString(1));
//                item.setMileageInterval(Integer.parseInt(cursor.getString(2)));
//                item.setTimeInterval(Integer.parseInt(cursor.getString(3)));
//                // Adding item to list
//                itemList.add(item);
//            } while (cursor.moveToNext());
//        }
// 
//        // return item list
//        cursor.close();
//        
//        return itemList;
//    }
// 
//    // Updating single item
//    public int updateItem(Item item) {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//        values.put(Item.KEY_ID, item.getID());
//        values.put(Item.KEY_ITEMDESCRIPTION, item.getItemDescription());
//        values.put(Item.KEY_ITEMMILEAGEINTERVAL, item.getMileageInterval());
//        values.put(Item.KEY_ITEMTIMEINTERVAL, item.getTimeInterval());
// 
//        // updating row
//        int rc = db.update(Item.TABLE_NAME, values, Item.KEY_ID + " = ?",
//                new String[] { String.valueOf(item.getID()) });
//        
//        
//        
//        return rc;
//    }
// 
//    // Deleting single item
//    public void deleteItem(Item item) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Item.TABLE_NAME, Item.KEY_ID + " = ?",
//                new String[] { String.valueOf(item.getID()) });
//        
//    }
// 
// 
//    // Getting items Count
//    public int getItemsCount() {
//        String countQuery = "SELECT  * FROM " + Item.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//        
//        
//        // return count
//        return cursor.getCount();
//    }
// 
//    /**
//     * All CRUD(Create, Read, Update, Delete) Operations
//     */
// 
//    // Adding new receipt
//    void addReceipt(Receipt receipt)  throws Exception {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//       
//        values.put(Receipt.KEY_ID,receipt.getID());
//        values.put(Receipt.KEY_RECEIPTFILE, receipt.getFile()); // Receipt description
//        values.put(Receipt.KEY_LOCATION_IDLOCATION, receipt.getLocationID());
//        values.put(Receipt.KEY_RECEIPTDATE, receipt.getDate());
//        values.put(Receipt.KEY_RECEIPTAMOUNT, receipt.getAmount());
//        values.put(Receipt.KEY_RECEIPTNOTES, receipt.getNotes());
// 
//        // Inserting Row
//        db.insertOrThrow(Receipt.TABLE_NAME, null, values);
//         // Closing database connection
//    }
// 
// 
//    // Getting single receipt
//    Receipt getReceiptById(int id) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
//        
// 
//        Cursor cursor = db.query(Receipt.TABLE_NAME, new String[] { 
//        					Receipt.KEY_ID, 
//        					Receipt.KEY_RECEIPTFILE,
//							Receipt.KEY_LOCATION_IDLOCATION,
//							Receipt.KEY_RECEIPTDATE,
//							Receipt.KEY_RECEIPTAMOUNT,
//							Receipt.KEY_RECEIPTNOTES  }, Receipt.KEY_ID + "=?",
//                new String[] { Integer.toString(id) }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//	        
//	
//	        Receipt receipt = new Receipt(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),cursor.getString(3),Integer.parseInt(cursor.getString(4)),cursor.getString(5));
//	        // return receipt
//	        cursor.close();
//	        
//	        return receipt;
//	    }
//	    else
//	    {
//	        cursor.close();
//	        
//	    	throw new java.lang.Exception("Cannot find receipt matching id "+id);
//	    }
//    }
//    
//    // Getting single receipt
//    Receipt getReceipt(String filename) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
//        
// 
//        Cursor cursor = db.query(Receipt.TABLE_NAME, new String[] { 
//        		Receipt.KEY_ID, 
//				Receipt.KEY_RECEIPTFILE,
//				Receipt.KEY_LOCATION_IDLOCATION,
//				Receipt.KEY_RECEIPTDATE,
//				Receipt.KEY_RECEIPTAMOUNT,
//				
//				Receipt.KEY_RECEIPTNOTES
//        		
//        }, Receipt.KEY_RECEIPTFILE + "=?",
//                new String[] { filename }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//	          
//	        Receipt receipt = new Receipt(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),cursor.getString(3),Integer.parseInt(cursor.getString(4)),cursor.getString(5));
//	        // return receipt
//	        cursor.close();
//	        
//	        return receipt;
//	    }
//	    else
//	    {
//	        cursor.close();
//	        
//	    	throw new java.lang.Exception("Cannot find receipt matching filename"+filename);
//	    }
//        
//    }
//    
//    // Getting single receipt
//    Receipt getReceiptByLocationAndDate(int lid, String date) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
//        
// 
//        Cursor cursor = db.query(Receipt.TABLE_NAME, new String[] { 
//        		Receipt.KEY_ID, 
//				Receipt.KEY_RECEIPTFILE,
//				Receipt.KEY_LOCATION_IDLOCATION,
//				Receipt.KEY_RECEIPTDATE,
//				Receipt.KEY_RECEIPTAMOUNT,
//				Receipt.KEY_RECEIPTNOTES
//        		
//        }, Receipt.KEY_LOCATION_IDLOCATION + "=? and "+ Receipt.KEY_RECEIPTDATE +"=?",
//                new String[] { Integer.toString(lid), date }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//	          
//	        Receipt receipt = new Receipt(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),cursor.getString(3),Integer.parseInt(cursor.getString(4)),cursor.getString(5));
//	        // return receipt
//	        cursor.close();
//	        
//	        return receipt;
//	    }
//	    else
//	    {
//	        cursor.close();
//	        
//	    	throw new java.lang.Exception("Cannot find receipt matching location id="+lid+" and date="+date);
//	    }
//        
//    }
//
//     
//    // Getting All Receipts
//    public List<DatabaseObject> getAllReceipts() {
//        List<DatabaseObject> receiptList = new ArrayList<DatabaseObject>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Receipt.TABLE_NAME;
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Receipt receipt = new Receipt();
//                
//                receipt.setID(Integer.parseInt(cursor.getString(0)));
//                receipt.setFile(cursor.getString(1));
//                receipt.setLocationID(Integer.parseInt(cursor.getString(2)));
//                receipt.setDate(cursor.getString(3));
//                receipt.setAmount(Integer.parseInt(cursor.getString(4)));
//                receipt.setNotes(cursor.getString(5));
//                // Adding receipt to list
//                receiptList.add(receipt);
//            } while (cursor.moveToNext());
//        }
// 
//        // return receipt list
//
//        cursor.close();
//        
//        return receiptList;
//    }
//    
//    // Getting All Receipts
//    public List<DatabaseObject> getAllReceiptsByLocationId(int vid) {
//        List<DatabaseObject> receiptList = new ArrayList<DatabaseObject>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Receipt.TABLE_NAME + " where "+Receipt.KEY_LOCATION_IDLOCATION+"="+Integer.toString(vid);
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Receipt receipt = new Receipt();
//                
//                receipt.setID(Integer.parseInt(cursor.getString(0)));
//                receipt.setFile(cursor.getString(1));
//                receipt.setLocationID(Integer.parseInt(cursor.getString(2)));
//                
//                receipt.setDate(cursor.getString(3));
//                receipt.setAmount(Integer.parseInt(cursor.getString(4)));
//                receipt.setNotes(cursor.getString(5));
//                // Adding receipt to list
//                receiptList.add(receipt);
//            } while (cursor.moveToNext());
//        }
// 
//        // return receipt list
//        cursor.close();
//        
//        return receiptList;
//    }
//    
//
// 
//    // Updating single receipt
//    public int updateReceipt(Receipt receipt) {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//        values.put(Receipt.KEY_ID, receipt.getID());
//        values.put(Receipt.KEY_RECEIPTFILE, receipt.getFile());
//        values.put(Receipt.KEY_LOCATION_IDLOCATION,receipt.getLocationID());
//        values.put(Receipt.KEY_RECEIPTDATE,receipt.getDate());
//        values.put(Receipt.KEY_RECEIPTAMOUNT,receipt.getAmount());
//        values.put(Receipt.KEY_RECEIPTNOTES,receipt.getNotes());
// 
//        // updating row
//        int rc = db.update(Receipt.TABLE_NAME, values, Receipt.KEY_ID + " = ?",
//                new String[] { String.valueOf(receipt.getID()) });
//        
//        return rc;
//    }
// 
//    // Deleting single receipt
//    public void deleteReceipt(Receipt receipt) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        
//        deleteReceiptImage(receipt.getFile());
//        
//        db.delete(Receipt.TABLE_NAME, Receipt.KEY_ID + " = ?",
//                new String[] { String.valueOf(receipt.getID()) });
//        
//    }
// 
// 
//    // Getting receipts Count
//    public int getReceiptsCount() {
//        String countQuery = "SELECT  * FROM " + Receipt.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
// 
//        // return count
//        int count = cursor.getCount();
//        
//        return count;
//    }
// 
//    
//    // Adding new location
//    void addLocation(Location location) throws Exception {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//        
//        values.put(Location.KEY_ID,location.getID());
//        values.put(Location.KEY_LOCATIONDESCRIPTION, location.getLocationDescription()); // Location description
// 
//        // Inserting Row
//        db.insertOrThrow(Location.TABLE_NAME, null, values);
//         // Closing database connection
//    }
//
//    // Getting single location
//    Location getLocationById(int id) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
// 
//        Cursor cursor = db.query(Location.TABLE_NAME, new String[] { Location.KEY_ID, Location.KEY_LOCATIONDESCRIPTION }, Location.KEY_ID + "=?",
//                new String[] { Integer.toString(id) }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//
//	        Location location = new Location(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
//	        // return location
//	        cursor.close();
//	        
//	        return location;
//	    }
//	    else
//	    {
//	    	cursor.close();
//	    	
//	    	throw new java.lang.Exception("Cannot find location matching id "+id);
//	    }
//    }
//    // Getting single location
//    Location getLocation(String name) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
// 
//        Cursor cursor = db.query(Location.TABLE_NAME, new String[] { Location.KEY_ID, Location.KEY_LOCATIONDESCRIPTION }, Location.KEY_LOCATIONDESCRIPTION + "=?",
//                new String[] { name }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//
//	        Location location = new Location(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
//	        // return location
//	        cursor.close();
//	        
//	        return location;
//	    }
//	    else
//	    {
//
//	    	cursor.close();
//	    	
//	    	throw new java.lang.Exception("Cannot find location matching "+ name);
//	    }
//    }
//     
//    // Getting All Locations
//    public List<DatabaseObject> getAllLocations() {
//        List<DatabaseObject> locationList = new ArrayList<DatabaseObject>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Location.TABLE_NAME;
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Location location = new Location();
//                
//                location.setID(Integer.parseInt(cursor.getString(0)));
//                location.setLocationDescription(cursor.getString(1));
//               
//                // Adding location to list
//                locationList.add(location);
//            } while (cursor.moveToNext());
//        }
// 
//        // return location list
//        
//        return locationList;
//    }
// 
//    // Updating single location
//    public int updateLocation(Location location) {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//        values.put(Location.KEY_ID, location.getID());
//        values.put(Location.KEY_LOCATIONDESCRIPTION, location.getLocationDescription());
// 
//        // updating row
//        int rc = db.update(Location.TABLE_NAME, values, Location.KEY_ID + " = ?",
//                new String[] { String.valueOf(location.getID()) });
//        
//        return rc;
//    }
// 
//    // Deleting single location
//    public void deleteLocation(Location location) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Location.TABLE_NAME, Location.KEY_ID + " = ?",
//                new String[] { String.valueOf(location.getID()) });
//        
//    }
// 
// 
//    // Getting locations Count
//    public int getLocationsCount() {
//        String countQuery = "SELECT  * FROM " + Location.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
// 
//        // return count
//        int count = cursor.getCount();
//        
//        return count;
//    }
//
//    /**
//     * All CRUD(Create, Read, Update, Delete) Operations
//     */
// 
//    // Adding new work
//    void addWork(Work work) throws Exception {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//       
//        values.put(Work.KEY_ID,work.getID());
//        values.put(Work.KEY_VEHICLE_IDVEHICLE, work.getVehicleID()); // Work description
//        values.put(Work.KEY_ITEMS_IDITEMS, work.getItemID()); // Work description
//        values.put(Work.KEY_RECEIPT_IDRECEIPT, work.getReceiptID()); // Work description
//        values.put(Work.KEY_WORKMILEAGE, work.getMileage());
//        values.put(Work.KEY_WORKNOTES, work.getNotes()); // Work description
//        
//        // Inserting Row
//        db.insertOrThrow(Work.TABLE_NAME, null, values);
//         // Closing database connection
//    }
// 
// 
//    // Getting single work
//    Work getWorkById(int id) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
//        
// 
//        Cursor cursor = db.query(Work.TABLE_NAME, new String[] { 
//        					Work.KEY_ID, 
//        					Work.KEY_VEHICLE_IDVEHICLE,
//        					Work.KEY_ITEMS_IDITEMS,
//        					Work.KEY_RECEIPT_IDRECEIPT,
//        					Work.KEY_WORKMILEAGE,
//							Work.KEY_WORKNOTES  
//							}, Work.KEY_ID + "=?",
//                new String[] { Integer.toString(id) }, null, null, null, null);
//        if (cursor.getCount() > 0 )
//        {
//        	
//            cursor.moveToFirst();
//
//	        Work work = new Work(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),cursor.getString(5));
//	        cursor.close();
//	        
//	        // return work
//	        return work;
//	        
//	    }
//	    else
//	    {
//	    	cursor.close();
//	    	
//	    	throw new java.lang.Exception("Cannot find work matching id "+id);
//	    }
//    }
//    
//    Work getWorkByVehicleItemReceipt(int vid,int iid, int rid) throws Exception {
//        SQLiteDatabase db = this.getReadableDatabase();
//        
// 
//        Cursor cursor = db.query(Work.TABLE_NAME, new String[] { 
//        					Work.KEY_ID, 
//        					Work.KEY_VEHICLE_IDVEHICLE,
//        					Work.KEY_ITEMS_IDITEMS,
//        					Work.KEY_RECEIPT_IDRECEIPT,
//        					Work.KEY_WORKMILEAGE,
//							Work.KEY_WORKNOTES  
//							}, 
//							Work.KEY_VEHICLE_IDVEHICLE + "=? and "+Work.KEY_ITEMS_IDITEMS+"=? and "+Work.KEY_RECEIPT_IDRECEIPT+"=?",
//                new String[] { Integer.toString(vid), Integer.toString(iid), Integer.toString(rid) }, null, null, null, null);
//        if (cursor.getCount() > 0)
//        {
//            cursor.moveToFirst();
//	
//	        Work work = new Work(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),cursor.getString(5));
//	        // return work
//	        cursor.close();
//	        
//	        return work;
//	    }
//	    else
//	    {
//	    	
//	    	cursor.close();
//	    	
//	    	throw new java.lang.Exception("Cannot find work for vehicleID="+vid+" itemID="+iid+" receiptID="+rid);
//	    }
//    }
//     
//    // Getting All Works
//    public List<DatabaseObject> getAllWorks() {
//        List<DatabaseObject> workList = new ArrayList<DatabaseObject>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Work.TABLE_NAME;
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Work work = new Work();
//                
//                work.setID(Integer.parseInt(cursor.getString(0)));
//                work.setVehicleID(Integer.parseInt(cursor.getString(1)));
//                work.setItemID(Integer.parseInt(cursor.getString(2)));
//                work.setReceiptID(Integer.parseInt(cursor.getString(3)));
//                work.setMileage(Integer.parseInt(cursor.getString(4)));
//                work.setNotes(cursor.getString(5));
//                // Adding work to list
//                workList.add(work);
//            } while (cursor.moveToNext());
//        }
// 
//        // return work list
//        cursor.close();
//        
//        return workList;
//    }
// 
//    // Getting All Works
//    public List<DatabaseObject> getAllWorksByVehicleId(int vid) {
//        List<DatabaseObject> workList = new ArrayList<DatabaseObject>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Work.TABLE_NAME + " where "+Work.KEY_VEHICLE_IDVEHICLE+"="+Integer.toString(vid);
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
// 
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Work work = new Work();
//                
//                work.setID(Integer.parseInt(cursor.getString(0)));
//                work.setVehicleID(Integer.parseInt(cursor.getString(1)));
//                work.setItemID(Integer.parseInt(cursor.getString(2)));
//                work.setReceiptID(Integer.parseInt(cursor.getString(3)));
//                work.setMileage(Integer.parseInt(cursor.getString(4)));
//                work.setNotes(cursor.getString(5));
//                // Adding work to list
//                workList.add(work);
//            } while (cursor.moveToNext());
//        }
// 
//        // return work list
//        cursor.close();
//        
//        return workList;
//    }
//    
//    // Updating single work
//    public int updateWork(Work work) {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//        values.put(Work.KEY_ID, work.getID());
//        values.put(Work.KEY_VEHICLE_IDVEHICLE, work.getVehicleID());
//        values.put(Work.KEY_ITEMS_IDITEMS, work.getItemID());
//        values.put(Work.KEY_RECEIPT_IDRECEIPT,work.getReceiptID());
//        values.put(Work.KEY_WORKMILEAGE,work.getMileage());
//        values.put(Work.KEY_WORKNOTES,work.getNotes());
// 
//        // updating row
//        int rc =  db.update(Work.TABLE_NAME, values, Work.KEY_ID + " = ?",
//                new String[] { String.valueOf(work.getID()) });
//        
//        return rc;
//    }
// 
//    // Deleting single work
//    public void deleteWork(Work work) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Work.TABLE_NAME, Work.KEY_ID + " = ?",
//                new String[] { String.valueOf(work.getID()) });
//        
//    }
// 
// 
//    // Getting works Count
//    public int getWorksCount() {
//        String countQuery = "SELECT  * FROM " + Work.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//        
// 
//        // return count
//        return cursor.getCount();
//    }
    
    public void deleteAllRecordsFromTable(String table_name) {
    	
//    	if (table_name.equals(Receipt.TABLE_NAME))
//    	{
//    		deleteAllReceiptImages();
//    	}
//    	
//    	try {
//	        SQLiteDatabase db = this.getWritableDatabase();
//	        db.delete(table_name, null, null);
//	        
//    	} catch (NullPointerException npe)
//    	{
//    		
//    	}
    	
    }
    
    private void deleteAllReceiptImages() 
    {
//    	List<DatabaseObject> receiptList = getAllReceipts();
//    	for (int index = 0; index < receiptList.size(); index++)
//    	{
//    		deleteReceiptImage(((Receipt)receiptList.get(index)).getFile());
//    	}
    }
    
    /// http://www.mkyong.com/java/how-to-delete-file-in-java/
    private void deleteReceiptImage(String filename)
    {
//		try 
//		{
//			File file = new File(filename);
//			file.delete();
//		}
//		catch (Exception e)
//		{
//			System.err.println("Could not delete "+filename);
//			System.err.println(e.getMessage());
//		}
    }
	
    
}
