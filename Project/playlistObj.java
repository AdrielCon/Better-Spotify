import java.util.ArrayList; //allows program to create an arraylist for songObj's

public class playlistObj {
    

    private String playlistName; //name of the playlist
    private ArrayList<songObj> songs; //arraylist that will hold all the songs in the playlist

    public playlistObj(String playlistName) { //constructor for the playlistObj class
        this.playlistName = playlistName; //sets the name of the playlist
        this.songs = new ArrayList<songObj>(); //initializes the arraylist of songs
    }

    public String getPlaylistName() { //getter for the playlist name
        return playlistName; //returns the name of the playlist
    }

    public void addSong(songObj song) { //method to add a song to the playlist
        songs.add(song); //adds the song to the arraylist of songs
    }

    
    
}