//============================================================================
// Name : QuestFileParser.java
// Author : Rudhir
// Version :
// Copyright :
// Description :
//============================================================================

package com.android.quiz.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Environment;

public class QuestFileParser {
	
    private final int MAX_ANS_CHOICES = 10;
    private int questCnt;
    private String line;
    private String quizFile;
    private ArrayList<QuestStruct> questList;

    //
    // Constructor : Main parser
    //
    public QuestFileParser(String filename)
    {
        questCnt = 0;
        quizFile = filename;
        questList = new ArrayList<QuestStruct> ();
  
        int ansChoiceCnt = 0;
        try {
        	// Read from SD Card
        	File extDir = Environment.getExternalStorageDirectory();
        	FileInputStream fIn = new FileInputStream(new File(extDir, quizFile));

            BufferedReader in = new BufferedReader(new InputStreamReader(fIn)); 

            //while(skipComentsAndEmptyLines(in) != -1) {
            while(true) {

                // Search for "@Q"
                if(skipComentsAndEmptyLines(in) == -1) { break;}
                if (line.startsWith("@Q")) {

                    // Found "@Q", search for "@A"
                    QuestStruct q = new QuestStruct();    
                    ansChoiceCnt = 0;
                    
                    if(skipComentsAndEmptyLines(in) == -1) { break;}
                    while (!line.startsWith("@A")) {
                        q.appendQuestText(line);
                        if(skipComentsAndEmptyLines(in) == -1) { break;}
                    }

                    // Found "@A", search for "@E"
                    if(skipComentsAndEmptyLines(in) == -1) { break;}
                    q.setRightAns(Integer.parseInt(line));
                    if(skipComentsAndEmptyLines(in) == -1) { break;}
                    while (!line.startsWith("@E")) {
                        q.addAnsChoice(line);
                        ansChoiceCnt++;
                        if(ansChoiceCnt >= MAX_ANS_CHOICES) {
                            break;
                        }
                        if(skipComentsAndEmptyLines(in) == -1) { break;}
                    }

                    // Found "@E", end of question set
                    questList.add(q);
                    questCnt++;
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
    }

    public int skipComentsAndEmptyLines(BufferedReader in)
    {
        try {

            while(true) {

                // Read line
                line = in.readLine();
                // End of file. Notity caller.
                if(line == null) {
                    return -1;
                }

                // Make sure it is not end of file. Then check for comments and 
                // empty lines
                if (line.equals("") || line.startsWith("*")) {
                    continue;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
        return 0;
    }

    public int getNumQuests ()
    {
        return questCnt;
    }

    public String getQuestTextAt (int questIndex)
    {
        try {
            QuestStruct q = questList.get(questIndex);
            return q.getQuestText();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
        return "";
    }

    public int getRightAnsAt (int questIndex)
    {
        try {
            QuestStruct q = questList.get(questIndex);
            return q.getRightAns();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
        return 0;
    }

    public String[] getAnsListAt (int questIndex)
    {
        try {
            QuestStruct q = questList.get(questIndex);
            return q.getAnsList();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
        
        String DummyS[] = new String[1];
        return DummyS;
    }

    public int getNumAnsChoicesAt (int questIndex)
    {
        try {
            QuestStruct q = questList.get(questIndex);
            return q.getNumAnsChoices();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
        return 0;
    }

    public int getQuestSetAt (int questIndex, String text, int ansIndex[], 
                              String ansList[])
    {
        try {
            QuestStruct q = questList.get(questIndex);
            text = q.getQuestText();    
            ansIndex[0] = q.getRightAns();
            ansList = q.getAnsList();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
        return 0;
    }

    //
    // This is a debug function
    //
    public int printAllQuests () 
    {
        String ansList[] = new String[MAX_ANS_CHOICES];

        try {
            for(int i = 0; i < questCnt; i++) {
                QuestStruct q = questList.get(i);
                System.out.println(q.getQuestText());
                System.out.println(q.getRightAns());

                ansList = q.getAnsList();
                for(int j = 0; j < q.getNumAnsChoices(); j++) {
                    System.out.println(ansList[j]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
        return 0;
    }  

}
