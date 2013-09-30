//============================================================================
// Name : RunQuizActivity.java
// Author : Rudhir
// Version :
// Copyright :
// Description :
//============================================================================

package com.android.quiz.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RunQuizActivity extends Activity {
	
	private final int MAX_ANS_CHOICES = 5;
    private final int ANS_1 = 1;
    private final int ANS_2 = 2;
    private final int ANS_3 = 3;
    private final int ANS_4 = 4;
    private final int ANS_5 = 5;
    private final int CLOSE_ACTIVITY = 1;
    private final int CLOSE_DIALOG = 2;
	private Random randQuestGenerator;
	private ArrayList<Integer> rndQuestNumList;
	private int questAnswered;
	private int questIndex;
	private int numRightAns;
	private int usrAnsChoice;
	private int maxQuests;
	private int timeLimitSec;
	private int tCnt;
	private String quizFilePath;
	private Boolean timeUp = false;
	private QuestFileParser fileParser;
	
	// GUI elements
	private Timer wallClockTimer;
    private TextView txtWallClock;
    private TextView txtQuestNum;
    private EditText editTxtQuestion;
    private RadioButton radioChoice1;
    private RadioButton radioChoice2;
    private RadioButton radioChoice3;
    private RadioButton radioChoice4;
    private RadioButton radioChoice5;
    private RadioGroup radioGroup1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Intent intent = getIntent();
        setContentView(R.layout.activity_run_quiz);
        
        // bind GUI elements with local controls
        txtWallClock = (TextView) findViewById(R.id.textViewWallclock);
        txtQuestNum = (TextView) findViewById(R.id.textViewQuestionNum);
        editTxtQuestion = (EditText) findViewById(R.id.editTextQuestion);
        radioChoice1 = (RadioButton) findViewById(R.id.radioAnsChoice1);
        radioChoice2 = (RadioButton) findViewById(R.id.radioAnsChoice2);
        radioChoice3 = (RadioButton) findViewById(R.id.radioAnsChoice3);
        radioChoice4 = (RadioButton) findViewById(R.id.radioAnsChoice4);
        radioChoice5 = (RadioButton) findViewById(R.id.radioAnsChoice5);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup);
        
        // Initialize
        quizInitialize();
        
        // Start
        quizStart();
     }
	
	public void quizInitialize() {
		
        randQuestGenerator = new Random(System.currentTimeMillis());
        rndQuestNumList = new ArrayList<Integer>();
        questAnswered = 0;
        questIndex = -1;
        numRightAns = 0;
        usrAnsChoice = -1;
        wallClockTimer = null;
        
        editTxtQuestion.setFocusable(false);
        
        // Load the settings from file
        String[] quizFile = new String[1];
        int[] qCnt = new int[1];
        int[] tLimit = new int[1];
        getSettings(quizFile, qCnt, tLimit);
        int maxQNos = qCnt[0];
        int timeLimit = tLimit[0];
      
        // Parse the question file
        fileParser = new QuestFileParser(quizFile[0]);
        if((maxQNos > fileParser.getNumQuests()) || 
           (maxQNos <= 0)) 
        	{
            maxQuests = fileParser.getNumQuests();
        	} 
        else 
        	{
            maxQuests = maxQNos;
        	}

        // Generate list of non repeating random questions
        int qNum = -1;
        for(int i = 0; i < maxQuests; i++) 
        	{
            qNum = randQuestGenerator.nextInt(fileParser.getNumQuests());
            while(rndQuestNumList.contains(qNum)) 
            	{
                qNum = randQuestGenerator.nextInt(fileParser.getNumQuests());
            	}
            rndQuestNumList.add(qNum);
        	}
        quizFilePath = quizFile[0];
        
        // Set the quiz time. Default is 30 minutes.
        if(timeLimit <= 0) 
        	{
        	//timeLimitSec = 30 * 60 * 1000;
        	timeLimitSec = 30 * 60;
        	} 
        else 
        	{
        	timeLimitSec = timeLimit;
        	}
        tCnt = 0;
        
	}
		
   public void getSettings(String[] file, int[] qCnt, int[] tLimit) {
    	
    	try 
    		{
    		String strQuestCnt = "";
    		String strTimeLimit = "";
			FileInputStream fIn = openFileInput("Meta.dat");
			BufferedReader myReader = new BufferedReader(
											 new InputStreamReader(fIn));
			int cnt = 1;
			String line = "";
			while ((line = myReader.readLine()) != null) 
				{
				if(cnt == 1)	
					file[0] = line;
				if(cnt == 2)
					strQuestCnt = line;
				if(cnt == 3)
					strTimeLimit = line;
				cnt++;
				}
			myReader.close();
			
			qCnt[0] = Integer.parseInt(strQuestCnt);
			tLimit[0] = Integer.parseInt(strTimeLimit);
			} 
    	catch (Exception e) 
    		{
			Toast.makeText(getBaseContext(), e.getMessage(),
					       Toast.LENGTH_SHORT).show();
    		}
    }
   
   public void quizStart() {
   
   		// Start wall clock
		wallClockTimerStart();
   
        // Fetch question
        getNextQuest();
   }

    private void getNextQuest ()
    {
        int numAnsChoices = 0;
        String questText;

        // Display Question Number
        String msg = "Question " + (questAnswered+1);
        txtQuestNum.setText(msg);

        // Get Question Index
        questIndex = rndQuestNumList.get(questAnswered);
       
        // Fetch Question
        questText = fileParser.getQuestTextAt (questIndex);

        // Display Question
        System.out.println(questText);
        editTxtQuestion.setText(questText);

        // Display Answer Choices
        numAnsChoices = fileParser.getNumAnsChoicesAt (questIndex);
        String ansList[] = new String[MAX_ANS_CHOICES];
        ansList = fileParser.getAnsListAt (questIndex);
        displayAnswerChoices(numAnsChoices, ansList);

    }

   private void wallClockTimerStart() {
   		wallClockTimer = new Timer();
   		
   		final Handler handler = new Handler();
   		TimerTask tTask = new TimerTask() {
	        public void run() {
	                handler.post(new Runnable() {
	                        public void run() {
								updateWallTime();
	                        }
	               });
	        }
	    };
    	        
    	wallClockTimer.scheduleAtFixedRate(tTask, 1, 1000);
   		}
     
    private void wallClockTimerStop() {
   		wallClockTimer.cancel();
    }
   		
    private void updateWallTime() {
    	
    	String msg = "Time Remaining : "+(timeLimitSec - tCnt)+" seconds";
    	txtWallClock.setText(msg);
   	    if(tCnt >= (timeLimitSec)) {
    		timeUp = true;
			reportScore();
    	}
    	tCnt++;
    }	
    
    private void displayAnswerChoices(int numChoices, String ans[]) {
    
        // Clear the radio buttons
     	radioGroup1.clearCheck();
         	
        radioChoice1.setVisibility(View.GONE);
        radioChoice2.setVisibility(View.GONE);
        radioChoice3.setVisibility(View.GONE);
        radioChoice4.setVisibility(View.GONE);
        radioChoice5.setVisibility(View.GONE);
        
        // Show only those Radio Buttons which are required
        switch (numChoices) {
            case  5 :  radioChoice5.setText (ans[4]);
                       radioChoice5.setVisibility(View.VISIBLE);

            case  4 :  radioChoice4.setText (ans[3]);
                       radioChoice4.setVisibility(View.VISIBLE);

            case  3 :  radioChoice3.setText (ans[2]);
                       radioChoice3.setVisibility(View.VISIBLE);

            case  2 :  radioChoice2.setText (ans[1]);
                       radioChoice2.setVisibility(View.VISIBLE);

            case  1 :  radioChoice1.setText (ans[0]);
                       radioChoice1.setVisibility(View.VISIBLE);

        }
         	
         	// Display answers
	    	/*radioChoice1.setText(ans[0]);
	    	radioChoice2.setText(ans[1]);
	    	radioChoice3.setText(ans[2]);
	    	radioChoice4.setText(ans[3]);
	    	radioChoice5.setText(ans[4]);
	    	radioChoice5.setVisibility(View.GONE);*/
    }

	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton)view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) 
	    	{
	        case R.id.radioAnsChoice1:
	            if (checked)
	            	usrAnsChoice = ANS_1;
	            break;
	        case R.id.radioAnsChoice2:
	            if (checked)
	            	usrAnsChoice = ANS_2;
	            break;
	        case R.id.radioAnsChoice3:
	            if (checked)
	            	usrAnsChoice = ANS_3;
	            break;
	        case R.id.radioAnsChoice4:
	            if (checked)
	            	usrAnsChoice = ANS_4;
	            break;
	        case R.id.radioAnsChoice5:
	            if (checked)
	            	usrAnsChoice = ANS_5;
	            break;
	    	}

	}
	
	public void nextQuestHandler(View view) {
        questAnswered++;

        if(usrAnsChoice == fileParser.getRightAnsAt(questIndex)) {
            numRightAns++;
        } else {
        	String msg = "WRONG ANSWER\n\nCorrect answer was choice " +
        				 fileParser.getRightAnsAt(questIndex);
        	showMessageDialog(msg, CLOSE_DIALOG);
   		}

        if(questAnswered >= maxQuests) {
            reportScore();
        } else {

            // Clear last answer
            usrAnsChoice = -1;
            getNextQuest();
        }

	}
	
	public void quitHandler(View view) {
    	reportScore();
	}
	
	private void reportScore() {
 
        int pctScore = 0;
        if(maxQuests > 0) {
            pctScore = (int)(((double)numRightAns/(double)maxQuests) * 100.0);
        }
        
        writeScoreToFile(pctScore);
        
        String msg = "";
        if(timeUp)
	        {
	        msg = "YOUR TIME IS UP!!\n\n";	
	        }
        msg += "You answered " + numRightAns + " out of " + maxQuests;
        msg += "\n Your score is " + pctScore + "%";
        msg += "\n Elapsed time in seconds : " + tCnt;
        msg += "\n\n";
        
		showMessageDialog(msg, CLOSE_ACTIVITY);
	}
	
	private void showMessageDialog(String msg, final int closeType) {
	
	    wallClockTimerStop();
	    
	    final Context context = this;
        AlertDialog.Builder alertDialogBuilder = 
        						new AlertDialog.Builder(context);
 
		// set title
		alertDialogBuilder.setTitle("Score Report");
 
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
							wallClockTimerStart();
						}
					}
				  });
 
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
 
		// show it
		alertDialog.show();
	}
	
    private void writeScoreToFile(int score) {
    	try
			{
			FileOutputStream fOut = openFileOutput("Score.dat", 
												   Context.MODE_PRIVATE | Context.MODE_APPEND);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			
			String outStr = "";
			outStr = MainActivity.username + "," +
					 quizFilePath + "," +
					 maxQuests + "," +
					 score;
			myOutWriter.append(outStr);
			myOutWriter.append("\n");
			myOutWriter.flush();
			myOutWriter.close();
			fOut.close();
			}
		catch(IOException e)
			{
			Toast.makeText(getBaseContext(),
							"EXCEPTION!! Score File Cannot be created",
							Toast.LENGTH_SHORT).show();
			}
    }

}
