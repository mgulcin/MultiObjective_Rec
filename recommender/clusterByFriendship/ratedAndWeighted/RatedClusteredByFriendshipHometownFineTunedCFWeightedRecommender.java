package recommender.clusterByFriendship.ratedAndWeighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipHometownFineTunedCFRecommender;

public class RatedClusteredByFriendshipHometownFineTunedCFWeightedRecommender extends
RatedClusteredByFriendshipHometownFineTunedCFRecommender {

	public RatedClusteredByFriendshipHometownFineTunedCFWeightedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	
}