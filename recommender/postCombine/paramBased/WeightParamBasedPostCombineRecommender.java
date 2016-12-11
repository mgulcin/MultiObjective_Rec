package recommender.postCombine.paramBased;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.util.ArrayList;

import outputWriter.Printer;
import recommender.RecommendationDBDecisionMaker;
import recommender.postCombine.ParamBasedPostCombineRecommender;
import data.UserCheckin;

public abstract class WeightParamBasedPostCombineRecommender extends
		ParamBasedPostCombineRecommender {

	protected abstract void assignAlphaValues();
	
	public WeightParamBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * simUser scores are assigned by their similarity to the target
	 */
	protected Double assignScore(UserCheckin targetUser, UserCheckin simUser,
			Connection con, RecType recType) {
		Double score = 0.0;

		ArrayList<String> simNameList = RecommendationDBDecisionMaker.getSimName(recType);
		if(simNameList.size() > 1){
			System.out.println("Error, you are using another hybrid system!!");
			System.exit(-1);
		} else{
			ArrayList<Double> vals = this.getSimilaritiesFromDB(targetUser,simUser,con, simNameList);
			score = vals.get(0);
		}

		return score;
	}

}
