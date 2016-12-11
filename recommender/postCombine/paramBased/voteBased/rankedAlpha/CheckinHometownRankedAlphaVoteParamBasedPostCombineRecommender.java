package recommender.postCombine.paramBased.voteBased.rankedAlpha;

import inputReader.Checkin2011DBReader;

import java.util.ArrayList;

import outputWriter.Printer;
import recommender.Recommender;
import recommender.postCombine.paramBased.voteBased.RankedAlphaVoteParamBasedPostCombineRecommender;

public class CheckinHometownRankedAlphaVoteParamBasedPostCombineRecommender extends
RankedAlphaVoteParamBasedPostCombineRecommender {

	public CheckinHometownRankedAlphaVoteParamBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		ArrayList<RecType> recTypeList = new ArrayList<Recommender.RecType>();
		recTypeList.add(RecType.CheckinLocCF);
		recTypeList.add(RecType.HometownCF);
		setRecTypeList(recTypeList);
		
		// alpha values
		assignAlphaValues();
	}

}
