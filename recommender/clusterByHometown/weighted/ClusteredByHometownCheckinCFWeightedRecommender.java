package recommender.clusterByHometown.weighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByHometown.base.ClusteredByHometownCheckinCFRecommender;

public class ClusteredByHometownCheckinCFWeightedRecommender extends
ClusteredByHometownCheckinCFRecommender {

	public ClusteredByHometownCheckinCFWeightedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
