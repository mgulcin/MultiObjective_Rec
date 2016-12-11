package recommender.clusterByHometown.weighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByHometown.base.ClusteredByHometownSocNwCFRecommender;

public class ClusteredByHometownSocNwCFWeightedRecommender extends 
ClusteredByHometownSocNwCFRecommender {

	public ClusteredByHometownSocNwCFWeightedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
