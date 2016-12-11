package recommender.base;

import inputReader.Checkin2011DBReader;

import java.util.ArrayList;

import outputWriter.Printer;
import similarity.MOBasedSimilarityCalculator;
import data.Similarity;
import data.Similarity.SimType;

public class MOUSCheckinFSNSocNwHometownAvgRecommender extends MOUSCheckinFSNSocNwHometownRecommender {

	public MOUSCheckinFSNSocNwHometownAvgRecommender(
			Integer numberOfSimilarUsers, Double similarUserThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
	}
	
	public ArrayList<Integer> getMostSimilarUsers(ArrayList<Similarity> userSimVals,
			Integer numberOfSimilarUsers, Double similarUserThreshold) {
		// 1) create dominance matrix
		MOBasedSimilarityCalculator moSimCalc = new MOBasedSimilarityCalculator();
		ArrayList<Similarity.SimType> featuresToUse = new ArrayList<Similarity.SimType>();
		featuresToUse.add(Similarity.SimType.CHECKIN);
		featuresToUse.add(Similarity.SimType.FSN);
		featuresToUse.add(Similarity.SimType.SOCIALNW);
		featuresToUse.add(Similarity.SimType.HOMETOWN);
		Double[][] dominanceMatrix = moSimCalc.createDominanceMatrix(userSimVals, 
				featuresToUse);

		// 2) select non-dominated friends
		ArrayList<Similarity> nonDominatedSims = moSimCalc.findNonDominatedSims(userSimVals, 
				dominanceMatrix);

		// 3) sort non-dominated friends by checkin & sn- using the sortOrder
		ArrayList<Similarity.SimType> sortOrder = new ArrayList<Similarity.SimType>();
		sortOrder.add(SimType.CHECKIN);  
		sortOrder.add(SimType.FSN); 
		sortOrder.add(SimType.SOCIALNW);
		sortOrder.add(Similarity.SimType.HOMETOWN);
		ArrayList<Integer> similarUsers = moSimCalc.sortByAvg(nonDominatedSims, sortOrder, 
				numberOfSimilarUsers, userSimilarityThreshold);		

		//System.out.println("Size: " + similarUsers.size());

		return similarUsers;
	}

}
