// packages and imports
package core;

import java.util.ArrayList;

/**
 *
 * @author Nicolas
 */

// board class
public class Board 
{   
    // declare variable names and values
    private final int NUMBER_OF_DICE = 16;
    private final int NUMBER_OF_SIDES = 6;
    // variable used in the shakeDice method for 4x4 grid
    private int counter;
    
    // create ArrayList called diceData
    private ArrayList diceData = new ArrayList();
    // create ArrayList called die
    private final ArrayList<Die> die  = new ArrayList();
    
    // put boggleData into the diceData which is an ArrayList
    public Board(ArrayList boggleData) 
    {
            diceData = boggleData;
    }
   
    // populateDice creates new instance of dice 
     public void populateDice()
     {
        Die createDie = new Die();
        for(int i=0; i<diceData.size(); i++)
        {
            // mod to check the number of sides and when 6 goes to new die.
            if(i%NUMBER_OF_SIDES == 0 && i != 0)
            {
        	die.add(createDie);
          	createDie = new Die();
            }
            createDie.addLetter(diceData.get(i).toString());
           // add a die
            if(i+1 == diceData.size())
                die.add(createDie);
        }
    }
    
   // this method is used to print out all the dice stored in another method  
   public void displayAllDice() 
   {
    for(int i=0; i<die.size(); i++) 
    {
      System.out.print("Die " + i + ": ");
      // calls the displayAllLetters method and prints the string in loop
      die.get(i).displayAllLetters();
      System.out.println();
    }    
  }
    
   // shakeDice method causing random letter 4x4 grid to happen
    public ArrayList shakeDice() 
    {
        // prints out the name Boggle Board
        System.out.println("Boggle Board");
        populateDice();
        String randomLetter;
        // loop the dice pulling a random letter from the get letter method
        // then set random letter to the dice getletter and print it out
        for(int k=0; k<NUMBER_OF_DICE; k++)
        {
           randomLetter = die.get(k).getLetter();            
            System.out.print(randomLetter+" ");
            // add to counter to skip first mod, these few lines cause 4x4 grid
            counter++;
            if(counter %4 == 0)
            {
                System.out.println();
            } 
        }
        return die;
    } 
}