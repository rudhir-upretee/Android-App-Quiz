package com.android.quiz.app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class StartScreenActivity extends Activity {
	
	private final int CLOSE_ACTIVITY = 1;
	private final int CLOSE_DIALOG = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    public void setOptions(View view) {
    	Intent intent = new Intent(this, SetOptionsActivity.class);
    	startActivity(intent);
    }
    
    public void displayScore(View view) {
    	/*Intent intent = new Intent(this, DisplayScoreActivity.class);
    	startActivity(intent);*/
    	showScore();
    	
    }
    
    public void runQuiz(View view) {
    	Intent intent = new Intent(this, RunQuizActivity.class);
    	startActivity(intent);
    }
    
	public void showScore()
	{
    String line = "";
    String username = MainActivity.username;
    String outStr = "Score for user : " + username + "\n\n";
	
	try
		{
		FileInputStream fIn = openFileInput("Score.dat");
		BufferedReader myReader = new BufferedReader(
										 new InputStreamReader(fIn));

		while ((line = myReader.readLine()) != null) {

			if (line.startsWith(username)) 
				{
				// Found the entry for this user
				//System.out.println(line);
				int tokCnt = 0;
				StringTokenizer sTok = new StringTokenizer(line, ",");
				while (sTok.hasMoreElements()) 
					{
					tokCnt++;
					if(tokCnt == 1)
						{
						//First token is the username
						sTok.nextElement();
						continue;
						}
					else if(tokCnt == 2)
						{
						outStr += "File: " + 
									sTok.nextElement().toString() + 
									" | ";
						}
					else if(tokCnt == 3)
						{
						outStr += "Questions: " + 
									sTok.nextElement().toString() + 
									" | ";
						}
					else if(tokCnt == 4)
						{
						outStr += "Score: " +
									sTok.nextElement().toString() + 
									" | ";
						}
					
					}
				outStr += "\n";
				}
			}
		// Display score
		showMessageDialog (outStr, CLOSE_DIALOG);
		myReader.close();
		}
	catch(Exception e)
		{
		Toast.makeText(getBaseContext(),
				"EXCEPTION !! Cannot display score",
				Toast.LENGTH_SHORT).show();
		}
	}

	private void showMessageDialog(String msg, final int closeType) {
		
	    final Context context = this;
        AlertDialog.Builder alertDialogBuilder = 
        						new AlertDialog.Builder(context);
 
		// set title
		alertDialogBuilder.setTitle("Score");
 
		// set dialog message
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						if(closeType == CLOSE_ACTIVITY) {
							finish();
						} else if(closeType == CLOSE_DIALOG) {
							dialog.cancel();
						}
					}
				  });
 
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
 
		// show it
		alertDialog.show();
	}
	


}
