// packages and imports
package boggle;

import core.Board;
import inputOuput.ReadDataFile;
import java.util.ArrayList;
import userInterface.BoggleUi;

/**
 *
 * @author Nicolas
 */

// boggle class
public class Boggle 
{
    // new ArrayList with boggleData and add the BoggleData file as inputFile
    private static ArrayList boggleData = new ArrayList();
    private static final String inputFile = "BoggleData.txt";
    
    // assign3 - adding input from TempDict
    public static ArrayList dictionaryData = new ArrayList();
    private static final String inputDictionary = "TemporaryDictionary.txt";
    
    //extra- adding in the highscore data
    public static ArrayList scoreData = new ArrayList();
    private static final String inputScore = "HighScore.txt";

    public static void main(String[] args) 
    {
        ReadDataFile dataRead = new ReadDataFile(inputFile);
        // method populateData() from the class ReadDataFile in "inputOutput"
        dataRead.populateData();
        // make variable boggleData = to method call getData() in ReadDataFile
        boggleData = dataRead.getData();
        
        
        // assign3 - passing in the dictionary into the variable name
        ReadDataFile readDictionary = new ReadDataFile(inputDictionary);
        // assign3 - creating call for reading in data
        readDictionary.populateData();
        //assign3 - make variable dictionaryData = to method call getData() again
        dictionaryData = readDictionary.getData();
        
        //extra- reading in data from the highscore file
        ReadDataFile readScore = new ReadDataFile(inputScore);
        // creating call for reading in data
        readScore.populateData();
        // make variable scoredata equal to method call getData() n ReadDataFile
        scoreData = readScore.getData();
        
        
        // make object of class Board passing data from boggleData as argument
        Board boggleBoard = new Board(boggleData);
        // calling populateDice() on the boggleBoard creating instances of dice  
        boggleBoard.populateDice();
        // calling method to diplay all die 0-15
      	boggleBoard.displayAllDice(); 
        // calling method for boggle Ui and create the board for front end
        BoggleUi boggleUi = new BoggleUi(boggleBoard, dictionaryData, scoreData);
    }
}