package recommender.postprocessByLocHvr.rated;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.postprocessByLocHvr.base.PostProcessedByLocHvrCheckinCFRecommender;

public class RatedPostProcessedByLocHvrCheckinCFRecommender extends PostProcessedByLocHvrCheckinCFRecommender {

	public RatedPostProcessedByLocHvrCheckinCFRecommender(Integer numberOfSimilarUsers,
			Double similarUserThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType recommenderType, Double distThreshold, Double eucDistSimThreshold) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, recommenderType, distThreshold, eucDistSimThreshold);
		// TODO Auto-generated constructor stub
	}
	
	
	public RatedPostProcessedByLocHvrCheckinCFRecommender(Integer numberOfSimilarUsers,
			Double similarUserThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType recommenderType) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, recommenderType);
		// TODO Auto-generated constructor stub
	}


}
