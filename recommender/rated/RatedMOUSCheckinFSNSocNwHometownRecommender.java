package recommender.rated;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.base.MOUSCheckinFSNSocNwHometownRecommender;

public class RatedMOUSCheckinFSNSocNwHometownRecommender extends MOUSCheckinFSNSocNwHometownRecommender {

	public RatedMOUSCheckinFSNSocNwHometownRecommender(
			Integer numberOfSimilarUsers, Double similarUserThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}



}
