/**
 * 
 */
package outputWriter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import data.Rating;
import data.Recommendation;
import data.Similarity;
import data.User;

/**
 * @author mg
 *
 */
public abstract class Printer {
	public enum PrinterType
	{
		FILEPRINTER,
		CONSOLEPRINTER
	};
	
	// true --> print, false--> do not print
	protected boolean debugPrint = true;
	protected PrinterType type = null;
	
	
	/**
	 * @param debugPrint
	 * @param type
	 */
	public Printer(boolean debugPrint, PrinterType type ) {
		super();
		this.debugPrint = debugPrint;
		this.type = type;
	}
	
	public abstract void printUsers(String path, ArrayList<User> users) throws FileNotFoundException;
	public abstract void printUser(String path, User user) throws FileNotFoundException;
	public abstract void printFriends(String path, HashMap<Integer, ArrayList<Integer>> friends) throws FileNotFoundException;
	public abstract void printRatings(String path, ArrayList<Rating> ratings) throws FileNotFoundException;
	public abstract void printRecommendations(String path, HashMap<User, ArrayList<Recommendation>> itemRecProb) throws FileNotFoundException;
	public abstract void printString(String path, String string) throws FileNotFoundException;
	public abstract void printUsersIds(String string, User u, ArrayList<Integer> similarUsers) throws FileNotFoundException;
	public abstract void printUserSimilarities(String path, User u) throws FileNotFoundException;
	public abstract void printSimilarities(String path, User user, ArrayList<Similarity> similarities) throws FileNotFoundException;

	public abstract void printPrecisionEvalResult(String path, HashSet<Integer> controlUserIdList, HashMap<Integer, Double> userPrecMap,
			HashMap<Integer, Double> userTPMap, HashMap<Integer, Double> userFPMap) throws FileNotFoundException;
	}
