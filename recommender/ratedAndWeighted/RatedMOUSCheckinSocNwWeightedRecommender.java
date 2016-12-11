package recommender.ratedAndWeighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.rated.RatedMOUSCheckinSocNwRecommender;

public class RatedMOUSCheckinSocNwWeightedRecommender extends RatedMOUSCheckinSocNwRecommender {

	public RatedMOUSCheckinSocNwWeightedRecommender(
			Integer numberOfSimilarUsers, Double similarUserThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
