package recommender.clusterByHometownFineTuned.rated;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.clusterByHometownFineTuned.base.ClusteredByHometownFineTunedMOUSCheckinSocNwRecommender;

public class RatedClusteredByHometownFineTunedMOUSCheckinSocNwRecommender extends
ClusteredByHometownFineTunedMOUSCheckinSocNwRecommender {

	public RatedClusteredByHometownFineTunedMOUSCheckinSocNwRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}