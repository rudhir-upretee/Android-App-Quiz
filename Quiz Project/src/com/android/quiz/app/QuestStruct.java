//============================================================================
// Name : QuestStruct.java
// Author : Rudhir
// Version :
// Copyright :
// Description :
//============================================================================

package com.android.quiz.app;

public class QuestStruct {
	
    // Maximum choice per question is 10
    private final int MAX_ANS_CHOICES = 10;

    // Stores the question string
    private String questText;

    // Stores the right answer index
    private int rightAns;

    // Keeps count of total number of answer choices 
    private int numAnsChoice;

    // Stores all the answer choices strings
    private String ansList[];

    // 
    // Constructor Initializes 
    //
    public QuestStruct()
    {
        questText = "";
        rightAns = 1;
        numAnsChoice = 0;
        ansList = new String[MAX_ANS_CHOICES];
    }

    public void appendQuestText (String text)
    {
        questText = questText + text + " ";
    }

    public void setRightAns (int index)
    {
        rightAns = index;
    }

    public void addAnsChoice (String ans)
    {
        try {
            ansList[numAnsChoice++] = ans;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.out.println("---->  Runtime exception <----");
            System.out.println("Exiting program");
            System.out.println();
            System.exit(0);
        }
    }

    public int getRightAns ()
    {
        return rightAns;
    }

    public String getQuestText ()
    {
        return questText;
    }

    public int getNumAnsChoices ()
    {
        return numAnsChoice;
    }

    public String[] getAnsList ()
    {
        return ansList;
    }

}
