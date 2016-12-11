package recommender.clusterByFriendship.weighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByFriendship.base.ClusteredByFriendshipSocNwCFRecommender;

public class ClusteredByFriendshipSocNwCFWeightedRecommender extends
ClusteredByFriendshipSocNwCFRecommender {

	public ClusteredByFriendshipSocNwCFWeightedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
