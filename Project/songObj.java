public class songObj { //class for a new dataType called a song
    private String title; //private class data value
    private String artist; //private class data value
    private String genre; //private class data value
    private String album; //private class data value
    private double popularity;
    private double danceability;
    private double energy;
    private double acousticness;
    private double instrumentalness;
    private double valence;
    private double tempo;
    public songObj ( String title, String artist, String genre, String album, double popularity, double danceability, double energy, double acousticness, double instrumentalness, double valence, double tempo) { //constructor method for class
        this.title = title; //input will be assigned to data value
        this.artist = artist;
        this.genre = genre;
        this.album = album;
        this.popularity = popularity;
        this.danceability = danceability;
        this.energy = energy;
        this.acousticness = acousticness;
        this.instrumentalness = instrumentalness;
        this.valence = valence;
        this.tempo = tempo;

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
    public double getAcousticness() { 
        return acousticness;
    }
    public double getInstrumentalness() {
        return instrumentalness;
    }
    public double getValence() {
        return valence;
    }
    public double getTempo() { 
        return tempo;
    }
}
