package recommender.postProcessByLoc.rated;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.postProcessByLoc.base.PostProcessedByLocMOUSCheckinSocNwRecommender;

public class RatedPostProcessedByLocMOUSCheckinSocNwRecommender extends PostProcessedByLocMOUSCheckinSocNwRecommender {

	public RatedPostProcessedByLocMOUSCheckinSocNwRecommender(Integer numberOfSimilarUsers,
			Double similarUserThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
