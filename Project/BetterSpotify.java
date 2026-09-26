import java.util.ArrayList; //to create list of songs
import java.util.Scanner; //allows program to read from user inputs
import java.io.File; //allows program to read from outside files
import java.io.FileNotFoundException; //error handling in case spotify file not found

public class BetterSpotify {

    public static void main(String[] args) {
        File KaggleDataset = new File("dataset.csv"); //creates a file object that will hold data.csv

        System.out.println("Database obj created successfully"); //test to make sure object got created properly

        ArrayList <songObj> songList = new ArrayList<songObj>(); //Create empty ArrayList that will store ALL csv songs as objects 
        //======================================================Building large CSV array====================================================
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
//============================Pulling out sing data class vlaues from CSV columns to maake aa sing songObj per row=========================
                String artist = columns[2];
                String title = columns[4];

                double danceability = Double.parseDouble(columns[8]);
                double energy = Double.parseDouble(columns[9]);

                String genre = columns[20];

                songObj currentSong = new songObj( title, artist, genre, danceability, energy); //creates a new songObj with the data from the current line of the dataset

                songList.add(currentSong);
                }
             //End of while loop
//===============================Error Handling for Scanner=========================================================
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
        System.out.println("Welcome to BetterSpotify!");
        System.out.println("1. Recommended Search");
        System.out.println("2. My Playlists");
        System.out.println("3. Exit");
        System.out.println("Please select an option: ");
        int userOption = userInput.nextInt();
        userInput.nextLine(); // Consume the newline character left by nextInt()


        switch (userOption) {//Switch statement that will execute code based on user input
            
            case 1: System.out.println("Pleaase enter the title of a song you would like to search for: ");
            String userTitle = userInput.nextLine();
//============================================================= Title Search =========================================
        
        songObj foundSong = null; //This will be the userInput song if it exists
        for (songObj currentSong : songList ) { //For Each Loop - For each songObj in songList, temporarily call that object currentSong
            if (currentSong.getTitle().equalsIgnoreCase(userTitle) ) { // COMPARES currentSong's private title to the userTitle -- equalsIgnoreCase() makes sure titles are not CASE SENSITIVE 
                foundSong = currentSong;
                break;
            }

        }
//=========================================== Check if Song Found AND match genres once it is ===================================

        if (foundSong != null) { // Check whether the Title Search was successful 

            System.out.println("Song found!"); 
            System.out.println("Title: " + foundSong.getTitle()); 
            System.out.println("Artist: " + foundSong.getArtist()); 
            System.out.println("Genre: " + foundSong.getGenre());
            
            ArrayList <songObj> matchingGenre = new ArrayList<songObj>(); // Creates an EMPTY LIST that will hold all songs of the same genre as input song
            for (songObj currentSong : songList) { //loops through every songObj in the full CSV
                if (currentSong.getGenre().equalsIgnoreCase(foundSong.getGenre())) { // Compares genres of our "pointer"

                    boolean sameTitle = currentSong.getTitle().equalsIgnoreCase(foundSong.getTitle()); //compares Input song's title to every other song's title

                    boolean sameArtist = currentSong.getArtist().equalsIgnoreCase(foundSong.getArtist()); //compares Input song's artist to every other song's artist

                    if (!(sameTitle && sameArtist)) {
                        matchingGenre.add(currentSong); //Adds a REFERENCE copy of the songObj to pool of potential recommendations
                    }
                } 
            }
            
//============================================= Generating Recommendations =============================================================================

            ArrayList <songObj> recommendations = new ArrayList<songObj>(); //This is where we will store oure most similar songs that get recommended to the user

            ArrayList<Double> distances = new ArrayList<Double>(); //Each song has a "distance" relative to our user's song foundSong. and those distances will be stored her for comparrison later


            //==================================== Determining how similar each song in <matchingGenre> to our input song  ======================================

            for (songObj currentSong : matchingGenre) { //SEARCH NODE will parse through the newly made list of songs that match the genre of userSong
                double danceDifference = foundSong.getDanceability() - currentSong.getDanceability(); //Calculate the difference in danceability between foundSong and each song in our genre list
                double energyDifference = foundSong.getEnergy() - currentSong.getEnergy(); //Calculate the difference in energies between energies of userSong and each song in our genre list
                double distance = Math.sqrt((danceDifference * danceDifference) + (energyDifference * energyDifference)); //Were using the DISTANCE EQUATION to determine similarity of songs

                //======================================================= Caps the size of recommendations to FOUR ======================================
                
                if (recommendations.size() < 4) { //We are checking if our recommendations vector is full yet
                    recommendations.add(currentSong); // Stores current candidate song
                    distances.add(distance); // Stores the current candidate song's calculated distance
                }

                //======================================================= Find and replace the most "distant" song in recommendations ===========================

                else { // This will run once Recommendations List if full
                    int highestDistanceIndex = 0; // We're gonna sort through <distances> to find which has the largest distance (least similar)

                    for (int i = 1; i < distances.size(); i++) { // start working through the list
                        if (distances.get(i) > distances.get(highestDistanceIndex)) { //Directly compares the current loser to the current [i] index
                            highestDistanceIndex = i; //Marks current index as the LOSER if it has a larger distance
                        }
                    }

                    if (distance < distances.get(highestDistanceIndex)) { // now we're comparing new candidates to our current LOSER song
                        recommendations.set(highestDistanceIndex, currentSong); //Replace the LOSER song if new candidate has a lower distance (is more similar) int the <recommendations> list
                        distances.set(highestDistanceIndex, distance); //Replace the LOSER song if new candidate has a lower distance (is more similar) int the <distances> list
                    }

                }
            }
              // ============================================================== TEMP TEST ========================================================================================

        System.out.println("\nThese songs are similar to " + foundSong.getTitle());

        for ( int i = 0; i < recommendations.size(); i++) {
            songObj similarToFoundSong = recommendations.get(i); // Transfers properties of each song in <recommendations> to a temporary songObj

            System.out.println((i+1) + ". " + similarToFoundSong.getTitle() + "- " + similarToFoundSong.getArtist()); 
        }

        //============================================================== Choosing on of the recommendations ==================================================================

        Scanner recChoiceInputScanner = new Scanner(System.in); //Scanner that will read user input for which song they want to add to their playlist
        System.out.println("\nChoose a song to add to \"My Liked Songs Playlist\" by tying 1-4: "); //Ask user to select one song

        //=============================================================== Error Handling of user input =========================================================
        int userChoice;

        while (true) { //This will repeat until a valid input is given (int 1-4)
            
            if (recChoiceInputScanner.hasNextInt()) { // check for INTEGER

                userChoice = recChoiceInputScanner.nextInt(); //Reads the integer 
                
                if (userChoice >= 1 && userChoice <= 4) { //Checks that integer is within valid range <recommendations>
                    
                    break; //A valid input has been input
                
                }
            }

                else { // Loops immediately if input is not an integer
                    
                recChoiceInputScanner.next(); //clears the invalid input
                }

                System.out.println("Invalid input. Please enter a number from 1-4."); //warning of invalid input
            
        }
        //===================================== Show user what they chose ===========================================================

        songObj chosenSong = recommendations.get(userChoice - 1); //creates a songObj based on the index of userChoise input

        System.out.println("You chose: " + chosenSong.getTitle() + " - " + chosenSong.getArtist()); //Displays the song chosen by the user








        } else { // foundSong remained NULL meaning no matching title was found
            System.out.println("Song not found :( "); 
        }

                break;
            case 2:
                System.out.println("My Playlists");
                break;
            case 3:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid option. Please try again."); //default case if user input is not 1, 2, or 3
        }//end of switch statement
    }//end of main method
//=========================================== Method to parse CSV lines ==========================================================
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