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
	

    // Progress Dialog
    protected ProgressDialog pDialog = null;
    
	protected boolean autoSave = false;
	protected int colorTheme = LayoutUtils.HIGHLIGHT_COLOR;
	
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
	

	    Intent intent = new Intent(this, InfoView.class);
	    startActivity(intent);

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
	
	


}






