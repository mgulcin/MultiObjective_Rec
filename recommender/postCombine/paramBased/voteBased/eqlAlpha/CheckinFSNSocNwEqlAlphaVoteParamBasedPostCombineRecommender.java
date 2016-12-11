package recommender.postCombine.paramBased.voteBased.eqlAlpha;

import inputReader.Checkin2011DBReader;

import java.util.ArrayList;

import outputWriter.Printer;
import recommender.Recommender;
import recommender.postCombine.paramBased.voteBased.EqlAlphaVoteParamBasedPostCombineRecommender;

public class CheckinFSNSocNwEqlAlphaVoteParamBasedPostCombineRecommender extends
EqlAlphaVoteParamBasedPostCombineRecommender {

	public CheckinFSNSocNwEqlAlphaVoteParamBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		
		// rectypes
		ArrayList<RecType> recTypeList = new ArrayList<Recommender.RecType>();
		recTypeList.add(RecType.CheckinLocCF);
		recTypeList.add(RecType.FSN);
		recTypeList.add(RecType.SocNwCF);
		setRecTypeList(recTypeList);
		
		// alpha values
		assignAlphaValues();
	}

}
