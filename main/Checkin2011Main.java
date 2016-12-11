package main;

import java.io.FileNotFoundException;

import outputWriter.FilePrinter;
import outputWriter.Printer;
import recommender.RecommendationDBDecisionMaker;
import recommender.Recommender.RecType;
import recommender.postprocessByLocHvr.base.PostProcessedByLocHvrCheckinCFRecommender;


public class Checkin2011Main {

	/*
	 * RecTypes:: Checkin2011
	 * See RecType class
	 */

	public static Integer numberOfSimilarUsers = 30;
	private static Double userSimilarityThreshold = 0.0;
	private static Double minScoreVal = 0.0;
	private static Double maxScoreVal = 1.0;
	private static Double outputListSize = 10.0;// max outlist size to be compared
	public static String dbName = null;

	static RecType[] recTypeList = {
		///-----base---------
		RecType.CheckinLocCF, 
		/*	RecType.FSN, 
				RecType.SocNwCF, 
				RecType.HometownCF,
				RecType.MOUSCheckinSocNw, 
				RecType.MOUSCheckinFSNSocNw,
				RecType.MOUSCheckinFSNSocNwHometown, 
				RecType.MOUSCheckinHometown,*/
		///--------weighteds-------
		/*RecType.CheckinLocCFWeighted, RecType.FSNWeighted, 
				RecType.SocNwCFWeighted, RecType.HometownCFWeighted,
				RecType.MOUSCheckinFSNSocNwHometownWeighted, 
				RecType.MOUSCheckinHometownWeighted,
				RecType.MOUSCheckinSocNwWeighted,
				RecType.MOUSCheckinFSNSocNwWeighted
		///-----rateds---------
		RecType.RatedCheckinLocCF, RecType.RatedFSN, 
				RecType.RatedSocNwCF, RecType.RatedHometownCF,
				RecType.RatedMOUSCheckinSocNw, 
				RecType.RatedMOUSCheckinFSNSocNw,
				RecType.RatedMOUSCheckinFSNSocNwHometown, 
				RecType.RatedMOUSCheckinHometown,
		///---- rated & weighted----------
		RecType.RatedCheckinLocCFWeighted, RecType.RatedFSNWeighted, 
				RecType.RatedSocNwCFWeighted, RecType.RatedHometownCFWeighted,
				RecType.RatedMOUSCheckinSocNwWeighted, 
				RecType.RatedMOUSCheckinFSNSocNwWeighted, 
				RecType.RatedMOUSCheckinFSNSocNwHometownWeighted, 
				RecType.RatedMOUSCheckinHometownWeighted*/
		///--------- new features----------- hometown fine tuned
		/*RecType.HometownFineTunedCF,
				RecType.HometownFineTunedCFWeighted,
				RecType.RatedHometownFineTunedCF,
				RecType.RatedHometownFineTunedCFWeighted,
				RecType.MOUSCheckinHometownFineTuned,
				RecType.MOUSCheckinFSNSocNwHometownFineTuned,
				RecType.MOUSCheckinHometownFineTunedWeighted, 
				RecType.MOUSCheckinFSNSocNwHometownFineTunedWeighted,
				RecType.RatedMOUSCheckinHometownFineTuned, 
				RecType.RatedMOUSCheckinFSNSocNwHometownFineTuned,
				RecType.RatedMOUSCheckinHometownFineTunedWeighted, 
				RecType.RatedMOUSCheckinFSNSocNwHometownFineTunedWeighted,*/
		/// --- cluster based - by hometown -----------
		/*RecType.ClusteredByHometownCheckinLocCF,
		RecType.ClusteredByHometownFSN, 
		RecType.ClusteredByHometownSocNwCF,
		RecType.ClusteredByHometownMOUSCheckinSocNw, 
		RecType.ClusteredByHometownMOUSCheckinFSNSocNw,
		RecType.ClusteredByHometownCheckinLocCFWeighted,
		RecType.ClusteredByHometownFSNWeighted, 
		RecType.ClusteredByHometownSocNwCFWeighted,
		RecType.ClusteredByHometownMOUSCheckinSocNwWeighted, 
		RecType.ClusteredByHometownMOUSCheckinFSNSocNwWeighted,
		RecType.RatedClusteredByHometownCheckinLocCF, 
		RecType.RatedClusteredByHometownFSN, 
		RecType.RatedClusteredByHometownSocNwCF,
		RecType.RatedClusteredByHometownMOUSCheckinSocNw, 
		RecType.RatedClusteredByHometownMOUSCheckinFSNSocNw, 
		RecType.RatedClusteredByHometownCheckinLocCFWeighted, 
		RecType.RatedClusteredByHometownFSNWeighted, 
		RecType.RatedClusteredByHometownSocNwCFWeighted,
		RecType.RatedClusteredByHometownMOUSCheckinSocNwWeighted, 
		RecType.RatedClusteredByHometownMOUSCheckinFSNSocNwWeighted,*/
		/*/// ---- cluster based -- cluster by friendship
		// base
		RecType.ClusteredByFriendshipCheckinLocCF,
				RecType.ClusteredByFriendshipSocNwCF,
				RecType.ClusteredByFriendshipHometownCF,
				RecType.ClusteredByFriendshipHometownFineTunedCF,
				RecType.ClusteredByFriendshipMOUSCheckinSocNw,
				RecType.ClusteredByFriendshipMOUSCheckinHometown,
				RecType.ClusteredByFriendshipMOUSCheckinHometownFineTuned,
				RecType.ClusteredByFriendshipMOUSCheckinSocNwHometown,
				RecType.ClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned,
		// weighted
		RecType.ClusteredByFriendshipCheckinLocCFWeighted, 
				RecType.ClusteredByFriendshipSocNwCFWeighted,
				RecType.ClusteredByFriendshipHometownCFWeighted,
				RecType.ClusteredByFriendshipHometownFineTunedCFWeighted,
				RecType.ClusteredByFriendshipMOUSCheckinSocNwWeighted,
				RecType.ClusteredByFriendshipMOUSCheckinHometownWeighted,
				RecType.ClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted,
				RecType.ClusteredByFriendshipMOUSCheckinSocNwHometownWeighted,
				RecType.ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted,
		// rated
		RecType.RatedClusteredByFriendshipCheckinLocCF,
				RecType.RatedClusteredByFriendshipSocNwCF,
				RecType.RatedClusteredByFriendshipHometownCF,
				RecType.RatedClusteredByFriendshipHometownFineTunedCF,
				RecType.RatedClusteredByFriendshipMOUSCheckinSocNw, 
				RecType.RatedClusteredByFriendshipMOUSCheckinHometown,
				RecType.RatedClusteredByFriendshipMOUSCheckinHometownFineTuned,
				RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometown, 
				RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned,
		// rated & weighted
		RecType.RatedClusteredByFriendshipCheckinLocCFWeighted, 
				RecType.RatedClusteredByFriendshipSocNwCFWeighted,
				RecType.RatedClusteredByFriendshipHometownCFWeighted,
				RecType.RatedClusteredByFriendshipHometownFineTunedCFWeighted,
				RecType.RatedClusteredByFriendshipMOUSCheckinSocNwWeighted, 
				RecType.RatedClusteredByFriendshipMOUSCheckinHometownWeighted,
				RecType.RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted,
				RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeighted, 
				RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted*/
		/*/// --- cluster based - by hometownFineTuned -----------
				// base
				RecType.ClusteredByHometownFineTunedCheckinLocCF, 
				RecType.ClusteredByHometownFineTunedFSN, 
				RecType.ClusteredByHometownFineTunedSocNwCF,
				RecType.ClusteredByHometownFineTunedMOUSCheckinSocNw, 
				RecType.ClusteredByHometownFineTunedMOUSCheckinFSNSocNw, 
				// weighted
				RecType.ClusteredByHometownFineTunedCheckinLocCFWeighted, 
				RecType.ClusteredByHometownFineTunedFSNWeighted, 
				RecType.ClusteredByHometownFineTunedSocNwCFWeighted, 
				RecType.ClusteredByHometownFineTunedMOUSCheckinSocNwWeighted, 
				RecType.ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted, 
				// rated
				RecType.RatedClusteredByHometownFineTunedCheckinLocCF, 
				RecType.RatedClusteredByHometownFineTunedFSN, 
				RecType.RatedClusteredByHometownFineTunedSocNwCF,
				RecType.RatedClusteredByHometownFineTunedMOUSCheckinSocNw, 
				RecType.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNw, 
				// rated and weighted
				RecType.RatedClusteredByHometownFineTunedCheckinLocCFWeighted, 
				RecType.RatedClusteredByHometownFineTunedFSNWeighted, 
				RecType.RatedClusteredByHometownFineTunedSocNwCFWeighted,
				RecType.RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeighted, 
				RecType.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted */		
		/*/// ---- postprocessed based -- cluster by hometown_finetuned and location fine tuned
				// base
				RecType.PostProcessedByLocCheckinLocCF, 
				RecType.PostProcessedByLocFSN, 
				RecType.PostProcessedByLocSocNwCF,
				RecType.PostProcessedByLocMOUSCheckinSocNw, 
				RecType.PostProcessedByLocMOUSCheckinFSNSocNw, 
				// weighted
				RecType.PostProcessedByLocCheckinLocCFWeighted, 
				RecType.PostProcessedByLocFSNWeighted, 
				RecType.PostProcessedByLocSocNwCFWeighted, 
				RecType.PostProcessedByLocMOUSCheckinSocNwWeighted, 
				RecType.PostProcessedByLocMOUSCheckinFSNSocNwWeighted, 
				// rated
				RecType.RatedPostProcessedByLocCheckinLocCF, 
				RecType.RatedPostProcessedByLocFSN, 
				RecType.RatedPostProcessedByLocSocNwCF,
				RecType.RatedPostProcessedByLocMOUSCheckinSocNw, 
				RecType.RatedPostProcessedByLocMOUSCheckinFSNSocNw, 
				// rated and weighted
				RecType.RatedPostProcessedByLocCheckinLocCFWeighted, 
				RecType.RatedPostProcessedByLocFSNWeighted, 
				RecType.RatedPostProcessedByLocSocNwCFWeighted,
				RecType.RatedPostProcessedByLocMOUSCheckinSocNwWeighted, 
				RecType.RatedPostProcessedByLocMOUSCheckinFSNSocNwWeighted,*/
		///////////////// hometown finetuned with heversine dist metric //////////////
		/// ---- postprocessed based -- compare user HometownFineTuned and location fine tuned
		/*// base
				RecType.PostProcessedByLocHvrCheckinLocCF, 
				RecType.PostProcessedByLocHvrFSN, 
				RecType.PostProcessedByLocHvrSocNwCF,
				RecType.PostProcessedByLocHvrMOUSCheckinSocNw, 
				RecType.PostProcessedByLocHvrMOUSCheckinFSNSocNw, */
				/*// weighted
				RecType.PostProcessedByLocHvrCheckinLocCFWeighted, 
				RecType.PostProcessedByLocHvrFSNWeighted, 
				RecType.PostProcessedByLocHvrSocNwCFWeighted, 
				RecType.PostProcessedByLocHvrMOUSCheckinSocNwWeighted, 
				RecType.PostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted, */
				// rated
				/*RecType.RatedPostProcessedByLocHvrCheckinLocCF, 
				RecType.RatedPostProcessedByLocHvrFSN, 
				RecType.RatedPostProcessedByLocHvrSocNwCF,
				RecType.RatedPostProcessedByLocHvrMOUSCheckinSocNw, 
				RecType.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNw, 
				// rated and weighted
				RecType.RatedPostProcessedByLocHvrCheckinLocCFWeighted, 
				RecType.RatedPostProcessedByLocHvrFSNWeighted, 
				RecType.RatedPostProcessedByLocHvrSocNwCFWeighted,
				RecType.RatedPostProcessedByLocHvrMOUSCheckinSocNwWeighted, 
				RecType.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted*/
		/*/// ---- vote based (all in base setting!!) ----////
		RecType.CheckinHometownVoteBasedPostCombine,
		RecType.CheckinSocNwVoteBasedPostCombine,
		RecType.CheckinFSNSocNwVoteBasedPostCombine,
		RecType.CheckinFSNSocNwHometownVoteBasedPostCombine,
				/// ---- weight avg based (all in base setting!!) ----////
		RecType.CheckinHometownWeightAvgBasedPostCombine,
		RecType.CheckinSocNwWeightAvgBasedPostCombine,
		RecType.CheckinFSNSocNwWeightAvgBasedPostCombine,
		RecType.CheckinFSNSocNwHometownWeightAvgBasedPostCombine,
				/// ---- mixed based (all in base setting!!) ----////
		RecType.CheckinHometownMixedBasedPostCombine,
		RecType.CheckinSocNwMixedBasedPostCombine,
		RecType.CheckinFSNSocNwMixedBasedPostCombine,
		RecType.CheckinFSNSocNwHometownMixedBasedPostCombine,*/
		/*/// ---- parameter usage (Ye et al,2011) + vote + eql Alpha values ---- ////
		RecType.CheckinHometownEqlAlphaVoteParamBasedPostCombine,
		RecType.CheckinSocNwEqlAlphaVoteParamBasedPostCombine,
		RecType.CheckinFSNSocNwEqlAlphaVoteParamBasedPostCombine,
		RecType.CheckinFSNSocNwHometownEqlAlphaVoteParamBasedPostCombine,
		/// ---- parameter usage (Ye et al,2011) + vote + ranked Alpha values ---- ////
		RecType.CheckinHometownRankedAlphaVoteParamBasedPostCombine,
		RecType.CheckinSocNwRankedAlphaVoteParamBasedPostCombine,
		RecType.CheckinFSNSocNwRankedAlphaVoteParamBasedPostCombine,
		RecType.CheckinFSNSocNwHometownRankedAlphaVoteParamBasedPostCombine,
		/// ---- parameter usage (Ye et al,2011) + weight + eql Alpha values ---- ////
		RecType.CheckinHometownEqlAlphaWeightParamBasedPostCombine,
		RecType.CheckinSocNwEqlAlphaWeightParamBasedPostCombine,
		RecType.CheckinFSNSocNwEqlAlphaWeightParamBasedPostCombine,
		RecType.CheckinFSNSocNwHometownEqlAlphaWeightParamBasedPostCombine,
		/// ---- parameter usage (Ye et al,2011) + weight + ranked Alpha values ---- ////
		RecType.CheckinHometownRankedAlphaWeightParamBasedPostCombine,
		RecType.CheckinSocNwRankedAlphaWeightParamBasedPostCombine,
		RecType.CheckinFSNSocNwRankedAlphaWeightParamBasedPostCombine,
		RecType.CheckinFSNSocNwHometownRankedAlphaWeightParamBasedPostCombine,*/
		/// ---- parameter usage (Ye et al,2011) + vote + assigned Alpha values ---- ////
		/*RecType.CheckinHometownAssignedAlphaVoteParamBasedPostCombine,
		RecType.CheckinSocNwAssignedAlphaVoteParamBasedPostCombine,
		RecType.CheckinFSNSocNwAssignedAlphaVoteParamBasedPostCombine,
		RecType.CheckinFSNSocNwHometownAssignedAlphaVoteParamBasedPostCombine, */
		/// ---- parameter usage (Ye et al,2011) + weight + assigned Alpha values ---- ////
		/*RecType.CheckinHometownAssignedAlphaWeightParamBasedPostCombine,
		RecType.CheckinSocNwAssignedAlphaWeightParamBasedPostCombine,
		RecType.CheckinFSNSocNwAssignedAlphaWeightParamBasedPostCombine,
		RecType.CheckinFSNSocNwHometownAssignedAlphaWeightParamBasedPostCombine*/
	/*	/// Time based calculations - using older base,Rate, Weight , Rate&Weight based settings
		// base
		RecType.MOUSCheckinTime, 
		RecType.MOUSCheckinHometownTime, 
		RecType.MOUSCheckinHometownFSNTime, 
		RecType.MOUSCheckinHometownFSNSocNwTime, 
		RecType.MOUSCheckinHometownFineTunedFSNSocNwTime,
		// weight based		
		RecType.MOUSCheckinTimeWeighted, 
		RecType.MOUSCheckinHometownTimeWeighted, 
		RecType.MOUSCheckinHometownFSNTimeWeighted, 
		RecType.MOUSCheckinHometownFSNSocNwTimeWeighted, 
		//RecType.MOUSCheckinHometownFineTunedFSNSocNwTimeWeighted,
		// rate base
		RecType.RatedMOUSCheckinTime, 
		RecType.RatedMOUSCheckinHometownTime, 
		RecType.RatedMOUSCheckinHometownFSNTime, 
		RecType.RatedMOUSCheckinHometownFSNSocNwTime, 
		//RecType.RatedMOUSCheckinHometownFineTunedFSNSocNwTime,
		// rate&weight base
		RecType.RatedMOUSCheckinTimeWeighted, 
		RecType.RatedMOUSCheckinHometownTimeWeighted, 
		RecType.RatedMOUSCheckinHometownFSNTimeWeighted, 
		RecType.RatedMOUSCheckinHometownFSNSocNwTimeWeighted, */
		//RecType.RatedMOUSCheckinHometownFineTunedFSNSocNwTimeWeighted
		// only time cf
		/* RecType.TimeCF, 
		 RecType.TimeCFWeighted, 
		 RecType.RatedTimeCF, 
		 RecType.RatedTimeCFWeighted*/
		/*//// dynamic time requirement of users - add post process based on users temporal preference
		/// performed only for base settings!!!
		///--- single criterion
		RecType.PostProcessedByTimeCheckinLocCF, 
		RecType.PostProcessedByTimeFSN, 
		RecType.PostProcessedByTimeSocNwCF,
		RecType.PostProcessedByTimeHometownCF,
		RecType.PostProcessedByTimeTimeCF,
		// mo-base 
		RecType.PostProcessedByTimeMOUSCheckinSocNw, 
		RecType.PostProcessedByTimeMOUSCheckinFSNSocNw,
		RecType.PostProcessedByTimeMOUSCheckinFSNSocNwHometown, 
		RecType.PostProcessedByTimeMOUSCheckinHometown,
		// mo-base with time
		RecType.PostProcessedByTimeMOUSCheckinTime, 
		RecType.PostProcessedByTimeMOUSCheckinHometownTime,
		RecType.PostProcessedByTimeMOUSCheckinHometownFSNTime, 
		RecType.PostProcessedByTimeMOUSCheckinHometownFSNSocNwTime*/
			
	};

	public static void main(String[] args) {

		try {
			Printer printer = new FilePrinter(false);
			String path  = ".//result//TimeSpent-CF";
			printer.printString(path, "recType, timeSpent(millisecond)");


			for(RecType recType:recTypeList){

			 // N= number of users
				for(Integer N = 30; N<= 30; N = N+10){
					Checkin2011Main.numberOfSimilarUsers = N;
					dbName = "recommender"+ recType.getValue() +"_"+ Checkin2011Main.numberOfSimilarUsers ;
					RecommendationDBDecisionMaker dm = new RecommendationDBDecisionMaker(recType,
							numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal, outputListSize);
				
					
					// Recommend
					long prev = System.currentTimeMillis();
					dm.recommend(recType);
					long next = System.currentTimeMillis();
					System.out.println("Time spent (millisecond): "+ (next-prev) );
					printer.printString(path, recType.getValue() +" , "  + (next-prev));
					


					// Evaluate
					if(RecommendationDBDecisionMaker.isPostProcessBased(recType) 
							&& RecommendationDBDecisionMaker.isPostProcessedByLocHvr(recType)){
						for(Double distThresh : PostProcessedByLocHvrCheckinCFRecommender.distThresholdList){
							dm.evaluate(recType,outputListSize, distThresh);	
						}
					} else if(RecommendationDBDecisionMaker.isAssignedAlphaVoteParamBased(recType)){
						dm.evaluateByAsgAlphaVote(recType,outputListSize);
					} else if(RecommendationDBDecisionMaker.isAssignedAlphaWeightParamBased(recType)){
						dm.evaluateByAsgAlphaWeight(recType,outputListSize);
					} else if(RecommendationDBDecisionMaker.isPostProcessedByTime(recType)){
						dm.evaluateByTimeCategory(recType,outputListSize);
					}
					else {
						dm.evaluate(recType,outputListSize);	
					}

				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
