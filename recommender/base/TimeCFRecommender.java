package recommender.base;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.util.ArrayList;

import outputWriter.Printer;
import recommender.RecommendationDBDecisionMaker;
import recommender.Recommender;
import data.UserCheckin;


public class TimeCFRecommender extends Recommender{

	public TimeCFRecommender(Integer numberOfSimilarUsers, Double similarUserThreshold,
			Double minScoreVal, Double maxScoreVal,
			Printer printer, Checkin2011DBReader reader, RecType recommenderType) {
		super(numberOfSimilarUsers,similarUserThreshold,
				minScoreVal,maxScoreVal, printer, reader, recommenderType);

	}


	@Override
	/**
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
