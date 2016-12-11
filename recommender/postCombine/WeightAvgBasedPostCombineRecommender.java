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
import recommender.RecommendationDBDecisionMaker;
import data.UserCheckin;

/**
 * @author mg
 *
 */
public class WeightAvgBasedPostCombineRecommender extends PostCombineRecommender{

	public WeightAvgBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * Perform user combination by calculating avg weights(similarities) of users, such that
	 * first calculate similarity averages for each user, 
	 * if a user is suggested by many methods, it is more probable that it is a neighbour
	 */
	protected ArrayList<UserCheckin> combineSimUsers(List<List<UserCheckin>> simUserList,
			UserCheckin targetUser, Connection con) {
		// map users to total weight 
		HashMap<UserCheckin,Double> userSuggestionMap = new HashMap<UserCheckin, Double>();
		
		// get sim users found by each method
		for(List<UserCheckin> suggestionList: simUserList){
			// perform analysis for each suggested user
			for(UserCheckin simUser:suggestionList){
				if(userSuggestionMap.containsKey(simUser)){
					// already in map, no need further calculations
					// we have already calculated avg in calculateAvgSimToTarget method
					
				} else{
					// a new user is suggested!!
					Double avgSim = calculateAvgSimToTarget(targetUser, simUser, con);
					userSuggestionMap.put(simUser, avgSim);
				}
			}
		}
		
		// sort map in terms of avg sim !!
		// the previous map is sorted by userscheckin, just create the reverse:)
		Map<Double,List<UserCheckin>> userSuggestCountMap = new TreeMap<Double, List<UserCheckin>>(Collections.reverseOrder());
		for(Entry<UserCheckin, Double> e:userSuggestionMap.entrySet()){
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
		for(Entry<Double, List<UserCheckin>> e:userSuggestCountMap.entrySet()){
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

	private Double calculateAvgSimToTarget(UserCheckin targetUser,
			UserCheckin simUser, Connection con) {
		Double avgSim = 0.0;
				
		// for each recType get related val and calculate avg
		Double totalSim = 0.0;
		for(RecType recType:recTypeList){
			ArrayList<String> simNameList = RecommendationDBDecisionMaker.getSimName(recType);
			if(simNameList.size() > 1){
				System.out.println("Error, you are using another hybrid system!!");
				System.exit(-1);
			} else{
				ArrayList<Double> vals = this.getSimilaritiesFromDB(targetUser,simUser,con, simNameList);
				Double val = vals.get(0);
				totalSim += val;
			}
		}
		avgSim = totalSim / recTypeList.size();
		
		return avgSim;
	}

}
