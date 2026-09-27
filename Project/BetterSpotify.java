import java.util.ArrayList; //to create list of songs
import java.util.Scanner; //allows program to read from user inputs
import java.io.File; //allows program to read from outside files
import java.io.IOException; //error handling in case spotify file not found
import java.io.FileNotFoundException; //error handling in case spotify file not found
import java.io.FileWriter; //allows program to write to outside files
import java.util.HashMap;

public class BetterSpotify {

    public static void main(String[] args) {
        File PlaylistFile = new File("playlists.txt"); // creates a file object that will hold playlist.txt
        File KaggleDataset = new File("dataset.csv"); // creates a file object that will hold data.csv
        HashMap<String, ArrayList<String>> playlists = new HashMap<>();
        try {
            if (!PlaylistFile.exists()) {
                PlaylistFile.createNewFile(); // Creates a new file if it doesn't exist

                FileWriter writer = new FileWriter(PlaylistFile);
                writer.write("[Liked Songs]\n");
                writer.close();
                System.out.println("Playlist file created successfully.");
            }
        } catch (IOException e) {
            System.out.println("Could not create this file.");
        }
        loadPlaylists(PlaylistFile, playlists);
        playlists.putIfAbsent("Liked Songs", new ArrayList<String>());
        Scanner userInput = new Scanner(System.in);
        System.out.println("Database obj created successfully"); // test to make sure object got created properly

        ArrayList<songObj> songList = new ArrayList<songObj>(); // Create empty ArrayList that will store ALL csv songs
                                                                // as objects
        // ======================================================Building large CSV
        // array====================================================
        try {
            Scanner fileSearch = new Scanner(KaggleDataset, "UTF-8"); // scanner obj created that points at Kaggle
            fileSearch.nextLine(); // Make Sure Header doesn't become an object
            int totalRows = 0; // TEST
            int badRows = 0; // TEST
            while (fileSearch.hasNextLine()) { // initiating a loop that repeasts until file ends
                totalRows++;

                String dataline = fileSearch.nextLine(); // stores the next line from dataset and stores it in a string
                                                         // variable
                String[] columns = parseCSVLine(dataline);

                if (columns.length < 21) { // Checks for any ROWS with missing data which leads to errors in creating
                                           // songObj's
                    badRows++;
                    continue; // Skips bad ROWS
                }
                // ============================Pulling out sing data class vlaues from CSV
                // columns to maake aa sing songObj per row=========================
                String artist = columns[2];
                String title = columns[4];

                double danceability = Double.parseDouble(columns[8]);
                double energy = Double.parseDouble(columns[9]);
                String album = columns[3];

                String genre = columns[20];
                double durationMs = Double.parseDouble(columns[6]);

                songObj currentSong = new songObj(title, artist, genre, danceability, energy, durationMs, album); // creates
                                                                                                                  // a
                                                                                                                  // new
                                                                                                                  // songObj
                                                                                                                  // with
                                                                                                                  // the
                                                                                                                  // data
                                                                                                                  // from
                                                                                                                  // the
                                                                                                                  // current
                                                                                                                  // line
                                                                                                                  // of
                                                                                                                  // the
                                                                                                                  // dataset

                songList.add(currentSong);
            }
            // ===============================Error Handling for
            // Scanner=========================================================
            if (fileSearch.ioException() != null) { // Checks whether Scanner stopped reading because an input/output
                                                    // error occurred
                System.err.println(fileSearch.ioException()); // Prints the exact error that caused Scanner to stop
                                                              // reading
            }

            fileSearch.close(); // always make sure to close a file
            System.out.println("Songs loaded: " + songList.size()); // Prints out the number of songObj's we have to
                                                                    // search from
            System.err.println(" Bad rows: " + badRows);
            System.err.println(" Total Rows: " + totalRows);

        } catch (FileNotFoundException e) { // incase file not found
            System.err.println(" could not open file dataset.csv"); // incase file not found

        }
        mainMenu(userInput, songList, playlists, PlaylistFile);

    }

    public static void savePlaylist(File playlistFile, HashMap<String, ArrayList<String>> playlists) {
        try {
            FileWriter writer = new FileWriter(playlistFile);
            for (String playlistName : playlists.keySet()) {
                writer.write("[" + playlistName + "]\n");
                ArrayList<String> songs = playlists.get(playlistName);

                for (String song : songs) {
                    writer.write(song + "\n");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Could not save playlist.");
        }
    }

    public static void loadPlaylists(File playlistFile, HashMap<String, ArrayList<String>> playlists) { // Finds
                                                                                                        // playlist
                                                                                                        // names and
                                                                                                        // adds in songs
                                                                                                        // to its
                                                                                                        // playlist
        try {
            Scanner playlistScanner = new Scanner(playlistFile);
            String currentPlaylist = null;
            while (playlistScanner.hasNextLine()) {
                String line = playlistScanner.nextLine();

                if (line.startsWith("[") && line.endsWith("]")) {// detects playlist names
                    currentPlaylist = line.substring(1, line.length() - 1);
                    playlists.put(currentPlaylist, new ArrayList<String>());
                } else if (currentPlaylist != null && !line.isBlank()) {// prevents adding empty spaces as fake songs
                    playlists.get(currentPlaylist).add(line);// correctly reads songs inside playlist
                }
            }
            playlistScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not findd playlist file.");
        }
    }

    public static String[] parseCSVLine(String line) { // Method that takes the lines we read and returns a column of
        // useful data

        String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Very Important-- Splits only on
                                                                                // commas that are NOT inside
                                                                                // quotations. THIS IS STOLEN
        for (int i = 0; i < columns.length; i++) { // Loops through every column that was created by above command
            columns[i] = columns[i].trim(); // Removes unnecessary spaces from the beginning/end of the column

            if (columns[i].startsWith("\"") && columns[i].endsWith("\"")) {
                columns[i] = columns[i].substring(1, columns[i].length() - 1);
            }
        }
        return columns;
    }

    // =======Logic for playlist sub menu===============================
    public static void playlistMenu(Scanner userInput, HashMap<String, ArrayList<String>> playlists,
            File playlistFile) {
        boolean playlistMenuRunning = true;
        while (playlistMenuRunning) {
            System.out.println("\n1, My Liked songs Playlist");
            System.out.println("2. View my Playlists");
            System.out.println("3. Create New Playlist");
            System.out.println("4. Back to Main Menu");
            System.out.println("Please select an option: ");

            int playlistOption = userInput.nextInt();
            userInput.nextLine();

            switch (playlistOption) {
                case 1:
                    viewPlaylist(playlists, "Liked Songs", playlistFile, userInput);
                    break;
                case 2:
                    viewMyPlaylists(userInput, playlists, playlistFile);
                    break;
                case 3:
                    createNewPlaylist(userInput, playlists, playlistFile);
                    break;
                case 4:
                    playlistMenuRunning = false;
                    break;
                default:
                    System.out.println("Invalid response, please try again fucker.");
                    break;
            }
        }
    }

    public static void createNewPlaylist(Scanner userInput, HashMap<String, ArrayList<String>> playlists,
            File playlistFile) {
        System.out.println("Enter the name for your new playlist.");
        String playlistName = userInput.nextLine();
        playlists.put(playlistName, new ArrayList<String>());
        savePlaylist(playlistFile, playlists);
    }

    public static void viewMyPlaylists(
            Scanner userInput,
            HashMap<String, ArrayList<String>> playlists, File playlistFile) {

        ArrayList<String> playlistNames = new ArrayList<String>();
        playlistNames.add("Liked Songs");

        for (String playlistName : playlists.keySet()) {
            if (!playlistName.equals("Liked Songs")) {
                playlistNames.add(playlistName);
            }
        }

        for (int i = 0; i < playlistNames.size(); i++) {
            System.out.println("[" + (i + 1) + "]" + playlistNames.get(i));
        }
        System.out.println("Select a playlist:");
        int playlistChoice = userInput.nextInt();
        userInput.nextLine();
        String selectedPlaylistName = playlistNames.get(playlistChoice - 1);
        viewPlaylist(playlists, selectedPlaylistName, playlistFile, userInput);

        if (playlistChoice < 1 || playlistChoice > playlistNames.size()) {
            System.out.println("Invalid playlist selection");
            return;
        }
    }

    public static String selectPlaylist(Scanner userInput, HashMap<String, ArrayList<String>> playlists) {
        ArrayList<String> playlistNames = new ArrayList<String>();
        playlistNames.add("Liked Songs");
        for (String playlistName : playlists.keySet()) {
            if (!playlistName.equals("Liked Songs")) {
                playlistNames.add(playlistName);
            }
        }

        for (int i = 0; i < playlistNames.size(); i++) {
            System.out.println("[" + (i + 1) + "]" + playlistNames.get(i));
        }
        System.out.println("Select a playlist:");
        int playListChoice = userInput.nextInt();
        userInput.nextLine();

        if (playListChoice < 1 || playListChoice > playlistNames.size()) {
            System.out.println("Invalid playlist selection.");
            return null;
        }

        return playlistNames.get(playListChoice - 1);
    }

    public static void viewPlaylist(HashMap<String, ArrayList<String>> playlists, String playlistName,
            File playlistFile, Scanner userInput) {
        ArrayList<String> selectedPlaylist = playlists.get(playlistName);

        if (selectedPlaylist == null) {
            System.out.println("Playlist not found :(");
            return;
        }
        boolean viewingPlaylist = true;

        while (viewingPlaylist) {

            for (int i = 0; i < selectedPlaylist.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + selectedPlaylist.get(i));
            }
            System.out.println("\n1. Sort By");
            System.out.println("2. Delete a Song");
            System.out.println("3. Go Back");
            System.out.println("Please select an option: ");

            int playlistAction = userInput.nextInt();
            userInput.nextLine();

            switch (playlistAction) {
                case 1:
                    System.out.println("\nSort By:");
                    System.out.println("1. Title - Ascending");
                    System.out.println("2. Title - Descending");
                    System.out.println("3. Artist - Ascending");
                    System.out.println("4. Artist - Descending");
                    System.out.println("5. Album - Ascending");
                    System.out.println("6. Album - Descending");
                    System.out.println("7. Genre - Ascending");
                    System.out.println("8. Genre - Descending");
                    System.out.println("9. Go Back");
                    System.out.println("Please select an option: ");

                    int sortChoice = userInput.nextInt();
                    userInput.nextLine();

                    switch (sortChoice) {

                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            sortPlaylist(selectedPlaylist, sortChoice);
                            savePlaylist(playlistFile, playlists);
                            break;

                        case 9:
                            break;

                        default:
                            System.out.println("Invalid response.");
                            break;
                    }

                     break;

                case 2:
                    deleteSong(playlists, playlistName, playlistFile, userInput);
                    break;

                case 3:
                    viewingPlaylist = false;
                    break;

                default:
                    System.out.println("Invalid response.");
                    break;
            }
        }
    }

    public static void sortPlaylist(
            ArrayList<String> selectedPlaylist,
            int sortChoice) {

        int fieldIndex = 0;

        // Determine which part of the song we are sorting by
        switch (sortChoice) {
            case 1:
            case 2:
                fieldIndex = 0; // Title
                break;

            case 3:
            case 4:
                fieldIndex = 1; // Artist
                break;

            case 5:
            case 6:
                fieldIndex = 2; // Album
                break;

            case 7:
            case 8:
                fieldIndex = 3; // Genre
                break;
        }

        // Odd choices are ascending, even choices are descending
        boolean ascending = sortChoice % 2 != 0;

        // Insertion Sort
        for (int i = 1; i < selectedPlaylist.size(); i++) {

            String currentSong = selectedPlaylist.get(i);
            String[] currentSongData = currentSong.split("\\|");
            String currentValue = currentSongData[fieldIndex];

            int j = i - 1;

            while (j >= 0) {

                String previousSong = selectedPlaylist.get(j);
                String[] previousSongData = previousSong.split("\\|");
                String previousValue = previousSongData[fieldIndex];

                int comparison = previousValue.compareToIgnoreCase(currentValue);

                if ((ascending && comparison > 0) ||
                        (!ascending && comparison < 0)) {

                    selectedPlaylist.set(j + 1, previousSong);
                    j--;

                } else {
                    break;
                }
            }

            selectedPlaylist.set(j + 1, currentSong);
        }
    }

    public static void addToPlaylist(File playlistFile, songObj chosenSong, String playlistName,
            HashMap<String, ArrayList<String>> playlists) {// Adds songs to Liked songs playlist
        ArrayList<String> selectedPlaylist = playlists.get(playlistName);
        if (selectedPlaylist == null) {
            System.out.println("Playlist not found:(");
            return;
        }
        String songData =
        chosenSong.getTitle() + "|" +
        chosenSong.getArtist() + "|" + 
        chosenSong.getAlbum() + "|" +
        chosenSong.getGenre() + "|" +
        chosenSong.getdurationMs();

        selectedPlaylist.add(songData);
        savePlaylist(playlistFile, playlists);

        System.out.println(chosenSong.getTitle() + " was added to " + playlistName);
    }

    public static void deleteSong(HashMap<String, ArrayList<String>> playlists, String playlistName, File playlistFile,
            Scanner userInput) {
        ArrayList<String> selectedPlaylist = playlists.get(playlistName);

        System.out.println("Select a song to delete: ");
        int userChoice = userInput.nextInt();
        userInput.nextLine();

        if (userChoice < 1 || userChoice > selectedPlaylist.size()) {
            System.out.println("Invalid song selection");
            return;
        }
        String removedSong = selectedPlaylist.remove(userChoice - 1);

        savePlaylist(playlistFile, playlists);

        System.out.println(removedSong + " was deleted from " + playlistName);
    }

    public static void mainMenu(Scanner userInput, ArrayList<songObj> songList,
            HashMap<String, ArrayList<String>> playlists, File playlistFile) {// Handles main menu logic
        boolean programRunning = true;

        while (programRunning) {
            System.out.println("Welcome to BetterSpotify!");
            System.out.println("1. Recommended Search");
            System.out.println("2. My Playlists");
            System.out.println("3. Exit");
            System.out.println("Please select an option: ");
            int userOption = userInput.nextInt();
            userInput.nextLine(); // Consume the newline character left by nextInt()
            switch (userOption) {
                case 1:
                    reccomendedSearch(songList, userInput, playlists, playlistFile);
                    break;
                case 2:
                    playlistMenu(userInput, playlists, playlistFile);
                    break;
                case 3:
                    System.out.println("Goodbye bitch!");
                    programRunning = false;
                    break;
                default:
                    System.out.println("Invalidd response, Please try again.");
            }
        }
    }

    public static void reccomendedSearch(ArrayList<songObj> songList, Scanner userInput,
            HashMap<String, ArrayList<String>> playlists, File playlistFile) {// This method is for the
        // reccomended searching
        // algorithm
        System.out.println("Pleaase enter the title of a song you would like to search for: ");
        String userTitle = userInput.nextLine();
        // ============================================================= Title Search
        // =========================================

        songObj foundSong = null;
        for (songObj currentSong : songList) {
            if (currentSong.getTitle().equalsIgnoreCase(userTitle)) {
                foundSong = currentSong;
                break;
            }

        }
        // =========================================== Check if Song Found AND match
        // genres once it is ===================================

        if (foundSong != null) { // Check whether the Title Search was successful

            System.out.println("Song found!");
            System.out.println("Title: " + foundSong.getTitle());
            System.out.println("Artist: " + foundSong.getArtist());
            System.out.println("Genre: " + foundSong.getGenre());

            ArrayList<songObj> matchingGenre = new ArrayList<songObj>(); // Creates an EMPTY LIST that will hold all
                                                                         // songs of the same genre as input song
            for (songObj currentSong : songList) { // loops through every songObj in the full CSV
                if (currentSong.getGenre().equalsIgnoreCase(foundSong.getGenre())) { // Compares genres of our "pointer"

                    boolean sameTitle = currentSong.getTitle().equalsIgnoreCase(foundSong.getTitle()); // compares Input
                                                                                                       // song's title
                                                                                                       // to every other
                                                                                                       // song's title

                    boolean sameArtist = currentSong.getArtist().equalsIgnoreCase(foundSong.getArtist()); // compares
                                                                                                          // Input
                                                                                                          // song's
                                                                                                          // artist to
                                                                                                          // every other
                                                                                                          // song's
                                                                                                          // artist

                    if (!(sameTitle && sameArtist)) {
                        matchingGenre.add(currentSong); // Adds a REFERENCE copy of the songObj to pool of potential
                                                        // recommendations
                    }
                }
            }

            // ============================================= Generating Recommendations
            // =============================================================================

            ArrayList<songObj> recommendations = new ArrayList<songObj>(); // This is where we will store oure most
                                                                           // similar songs that get recommended to the
                                                                           // user

            ArrayList<Double> distances = new ArrayList<Double>(); // Each song has a "distance" relative to our user's
                                                                   // song foundSong. and those distances will be stored
                                                                   // her for comparrison later

            // ==================================== Determining how similar each song in
            // <matchingGenre> to our input song ======================================

            for (songObj currentSong : matchingGenre) { // SEARCH NODE will parse through the newly made list of songs
                                                        // that match the genre of userSong
                double danceDifference = foundSong.getDanceability() - currentSong.getDanceability(); // Calculate the
                                                                                                      // difference in
                                                                                                      // danceability
                                                                                                      // between
                                                                                                      // foundSong and
                                                                                                      // each song in
                                                                                                      // our genre list
                double energyDifference = foundSong.getEnergy() - currentSong.getEnergy(); // Calculate the difference
                                                                                           // in energies between
                                                                                           // energies of userSong and
                                                                                           // each song in our genre
                                                                                           // list
                double distance = Math
                        .sqrt((danceDifference * danceDifference) + (energyDifference * energyDifference)); // Were
                                                                                                            // using the
                                                                                                            // DISTANCE
                                                                                                            // EQUATION
                                                                                                            // to
                                                                                                            // determine
                                                                                                            // similarity
                                                                                                            // of songs

                // ======================================================= Caps the size of
                // recommendations to FOUR ======================================

                if (recommendations.size() < 4) { // We are checking if our recommendations vector is full yet
                    recommendations.add(currentSong); // Stores current candidate song
                    distances.add(distance); // Stores the current candidate song's calculated distance
                }

                // ======================================================= Find and replace the
                // most "distant" song in recommendations ===========================

                else { // This will run once Recommendations List if full
                    int highestDistanceIndex = 0; // We're gonna sort through <distances> to find which has the largest
                                                  // distance (least similar)

                    for (int i = 1; i < distances.size(); i++) { // start working through the list
                        if (distances.get(i) > distances.get(highestDistanceIndex)) { // Directly compares the current
                                                                                      // loser to the current [i] index
                            highestDistanceIndex = i; // Marks current index as the LOSER if it has a larger distance
                        }
                    }

                    if (distance < distances.get(highestDistanceIndex)) { // now we're comparing new candidates to our
                                                                          // current LOSER song
                        recommendations.set(highestDistanceIndex, currentSong); // Replace the LOSER song if new
                                                                                // candidate has a lower distance (is
                                                                                // more similar) int the
                                                                                // <recommendations> list
                        distances.set(highestDistanceIndex, distance); // Replace the LOSER song if new candidate has a
                                                                       // lower distance (is more similar) int the
                                                                       // <distances> list
                    }

                }
            }
            // ============================================================== TEMP TEST
            // ========================================================================================

            System.out.println("\nThese songs are similar to " + foundSong.getTitle());

            for (int i = 0; i < recommendations.size(); i++) {
                songObj similarToFoundSong = recommendations.get(i); // Transfers properties of each song in
                                                                     // <recommendations> to a temporary songObj

                System.out.println(
                        (i + 1) + ". " + similarToFoundSong.getTitle() + "- " + similarToFoundSong.getArtist());
            }

            // ============================================================== Choosing on of
            // the recommendations
            // ==================================================================

            System.out.println("\nChoose a song to add to \"My Liked Songs Playlist\" by tying 1-4: "); // Ask user to
                                                                                                        // select one
                                                                                                        // song

            // =============================================================== Error
            // Handling of user input
            // =========================================================
            int userChoice;

            while (true) { // This will repeat until a valid input is given (int 1-4)

                if (userInput.hasNextInt()) { // check for INTEGER

                    userChoice = userInput.nextInt(); // Reads the integer

                    if (userChoice >= 1 && userChoice <= 4) { // Checks that integer is within valid range
                                                              // <recommendations>

                        break; // A valid input has been input

                    }
                }

                else { // Loops immediately if input is not an integer

                    userInput.next(); // clears the invalid input
                }

                System.out.println("Invalid input. Please enter a number from 1-4."); // warning of invalid input
            }

            songObj chosenSong = recommendations.get(userChoice - 1); // creates a songObj based on the index of
                                                                      // userChoise input

            System.out.println("You chose: " + chosenSong.getTitle() + " - " + chosenSong.getArtist()); // Displays the
                                                                                                        // song chosen
                                                                                                        // by the user
            String selectedPlaylistName = selectPlaylist(userInput, playlists);
            if (selectedPlaylistName != null) {
                addToPlaylist(playlistFile, chosenSong, selectedPlaylistName, playlists);
            }
        } else { // foundSong remained NULL meaning no matching title was found
            System.out.println("Song not found :( ");
        }
    }
}