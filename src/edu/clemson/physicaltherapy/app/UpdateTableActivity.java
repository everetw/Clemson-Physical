package edu.clemson.physicaltherapy.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

public abstract class UpdateTableActivity extends DisplayTableActivity {
	
	protected void deleteAllRows()

	  {
	  		String title = "Delete All";
	  		String message = "Delete All Rows in "+getTableName()+" Table?";
	
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			// set title

			alertDialogBuilder.setTitle(title);
	
			// set dialog message
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
	
							
						}
					  })
					;
			alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					displayToast("Deleted all rows!");
					dbSQLite.deleteAllRecordsFromTable(getTableName());
					drawTable();
					
				}
						
					});
	
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	
					// show it
					alertDialog.show();
	  }
	
    
    protected abstract void saveAllRows();

    protected void saveAllRows(ViewGroup tl)
    {
    	
    	// skip first row of labels
		for (int index = 1; index < tl.getChildCount(); index++)
		{
			// unselect all children
			TableRow unselected;
			try 
			{
				unselected = (TableRow)tl.getChildAt(index);
			}
			catch (java.lang.ClassCastException cce)
			{
				continue;
			}

			// Check the new row flag at the end.
			String flag = ((TextView)unselected.getChildAt(unselected.getChildCount()-1)).getText().toString();

			if (Boolean.valueOf(flag))
			{
				saveNewRow(unselected);
			}
			else
			{
				updateRow(unselected);
			}
			
			unselected.setSelected(false);
			unselected.setBackgroundColor(LayoutUtils.DARK_GRAY);
		}
    	
    	
    }

    


    protected void addSaveFunctionToRow(TableRow tableRow)
    {
    	
    	// Create the save/update button.
    	ImageButton button;
   
	    
    	
        button= new ImageButton(this);
        button.setImageResource(android.R.drawable.ic_menu_save);
        button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v)
            {
            	// Get the table row
        		TableRow tr = (TableRow)v.getParent();
        		
				String flag = ((TextView)tr.getChildAt(tr.getChildCount()-1)).getText().toString();

				if (Boolean.valueOf(flag))
				{
					saveNewRow(tr);
				}
				else
				{
					updateRow(tr);
					tr.performClick();
				}

            	
            	
           }
        });
        
        /// if autosave is enabled, must hide the button and override the row listener. Otherwise, enable the button.
	    if (autoSave)
	    {
	    	button.setVisibility(View.GONE);
	    	button.setEnabled(false);
	    	tableRow.setOnClickListener(new View.OnClickListener() 
	    	{
				
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					
					// get the table Row
					//TableRow tr = (TableRow)v;
					// get the table layout above
					
					boolean update;
					
					// if the current row isn't selected, we're going to need to update.
					if (! v.isSelected())
					{
						//displayToast(context,"Row change!");
						update = true;
					}
					else
					{
						return;
					}
					
					ViewGroup tl = (ViewGroup)v.getParent();
					//skip the first row of lables
					for (int index = 1; index < tl.getChildCount(); index++)
					{
						// unselect all children
						TableRow unselected;
						try 
						{
							unselected = (TableRow)tl.getChildAt(index);
						}
						catch (java.lang.ClassCastException cce)
						{
							continue;
						}
						
						// if we need to update
						if ( unselected.isSelected())
						{
							// Check the new row flag at the end.
							String flag = ((TextView)unselected.getChildAt(unselected.getChildCount()-1)).getText().toString();

							if (Boolean.valueOf(flag))
							{
								saveNewRow(unselected);
							}
							else
							{
								updateRow(unselected);
							}
						}
						unselected.setSelected(false);
						unselected.setBackgroundColor(LayoutUtils.DARK_GRAY);
					}

					
					
					//displayToast(context,"Table row "+v.getId()+"clicked");
					v.setBackgroundColor(LayoutUtils.HIGHLIGHT_COLOR);
					v.setSelected(true);
					
					
				}
			} );
	
	    }
	    
	    tableRow.addView(button);

    }
    


    
    protected abstract void addNewRow();
    protected abstract void updateRow(TableRow tr);
    protected abstract void saveNewRow(TableRow tr);


}
