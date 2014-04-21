package edu.clemson.physicaltherapy.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.MediaController;
import edu.clemson.physicaltherapy.R;

public class AudioPlayerView extends VideoViewActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		
		String audio_location = intent.getStringExtra("audio_location");
		setVideoPath(audio_location);
		
		// Override the media controller to always show. 
		// http://stackoverflow.com/questions/3581208/show-the-mediacontroller
		MediaController controller = new MediaController(this){
		    @Override
		    public void hide() {
		        this.show(0);
		    }

		    @Override
		    public void setMediaPlayer(MediaPlayerControl player) {
		        super.setMediaPlayer(player);
		        this.show();
		    }
		};
		
		
		setMediaController(controller);
		
		start();
		controller.show(0);
		
		
	}

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.dialog_play_audio);
	}

	
	@Override
	protected String getVideoSubdirectory() {
		
		return "audio_notes";
	}
	

	@Override
	/**
	 * @fn public boolean onOptionsItemSelected(MenuItem item)
	 * @brief Handles menu item selection. 
	 * Only menu item here is the "action_about" for the info activity.
	 * @param item MenuItem that was selected
	 * @return true  
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
	
		 Intent intent = null;
		/// Menu from http://developer.android.com/guide/topics/ui/menus.html#options-menu
	    switch (item.getItemId()) {
	   
		case android.R.id.home:

			NavUtils.navigateUpFromSameTask(this);
			return true; 
			

	    }
	    return true;
	}

	@Override
	protected void addAnnotation(int time, String annotation) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void deleteAnnotation(int time, String annotation, int interval) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateAnnotation(int time, String annotation, int interval) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String readAnnotation(int time, int interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void deleteAllAnnotations() {
		// TODO Auto-generated method stub

	}


}
