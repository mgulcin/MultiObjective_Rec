package recommender.weighted;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.base.SocNwCFRecommender;

public class SocNwCFWeightedRecommender extends SocNwCFRecommender {

	public SocNwCFWeightedRecommender(Integer numberOfSimilarUsers,
			Double similarUserThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	

}
