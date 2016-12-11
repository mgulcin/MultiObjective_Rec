package recommender;

import inputReader.Checkin2011DBReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import outputWriter.Printer;
import data.Checkin;
import data.CheckinTime;
import data.CheckinTime.TimeCategory;
import data.Rating;
import data.Recommendation;
import data.Similarity;
import data.User;
import data.UserCheckin;

public abstract class PostProcessByTimeRecommender extends Recommender {

	CheckinTime.TimeCategory chosenTimeCategory;
	
	/**
	 * Constructor
	 *  
	 * @param numberOfSimilarUsers
	 * @param userSimilarityThreshold
	 * @param minScoreVal
	 * @param maxScoreVal
	 * @param printer
	 * @param reader
	 * @param type
	 * @param chosenTimeCategory
	 */
	public PostProcessByTimeRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type, CheckinTime.TimeCategory chosenTimeCategory) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		this.chosenTimeCategory = chosenTimeCategory;
	}
	
	/**
	 * Constructor
	 * default chosen time category = weekday afternoon
	 * 
	 * @param numberOfSimilarUsers
	 * @param userSimilarityThreshold
	 * @param minScoreVal
	 * @param maxScoreVal
	 * @param printer
	 * @param reader
	 * @param type
	 */
	public PostProcessByTimeRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		this.chosenTimeCategory = CheckinTime.TimeCategory.WD_AFTERNOON;
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
		boolean readCheckinTimes = true;
		
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

	/**
	 * If using time categories
	 * - combine checkintime categories into the checkin as a step in between 1 and 2
	 * (so that use combined checkins while creating the recommendations in step 3)
	 */
	@Override
	protected PriorityQueue<Recommendation> findRecommendations(User u, 
			ArrayList<UserCheckin> similarUsers, Similarity.SimType simType,
			Connection con) {
		// 1) Get ratings from similar users (rating, usersWhoRecommendedThisActivity )
		HashMap<Checkin, ArrayList<Rating>> allRecommendedItemsTemp= 
				combineCheckins(u, similarUsers);

		//1.5) Combining time categories into one, update keys of allRecommendedItems
		HashMap<Checkin, ArrayList<Rating>> allRecommendedItems = 
				combineTimeCategories(allRecommendedItemsTemp);
		
		allRecommendedItemsTemp = null;
		
		
		// 2) Calculate recommendation prob./score for each checkinList-??
		HashMap<Checkin, Double> checkinRecMap = 
				calculateCheckinScores(u, allRecommendedItems, simType, con);
		allRecommendedItems = null;

		// 3) find items that contains related checkin & sort acc score
		PriorityQueue<Recommendation> rec = createRecsFromCheckins(checkinRecMap);

		return rec;
	}
	
	/**
	 * Update key's category values based on the values in the rating list
	 * @param allRecommendedItems
	 */
	protected HashMap<Checkin, ArrayList<Rating>> combineTimeCategories(
			HashMap<Checkin, ArrayList<Rating>> allRecommendedItems) {
		
		HashMap<Checkin, ArrayList<Rating>> retMap = new HashMap<Checkin, ArrayList<Rating>>();
		
		for(Entry<Checkin, ArrayList<Rating>> entry:allRecommendedItems.entrySet()){
			Checkin c = entry.getKey();
			ArrayList<Rating> ratingList = entry.getValue();
			
			// update checkin c with new timeCategory list based on input rating list
			ArrayList<CheckinTime.TimeCategory> newCatList = new ArrayList<CheckinTime.TimeCategory>();
			for(Rating rating:ratingList){
				ArrayList<TimeCategory> catList = ((Checkin)rating.getSubject()).getCheckinTimeCatList();
				newCatList.addAll(catList);
			}
			c.setCheckinTimeCatList(null);
			c.setCheckinTimeCatList(newCatList);
			
			// put updated c with old rating values into the returning map
			retMap.put(c, ratingList);
		}
		
		
		return retMap;
	}

	/**
	 * Add to recommendations by controlling the filter
	 */
	@Override
	protected ArrayList<Recommendation> getBestKRecommendations(UserCheckin u,
			PriorityQueue<Recommendation> rec, Double k, Connection con) {
		// Return best k recommendation as a result
		ArrayList<Recommendation> resultMap = new ArrayList<Recommendation>();
		while(resultMap.size() < k){
			Recommendation r = rec.poll();

			if(r!=null){
				if(canAddToResult(u,r, con)){
					resultMap.add(r);	
				}
			} else {
				// no element left in the queue
				break;
			}

		}
		return resultMap;
	}

	// DO NOT forget to override when necessary!!
	/**
	 * Control the condition and if match return true, 
	 * such that the recommendation can be add to the result
	 * @param user
	 * @param recommendation
	 * @param con
	 * @return
	 */
	protected boolean canAddToResult(UserCheckin u, Recommendation r, Connection con) {
		// control if rec is given for related time category
		boolean retVal = false;
		
		// this loc is visited in the time categories given in checkinTimeCatList by the user u
		ArrayList<TimeCategory> checkinTimeCatList = ((Checkin)r.getSubject()).getCheckinTimeCatList();
				
		if(checkinTimeCatList.contains(chosenTimeCategory)){
			retVal = true;
		}		
		return retVal;
	}

	//TODO overwrite by PostProcess Recommenders
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
									+ "(userid,latitude,longtitude,time,locId, score, recRank, timeCategory)"
									+ "VALUES(?,?,?,STR_TO_DATE(?,'%Y-%m-%e %H:%i:%s'),?,?,?, ?)");

					stmt.setInt(1, userid);
					stmt.setString(2, latitude.toString());
					stmt.setString(3, longtitude.toString());
					stmt.setString(4, time);
					stmt.setInt(5, locid);
					stmt.setString(6, score.toString());
					stmt.setInt(7, recRank);
					stmt.setString(8, chosenTimeCategory.toString());
					recRank++;

					int countInserted = stmt.executeUpdate();
					if(countInserted < 1){
						System.out.println("Error in populate database");
						System.exit(-1);
					}
					stmt.close();
				}
				//Close the Statement & connection
				stmnt.close();


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void createRecTable(String dbName, Connection con) {
			try {
				//Using the Connection Object now Create a Statement
				Statement stmnt = con.createStatement();

				// create table
				String sqlCreate = "create table "+ dbName+
						"(userid int,latitude varchar(50),longtitude varchar(50),"
						+ "time datetime,locId int,score varchar(50), recRank int, " 
						+ "timeCategory varchar(50), primary key (userid,recRank,timeCategory))";
				System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
				stmnt.execute(sqlCreate);

				//Close the Statement & connection
				stmnt.close();


			}  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public String toString() {
			return "PostProcessByTimeRecommender [chosenTimeCategory="
					+ chosenTimeCategory + "]";
		}

		public CheckinTime.TimeCategory getChosenTimeCategory() {
			return chosenTimeCategory;
		}

		public void setChosenTimeCategory(CheckinTime.TimeCategory chosenTimeCategory) {
			this.chosenTimeCategory = chosenTimeCategory;
		}
	
}
