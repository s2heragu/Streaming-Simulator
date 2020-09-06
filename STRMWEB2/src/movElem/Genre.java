package movElem;

public class Genre {

	//genre name
	private String Genre;
	
	//first movie in list
	public Movie front;
	
	//size of genre
	private int size;
	
	//prevents instantiation
	public Genre() {
		
	}
	
	//correct instantiation
	public Genre(String movie, String genre){
		Genre = genre;
		if(movie!=null && movie.length()!=0) {
			front = new Movie(movie,genre,null);
			size = 1;
		}
		else {
			front = null;
			size = 0;
		}
	}
	
	//adding a movie to the genre
	public void addMovie(String movie) {
		front = new Movie(movie,Genre,front);
		size++;
	}
	
	//adding a movie to the genre
	public void addMovie(String movie, String genre) {
		front = new Movie(movie,genre,front);
		size++;
	}
	
	//sees if genre is empty
	public boolean isEmpty() {
		if(size==0) {
			return true;
		}
		return false;
	}
	
	//returns genre name
	public String genre() {
		return Genre;
	}
	
	//returns number of movies in genre
	public int size() {
		return size;
	}
	
	//to string method
	public String toString() {
		if(isEmpty()) {
			return "No movies found.";
		}
		Movie ptr = front;
		String retVal = "";
		String G = Genre.toUpperCase();
		retVal += G + "\n";
		int i = 1;
		while(ptr!=null) {
			retVal += i + ". " + ptr.toString()+"\n";
			ptr = ptr.next;
			i++;
		}
		
		return retVal;
	}
	
}

