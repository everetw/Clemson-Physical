package com.example.clemsonphysical;

import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class MainActivity extends DisplayTableActivity {
	
	private enum field_order  
		{
			KEY_EXERCISE_ID,
			KEY_EXERCISE_NAME,
			KEY_EXERCISE_VIDEO_URL,
			KEY_EXERCISE_INSTRUCTION_URL,
			KEY_EXERCISE_FILE_LOCATION,
			NEW_ROW;
		};

	private static final int FONT_SIZE = 30;
	
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	
	private void createData()
	{
		deleteAllRecordsFromTable(Exercise.TABLE_NAME);
	    
	    
	    
	    try
	    {
			addToDatabase(new Exercise(0,"Exercise 1","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 2","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 3","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 4","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 5","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 6","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 7","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 8","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 9","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 10","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 11","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 12","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 13","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 14","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 15","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 16","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 17","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 18","url","instructions","location"));
			addToDatabase(new Exercise(0,"Exercise 19","url","instructions","location"));

	    }
	    catch (Exception e)
	    {
	    
	    }
	    drawTable();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.xml.settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            break;
 
        case R.layout.activity_info_view:
            Intent intent = new Intent(this, InfoView.class);
            startActivity(intent);
            break;
            
        case R.id.create_data:
            createData();
            break;
        
        }
        
 
        return true;
    }
	
	public void showExercise(View view){
		Intent intent = new Intent(this, ExerciseView.class);
		startActivity(intent);
	}

	
	public void showPractitionerVideo(View view){
		Intent intent = new Intent(this, PractitionerVideoView.class);
		startActivity(intent);
	}

	@Override
	protected String getTableName() {
		
		return Exercise.TABLE_NAME;
	}


	@Override
	protected void drawTable() {
		
		
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
    	
    	
        TextView textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_ID.getKeyLabel(), FONT_SIZE,LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
    	textView.setVisibility(View.GONE);
    	tableRow.addView(textView);
        

    	
        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_NAME.getKeyLabel(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
        LayoutParams trlp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1f);
        trlp.setMargins(2, 2, 2, 2);
        textView.setLayoutParams(trlp);
        
        tableRow.addView(textView);
        //TODO Add on click listener

        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_VIDEO_URL.getKeyLabel(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
        tableRow.addView(textView);
        textView.setVisibility(View.GONE);
        
        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_INSTRUCTION_URL.getKeyLabel(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
        tableRow.addView(textView);
        textView.setVisibility(View.GONE);
        
        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_FILE_LOCATION.getKeyLabel(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
        tableRow.addView(textView);
        textView.setVisibility(View.GONE);
        
        // New Row Indicator = Must be last 
        textView = LayoutUtils.createTextView(this, "New Row", FONT_SIZE, LayoutUtils.DARK_GRAY,LayoutUtils.LIGHT_GRAY);
        textView.setVisibility(View.GONE);
        tableRow.addView(textView);

        
        tableLayout.addView(tableRow);
		


        
        //LinkedList<DatabaseObject>();
        //rowList.add(tableRow);

        List<DatabaseObject> exerciselist = null;
        

        // TODO Get exercises from plan, if set in preferences.
        exerciselist = Exercise.getAll(dbSQLite);
   
        
        for (int i = 0; i < exerciselist.size(); i++){
        	
            // First row: Entered data
        	tableRow = LayoutUtils.createTableRow(this);
        	
        	
        	Exercise exercise = (Exercise) exerciselist.get(i);
        	
        	textView = LayoutUtils.createTextView(this, Integer.toString(exercise.getId()), FONT_SIZE, LayoutUtils.DARK_GRAY,LayoutUtils.LIGHT_GRAY);
        	textView.setVisibility(View.GONE);
        	tableRow.addView(textView);
            
        	
            textView = LayoutUtils.createTextView(this, exercise.getName(), FONT_SIZE, LayoutUtils.DARK_GRAY,LayoutUtils.LIGHT_GRAY);
            trlp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1f);
            trlp.setMargins(2, 2, 2, 2);
            textView.setLayoutParams(trlp);
            
            textView.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				
    				
    				ViewGroup vp = (ViewGroup) v.getParent();
					vp.performClick();
    				displayToast(((TextView)v).getText().toString() + " clicked!");
    				onExerciseFieldClick(v);
    				// Start the intent.
    				// Send to the exercise screen.


    			}
    		});
            
            tableRow.addView(textView);
            
            textView = LayoutUtils.createTextView(this, exercise.getVideoUrl(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
            tableRow.addView(textView);
            textView.setVisibility(View.GONE);
            
            textView = LayoutUtils.createTextView(this, exercise.getInstructionUrl(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
            tableRow.addView(textView);
            textView.setVisibility(View.GONE);
            
            textView = LayoutUtils.createTextView(this, exercise.getFileLocation(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
            tableRow.addView(textView);
            textView.setVisibility(View.GONE);
    
	        // New Row Indicator = Must be last 
	        textView = LayoutUtils.createTextView(this, "false", FONT_SIZE, LayoutUtils.DARK_GRAY,LayoutUtils.LIGHT_GRAY);
	        textView.setVisibility(View.GONE);
	        tableRow.addView(textView);

	        tableLayout.addView(tableRow);
	        

        
        }
        
		
	}
	
	private Exercise getExerciseFromTableRow(TableRow tr)
	{
		// ID - child 0
		int  id =  Integer.parseInt(((TextView)tr.getChildAt(field_order.KEY_EXERCISE_ID.ordinal())).getText().toString());
		
		// Exercise Name
		String name = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_NAME.ordinal())).getText().toString();
		
		// Video Url
		String video_url = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_VIDEO_URL.ordinal())).getText().toString();
		
		// Instruction URL
		String instruction_url = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_INSTRUCTION_URL.ordinal())).getText().toString();
		
		// File location
		String file_location = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_FILE_LOCATION.ordinal())).getText().toString();

		
		return new Exercise(id,name,video_url,instruction_url,file_location);
	
	}


	private void onExerciseFieldClick(View v) 
	{
		Intent intent = new Intent(this,ExerciseView.class);
		
    	// Get the table row
    	TableRow tr = (TableRow)v.getParent();
    	
    	Exercise exercise = getExerciseFromTableRow(tr);
    	//String keys = LayoutUtils.getKeysFromTableRow(tr);
    	
    	/// http://stackoverflow.com/questions/2736389/how-to-pass-object-from-one-activity-to-another-in-android
    	intent.putExtra("ExerciseClass", exercise);
    	 
    	
		startActivity(intent);
	}



}
