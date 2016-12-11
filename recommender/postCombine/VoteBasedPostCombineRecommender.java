/**
 * 
 */
package recommender.postCombine;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import outputWriter.Printer;
import data.UserCheckin;

/**
 * @author mg
 *
 */
public class VoteBasedPostCombineRecommender extends PostCombineRecommender{
	
	public VoteBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * Perform user combination by using votes, such that
	 * if a user is suggested by many methods, it is more probable that it is a neighbour
	 */
	protected ArrayList<UserCheckin> combineSimUsers(List<List<UserCheckin>> simUserList, 
			UserCheckin targetUser, Connection con) {
		// map users to #being-suggested 
		HashMap<UserCheckin,Integer> userSuggestionMap = new HashMap<UserCheckin, Integer>();
		
		// get sim users found by each method
		for(List<UserCheckin> suggestionList: simUserList){
			// perform analysis for each suggested user
			for(UserCheckin simUser:suggestionList){
				if(userSuggestionMap.containsKey(simUser)){
					// already in map, just increment count
					Integer newVal = userSuggestionMap.get(simUser) + 1;
					userSuggestionMap.put(simUser, newVal);
				} else{
					// a new user is suggested!!
					userSuggestionMap.put(simUser, 1);
				}
			}
		}
		
		// sort map in terms of count of suggestions!!
		// the previous map is soreted by userscheckin, just cretae the reverse:)
		Map<Integer,List<UserCheckin>> userSuggestCountMap = new TreeMap<Integer, List<UserCheckin>>(Collections.reverseOrder());
		for(Entry<UserCheckin, Integer> e:userSuggestionMap.entrySet()){
			if(userSuggestCountMap.containsKey(e.getValue())){
				// add the user to the related list
				List<UserCheckin> users = userSuggestCountMap.get(e.getValue());
				users.add(e.getKey());
				userSuggestCountMap.put(e.getValue(), users);
			} else{
				// a new count value 
				List<UserCheckin> users = new ArrayList<UserCheckin>();
				users.add(e.getKey());
				userSuggestCountMap.put(e.getValue(), users);
			}
		}
		
		// get the top-N users from the sorted list as neighbours	
		ArrayList<UserCheckin> neighbours = new ArrayList<UserCheckin>();
		for(Entry<Integer, List<UserCheckin>> e:userSuggestCountMap.entrySet()){
			// collect neighbours
			if(neighbours.size() + e.getValue().size() <= numberOfSimilarUsers){
				// add all users
				neighbours.addAll(e.getValue());
			} else {
				// add only first n of them 
				for(UserCheckin user:e.getValue()){
					if(neighbours.size() < numberOfSimilarUsers){
						neighbours.add(user);
					} else{
						break;
					}
				}
			}
		}
		
		
		return neighbours;
	}

}
