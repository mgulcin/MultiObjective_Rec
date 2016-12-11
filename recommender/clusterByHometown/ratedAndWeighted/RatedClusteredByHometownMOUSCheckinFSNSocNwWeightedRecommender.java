package recommender.clusterByHometown.ratedAndWeighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByHometown.rated.RatedClusteredByHometownMOUSCheckinFSNSocNwRecommender;

public class RatedClusteredByHometownMOUSCheckinFSNSocNwWeightedRecommender
		extends RatedClusteredByHometownMOUSCheckinFSNSocNwRecommender {

	public RatedClusteredByHometownMOUSCheckinFSNSocNwWeightedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
