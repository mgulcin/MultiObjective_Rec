package recommender.postCombine.mixedBased;

import inputReader.Checkin2011DBReader;

import java.util.ArrayList;

import outputWriter.Printer;
import recommender.Recommender;
import recommender.postCombine.MixedBasedPostCombineRecommender;

public class CheckinSocNwMixedBasedPostCombineRecommender extends
MixedBasedPostCombineRecommender {

	public CheckinSocNwMixedBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		ArrayList<RecType> recTypeList = new ArrayList<Recommender.RecType>();
		recTypeList.add(RecType.CheckinLocCF);
		recTypeList.add(RecType.SocNwCF);
		setRecTypeList(recTypeList);
	}

}
