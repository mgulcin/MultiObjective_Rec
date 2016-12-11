package recommender.postCombine.paramBased.weightBased;

import inputReader.Checkin2011DBReader;

import java.util.ArrayList;

import outputWriter.Printer;
import recommender.RecommendationDBDecisionMaker;
import recommender.postCombine.paramBased.WeightParamBasedPostCombineRecommender;
import data.Similarity.SimType;

public class RankedAlphaWeightParamBasedPostCombineRecommender
		extends WeightParamBasedPostCombineRecommender {

	public RankedAlphaWeightParamBasedPostCombineRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
	}

	@Override
	protected void assignAlphaValues() {
		int size = recTypeList.size();
		this.alphaList = new ArrayList<Double>(size);
		
		// all alpha values will be this val * reversedRank (e.g. Rank 1-2-3 will have Scores: 3-2-1)
		Double totalScore = 0.0;
		ArrayList<Double> rankScoreList = new ArrayList<Double>(size);
		for(int i=0; i < size; i++){
			
			RecType recType = recTypeList.get(i);
			ArrayList<String> simNameList = RecommendationDBDecisionMaker.getSimName(recType);
			if(simNameList.size() > 1){
				System.out.println("Error, you are using another hybrid system!!");
				System.exit(-1);
			} else{
				String simName = simNameList.get(0);
				SimType simType = RecommendationDBDecisionMaker.mapFieldNameToSimType(simName);
				Double rankScore = RecommendationDBDecisionMaker.getRankScore(simType);
				rankScoreList.add(rankScore);
				totalScore += rankScore;
			}
			
		}
				
		// fill alphaList with alphaVal=rankScore/totalScore
		for(int i=0; i < size; i++){
			Double rankScore = rankScoreList.get(i);
			alphaList.add(rankScore/totalScore);
		}

	}
	
	@Override
	protected Double getAlphaValue(int index) {
		return alphaList.get(index);
	}

}
