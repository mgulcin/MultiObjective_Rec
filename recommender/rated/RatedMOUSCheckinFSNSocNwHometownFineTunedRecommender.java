package recommender.rated;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.base.MOUSCheckinFSNSocNwHometownFineTunedRecommender;

public class RatedMOUSCheckinFSNSocNwHometownFineTunedRecommender extends MOUSCheckinFSNSocNwHometownFineTunedRecommender {

	public RatedMOUSCheckinFSNSocNwHometownFineTunedRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}



}
