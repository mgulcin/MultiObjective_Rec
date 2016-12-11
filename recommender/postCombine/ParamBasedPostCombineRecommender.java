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
 * Inspired from Ye et al. 2011 paper!! Refere back to it if necessary,
 *  Fusion of criteria by params: Score = Total(alpha_i * Score_i) ; i is the feature
 */
public abstract class ParamBasedPostCombineRecommender extends PostCombineRecommender{

	protected List<Double> alphaList;// size of alphaList should be equal to recTypeList size!!
	
	// abstract methods
	abstract protected Double assignScore(UserCheckin targetUser, UserCheckin simUser, 
			Connection con, RecType recType);
	abstract protected Double getAlphaValue(int index);
	
	// methods
	public ParamBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * 
	 * Perform user combination by using parameters, such that
	 * totalScore = Total(alpha_i * Score_i) ; i is the feature or the method using single feature
	 * Choose the users with highest scores
	 * 
	 * TODO neighbour toplama kismi duplicate bircok methodda
	 * TODO burada yapilan assign score benzeri method VoteBased ve WeightAvg yontemleri icin de kullanilabilir
	 */
	protected ArrayList<UserCheckin> combineSimUsers(List<List<UserCheckin>> simUserList, 
			UserCheckin targetUser, Connection con) {
		// map users to totalScore
		HashMap<UserCheckin,Double> userSuggestionMap = new HashMap<UserCheckin, Double>();
		
		// get sim users found by each method
		int size = recTypeList.size();
		for(int i=0; i< size; i++)
		{
			List<UserCheckin> suggestionList = simUserList.get(i);
			Double alpha = getAlphaValue(i);//alphaList.get(i);
			RecType recType = recTypeList.get(i);
			
			// perform analysis for each suggested user
			for(UserCheckin simUser:suggestionList){
				if(userSuggestionMap.containsKey(simUser)){
					// already in map, just increment totalScore
					Double newVal = userSuggestionMap.get(simUser) + 
							assignScore(targetUser, simUser, con, recType) * alpha;
					userSuggestionMap.put(simUser, newVal);
				} else{
					// a new user is suggested!!
					Double score = assignScore(targetUser, simUser, con, recType) * alpha;
					userSuggestionMap.put(simUser, score);
				}
			}
		}
		
		
		// sort map in terms of count of suggestions!!
		// the previous map is sorted by userscheckin, just cretae the reverse:)
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

	public List<Double> getAlphaList() {
		return alphaList;
	}

	public void setAlphaList(List<Double> alphaList) {
		this.alphaList = alphaList;
	}
	
	

}
