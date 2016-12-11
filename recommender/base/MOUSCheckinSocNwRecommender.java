package recommender.base;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import outputWriter.Printer;
import recommender.RecommendationDBDecisionMaker;
import recommender.Recommender;
import similarity.MOBasedSimilarityCalculator;
import data.Similarity;
import data.Similarity.SimType;
import data.UserCheckin;


public class MOUSCheckinSocNwRecommender extends Recommender{

	public MOUSCheckinSocNwRecommender(Integer numberOfSimilarUsers,
			Double similarUserThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, similarUserThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * Return most similar users
	 * 	- depending on the threshold on number of users to return and 
	 * 	- the similarity threshold
	 * Read these users from database
	 */
	public ArrayList<UserCheckin> getMostSimilarUsers(UserCheckin user, 
			Connection con, Integer numberOfSimilarUsers, 
			Double userSimilarityThreshold) {

		// 1) Get k-many similar users 
		double extensionParam = 3.0;//TODO make parametric
		int similarUsersCount = (int) (numberOfSimilarUsers * extensionParam);
		ArrayList<String> fieldName = RecommendationDBDecisionMaker.getSimName(this.type);
		ArrayList<Integer> similarUsersFromDB = getMostSimilarUsersFromDB(user, con, 
				fieldName, similarUsersCount, userSimilarityThreshold);

		// 2) Get their Similarities
		ArrayList<Similarity> userSimVals = getUserSimVals(user, similarUsersFromDB,
				con, fieldName);

		// 3) Get most similar users using multi-obj-opt
		ArrayList<Integer> similarUsers = new ArrayList<Integer>();
		while(similarUsers.size() < numberOfSimilarUsers){
			ArrayList<Integer> similarUsersTemp	= getMostSimilarUsers(similarUsers, 
					userSimVals,numberOfSimilarUsers, userSimilarityThreshold);
			
			if(similarUsersTemp.size() > 0 ) {
				int size = similarUsersTemp.size();
				if((similarUsers.size() + size) <= numberOfSimilarUsers){
					similarUsers.addAll(similarUsersTemp);
				}else{
					for(int i = 0; i < size; i++){
						if(similarUsers.size() < numberOfSimilarUsers){
							Integer similarUser = similarUsersTemp.get(i);
							similarUsers.add(similarUser);
						} else{
							break;
						}
					}
				}
			} else{
				break;
			}
		}


		// create usercheckinlist by reading the similar users info from db
		ArrayList<UserCheckin> userList = createUserList(similarUsers, con);

		return userList;
	}

	private ArrayList<Similarity> getUserSimVals(UserCheckin user,
			ArrayList<Integer> similarUsersFromDB, Connection con,
			ArrayList<String> fieldName) {
		ArrayList<Similarity> similarities = new ArrayList<>();

		for(Integer simUser:similarUsersFromDB){
			Similarity sim = getSimilarityFromDB(user, simUser, con, fieldName);
			similarities.add(sim);			
		}

		return similarities;
	}

	private Similarity getSimilarityFromDB(UserCheckin user, Integer simUser,
			Connection con, ArrayList<String> fieldName) {
		// get similarity using fNames
		Similarity sim  = null;
		int size = fieldName.size();
		for(int i = 0; i < size; i++){
			String fName = fieldName.get(i);
			sim = getSimilarityFromDB(user,simUser, con,fName,sim);
		}

		return sim;
	}


	private Similarity getSimilarityFromDB(UserCheckin user, Integer simUserId,
			Connection con, String fName, Similarity sim) {
		SimType simType = RecommendationDBDecisionMaker.mapFieldNameToSimType(fName);

		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids
			String sqlSelect = "select userid2," + fName
					+ " from " + fName+ "Table "
					+ " where userid1=" + user.getUserId()
					+ " and userid2=" + simUserId;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			Double simVal = 0.0;
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				simVal = rs.getDouble(fName);

				break;
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();

			// create/update similarity 
			UserCheckin simUser = new UserCheckin(simUserId);
			if(sim == null){
				// create new sim
				sim = new Similarity(simUser,simVal,simType);	
			} else{
				// update sim
				HashMap<SimType, Double> simMap = sim.getSimilarityMap();
				simMap.put(simType, simVal);
				sim.setSimilarityMap(simMap);
			}
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sim;

	}


	public ArrayList<Integer> getMostSimilarUsers(
			ArrayList<Integer> alreadySelected,
			ArrayList<Similarity> userSimVals, Integer numberOfSimilarUsers,
			Double userSimilarityThreshold) {

		// remove elements(non-dominated users) which are already selected
		ArrayList<Similarity> userSimValsPruned = new ArrayList<Similarity>();

		for(Similarity sim:userSimVals){
			Integer userid = sim.getSimilarUser().getUserId();
			if(alreadySelected.contains(userid)){
				// do nothing
			} else{
				userSimValsPruned.add(sim);
			}
		}

		// run normal getSimilar users & return
		return getMostSimilarUsers(userSimValsPruned, numberOfSimilarUsers, userSimilarityThreshold);

	}

	public ArrayList<Integer> getMostSimilarUsers(ArrayList<Similarity> userSimVals,
			Integer numberOfSimilarUsers, Double similarUserThreshold) {
		// 1) create dominance matrix
		MOBasedSimilarityCalculator moSimCalc = new MOBasedSimilarityCalculator();
		ArrayList<Similarity.SimType> featuresToUse = new ArrayList<Similarity.SimType>();
		featuresToUse.add(Similarity.SimType.CHECKIN);
		featuresToUse.add(Similarity.SimType.SOCIALNW);
		Double[][] dominanceMatrix = moSimCalc.createDominanceMatrix(userSimVals, 
				featuresToUse);

		// 2) select non-dominated friends
		ArrayList<Similarity> nonDominatedSims = moSimCalc.findNonDominatedSims(userSimVals, 
				dominanceMatrix);

		// 3) sort non-dominated friends by checkin & sn- using the sortOrder
		ArrayList<Similarity.SimType> sortOrder = new ArrayList<Similarity.SimType>();
		sortOrder.add(SimType.CHECKIN); 
		sortOrder.add(SimType.SOCIALNW); 
		ArrayList<Integer> similarUsers = moSimCalc.sortBy(nonDominatedSims, sortOrder, 
				numberOfSimilarUsers, userSimilarityThreshold);		

		//System.out.println("Size: " + similarUsers.size());

		return similarUsers;
	}
}
