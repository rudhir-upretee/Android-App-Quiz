//============================================================================
// Name : AddQuestionActivity.java
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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddQuestionActivity extends Activity {
	// GUI controls
	EditText txtData;
	Button btnWriteSDFile;
	Button btnReadSDFile;
	Button btnClearScreen;
	Button btnClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Intent intent = getIntent();
        setContentView(R.layout.activity_display_score);
        
        // bind GUI elements with local controls
    	txtData = (EditText) findViewById(R.id.txtData);
    	//txtData.setHint("Enter some lines of data here...");
    }
    
    public void writeToFile(View view) {

		try {
			FileOutputStream fOut = openFileOutput("myfile.txt", 
												Context.MODE_PRIVATE);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
			myOutWriter.append(txtData.getText());
			myOutWriter.close();
			fOut.close();
			Toast.makeText(getBaseContext(),
					"Done writing SD 'mysdfile.txt'",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

    }
    
    public void clearScreen(View view) {
    	txtData.setText("");

    }
    
    public void readFromFile(View view) {
    	
    	try {
			FileInputStream fIn = openFileInput("myfile.txt");
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
			txtData.setText(aBuffer);
			myReader.close();
			Toast.makeText(getBaseContext(),
					"Done reading SD 'mysdfile.txt'",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

    }
    
    public void exit(View view) {
    	finish();

    }

}
