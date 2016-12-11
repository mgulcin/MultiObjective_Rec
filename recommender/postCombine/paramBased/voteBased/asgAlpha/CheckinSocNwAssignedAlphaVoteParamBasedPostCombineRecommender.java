package recommender.postCombine.paramBased.voteBased.asgAlpha;

import inputReader.Checkin2011DBReader;

import java.math.BigDecimal;
import java.util.ArrayList;

import outputWriter.Printer;
import recommender.Recommender;
import recommender.postCombine.paramBased.voteBased.AssignedAlphaVoteParamBasedPostCombineRecommender;

public class CheckinSocNwAssignedAlphaVoteParamBasedPostCombineRecommender extends
AssignedAlphaVoteParamBasedPostCombineRecommender {

	public CheckinSocNwAssignedAlphaVoteParamBasedPostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		ArrayList<RecType> recTypeList = new ArrayList<Recommender.RecType>();
		recTypeList.add(RecType.CheckinLocCF);
		recTypeList.add(RecType.SocNwCF);
		setRecTypeList(recTypeList);
		
		// alpha values
		assignAlphaValues();
	}

	@Override
	/**
	 * Depending on the result of previos experiments, one parameter is set to givenVal
	 * and the rest is decided by searching the range [0.0,1.0-givenVal] 
	 * 
	 * The order of alphas is same as in the name of the method!!
	 */
	protected void decideAlphaValArray() {
		int size = recTypeList.size();
		
		// depends on the number of elements in the recTypeList
		this.alphaValList = new ArrayList<Double[]>();
		
		for(double alpha = 0.0; alpha <= 1.0 ; alpha = alpha + 0.1){
			int decimalPlaces = 1;
			BigDecimal bd = new BigDecimal(alpha);
		    bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		    alpha = bd.doubleValue();
		    
		    BigDecimal bd2 = new BigDecimal(1.0-alpha);
		    bd2 = bd2.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		    double alpha2 = bd2.doubleValue();
		    
			Double[] valList = new Double[size];
			valList[0] = alpha;
			valList[1] = alpha2;
			alphaValList.add(valList);
		}
	}
}
