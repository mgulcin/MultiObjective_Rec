package recommender.clusterByFriendship.base;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.util.ArrayList;

import outputWriter.Printer;
import recommender.RecommendationDBDecisionMaker;
import recommender.Recommender;
import data.UserCheckin;

public class ClusteredByFriendshipHometownFineTunedCFRecommender extends
Recommender {

	public ClusteredByFriendshipHometownFineTunedCFRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<UserCheckin> getMostSimilarUsers(UserCheckin user,
			Connection con, Integer numberOfSimilarUsers,
			Double userSimilarityThreshold) {
		// 1) Get k-many similar users 
		ArrayList<String> fieldName = RecommendationDBDecisionMaker.getSimName(this.type);
		ArrayList<String> clusterFieldName = RecommendationDBDecisionMaker.getClusterSimName(this.type);
		ArrayList<Integer> similarUsers = getMostSimilarUsersFromClusterFromDB(user, con, 
				fieldName, clusterFieldName, numberOfSimilarUsers, userSimilarityThreshold);


		// create usercheckinlist by reading the similar users info from db
		ArrayList<UserCheckin> userList = createUserList(similarUsers, con);


		return userList;
	}

}
