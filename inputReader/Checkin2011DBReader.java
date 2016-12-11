package inputReader;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.DbUtil;
import main.LongLat;
import recommender.RecommendationDBDecisionMaker;
import recommender.Recommender.RecType;
import data.Checkin;
import data.CheckinTime;
import data.Rating;
import data.Similarity;
import data.Similarity.SimType;
import data.UserCheckin;

public class Checkin2011DBReader{
	

	/**
	 * Read info of user
	 * @param recommenderType 
	 * 
	 * @param user
	 * @param printer
	 * @param reader

	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public  void readInputs(RecType recommenderType, UserCheckin user,  Connection con,  
			boolean readHometown, boolean readFriends, 
			boolean readCheckins, boolean readCheckinTimes, 
			boolean readRatings) 
					throws NumberFormatException, IOException {


		if(readHometown){
			Integer hometownId = this.getHometownId(user, con);
			user.setHometown(hometownId);
		}

		if(readFriends){
			// friends
			ArrayList<Integer> friends = this.getFriendsIds(user, con);
			user.setFriends(friends);
		}

		if(readCheckins){
			// checkins
			ArrayList<Checkin> checkinList = this.getCheckin(user.getUserId(), con, "checkinsJan");
			user.setCheckins(checkinList);
		}
		
		if(readCheckinTimes){
			// checkins
			ArrayList<Checkin> checkins = user.getCheckins();
			// get dates we are interested in
			// TODO make the dates dynamic
			String condDate1 = "2011-01-01";
			String condDate2 = "2011-02-01";
			for(Checkin c:checkins){
				ArrayList<CheckinTime.TimeCategory> checkinTimeList = 
						this.getCheckinTimes(user.getUserId(), c.getLocationId(), 
								condDate1, condDate2, con);
				c.setCheckinTimeCatList(checkinTimeList);
			}
			user.setCheckins(checkins);
		}

		if(readRatings){
			// ratings
			ArrayList<Rating> ratings = this.getRatings(recommenderType, user, con, "checkinsJan");
			user.setRatings(ratings);
		}
	}
	

	public Integer getHometownId(UserCheckin user, Connection con) {

		Integer hometownId = -1;

		// Loading the Database Connection Driver
		try {
			
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  distinct userids
			String sqlSelect = "select hometownid from users where id="+user.getUserId();
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer hometownid = rs.getInt("hometownid");
				hometownId = hometownid;
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();
			

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hometownId;
	}
	
	public Integer getHometownId(Integer userid, Connection con) {

		Integer hometownId = -1;

		// Loading the Database Connection Driver
		try {
			
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  distinct userids
			String sqlSelect = "select hometownid from users where id="+userid;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer hometownid = rs.getInt("hometownid");
				hometownId = hometownid;
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hometownId;
	}
	
	public boolean isFromSameHometown(Integer userid1, Integer userid2, Connection con) {

		boolean retVal = false;

		// Loading the Database Connection Driver
		try {
			
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  distinct userids
			String sqlSelect = "select * from users as users1"
					+ " inner join users as users2 on users1.hometownid=users2.hometownid"
					+ " where users2.id=" +userid2
					+ " and users1.id=" +userid1;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
				
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				retVal=true;
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;
	}

	public ArrayList<Integer> getFriendsIds(UserCheckin user, Connection con) {
		ArrayList<Integer> friendsIds = new ArrayList<Integer>();

		// Loading the Database Connection Driver
		try {
			

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids
			String sqlSelect = "select user2id from friends where user1id="+user.getUserId();
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer fId = rs.getInt("user2id");
				friendsIds.add(fId);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return friendsIds;
	}
	
	public Integer getFriendsCount(UserCheckin user, Connection con) {
		Integer count = 0;

		// Loading the Database Connection Driver
		try {
			

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids
			String sqlSelect = "select count(*) as count from friends where user1id="+user.getUserId();
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				count = rs.getInt("count");
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	public Integer getFriendsCount(Integer userid, Connection con) {
		Integer count = 0;

		// Loading the Database Connection Driver
		try {
			

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids
			String sqlSelect = "select count(*) as count from friends where user1id="+userid;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				count = rs.getInt("count");
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}
	
	public Integer findCommonFriendCount(Integer userId1, Integer userId2, Connection con) {
		Integer commonFriendCount = 0;

		// Loading the Database Connection Driver
		try {
			

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  distinct userids
			String sqlSelect = "select count(*) as count from friends as friends1"
					+ " inner join friends as friends2 on friends1.user2id=friends2.user2id"
					+ " where friends2.user1id=" +userId2
					+ " and friends1.user1id=" +userId1;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				commonFriendCount = rs.getInt("count");
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return commonFriendCount;
	}
	
	public boolean areFriends(Integer userId1, Integer userId2,Connection con) {
		boolean retVal = false;

		// Loading the Database Connection Driver
		try {
			

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids
			String sqlSelect = "select * from friends" 
					+ " where user1id=" + userId1
					+ " and user2id=" + userId2;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				retVal = true;
				break;
			}


			//Close the Statement 
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;
	}

	/*
	 * Get checkin times of given user in given location in the given interval
	 */
	private ArrayList<CheckinTime.TimeCategory> getCheckinTimes(Integer userId,
			Integer locationId, String condDate1, String condDate2, Connection con) {
		ArrayList<CheckinTime.TimeCategory> checkinTimeCatList = new ArrayList<CheckinTime.TimeCategory>();
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get checkins
			String sqlSelect = "select time from checkins where"
					+ " userid=" + userId
					+ " and locid=" + locationId
					+ " and time between \'" + condDate1 +"\'"
					+ " and \'" + condDate2 + "\'";
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				String time = rs.getString("time");
				CheckinTime.TimeCategory timeCat = CheckinTime.mapToTimeCategory(time);
				checkinTimeCatList.add(timeCat);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return checkinTimeCatList;
	}
	
	public ArrayList<Checkin> getCheckins(ArrayList<Integer> userids, Connection con, String tableName) {
		ArrayList<Checkin> checkinList = new ArrayList<Checkin>();

		for(Integer id: userids){
			ArrayList<Checkin> checkins = getCheckin(id, con, tableName);
			checkinList.addAll(checkins);
		}

		return checkinList;
	}

	/**
	 * Returns only locids of the checkin
	 * @param id
	 * @param con
	 * @param tableName
	 * @return
	 */
	public ArrayList<Checkin> getCheckin(Integer id, Connection con, String tableName) {
		ArrayList<Checkin> checkinList = new ArrayList<Checkin>();;
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get checkins
			String sqlSelect = "select locid from " +tableName +" where userid="+id;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer locationId = rs.getInt("locid");
				Checkin checkin = new Checkin(0.0, 0.0, "0", locationId);
				checkinList.add(checkin);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return checkinList;
	}
	
	/**
	 * Returns only locid from checkinsJan table
	 * @param id
	 * @param con
	 * @return
	 */
	public ArrayList<Checkin> getCheckinFromCheckinJan(Integer id,
			Connection con) {
		ArrayList<Checkin> checkinList = new ArrayList<Checkin>();;
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get checkins
			String sqlSelect = "select locid from checkinsJan where userid="+id;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer locationId = rs.getInt("locid");
				Checkin checkin = new Checkin(0.0, 0.0, "0", locationId);
				checkinList.add(checkin);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return checkinList;
	} 
	
	public Integer getCheckinCount(Integer id, Connection con) {
		Integer count = 0;
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get checkins
			String sqlSelect = "select count(*) as count from checkinsJan where userid="+id;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				count = rs.getInt("count");
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}
	
	public Integer getCheckinCount(Integer id, Integer locid, Connection con) {
		Integer count = 0;
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get checkins
			String sqlSelect = "select sum(count) as sum from checkinsJan where userid="+id +" and locid="+locid;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				count = rs.getInt("sum");
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}
	
	public Integer findCommonCheckinCount(Integer userId1, Integer userId2, Connection con) {
		Integer commonFriendCount = 0;

		// Loading the Database Connection Driver
		try {
			

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  distinct userids
			String sqlSelect = "select count(*) as count from checkinsJan as checkins1"
					+ " inner join checkinsJan as checkins2 on checkins1.locid=checkins2.locid"
					+ " where checkins2.userid=" +userId2
					+ " and checkins1.userid=" +userId1;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				commonFriendCount = rs.getInt("count");
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return commonFriendCount;
	}
	
	public ArrayList<Rating> getRatings(RecType recommenderType, UserCheckin user, Connection con,
			String tableName) throws IOException {
		ArrayList<Rating> ratingList = new ArrayList<Rating>();

		// get  user id
		Integer id = user.getUserId();

		// get checkins
		ArrayList<Checkin> checkins = user.getCheckins(); //getCheckin(id, con, tableName);
		for(Checkin checkin:checkins){
			Rating rat = new Rating(id, checkin, 1.0);//TODO binary data for now!!
			if(RecommendationDBDecisionMaker.isRateBased(recommenderType) == true){
				// get real rating from database!!Dont forget to update val acc. thresholds
				Rating r = getRatingFromCheckinJan(id, checkin.getLocationId(), con);
				
				// return ratings based on threshold identified
				DbUtil.updateRatingScore(DbUtil.thresholds, r);
				
				// update
				rat.setRating(r.getRating());
			}
			ratingList.add(rat);
		}


		// Return
		return ratingList;
	}

	public Rating getRatingFromCheckinJan(Integer id, Integer locationId,
			Connection con) {
		Rating r = null;
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get checkins
			String sqlSelect = "select count from checkinsJan where userid="+id 
					+ " and locId="+locationId;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Double count = Double.valueOf(rs.getInt("count"));
				Checkin checkin = new Checkin(0.0, 0.0, "0", locationId);
				r = new Rating(id,checkin,count);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return r;
	}


	public ArrayList<Rating> getRatingFromCheckinJan(Integer id, Connection con) {
		ArrayList<Rating> ratingList = new ArrayList<Rating>();
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get checkins
			String sqlSelect = "select locid, count from checkinsJan where userid="+id;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer locationId = rs.getInt("locid");
				Double count = Double.valueOf(rs.getInt("count"));
				Checkin checkin = new Checkin(0.0, 0.0, "0", locationId);
				Rating rating = new Rating(id,checkin,count);
				ratingList.add(rating);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ratingList;
	}

	public ArrayList<Double> getSimilaritiesFromDB(
			UserCheckin recommendedUser, Integer recommenderUserId, Connection con,
			ArrayList<String> fieldName) {
		ArrayList<Double> simVals = new ArrayList<Double>();
		int size = fieldName.size();
		for(int i = 0; i < size; i++){
			String fName = fieldName.get(i);
			Double simVal = getSimilaritiesFromDB(recommendedUser, recommenderUserId, con, fName);
			simVals.add(simVal);
		}

		return simVals;
	}


	public ArrayList<Integer> getMostSimilarUsersFromDB(UserCheckin user,
			Connection con, String fieldName, Integer numberOfSimilarUsers,
			Double userSimilarityThreshold) {
		ArrayList<Integer> mostSimilarUsers = new ArrayList<>();
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids--TODO kontrol et sql queryi			
			String sqlSelect = "select userid2, "+ fieldName
					+ " from " + fieldName+ "Table "
					+ " where userid1=" + user.getUserId()
					+ " and "+ fieldName + ">=" + userSimilarityThreshold
					+ " order by " + fieldName 
					+ " desc limit 0," + numberOfSimilarUsers;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer fId = rs.getInt("userid2");
				mostSimilarUsers.add(fId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mostSimilarUsers;
	}
	
	public ArrayList<Integer> getMostSimilarUsersFromClusterFromDB(
			UserCheckin user, Connection con, String fieldName, String clusterFieldName,
			Integer numberOfSimilarUsers, Double userSimilarityThreshold) {
		ArrayList<Integer> mostSimilarUsers = new ArrayList<>();
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get neighbour ids who are from the same hometown/who are friends and most similar		
			String sqlSelect = "select t1.userid2, t1."+ fieldName
					+ " from " + fieldName + "Table as t1" 
					+ " ," + clusterFieldName + "Table as t2"
					+ " where t1.userid1=" + user.getUserId()
					+ " and t1.userid1=t2.userid1"
					+ " and t1.userid2=t2.userid2"
					+ " and t1."+ fieldName + ">=" + userSimilarityThreshold
					+ " order by t1." + fieldName 
					+ " desc limit 0," + numberOfSimilarUsers;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer fId = rs.getInt("userid2");
				mostSimilarUsers.add(fId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mostSimilarUsers;
	}

	public Double getSimilaritiesFromDB(UserCheckin recommendedUser,
			UserCheckin recommenderUser, Connection con, String fieldName) { 
		Double simVal = 0.0;
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids--TODO kontrol et sql queryi			
			String sqlSelect = "select "+ fieldName
					+ " from " + fieldName+ "Table "
					+ " where userid1=" + recommendedUser.getUserId()
					+ " and userid2=" + recommenderUser.getUserId();

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				simVal = Double.valueOf(rs.getString(fieldName));
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return simVal;
	}
	
	public Double getSimilaritiesFromDB(UserCheckin recommendedUser,
			Integer recommenderUserId, Connection con, String fieldName) {
		Double simVal = 0.0;
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids--TODO kontrol et sql queryi			
			String sqlSelect = "select "+ fieldName
					+ " from " + fieldName+ "Table "
					+ " where userid1=" + recommendedUser.getUserId()
					+ " and userid2=" + recommenderUserId;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				simVal = Double.valueOf(rs.getString(fieldName));
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return simVal;
	}

	public LongLat getHometownFinetuned(Integer userid, Connection con) {
		LongLat longlat = null;

		// Loading the Database Connection Driver
		try {
			
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  longlat
			String sqlSelect = "select latitude,longtitude from hometownFineTuned where userid="+userid;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Double longVal = Double.valueOf(rs.getFloat("longtitude"));
				Double latVal = Double.valueOf(rs.getFloat("latitude"));
				longlat = new LongLat(longVal,latVal);
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return longlat;
	}
	
	public LongLat getLocationCenter(Integer locId, Connection con) {
		LongLat longlat = null;

		// Loading the Database Connection Driver
		try {
			
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  longlat
			String sqlSelect = "select latitude,longtitude from checkinLocCenter where locid="+locId;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Double longVal = Double.valueOf(rs.getFloat("longtitude"));
				Double latVal = Double.valueOf(rs.getFloat("latitude"));
				longlat = new LongLat(longVal,latVal);
				break;
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return longlat;
	}
	
	public static HashMap<Integer,LongLat> getHometownFinetuned(Integer userid1, Integer userid2, Connection con) {
		HashMap<Integer,LongLat> longlatMap = new HashMap<Integer, LongLat>();

		// Loading the Database Connection Driver
		try {
			
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  longlat
			String sqlSelect = "select userid, latitude,longtitude from hometownFineTuned " +
					"where userid="+userid1 + " or userid="+userid2;
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer userid = rs.getInt("userid");
				Double longVal = Double.valueOf(rs.getFloat("longtitude"));
				Double latVal = Double.valueOf(rs.getFloat("latitude"));
				LongLat longLat = new LongLat(longVal,latVal);
				longlatMap.put(userid, longLat);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return longlatMap;
	}
	
	public HashMap<Integer,LongLat> getAllHometownFinetuned(Connection con) {
		HashMap<Integer,LongLat> longlatMap = new HashMap<Integer, LongLat>();

		// Loading the Database Connection Driver
		try {
			
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  longlat
			String sqlSelect = "select userid, latitude,longtitude from hometownFineTuned ";
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer userid = rs.getInt("userid");
				Double longVal = Double.valueOf(rs.getFloat("longtitude"));
				Double latVal = Double.valueOf(rs.getFloat("latitude"));
				LongLat longLat = new LongLat(longVal,latVal);
				longlatMap.put(userid, longLat);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return longlatMap;
	}
	
	
	
	public HashMap<Integer,LongLat> getAllCheckinLocCenter(Connection con) {
		HashMap<Integer,LongLat> longlatMap = new HashMap<Integer, LongLat>();

		// Loading the Database Connection Driver
		try {
			
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select  longlat
			String sqlSelect = "select locid, latitude,longtitude from checkinLocCenter ";
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer locid = rs.getInt("locid");
				Double longVal = Double.valueOf(rs.getFloat("longtitude"));
				Double latVal = Double.valueOf(rs.getFloat("latitude"));
				LongLat longLat = new LongLat(longVal,latVal);
				longlatMap.put(locid, longLat);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();


		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return longlatMap;
	}
	
	
	public HashMap<Integer, Double> getSimValsTo(Integer userid,
			String fieldName, Connection con) {
		// Loading the Database Connection Driver
		HashMap<Integer, Double>  simValsMap = new HashMap<Integer, Double>();
				try {
					
					//Using the Connection Object now Create a Statement
					Statement stmnt = con.createStatement();

					// select  longlat
					String sqlSelect = "select userid1, userid2,"+ fieldName +" " 
							+ " from " + fieldName+"Table "
							+ " where userid2="+userid;
					//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
					ResultSet rs = stmnt.executeQuery(sqlSelect);
					while (rs.next()) {
						Integer userid1 = rs.getInt("userid1");
						Integer userid2 = rs.getInt("userid2");
						Double simVal = Double.valueOf(rs.getString(fieldName));
						simValsMap.put(userid1,simVal);
					}


					//Close the Statement & connection
					stmnt.close();
					rs.close();


				}  catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return simValsMap;
	}
	
	public Double getUserHtownLocCenterDist(Integer userid, Integer locid, Connection con) {
		Double dist = DbUtil.earthRadious;
		
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			String sqlSelect = "select hometownFinetunedUbyLocHvrDist"
					+ " from hometownFinetunedUbyLocHvrDistTable "
					+ " where userid1=" + userid
					+ " and locid=" + locid;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging

			
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				dist = rs.getDouble("hometownFinetunedUbyLocHvrDist");
				break;
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dist;
	}
	

	public Similarity getSimilarityFromDB(UserCheckin user, Integer simUserId,
			Connection con, String fName, Similarity sim) {
		SimType simType = RecommendationDBDecisionMaker.mapFieldNameToSimType(fName);

		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get friends ids--TODO kontrol et sql queryi			
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
	public List<LongLat> getAllCheckinLatLong(Integer userId, Connection con) {
		List<LongLat> latlongList = new ArrayList<LongLat>();
	
		ArrayList<Checkin> list = getCheckinFromCheckinJan(userId, con);
		for(Checkin checkin:list){
			Integer locId = checkin.getLocationId();
			LongLat locCenter = getLocationCenter(locId, con);	
			latlongList.add(locCenter);
		}

		return latlongList;
	}

	public ArrayList<String> getCheckinTimeFromCheckinJan(Integer userId,
			Connection con) {
		
		ArrayList<String> list =  new ArrayList<String>();
		
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get checkins

			//String sqlSelect = "select time from checkinsJan where userid="+userId;
			String sqlSelect = "select cin.time as time from checkins as cin"
					+ " inner join checkinsJan as cjan"
					+ " on cjan.userid=cin.userid where"
					+ " cin.time between \'2011-01-01\' and \'2011-02-01\'"
					+ " and cJan.locid=cin.locid"
					+ " and cin.userid=" + userId
					+ " order by cin.time";
			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				String time = rs.getString("time");
				list.add(time);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		return list;
	}
	

	public Double calculateHeversineDist(Integer userId1, Integer userId2,
			 Connection con) {
		Double retVal = 0.0;
		HashMap<Integer,LongLat> longLatMap = getHometownFinetuned(userId1, userId2, con);
		LongLat longLat1 = longLatMap.get(userId1);
		LongLat longLat2 = longLatMap.get(userId2);

		retVal = DbUtil.calculateHaversineDist(longLat1, longLat2);

		longLat1 = null; 
		longLat2 = null;
		longLatMap = null;

		return retVal;
	}
}


