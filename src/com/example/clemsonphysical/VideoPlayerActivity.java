package com.example.clemsonphysical;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;



/// Fix surface has been released problem https://androidproblem.wordpress.com/category/mediaplayer/

public abstract class VideoPlayerActivity extends DatabaseActivity implements OnCompletionListener {


	protected VideoView videoView;
	protected VideosManager videoManager;
	protected Utilities utils;
	protected int currentVideoIndex = 0; 
	protected boolean isShuffle = false;
	protected boolean isRepeat = false;
	protected ArrayList<HashMap<String, String>> videosList = new ArrayList<HashMap<String, String>>();
	

	public static final int IMAGE_REQUEST_CODE = 1;
	public static final int REQUEST_IMAGE_CAPTURE = 2;
	public static final int REQUEST_VIDEO_CAPTURE = 3;
	
	protected TextView imageTextViewHolder;
	private Bitmap bitmap;
	protected ImageView imageViewHolder;
	protected Uri mediaUri;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practitioner_video_view);
		
		// All player buttons

		videoView = (VideoView) findViewById(R.id.videoView1);		
		videoManager = new VideosManager();
				
		utils = new Utilities();
		
		// Listeners
		//videoProgressBar.setOnSeekBarChangeListener(this); // Important
		videoView.setOnCompletionListener(this); // Important
		videoView.setMediaController(new MediaController(this));
		
		// Getting all videos list
		videosList = videoManager.getPlayList(this.getExternalFilesDir("videos"));
		
		// By default play first video
		playVideo(0);
				

	}
	

	
	/**
	 * Function to play a video
	 * @param videoIndex - index of video
	 * */
	public void  playVideo(int videoIndex){
		// Play video
		try {
			if (videoIndex >= videosList.size())
				return;
			videoView.setVideoPath(videosList.get(videoIndex).get("videoPath"));
        	videoView.start();
		
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		  catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			
		}
	}


	/**
	 * On Video Playing completed
	 * if repeat is ON play same video again
	 * if shuffle is ON play random video
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
		// check for repeat is ON or OFF
		if(isRepeat){
			// repeat is on play same video again
			playVideo(currentVideoIndex);
		} else if(isShuffle){
			// shuffle is on - play a random video
			Random rand = new Random();
			currentVideoIndex = rand.nextInt((videosList.size() - 1) - 0 + 1) + 0;
			playVideo(currentVideoIndex);
		} else{
			// no repeat or shuffle ON - play next video
			if(currentVideoIndex < (videosList.size() - 1)){
				playVideo(currentVideoIndex + 1);
				currentVideoIndex = currentVideoIndex + 1;
			}else{
				// play first video
				playVideo(0);
				currentVideoIndex = 0;
			}
		}
	}
	
	@Override
	 public void onDestroy(){
	 super.onDestroy();
	    //mp.release();
	 }
	
	@Override
	
	/**
	 *
	 * @fn public boolean onCreateOptionsMenu(Menu menu)
	 * @brief Inflate the menu; this adds items to the action bar if it is present.
	 * @param menu Meny to be created.
	 * @return true
	 */
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.video_menu, menu);
		return true;
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
			
	    case R.id.action_about:
//	    	intent = new Intent(this, InfoView.class);
//	    	startActivity(intent);
	    	break;

	    case R.id.action_add_annotation:
	    	Annotate();
	    	break;
	    	
	    case R.id.action_annotation_list:
	    	//deleteAllRows();
	    	break;	
	    	
	    case R.id.action_settings:
	    	intent = new Intent(this, SettingsActivity.class);
	    	startActivity(intent);
	    	break;
	    case R.id.action_record:
	    	dispatchTakeVideoIntent();
	    	break;
	    	
	    }
	    return true;
	}

	public abstract void Annotate();


	
    public void dispatchSelectImageIntent(ImageView iv, TextView tv)
    {
    	/// Working with an image picker from http://www.vogella.com/tutorials/AndroidCamera/article.html

    	// save the view.
    	imageViewHolder = iv;
    	imageTextViewHolder = tv;

    	// create the intent
    	Intent intent = new Intent();
    	intent.setType("image/*");
    	intent.setAction(Intent.ACTION_GET_CONTENT);
    	intent.addCategory(Intent.CATEGORY_OPENABLE);
    	startActivityForResult(intent, IMAGE_REQUEST_CODE);
    	
    }
    
    public void dispatchTakePictureIntent(ImageView iv, TextView tv) {
		
		// save the view.
		imageViewHolder = iv;
		imageTextViewHolder = tv;
		
		/// Started with this http://developer.android.com/training/camera/photobasics.html
		/// But ran into this problem http://stackoverflow.com/questions/13912095/java-lang-nullpointerexception-on-bundle-extras-data-getextras
		
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) 
        {
            // Create the File where the photo should go
            File photoFile = null;
            try 
            {
                photoFile = createImageFile();
            } catch (IOException ex) 
            {
                // Error occurred while creating the File
                
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
            	mediaUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,  mediaUri                     );
                //takePictureIntent.putExtra("filename", "file:"+photoFile.getAbsolutePath());
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = this.getExternalFilesDir("receipts");
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intent
	    return image;
	}
	
    public void dispatchTakeVideoIntent() {
		
		// save the view.
		
		//imageTextViewHolder = tv;
		
		/// Started with this http://developer.android.com/training/camera/photobasics.html
		/// But ran into this problem http://stackoverflow.com/questions/13912095/java-lang-nullpointerexception-on-bundle-extras-data-getextras
		
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) 
        {
            // Create the File where the photo should go
            File videoFile = null;
            try 
            {
                videoFile = createVideoFile();
            } catch (IOException ex) 
            {
                // Error occurred while creating the File
                
            }
            // Continue only if the File was successfully created
            if (videoFile != null)
            {
            	mediaUri = Uri.fromFile(videoFile);
            	// Use high quality (.mp4) video. Low quality (.3gp) video sucks. 
            	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,  mediaUri );
                //takePictureIntent.putExtra("filename", "file:"+photoFile.getAbsolutePath());
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }

	
	private File createVideoFile() throws IOException {
	    // Create an video file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String videoFileName = "VID_" + timeStamp + "_";
	    File storageDir = this.getExternalFilesDir(getVideoSubdirectory());
	    File video = File.createTempFile(
	        videoFileName,  /* prefix */
	        ".mp4",         /* suffix */
	        //".3gp",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intent
	    return video;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
        	 currentVideoIndex = data.getExtras().getInt("videoIndex");
        	 // play selected video
            playVideo(currentVideoIndex);
       }
		
		if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		{
			imageTextViewHolder.setText(data.getDataString());
			//LayoutUtils.loadImage(this,data.getData(),imageViewHolder);
			try
			{
				rotateAndSetImage(imageViewHolder,data.getData().getPath());
			}
			catch (Exception e)
			{
				//imageViewHolder.setImageResource(R.drawable.ic_receipt);
			}
			
			
		}
		else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) 
		{
			/// Fixed null pointer exceptions from - http://www.androidhive.info/2013/09/android-working-with-camera-api/
	        try {
	            rotateAndSetImage(imageViewHolder,mediaUri.getPath());
	            imageTextViewHolder.setText(mediaUri.getPath());
	        } catch (NullPointerException e) {
	            e.printStackTrace();
	        }
	        
	    }
		else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) 
		{
			/// Fixed null pointer exceptions from - http://www.androidhive.info/2013/09/android-working-with-camera-api/
	        try {
	        	displayToast("Preview Video "+mediaUri.getPath());
	        	previewVideo(mediaUri.getPath());
	            //rotateAndSetImage(imageViewHolder,mediaUri.getPath());
	            //imageTextViewHolder.setText(mediaUri.getPath());
	        } catch (NullPointerException e) {
	            e.printStackTrace();
	        }
	        
	    }
		else if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_VIDEO_CAPTURE) && resultCode == RESULT_CANCELED)
		{
			File file = new File(mediaUri.getPath());
			file.delete();
		}

		
	}
	
	protected void previewVideo(String path)
	{
        try {
            
        	videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(path);
            // start playing
            videoView.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	protected void rotateAndSetImage(ImageView imageView, String path)
	{
    	
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 4;

        
        final Bitmap bitmap = BitmapFactory.decodeFile(path,
                options);
	    // is this damn thing sideways? Should be in portrait mode
	    /// Rotating images from http://www.higherpass.com/Android/Tutorials/Working-With-Images-In-Android/3/
	    System.err.println("Bitmap: w="+bitmap.getWidth()+" h="+bitmap.getHeight());
	    if (bitmap.getWidth() <= bitmap.getHeight())
	    {
	    	imageView.setImageBitmap(bitmap);
	    } 
	    else
	    {
	        Matrix mat = new Matrix();
	        mat.postRotate(90);
	        Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
	        imageView.setImageBitmap(bMapRotate);
	    }
	}
	
	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	 
	    // save file url in bundle as it will be null on scren orientation
	    // changes
	    outState.putParcelable("media_uri", mediaUri);
	}
	 
	/*
	 * Here we restore the fileUri again
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	 
	    // get the file url
	    mediaUri = savedInstanceState.getParcelable("media_uri");
	}
    
	protected abstract String getVideoSubdirectory();
	
	
}