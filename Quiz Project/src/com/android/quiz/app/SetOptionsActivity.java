//============================================================================
// Name : SetOptionsActivity.java
// Author : Rudhir
// Version :
// Copyright :
// Description :
//============================================================================

package com.android.quiz.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetOptionsActivity extends Activity {
	// GUI controls
	EditText editTxtQuestFile;
	EditText editTxtQuestCnt;
	EditText editTxtTimeLimit;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Intent intent = getIntent();
        setContentView(R.layout.activity_set_options);
        
        // bind GUI elements with local controls
        editTxtQuestFile = (EditText) findViewById(R.id.editTextQuestionFile);
        editTxtQuestCnt = (EditText) findViewById(R.id.editTextQuestionCnt);
        editTxtTimeLimit = (EditText) findViewById(R.id.editTextTimeLimit);
        
        // Load settings from file
        loadFromFile();
    }
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
	
	public void saveSettingsHandler(View view) {
		saveToFile(editTxtQuestFile.getText().toString(), 
				    editTxtQuestCnt.getText().toString(),
				    editTxtTimeLimit.getText().toString());
		finish();
	}
	
	public void cancelSettingsHandler(View view) {
		finish();
	}
	
    public void saveToFile(String questFile, String questCnt, 
    					    String timeLimit) {

		try 
			{
			FileOutputStream fOut = openFileOutput("Meta.dat", 
												   Context.MODE_PRIVATE);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.write(questFile);
			myOutWriter.write("\n");
			myOutWriter.write(questCnt);
			myOutWriter.write("\n");
			myOutWriter.write(timeLimit);
			myOutWriter.write("\n");
			myOutWriter.flush();
			myOutWriter.close();
			fOut.close();

			Toast.makeText(getBaseContext(),
							"Saved Settings",
							Toast.LENGTH_SHORT).show();
			} 
		catch (Exception e) 
			{
			Toast.makeText(getBaseContext(), e.getMessage(),
					       Toast.LENGTH_SHORT).show();
			}
    }
    
    public void loadFromFile() {
    	
    	try 
    		{
			FileInputStream fIn = openFileInput("Meta.dat");
			BufferedReader myReader = new BufferedReader(
											 new InputStreamReader(fIn));
			int cnt = 1;
			String line = "";
			while ((line = myReader.readLine()) != null) 
				{
				if(cnt == 1)	
					editTxtQuestFile.setText(line);
				if(cnt == 2)
					editTxtQuestCnt.setText(line);
				if(cnt == 3)
					editTxtTimeLimit.setText(line);
				cnt++;
				}
			myReader.close();
			} 
    	catch (Exception e) 
    		{
			Toast.makeText(getBaseContext(), e.getMessage(),
					       Toast.LENGTH_SHORT).show();
    		}
    }

}
