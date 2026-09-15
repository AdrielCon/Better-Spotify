import java.util.ArrayList; //to create list of songs
import java.util.Scanner; //allows program to read from user inputs
import java.io.File; //allows program to read from outside files
import java.io.FileNotFoundException; //error handling in case spotify file not found

public class BetterSpotify {

    public static void main(String[] args) {
        File KaggleDataset = new File("dataset.csv"); //creates a file object that will hold data.csv

        System.out.println("Database obj created successfully"); //test to make sure object got created properly

        ArrayList <songObj> songList = new ArrayList<songObj>(); //Create empty ArrayList that will store ALL csv songs as objects 

        try { 
            Scanner fileSearch = new Scanner(KaggleDataset, "UTF-8"); //scanner obj created that points at Kaggle-- UTF-8 is needed because asian characters
           fileSearch.nextLine(); // Make Sure Header doesn't become an object
           int totalRows = 0; //TEST
           int badRows = 0; //TEST
            while (fileSearch.hasNextLine()) { //initiating a loop that repeasts until file ends
                totalRows++; 

                String dataline = fileSearch.nextLine(); //stores the next line from dataset and stores it in a string variable
                String[] columns = parseCSVLine(dataline); 

                if (columns.length < 21) { //Checks for any ROWS with missing data which leads to errors in creating songObj's 
                    badRows++;
                    continue; // Skips bad ROWS 
                }

                String artist = columns[2]; //Takes the artist from current song and makes a variable out of it

                String title = columns[4];

                String genre = columns[20];

                songObj currentSong = new songObj(title, artist, genre); //Takes variables and implements an Object

                songList.add(currentSong); //Adds the newly made Object into our Array

            } //End of while loop

            if (fileSearch.ioException() != null) { // Checks whether Scanner stopped reading because an input/output error occurred
             System.err.println(fileSearch.ioException()); // Prints the exact error that caused Scanner to stop reading
            }

            fileSearch.close(); //always make sure to close a file 
            System.out.println("Songs loaded: " + songList.size()); //Prints out the number of songObj's we have to search from
            System.err.println(" Bad rows: " + badRows );
            System.err.println(" Total Rows: " + totalRows );           
        
        } catch (FileNotFoundException e) { //incase file not found
            System.err.println(" could not open file data.csv"); //incase file not found
        }
        Scanner userInput = new Scanner(System.in); //Creates variable to be inputed by USER
        System.out.println("Enter the name of a Song: "); //Prompt for user to input 
        String userTitle = userInput.nextLine(); //Reads the entire line that was provided by User
        System.out.println( "You searched for: " + userTitle); //TEMP TEST 
    }
    public static String[] parseCSVLine(String line) { //Method that takes the lines we read and returns a column of useful data

        String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); //Very Important-- Splits only on commas that are NOT inside quotations. THIS IS STOLEN

        for (int i = 0; i < columns.length; i++) { //Loops through every column that was created by above command
            columns[i] = columns[i].trim(); // Removes unnecessary spaces from the beginning/end of the column

            if (columns[i].startsWith("\"") && columns[i].endsWith("\"")){
                columns[i] = columns[i].substring(1,columns[i].length() - 1);
            }
         }
         return columns;
    }
}
