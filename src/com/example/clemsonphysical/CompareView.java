package com.example.clemsonphysical;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class CompareView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compare_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compare_view, menu);
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
        }
 
        return true;
    }

}
