import java.util.ArrayList; //to create list of songs
import java.util.Scanner; //allows program to read from user inputs
import java.io.File; //allows program to read from outside files
import java.io.FileNotFoundException; //error handling in case spotify file not found

public class BetterSpotify {

    public static void main(String[] args) {
        File KaggleDataset = new File("dataset.csv"); //creates a file object that will hold data.csv

        System.out.println("Database obj created successfully"); //test to make sure object got created properly

        try { 
            Scanner fileSearch = new Scanner(KaggleDataset); //scanner obj created that points at Kaggle
            while (fileSearch.hasNextLine()) { //initiating a loop that repeasts until file ends
                String dataline = fileSearch.nextLine(); //stores the next line from dataset and stores it in a string variable
                System.err.println(dataline); //prints the line that just got read   
            } //End of while loop

            fileSearch.close(); //always make sure to close a file 
        
        } catch (FileNotFoundException e) { //incase file not found
            System.err.println(" could not open file data.csv"); //incase file not found
        }
    }
} //HELLO HELLO
