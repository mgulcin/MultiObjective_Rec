package outputWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.xml.stream.Location;

import data.Rating;
import data.Recommendation;
import data.Similarity;
import data.User;

public class ConsolePrinter extends Printer{

	public ConsolePrinter(boolean debugVal) {
		super(debugVal,PrinterType.CONSOLEPRINTER);
	}

	public void printUsers(String path, ArrayList<User> users) {
		if(debugPrint == false)
		{
			return;
		}
		//print users list to console
		for(User u:users){
			System.out.println(u.toString());
		}
	}
	
	public void printUser(String path, User u) {
		if(debugPrint == false)
		{
			return;
		}
		//print users list to console
			System.out.println(u.toString());
		
	}

	public void printFriends(String path, HashMap<Integer, ArrayList<Integer>> friends) {
		if(debugPrint == false)
		{
			return;
		}
		//print friends list to console
		for(java.util.Map.Entry<Integer, ArrayList<Integer>> e:friends.entrySet()){
			System.out.print(e.getKey() + ": ");
			for(Integer fId: e.getValue())
			{
				System.out.print(fId+", ");
			}
			System.out.println();
		}
	}


	public void printLocations(String path, ArrayList<Location> locations) {
		if(debugPrint == false)
		{
			return;
		}
		//print location list to console
		for(Location l:locations){
			System.out.println(l.toString());
		}
	}

	public void printRatings(String path, ArrayList<Rating> ratings)
	{
		if(debugPrint == false)
		{
			return;
		}

		for(Rating r: ratings)
		{
			System.out.println(r.toString());
		}
	}


	public void printRecommendations(String path, 
			HashMap<User, ArrayList<Recommendation>> itemRecProb)
	{
		if(debugPrint == false)
		{
			return;
		}

		for(Entry<User, ArrayList<Recommendation>> rec:itemRecProb.entrySet())
		{
			System.out.println(rec.getKey().getUserId()+": ");
			for(Recommendation rec2:rec.getValue())
			{
				System.out.println(rec2.toString());
			}
			
			System.out.println("------------------------------------------------------------");
		}

		
	}

	
	public void printString(String path, String string)
	{
		System.out.println(string);
	}
	
	public void printUsersIds(String path, User user, ArrayList<Integer> similarUsers)
	{
		if(debugPrint == false)
		{
			return;
		}
		//print users list to console
		System.out.println(user.getUserId()+":");
		for(Integer u:similarUsers){
			System.out.print(u.toString()+", ");
		}
		
		System.out.println("\n-------------------------------");
	}

	@Override
	public void printUserSimilarities(String path, User u) {
		System.out.println(u.getUserId());
		
		for(Similarity s:u.getSimilarities().values())
		{
			System.out.println(s.toString());
		}
		System.out.println("\n-------------------------------");
	}

	@Override
	public void printSimilarities(String path, User user,
			ArrayList<Similarity> similarities) {
		System.out.println(user.getUserId());
		
		for(Similarity s:similarities)
		{
			System.out.println(s.toString());
		}
		System.out.println("\n-------------------------------");
		
	}

	@Override
	public void printPrecisionEvalResult(String path, HashSet<Integer> controlUserIdList,
			HashMap<Integer, Double> userPrecMap, HashMap<Integer, Double> userTPMap,
			HashMap<Integer, Double> userFPMap) {
		// TODO Auto-generated method stub
		
	}

	
}
