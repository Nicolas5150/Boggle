// packages and imports
package core;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Nicolas
 */

// die class
public class Die 
{   
    // creating new variable for sides of dice and ArrayList
    private final int NUMBER_OF_SIDES = 6;
    private final ArrayList <String> die = new ArrayList<>();
    private String letter;
    
    // method randomLetter
    public void randomLetter()
    {
        Random gen = new Random ();       
        int num = gen.nextInt(this.NUMBER_OF_SIDES);
        // use to get the random letter that gets passed into shake die method
         letter = die.get(num);  
    }
    
    // method getting this randomLetter 
    public String getLetter()
    {
        this.randomLetter();
        return letter;   
    }
    
    // method addLetter for dice
    public void addLetter(String dice)
    {
        die.add(dice);       
    }
    
    // method displayAllLetters is used to print the name & value of the dies
    public void displayAllLetters()
    {
        for (String die1 : die) 
        {
            // printing out letters and a space
            System.out.print(die1 + " ");            
        }
    }
}