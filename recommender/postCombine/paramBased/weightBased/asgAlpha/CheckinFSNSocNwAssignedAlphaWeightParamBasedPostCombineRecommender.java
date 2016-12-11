package recommender.postCombine.paramBased.weightBased.asgAlpha;

import inputReader.Checkin2011DBReader;

import java.math.BigDecimal;
import java.util.ArrayList;

import outputWriter.Printer;
import recommender.Recommender;
import recommender.postCombine.paramBased.weightBased.AssignedAlphaWeightParamBasedPostCombineRecommender;

public class CheckinFSNSocNwAssignedAlphaWeightParamBasedPostCombineRecommender extends
AssignedAlphaWeightParamBasedPostCombineRecommender {

	public CheckinFSNSocNwAssignedAlphaWeightParamBasedPostCombineRecommender(Integer numberOfSimilarUsers,
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

	@Override
	/**
	 * Depending on the result of previos experiments, one parameter is set to givenVal
	 * and the rest is decided by searching the range [0.0,1.0-givenVal] 
	 * 
	 * The order of alphas is same as in the name of the method!!
	 */
	protected void decideAlphaValArray() {
		int size = recTypeList.size();
		// array olmali list olunca order onemli oluyor, arrayde istedigim indexe insert edebiliyorum,
		
		// depends on the number of elements in the recTypeList
		this.alphaValList = new ArrayList<Double[]>();
		double givenVal = 0.9; // TODO decided value!!
		if(givenVal == -1.0 ){
			System.out.println("Assign the alpha values for the 2 variables!! "
					+ "Don't forget to update valList index vaues accordingly!!");
			System.exit(-1);
		}
		for(double alpha = 0.0; alpha <= (1.0 - givenVal); alpha = alpha + 0.01){
			int decimalPlaces = 2;
			BigDecimal bd = new BigDecimal(alpha);
		    bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		    alpha = bd.doubleValue();
		    
		    BigDecimal bd2 = new BigDecimal(1.0-(alpha+givenVal));
		    bd2 = bd2.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		    double alpha2 = bd2.doubleValue();
		    
			Double[] valList = new Double[size];
			valList[0] = givenVal; 
			valList[1] = alpha;
			valList[2] = alpha2;
			alphaValList.add(valList);
		}
	}

}
