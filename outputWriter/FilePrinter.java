package outputWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.xml.stream.Location;

import data.Rating;
import data.Recommendation;
import data.Similarity;
import data.User;

public class FilePrinter extends Printer{

	public FilePrinter(boolean debugVal) {
		super(debugVal,PrinterType.FILEPRINTER);
	}
	
	public void printUsers(String path, ArrayList<User> users) throws FileNotFoundException {
		//print users list to file
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		for(User u:users){
			ps.println(u.toString());
		}
		
		ps.flush();
		ps.close();
	}
	
	public void printUser(String path, User u) throws FileNotFoundException {
		//print users list to file
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		ps.println(u.toString());
		
		ps.flush();
		ps.close();
	}

	public void printFriends(String path, HashMap<Integer, ArrayList<Integer>> friends) throws FileNotFoundException {
		//print friends list to file
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		for(java.util.Map.Entry<Integer, ArrayList<Integer>> e:friends.entrySet()){
			ps.print(e.getKey() + ": ");
			for(Integer fId: e.getValue())
			{
				ps.print(fId+", ");
			}
			ps.println();
		}
		
		ps.flush();
		ps.close();
	}


	public void printLocations(String path, ArrayList<Location> locations) throws FileNotFoundException {
		//print location list to file
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		for(Location l:locations){
			ps.println(l.toString());
		}
		
		ps.flush();
		ps.close();
	}

	public void printRatings(String path, ArrayList<Rating> ratings) throws FileNotFoundException
	{
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		for(Rating r: ratings)
		{
			ps.println(r.getUserId()+","+
					   r.getSubject().toString()+","+
					   r.getRating()
					);
		}
		
		ps.flush();
		ps.close();
	}
	
	public void printRecommendations(String path, HashMap<User, ArrayList<Recommendation>> itemRecProb) throws FileNotFoundException
	{
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		for(Entry<User, ArrayList<Recommendation>> rec:itemRecProb.entrySet())
		{
			ps.println(rec.getKey().getUserId()+": ");
			
			for(Recommendation rec2:rec.getValue())
			{
				ps.println(rec2.toString());
			}
			ps.println("------------------------------------------------------------");
			
		}
		
		ps.flush();
		ps.close();
	}

	

	public void printString(String path, String string) throws FileNotFoundException
	{
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		ps.println(string);
		
		ps.flush();
		ps.close();
	}
	
	public void printUsersIds(String path, User user, ArrayList<Integer> similarUsers) throws FileNotFoundException
	{
		//print users list to file
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);

		ps.println(user.getUserId()+":");
		for(Integer u:similarUsers){
			ps.print(u.toString()+", ");
		}
		ps.println("\n-------------------------------");

		ps.flush();
		ps.close();
	}
	
	@Override
	public void printUserSimilarities(String path, User u) throws FileNotFoundException {
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		ps.println(u.getUserId());
		for(Similarity s:u.getSimilarities().values())
		{
			ps.println(s.toString());
		}
		ps.println("\n-------------------------------");
		
		ps.flush();
		ps.close();
	}

	@Override
	public void printSimilarities(String path, User user,
			ArrayList<Similarity> similarities) throws FileNotFoundException {
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		ps.println(user.getUserId());
		for(Similarity s:similarities)
		{
			ps.println(s.toString());
		}
		ps.println("\n-------------------------------");
		
		ps.flush();
		ps.close();
	}

	@Override
	public void printPrecisionEvalResult(String path, HashSet<Integer> userIdList, HashMap<Integer, Double> userPrecMap,
			HashMap<Integer, Double> userTPMap, HashMap<Integer, Double> userFPMap) throws FileNotFoundException {

		//print users list to file
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);

		
		for(Integer userid:userIdList){
			Double truePos = userTPMap.get(userid);
			Double falsePos = userFPMap.get(userid);
			Double precision = userPrecMap.get(userid);
			String str = "userid: " + userid 
					+ " tp: " + truePos 
					+ " fp: " + falsePos
					+ " prec: " + precision;
			ps.println(str.toString());
		}

		ps.flush();
		ps.close();

	}

}
