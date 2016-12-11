package recommender.clusterByHometownFineTuned.ratedAndWeighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByHometownFineTuned.rated.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwRecommender;

public class RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeightedRecommender
		extends RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwRecommender {

	public RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeightedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
