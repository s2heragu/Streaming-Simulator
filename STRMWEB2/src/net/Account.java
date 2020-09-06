package net;

import java.io.File;
import java.io.IOException;
import java.util.*;
import sort.*;
import movElem.*;
public class Account {

	private String username;
	private String password;
	private int streams;
	private int movies;
	private HashMap<String,Movie> Streamed;
	Movie frontStream;
	
	public Account(String user, String pass) {
		username = user;
		password = pass;
		Streamed = new HashMap<String,Movie>();
		frontStream = null;
		streams = 0;
		movies = 0;
	}
	
	public Account(String filename) throws IOException{
		Scanner scans = new Scanner(new File(filename));
		String us = scans.nextLine().trim();
		String pa = scans.nextLine().trim();
		username = us;
		password = pa;
		Streamed = new HashMap<String,Movie>();
		frontStream = null;
		streams = 0;
		movies = 0;
		int movs = Integer.parseInt(scans.nextLine().trim());
		for(int i = 0;i<movs;i++) {
			String[]stuffs = scans.nextLine().trim().split(";");
			Movie mov = new Movie(stuffs[0],stuffs[1],null);
			int ind = Integer.parseInt(stuffs[2]);
			setUpStream(mov, ind);
		}
	}
	
	public void Stream(Streamer stream, Movie movie, boolean all) {
		Movie mov = Streamed.get(movie.Name());
		if(mov==null) {
			frontStream = new Movie(movie.Name(),movie.genre(),frontStream);
			mov = frontStream;
			Streamed.put(mov.Name(),mov);
			movies++;
		}
		stream.Stream(movie, all);
		mov.Stream();
		streams++;
	}
	
	public void setUpStream(Movie movie, int strms) {
		Movie mov = Streamed.get(movie.Name());
		if(mov==null) {
			frontStream = new Movie(movie.Name(),movie.genre(),frontStream);
			mov = frontStream;
			Streamed.put(mov.Name(),mov);
			movies++;
		}
		mov.massStream(strms);
		streams+=strms;
	}
	
	public void ChangeUsername(String currPassword, String newUsername) throws IllegalArgumentException{
		if(!currPassword.equals(password)) {
			throw new IllegalArgumentException();
		}
		username = newUsername;
	}
	
	public void ChangePassword(String currPassword, String newPassword) throws IllegalArgumentException{
		if(!currPassword.equals(password)) {
			throw new IllegalArgumentException();
		}
		password = newPassword;
	}
	
	public ArrayList<ArrayList<Movie>> favorites(Streamer stream){
		if(frontStream==null) {
			return null;
		}
		frontStream = Sort.mergeSort(frontStream,movies,"p");
		Movie ptr = frontStream;
		ArrayList<Movie>favs = new ArrayList<Movie>();
		ArrayList<String>favNames = new ArrayList<String>();
		ArrayList<Movie>recommended = new ArrayList<Movie>();
		ArrayList<String>recNames = new ArrayList<String>();
		int i = 0;
		
		while(ptr!=null && i<3) {
			Movie mov = stream.SearchMovie(ptr.Name(), ptr.genre());
		    favs.add(mov);
		    favNames.add(mov.Name());
		    ptr = ptr.next;
		    i++;
		}
		
		int j = 0;
		
		while(j<3 && j<favs.size()) {
			Movie mov = favs.get(j);
			Genre g = stream.returnGenre(mov.genre());
			Movie pt = g.front;
			while (pt!=null) {
				if(!favNames.contains(pt.Name()) && !recNames.contains(pt.Name())){
					break;
				}
				pt = pt.next;
			}
			if(pt!=null) {
				recommended.add(pt);
				recNames.add(pt.Name());
			}
			j++;
		}
		ArrayList<ArrayList<Movie>>out = new ArrayList<ArrayList<Movie>>();
		out.add(favs);
		out.add(recommended);
		return out;
	}
	
	public String Password() {
		return password;
	}
	
	public int Movies() {
		return movies;
	}
	
	public int Streams() {
		return streams;
	}
	
	public String Username() {
		return username;
	}
	
	public String print(Streamer stream) {
		String retval = "";
		retval += "PROFILE: \n\n";
		retval += "Username: " + username + "\n";
		if(stream.isEmpty()) {
			retval += "\n";
			retval += "No streams. \n\n";
			retval += "No favorites. + \n\n";
			retval += "No recommended movies.\n\n";
			return retval;
		}
		ArrayList<ArrayList<Movie>>faves = favorites(stream);
		if(faves==null) {
			retval += "\n";
			retval += "No streams. \n\n";
			retval += "No favorites.\n\n";
			retval += "No recommended movies.\n\n";
			return retval;
		}
		retval += "\n";
		retval += "Streams: " + streams + "\n\n";
		retval += "Favorites: ";
		int i = 0;
		while(i<3 && i<faves.get(0).size()) {
			int j = i+1;
			retval += "\n";
			retval += j+". " + faves.get(0).get(i).toString();
			i++;
		}
		if(faves.get(1).isEmpty()) {
			retval += "\n\n";
			retval += "No recommended movies.\n\n";
			return retval;
		}
		retval += "\n\n";
		retval += "Recommended for you: ";
		int k = 0;
		while(k<3 && k<faves.get(1).size()) {
			int j = k+1;
			retval += "\n";
			retval += j+". " + faves.get(1).get(k).toString();
			k++;
		}
		retval+="\n\n";
		return retval;
		
	}
	
	
}
