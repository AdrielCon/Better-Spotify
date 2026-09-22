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
            Scanner fileSearch = new Scanner(KaggleDataset, "UTF-8"); //scanner obj created that points at Kaggle
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
            System.err.println(" could not open file dataset.csv"); //incase file not found
        }
        Scanner userInput = new Scanner(System.in); //Creates variable to be inputed by USER
        System.out.println("Enter the name of a Song: "); //Prompt for user to input 
        String userTitle = userInput.nextLine(); //Reads the entire line that was provided by User
        
        
        //================================= Title Search =========================================
        
        songObj foundSong = null; //Create a songObj that is empty??
        for (songObj currentSong : songList ) { //For Each Loop - For each songObj in songList, temporarily call that object currentSong
            if (currentSong.getTitle().equalsIgnoreCase(userTitle) ) { // COMPARES currentSong's private title to the userTitle -- equalsIgnoreCase() makes sure titles are not CASE SENSITIVE 
                foundSong = currentSong;
                break;
            }

        }
        //================================= Check if Song Found AND match genres once it is ===================================

        if (foundSong != null) { // Check whether the Title Search was successful 

            System.out.println("Song found!"); 
            System.out.println("Title: " + foundSong.getTitle()); 
            System.out.println("Artist: " + foundSong.getArtist()); 
            System.out.println("Genre: " + foundSong.getGenre());
            
            ArrayList <songObj> matchingGenre = new ArrayList<songObj>(); // Creates an EMPTY LIST that will hold all songs of the same genre as input song
            for (songObj currentSong : songList) { //loops through every songObj in the full CSV
                if (currentSong.getGenre().equalsIgnoreCase(foundSong.getGenre())) { // Compares genres of our "pointer"
                    if (currentSong != foundSong) {
                        matchingGenre.add(currentSong); //Adds a REFERENCE copy of the songObj to pool of potential recommendations
                    }
                } 
            }
            System.out.println("Songs with matching genre: " + matchingGenre.size()); //TEMP TEST showing how many potential recommendations were found


        } else { // foundSong remained NULL meaning no matching title was found
            System.out.println("Song not found :( "); 
        }
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
