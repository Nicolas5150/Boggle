// packages and imports
package inputOuput;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;//the .File exends object
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Nicolas
 */

// reading data class
public class ReadDataFile 
{   
    // variable call for string and a new ArrayList
    private Scanner input;
    private final String dataFile;
    private final ArrayList data = new ArrayList();
     
    // the parameter of the String representing name of data file declared
    public ReadDataFile(String inDataFile)
    {
        this.dataFile = inDataFile;
    }
    
    // method populate data which gets dice letters from BoggleData.txt
    public void populateData()
    {
        // try this code below, which should all work else use the catch method
        try 
        { 
            // make instance of class URL by doing file name of the data file
            URL url = getClass().getResource(dataFile);
            
            // create an instance of class File using the URL from above
            // URI is used because we are using a mac computer
            File file = new File(url.toURI()); 
            
            // initialize variable of Scanner 
            Scanner inputFile =new Scanner(file);
    
            // while loop to go throught the data file until nothings left 
            while(inputFile.hasNext())
            {
                //Add to arrayList the data in the file
                data.add(inputFile.next());
            }
        }
        // else catch the exceptions that may happen
        catch(URISyntaxException | FileNotFoundException ex)
        {
            System.out.println(ex.toString());
        }       
}
  
    // method that will return data from the ArrayList created
    public ArrayList getData()
    {
        return data;
    }
}