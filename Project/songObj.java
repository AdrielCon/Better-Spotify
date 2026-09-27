public class songObj { //class for a new dataType called a song
    private String title; //private class data value
    private String artist; //private class data value
    private String genre; //private class data value
    private String album; //private class data value
    private double popularity;
    private double danceability;
    private double energy;
    private double durationMs;

    public songObj ( String title, String artist, String genre, double danceability, double energy, double durationMs, String album) { //constructor method for class
        this.title = title; //input will be assigned to data value
        this.artist = artist;
        this.genre = genre;
        this.danceability = danceability;
        this.energy = energy;
        this.durationMs = durationMs;
        this.album = album;
       

    }
// Getter methods for each of the private data values in the songObj class
    public String getTitle() { 
        return title;
    }
     public String getArtist() { 
        return artist;
    }
    public String getGenre() { 
        return genre;
    }
    public String getAlbum() { 
        return album;
    }
    public double getPopularity() {
        return popularity;
    }
    public double getDanceability() {
        return danceability;
    }
    public double getEnergy() {
        return energy;
    }
    public String getdurationMs() {
        int totalSeconds = (int) (durationMs / 1000);
    int minutes = totalSeconds / 60;
    int seconds = totalSeconds % 60;

    return String.format("%d:%02d", minutes, seconds);
    }
}
