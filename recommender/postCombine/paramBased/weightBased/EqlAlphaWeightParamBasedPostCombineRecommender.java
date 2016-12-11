package recommender.postCombine.paramBased.weightBased;

import inputReader.Checkin2011DBReader;

import java.util.ArrayList;

import outputWriter.Printer;
import recommender.postCombine.paramBased.WeightParamBasedPostCombineRecommender;

public class EqlAlphaWeightParamBasedPostCombineRecommender
		extends WeightParamBasedPostCombineRecommender {

	public EqlAlphaWeightParamBasedPostCombineRecommender(
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
		
		// all alpha values will be equal
		Double alphaVal = 1.0/size;

		// fill alphaList with alphaVal
		for(int i=0; i < size; i++){
			alphaList.add(alphaVal);
		}

	}

	@Override
	protected Double getAlphaValue(int index) {
		return alphaList.get(index);
	}
}
