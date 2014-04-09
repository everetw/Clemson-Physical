package edu.clemson.physicaltherapy.app;

import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.DatabaseObject;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;

public class LogView extends UpdateTableActivity implements AdapterView.OnItemSelectedListener {
	
	private static int FONT_SIZE = 15;
	
	private enum field_order  
	{
		VIEW,
		KEY_IDEXERCISE_LOG,
		KEY_EXERCISE_IDEXERCISE,
		KEY_CREATE_TIME,
		COMPARE,
		KEY_EXERCISE_NAME,
		
		KEY_EXERCISE_LOG_VIDEO_LOCATION,
		KEY_EXERCISE_LOG_VIDEO_NOTES,
		SAVE,
		DELETE,
		
		NEW_ROW;
	};

	private static final int NONE_SELECTED = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_view);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		
		// get the default font size
		/// http://stackoverflow.com/questions/6263250/convert-pixels-to-sp
		
		TextView testTextView = (TextView) findViewById(R.id.dummyTextView);
		float textSize = testTextView.getTextSize();
		FONT_SIZE = LayoutUtils.pixelsToSp(this, textSize);
		
		// This screen is a table view with a spinner to select the exercise. 
		
		// Get the exercise from the intent.
		Intent intent = getIntent();
		Exercise exercise = (Exercise) intent.getSerializableExtra("ExerciseClass");
		//System.err.println(exercise.getName());
		
		Spinner spinner = (Spinner) findViewById(R.id.exerciseSpinner);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		// There has GOT to be a better way to do this, but this works. 
		
		
		int selected = NONE_SELECTED;
		int selected_id = NONE_SELECTED;
		
        List<DatabaseObject> exerciseList = Exercise.getAll(dbSQLite);
        
        String [] spinnerList = new String[exerciseList.size()+1];
        spinnerList[0] = "All Exercises";
        
        for (int i = 0; i < exerciseList.size(); i++)
        {
        	spinnerList[i+1] = ((Exercise)exerciseList.get(i)).getName();
        	try {
	        	if (((Exercise)exerciseList.get(i)).getName().equals(exercise.getName()))
	        	{
	        		selected = i+1;
	        		selected_id = exercise.getId();
	        		
	        	}
        	}
        	catch (NullPointerException npe)
        	{
        		
        	}
        }
        
        // Create spinner from array http://stackoverflow.com/questions/2784081/android-create-spinner-programmatically-from-array
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinnerList);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		//System.err.println("Selection is "+selected);
		if (selected > NONE_SELECTED)
		{
			spinner.setSelection(selected);
			
		}
		TextView htv = (TextView) findViewById(R.id.exerciseIdTextView);
		htv.setText(Integer.toString(selected_id));
		drawTable();
	}
	

	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		
		// when an item is selected, put the id in a hidden text view. 
		
		TextView htv = (TextView) findViewById(R.id.exerciseIdTextView);
		Exercise exercise = null;
		try 
		{

			exercise = Exercise.getByName(dbSQLite,((TextView)v).getText().toString());
			htv.setText(Integer.toString(exercise.getId()));
		}
		catch (Exception e)
		{
			htv.setText(Integer.toString(NONE_SELECTED));
			
		}
		
		drawTable();
		
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// when an item is selected, set id to -1 
		TextView htv = (TextView) findViewById(R.id.exerciseIdTextView);
		htv.setText(Integer.toString(NONE_SELECTED));
		drawTable();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_view, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
 	   
 		case android.R.id.home:

 			NavUtils.navigateUpFromSameTask(this);
 			return true; 
 			
 
        case R.xml.settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            break;
 
        case R.layout.activity_info_view:
            Intent intent = new Intent(this, InfoView.class);
            startActivity(intent);
            break;
        
        case R.id.action_share:
        	displayToast("Send logs to external service.");
        	break;
        }
 
        return super.onOptionsItemSelected(item);
    }

	public void showUserVideo(View view){
		
    	TableRow tr = (TableRow) view.getParent();
    	
    	//Receipt receipt = getReceiptFromTableRow(tr);
    	ExerciseLog exerciseLog = getExerciseLogFromTableRow(tr);
    	    	
		Intent intent = new Intent(this, UserVideoView.class);
		intent.putExtra("ExerciseLogClass", exerciseLog);
		
		startActivity(intent);
	}

	@Override
	protected String getTableName() {
		
		return ExerciseLog.TABLE_NAME;
	}

	@Override
	protected void drawTable() {
		
		// Is the spinner populated?
        TextView htv = (TextView) findViewById(R.id.exerciseIdTextView);
        int exercise_id = Integer.parseInt(htv.getText().toString());
		
        // Get the Table Layout 
	    TableLayout tableLayout = (TableLayout) findViewById(R.id.tlGridTable);
	    //TODO: Next 3 lines in XML
	    tableLayout.setBaselineAligned(false);
	    tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_BEGINNING | TableLayout.SHOW_DIVIDER_END | TableLayout.SHOW_DIVIDER_MIDDLE);
	    tableLayout.setDividerPadding(2);
	    tableLayout.removeAllViews();
	    tableLayout.setWeightSum(1f);
	    tableLayout.setStretchAllColumns(true);
	   
	     // Create the labels
    	TableRow tableRow = LayoutUtils.createTableRow(this);
    	tableRow.setId(0);
    	
    	TextView textView = LayoutUtils.createTextView(this, "View", FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
        tableRow.addView(textView);
    	
        textView = LayoutUtils.createTextView(this, ExerciseLog.DbKeys.KEY_ID.getKeyLabel(), FONT_SIZE,LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
    	textView.setVisibility(View.GONE);
    	tableRow.addView(textView);
        
        textView = LayoutUtils.createTextView(this, ExerciseLog.DbKeys.KEY_EXERCISE_IDEXERCISE.getKeyLabel(), FONT_SIZE,LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
        textView.setVisibility(View.GONE);
    	tableRow.addView(textView);
    	
        textView = LayoutUtils.createTextView(this, ExerciseLog.DbKeys.KEY_CREATE_TIME.getKeyLabel(), FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
        tableRow.addView(textView);
    	
        textView = LayoutUtils.createTextView(this, "Compare",  FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
        tableRow.addView(textView);
        
        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_NAME.getKeyLabel(), FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
    	if (exercise_id == NONE_SELECTED)
        {
            textView.setVisibility(View.VISIBLE);
        }
        else
    	{
    		textView.setVisibility(View.GONE);	
    	}
    	
    	tableRow.addView(textView);
    	

        

        textView = LayoutUtils.createTextView(this, ExerciseLog.DbKeys.KEY_EXERCISE_LOG_VIDEO_LOCATION.getKeyLabel(), FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
        tableRow.addView(textView);
        textView.setVisibility(View.GONE);
        
        
        textView = LayoutUtils.createTextView(this, ExerciseLog.DbKeys.KEY_EXERCISE_LOG_VIDEO_NOTES.getKeyLabel(), FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
        tableRow.addView(textView);
        
        textView = LayoutUtils.createTextView(this, "Save",   FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
	    if (autoSave)
        {
        	textView.setVisibility(View.GONE);
        }
        else
        {
        	textView.setVisibility(View.VISIBLE);
        }
        tableRow.addView(textView);

	

        
        textView = LayoutUtils.createTextView(this, "Delete",  FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
        tableRow.addView(textView);
        
        // New Row Indicator = Must be last 
        textView = LayoutUtils.createTextView(this, "New Row", FONT_SIZE, LayoutUtils.TEXT_COLOR,LayoutUtils.BACKGROUND_COLOR);
        textView.setVisibility(View.GONE);
        tableRow.addView(textView);

        
        tableLayout.addView(tableRow);
		


        
        //LinkedList<DatabaseObject>();
        //rowList.add(tableRow);

        List<DatabaseObject> exerciseLoglist = null;
        


        if (exercise_id == -1)
        {
        	exerciseLoglist = ExerciseLog.getAll(dbSQLite);
        }
        else
        {
        	exerciseLoglist = ExerciseLog.getAllByExerciseId(dbSQLite,exercise_id);
        }
        
   
        
        for (int i = 0; i < exerciseLoglist.size(); i++){
        	
            // First row: Entered data
        	tableRow = LayoutUtils.createTableRow(this);
        	
        	
        	
        	ExerciseLog exerciseLog = (ExerciseLog) exerciseLoglist.get(i);

			
	        ImageButton button; 
	        
	        
	    	button = new ImageButton(this);
	    	button.setImageResource(android.R.drawable.ic_media_play);
	    	button.setOnClickListener(new View.OnClickListener(){
		    	public void onClick(View v){
		    		showUserVideo(v);
		    	}

	    	});
	    	tableRow.addView(button);
        	textView = LayoutUtils.createTextView(this, Integer.toString(exerciseLog.getId()), FONT_SIZE, LayoutUtils.TEXT_COLOR,LayoutUtils.BACKGROUND_COLOR);
        	textView.setVisibility(View.GONE);
        	tableRow.addView(textView);
            
        	textView = LayoutUtils.createTextView(this, Integer.toString(exerciseLog.getExerciseId()), FONT_SIZE, LayoutUtils.TEXT_COLOR,LayoutUtils.BACKGROUND_COLOR);
        	textView.setVisibility(View.GONE);
        	
            // Add listener for exercise name. Will take to exercise view screen.
//          textView.setOnClickListener(new View.OnClickListener() {
//  			
//  			@Override
//  			public void onClick(View v) {
//  				
//  				
//  				ViewGroup vp = (ViewGroup) v.getParent();
//					vp.performClick();
//  				displayToast(((TextView)v).getText().toString() + " clicked!");
//  				onExerciseLogFieldClick(v);
//  				// Start the intent.
//  				// Send to the exerciseLog screen.
//
//
//  			}
//  		});
        	
        	tableRow.addView(textView);
        	
            textView = LayoutUtils.createTextView(this, exerciseLog.getCreateTime(), FONT_SIZE, LayoutUtils.TEXT_COLOR, LayoutUtils.BACKGROUND_COLOR );
            tableRow.addView(textView);
            
	    	button = new ImageButton(this);
	    	button.setImageResource(R.drawable.ic_action_compare);
	    	button.setOnClickListener(new View.OnClickListener(){
		    	public void onClick(View v){
		    		showVideoComparison(v);
		    	}

	    	});
	    	tableRow.addView(button);
            
            
            
        	if (exercise_id == NONE_SELECTED)
        	{
	        	Exercise exercise = null;
				try {
					exercise = (Exercise) Exercise.getById(dbSQLite, exerciseLog.getExerciseId());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.err.println("Exercise not found for id="+exerciseLog.getExerciseId()+". Your database may be corrupt!");
					e1.printStackTrace();
					exercise = new Exercise();
				}
	            textView = LayoutUtils.createTextView(this, exercise.getName(), FONT_SIZE, LayoutUtils.TEXT_COLOR,LayoutUtils.BACKGROUND_COLOR);
	        
        	}
        	else
        	{
        		Spinner spinner = (Spinner) findViewById(R.id.exerciseSpinner);
        		textView = LayoutUtils.createTextView(this, spinner.getSelectedItem().toString(), FONT_SIZE, LayoutUtils.TEXT_COLOR,LayoutUtils.BACKGROUND_COLOR);
        		textView.setVisibility(View.GONE);
        	}

        	tableRow.addView(textView);
	        

            

            

            
            textView = LayoutUtils.createTextView(this, exerciseLog.getVideoLocation(), FONT_SIZE,LayoutUtils.TEXT_COLOR, LayoutUtils.BACKGROUND_COLOR );
            tableRow.addView(textView);
            textView.setVisibility(View.GONE);
            
            
            EditText editText = LayoutUtils.createEditText(this, exerciseLog.getVideoNotes(), FONT_SIZE, LayoutUtils.TEXT_COLOR, LayoutUtils.WHITE);
            tableRow.addView(editText);
            
            
            // Add the save button.
            addSaveFunctionToRow(tableRow);
            


	    	

	        
	        button = new ImageButton(this);
	        button.setImageResource(android.R.drawable.ic_menu_delete);
	        
	        button.setOnClickListener(new View.OnClickListener(){
	            public void onClick(View v){
	                 // Do some operation for minus after getting v.getId() to get the current row
	            	// http://stackoverflow.com/questions/14112044/android-how-to-get-the-id-of-a-parent-view
	            	 //save button.
	            	TableRow tr = (TableRow)v.getParent();
	            	
	            	//Receipt receipt = getReceiptFromTableRow(tr);
	            	ExerciseLog exerciseLog = getExerciseLogFromTableRow(tr);
	            	String keys = LayoutUtils.getKeysFromTableRow(tr);
	            	try 
	            	{
	            		exerciseLog.delete(dbSQLite);	
	            	}
	            	catch (Exception e)
	            	{
	            		displayMessageDialog(e.getMessage(),e.toString());
	            	}
	            	
	            	
	            	//send click through to parent.
	            	tr.performClick();
	            	

	            	TableLayout tl = (TableLayout)tr.getParent();
	            	tl.removeView(tr);
	            	
	            	
	            }
	        
	            
	        });
	        
	        tableRow.addView(button);
	        // New Row Indicator = Must be last 
	        textView = LayoutUtils.createTextView(this, "false", FONT_SIZE, LayoutUtils.TEXT_COLOR,LayoutUtils.BACKGROUND_COLOR);
	        textView.setVisibility(View.GONE);
	        tableRow.addView(textView);

	        
	        
	        
	        tableLayout.addView(tableRow);
	        

        
        }
        
	}



	protected void showVideoComparison(View view) {
    	TableRow tr = (TableRow) view.getParent();
    	
    	//Receipt receipt = getReceiptFromTableRow(tr);
    	ExerciseLog exerciseLog = getExerciseLogFromTableRow(tr);
    	    	
		Intent intent = new Intent(this, CompareView.class);
		intent.putExtra("ExerciseLogClass", exerciseLog);
		
		startActivity(intent);
		
		
	}


	protected ExerciseLog getExerciseLogFromTableRow(TableRow tr) {
		{
			int  id =  Integer.parseInt(((TextView)tr.getChildAt(field_order.KEY_IDEXERCISE_LOG.ordinal())).getText().toString());
			int  exercise_id =  Integer.parseInt(((TextView)tr.getChildAt(field_order.KEY_EXERCISE_IDEXERCISE.ordinal())).getText().toString());
			String create_time = ((TextView)tr.getChildAt(field_order.KEY_CREATE_TIME.ordinal())).getText().toString();
			String video_location = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_LOG_VIDEO_LOCATION.ordinal())).getText().toString();
			String video_notes = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_LOG_VIDEO_LOCATION.ordinal())).getText().toString();
			
			return new ExerciseLog(id,exercise_id,video_location,video_notes,create_time);
		
		}
	}

	@Override
	protected void saveAllRows() {

		// Not applicable.
	}

	@Override
	protected void addNewRow() {
		// Not applicable.
		
	}

	@Override
	protected void updateRow(TableRow tr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveNewRow(TableRow tr) {
		// Not applicable.
		
	}
	
	protected String getVideoSubdirectory()
	{
		return "videos";
	}
	

	
}
