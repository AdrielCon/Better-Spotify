import java.util.ArrayList; //to create list of songs
import java.util.Scanner; //allows program to read from user inputs
import java.io.File; //allows program to read from outside files
import java.io.FileNotFoundException; //error handling in case spotify file not found
import java.util.HashMap; //allows program to create a hashmap for title, artist, and genre recommendations

public class BetterSpotify {

    public static void main(String[] args) {
        File KaggleDataset = new File("dataset.csv"); //creates a file object that will hold data.csv

        System.out.println("Database obj created successfully"); //test to make sure object got created properly

        ArrayList <songObj> songList = new ArrayList<songObj>(); //Create empty ArrayList that will store ALL csv songs as objects 
        HashMap <String, ArrayList<songObj>> titleIndex = new HashMap<String, ArrayList<songObj>>(); //Create empty HashMap that will store ALL csv songs as objects with the title as the key
        HashMap <String, ArrayList<songObj>> artistIndex = new HashMap<String, ArrayList<songObj>>();
        HashMap <String, ArrayList<songObj>> genreIndex = new HashMap<String, ArrayList<songObj>>();
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
                String album = columns[3]; 
                String title = columns[4];

                double popularity = Double.parseDouble(columns[5]);
                double danceability = Double.parseDouble(columns[8]);
                double energy = Double.parseDouble(columns[9]);
                double acousticness = Double.parseDouble(columns[14]);
                double instrumentalness = Double.parseDouble(columns[15]);
                double valence = Double.parseDouble(columns[17]);
                double tempo = Double.parseDouble(columns[18]);
                String genre = columns[20];

                songObj currentSong = new songObj(
                    title, artist, genre, album, popularity, danceability, 
                    energy, acousticness, instrumentalness, valence, tempo
                ); //Takes variables and implements an Object

                songList.add(currentSong); //Adds the newly made Object into our Array

                //================================= HashMap Indexing =========================================

                String titleKey = currentSong.getTitle().toLowerCase(); //Creates a key for the HashMap that is the title of the song in lowercase

                if (titleIndex.containsKey(titleKey)) { //Checks if the title already exists in the HashMap
                    titleIndex.get(titleKey).add(currentSong); //If it does, add the current song to the existing list
                } else {
                    ArrayList<songObj> newTitleList = new ArrayList<songObj>(); //If it doesn't, create a new list and add the current song
                    newTitleList.add(currentSong);
                    titleIndex.put(titleKey, newTitleList); //Put the new list into the HashMap with the title as the key
                }

                String artistKey = currentSong.getArtist().toLowerCase(); //Creates a key for the HashMap that is the artist of the song in lowercase

                if (artistIndex.containsKey(artistKey)) { //Checks if the artist already exists in the HashMap
                    artistIndex.get(artistKey).add(currentSong); //If it does, add the current song to the existing list
                } else {
                    ArrayList<songObj> newArtistList = new ArrayList<songObj>(); //If it doesn't, create a new list and add the current song
                    newArtistList.add(currentSong);
                    artistIndex.put(artistKey, newArtistList); //Put the new list into the HashMap with the artist as the key
                }

                String genreKey = currentSong.getGenre().toLowerCase(); //Creates a key for the HashMap that is the genre of the song in lowercase

                if (genreIndex.containsKey(genreKey)) { //Checks if the genre already exists in the HashMap
                    genreIndex.get(genreKey).add(currentSong); //If it does, add the current song to the existing list
                } else {
                    ArrayList<songObj> newGenreList = new ArrayList<songObj>(); //If it doesn't, create a new list and add the current song
                    newGenreList.add(currentSong);
                    genreIndex.put(genreKey, newGenreList); //Put the new list into the HashMap with the genre as the key
                }
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
        System.out.println("Welcome to BetterSpotify!");
        System.out.println("1. Search");
        System.out.println("2.Liked Songs");
        System.out.println("3. Playlists");
        System.out.println("4. Create Playlist");
        System.out.println("5. Exit");
        System.out.println("Please select an option: ");
        int userOption = userInput.nextInt();
        userInput.nextLine(); // Consume the newline character left by nextInt()


        switch (userOption) {//Switch statement that will execute code based on user input
            case 1:
                searchSong(userInput, songList);
                break;
            case 2:
                System.out.println("Liked Songs");
                break;
            case 3:
                System.out.println("Playlists");
                break;
            case 4:
                System.out.println("Create Playlist");
            case 5:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
//================================= Search Method =========================================
     public static void searchSong(Scanner userInput, ArrayList<songObj> songList) {
           System.out.println("Enter the name of a song");
           String userTitle = userInput.nextLine();

           songObj foundSong = null;

              for (songObj currentSong : songList) { //loops through every songObj in the full CSV
                 if (currentSong.getTitle().equalsIgnoreCase(userTitle)) { // Compares title of our "pointer"
                      foundSong = currentSong; //If a match is found, we store the reference to that songObj in foundSong
                      break; //Exits the loop since we found a match
                 }
                }
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
