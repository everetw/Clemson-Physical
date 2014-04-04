package com.example.clemsonphysical;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public abstract class CameraActivity extends DatabaseActivity {
	
	public static final int IMAGE_REQUEST_CODE = 1;
	public static final int REQUEST_IMAGE_CAPTURE = 2;
	public static final int REQUEST_VIDEO_CAPTURE = 3;
	
	private Bitmap bitmap;
	protected Uri mediaUri;

	
	
	
    public void dispatchSelectImageIntent()
    {
    	/// Working with an image picker from http://www.vogella.com/tutorials/AndroidCamera/article.html

    	// create the intent
    	Intent intent = new Intent();
    	intent.setType("image/*");
    	intent.setAction(Intent.ACTION_GET_CONTENT);
    	intent.addCategory(Intent.CATEGORY_OPENABLE);
    	startActivityForResult(intent, IMAGE_REQUEST_CODE);
    	
    }
    
    public void dispatchTakePictureIntent() {
		
		
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
    
	protected void previewVideo(VideoView vv, String path)
	{
        try {
            
        	vv.setVisibility(View.VISIBLE);
            vv.setVideoPath(path);
            // start playing
            vv.start();
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
	
	
	protected abstract String getVideoSubdirectory();

}
