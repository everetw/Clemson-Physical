/**
 * 
 */
package com.example.clemsonphysical;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jburton
 * @class LayoutUtils
 * 
 * @brief A series of static methods that create widgets for a table layout.
 *
 */
@SuppressLint("NewApi")
public class LayoutUtils 
{
	
	private static boolean dialogResult = false;
	private static String dateString = null;
	public static final int DARK_GRAY = Color.rgb(51, 51, 51);
	public static final int WHITE = Color.rgb(255, 255, 255);
	public static final int LIGHT_GRAY = Color.rgb(200, 200, 200);
	public static final int BLUE = Color.rgb(0, 0, 255);
	public static final int LIGHT_BLUE = Color.rgb(200, 200, 255);
	public static final int GREEN = Color.rgb(0, 255, 0);
	public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
	public static final int RED = Color.rgb(255, 0, 0);
	public static final int LIGHT_RED = Color.rgb(255, 200, 200);
	public static final int MAGENTA = Color.rgb(255, 0, 255);
	public static final int LIGHT_MAGENTA = Color.rgb(255, 200, 255);
	public static final int YELLOW = Color.rgb(255, 255, 0);
	public static final int LIGHT_YELLOW = Color.rgb(255, 255, 200);
	public static final int CYAN = Color.rgb(0, 255, 255);
	public static final int LIGHT_CYAN = Color.rgb(200, 255, 255);
	
	public static int TEXT_COLOR=DARK_GRAY;
	public static int BACKGROUND_COLOR=LIGHT_GRAY;
	public static int HIGHLIGHT_COLOR = BLUE;
	public static int SPINNER_COLOR = LIGHT_BLUE;
	public static final int OPTION_BLUE = 0;
	public static final int OPTION_RED = 2;
	public static final int OPTION_GREEN = 1;
	
	public static final int OPTION_MAGENTA = 3;
	public static final int OPTION_YELLOW = 4;
	public static final int OPTION_CYAN = 5;
	
	public static final int SMALL_FONT = 10;
	public static final int MEDIUM_FONT = 15;
	public static final int LARGE_FONT = 25;
	
	
	/**
	 * @fn public static TableRow createTableRow(Context context)
	 * Creates a table row with no margins and no padding that will expand to the entire length of the parent TableLayout.
	 * @param context
	 * @return Created TableRow.
	 */
	public static TableRow createTableRow(final Context context)
	{
		TableRow tableRow = new TableRow(context);
		///http://stackoverflow.com/questions/2481455/set-margins-in-a-linearlayout-programmatically
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(2, 2, 2, 2);
		tableRow.setLayoutParams(layoutParams);
    	tableRow.setFocusable(true);
    	tableRow.setClickable(true);
    	tableRow.requestDisallowInterceptTouchEvent(false);
    	//tableRow.setBackgroundColor(DARK_GRAY);

    	tableRow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// get the table Row
				//TableRow tr = (TableRow)v;
				// get the table layout above
				

				ViewGroup tl = (ViewGroup)v.getParent();
				
				for (int index = 0; index < tl.getChildCount(); index++)
				{
					// unselect all children
					View unselected = tl.getChildAt(index);
					unselected.setSelected(false);
					unselected.setBackgroundColor(DARK_GRAY);
				}
				
				
				//displayToast(context,"Table row "+v.getId()+"clicked");
				v.setBackgroundColor(HIGHLIGHT_COLOR);
				v.setSelected(true);
				

				
			}
		} );


		
		return tableRow;
		
	}
	/**
	 * @fn public static View createHorizontalLine(Context context,int color)
	 * @brief Creates a horizontal line of height 1 that goes across the parent.
	 * @param context 
	 * @param color Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @return Created horizontal line View Object.
	 */
	public static View createHorizontalLine(Context context,int color)
	{
		/// Creating lines http://stackoverflow.com/questions/5092116/how-can-i-add-separating-lines-between-my-tablerows-that-are-created-programmati
		View h_line = new View(context);
        h_line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
        h_line.setBackgroundColor(color);
        h_line.setPadding(0, 0, 0, 0);
        return h_line;
	}
	/**
	 * @fn public static View createVerticalLine(Context context,int color)
	 * @brief Creates a vertical line of width 1 that goes across the parent.
	 * @param context 
	 * @param color Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @return Created vertical line View Object.
	 */
	
	public static View createVerticalLine(Context context,int color)
	{
		/// Creating lines http://stackoverflow.com/questions/5092116/how-can-i-add-separating-lines-between-my-tablerows-that-are-created-programmati
		View v_line = new View(context);
        v_line.setLayoutParams(new TableRow.LayoutParams(1,TableRow.LayoutParams.MATCH_PARENT));
        v_line.setBackgroundColor(color);
        v_line.setPadding(0, 0, 0, 0);
        return v_line;
	}
	/**
	 * @fn public static TextView createTextView(Context context,String message,int size,int textColor,int backgroundColor)
	 * @brief Creates a textview object.
	 * @param context 
	 * @param message Message to be displayed in the text view
	 * @param size Text size
	 * @param textColor Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @param backgroundColor Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @return Created TextView Object.
	 */
	
	public static TextView createTextView(Context context,String message,int size,int textColor,int backgroundColor)
	{
		/// http://stackoverflow.com/questions/11504635/layout-margin-for-text-view-programmatically
		TextView textView = new TextView(context);
		TableRow.LayoutParams tvlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(tvlp);
        tvlp.setMargins(2, 2, 2, 2);
        textView.setText(message);
        textView.setTextSize(size);
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(textColor);
        /// http://stackoverflow.com/questions/432037/how-do-i-center-text-horizontally-and-vertical-in-a-textview-in-android
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        


        
        return textView;
	}
	
	/**
	 * @fn public static TextView createDateTextView(Context context,String message,int size,int textColor,int backgroundColor)
	 * @brief Creates a textview object that displays a datepicker on click.
	 * @param context 
	 * @param message Message to be displayed in the text view
	 * @param size Text size
	 * @param textColor Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @param backgroundColor Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @return Created TextView Object.
	 */
	
	public static TextView createDateTextView(final Context context,String message,int size,int textColor,int backgroundColor)
	{
		/// http://stackoverflow.com/questions/11504635/layout-margin-for-text-view-programmatically
		TextView textView = new TextView(context);
		TableRow.LayoutParams tvlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(tvlp);
        tvlp.setMargins(2, 2, 2, 2);
        textView.setText(message);
        textView.setTextSize(size);
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(textColor);
        /// http://stackoverflow.com/questions/432037/how-do-i-center-text-horizontally-and-vertical-in-a-textview-in-android
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Use the current date as the default date in the picker
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				
				LayoutUtils.displayDatePickerDialog(context, v, year, month, day);


			}
		});


        
        return textView;
	}
	

	/**
	 * @fn public static EditText createEditText(Context context,String message,int size,int textColor,int backgroundColor)
	 * @brief Creates a textview object.
	 * @param context 
	 * @param message Message to be displayed in the text view
	 * @param size Text size
	 * @param textColor Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @param backgroundColor Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @return Created EditText Object.
	 */
	
	public static EditText createEditText(final Context context,String message,int size,int textColor,int backgroundColor)
	{
		/// http://stackoverflow.com/questions/11504635/layout-margin-for-text-view-programmatically
		EditText editText = new EditText(context);
		TableRow.LayoutParams tvlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(tvlp);
        tvlp.setMargins(2, 2, 2, 2);
        editText.setText(message);
        editText.setTextSize(size);
        editText.setBackgroundColor(backgroundColor);
        editText.setTextColor(textColor);
        /// http://stackoverflow.com/questions/432037/how-do-i-center-text-horizontally-and-vertical-in-a-textview-in-android
        editText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        

        
        //Send click event to the parent if the focus changed.
        /// http://stackoverflow.com/questions/8397609/onclicklistener-listens-only-on-the-second-time
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
	            {
	                
					
					ViewGroup vp = (ViewGroup) v.getParent();
					vp.performClick();
	            }
				
			}
		});

        
        
        return editText;
	}
	
	
	/**
	 * @fn public static Spinner createSpinner(Context context, String[] spinnerList,	int backgroundColor)
	 * @brief Creates a Spinner object with default background.
	 * @param context 
	 * @param spinnerlist list of possible values for the spinner
	 * @return Created Spinner Object.
	 */
	
	public static Spinner createSpinner(Context context, String[] spinnerList)
	{
		return createSpinner(context,spinnerList,SPINNER_COLOR);
	}
	
	/**
	 * @fn public static Spinner createSpinner(Context context, String[] spinnerList,	int backgroundColor)
	 * @brief Creates a Spinner object.
	 * @param context 
	 * @param spinnerlist list of possible values for the spinner
	 * @param backgroundColor Numerical representation of color. Use android.graphics.Color.rgb(red,green,blue)
	 * @return Created Spinner Object.
	 */
	
	
	public static Spinner createSpinner(Context context, String[] spinnerList,	int backgroundColor)
	{
		/// http://stackoverflow.com/questions/11504635/layout-margin-for-text-view-programmatically
		
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		Spinner spinner = new Spinner(context);

        
        // Create spinner from array http://stackoverflow.com/questions/2784081/android-create-spinner-programmatically-from-array
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,spinnerList);

		
		
		TableRow.LayoutParams tvlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        spinner.setLayoutParams(tvlp);
        tvlp.setMargins(2, 2, 2, 2);
        spinner.setPadding(0, 0, 0, 0);
        //spinner.setText(message);
        //spinner.setTextSize(size);
        spinner.setBackgroundColor(backgroundColor);
        

    	// Specify the layout to use when the list of choices appears
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	spinner.setAdapter(adapter);
    	//spinner.setOnItemSelectedListener(context);
    	
    	
        //spinner.setTextColor(textColor);
        /// http://stackoverflow.com/questions/432037/how-do-i-center-text-horizontally-and-vertical-in-a-textview-in-android
        spinner.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        


        
        return spinner;
	}
	/**
	 * @fn public static WebView createHtmlView(Context context,String html)
	 * @brief This creates a new WebView object to display the HTML code in string.
	 * @param context
	 * @param html HTML code to be displayed. This is a string of the code itself, not a link to a file.
	 * @return Created WebView object.
	 */

	public static WebView createHtmlView(Context context,String html)
	{
	    WebView webview = new WebView(context);
	    webview.loadData(html, "text/html", null);
	    webview.setPadding(5, 0, 5, 0);
	    return webview;
	}
	

    /**
     * @fn public void displayToast(String message)
     * @brief Displays a popup "Toast" message to the user.
     * Displaying toasts from http://developer.android.com/guide/topics/ui/notifiers/toasts.html
     * @param message Message to display
     */
	
	public static void displayToast(Context context, String message)
	{
		
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	   /**
     * @fn public void displayMessageDialog(String message, String title)
     * @brief Displays a message dialog to the user.
     * Displaying message dialogs from http://www.mkyong.com/android/android-alert-dialog-example/
     * @param message Message to display
     * @param title Title of dialog
     * 
     */
    
    public static void displayMessageDialog(Context context, String title, String message)
    {
    	

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set title
		if (title == null)
		{
			title = "Message";
		}
		alertDialogBuilder.setTitle(title);
 
		// set dialog message
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				  })
				;
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
    }
    
	public static String getKeysFromTableRow(TableRow tr)
	{
		String keys = "";
		for (int j = 0; j < tr.getChildCount(); j++)
		{
			try
			{
				TextView tv = (TextView)tr.getChildAt(j);
				keys += tv.getText().toString()+" | ";
			}
			catch (java.lang.ClassCastException c)
			{
				break;
			
			}
			
		}
		return keys;
	}
	
	public static void setDialogResult(boolean result) {
		dialogResult = result;
	}

	public static boolean getDialogResult() {
		return dialogResult;
	}

//
//		Don't use this. Copy, then add a callback function in the result.
//
//    public static void displayYesNoDialog(Context context, String message, String title)
//    {
//    	
//
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//		// set title
//		if (title == null)
//		{
//			title = "Message";
//		}
//		alertDialogBuilder.setTitle(title);
// 
//		// set dialog message
//		alertDialogBuilder
//				.setMessage(message)
//				.setCancelable(false)
//				.setNegativeButton("No",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, just close
//						// the dialog box and do nothing
//						dialog.cancel();
//						LayoutUtils.setDialogResult(false);
//						
//					}
//				  })
//				;
//		alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				LayoutUtils.setDialogResult(true);
//				
//			}
//					
//				});
// 
//				// create alert dialog
//				AlertDialog alertDialog = alertDialogBuilder.create();
// 
//				// show it
//				alertDialog.show();
//    }
//    
    public static void displayDatePickerDialog(final Context context, final View v, int year, int month, int day)
    {
    	
    	/// Date picker dialog = http://www.learn-android-easily.com/2013/06/datepicker-and-timepicker-dialog-in.html
    	String title = "Select Date";
    	
        // Register  DatePickerDialog listener
        DatePickerDialog.OnDateSetListener mDateSetListener =
                               new DatePickerDialog.OnDateSetListener() {
                           // the callback received when the user "sets" the Date in the DatePickerDialog
                                   public void onDateSet(DatePicker view, int yearSelected,
                                                         int monthOfYear, int dayOfMonth) {

                                      
	                      			//  LayoutUtils.displayToast(context, "Setting date");
	                    			  
	                      			// Set the Selected Date in Select date Button
	                      			  if (LayoutUtils.getDialogResult())
	                      			  {
	                                      LayoutUtils.setDate(yearSelected+"-"+Integer.toString(monthOfYear+1)+"-"+dayOfMonth);
		                      			  TextView tv = (TextView)v;
		                      			  tv.setText(LayoutUtils.getDate());
	                      			  }
	                    				
                                   }
                               };

    	DatePickerDialog dpd = new DatePickerDialog(context, mDateSetListener,year,month,day);

    	/// Cancel date picker dialog = http://stackoverflow.com/questions/11444238/jelly-bean-datepickerdialog-is-there-a-way-to-cancel
	    	dpd.setButton(DialogInterface.BUTTON_POSITIVE, "OK", 
    	        new DialogInterface.OnClickListener() {
    	            @Override
    	            public void onClick(DialogInterface dialog, int which) {

    	            	
    	            	LayoutUtils.setDialogResult(true);
    	            	dialog.cancel();
    	                
    	            }
    	        });
    	    dpd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", 
    	        new DialogInterface.OnClickListener() {
    	            @Override
    	            public void onClick(DialogInterface dialog, int which) {
    	            	LayoutUtils.setDialogResult(false);
    	                dialog.cancel();
    	            }
    	        });
    	
    	dpd.show();
    	
    }
    
    public static void setDate(String date)
    {
    	dateString = date;
    }
    
    public static String getDate()
    {
    	return dateString;
    }
    

    
    public static void setHighlightColor(int colorPreference)
    {
    	
    	switch (colorPreference)
    	{
	    	case OPTION_RED:
	    		HIGHLIGHT_COLOR = RED;
	    		SPINNER_COLOR=LIGHT_RED;
	    		break;
	    		
	    	case OPTION_GREEN:
	    		HIGHLIGHT_COLOR = GREEN;
	    		SPINNER_COLOR=LIGHT_GREEN;
	    		break;
	    		
	    	case OPTION_MAGENTA:
	    		HIGHLIGHT_COLOR = MAGENTA;
        		SPINNER_COLOR=LIGHT_MAGENTA;
        		break;

	    	case OPTION_YELLOW:
        		HIGHLIGHT_COLOR = YELLOW;
        		SPINNER_COLOR=LIGHT_YELLOW;
        		break;
        		
        	case OPTION_CYAN:
        		HIGHLIGHT_COLOR = CYAN;
        		SPINNER_COLOR=LIGHT_CYAN;
        		break;
        		
        	case OPTION_BLUE:
        			default:
            		HIGHLIGHT_COLOR = BLUE;
            		SPINNER_COLOR=LIGHT_BLUE;
            		break;
    	}
    	
    }
    
	public static void loadImage(final Context context, String uriString, ImageView imageView) 
	{
		try
		{
			Uri resource = Uri.parse(uriString);
			loadImage(context, resource, imageView);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadImage(final Context context, Uri resource,ImageView imageView) 
	{
		Drawable drawable;
		try {
		    InputStream inputStream = context.getContentResolver().openInputStream(resource);
		    drawable = Drawable.createFromStream(inputStream, resource.toString() );
		} catch (FileNotFoundException e) {
		    drawable = context.getResources().getDrawable(R.drawable.ic_launcher);
		}
		
		imageView.setBackgroundDrawable(drawable);

	}
	
	public static int getPixels(final Context context, int dip)
	{
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
	}
	
/// http://stackoverflow.com/questions/1224996/java-convert-string-to-html-string

	public static String stringToHTMLString(String string) {
	    StringBuffer sb = new StringBuffer(string.length());
	    // true if last char was blank
	    boolean lastWasBlankChar = false;
	    int len = string.length();
	    char c;
	
	    for (int i = 0; i < len; i++)
	        {
	        c = string.charAt(i);
	        if (c == ' ') {
	            // blank gets extra work,
	            // this solves the problem you get if you replace all
	            // blanks with &nbsp;, if you do that you loss 
	            // word breaking
	            if (lastWasBlankChar) {
	                lastWasBlankChar = false;
	                sb.append("&nbsp;");
	                }
	            else {
	                lastWasBlankChar = true;
	                sb.append(' ');
	                }
	            }
	        else {
	            lastWasBlankChar = false;
	            //
	            // HTML Special Chars
	            if (c == '"')
	                sb.append("&quot;");
	            else if (c == '&')
	                sb.append("&amp;");
	            else if (c == '<')
	                sb.append("&lt;");
	            else if (c == '>')
	                sb.append("&gt;");
	            else if (c == '\n')
	                // Handle Newline
	                sb.append("&lt;br/&gt;");
	            else {
	                int ci = 0xffff & c;
	                if (ci < 160 )
	                    // nothing special only 7 Bit
	                    sb.append(c);
	                else {
	                    // Not 7 Bit use the unicode system
	                    sb.append("&#");
	                    sb.append(new Integer(ci).toString());
	                    sb.append(';');
	                    }
	                }
	            }
	        }
	    return sb.toString();
	}


	
	
	
	
}
