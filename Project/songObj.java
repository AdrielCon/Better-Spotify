public class songObj { //class for a new dataType called a song
    private String title; //private class data value
    private String artist; //private class data value
    private String genre; //private class data value
    public songObj ( String title, String artist, String genre) { //constructor method for class
        this.title = title; //input will be assigned to data value
        this.artist = artist;
        this.genre = genre; 

    }

    public String getTitle() { //GETTER METHOD for retrieveing a songObj's title
        return title;
    }
     public String getArtist() { //GETTER METHOD for retrieveing a songObj's Artist
        return artist;
    }
    public String getGenre() { //GETTER METHOD for retrieveing a songObj's genre
        return genre;
    }
}
