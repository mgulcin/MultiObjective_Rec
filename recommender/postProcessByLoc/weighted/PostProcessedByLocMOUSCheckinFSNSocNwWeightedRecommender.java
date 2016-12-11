package recommender.postProcessByLoc.weighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.postProcessByLoc.base.PostProcessedByLocMOUSCheckinFSNSocNwRecommender;

public class PostProcessedByLocMOUSCheckinFSNSocNwWeightedRecommender extends PostProcessedByLocMOUSCheckinFSNSocNwRecommender {

	public PostProcessedByLocMOUSCheckinFSNSocNwWeightedRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}


}
