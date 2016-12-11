package recommender.clusterByFriendship.ratedAndWeighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipMOUSCheckinHometownRecommender;

public class RatedClusteredByFriendshipMOUSCheckinHometownWeightedRecommender extends
RatedClusteredByFriendshipMOUSCheckinHometownRecommender {

	public RatedClusteredByFriendshipMOUSCheckinHometownWeightedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
