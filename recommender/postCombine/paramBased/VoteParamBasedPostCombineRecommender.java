package recommender.postCombine.paramBased;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;

import outputWriter.Printer;
import recommender.postCombine.ParamBasedPostCombineRecommender;
import data.UserCheckin;

public abstract class VoteParamBasedPostCombineRecommender extends
		ParamBasedPostCombineRecommender {

	protected abstract void assignAlphaValues();
	
	public VoteParamBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * All simUsers are equal, such as giving votes
	 * @param targetUser
	 * @param simUser
	 * @param con
	 * @return
	 */
	protected Double assignScore(UserCheckin targetUser, 
			UserCheckin simUser, Connection con, RecType recType) {
		Double score = 1.0;
		return score;
	}

}
