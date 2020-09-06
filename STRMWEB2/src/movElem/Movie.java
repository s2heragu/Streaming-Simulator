package movElem;

public class Movie {

	//name of the movie
	private String name;
	
	//genre of the movie
	private String genre;
	
	//number of times Movie has been streamed
	private int streams;
	
	//number of reviews given on the movie
	private int reviews;
	
	//score of the movie out of 1
	private double score;
	
	//star rating out of 5 stars
	private String rating;
	
	//next movie in linked-list
	public Movie next;
	
	public Movie() {
		//prevent instantiation
	}
	
	//correct instantiation
	public Movie(String Name, String Genre, Movie Next) {
		name = Name;
		genre = Genre;
		reviews = 0;
		score = 0;
		streams = 0;
		rating = "- - - - -";
		next = Next;
	}
	
	public void review(double Score) {
		//updating percentage score and reviews accordingly
		score = (reviews*score+Score/100)/(reviews+1);
		reviews++;
		
		if(score>=0.88 && score<=1) {
			rating = "* * * * *";
		}
		else if(score>=0.74 && score<0.88) {
			rating = "* * * *";
		}
		else if(score>=0.54 && score<0.74) {
			rating = "* * *";
		}
		else if(score>=0.34 && score<0.54) {
			rating = "* *";
		}
		else if(score>=0.20 && score<0.34) {
			rating = "*";
		}
		else if(score<0.2) {
			rating = "-";
		}
	}
	
	//returns movie name
	public String Name() {
		return name;
	}
	
	//large number of streams instantaneously
	public void massStream(int large) {
		streams+=large;
	}
	
	//records when someone streams this movie
	public void Stream() {
		streams++;
	}
	
	//returns genre
	public String genre() {
		return genre;
	}
	
	//returns number of reviews
	public int reviews() {
		return reviews;
	}
	
	//returns the "quality" of the movie
	public String rating() {
		return rating;
	}
	
	//returns fraction score of the movie
	public double Score() {
		return score;
	}
	
	//returns number of streams
	public int Streams() {
		return streams;
	}
	
	//returns the score of the movie as a percentage
	public String strScore() {
		int Score = (int)(score*100);
		return "" + Score;
	}
	
	public String toString() {
		return "Name: " + name + "; Genre: " + genre + "; Score: " + strScore()
		+ "%; Streams: " + streams + "; Rating: " + rating();
	}
	
}

