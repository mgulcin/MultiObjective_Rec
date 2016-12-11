package recommender;

import inputReader.Checkin2011DBReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import main.DbUtil;
import outputWriter.Printer;
import data.Checkin;
import data.Rating;
import data.Recommendation;
import data.Similarity;
import data.Subject;
import data.User;
import data.UserCheckin;



/**
 * @author mg
 *
 */
public abstract class Recommender {

	public enum RecType {
		///-----base---------
		CheckinLocCF(0), FSN(1), SocNwCF(2),HometownCF(3),
		MOUSCheckinSocNw(4), MOUSCheckinFSNSocNw(5),
		MOUSCheckinFSNSocNwHometown(6), MOUSCheckinHometown(7),
		///--------weighteds-------
		CheckinLocCFWeighted(8), FSNWeighted(9), SocNwCFWeighted(10),HometownCFWeighted(11),
		MOUSCheckinFSNSocNwHometownWeighted(12), MOUSCheckinHometownWeighted(13),
		MOUSCheckinSocNwWeighted(14), MOUSCheckinFSNSocNwWeighted(15),
		///-----rateds---------
		RatedCheckinLocCF(16), RatedFSN(17), RatedSocNwCF(18), RatedHometownCF(19), 
		RatedMOUSCheckinSocNw(20), RatedMOUSCheckinFSNSocNw(21), 
		RatedMOUSCheckinFSNSocNwHometown(22), RatedMOUSCheckinHometown(23),
		///---- rated & weighted
		RatedCheckinLocCFWeighted(24), RatedFSNWeighted(25), RatedSocNwCFWeighted(26), RatedHometownCFWeighted(27),
		RatedMOUSCheckinSocNwWeighted(28), RatedMOUSCheckinFSNSocNwWeighted(29), 
		RatedMOUSCheckinFSNSocNwHometownWeighted(30), RatedMOUSCheckinHometownWeighted(31), 
		///--- new features - hometown fine tuned -
		HometownFineTunedCF(32), HometownFineTunedCFWeighted(33),
		RatedHometownFineTunedCF(34), RatedHometownFineTunedCFWeighted(35), 
		MOUSCheckinHometownFineTuned(36),MOUSCheckinFSNSocNwHometownFineTuned(37),
		MOUSCheckinHometownFineTunedWeighted(38),MOUSCheckinFSNSocNwHometownFineTunedWeighted(39),
		RatedMOUSCheckinHometownFineTuned(40), RatedMOUSCheckinFSNSocNwHometownFineTuned(41),
		RatedMOUSCheckinHometownFineTunedWeighted(42), RatedMOUSCheckinFSNSocNwHometownFineTunedWeighted(43), 
		/// ---- cluster based -- cluster by hometown (NOT htfinetuned)
		// base
		ClusteredByHometownCheckinLocCF(44), 
		ClusteredByHometownFSN(45), 
		ClusteredByHometownSocNwCF(46),
		ClusteredByHometownMOUSCheckinSocNw(47), 
		ClusteredByHometownMOUSCheckinFSNSocNw(48), 
		// weighted
		ClusteredByHometownCheckinLocCFWeighted(49), 
		ClusteredByHometownFSNWeighted(50), 
		ClusteredByHometownSocNwCFWeighted(51), 
		ClusteredByHometownMOUSCheckinSocNwWeighted(52), 
		ClusteredByHometownMOUSCheckinFSNSocNwWeighted(53), 
		// rated
		RatedClusteredByHometownCheckinLocCF(54), 
		RatedClusteredByHometownFSN(55), 
		RatedClusteredByHometownSocNwCF(56),
		RatedClusteredByHometownMOUSCheckinSocNw(57), 
		RatedClusteredByHometownMOUSCheckinFSNSocNw(58), 
		// rated and weighted
		RatedClusteredByHometownCheckinLocCFWeighted(59), 
		RatedClusteredByHometownFSNWeighted(60), 
		RatedClusteredByHometownSocNwCFWeighted(61),
		RatedClusteredByHometownMOUSCheckinSocNwWeighted(62), 
		RatedClusteredByHometownMOUSCheckinFSNSocNwWeighted(63), 
		/// ---- cluster based -- cluster by friendship
		// base
		ClusteredByFriendshipCheckinLocCF(64), 
		ClusteredByFriendshipSocNwCF(65),
		ClusteredByFriendshipHometownCF(66),
		ClusteredByFriendshipHometownFineTunedCF(67),
		ClusteredByFriendshipMOUSCheckinSocNw(68), 
		ClusteredByFriendshipMOUSCheckinHometown(69),
		ClusteredByFriendshipMOUSCheckinHometownFineTuned(70),
		ClusteredByFriendshipMOUSCheckinSocNwHometown(71), 
		ClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned(72),
		// weighted
		ClusteredByFriendshipCheckinLocCFWeighted(73), 
		ClusteredByFriendshipSocNwCFWeighted(74),
		ClusteredByFriendshipHometownCFWeighted(75),
		ClusteredByFriendshipHometownFineTunedCFWeighted(76),
		ClusteredByFriendshipMOUSCheckinSocNwWeighted(77), 
		ClusteredByFriendshipMOUSCheckinHometownWeighted(78),
		ClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted(79),
		ClusteredByFriendshipMOUSCheckinSocNwHometownWeighted(80), 
		ClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted(81),
		// rated 
		RatedClusteredByFriendshipCheckinLocCF(82), 
		RatedClusteredByFriendshipSocNwCF(83),
		RatedClusteredByFriendshipHometownCF(84),
		RatedClusteredByFriendshipHometownFineTunedCF(85),
		RatedClusteredByFriendshipMOUSCheckinSocNw(86), 
		RatedClusteredByFriendshipMOUSCheckinHometown(87),
		RatedClusteredByFriendshipMOUSCheckinHometownFineTuned(88),
		RatedClusteredByFriendshipMOUSCheckinSocNwHometown(89), 
		RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTuned(90),
		// rated & weighted
		RatedClusteredByFriendshipCheckinLocCFWeighted(91), 
		RatedClusteredByFriendshipSocNwCFWeighted(92),
		RatedClusteredByFriendshipHometownCFWeighted(93),
		RatedClusteredByFriendshipHometownFineTunedCFWeighted(94),
		RatedClusteredByFriendshipMOUSCheckinSocNwWeighted(95), 
		RatedClusteredByFriendshipMOUSCheckinHometownWeighted(96),
		RatedClusteredByFriendshipMOUSCheckinHometownFineTunedWeighted(97),
		RatedClusteredByFriendshipMOUSCheckinSocNwHometownWeighted(98), 
		RatedClusteredByFriendshipMOUSCheckinSocNwHometownFineTunedWeighted(99),
		/// ---- cluster based -- cluster by hometown_finetuned
		// base
		ClusteredByHometownFineTunedCheckinLocCF(100), 
		ClusteredByHometownFineTunedFSN(101), 
		ClusteredByHometownFineTunedSocNwCF(102),
		ClusteredByHometownFineTunedMOUSCheckinSocNw(103), 
		ClusteredByHometownFineTunedMOUSCheckinFSNSocNw(104), 
		// weighted
		ClusteredByHometownFineTunedCheckinLocCFWeighted(105), 
		ClusteredByHometownFineTunedFSNWeighted(106), 
		ClusteredByHometownFineTunedSocNwCFWeighted(107), 
		ClusteredByHometownFineTunedMOUSCheckinSocNwWeighted(108), 
		ClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted(109), 
		// rated
		RatedClusteredByHometownFineTunedCheckinLocCF(110), 
		RatedClusteredByHometownFineTunedFSN(111), 
		RatedClusteredByHometownFineTunedSocNwCF(112),
		RatedClusteredByHometownFineTunedMOUSCheckinSocNw(113), 
		RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNw(114), 
		// rated and weighted
		RatedClusteredByHometownFineTunedCheckinLocCFWeighted(115), 
		RatedClusteredByHometownFineTunedFSNWeighted(116), 
		RatedClusteredByHometownFineTunedSocNwCFWeighted(117),
		RatedClusteredByHometownFineTunedMOUSCheckinSocNwWeighted(118), 
		RatedClusteredByHometownFineTunedMOUSCheckinFSNSocNwWeighted(119),
		/// ---- postprocessed based -- compare user HometownFineTuned and location fine tuned
		// base
		PostProcessedByLocCheckinLocCF(120), 
		PostProcessedByLocFSN(121), 
		PostProcessedByLocSocNwCF(122),
		PostProcessedByLocMOUSCheckinSocNw(123), 
		PostProcessedByLocMOUSCheckinFSNSocNw(124), 
		// weighted
		PostProcessedByLocCheckinLocCFWeighted(125), 
		PostProcessedByLocFSNWeighted(126), 
		PostProcessedByLocSocNwCFWeighted(127), 
		PostProcessedByLocMOUSCheckinSocNwWeighted(128), 
		PostProcessedByLocMOUSCheckinFSNSocNwWeighted(129), 
		// rated
		RatedPostProcessedByLocCheckinLocCF(130), 
		RatedPostProcessedByLocFSN(131), 
		RatedPostProcessedByLocSocNwCF(132),
		RatedPostProcessedByLocMOUSCheckinSocNw(133), 
		RatedPostProcessedByLocMOUSCheckinFSNSocNw(134), 
		// rated and weighted
		RatedPostProcessedByLocCheckinLocCFWeighted(135), 
		RatedPostProcessedByLocFSNWeighted(136), 
		RatedPostProcessedByLocSocNwCFWeighted(137),
		RatedPostProcessedByLocMOUSCheckinSocNwWeighted(138), 
		RatedPostProcessedByLocMOUSCheckinFSNSocNwWeighted(139),
		///////////////// hometown finetuned with heversine dist metric //////////////
		/// ---- postprocessed based -- compare user HometownFineTuned and location fine tuned
		// base
		PostProcessedByLocHvrCheckinLocCF(140), 
		PostProcessedByLocHvrFSN(141), 
		PostProcessedByLocHvrSocNwCF(142),
		PostProcessedByLocHvrMOUSCheckinSocNw(143), 
		PostProcessedByLocHvrMOUSCheckinFSNSocNw(144), 
		// weighted
		PostProcessedByLocHvrCheckinLocCFWeighted(145), 
		PostProcessedByLocHvrFSNWeighted(146), 
		PostProcessedByLocHvrSocNwCFWeighted(147), 
		PostProcessedByLocHvrMOUSCheckinSocNwWeighted(148), 
		PostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted(149), 
		// rated
		RatedPostProcessedByLocHvrCheckinLocCF(150), 
		RatedPostProcessedByLocHvrFSN(151), 
		RatedPostProcessedByLocHvrSocNwCF(152),
		RatedPostProcessedByLocHvrMOUSCheckinSocNw(153), 
		RatedPostProcessedByLocHvrMOUSCheckinFSNSocNw(154), 
		// rated and weighted
		RatedPostProcessedByLocHvrCheckinLocCFWeighted(155), 
		RatedPostProcessedByLocHvrFSNWeighted(156), 
		RatedPostProcessedByLocHvrSocNwCFWeighted(157),
		RatedPostProcessedByLocHvrMOUSCheckinSocNwWeighted(158), 
		RatedPostProcessedByLocHvrMOUSCheckinFSNSocNwWeighted(159),
		///////////////// post combine (Burke,2002): first find neighbours than combine //////////////
		/// ---- vote based (all in base setting!!) ----////
		CheckinHometownVoteBasedPostCombine(160),
		CheckinSocNwVoteBasedPostCombine(161),
		CheckinFSNSocNwVoteBasedPostCombine(162),
		CheckinFSNSocNwHometownVoteBasedPostCombine(163),
		/// ---- weight avg based (all in base setting!!) ----////
		CheckinHometownWeightAvgBasedPostCombine(164),
		CheckinSocNwWeightAvgBasedPostCombine(165),
		CheckinFSNSocNwWeightAvgBasedPostCombine(166),
		CheckinFSNSocNwHometownWeightAvgBasedPostCombine(167),
		/// ---- mixed based (all in base setting!!) ----////
		CheckinHometownMixedBasedPostCombine(168),
		CheckinSocNwMixedBasedPostCombine(169),
		CheckinFSNSocNwMixedBasedPostCombine(170),
		CheckinFSNSocNwHometownMixedBasedPostCombine(171),
		/// ---- parameter usage (Ye et al,2011) + vote + eql Alpha values ---- ////
		CheckinHometownEqlAlphaVoteParamBasedPostCombine(172),
		CheckinSocNwEqlAlphaVoteParamBasedPostCombine(173),
		CheckinFSNSocNwEqlAlphaVoteParamBasedPostCombine(174),
		CheckinFSNSocNwHometownEqlAlphaVoteParamBasedPostCombine(175),
		/// ---- parameter usage (Ye et al,2011) + vote + ranked Alpha values ---- ////
		CheckinHometownRankedAlphaVoteParamBasedPostCombine(176),
		CheckinSocNwRankedAlphaVoteParamBasedPostCombine(177),
		CheckinFSNSocNwRankedAlphaVoteParamBasedPostCombine(178),
		CheckinFSNSocNwHometownRankedAlphaVoteParamBasedPostCombine(179),
		/// ---- parameter usage (Ye et al,2011) + weight + eql Alpha values ---- ////
		CheckinHometownEqlAlphaWeightParamBasedPostCombine(180),
		CheckinSocNwEqlAlphaWeightParamBasedPostCombine(181),
		CheckinFSNSocNwEqlAlphaWeightParamBasedPostCombine(182),
		CheckinFSNSocNwHometownEqlAlphaWeightParamBasedPostCombine(183),
		/// ---- parameter usage (Ye et al,2011) + weight + ranked Alpha values ---- ////
		CheckinHometownRankedAlphaWeightParamBasedPostCombine(184),
		CheckinSocNwRankedAlphaWeightParamBasedPostCombine(185),
		CheckinFSNSocNwRankedAlphaWeightParamBasedPostCombine(186),
		CheckinFSNSocNwHometownRankedAlphaWeightParamBasedPostCombine(187),
		/// ---- parameter usage (Ye et al,2011) + vote + assigned Alpha values ---- ////
		CheckinHometownAssignedAlphaVoteParamBasedPostCombine(188),
		CheckinSocNwAssignedAlphaVoteParamBasedPostCombine(189),
		CheckinFSNSocNwAssignedAlphaVoteParamBasedPostCombine(190),
		CheckinFSNSocNwHometownAssignedAlphaVoteParamBasedPostCombine(191), 
		/// ---- parameter usage (Ye et al,2011) + weight + assigned Alpha values ---- ////
		CheckinHometownAssignedAlphaWeightParamBasedPostCombine(192),
		CheckinSocNwAssignedAlphaWeightParamBasedPostCombine(193),
		CheckinFSNSocNwAssignedAlphaWeightParamBasedPostCombine(194),
		CheckinFSNSocNwHometownAssignedAlphaWeightParamBasedPostCombine(195),
		/// Time based calculations - using older base,Rate, Weight , Rate&Weight based settings
		// base
		MOUSCheckinTime(196), 
		MOUSCheckinHometownTime(197), 
		MOUSCheckinHometownFSNTime(198), 
		MOUSCheckinHometownFSNSocNwTime(199), 
		MOUSCheckinHometownFineTunedFSNSocNwTime(200),
		// weight based		
		MOUSCheckinTimeWeighted(201), 
		MOUSCheckinHometownTimeWeighted(202), 
		MOUSCheckinHometownFSNTimeWeighted(203), 
		MOUSCheckinHometownFSNSocNwTimeWeighted(204), 
		MOUSCheckinHometownFineTunedFSNSocNwTimeWeighted(205),
		// rate base
		RatedMOUSCheckinTime(206), 
		RatedMOUSCheckinHometownTime(207), 
		RatedMOUSCheckinHometownFSNTime(208), 
		RatedMOUSCheckinHometownFSNSocNwTime(209), 
		RatedMOUSCheckinHometownFineTunedFSNSocNwTime(210),
		// rate&weight base
		RatedMOUSCheckinTimeWeighted(211), 
		RatedMOUSCheckinHometownTimeWeighted(212), 
		RatedMOUSCheckinHometownFSNTimeWeighted(213), 
		RatedMOUSCheckinHometownFSNSocNwTimeWeighted(214), 
		RatedMOUSCheckinHometownFineTunedFSNSocNwTimeWeighted(215),
		///--- time (base/rated/weighted/rated&weighted)
		TimeCF(216), TimeCFWeighted(217), 
		RatedTimeCF(218), RatedTimeCFWeighted(219),
		//// dynamic time requirement of users - add post process based on users temporal preference
		/// performed only for base settings!!!
		///--- single criterion
		PostProcessedByTimeCheckinLocCF(220),
		PostProcessedByTimeFSN(221), 
		PostProcessedByTimeSocNwCF(222),
		PostProcessedByTimeHometownCF(223),
		PostProcessedByTimeTimeCF(224),
		// mo-base 
		PostProcessedByTimeMOUSCheckinSocNw(225),
		PostProcessedByTimeMOUSCheckinFSNSocNw(226),
		PostProcessedByTimeMOUSCheckinFSNSocNwHometown(227), 
		PostProcessedByTimeMOUSCheckinHometown(228),
		// mo-base with time
		PostProcessedByTimeMOUSCheckinTime(229), 
		PostProcessedByTimeMOUSCheckinHometownTime(230),
		PostProcessedByTimeMOUSCheckinHometownFSNTime(231), 
		PostProcessedByTimeMOUSCheckinHometownFSNSocNwTime(232);


		private int value;

		private RecType(int value) {
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public int getValue() {
			return value;
		}


	}

	// user list
	protected ArrayList<User> users;

	// subject list
	protected ArrayList<Subject> subjects;
	//rating list
	protected ArrayList<Rating> ratings;

	protected Printer printer;
	protected Checkin2011DBReader reader;
	protected RecType type;


	//parameters used in calculations
	protected Integer numberOfSimilarUsers;
	protected Double userSimilarityThreshold;
	protected Double minScoreVal;
	protected Double maxScoreVal;

	// abstract classes
	public abstract ArrayList<UserCheckin> getMostSimilarUsers(UserCheckin user,
			Connection con, Integer numberOfSimilarUsers, 
			Double userSimilarityThreshold);


	/**
	 * @param numberOfSimilarUsers
	 * @param printer
	 */
	public Recommender(Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, 
			Printer printer, Checkin2011DBReader reader, RecType type) {
		super();
		this.numberOfSimilarUsers = numberOfSimilarUsers;
		this.userSimilarityThreshold = userSimilarityThreshold;
		this.minScoreVal = minScoreVal;
		this.maxScoreVal = maxScoreVal;
		this.printer = printer;
		this.reader = reader;
		this.type = type;
	}



	/**
	 * {@code} getter/setters
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		if(this.users == null){
			users = new ArrayList<User>();
		}
		this.users.add(user);
	}

	public void addUsers(ArrayList<User> addUsers) {
		if(this.users == null){
			users = new ArrayList<User>();
		}
		for(User newUser:addUsers){
			this.users.add(newUser);
		}

	}

	/**
	 * @return the ratings
	 */
	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	/**
	 * @param ratings the ratings to set
	 */
	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
	}

	public void addRatings(ArrayList<Rating> ratings) {
		if(this.ratings == null){
			this.ratings = new ArrayList<Rating>();
		}
		this.ratings.addAll(ratings);
	}

	/**
	 * @return the type
	 */
	public RecType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(RecType type) {
		this.type = type;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Recommender [users=" + users 
				+ ", subjects=" + subjects 
				+ ", ratings=" + ratings
				+ ", printer=" + printer 
				+ ", reader=" + reader
				+ ", type=" + type 
				+ ", numberOfSimilarUsers=" + numberOfSimilarUsers
				+ ", userSimilarityThreshold=" + userSimilarityThreshold
				+ ", minScoreVal=" + minScoreVal 
				+ ", maxScoreVal=" + maxScoreVal 
				+ "]";
	}

	public ArrayList<Recommendation> recommend(UserCheckin u, Connection con, Double outputListSize) {
		// find item-recScore by collobarative filtering

		// 1) Find k-many similar users 
		ArrayList<UserCheckin> similarUsers = getMostSimilarUsers(u,con,
				numberOfSimilarUsers, userSimilarityThreshold);

		// 3) Find recommendations - TODO sorted by score!!
		PriorityQueue<Recommendation> rec = findRecommendations(u, similarUsers,
				Similarity.SimType.CHECKIN, con);

		// 4) Return best k recommendation as a result
		ArrayList<Recommendation> resultMap = getBestKRecommendations(u,rec, outputListSize, con);

		// return 
		return resultMap;
	}

	/**
	 * Read the similar users info
	 * used by getMostSimilarUsers
	 * @param similarUsers
	 * @param con
	 * @return
	 */
	protected ArrayList<UserCheckin> createUserList(
			ArrayList<Integer> similarUsers, Connection con) {
		boolean readHometown = false;
		boolean readFriends = false;
		boolean readCheckins = true;
		boolean readRatings = true;
		boolean readCheckinTimes = false;
		
		ArrayList<UserCheckin> userList = new ArrayList<UserCheckin>();
		for(Integer simUser:similarUsers){
			UserCheckin newUser = new UserCheckin(simUser);
			
			//read those similar users info
			try {
				reader.readInputs(this.type, newUser, con,
						readHometown,readFriends, readCheckins, readCheckinTimes, readRatings);
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			// add to retList
			userList.add(newUser);
		}
		return userList;
	}
	

	//TODO Dont forget to Override in post process based approaches!!
	protected ArrayList<Recommendation> getBestKRecommendations(UserCheckin u,
			PriorityQueue<Recommendation> rec, Double k, Connection con) {

		// Return best k recommendation as a result
		ArrayList<Recommendation> resultMap = new ArrayList<Recommendation>();
		while(resultMap.size() < k){
			Recommendation r = rec.poll();

			if(r!=null){
				resultMap.add(r);	
			} else {
				// no element left in the queue
				break;
			}

		}
		return resultMap;
	}

	/**
	 * Returns unordered list if  multiple simFields given o.w. return an ordered list
	 * @param user
	 * @param con
	 * @param fieldName
	 * @param numberOfSimilarUsers
	 * @param userSimilarityThreshold
	 * @return
	 */
	protected ArrayList<Integer> getMostSimilarUsersFromDB(UserCheckin user,
			Connection con, ArrayList<String> fieldName,
			Integer numberOfSimilarUsers, Double userSimilarityThreshold) {
		ArrayList<Integer> ret = new ArrayList<Integer>();

		int size = fieldName.size();
		if(size == 1){
			// if only one simType is used, return an ordered list
			String fName = fieldName.get(0);
			ret = reader.getMostSimilarUsersFromDB(user, con, fName,
					numberOfSimilarUsers, userSimilarityThreshold);
		} else{
			// return an unordered list
			HashSet<Integer> retSimUsers = new HashSet<>();

			// consider all fieldNames
			for(int i = 0; i < size; i++){
				String fName = fieldName.get(i);
				ArrayList<Integer> tempList = reader.getMostSimilarUsersFromDB(user, con, fName,
						numberOfSimilarUsers, userSimilarityThreshold);

				// since same user can be collected from each criterion, the output list is unordered
				retSimUsers.addAll(tempList);	
			}

			ret.addAll(retSimUsers);
		}


		return ret;
	}


	protected ArrayList<Double> getSimilaritiesFromDB(UserCheckin recommendedUser, UserCheckin recommenderUser,
			Connection con, ArrayList<String> fieldName){
		ArrayList<Double> simVals = new ArrayList<Double>();

		int size = fieldName.size();
		for(int i = 0; i < size; i++){
			String fName = fieldName.get(i);
			Double simVal = reader.getSimilaritiesFromDB(recommendedUser, recommenderUser, con, fName);
			simVals.add(simVal);
		}

		return simVals;
	}


	protected ArrayList<Integer> getMostSimilarUsersFromClusterFromDB(
			UserCheckin user, Connection con, ArrayList<String> fieldName,
			ArrayList<String> clusterFieldName, Integer numberOfSimilarUsers, Double userSimilarityThreshold) {
		HashSet<Integer> retSimUsers = new HashSet<>();

		int size = fieldName.size();
		for(int i = 0; i < size; i++){
			String fName = fieldName.get(i);
			String cName = clusterFieldName.get(0);
			ArrayList<Integer> tempList = reader.getMostSimilarUsersFromClusterFromDB(user, con, fName, 
					cName, numberOfSimilarUsers, userSimilarityThreshold);
			retSimUsers.addAll(tempList);	
		}

		ArrayList<Integer> ret = new ArrayList<Integer>(retSimUsers.size());
		ret.addAll(retSimUsers);
		return ret;
	}


	protected PriorityQueue<Recommendation> findRecommendations(User u, 
			ArrayList<UserCheckin> similarUsers, Similarity.SimType simType,
			Connection con) {
		// 1) Get ratings from similar users (rating, usersWhoRecommendedThisActivity )
		HashMap<Checkin, ArrayList<Rating>> allRecommendedItems= 
				combineCheckins(u, similarUsers);
		
		// 2) Calculate recommendation prob./score for each checkinList-??
		HashMap<Checkin, Double> checkinRecMap = 
				calculateCheckinScores(u, allRecommendedItems, simType, con);

		// 3) find items that contains related checkin & sort acc score
		PriorityQueue<Recommendation> rec = createRecsFromCheckins(checkinRecMap);

		return rec;
	}



	protected HashMap<Checkin, Double> calculateCheckinScores(User u,
			HashMap<Checkin, ArrayList<Rating>> recommendedCheckins,
			Similarity.SimType simType, Connection con) {
		// calculate average, for checkins
		HashMap<Checkin, Double> recScores = new HashMap<Checkin, Double>();

		for(Map.Entry<Checkin, ArrayList<Rating>> r:recommendedCheckins.entrySet())
		{
			Checkin checkin = r.getKey();
			ArrayList<Rating> recommenderUsers = r.getValue();

			Double recScore = findRecScore(checkin, (UserCheckin)u, recommenderUsers, con);

			recScores.put(checkin, recScore);
		}

		return recScores;
	}


	protected Double findRecScore(Checkin checkin, UserCheckin recommendedUser, 
			ArrayList<Rating> recommenderUsers, Connection con) {
		Double recScore = 0.0;

		if(RecommendationDBDecisionMaker.isRateBased(this.type)){
			// use scores(ratings) of checkins
			recScore = findRecScoreRated(checkin, recommendedUser, recommenderUsers, con);
		} else if(RecommendationDBDecisionMaker.isWeightBased(this.type)){
			// use similarities as weights
			recScore = findRecScoreWeighted(recommendedUser, recommenderUsers, con);
		} else if(RecommendationDBDecisionMaker.isRateAndWeightBased(this.type)){
			// use scores(ratings) of checkins
			recScore = findRecScoreRatedAndWeighted(checkin, recommendedUser, recommenderUsers, con);
		} else{
			// all users are assumed to be equal
			Double recommenderSize = (double) recommenderUsers.size();
			recScore = recommenderSize;
		}

		return recScore;
	}

	private Double findRecScoreRatedAndWeighted(Checkin checkin,
			UserCheckin recommendedUser, ArrayList<Rating> recommenderUsers,
			Connection con) {
		Double recScore = 0.0;
		// for the checkin find avg of rating for each recommender users
		Double totalCount = 0.0;
		Double avgRating = 0.0;
		for(Rating rating: recommenderUsers){
			Double recommenderUserSimWeight = findRecommenderUserSimWeight((UserCheckin)recommendedUser, 
					rating.getUserId(), con);
			Double score = findRecScoreRated(checkin,recommendedUser, rating, con);
			avgRating += score * recommenderUserSimWeight;
			totalCount++;
		}

		recScore = avgRating;// use total of the scores, not avg
		//recScore = avgRating/totalCount;		
		return recScore;
	}


	protected Double findRecScoreRated(Checkin checkin, UserCheckin recommendedUser,
			ArrayList<Rating> recommenderUsers, Connection con) {
		Double recScore = 0.0;
		// for the checkin find avg of rating for each recommender users
		Double totalCount = 0.0;
		Double avgRating = 0.0;
		for(Rating rating: recommenderUsers){
			// if this rating is related to the same checkin perform calc.
			Checkin c = (Checkin) rating.getSubject();
			if(c.equals(checkin)){
				Integer userId = rating.getUserId();
				Rating rat = reader.getRatingFromCheckinJan(userId, checkin.getLocationId(), con);
				DbUtil.updateRatingScore(DbUtil.thresholds, rat);
				avgRating += rat.getRating();
				totalCount++;
			}
		}

		recScore = avgRating;// total of rated results!!
		return recScore;
	}

	protected Double findRecScoreRated(Checkin checkin, UserCheckin recommendedUser,
			Rating recommenderUser, Connection con){
		Double recScore = 0.0;


		// if this rating is related to the same checkin perform calc.
		Checkin c = (Checkin) recommenderUser.getSubject();
		if(c.equals(checkin)){
			Integer userId = recommenderUser.getUserId();
			Rating rat = reader.getRatingFromCheckinJan(userId, checkin.getLocationId(), con);
			DbUtil.updateRatingScore(DbUtil.thresholds, rat);
			recScore = rat.getRating();
		}

		return recScore;
	}

	protected Double findRecScoreWeighted(UserCheckin recommendedUser, 
			ArrayList<Rating> recommenderUsers, Connection con) {
		// find avg. of recScore based on similarities
		Double recScore = 0.0;

		// for each user find the avg. sim score based on different sim metrics
		// retscore is the avg of avgsimScores of recommender users
		for(Rating rating: recommenderUsers){
			// find avg sim of each user
			Double avgSim = findRecommenderUserSimWeight((UserCheckin)recommendedUser, 
					rating.getUserId(), con);

			// recscore is total of avgsims of users
			recScore += avgSim;
		}

		recScore = recScore;//Total of similarities NOT abvg of sims
		//recScore = recScore/recommenderUsers.size();
		return recScore;
	}

	protected Double findRecommenderUserSimWeight(UserCheckin recommendedUser, Integer recommenderUserId,
			Connection con){
		Double avgSim = 0.0;
		ArrayList<String> fieldNameList = RecommendationDBDecisionMaker.getSimName(this.type);
		ArrayList<Double> simVals = reader.getSimilaritiesFromDB((UserCheckin)recommendedUser, 
				recommenderUserId, con, fieldNameList);

		for(Double simVal:simVals){
			avgSim += simVal;
		}
		avgSim = avgSim/simVals.size();

		return avgSim;
	}

	protected HashMap<Checkin, ArrayList<Rating>> combineCheckins(
			User me, ArrayList<UserCheckin> similarUsers) {
		// combine users & items, note do not consider user herself/hisself
		HashMap<Checkin, ArrayList<Rating>> recommendedCheckins =
				new  HashMap<Checkin, ArrayList<Rating>>();

		for(UserCheckin simUser:similarUsers)
		{
			Integer simUserId = simUser.getUserId();

			if(simUserId.equals(me.getUserId()))
			{
				// do not consider user herself/himself
				continue;
			}

			// for each similar user, get checkins
			ArrayList<Rating> recRatings = simUser.getRatings();

			// for each new checkinList add to map
			// for each checkinList add this user to map
			for(Rating recRating: recRatings)
			{
				Checkin recCheckin = (Checkin) recRating.getSubject();

				if(recommendedCheckins.containsKey(recCheckin))
				{
					// already on map -- add sim user to the list
					ArrayList<Rating> ratingList = recommendedCheckins.get(recCheckin);
					ratingList.add(recRating);
										
					// put the updated checkin val
					recommendedCheckins.put(recCheckin, ratingList);
				}
				else
				{
					// new to map -- create new recommender rating list
					ArrayList<Rating> ratingList = new ArrayList<Rating>();
					ratingList.add(recRating);
					recommendedCheckins.put(recCheckin, ratingList);
				}

			}

		}

		return recommendedCheckins;
	}



	protected PriorityQueue<Recommendation> createRecsFromCheckins(
			HashMap<Checkin, Double> checkinRecMap) {
		// for each item add score and create recommendation
		Comparator<Recommendation> recComp = new RecommendationComparator();
		PriorityQueue<Recommendation> recList = 
				new PriorityQueue<Recommendation>(numberOfSimilarUsers, recComp);

		for(Entry<Checkin, Double> e:checkinRecMap.entrySet())
		{
			Checkin checkin = e.getKey();
			Double score = e.getValue();

			//checkin based
			Checkin addCheckin = new Checkin(checkin.getLatitude(), 
					checkin.getLongtitude(), checkin.getTime(), checkin.getLocationId());
			addCheckin.setCheckinTimeCatList(checkin.getCheckinTimeCatList());
			Recommendation rec = new Recommendation(addCheckin, score);
			recList.add(rec);
		}

		return recList;
	}

	//TODO overwrite by PostProcess Recommenders & AssignedAlpha Recommenders
	protected void populateDatabase(String dbName, Integer userid, ArrayList<Recommendation> recList,
			Connection con) {
		// populate database table with recommendations
		// Loading the Database Connection Driver
		try {


			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// insert recommendations
			int recRank = 1;
			for(Recommendation r:recList){

				Double score = r.getScore();
				Checkin checkin = (Checkin) r.getSubject();
				Double latitude = checkin.getLatitude();
				Double longtitude = checkin.getLongtitude();
				String time = "1970-01-01 00:00:01";//checkin.getTime();
				//int index = timeTemp.lastIndexOf(".");
				//String time = timeTemp.substring(0,index);
				Integer locid = checkin.getLocationId();

				// INSERT a partial record
				PreparedStatement stmt = con.prepareStatement(
						"insert into "+dbName+" " 
								+ "(userid,latitude,longtitude,time,locId, score, recRank)"
								+ "VALUES(?,?,?,STR_TO_DATE(?,'%Y-%m-%e %H:%i:%s'),?,?,?)");

				stmt.setInt(1, userid);
				stmt.setString(2, latitude.toString());
				stmt.setString(3, longtitude.toString());
				stmt.setString(4, time);
				stmt.setInt(5, locid);
				stmt.setString(6, score.toString());
				stmt.setInt(7, recRank);
				recRank++;

				int countInserted = stmt.executeUpdate();	

				stmt.closeOnCompletion();
			}
			//Close the Statement & connection
			stmnt.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected  void createRecTable(String dbName, Connection con) {
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// create table
			String sqlCreate = "create table "+ dbName+
					"(userid int,latitude varchar(50),longtitude varchar(50),"
					+ "time datetime,locId int,score varchar(50), recRank int, PRIMARY KEY (userid,locid))";
			//System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);


			//Close the Statement & connection
			stmnt.close();


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
