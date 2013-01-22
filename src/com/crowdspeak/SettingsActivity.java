package com.crowdspeak;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsActivity extends Activity {
	
	
	//put PREFS_NAME in strings.xml
	SharedPreferences settings = getSharedPreferences("crowdspeak", 0);
	//boolean logged = settings.getBoolean("logged", false);
    
	
	
	Editor editor = settings.edit();
	//editor.putString("location", "text");
    // Commit the edits!
    //editor.commit();


}
