package net;
import java.io.File;
import java.io.IOException;
import java.util.*;
import sort.*;
import movElem.*;

public class Streamer {

	private HashMap<String,Genre>genres;
	private HashMap<String,Account>accounts;
	
	//default constructor
	public Streamer () {
		genres = new HashMap<String,Genre>();
		genres.put("all",new Genre(null,"all"));
		accounts = new HashMap<String,Account>();
	}
	
	//constructor with input filename to "load" streamer
	public Streamer(String filename) throws IOException{
		genres = new HashMap<String,Genre>();
		genres.put("all",new Genre(null,"all"));
		accounts = new HashMap<String,Account>();
		Scanner scan = new Scanner(new File(filename));
		int numMovies = Integer.parseInt(scan.nextLine().trim());
		for (int i=0; i < numMovies; i++) {
			String s = scan.nextLine().trim();
			String [] stuff = s.split(";");
			String name = stuff[0];
			String genr = stuff[1];
			int streams = Integer.parseInt(stuff[2]);
			double rating = Integer.parseInt(stuff[3]);
			add(name, genr);
			Movie movie = SearchMovie(name, genr);
			MassStream(movie, streams, false);
			int reviews = Integer.parseInt(stuff[4]);
			MassCritique(movie, rating, reviews, false);
		}
		int numAcc = Integer.parseInt(scan.nextLine().trim());
		for(int j = 0;j<numAcc;j++) {
			String d = scan.nextLine().trim();
			Account acc = new Account(d);
			reAddAccount(acc);
		}
	}
	
	//adds an account given a username and password
	public void addAccount(String username, String password) throws IllegalArgumentException{
		Account a = accounts.get(username);
		if(a!=null) {
			throw new IllegalArgumentException();
		}
		a = new Account(username,password);
		accounts.put(username,a);
	}
	
	//relaying account information to text form to store streamer data
	public ArrayList<ArrayList<String>> ACCStxt(){
		if(accounts.isEmpty()) {
			return null;
		}
		ArrayList<String>nms = new ArrayList<String>();
		ArrayList<String>txt = new ArrayList<String>();
		for(Account value: accounts.values()) {
			nms.add(value.Username());
			txt.add(accTXT(value));
		}
		ArrayList<ArrayList<String>>out = new ArrayList<ArrayList<String>>();
		out.add(nms);
		out.add(txt);
		return out;
	}
	
	//relaying account information to text form to store streamer data
	public String accTXT(Account acc) {
		String retVal = "";
		retVal+=acc.Username()+"\n";
		retVal+=acc.Password()+"\n";
		retVal+=acc.Movies();
		if(acc.Movies()!=0) {
			Movie ptr = acc.frontStream;
			while(ptr!=null) {
				String temp = ptr.Name()+";"+ptr.genre()+";"+ptr.Streams();
				retVal += "\n"+temp;
				ptr = ptr.next;
			}
		}
		return retVal;
	}
	
	//relays movie information to text form to store streamer data
	public String moviesTXT() {
		Genre g = genres.get("all");
		if(g.size()==0) {
			return "0";
		}
		String retVal = "";
		retVal += g.size();
		Movie ptr = g.front;
		while(ptr!=null) {
			retVal += "\n";
			retVal += ptr.Name()+";"+ptr.genre()+";"+ptr.Streams()+";"+ptr.strScore()+";"+ptr.reviews();
			ptr = ptr.next;
		}
		return retVal;
	}
	
	//re-adding account: used for changing username
	public void reAddAccount(Account acc) {
		accounts.put(acc.Username(),acc);
	}
	
	//accessing an account given a username; returns null if there is no such account
	public Account getAccount(String username) {
		Account a = accounts.get(username);
		if(a==null) {
			return null;
		}
		return a;
	}
	
	//deletes account
	public void deleteAccount(String username) {
		accounts.remove(username);
	}
	
	//adds a movie to the streamer
	public void add(String movie, String genre) 
			throws IllegalArgumentException{
		if(movie == null || movie.length() == 0 || genre == null || genre.length()==0) {
			throw new IllegalArgumentException("Invalid movie name.");
		}
		String g = genre.toLowerCase();
		String m = movie.toUpperCase();
		if(g.equals("all")) {
			throw new IllegalArgumentException("No such genre.");
		}
		Genre G = genres.get(g);
		if(G == null) {
			genres.put(g,new Genre(m,g));
			G = genres.get(g);
		}
		else{
			Movie ptr = G.front;
			while(ptr!=null) {
				if(ptr.Name().equals(m)) {
					throw new IllegalArgumentException("Movie already exists.");
				}
				ptr = ptr.next;
			}
			G.addMovie(m);
		}
		Genre All = genres.get("all");
		All.addMovie(m,g);
	}
	
	//searches for a movie in the streamer given a movie and genre
	public Movie SearchMovie(String movie, String Genre) {
		String m = movie.toUpperCase();
		String g = Genre.toLowerCase();
		Genre G = genres.get(g);
		if(G==null) {
			return null;
		}
		Movie ptr = G.front;
		while(ptr!=null) {
			if(ptr.Name().equals(m)) {
				return ptr;
			}
			ptr = ptr.next;
		}
		
		return null;
	}
	
	public ArrayList<Movie> KeywordSearch(String keyword, String genre){
		String genr = genre;
		if(genre==null) {
			return null;
		}
		String gen = genr.toLowerCase();
		if(genres.get("all").isEmpty()||genres.get(gen)==null) {
			return null;
		}
		String KEY = keyword.toUpperCase();
		Movie ptr = genres.get(gen).front;
		ArrayList<Movie>out = new ArrayList<Movie>();
		while(ptr!=null) {
			if(ptr.Name().indexOf(KEY)!=-1) {
				out.add(ptr);
			}
			ptr = ptr.next;
		}
		if(out.isEmpty()) {
			return null;
		}
		return out;
	}
	
	//streams a movie
	public void Stream(Movie movie, boolean all){
		if(all) {
			movie.Stream();
			Movie dup = SearchMovie(movie.Name(),movie.genre());
			dup.Stream();
		}
		else {
			movie.Stream();
			Movie dup = SearchMovie(movie.Name(),"all");
			dup.Stream();
		}
	}
	
	//many movie streams at once
	public void MassStream(Movie movie, int big, boolean all) {
		if(all) {
			movie.massStream(big);
			Movie dup = SearchMovie(movie.Name(),movie.genre());
			dup.massStream(big);
		}
		else {
			movie.massStream(big);
			Movie dup = SearchMovie(movie.Name(),"all");
			dup.massStream(big);
		}
	}
	
	//critiques a movie
	public void Critique(Movie movie, double Score, boolean all) throws
	IllegalArgumentException{
		if(Score<0||Score>100) {
			throw new IllegalArgumentException("Invalid Score.");
		}
		if(all) {
			movie.review(Score);
			Movie dup = SearchMovie(movie.Name(),movie.genre());
			dup.review(Score);
		}
		else {
			movie.review(Score);
			Movie dup = SearchMovie(movie.Name(),"all");
			dup.review(Score);
		}
	}
	
	//many critiques at once
	public void MassCritique(Movie movie, double Score, int reviews, boolean all)throws
	IllegalArgumentException{
		if(Score<0||Score>100) {
			throw new IllegalArgumentException("Invalid Score.");
		}
		if(all) {
			for(int i = 0;i<reviews;i++) {
				movie.review(Score);
			}
			Movie dup = SearchMovie(movie.Name(),movie.genre());
			for(int i = 0;i<reviews;i++) {
				dup.review(Score);
			}
		}
		else {
			for(int i = 0;i<reviews;i++) {
				movie.review(Score);
			}
			Movie dup = SearchMovie(movie.Name(),"all");
			for(int i = 0;i<reviews;i++) {
				dup.review(Score);
			}
		}
	}
	
	//orders movies in streamer given an input parameter
	public ArrayList<Movie> Order(String g, int size, String param) {
		if((!(param.equals("p")||param.equals("r")))||size<=0) {
			return null;
		}
		String gr = g.toLowerCase();
		Genre genre = genres.get(gr);
		if(genre==null || genres.isEmpty()) {
			return null;
		}
		genre.front = Sort.mergeSort(genre.front,genre.size(),param);
		ArrayList<Movie>out = new ArrayList<Movie>();
		Movie ptr = genre.front;
		int i = size;
		while(ptr!=null && i>0) {
			out.add(ptr);
			ptr=ptr.next;
			i--;
		}
		return out;
	}
	
	public Genre returnAll() {
		Genre g = genres.get("all");
		return g;
	}
	
	public String returnStrGenre(String genre){
		
		String f = genre.toLowerCase();
		Genre g = genres.get(f);
		if(g==null) {
			return null;
		}
		return g.toString();
		
	}
	
	public boolean isEmpty() {
		Genre g = genres.get("all");
		if(g.size()==0) {
			return true;
		}
		return false;
	}
	
	public Genre returnGenre(String genre) {
		Genre g = genres.get(genre);
		if(g==null) {
			return null;
		}
		return g;
	}
}
