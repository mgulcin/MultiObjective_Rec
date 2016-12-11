package recommender;

import inputReader.Checkin2011DBReader;
import outputWriter.Printer;
import recommender.Recommender.RecType;
import recommender.base.CheckinCFRecommender;
import recommender.base.FSNRecommender;
import recommender.base.HometownCFRecommender;
import recommender.base.HometownFineTunedCFRecommender;
import recommender.base.MOUSCheckinFSNSocNwHometownFineTunedRecommender;
import recommender.base.MOUSCheckinFSNSocNwHometownRecommender;
import recommender.base.MOUSCheckinFSNSocNwRecommender;
import recommender.base.MOUSCheckinHometownFSNSocNwTimeRecommender;
import recommender.base.MOUSCheckinHometownFSNTimeRecommender;
import recommender.base.MOUSCheckinHometownFineTunedFSNSocNwTimeRecommender;
import recommender.base.MOUSCheckinHometownFineTunedRecommender;
import recommender.base.MOUSCheckinHometownRecommender;
import recommender.base.MOUSCheckinHometownTimeRecommender;
import recommender.base.MOUSCheckinSocNwRecommender;
import recommender.base.MOUSCheckinTimeRecommender;
import recommender.base.SocNwCFRecommender;
import recommender.base.TimeCFRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipCheckinCFRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipHometownCFRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipHometownFineTunedCFRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipMOUSCheckinHometownFineTunedRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipMOUSCheckinHometownRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipMOUSCheckinSocNwHometownRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipMOUSCheckinSocNwRecommender;
import recommender.clusterByFriendship.base.ClusteredByFriendshipSocNwCFRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipCheckinCFRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipHometownCFRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipHometownFineTunedCFRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipMOUSCheckinHometownFineTunedRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipMOUSCheckinHometownRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipMOUSCheckinSocNwHometownRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipMOUSCheckinSocNwRecommender;
import recommender.clusterByFriendship.rated.RatedClusteredByFriendshipSocNwCFRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipCheckinCFWeightedRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipHometownCFWeightedRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipHometownFineTunedCFWeightedRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeightedRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipMOUSCheckinHometownWeightedRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeightedRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeightedRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipMOUSCheckinSocNwWeightedRecommender;
import recommender.clusterByFriendship.ratedAndWeighted.RatedClusteredByFriendshipSocNwCFWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipCheckinCFWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipHometownCFWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipHometownFineTunedCFWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipMOUSCheckinHometownFineTunedWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipMOUSCheckinHometownWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipMOUSCheckinSocNwHometownWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipMOUSCheckinSocNwWeightedRecommender;
import recommender.clusterByFriendship.weighted.ClusteredByFriendshipSocNwCFWeightedRecommender;
import recommender.clusterByHometown.base.ClusteredByHometownCheckinCFRecommender;
import recommender.clusterByHometown.base.ClusteredByHometownFSNRecommender;
import recommender.clusterByHometown.base.ClusteredByHometownMOUSCheckinFSNSocNwRecommender;
import recommender.clusterByHometown.base.ClusteredByHometownMOUSCheckinSocNwRecommender;
import recommender.clusterByHometown.base.ClusteredByHometownSocNwCFRecommender;
import recommender.clusterByHometown.rated.RatedClusteredByHometownCheckinCFRecommender;
import recommender.clusterByHometown.rated.RatedClusteredByHometownFSNRecommender;
import recommender.clusterByHometown.rated.RatedClusteredByHometownMOUSCheckinFSNSocNwRecommender;
import recommender.clusterByHometown.rated.RatedClusteredByHometownMOUSCheckinSocNwRecommender;
import recommender.clusterByHometown.rated.RatedClusteredByHometownSocNwCFRecommender;
import recommender.clusterByHometown.ratedAndWeighted.RatedClusteredByHometownCheckinCFWeightedRecommender;
import recommender.clusterByHometown.ratedAndWeighted.RatedClusteredByHometownFSNWeightedRecommender;
import recommender.clusterByHometown.ratedAndWeighted.RatedClusteredByHometownMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.clusterByHometown.ratedAndWeighted.RatedClusteredByHometownMOUSCheckinSocNwWeightedRecommender;
import recommender.clusterByHometown.ratedAndWeighted.RatedClusteredByHometownSocNwCFWeightedRecommender;
import recommender.clusterByHometown.weighted.ClusteredByHometownCheckinCFWeightedRecommender;
import recommender.clusterByHometown.weighted.ClusteredByHometownFSNWeightedRecommender;
import recommender.clusterByHometown.weighted.ClusteredByHometownMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.clusterByHometown.weighted.ClusteredByHometownMOUSCheckinSocNwWeightedRecommender;
import recommender.clusterByHometown.weighted.ClusteredByHometownSocNwCFWeightedRecommender;
import recommender.clusterByHometownFineTuned.base.ClusteredByHometownFineTunedCheckinCFRecommender;
import recommender.clusterByHometownFineTuned.base.ClusteredByHometownFineTunedFSNRecommender;
import recommender.clusterByHometownFineTuned.base.ClusteredByHometownFineTunedMOUSCheckinFSNSocNwRecommender;
import recommender.clusterByHometownFineTuned.base.ClusteredByHometownFineTunedMOUSCheckinSocNwRecommender;
import recommender.clusterByHometownFineTuned.base.ClusteredByHometownFineTunedSocNwCFRecommender;
import recommender.clusterByHometownFineTuned.rated.RatedClusteredByHometownFineTunedCheckinCFRecommender;
import recommender.clusterByHometownFineTuned.rated.RatedClusteredByHometownFineTunedFSNRecommender;
import recommender.clusterByHometownFineTuned.rated.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwRecommender;
import recommender.clusterByHometownFineTuned.rated.RatedClusteredByHometownFineTunedMOUSCheckinSocNwRecommender;
import recommender.clusterByHometownFineTuned.rated.RatedClusteredByHometownFineTunedSocNwCFRecommender;
import recommender.clusterByHometownFineTuned.ratedAndWeighted.RatedClusteredByHometownFineTunedCheckinCFWeightedRecommender;
import recommender.clusterByHometownFineTuned.ratedAndWeighted.RatedClusteredByHometownFineTunedFSNWeightedRecommender;
import recommender.clusterByHometownFineTuned.ratedAndWeighted.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.clusterByHometownFineTuned.ratedAndWeighted.RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeightedRecommender;
import recommender.clusterByHometownFineTuned.ratedAndWeighted.RatedClusteredByHometownFineTunedSocNwCFWeightedRecommender;
import recommender.clusterByHometownFineTuned.weighted.ClusteredByHometownFineTunedCheckinCFWeightedRecommender;
import recommender.clusterByHometownFineTuned.weighted.ClusteredByHometownFineTunedFSNWeightedRecommender;
import recommender.clusterByHometownFineTuned.weighted.ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.clusterByHometownFineTuned.weighted.ClusteredByHometownFineTunedMOUSCheckinSocNwWeightedRecommender;
import recommender.clusterByHometownFineTuned.weighted.ClusteredByHometownFineTunedSocNwCFWeightedRecommender;
import recommender.postCombine.mixedBased.CheckinFSNSocNwHometownMixedBasedPostCombineRecommender;
import recommender.postCombine.mixedBased.CheckinFSNSocNwMixedBasedPostCombineRecommender;
import recommender.postCombine.mixedBased.CheckinHometownMixedBasedPostCombineRecommender;
import recommender.postCombine.mixedBased.CheckinSocNwMixedBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.asgAlpha.CheckinFSNSocNwAssignedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.asgAlpha.CheckinFSNSocNwHometownAssignedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.asgAlpha.CheckinHometownAssignedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.asgAlpha.CheckinSocNwAssignedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.eqlAlpha.CheckinFSNSocNwEqlAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.eqlAlpha.CheckinFSNSocNwHometownEqlAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.eqlAlpha.CheckinHometownEqlAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.eqlAlpha.CheckinSocNwEqlAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.rankedAlpha.CheckinFSNSocNwHometownRankedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.rankedAlpha.CheckinFSNSocNwRankedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.rankedAlpha.CheckinHometownRankedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.voteBased.rankedAlpha.CheckinSocNwRankedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.asgAlpha.CheckinFSNSocNwAssignedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.asgAlpha.CheckinFSNSocNwHometownAssignedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.asgAlpha.CheckinHometownAssignedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.asgAlpha.CheckinSocNwAssignedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.eqlAlpha.CheckinFSNSocNwEqlAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.eqlAlpha.CheckinFSNSocNwHometownEqlAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.eqlAlpha.CheckinHometownEqlAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.eqlAlpha.CheckinSocNwEqlAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.rankedAlpha.CheckinFSNSocNwHometownRankedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.rankedAlpha.CheckinFSNSocNwRankedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.rankedAlpha.CheckinHometownRankedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.rankedAlpha.CheckinSocNwRankedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postCombine.voteBased.CheckinFSNSocNwHometownVoteBasedPostCombineRecommender;
import recommender.postCombine.voteBased.CheckinFSNSocNwVoteBasedPostCombineRecommender;
import recommender.postCombine.voteBased.CheckinHometownVoteBasedPostCombineRecommender;
import recommender.postCombine.voteBased.CheckinSocNwVoteBasedPostCombineRecommender;
import recommender.postCombine.weightAvgBased.CheckinFSNSocNwHometownWeightAvgBasedPostCombineRecommender;
import recommender.postCombine.weightAvgBased.CheckinFSNSocNwWeightAvgBasedPostCombineRecommender;
import recommender.postCombine.weightAvgBased.CheckinHometownWeightAvgBasedPostCombineRecommender;
import recommender.postCombine.weightAvgBased.CheckinSocNwWeightAvgBasedPostCombineRecommender;
import recommender.postProcessByLoc.base.PostProcessedByLocCheckinCFRecommender;
import recommender.postProcessByLoc.base.PostProcessedByLocFSNRecommender;
import recommender.postProcessByLoc.base.PostProcessedByLocMOUSCheckinFSNSocNwRecommender;
import recommender.postProcessByLoc.base.PostProcessedByLocMOUSCheckinSocNwRecommender;
import recommender.postProcessByLoc.base.PostProcessedByLocSocNwCFRecommender;
import recommender.postProcessByLoc.rated.RatedPostProcessedByLocCheckinCFRecommender;
import recommender.postProcessByLoc.rated.RatedPostProcessedByLocFSNRecommender;
import recommender.postProcessByLoc.rated.RatedPostProcessedByLocMOUSCheckinFSNSocNwRecommender;
import recommender.postProcessByLoc.rated.RatedPostProcessedByLocMOUSCheckinSocNwRecommender;
import recommender.postProcessByLoc.rated.RatedPostProcessedByLocSocNwCFRecommender;
import recommender.postProcessByLoc.ratedAndWeighted.RatedPostProcessedByLocCheckinCFWeightedRecommender;
import recommender.postProcessByLoc.ratedAndWeighted.RatedPostProcessedByLocFSNWeightedRecommender;
import recommender.postProcessByLoc.ratedAndWeighted.RatedPostProcessedByLocMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.postProcessByLoc.ratedAndWeighted.RatedPostProcessedByLocMOUSCheckinSocNwWeightedRecommender;
import recommender.postProcessByLoc.ratedAndWeighted.RatedPostProcessedByLocSocNwCFWeightedRecommender;
import recommender.postProcessByLoc.weighted.PostProcessedByLocCheckinCFWeightedRecommender;
import recommender.postProcessByLoc.weighted.PostProcessedByLocFSNWeightedRecommender;
import recommender.postProcessByLoc.weighted.PostProcessedByLocMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.postProcessByLoc.weighted.PostProcessedByLocMOUSCheckinSocNwWeightedRecommender;
import recommender.postProcessByLoc.weighted.PostProcessedByLocSocNwCFWeightedRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeCheckinCFRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeFSNRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeHometownCFRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeMOUSCheckinFSNSocNwHometownRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeMOUSCheckinFSNSocNwRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeMOUSCheckinHometownFSNSocNwTimeRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeMOUSCheckinHometownFSNTimeRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeMOUSCheckinHometownRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeMOUSCheckinHometownTimeRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeMOUSCheckinSocNwRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeMOUSCheckinTimeRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeSocNwCFRecommender;
import recommender.postProcessByTime.base.PostProcessedByTimeTimeCFRecommender;
import recommender.postprocessByLocHvr.base.PostProcessedByLocHvrCheckinCFRecommender;
import recommender.postprocessByLocHvr.base.PostProcessedByLocHvrFSNRecommender;
import recommender.postprocessByLocHvr.base.PostProcessedByLocHvrMOUSCheckinFSNSocNwRecommender;
import recommender.postprocessByLocHvr.base.PostProcessedByLocHvrMOUSCheckinSocNwRecommender;
import recommender.postprocessByLocHvr.base.PostProcessedByLocHvrSocNwCFRecommender;
import recommender.postprocessByLocHvr.rated.RatedPostProcessedByLocHvrCheckinCFRecommender;
import recommender.postprocessByLocHvr.rated.RatedPostProcessedByLocHvrFSNRecommender;
import recommender.postprocessByLocHvr.rated.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwRecommender;
import recommender.postprocessByLocHvr.rated.RatedPostProcessedByLocHvrMOUSCheckinSocNwRecommender;
import recommender.postprocessByLocHvr.rated.RatedPostProcessedByLocHvrSocNwCFRecommender;
import recommender.postprocessByLocHvr.ratedAndWeighted.RatedPostProcessedByLocHvrCheckinCFWeightedRecommender;
import recommender.postprocessByLocHvr.ratedAndWeighted.RatedPostProcessedByLocHvrFSNWeightedRecommender;
import recommender.postprocessByLocHvr.ratedAndWeighted.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.postprocessByLocHvr.ratedAndWeighted.RatedPostProcessedByLocHvrMOUSCheckinSocNwWeightedRecommender;
import recommender.postprocessByLocHvr.ratedAndWeighted.RatedPostProcessedByLocHvrSocNwCFWeightedRecommender;
import recommender.postprocessByLocHvr.weighted.PostProcessedByLocHvrCheckinCFWeightedRecommender;
import recommender.postprocessByLocHvr.weighted.PostProcessedByLocHvrFSNWeightedRecommender;
import recommender.postprocessByLocHvr.weighted.PostProcessedByLocHvrMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.postprocessByLocHvr.weighted.PostProcessedByLocHvrMOUSCheckinSocNwWeightedRecommender;
import recommender.postprocessByLocHvr.weighted.PostProcessedByLocHvrSocNwCFWeightedRecommender;
import recommender.rated.RatedCheckinCFRecommender;
import recommender.rated.RatedFSNRecommender;
import recommender.rated.RatedHometownCFRecommender;
import recommender.rated.RatedHometownFineTunedCFRecommender;
import recommender.rated.RatedMOUSCheckinFSNSocNwHometownFineTunedRecommender;
import recommender.rated.RatedMOUSCheckinFSNSocNwHometownRecommender;
import recommender.rated.RatedMOUSCheckinFSNSocNwRecommender;
import recommender.rated.RatedMOUSCheckinHometownFSNSocNwTimeRecommender;
import recommender.rated.RatedMOUSCheckinHometownFSNTimeRecommender;
import recommender.rated.RatedMOUSCheckinHometownFineTunedFSNSocNwTimeRecommender;
import recommender.rated.RatedMOUSCheckinHometownFineTunedRecommender;
import recommender.rated.RatedMOUSCheckinHometownRecommender;
import recommender.rated.RatedMOUSCheckinHometownTimeRecommender;
import recommender.rated.RatedMOUSCheckinSocNwRecommender;
import recommender.rated.RatedMOUSCheckinTimeRecommender;
import recommender.rated.RatedSocNwCFRecommender;
import recommender.rated.RatedTimeCFRecommender;
import recommender.ratedAndWeighted.RatedCheckinCFWeightedRecommender;
import recommender.ratedAndWeighted.RatedFSNWeightedRecommender;
import recommender.ratedAndWeighted.RatedHometownCFWeightedRecommender;
import recommender.ratedAndWeighted.RatedHometownFineTunedCFWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinFSNSocNwHometownFineTunedWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinFSNSocNwHometownWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinFSNSocNwWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinHometownFSNSocNwTimeWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinHometownFSNTimeWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinHometownFineTunedFSNSocNwTimeWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinHometownFineTunedWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinHometownTimeWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinHometownWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinSocNwWeightedRecommender;
import recommender.ratedAndWeighted.RatedMOUSCheckinTimeWeightedRecommender;
import recommender.ratedAndWeighted.RatedSocNwCFWeightedRecommender;
import recommender.ratedAndWeighted.RatedTimeCFWeightedRecommender;
import recommender.weighted.CheckinCFWeightedRecommender;
import recommender.weighted.FSNWeightedRecommender;
import recommender.weighted.HometownCFWeightedRecommender;
import recommender.weighted.HometownFineTunedCFWeightedRecommender;
import recommender.weighted.MOUSCheckinFSNSocNwHometownFineTunedWeightedRecommender;
import recommender.weighted.MOUSCheckinFSNSocNwHometownWeightedRecommender;
import recommender.weighted.MOUSCheckinFSNSocNwWeightedRecommender;
import recommender.weighted.MOUSCheckinHometownFSNSocNwTimeWeightedRecommender;
import recommender.weighted.MOUSCheckinHometownFSNTimeWeightedRecommender;
import recommender.weighted.MOUSCheckinHometownFineTunedFSNSocNwTimeWeightedRecommender;
import recommender.weighted.MOUSCheckinHometownFineTunedWeightedRecommender;
import recommender.weighted.MOUSCheckinHometownTimeWeightedRecommender;
import recommender.weighted.MOUSCheckinHometownWeightedRecommender;
import recommender.weighted.MOUSCheckinSocNwWeightedRecommender;
import recommender.weighted.MOUSCheckinTimeWeightedRecommender;
import recommender.weighted.SocNwCFWeightedRecommender;
import recommender.weighted.TimeCFWeightedRecommender;

public class RecommenderCreater {

	Recommender recommender = null;

	/**
	 * 
	 * @param recommenderType
	 * @return
	 */
	public Recommender createRecommender(RecType recommenderType, Integer numberOfSimilarUsers, 
			Double similarUserThreshold, Double minScoreVal, Double maxScoreVal, Double outputListSize,
			Printer printer, Checkin2011DBReader reader) {
		switch(recommenderType){
		// base
		case CheckinLocCF: recommender = new CheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case FSN: recommender = new FSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case SocNwCF: recommender = new SocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case HometownCF: recommender = new HometownCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case HometownFineTunedCF: recommender = new HometownFineTunedCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinSocNw: recommender = new MOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinFSNSocNw: recommender = new MOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinFSNSocNwHometown: recommender = new MOUSCheckinFSNSocNwHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometown: recommender = new MOUSCheckinHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownFineTuned: recommender =  new MOUSCheckinHometownFineTunedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinFSNSocNwHometownFineTuned: recommender = new MOUSCheckinFSNSocNwHometownFineTunedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// weighted
		case CheckinLocCFWeighted: recommender = new CheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case FSNWeighted: recommender = new FSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case SocNwCFWeighted: recommender = new SocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case HometownCFWeighted: recommender = new HometownCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case HometownFineTunedCFWeighted: recommender = new HometownFineTunedCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinSocNwWeighted: recommender = new MOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinFSNSocNwWeighted: recommender = new MOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer,reader, recommenderType); break;
		case MOUSCheckinFSNSocNwHometownWeighted: recommender = new MOUSCheckinFSNSocNwHometownWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownWeighted: recommender = new MOUSCheckinHometownWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownFineTunedWeighted: recommender =  new MOUSCheckinHometownFineTunedWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinFSNSocNwHometownFineTunedWeighted: recommender = new MOUSCheckinFSNSocNwHometownFineTunedWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// rated
		case RatedCheckinLocCF: recommender = new RatedCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedFSN: recommender = new RatedFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedSocNwCF: recommender = new RatedSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer,reader, recommenderType); break;
		case RatedHometownCF: recommender = new RatedHometownCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedHometownFineTunedCF: recommender = new RatedHometownFineTunedCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinSocNw: recommender = new RatedMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinFSNSocNw: recommender = new RatedMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinFSNSocNwHometown: recommender = new RatedMOUSCheckinFSNSocNwHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometown: recommender = new RatedMOUSCheckinHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownFineTuned: recommender =  new RatedMOUSCheckinHometownFineTunedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinFSNSocNwHometownFineTuned: recommender = new RatedMOUSCheckinFSNSocNwHometownFineTunedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// rated & weighted
		case RatedCheckinLocCFWeighted: recommender = new RatedCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedFSNWeighted: recommender = new RatedFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedSocNwCFWeighted: recommender = new RatedSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer,reader, recommenderType); break;
		case RatedHometownCFWeighted: recommender = new RatedHometownCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedHometownFineTunedCFWeighted: recommender = new RatedHometownFineTunedCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinSocNwWeighted: recommender = new RatedMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinFSNSocNwWeighted: recommender = new RatedMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinFSNSocNwHometownWeighted: recommender = new RatedMOUSCheckinFSNSocNwHometownWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownWeighted: recommender = new RatedMOUSCheckinHometownWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownFineTunedWeighted: recommender =  new RatedMOUSCheckinHometownFineTunedWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinFSNSocNwHometownFineTunedWeighted: recommender = new RatedMOUSCheckinFSNSocNwHometownFineTunedWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// clustered by hometown
		case ClusteredByHometownCheckinLocCF: recommender = new ClusteredByHometownCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFSN: recommender = new ClusteredByHometownFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownSocNwCF: recommender = new ClusteredByHometownSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownMOUSCheckinSocNw: recommender = new ClusteredByHometownMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownMOUSCheckinFSNSocNw: recommender = new ClusteredByHometownMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownCheckinLocCFWeighted: recommender = new ClusteredByHometownCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFSNWeighted: recommender = new ClusteredByHometownFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownSocNwCFWeighted: recommender = new ClusteredByHometownSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownMOUSCheckinSocNwWeighted: recommender = new ClusteredByHometownMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownMOUSCheckinFSNSocNwWeighted: recommender = new ClusteredByHometownMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownCheckinLocCF: recommender = new RatedClusteredByHometownCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFSN: recommender = new RatedClusteredByHometownFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownSocNwCF: recommender = new RatedClusteredByHometownSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownMOUSCheckinSocNw: recommender = new RatedClusteredByHometownMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownMOUSCheckinFSNSocNw: recommender = new RatedClusteredByHometownMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownCheckinLocCFWeighted: recommender = new RatedClusteredByHometownCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFSNWeighted: recommender = new RatedClusteredByHometownFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownSocNwCFWeighted: recommender = new RatedClusteredByHometownSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownMOUSCheckinSocNwWeighted: recommender = new RatedClusteredByHometownMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownMOUSCheckinFSNSocNwWeighted: recommender = new RatedClusteredByHometownMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// clustered by friendship
		// base
		case ClusteredByFriendshipCheckinLocCF: recommender = new ClusteredByFriendshipCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipSocNwCF: recommender = new ClusteredByFriendshipSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipHometownCF: recommender = new ClusteredByFriendshipHometownCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipHometownFineTunedCF: recommender = new ClusteredByFriendshipHometownFineTunedCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinSocNw: recommender = new ClusteredByFriendshipMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinSocNwHometown: recommender = new ClusteredByFriendshipMOUSCheckinSocNwHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinHometown: recommender = new ClusteredByFriendshipMOUSCheckinHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinHometownFineTuned: recommender =  new ClusteredByFriendshipMOUSCheckinHometownFineTunedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned: recommender = new ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// weighted
		case ClusteredByFriendshipCheckinLocCFWeighted: recommender = new ClusteredByFriendshipCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipSocNwCFWeighted: recommender = new ClusteredByFriendshipSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipHometownCFWeighted: recommender = new ClusteredByFriendshipHometownCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipHometownFineTunedCFWeighted: recommender = new ClusteredByFriendshipHometownFineTunedCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinSocNwWeighted: recommender = new ClusteredByFriendshipMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinSocNwHometownWeighted: recommender = new ClusteredByFriendshipMOUSCheckinSocNwHometownWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinHometownWeighted: recommender = new ClusteredByFriendshipMOUSCheckinHometownWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted: recommender =  new ClusteredByFriendshipMOUSCheckinHometownFineTunedWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted: recommender = new ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// rated
		case RatedClusteredByFriendshipCheckinLocCF: recommender = new RatedClusteredByFriendshipCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipSocNwCF: recommender = new RatedClusteredByFriendshipSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipHometownCF: recommender = new RatedClusteredByFriendshipHometownCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipHometownFineTunedCF: recommender = new RatedClusteredByFriendshipHometownFineTunedCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinSocNw: recommender = new RatedClusteredByFriendshipMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometown: recommender = new RatedClusteredByFriendshipMOUSCheckinSocNwHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinHometown: recommender = new RatedClusteredByFriendshipMOUSCheckinHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinHometownFineTuned: recommender =  new RatedClusteredByFriendshipMOUSCheckinHometownFineTunedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned: recommender = new RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// rated & weighted
		case RatedClusteredByFriendshipCheckinLocCFWeighted: recommender = new RatedClusteredByFriendshipCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipSocNwCFWeighted: recommender = new RatedClusteredByFriendshipSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipHometownCFWeighted: recommender = new RatedClusteredByFriendshipHometownCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipHometownFineTunedCFWeighted: recommender = new RatedClusteredByFriendshipHometownFineTunedCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinSocNwWeighted: recommender = new RatedClusteredByFriendshipMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeighted: recommender = new RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinHometownWeighted: recommender = new RatedClusteredByFriendshipMOUSCheckinHometownWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted: recommender =  new RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted: recommender = new RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// clustered by hometown fine tuned
		case ClusteredByHometownFineTunedCheckinLocCF: recommender = new ClusteredByHometownFineTunedCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedFSN: recommender = new ClusteredByHometownFineTunedFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedSocNwCF: recommender = new ClusteredByHometownFineTunedSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedMOUSCheckinSocNw: recommender = new ClusteredByHometownFineTunedMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedMOUSCheckinFSNSocNw: recommender = new ClusteredByHometownFineTunedMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedCheckinLocCFWeighted: recommender = new ClusteredByHometownFineTunedCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedFSNWeighted: recommender = new ClusteredByHometownFineTunedFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedSocNwCFWeighted: recommender = new ClusteredByHometownFineTunedSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedMOUSCheckinSocNwWeighted: recommender = new ClusteredByHometownFineTunedMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted: recommender = new ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedCheckinLocCF: recommender = new RatedClusteredByHometownFineTunedCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedFSN: recommender = new RatedClusteredByHometownFineTunedFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedSocNwCF: recommender = new RatedClusteredByHometownFineTunedSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedMOUSCheckinSocNw: recommender = new RatedClusteredByHometownFineTunedMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNw: recommender = new RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedCheckinLocCFWeighted: recommender = new RatedClusteredByHometownFineTunedCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedFSNWeighted: recommender = new RatedClusteredByHometownFineTunedFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedSocNwCFWeighted: recommender = new RatedClusteredByHometownFineTunedSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeighted: recommender = new RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted: recommender = new RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;

		/// ---- postprocessed based -- compare hometown_finetuned and location fine tuned
		case PostProcessedByLocCheckinLocCF: recommender = new PostProcessedByLocCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocFSN: recommender = new PostProcessedByLocFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocSocNwCF: recommender = new PostProcessedByLocSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocMOUSCheckinSocNw: recommender = new PostProcessedByLocMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocMOUSCheckinFSNSocNw: recommender = new PostProcessedByLocMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocCheckinLocCFWeighted: recommender = new PostProcessedByLocCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocFSNWeighted:recommender = new PostProcessedByLocFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocSocNwCFWeighted: recommender = new PostProcessedByLocSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocMOUSCheckinSocNwWeighted: recommender = new PostProcessedByLocMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocMOUSCheckinFSNSocNwWeighted:recommender = new PostProcessedByLocMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocCheckinLocCF:recommender = new RatedPostProcessedByLocCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocFSN:recommender = new RatedPostProcessedByLocFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocSocNwCF:recommender = new RatedPostProcessedByLocSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocMOUSCheckinSocNw: recommender = new RatedPostProcessedByLocMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocMOUSCheckinFSNSocNw: recommender = new RatedPostProcessedByLocMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocCheckinLocCFWeighted: recommender = new RatedPostProcessedByLocCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocFSNWeighted:recommender = new RatedPostProcessedByLocFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocSocNwCFWeighted:recommender = new RatedPostProcessedByLocSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocMOUSCheckinSocNwWeighted: recommender = new RatedPostProcessedByLocMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocMOUSCheckinFSNSocNwWeighted:recommender = new RatedPostProcessedByLocMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;


		/// ---- postprocessed based -- compare hometown_finetuned and location fine tuned (in haversine dist)
		case PostProcessedByLocHvrCheckinLocCF: recommender = new PostProcessedByLocHvrCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrFSN: recommender = new PostProcessedByLocHvrFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrSocNwCF: recommender = new PostProcessedByLocHvrSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrMOUSCheckinSocNw: recommender = new PostProcessedByLocHvrMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrMOUSCheckinFSNSocNw: recommender = new PostProcessedByLocHvrMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrCheckinLocCFWeighted: recommender = new PostProcessedByLocHvrCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrFSNWeighted:recommender = new PostProcessedByLocHvrFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrSocNwCFWeighted: recommender = new PostProcessedByLocHvrSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrMOUSCheckinSocNwWeighted: recommender = new PostProcessedByLocHvrMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted:recommender = new PostProcessedByLocHvrMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrCheckinLocCF:recommender = new RatedPostProcessedByLocHvrCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrFSN:recommender = new RatedPostProcessedByLocHvrFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrSocNwCF:recommender = new RatedPostProcessedByLocHvrSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrMOUSCheckinSocNw: recommender = new RatedPostProcessedByLocHvrMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrMOUSCheckinFSNSocNw: recommender = new RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrCheckinLocCFWeighted: recommender = new RatedPostProcessedByLocHvrCheckinCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrFSNWeighted:recommender = new RatedPostProcessedByLocHvrFSNWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrSocNwCFWeighted:recommender = new RatedPostProcessedByLocHvrSocNwCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrMOUSCheckinSocNwWeighted: recommender = new RatedPostProcessedByLocHvrMOUSCheckinSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted:recommender = new RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;

		/// ---- post combine + vote based ----
		case CheckinHometownVoteBasedPostCombine: recommender = new CheckinHometownVoteBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwVoteBasedPostCombine: recommender = new CheckinSocNwVoteBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwVoteBasedPostCombine: recommender = new CheckinFSNSocNwVoteBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownVoteBasedPostCombine: recommender = new CheckinFSNSocNwHometownVoteBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;

		/// ---- post combine + vote based ----
		case CheckinHometownWeightAvgBasedPostCombine: recommender = new CheckinHometownWeightAvgBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwWeightAvgBasedPostCombine: recommender = new CheckinSocNwWeightAvgBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwWeightAvgBasedPostCombine: recommender = new CheckinFSNSocNwWeightAvgBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownWeightAvgBasedPostCombine: recommender = new CheckinFSNSocNwHometownWeightAvgBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;

		/// ---- post combine + mixed based ----
		case CheckinHometownMixedBasedPostCombine: recommender = new CheckinHometownMixedBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwMixedBasedPostCombine: recommender = new CheckinSocNwMixedBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwMixedBasedPostCombine: recommender = new CheckinFSNSocNwMixedBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownMixedBasedPostCombine: recommender = new CheckinFSNSocNwHometownMixedBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;

		/// ---- parameter usage (Ye et al,2011) + vote + eql Alpha values ---- ////
		case CheckinHometownEqlAlphaVoteParamBasedPostCombine: recommender = new CheckinHometownEqlAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwEqlAlphaVoteParamBasedPostCombine: recommender = new CheckinSocNwEqlAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwEqlAlphaVoteParamBasedPostCombine: recommender = new CheckinFSNSocNwEqlAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownEqlAlphaVoteParamBasedPostCombine: recommender = new CheckinFSNSocNwHometownEqlAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		/// ---- parameter usage (Ye et al,2011) + vote + ranked Alpha values ---- ////
		case CheckinHometownRankedAlphaVoteParamBasedPostCombine: recommender = new CheckinHometownRankedAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwRankedAlphaVoteParamBasedPostCombine: recommender = new CheckinSocNwRankedAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwRankedAlphaVoteParamBasedPostCombine: recommender = new CheckinFSNSocNwRankedAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownRankedAlphaVoteParamBasedPostCombine: recommender = new CheckinFSNSocNwHometownRankedAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		/// ---- parameter usage (Ye et al,2011) + weight + eql Alpha values ---- ////
		case CheckinHometownEqlAlphaWeightParamBasedPostCombine: recommender = new CheckinHometownEqlAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwEqlAlphaWeightParamBasedPostCombine: recommender = new CheckinSocNwEqlAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwEqlAlphaWeightParamBasedPostCombine: recommender = new CheckinFSNSocNwEqlAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownEqlAlphaWeightParamBasedPostCombine: recommender = new CheckinFSNSocNwHometownEqlAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		/// ---- parameter usage (Ye et al,2011) + weight + ranked Alpha values ---- ////
		case CheckinHometownRankedAlphaWeightParamBasedPostCombine: recommender = new CheckinHometownRankedAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwRankedAlphaWeightParamBasedPostCombine: recommender = new CheckinSocNwRankedAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwRankedAlphaWeightParamBasedPostCombine: recommender = new CheckinFSNSocNwRankedAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownRankedAlphaWeightParamBasedPostCombine: recommender = new CheckinFSNSocNwHometownRankedAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		/// ---- parameter usage (Ye et al,2011) + vote + assigned Alpha values ---- ////
		case CheckinHometownAssignedAlphaVoteParamBasedPostCombine: recommender = new CheckinHometownAssignedAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwAssignedAlphaVoteParamBasedPostCombine: recommender = new CheckinSocNwAssignedAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwAssignedAlphaVoteParamBasedPostCombine: recommender = new CheckinFSNSocNwAssignedAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownAssignedAlphaVoteParamBasedPostCombine:  recommender = new CheckinFSNSocNwHometownAssignedAlphaVoteParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		/// ---- parameter usage (Ye et al,2011) + weight + assigned Alpha values ---- ////
		case CheckinHometownAssignedAlphaWeightParamBasedPostCombine: recommender = new CheckinHometownAssignedAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinSocNwAssignedAlphaWeightParamBasedPostCombine: recommender = new CheckinSocNwAssignedAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwAssignedAlphaWeightParamBasedPostCombine: recommender = new CheckinFSNSocNwAssignedAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case CheckinFSNSocNwHometownAssignedAlphaWeightParamBasedPostCombine: recommender = new CheckinFSNSocNwHometownAssignedAlphaWeightParamBasedPostCombineRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;

		/// Time based calculations - using older base,Rate, Weight , Rate&Weight based settings
		// base
		case MOUSCheckinTime: recommender = new MOUSCheckinTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownTime:  recommender = new MOUSCheckinHometownTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownFSNTime: recommender = new MOUSCheckinHometownFSNTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownFSNSocNwTime:  recommender = new MOUSCheckinHometownFSNSocNwTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownFineTunedFSNSocNwTime: recommender = new MOUSCheckinHometownFineTunedFSNSocNwTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// weight based		
		case MOUSCheckinTimeWeighted:  recommender = new MOUSCheckinTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownTimeWeighted:  recommender = new MOUSCheckinHometownTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownFSNTimeWeighted:  recommender = new MOUSCheckinHometownFSNTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownFSNSocNwTimeWeighted: recommender = new MOUSCheckinHometownFSNSocNwTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case MOUSCheckinHometownFineTunedFSNSocNwTimeWeighted: recommender = new MOUSCheckinHometownFineTunedFSNSocNwTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// rate base
		case RatedMOUSCheckinTime:  recommender = new RatedMOUSCheckinTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownTime:  recommender = new RatedMOUSCheckinHometownTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownFSNTime:  recommender = new RatedMOUSCheckinHometownFSNTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownFSNSocNwTime:  recommender = new RatedMOUSCheckinHometownFSNSocNwTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownFineTunedFSNSocNwTime: recommender = new RatedMOUSCheckinHometownFineTunedFSNSocNwTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// rate&weight base
		case RatedMOUSCheckinTimeWeighted: recommender = new RatedMOUSCheckinTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownTimeWeighted:  recommender = new RatedMOUSCheckinHometownTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownFSNTimeWeighted:  recommender = new RatedMOUSCheckinHometownFSNTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownFSNSocNwTimeWeighted:  recommender = new RatedMOUSCheckinHometownFSNSocNwTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedMOUSCheckinHometownFineTunedFSNSocNwTimeWeighted: recommender = new RatedMOUSCheckinHometownFineTunedFSNSocNwTimeWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;	
		// time related
		case TimeCF: recommender = new TimeCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case TimeCFWeighted: recommender = new TimeCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedTimeCF: recommender = new RatedTimeCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case RatedTimeCFWeighted: recommender = new RatedTimeCFWeightedRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;

		//// dynamic time requirement of users - add post process based on users temporal preference-performed only for base settings!!!
		// single criterion
		case PostProcessedByTimeCheckinLocCF: recommender = new PostProcessedByTimeCheckinCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeFSN: recommender = new PostProcessedByTimeFSNRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeSocNwCF: recommender = new PostProcessedByTimeSocNwCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeHometownCF: recommender = new PostProcessedByTimeHometownCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeTimeCF: recommender = new PostProcessedByTimeTimeCFRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// mo-base 
		case PostProcessedByTimeMOUSCheckinSocNw: recommender = new PostProcessedByTimeMOUSCheckinSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeMOUSCheckinFSNSocNw: recommender = new PostProcessedByTimeMOUSCheckinFSNSocNwRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeMOUSCheckinFSNSocNwHometown: recommender = new PostProcessedByTimeMOUSCheckinFSNSocNwHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeMOUSCheckinHometown: recommender = new PostProcessedByTimeMOUSCheckinHometownRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		// mo-base with time
		case PostProcessedByTimeMOUSCheckinTime: recommender = new PostProcessedByTimeMOUSCheckinTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeMOUSCheckinHometownTime: recommender = new PostProcessedByTimeMOUSCheckinHometownTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeMOUSCheckinHometownFSNTime: recommender = new PostProcessedByTimeMOUSCheckinHometownFSNTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		case PostProcessedByTimeMOUSCheckinHometownFSNSocNwTime: recommender = new PostProcessedByTimeMOUSCheckinHometownFSNSocNwTimeRecommender(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal, printer, reader, recommenderType); break;
		
		default: System.out.println("Wrong recommender type!!"); System.exit(-1);
		}

		return recommender;
	}

}
