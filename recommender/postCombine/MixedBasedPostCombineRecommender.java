/**
 * 
 */
package recommender.postCombine;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import outputWriter.Printer;
import data.UserCheckin;

/**
 * @author mg
 *
 */
public class MixedBasedPostCombineRecommender extends PostCombineRecommender{

	public MixedBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * Perform user combination by using each recommender suggestions, such that
	 * add first user from the first recommender, second from the second rec. etc.
	 */
	protected ArrayList<UserCheckin> combineSimUsers(List<List<UserCheckin>> simUserList, 
			UserCheckin targetUser, Connection con) {
		HashSet<UserCheckin> neighbours = new HashSet<UserCheckin>();

		for(int i = 0; i < numberOfSimilarUsers; i++){
			boolean collectedAll = false;

			// from each method collect ith user
			for(List<UserCheckin> suggestionList: simUserList){
				if(suggestionList.size()>i){
					UserCheckin simUser = suggestionList.get(i);
					if(neighbours.size() < numberOfSimilarUsers){
						neighbours.add(simUser);
					} else {
						collectedAll = true;
						break;
					}
				}
			}

			// no need to loop if all neighbours are collected
			if(collectedAll){
				break;
			}
		}


		ArrayList<UserCheckin> list = new ArrayList<UserCheckin>(neighbours);
		return list;
	}

}
