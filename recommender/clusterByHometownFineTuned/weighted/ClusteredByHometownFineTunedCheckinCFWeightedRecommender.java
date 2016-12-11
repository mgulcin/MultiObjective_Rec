package recommender.clusterByHometownFineTuned.weighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByHometownFineTuned.base.ClusteredByHometownFineTunedCheckinCFRecommender;

public class ClusteredByHometownFineTunedCheckinCFWeightedRecommender extends
ClusteredByHometownFineTunedCheckinCFRecommender {

	public ClusteredByHometownFineTunedCheckinCFWeightedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
