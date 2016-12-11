package recommender;



import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import main.Checkin2011Main;
import main.DbUtil;
import outputWriter.FilePrinter;
import outputWriter.Printer;
import recommender.Recommender.RecType;
import recommender.postCombine.paramBased.voteBased.AssignedAlphaVoteParamBasedPostCombineRecommender;
import recommender.postCombine.paramBased.weightBased.AssignedAlphaWeightParamBasedPostCombineRecommender;
import recommender.postprocessByLocHvr.base.PostProcessedByLocHvrCheckinCFRecommender;
import data.CheckinTime;
import data.CheckinTime.TimeCategory;
import data.Recommendation;
import data.Similarity;
import data.Similarity.SimType;
import data.UserCheckin;
import evaluater.EvaluateCheckin2011DB;



/**
 * 
 * @author mg
 *
 * Performs same jobs as RecommendationDecisionMaker, namely recommend subject,
 *  but read data from database and decides recommendations one-by-one. So that, it never
 *  saves info in memory, but in database.
 */
public class RecommendationDBDecisionMaker {
	Printer printer;
	Recommender recommender;
	EvaluateCheckin2011DB evaluator;
	Checkin2011DBReader reader;
	Integer numberOfSimilarUsers;
	Double similarUserThreshold;
	Double minScoreVal;
	Double maxScoreVal;
	Double outputListSize;

	RecommenderCreater recommenderCreater;


	/**
	 * @param printer
	 * @param recommender
	 * @param evaluator
	 * @param reader
	 */
	public RecommendationDBDecisionMaker(Printer printer, Recommender recommender,
			EvaluateCheckin2011DB evaluator, Checkin2011DBReader reader,
			Integer numberOfSimilarUsers, Double similarUserThreshold,
			Double minScoreVal, Double maxScoreVal, Double outputListSize) {
		super();
		this.printer = printer;
		this.recommender = recommender;
		this.evaluator = evaluator;
		this.reader = reader;
		this.numberOfSimilarUsers = numberOfSimilarUsers;
		this.similarUserThreshold = similarUserThreshold;
		this.minScoreVal = minScoreVal;
		this.maxScoreVal = maxScoreVal;
		this.outputListSize = outputListSize;

		recommenderCreater = new RecommenderCreater();
	}

	public RecommendationDBDecisionMaker(RecType recommenderType,Integer numberOfSimilarUsers, 
			Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Double outputListSize) {
		super();
		this.printer = new FilePrinter(true);
		this.reader = new Checkin2011DBReader();
		this.numberOfSimilarUsers = numberOfSimilarUsers;
		this.similarUserThreshold = userSimilarityThreshold;
		this.minScoreVal = minScoreVal;
		this.maxScoreVal = maxScoreVal;
		this.outputListSize =  outputListSize;

		recommenderCreater = new RecommenderCreater();
		this.recommender = recommenderCreater.createRecommender(recommenderType, numberOfSimilarUsers, 
				similarUserThreshold, minScoreVal, maxScoreVal, outputListSize,
				printer, reader);
		this.evaluator = new EvaluateCheckin2011DB(recommender,printer);

	}

	/**
	 * Evaluate recommendations
	 * @param recType
	 * @param outputListSize
	 */
	public void evaluate(RecType recType, Double outputListSize) {
		evaluator.evaluateResult(recType, outputListSize);
	}

	/**
	 * Evaluate recommendations TODO why do I need a distThresh parameter?
	 * @param recType
	 * @param outputListSize
	 * @param distThresh
	 */
	public void evaluate(RecType recType, Double outputListSize,
			Double distThresh) {
		evaluator.evaluateResult(recType, outputListSize, distThresh);

	}

	/**
	 * Evluate recommendations, considering alpha values (weight based setting)
	 * @param recType
	 * @param outputListSize
	 */
	public void evaluateByAsgAlphaWeight(RecType recType, Double outputListSize) {
		AssignedAlphaWeightParamBasedPostCombineRecommender asgRecomender = (AssignedAlphaWeightParamBasedPostCombineRecommender) recommender;
		List<Double[]> alphaValsList = asgRecomender.getAlphaValList();
		for(Double[] alphaVals: alphaValsList){
			evaluator.evaluateResult(recType, outputListSize, alphaVals);
		}
	}

	/**
	 * Evluate recommendations, considering alpha values (vote based setting)
	 * @param recType
	 * @param outputListSize
	 */
	public void evaluateByAsgAlphaVote(RecType recType, Double outputListSize) {
		AssignedAlphaVoteParamBasedPostCombineRecommender asgRecomender = (AssignedAlphaVoteParamBasedPostCombineRecommender) recommender;
		List<Double[]> alphaValsList = asgRecomender.getAlphaValList();
		for(Double[] alphaVals: alphaValsList){
			evaluator.evaluateResult(recType, outputListSize, alphaVals);
		}
	}
	
	/**
	 * Evluate recommendations, considering timeCategory
	 * @param recType
	 * @param outputListSize
	 */
	public void evaluateByTimeCategory(RecType recType, Double outputListSize) {
		for(CheckinTime.TimeCategory timeCategory:CheckinTime.TimeCategory.values()){
			evaluator.evaluateResult(recType, outputListSize, timeCategory);
		}
	}


	/**
	 * For each user calculate recommendations 
	 * @param recommenderType
	 * @return 
	 */
	public void recommend(RecType recommenderType) {
		// get connection
		Connection con=null;
		try {
			con = DbUtil.getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// do not forget to create a new recommendation table for each recommendation!!
		recommender.createRecTable(Checkin2011Main.dbName, con);

		// get users 
		String tableName = "checkinsJan";//getTableName(recommender.getType()).get(0);//TODO why 0
		String columnName = "userid";
		HashSet<Integer> controlUserIdList = DbUtil.getIds(con, tableName, columnName);


		// perform calculations for each user
		for(Integer userid: controlUserIdList)
		{
			System.out.println("---------------------------------------------------");
			System.out.println(userid);
			System.out.println("---------------------------------------------------");


			// control if loop is runned for this user already
			boolean isRecommended = DbUtil.doesContainUserid
					(userid, "userid", Checkin2011Main.dbName , con);
			if(isRecommended == true){
				// is already recommended
				continue;
			}

			if(isPostProcessBased(recommenderType) && isPostProcessedByLocHvr(recommenderType)){
				for(Double distThresh : PostProcessedByLocHvrCheckinCFRecommender.distThresholdList){


					// clean recomender-and create new one for each user!!TODO tasarimi berbat !!
					this.recommender = null;
					this.recommender = recommenderCreater.createRecommender(recommenderType, numberOfSimilarUsers, 
							similarUserThreshold, minScoreVal, maxScoreVal, outputListSize,
							printer, reader);
					PostProcessByLocHvrRecommender postProcessByLocHvrRecommender = (PostProcessByLocHvrRecommender) recommender;
					postProcessByLocHvrRecommender.setDistThreshold(distThresh);


					// create user, using userid 
					// NOTE later in readInputs all fields for the user will be populated
					UserCheckin user = new UserCheckin(userid);


					try {
						// read info of the user
						boolean readHometown = false;
						boolean readFriends = false;
						boolean readCheckins = true;
						boolean readRatings = true;
						boolean readCheckinTimes = false;
						reader.readInputs(recommenderType, user, con,
								readHometown,readFriends, readCheckins, readCheckinTimes, readRatings);


						// find activity-recProbabilty by collobarative filtering
						ArrayList<Recommendation> rec = postProcessByLocHvrRecommender.recommend(user, con, outputListSize);

						// populate database table with recommendations
						recommender.populateDatabase(Checkin2011Main.dbName, userid, rec, con);

					} catch(Exception e){
						e.printStackTrace();
						System.exit(-1);
					}
				}
			} else if(isPostProcessBased(recommenderType) && isPostProcessedByTime(recommenderType)){
				// post process by time 
				CheckinTime.TimeCategory[] timeCategoryList = CheckinTime.TimeCategory.values();
				for(CheckinTime.TimeCategory category: timeCategoryList){
					// clean recomender-and create new one for each user!!TODO tasarimi berbat !!
					this.recommender = null;
					this.recommender = recommenderCreater.createRecommender(recommenderType, numberOfSimilarUsers, 
							similarUserThreshold, minScoreVal, maxScoreVal, outputListSize,
							printer, reader);

					PostProcessByTimeRecommender postProcessByTimeRecommender = (PostProcessByTimeRecommender) recommender;
					postProcessByTimeRecommender.setChosenTimeCategory(category);

					// create user, using userid 
					// NOTE later in readInputs all fields for the user will be populated
					UserCheckin user = new UserCheckin(userid);


					try {
						// read info of the user
						boolean readHometown = false;
						boolean readFriends = false;
						boolean readCheckins = true;
						boolean readRatings = true;
						boolean readCheckinTimes = true;
						reader.readInputs(recommenderType, user, con,readHometown,readFriends, readCheckins, 
								readCheckinTimes, readRatings);


						// find activity-recProbabilty by collobarative filtering
						ArrayList<Recommendation> rec = postProcessByTimeRecommender.recommend(user, con, outputListSize);

						// populate database table with recommendations
						postProcessByTimeRecommender.populateDatabase(Checkin2011Main.dbName, userid, rec, con);



					} catch(Exception e){
						e.printStackTrace();
						System.exit(-1);
					}
				}
			} else if(isAssignedAlphaVoteParamBased(recommenderType)){
				try {
					// clean recomender-and create new one for each user!!TODO tasarimi berbat !!
					this.recommender = null;
					this.recommender = recommenderCreater.createRecommender(recommenderType, numberOfSimilarUsers, 
							similarUserThreshold, minScoreVal, maxScoreVal, outputListSize,
							printer, reader);
					AssignedAlphaVoteParamBasedPostCombineRecommender asgAlphaVoteParamBasedPostCombineRecommender = (AssignedAlphaVoteParamBasedPostCombineRecommender) recommender;

					for(Double[] alphaValues: asgAlphaVoteParamBasedPostCombineRecommender.getAlphaValList()){
						asgAlphaVoteParamBasedPostCombineRecommender.setAlphaValues(alphaValues);

						// create user, using userid 
						// NOTE later in readInputs all fields for the user will be populated
						UserCheckin user = new UserCheckin(userid);

						// read info of the user
						boolean readHometown = false;
						boolean readFriends = false;
						boolean readCheckins = true;
						boolean readRatings = true;
						boolean readCheckinTimes = false;
						reader.readInputs(recommenderType, user, con,
								readHometown,readFriends, readCheckins, 
								readCheckinTimes, readRatings);


						// find activity-recProbabilty by collobarative filtering
						ArrayList<Recommendation> rec = asgAlphaVoteParamBasedPostCombineRecommender.recommend(user, con, outputListSize);

						// populate database table with recommendations
						recommender.populateDatabase(Checkin2011Main.dbName, userid, rec, con);

						user = null;
					}
				} catch(Exception e){
					e.printStackTrace();
					System.exit(-1);
				}

			}else if(isAssignedAlphaWeightParamBased(recommenderType)){
				// create user, using userid 
				// NOTE later in readInputs all fields for the user will be populated
				UserCheckin user = new UserCheckin(userid);

				try {
					// read info of the user
					boolean readHometown = false;
					boolean readFriends = false;
					boolean readCheckins = true;
					boolean readRatings = true;
					boolean readCheckinTimes = false;
					reader.readInputs(recommenderType, user, con,
							readHometown,readFriends, readCheckins, 
							readCheckinTimes, readRatings);


					// clean recomender-and create new one for each user!!TODO tasarimi berbat !!
					this.recommender = null;
					this.recommender = recommenderCreater.createRecommender(recommenderType, numberOfSimilarUsers, 
							similarUserThreshold, minScoreVal, maxScoreVal, outputListSize,
							printer, reader);
					AssignedAlphaWeightParamBasedPostCombineRecommender asgAlphaWeightParamBasedPostCombineRecommender = (AssignedAlphaWeightParamBasedPostCombineRecommender) recommender;

					for(Double[] alphaValues: asgAlphaWeightParamBasedPostCombineRecommender.getAlphaValList()){
						asgAlphaWeightParamBasedPostCombineRecommender.setAlphaValues(alphaValues);

						// find activity-recProbabilty by collobarative filtering
						ArrayList<Recommendation> rec = asgAlphaWeightParamBasedPostCombineRecommender.recommend(user, con, outputListSize);

						// populate database table with recommendations
						recommender.populateDatabase(Checkin2011Main.dbName, userid, rec, con);


					}
				} catch(Exception e){
					e.printStackTrace();
					System.exit(-1);
				}
			} else {
				// clean recomender-and create new one for each user!!TODO tasarimi berbat !!
				this.recommender = null;
				this.recommender = recommenderCreater.createRecommender(recommenderType, numberOfSimilarUsers, 
						similarUserThreshold, minScoreVal, maxScoreVal, outputListSize,
						printer, reader);

				// create user, using userid 
				// NOTE later in readInputs all fields for the user will be populated
				UserCheckin user = new UserCheckin(userid);


				try {
					// read info of the user
					boolean readHometown = false;
					boolean readFriends = false;
					boolean readCheckins = true;
					boolean readRatings = true;
					boolean readCheckinTimes = false;
					reader.readInputs(recommenderType, user, con,
							readHometown,readFriends, readCheckins, 
							readCheckinTimes, readRatings);


					// find activity-recProbabilty by collobarative filtering
					ArrayList<Recommendation> rec = recommender.recommend(user, con, outputListSize);

					// populate database table with recommendations
					recommender.populateDatabase(Checkin2011Main.dbName, userid, rec, con);



				} catch(Exception e){
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}

		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<String> getSimName(RecType recType) {
		// TODO make this parametric according to sth else
		ArrayList<String> simName = new ArrayList<>();
		switch(recType){
		case CheckinLocCF: 
		case CheckinLocCFWeighted:
			simName.add("checkinLocSimCos"); break;
		case FSN:
		case FSNWeighted:
			simName.add("friendship"); break;
		case SocNwCF: 
		case SocNwCFWeighted:
			simName.add("friendSimCos"); break;
		case HometownCF: 
		case HometownCFWeighted:
			simName.add("hometownSim");  break;
		case HometownFineTunedCF: 
		case HometownFineTunedCFWeighted:
			simName.add("hometownFineTunedSim");  break;
		case MOUSCheckinSocNw:
		case MOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendSimCos");break;
		case MOUSCheckinFSNSocNw:
		case MOUSCheckinFSNSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
		case MOUSCheckinFSNSocNwHometown:
		case MOUSCheckinFSNSocNwHometownWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("hometownSim"); break;
		case MOUSCheckinHometown: 
		case MOUSCheckinHometownWeighted: 
			simName.add("checkinLocSimCos");  
			simName.add("hometownSim");break;
		case MOUSCheckinHometownFineTuned:
		case MOUSCheckinHometownFineTunedWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("hometownFineTunedSim");  break;
		case MOUSCheckinFSNSocNwHometownFineTuned:
		case MOUSCheckinFSNSocNwHometownFineTunedWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("hometownFineTunedSim");  break;
		case RatedCheckinLocCF: 
		case RatedCheckinLocCFWeighted:
			simName.add("checkinLocSimCosRated"); break;
		case RatedFSN:
		case RatedFSNWeighted:
			simName.add("friendship"); break;
		case RatedSocNwCF: 
		case RatedSocNwCFWeighted: 
			simName.add("friendSimCos"); break;
		case RatedHometownCF:
		case RatedHometownCFWeighted:
			simName.add("hometownSim");  break;
		case RatedHometownFineTunedCF:
		case RatedHometownFineTunedCFWeighted:
			simName.add("hometownFineTunedSim");  break;
		case RatedMOUSCheckinSocNw:
		case RatedMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendSimCos");break;
		case RatedMOUSCheckinFSNSocNw:
		case RatedMOUSCheckinFSNSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
		case RatedMOUSCheckinFSNSocNwHometown:
		case RatedMOUSCheckinFSNSocNwHometownWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("hometownSim"); break;
		case RatedMOUSCheckinHometown:
		case RatedMOUSCheckinHometownWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("hometownSim");break;
		case RatedMOUSCheckinHometownFineTuned:
		case RatedMOUSCheckinHometownFineTunedWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("hometownFineTunedSim");break;
		case RatedMOUSCheckinFSNSocNwHometownFineTuned:
		case RatedMOUSCheckinFSNSocNwHometownFineTunedWeighted: 
			simName.add("checkinLocSimCosRated");  
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("hometownFineTunedSim"); break;
			/// cluster by hometown
		case ClusteredByHometownCheckinLocCF:
		case ClusteredByHometownCheckinLocCFWeighted:
			simName.add("checkinLocSimCos"); break;
		case ClusteredByHometownFSN:
		case ClusteredByHometownFSNWeighted:
			simName.add("friendship"); break;
		case ClusteredByHometownSocNwCF:
		case ClusteredByHometownSocNwCFWeighted:
			simName.add("friendSimCos"); break;
		case ClusteredByHometownMOUSCheckinSocNw:
		case ClusteredByHometownMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendSimCos");break;
		case ClusteredByHometownMOUSCheckinFSNSocNw:
		case ClusteredByHometownMOUSCheckinFSNSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
		case RatedClusteredByHometownCheckinLocCF: 
		case RatedClusteredByHometownCheckinLocCFWeighted:
			simName.add("checkinLocSimCosRated"); break;
		case RatedClusteredByHometownFSN:
		case RatedClusteredByHometownFSNWeighted:
			simName.add("friendship"); break;
		case RatedClusteredByHometownSocNwCF: 
		case RatedClusteredByHometownSocNwCFWeighted: 
			simName.add("friendSimCos"); break;
		case RatedClusteredByHometownMOUSCheckinSocNw:
		case RatedClusteredByHometownMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendSimCos");break;
		case RatedClusteredByHometownMOUSCheckinFSNSocNw:
		case RatedClusteredByHometownMOUSCheckinFSNSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
			/// cluster by friendship
		case ClusteredByFriendshipCheckinLocCF: 
		case ClusteredByFriendshipCheckinLocCFWeighted:
			simName.add("checkinLocSimCos"); break;
		case ClusteredByFriendshipSocNwCF: 
		case ClusteredByFriendshipSocNwCFWeighted:
			simName.add("friendSimCos"); break;
		case ClusteredByFriendshipHometownCF: 
		case ClusteredByFriendshipHometownCFWeighted:
			simName.add("hometownSim");  break;
		case ClusteredByFriendshipHometownFineTunedCF: 
		case ClusteredByFriendshipHometownFineTunedCFWeighted:
			simName.add("hometownFineTunedSim");  break;
		case ClusteredByFriendshipMOUSCheckinSocNw:
		case ClusteredByFriendshipMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendSimCos");break;
		case ClusteredByFriendshipMOUSCheckinSocNwHometown:
		case ClusteredByFriendshipMOUSCheckinSocNwHometownWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendSimCos");
			simName.add("hometownSim"); break;
		case ClusteredByFriendshipMOUSCheckinHometown: 
		case ClusteredByFriendshipMOUSCheckinHometownWeighted: 
			simName.add("checkinLocSimCos");  
			simName.add("hometownSim");break;
		case ClusteredByFriendshipMOUSCheckinHometownFineTuned:
		case ClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("hometownFineTunedSim");  break;
		case ClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned:
		case ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendSimCos");
			simName.add("hometownFineTunedSim");  break;
		case RatedClusteredByFriendshipCheckinLocCF: 
		case RatedClusteredByFriendshipCheckinLocCFWeighted:
			simName.add("checkinLocSimCosRated"); break;
		case RatedClusteredByFriendshipSocNwCF: 
		case RatedClusteredByFriendshipSocNwCFWeighted: 
			simName.add("friendSimCos"); break;
		case RatedClusteredByFriendshipHometownCF:
		case RatedClusteredByFriendshipHometownCFWeighted:
			simName.add("hometownSim");  break;
		case RatedClusteredByFriendshipHometownFineTunedCF:
		case RatedClusteredByFriendshipHometownFineTunedCFWeighted:
			simName.add("hometownFineTunedSim");  break;
		case RatedClusteredByFriendshipMOUSCheckinSocNw:
		case RatedClusteredByFriendshipMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendSimCos");break;
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometown:
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendSimCos");
			simName.add("hometownSim"); break;
		case RatedClusteredByFriendshipMOUSCheckinHometown:
		case RatedClusteredByFriendshipMOUSCheckinHometownWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("hometownSim");break;
		case RatedClusteredByFriendshipMOUSCheckinHometownFineTuned:
		case RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("hometownFineTunedSim");break;
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned:
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted: 
			simName.add("checkinLocSimCosRated");  
			simName.add("friendSimCos");
			simName.add("hometownFineTunedSim"); break;
			/// cluster by hometown fine tuned
		case ClusteredByHometownFineTunedCheckinLocCF:
		case ClusteredByHometownFineTunedCheckinLocCFWeighted:
			simName.add("checkinLocSimCos"); break;
		case ClusteredByHometownFineTunedFSN:
		case ClusteredByHometownFineTunedFSNWeighted:
			simName.add("friendship"); break;
		case ClusteredByHometownFineTunedSocNwCF:
		case ClusteredByHometownFineTunedSocNwCFWeighted:
			simName.add("friendSimCos"); break;
		case ClusteredByHometownFineTunedMOUSCheckinSocNw:
		case ClusteredByHometownFineTunedMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendSimCos");break;
		case ClusteredByHometownFineTunedMOUSCheckinFSNSocNw:
		case ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
		case RatedClusteredByHometownFineTunedCheckinLocCF: 
		case RatedClusteredByHometownFineTunedCheckinLocCFWeighted:
			simName.add("checkinLocSimCosRated"); break;
		case RatedClusteredByHometownFineTunedFSN:
		case RatedClusteredByHometownFineTunedFSNWeighted:
			simName.add("friendship"); break;
		case RatedClusteredByHometownFineTunedSocNwCF: 
		case RatedClusteredByHometownFineTunedSocNwCFWeighted: 
			simName.add("friendSimCos"); break;
		case RatedClusteredByHometownFineTunedMOUSCheckinSocNw:
		case RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendSimCos");break;
		case RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNw:
		case RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
			/// post process by loc fine tuned
		case PostProcessedByLocCheckinLocCF:
		case PostProcessedByLocCheckinLocCFWeighted:
		case PostProcessedByLocHvrCheckinLocCF:
		case PostProcessedByLocHvrCheckinLocCFWeighted:
			simName.add("checkinLocSimCos"); break;
		case PostProcessedByLocFSN:
		case PostProcessedByLocFSNWeighted:
		case PostProcessedByLocHvrFSN:
		case PostProcessedByLocHvrFSNWeighted:
			simName.add("friendship"); break;
		case PostProcessedByLocSocNwCF:
		case PostProcessedByLocSocNwCFWeighted:
		case PostProcessedByLocHvrSocNwCF:
		case PostProcessedByLocHvrSocNwCFWeighted:
			simName.add("friendSimCos"); break;
		case PostProcessedByLocMOUSCheckinSocNw:
		case PostProcessedByLocMOUSCheckinSocNwWeighted:
		case PostProcessedByLocHvrMOUSCheckinSocNw:
		case PostProcessedByLocHvrMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendSimCos");break;
		case PostProcessedByLocMOUSCheckinFSNSocNw:
		case PostProcessedByLocMOUSCheckinFSNSocNwWeighted:
		case PostProcessedByLocHvrMOUSCheckinFSNSocNw:
		case PostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
		case RatedPostProcessedByLocCheckinLocCF: 
		case RatedPostProcessedByLocCheckinLocCFWeighted:
		case RatedPostProcessedByLocHvrCheckinLocCF: 
		case RatedPostProcessedByLocHvrCheckinLocCFWeighted:
			simName.add("checkinLocSimCosRated"); break;
		case RatedPostProcessedByLocFSN:
		case RatedPostProcessedByLocFSNWeighted:
		case RatedPostProcessedByLocHvrFSN:
		case RatedPostProcessedByLocHvrFSNWeighted:
			simName.add("friendship"); break;
		case RatedPostProcessedByLocSocNwCF: 
		case RatedPostProcessedByLocSocNwCFWeighted: 
		case RatedPostProcessedByLocHvrSocNwCF: 
		case RatedPostProcessedByLocHvrSocNwCFWeighted: 
			simName.add("friendSimCos"); break;
		case RatedPostProcessedByLocMOUSCheckinSocNw:
		case RatedPostProcessedByLocMOUSCheckinSocNwWeighted:
		case RatedPostProcessedByLocHvrMOUSCheckinSocNw:
		case RatedPostProcessedByLocHvrMOUSCheckinSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendSimCos");break;
		case RatedPostProcessedByLocMOUSCheckinFSNSocNw:
		case RatedPostProcessedByLocMOUSCheckinFSNSocNwWeighted:
		case RatedPostProcessedByLocHvrMOUSCheckinFSNSocNw:
		case RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
			/// time
		case TimeCF: 
		case TimeCFWeighted:
			simName.add("timeSimCos"); break;
		case MOUSCheckinTime:
		case MOUSCheckinTimeWeighted:
			simName.add("checkinLocSimCos");  
			simName.add("timeSimCos");break;
		case MOUSCheckinHometownTime:
		case MOUSCheckinHometownTimeWeighted:
			simName.add("checkinLocSimCos"); 
			simName.add("hometownSim");
			simName.add("timeSimCos");break;
		case MOUSCheckinHometownFSNTime:
		case MOUSCheckinHometownFSNTimeWeighted:
			simName.add("checkinLocSimCos"); 
			simName.add("hometownSim");
			simName.add("friendship");
			simName.add("timeSimCos");break;
		case MOUSCheckinHometownFSNSocNwTime:
		case MOUSCheckinHometownFSNSocNwTimeWeighted:
			simName.add("checkinLocSimCos"); 
			simName.add("hometownSim");
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("timeSimCos");break;
		case MOUSCheckinHometownFineTunedFSNSocNwTime:
		case MOUSCheckinHometownFineTunedFSNSocNwTimeWeighted:
			simName.add("checkinLocSimCos"); 
			simName.add("hometownFineTunedSim");
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("timeSimCos");break;
		case RatedTimeCF: 
		case RatedTimeCFWeighted:
			simName.add("timeSimCos"); break;
		case RatedMOUSCheckinTime:
		case RatedMOUSCheckinTimeWeighted:
			simName.add("checkinLocSimCosRated");  
			simName.add("timeSimCos");break;
		case RatedMOUSCheckinHometownTime:
		case RatedMOUSCheckinHometownTimeWeighted:
			simName.add("checkinLocSimCosRated"); 
			simName.add("hometownSim");
			simName.add("timeSimCos");break;
		case RatedMOUSCheckinHometownFSNTime:
		case RatedMOUSCheckinHometownFSNTimeWeighted:
			simName.add("checkinLocSimCosRated"); 
			simName.add("hometownSim");
			simName.add("friendship");
			simName.add("timeSimCos");break;
		case RatedMOUSCheckinHometownFSNSocNwTime:
		case RatedMOUSCheckinHometownFSNSocNwTimeWeighted:
			simName.add("checkinLocSimCosRated"); 
			simName.add("hometownSim");
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("timeSimCos");break;
		case RatedMOUSCheckinHometownFineTunedFSNSocNwTime:
		case RatedMOUSCheckinHometownFineTunedFSNSocNwTimeWeighted:
			simName.add("checkinLocSimCosRated"); 
			simName.add("hometownFineTunedSim");
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("timeSimCos");break;
			//// dynamic time requirement of users - add post process based on users temporal preference
			/// performed only for base settings!!!
			///--- single criterion
		case PostProcessedByTimeCheckinLocCF:
			simName.add("checkinLocSimCos"); break;
		case PostProcessedByTimeFSN: 
			simName.add("friendship"); break;
		case PostProcessedByTimeSocNwCF:
			simName.add("friendSimCos"); break;
		case PostProcessedByTimeHometownCF:
			simName.add("hometownSim");  break;
		case PostProcessedByTimeTimeCF:
			simName.add("timeSimCos"); 
			break;
			// mo-base 
		case PostProcessedByTimeMOUSCheckinSocNw:
			simName.add("checkinLocSimCos");  
			simName.add("friendSimCos");break;
		case PostProcessedByTimeMOUSCheckinFSNSocNw:
			simName.add("checkinLocSimCos");  
			simName.add("friendship");
			simName.add("friendSimCos");break;
		case PostProcessedByTimeMOUSCheckinFSNSocNwHometown:
			simName.add("checkinLocSimCos");  
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("hometownSim"); break;
		case PostProcessedByTimeMOUSCheckinHometown:
			simName.add("checkinLocSimCos");  
			simName.add("hometownSim");break;
			// mo-base with time
		case PostProcessedByTimeMOUSCheckinTime:
			simName.add("checkinLocSimCos");  
			simName.add("timeSimCos");
			break;
		case PostProcessedByTimeMOUSCheckinHometownTime:
			simName.add("checkinLocSimCos");  
			simName.add("hometownSim");
			simName.add("timeSimCos");
			break;
		case PostProcessedByTimeMOUSCheckinHometownFSNTime:
			simName.add("checkinLocSimCos"); 
			simName.add("hometownSim");
			simName.add("friendship");
			simName.add("timeSimCos");
			break;
		case PostProcessedByTimeMOUSCheckinHometownFSNSocNwTime:
			simName.add("checkinLocSimCos"); 
			simName.add("hometownSim");
			simName.add("friendship");
			simName.add("friendSimCos");
			simName.add("timeSimCos");
			break;


		default : 
			System.out.println("Error in sim names!!! ");
			System.exit(-1);
		}

		return simName;
	}

	public static ArrayList<String> getClusterSimName(RecType recType) {
		ArrayList<String> clusterSimName = new ArrayList<>();
		switch(recType){
		// cluster by hometown
		case ClusteredByHometownCheckinLocCF:
		case ClusteredByHometownFSN:
		case ClusteredByHometownSocNwCF:
		case ClusteredByHometownMOUSCheckinSocNw:
		case ClusteredByHometownMOUSCheckinFSNSocNw:
		case ClusteredByHometownCheckinLocCFWeighted:
		case ClusteredByHometownFSNWeighted:
		case ClusteredByHometownSocNwCFWeighted:
		case ClusteredByHometownMOUSCheckinSocNwWeighted:
		case ClusteredByHometownMOUSCheckinFSNSocNwWeighted:
		case RatedClusteredByHometownCheckinLocCF:
		case RatedClusteredByHometownFSN:
		case RatedClusteredByHometownSocNwCF:
		case RatedClusteredByHometownMOUSCheckinSocNw:
		case RatedClusteredByHometownMOUSCheckinFSNSocNw:
		case RatedClusteredByHometownCheckinLocCFWeighted:
		case RatedClusteredByHometownFSNWeighted:
		case RatedClusteredByHometownSocNwCFWeighted:
		case RatedClusteredByHometownMOUSCheckinSocNwWeighted:
		case RatedClusteredByHometownMOUSCheckinFSNSocNwWeighted:
			clusterSimName.add("hometownSim"); break;
			/// cluster by friendship	
		case ClusteredByFriendshipCheckinLocCF:
		case ClusteredByFriendshipSocNwCF:
		case ClusteredByFriendshipHometownCF:
		case ClusteredByFriendshipHometownFineTunedCF:
		case ClusteredByFriendshipMOUSCheckinSocNw:
		case ClusteredByFriendshipMOUSCheckinHometown:
		case ClusteredByFriendshipMOUSCheckinHometownFineTuned:
		case ClusteredByFriendshipMOUSCheckinSocNwHometown:
		case ClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned:
		case ClusteredByFriendshipCheckinLocCFWeighted:
		case ClusteredByFriendshipSocNwCFWeighted:
		case ClusteredByFriendshipHometownCFWeighted:
		case ClusteredByFriendshipHometownFineTunedCFWeighted:
		case ClusteredByFriendshipMOUSCheckinSocNwWeighted:
		case ClusteredByFriendshipMOUSCheckinHometownWeighted:
		case ClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted:
		case ClusteredByFriendshipMOUSCheckinSocNwHometownWeighted:
		case ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted:
		case RatedClusteredByFriendshipCheckinLocCF:
		case RatedClusteredByFriendshipSocNwCF:
		case RatedClusteredByFriendshipHometownCF:
		case RatedClusteredByFriendshipHometownFineTunedCF:
		case RatedClusteredByFriendshipMOUSCheckinSocNw:
		case RatedClusteredByFriendshipMOUSCheckinHometown:
		case RatedClusteredByFriendshipMOUSCheckinHometownFineTuned:
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometown:
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned:
		case RatedClusteredByFriendshipCheckinLocCFWeighted:
		case RatedClusteredByFriendshipSocNwCFWeighted:
		case RatedClusteredByFriendshipHometownCFWeighted:
		case RatedClusteredByFriendshipHometownFineTunedCFWeighted:
		case RatedClusteredByFriendshipMOUSCheckinSocNwWeighted:
		case RatedClusteredByFriendshipMOUSCheckinHometownWeighted:
		case RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted:
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeighted:
		case RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted:
			clusterSimName.add("friendship"); break;
			// cluster by hometown fine tuned
		case ClusteredByHometownFineTunedCheckinLocCF:
		case ClusteredByHometownFineTunedFSN:
		case ClusteredByHometownFineTunedSocNwCF:
		case ClusteredByHometownFineTunedMOUSCheckinSocNw:
		case ClusteredByHometownFineTunedMOUSCheckinFSNSocNw:
		case ClusteredByHometownFineTunedCheckinLocCFWeighted:
		case ClusteredByHometownFineTunedFSNWeighted:
		case ClusteredByHometownFineTunedSocNwCFWeighted:
		case ClusteredByHometownFineTunedMOUSCheckinSocNwWeighted:
		case ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted:
		case RatedClusteredByHometownFineTunedCheckinLocCF:
		case RatedClusteredByHometownFineTunedFSN:
		case RatedClusteredByHometownFineTunedSocNwCF:
		case RatedClusteredByHometownFineTunedMOUSCheckinSocNw:
		case RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNw:
		case RatedClusteredByHometownFineTunedCheckinLocCFWeighted:
		case RatedClusteredByHometownFineTunedFSNWeighted:
		case RatedClusteredByHometownFineTunedSocNwCFWeighted:
		case RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeighted:
		case RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted:
			clusterSimName.add("hometownFineTunedSim"); break;
		default : break;
		}
		return clusterSimName;
	}

	public static SimType mapFieldNameToSimType(String fName) {
		SimType simType = null;
		switch(fName){
		case "checkinLocSimCos": 
		case "checkinLocSimCosRated":
			simType = SimType.CHECKIN; break;
		case "friendSimCos": 
			simType = SimType.SOCIALNW; break;
		case "friendship": 
			simType = SimType.FSN; break;
		case "hometownSim": 
		case "hometownFineTunedSim": 
			simType = SimType.HOMETOWN; break; 
		case "timeSimCos":
			simType = SimType.TIME; break;
		default:  break;
		}
		return simType;
	}

	/**
	 * Decide rankScore of simType based on the rankings provided
	 * e.g. Rank: 0-1-2-3-4-5
	 *     Score: 6-5-4-3-2-1 (we dont want to make the last one has 0 score!!)
	 * @param simType
	 * @return
	 */
	public static Double getRankScore(SimType simType){
		Double rankScore = 0.0 ;

		Double size = (double) Similarity.rankedListOfSimTypes.length;
		Double index = findIndex(Similarity.rankedListOfSimTypes, simType);
		if(index.equals(-1.0)){
			System.out.println("Something is wrong, control the simType and the rankedListOfSimTypes");
			System.exit(-1);
		}
		rankScore = size-index; 

		return rankScore;
	}

	private static Double findIndex(SimType[] rankedListOfSimTypes,
			SimType simType) {
		Double index = -1.0;
		Double size = (double) Similarity.rankedListOfSimTypes.length;
		for(int i = 0; i < size; i++){
			if(Similarity.rankedListOfSimTypes[i].equals(simType)){
				index = (double) i;
				break;
			}
		}

		return index;
	}

	public static boolean isRateBased(RecType recommenderType) {
		boolean rateBased = false;

		if(recommenderType == RecType.RatedCheckinLocCF || recommenderType == RecType.RatedFSN
				|| recommenderType == RecType.RatedHometownCF || recommenderType == RecType.RatedSocNwCF
				|| recommenderType == RecType.RatedTimeCF 
				|| recommenderType == RecType.RatedMOUSCheckinSocNw
				|| recommenderType == RecType.RatedMOUSCheckinFSNSocNw 
				|| recommenderType == RecType.RatedMOUSCheckinFSNSocNwHometown 
				|| recommenderType == RecType.RatedMOUSCheckinHometown
				|| recommenderType == RecType.RatedHometownFineTunedCF
				|| recommenderType == RecType.RatedMOUSCheckinFSNSocNwHometownFineTuned
				|| recommenderType == RecType.RatedMOUSCheckinHometownFineTuned
				|| recommenderType == RecType.RatedClusteredByHometownCheckinLocCF 
				|| recommenderType == RecType.RatedClusteredByHometownFSN
				|| recommenderType == RecType.RatedClusteredByHometownSocNwCF 
				|| recommenderType == RecType.RatedClusteredByHometownMOUSCheckinSocNw
				|| recommenderType == RecType.RatedClusteredByHometownMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedClusteredByFriendshipCheckinLocCF
				|| recommenderType == RecType.RatedClusteredByFriendshipSocNwCF
				|| recommenderType == RecType.RatedClusteredByFriendshipHometownCF
				|| recommenderType == RecType.RatedClusteredByFriendshipHometownFineTunedCF
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNw 
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinHometown
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinHometownFineTuned
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometown 
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedCheckinLocCF 
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedFSN
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedSocNwCF 
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedMOUSCheckinSocNw
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocCheckinLocCF
				|| recommenderType == RecType.RatedPostProcessedByLocFSN
				|| recommenderType == RecType.RatedPostProcessedByLocSocNwCF
				|| recommenderType == RecType.RatedPostProcessedByLocMOUSCheckinSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocHvrCheckinLocCF
				|| recommenderType == RecType.RatedPostProcessedByLocHvrFSN
				|| recommenderType == RecType.RatedPostProcessedByLocHvrSocNwCF
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedMOUSCheckinTime
				|| recommenderType == RecType.RatedMOUSCheckinHometownTime
				|| recommenderType == RecType.RatedMOUSCheckinHometownFSNTime
				|| recommenderType == RecType.RatedMOUSCheckinHometownFSNSocNwTime
				|| recommenderType == RecType.RatedMOUSCheckinHometownFineTunedFSNSocNwTime
				){
			rateBased = true;
		}
		return rateBased;
	}

	public static boolean isWeightBased(RecType recommenderType) {
		boolean weightBased = false;

		if(recommenderType == RecType.CheckinLocCFWeighted || recommenderType == RecType.FSNWeighted
				|| recommenderType == RecType.HometownCFWeighted || recommenderType == RecType.SocNwCFWeighted
				|| recommenderType == RecType.TimeCFWeighted 
				|| recommenderType == RecType.MOUSCheckinSocNwWeighted
				|| recommenderType == RecType.MOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.MOUSCheckinFSNSocNwHometownWeighted 
				|| recommenderType == RecType.MOUSCheckinHometownWeighted
				|| recommenderType == RecType.HometownFineTunedCFWeighted
				|| recommenderType == RecType.MOUSCheckinFSNSocNwHometownFineTunedWeighted 
				|| recommenderType == RecType.MOUSCheckinHometownFineTunedWeighted
				|| recommenderType == RecType.ClusteredByHometownCheckinLocCFWeighted 
				|| recommenderType == RecType.ClusteredByHometownFSNWeighted
				|| recommenderType == RecType.ClusteredByHometownSocNwCFWeighted 
				|| recommenderType == RecType.ClusteredByHometownMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.ClusteredByHometownMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.ClusteredByFriendshipCheckinLocCFWeighted 
				|| recommenderType == RecType.ClusteredByFriendshipSocNwCFWeighted
				|| recommenderType == RecType.ClusteredByFriendshipHometownCFWeighted
				|| recommenderType == RecType.ClusteredByFriendshipHometownFineTunedCFWeighted
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinHometownWeighted
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNwHometownWeighted 
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted
				|| recommenderType == RecType.ClusteredByHometownFineTunedCheckinLocCFWeighted 
				|| recommenderType == RecType.ClusteredByHometownFineTunedFSNWeighted
				|| recommenderType == RecType.ClusteredByHometownFineTunedSocNwCFWeighted 
				|| recommenderType == RecType.ClusteredByHometownFineTunedMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.PostProcessedByLocCheckinLocCFWeighted
				|| recommenderType == RecType.PostProcessedByLocFSNWeighted
				|| recommenderType == RecType.PostProcessedByLocSocNwCFWeighted
				|| recommenderType == RecType.PostProcessedByLocMOUSCheckinSocNwWeighted 
				|| recommenderType == RecType.PostProcessedByLocMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrCheckinLocCFWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrFSNWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrSocNwCFWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinSocNwWeighted 
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.MOUSCheckinTimeWeighted
				|| recommenderType == RecType.MOUSCheckinHometownTimeWeighted
				|| recommenderType == RecType.MOUSCheckinHometownFSNTimeWeighted
				|| recommenderType == RecType.MOUSCheckinHometownFSNSocNwTimeWeighted
				|| recommenderType == RecType.MOUSCheckinHometownFineTunedFSNSocNwTimeWeighted
				){

			weightBased = true;
		}
		return weightBased;
	}

	public static boolean isRateAndWeightBased(RecType recommenderType) {
		boolean rateBased = false;

		if(recommenderType == RecType.RatedCheckinLocCFWeighted || recommenderType == RecType.RatedFSNWeighted
				|| recommenderType == RecType.RatedHometownCFWeighted || recommenderType == RecType.RatedSocNwCFWeighted
				|| recommenderType == RecType.RatedTimeCFWeighted 
				|| recommenderType == RecType.RatedMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedMOUSCheckinFSNSocNwHometownWeighted 
				|| recommenderType == RecType.RatedMOUSCheckinHometownWeighted
				|| recommenderType == RecType.RatedHometownFineTunedCFWeighted
				|| recommenderType == RecType.RatedMOUSCheckinFSNSocNwHometownFineTunedWeighted 
				|| recommenderType == RecType.RatedMOUSCheckinHometownFineTunedWeighted
				|| recommenderType == RecType.RatedClusteredByHometownCheckinLocCFWeighted 
				|| recommenderType == RecType.RatedClusteredByHometownFSNWeighted
				|| recommenderType == RecType.RatedClusteredByHometownSocNwCFWeighted 
				|| recommenderType == RecType.RatedClusteredByHometownMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByHometownMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipCheckinLocCFWeighted 
				|| recommenderType == RecType.RatedClusteredByFriendshipSocNwCFWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipHometownCFWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipHometownFineTunedCFWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinHometownWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeighted 
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedCheckinLocCFWeighted 
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedFSNWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedSocNwCFWeighted 
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocCheckinLocCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocFSNWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocSocNwCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocMOUSCheckinSocNwWeighted 
				|| recommenderType == RecType.RatedPostProcessedByLocMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrCheckinLocCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrFSNWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrSocNwCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinSocNwWeighted 
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedMOUSCheckinTimeWeighted
				|| recommenderType == RecType.RatedMOUSCheckinHometownTimeWeighted
				|| recommenderType == RecType.RatedMOUSCheckinHometownFSNTimeWeighted
				|| recommenderType == RecType.RatedMOUSCheckinHometownFSNSocNwTimeWeighted
				|| recommenderType == RecType.RatedMOUSCheckinHometownFineTunedFSNSocNwTimeWeighted
				){
			rateBased = true;
		}
		return rateBased;
	}

	public static boolean isPostProcessBased(RecType recommenderType) {
		boolean postProcessBased = false;

		if(recommenderType == RecType.PostProcessedByLocCheckinLocCF 
				|| recommenderType == RecType.PostProcessedByLocCheckinLocCF
				|| recommenderType == RecType.PostProcessedByLocFSN
				|| recommenderType == RecType.PostProcessedByLocSocNwCF
				|| recommenderType == RecType.PostProcessedByLocMOUSCheckinSocNw
				|| recommenderType == RecType.PostProcessedByLocMOUSCheckinFSNSocNw
				|| recommenderType == RecType.PostProcessedByLocCheckinLocCFWeighted
				|| recommenderType == RecType.PostProcessedByLocFSNWeighted
				|| recommenderType == RecType.PostProcessedByLocSocNwCFWeighted
				|| recommenderType == RecType.PostProcessedByLocMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.PostProcessedByLocMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocCheckinLocCF 
				|| recommenderType == RecType.RatedPostProcessedByLocFSN
				|| recommenderType == RecType.RatedPostProcessedByLocSocNwCF
				|| recommenderType == RecType.RatedPostProcessedByLocMOUSCheckinSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocCheckinLocCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocFSNWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocSocNwCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrCheckinLocCF 
				|| recommenderType == RecType.PostProcessedByLocHvrCheckinLocCF
				|| recommenderType == RecType.PostProcessedByLocHvrFSN
				|| recommenderType == RecType.PostProcessedByLocHvrSocNwCF
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinSocNw
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinFSNSocNw
				|| recommenderType == RecType.PostProcessedByLocHvrCheckinLocCFWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrFSNWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrSocNwCFWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrCheckinLocCF 
				|| recommenderType == RecType.RatedPostProcessedByLocHvrFSN
				|| recommenderType == RecType.RatedPostProcessedByLocHvrSocNwCF
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocHvrCheckinLocCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrFSNWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrSocNwCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.PostProcessedByTimeCheckinLocCF
				|| recommenderType == RecType.PostProcessedByTimeFSN
				|| recommenderType == RecType.PostProcessedByTimeSocNwCF
				|| recommenderType == RecType.PostProcessedByTimeHometownCF
				|| recommenderType == RecType.PostProcessedByTimeTimeCF
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinSocNw
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinFSNSocNw
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinFSNSocNwHometown 
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinHometown
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinTime
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinHometownTime
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinHometownFSNTime
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinHometownFSNSocNwTime
				) {
			postProcessBased = true;
		}
		return postProcessBased;
	}

	public static boolean isPostProcessedByTime(RecType recommenderType) {
		boolean postProcessBased = false;

		if( recommenderType == RecType.PostProcessedByTimeCheckinLocCF
				|| recommenderType == RecType.PostProcessedByTimeFSN
				|| recommenderType == RecType.PostProcessedByTimeSocNwCF
				|| recommenderType == RecType.PostProcessedByTimeHometownCF
				|| recommenderType == RecType.PostProcessedByTimeTimeCF
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinSocNw
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinFSNSocNw
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinFSNSocNwHometown 
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinHometown
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinTime
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinHometownTime
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinHometownFSNTime
				|| recommenderType == RecType.PostProcessedByTimeMOUSCheckinHometownFSNSocNwTime
				) {
			postProcessBased = true;
		}
		return postProcessBased;
	}

	public static boolean isPostProcessedByLocHvr(RecType recommenderType) {
		boolean postProcessBased = false;

		if( recommenderType == RecType.PostProcessedByLocHvrCheckinLocCF 
				|| recommenderType == RecType.PostProcessedByLocHvrCheckinLocCF
				|| recommenderType == RecType.PostProcessedByLocHvrFSN
				|| recommenderType == RecType.PostProcessedByLocHvrSocNwCF
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinSocNw
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinFSNSocNw
				|| recommenderType == RecType.PostProcessedByLocHvrCheckinLocCFWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrFSNWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrSocNwCFWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.PostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrCheckinLocCF 
				|| recommenderType == RecType.RatedPostProcessedByLocHvrFSN
				|| recommenderType == RecType.RatedPostProcessedByLocHvrSocNwCF
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedPostProcessedByLocHvrCheckinLocCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrFSNWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrSocNwCFWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted
				) {
			postProcessBased = true;
		}
		return postProcessBased;
	}

	public static boolean isClusterBased(RecType recommenderType) {
		boolean clusterBased = false;

		if(recommenderType == RecType.ClusteredByHometownCheckinLocCF 
				|| recommenderType == RecType.ClusteredByHometownFSN
				|| recommenderType == RecType.ClusteredByHometownSocNwCF 
				|| recommenderType == RecType.ClusteredByHometownMOUSCheckinSocNw
				|| recommenderType == RecType.ClusteredByHometownMOUSCheckinFSNSocNw
				|| recommenderType == RecType.ClusteredByHometownCheckinLocCFWeighted 
				|| recommenderType == RecType.ClusteredByHometownFSNWeighted
				|| recommenderType == RecType.ClusteredByHometownSocNwCFWeighted 
				|| recommenderType == RecType.ClusteredByHometownMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.ClusteredByHometownMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByHometownCheckinLocCF 
				|| recommenderType == RecType.RatedClusteredByHometownFSN
				|| recommenderType == RecType.RatedClusteredByHometownSocNwCF 
				|| recommenderType == RecType.RatedClusteredByHometownMOUSCheckinSocNw
				|| recommenderType == RecType.RatedClusteredByHometownMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedClusteredByHometownCheckinLocCFWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFSNWeighted
				|| recommenderType == RecType.RatedClusteredByHometownSocNwCFWeighted
				|| recommenderType == RecType.RatedClusteredByHometownMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByHometownMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.ClusteredByFriendshipCheckinLocCF
				|| recommenderType == RecType.ClusteredByFriendshipSocNwCF
				|| recommenderType == RecType.ClusteredByFriendshipHometownCF
				|| recommenderType == RecType.ClusteredByFriendshipHometownFineTunedCF
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNw
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinHometown
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinHometownFineTuned
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNwHometown 
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned
				|| recommenderType == RecType.ClusteredByFriendshipCheckinLocCFWeighted 
				|| recommenderType == RecType.ClusteredByFriendshipSocNwCFWeighted
				|| recommenderType == RecType.ClusteredByFriendshipHometownCFWeighted
				|| recommenderType == RecType.ClusteredByFriendshipHometownFineTunedCFWeighted
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinHometownWeighted
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNwHometownWeighted 
				|| recommenderType == RecType.ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipCheckinLocCF
				|| recommenderType == RecType.RatedClusteredByFriendshipSocNwCF
				|| recommenderType == RecType.RatedClusteredByFriendshipHometownCF
				|| recommenderType == RecType.RatedClusteredByFriendshipHometownFineTunedCF
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNw 
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinHometown
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinHometownFineTuned
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometown 
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned
				|| recommenderType == RecType.RatedClusteredByFriendshipCheckinLocCFWeighted 
				|| recommenderType == RecType.RatedClusteredByFriendshipSocNwCFWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipHometownCFWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipHometownFineTunedCFWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinHometownWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeighted 
				|| recommenderType == RecType.RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted
				|| recommenderType == RecType.ClusteredByHometownFineTunedCheckinLocCF 
				|| recommenderType == RecType.ClusteredByHometownFineTunedFSN
				|| recommenderType == RecType.ClusteredByHometownFineTunedSocNwCF 
				|| recommenderType == RecType.ClusteredByHometownFineTunedMOUSCheckinSocNw
				|| recommenderType == RecType.ClusteredByHometownFineTunedMOUSCheckinFSNSocNw
				|| recommenderType == RecType.ClusteredByHometownFineTunedCheckinLocCFWeighted 
				|| recommenderType == RecType.ClusteredByHometownFineTunedFSNWeighted
				|| recommenderType == RecType.ClusteredByHometownFineTunedSocNwCFWeighted 
				|| recommenderType == RecType.ClusteredByHometownFineTunedMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedCheckinLocCF 
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedFSN
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedSocNwCF 
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedMOUSCheckinSocNw
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNw
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedCheckinLocCFWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedFSNWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedSocNwCFWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeighted
				|| recommenderType == RecType.RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted
				) {
			clusterBased = true;
		}
		return clusterBased;
	}

	public static boolean isAssignedAlphaVoteParamBased(RecType recommenderType) {
		boolean assignedAlphaVoteParamBased = false;

		if( recommenderType == RecType.CheckinHometownAssignedAlphaVoteParamBasedPostCombine
				||  recommenderType == RecType.CheckinSocNwAssignedAlphaVoteParamBasedPostCombine
				||  recommenderType == RecType.CheckinFSNSocNwAssignedAlphaVoteParamBasedPostCombine
				||  recommenderType == RecType.CheckinFSNSocNwHometownAssignedAlphaVoteParamBasedPostCombine
				) {
			assignedAlphaVoteParamBased = true;
		}
		return assignedAlphaVoteParamBased;
	}

	public static boolean isAssignedAlphaWeightParamBased(RecType recommenderType) {
		boolean assignedAlphaVoteParamBased = false;

		if( recommenderType == RecType.CheckinHometownAssignedAlphaWeightParamBasedPostCombine
				||  recommenderType == RecType.CheckinSocNwAssignedAlphaWeightParamBasedPostCombine
				||  recommenderType == RecType.CheckinFSNSocNwAssignedAlphaWeightParamBasedPostCombine
				||  recommenderType == RecType.CheckinFSNSocNwHometownAssignedAlphaWeightParamBasedPostCombine
				) {
			assignedAlphaVoteParamBased = true;
		}
		return assignedAlphaVoteParamBased;
	}

}
