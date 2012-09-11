package com.crowdspeak;

import android.app.Activity;

public class SettingsActivity extends Activity {
	
	
	//put PREFS_NAME in strings.xml
	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	boolean logged = settings.getBoolean("logged", false);
    
	
	
	SharedPreferences.Editor editor = settings.edit();
    editor.putBoolean("silentMode", mSilentMode);

    // Commit the edits!
    editor.commit();


}
