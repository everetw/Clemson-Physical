package edu.clemson.physicaltherapy;

import android.app.Application;

/// http://www.intridea.com/blog/2011/5/24/how-to-use-application-object-of-android

public class PhysicalTherapyApplication extends Application {
	
	private static PhysicalTherapyApplication singleton;
	private static boolean firstAccessed = true;

	@Override
	public void onCreate() 
	{
		super.onCreate();
		firstAccessed = true;
		singleton = this;
	}
	
	public PhysicalTherapyApplication getInstance()
	{
		return singleton;
	}
	
	public boolean firstAccessed()
	{
		boolean ret = firstAccessed;
		firstAccessed = false;
		return ret;
	}
}
