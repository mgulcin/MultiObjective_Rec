package recommender.postProcessByLoc.base;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.util.ArrayList;

import outputWriter.Printer;
import recommender.PostProcessByLocRecommender;
import recommender.RecommendationDBDecisionMaker;
import data.UserCheckin;

public class PostProcessedByLocCheckinCFRecommender extends PostProcessByLocRecommender {

	public PostProcessedByLocCheckinCFRecommender(Integer numberOfSimilarUsers,
			Double similarUserThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType recommenderType) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, recommenderType);
		
	}

	public PostProcessedByLocCheckinCFRecommender(Integer numberOfSimilarUsers,
			Double similarUserThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType recommenderType, Double distThreshold,
			Double eucDistSimThreshold) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, recommenderType,distThreshold, eucDistSimThreshold);
		
	}

	@Override
	/** // TODO directly copied from CheckinCFRecommender -- I should re-design the project!!
	 * Return most similar users
	 * 	- depending on the threshold on number of users to return and 
	 * 	- the similarity threshold
	 * Read these users from database
	 */
	public ArrayList<UserCheckin> getMostSimilarUsers(UserCheckin user, 
			Connection con, Integer numberOfSimilarUsers, 
			Double userSimilarityThreshold) {

		// 1) Get k-many similar users 
		ArrayList<String> fieldName = RecommendationDBDecisionMaker.getSimName(this.type);
		ArrayList<Integer> similarUsers = getMostSimilarUsersFromDB(user, con, 
				fieldName, numberOfSimilarUsers, userSimilarityThreshold);
		

		// create usercheckinlist by reading the similar users info from db
		ArrayList<UserCheckin> userList = createUserList(similarUsers, con);

		return userList;
	}

}
