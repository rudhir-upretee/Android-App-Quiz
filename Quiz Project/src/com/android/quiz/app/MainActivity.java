package com.android.quiz.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private EditText editTxtUname;
	public static String username = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        editTxtUname = (EditText) findViewById(R.id.editTextUsername);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    
    public void okUsernameHandler(View view) {
    	
    	username = editTxtUname.getText().toString();
    	System.out.println(username);
    	Intent intent = new Intent(this, StartScreenActivity.class);
    	startActivity(intent);
    }
    
    public void exitUsernameHandler(View view) {
        finish();
    }
    
    
}

