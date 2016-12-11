package main;

import inputReader.Checkin2011DBReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

import data.CheckinTime;
import data.CheckinTime.TimeCategory;
import data.Rating;
import evaluater.FindUpperBound;

public class DbUtil {

	// rating thresholds
	public static Double[] thresholds = {1.0, 5.0, 11.0};

	// Eath's radius
	public static Double earthRadious = 6373.0;// in km

	/**
	 *  update rating scores based on thresholds,
	 *  also find mean using the updated values
	 * @param thresholds
	 * @param checkinRatings
	 * @return 
	 */
	public static void updateRatingScore(Double[] thresholds,
			Rating rating) {
		Double rVal = rating.getRating();
		Double updatedVal = getUpdatedVal(thresholds, rVal);

		if(updatedVal.equals(0.0)){
			System.out.println("stop");
		}

		// update checkinRatings
		rating.setRating(updatedVal);
	}

	private static Double getUpdatedVal(Double[] thresholds, Double val) {
		Double retVal = 0.0;
		int size = thresholds.length;
		for(int tIndex = 0; tIndex < size; tIndex++){
			Double t = thresholds[tIndex];
			if(val<=t){
				retVal = tIndex + 1.0;
				break;
			}
		}

		// means above than the last thresold
		if(retVal.equals(0.0)){
			retVal = (double) size;//TODO should be size+1
		}

		return retVal;
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		String dbConn = "jdbc:mysql://path";
		String userName = "xxxxxxxx";
		String password = "xxxxxxxxxx";

		//Connecting to MYSQL Database
		//SQL Database name is java
		//SQL server is localhost, username:root, password:nopassword 
		//Connection con = DriverManager.getConnection("dbConn, userName,password);
		Connection con = DriverManager.getConnection(dbConn,
				userName,
				password);

		return con;
	}

	public static boolean doesContainUserid(Integer id, String fieldName, 
			String tableName, Connection con) {
		boolean retVal = false;
		try {

			DatabaseMetaData metadata = con.getMetaData();
			ResultSet resultSet = metadata.getTables(null, null, tableName, null);
			if(resultSet.next()){

				//Using the Connection Object now Create a Statement
				Statement stmnt = con.createStatement();


				// select a partial record
				String sql = "select * from "+  tableName
						+ " where "+ fieldName+"="+id;
				System.out.println("The SQL query is: " + sql);  // Echo for debugging
				ResultSet rs = stmnt.executeQuery(sql);
				if (rs.next()) {
					retVal = true;
				}

				//Close the Statement 
				stmnt.close();
				rs.close();

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;
	}

	public static HashSet<Integer> getIds(Connection con, String tableName, 
			String columnName) {
		// for evaluation perform calculations only for users whose items are removed!!
		HashSet<Integer> idSet = new HashSet<Integer>();

		// get ids -- given in the column 
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// select  distinct userids
			String sqlSelect = "select distinct "
					+ columnName 
					+ " from " + tableName;

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlSelect);
			while (rs.next()) {
				Integer id = rs.getInt(columnName);
				idSet.add(id);
			}


			//Close the Statement & connection
			stmnt.close();
			rs.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return idSet;
	}

	public  static boolean doesContain(Connection con, Integer userId, Integer locId, 
			String tableName, String condDate, String condDate2) {
		boolean retVal = false;
		try {


			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// select a partial record
			String sql = "select * from "+ tableName+ " "
					+ " where userId="+userId 
					+ " and locid="+locId 
					+ " and time between \'" + condDate +"\'"
					+ " and \'" + condDate2 + "\'";

			//System.out.println("The SQL query is: " + sql);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql);
			if (rs.next()) {
				retVal = true;
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * 
	 * @param con
	 * @param userId
	 * @param tableName get checkins from this table
	 * @param tableName2 control if checkin locs exists in this table
	 * @param condDate
	 * @param condDate2
	 * @return
	 */
	public static HashSet<Integer> getVisitedLocs(Connection con, Integer userId,
			String tableName, String tableName2, String condDate, String condDate2) {
		HashSet<Integer> retSet = new HashSet<Integer>();
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// select a partial record
			String sql = "select cin.locid as locid from "+ tableName+  " as cin inner join " 
					+ tableName2 + " as cjan on cjan.locid=cin.locid "
					+ " where cin.userId="+userId 
					+ " and cin.time between \'" + condDate +"\'"
					+ " and \'" + condDate2 + "\'";

			//System.out.println("The SQL query is: " + sql);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql);
			while (rs.next()) {
				Integer locId = rs.getInt("locid");
				retSet.add(locId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retSet;
	}
	
	/**
	 * 
	 * @param con
	 * @param userId
	 * @param tableName get checkins from this table
	 * @param tableName2 control if checkin locs exists in this table
	 * @param condDate
	 * @param condDate2
	 * @return
	 */
	public static HashSet<Integer> getVisitedLocs(Connection con, Integer userId,
			String condDate, String condDate2, Double thresh) {
		
		HashSet<Integer> retSet = new HashSet<Integer>();
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select a partial record
			String sql = "select cin.locid as locid"
					+ " from checkins as cin" 
					+ " where cin.time between \'" + condDate +"\'"
					+ " and \'" + condDate2 + "\'"
					+ " and cin.userid="+ userId 
					+ " and cin.locid in (select distinct(locid) as locid from checkinsJan)"
					+ " order by cin.locid";

			//System.out.println("The SQL query is: " + sql);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql);
			Checkin2011DBReader reader = new Checkin2011DBReader();
			while (rs.next()) {
				Integer locId = rs.getInt("locid");
				
				// control if loc is in given distance threshold 
				boolean inDistanceThreshold = FindUpperBound.isInCircle(reader, con,
						thresh, userId, locId);
				if(inDistanceThreshold){
					retSet.add(locId);
				}
				
				retSet.add(locId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retSet;
	}

	public static void writeToFile(String path, String data) {
		//print  to file
		try {
			FileOutputStream fos = new FileOutputStream(path,true);
			PrintStream ps = new PrintStream(fos);

			ps.println(data);

			ps.flush();
			ps.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static HashMap<Integer, Integer> getRankedRecList(String tableName,
			Integer userId, Connection con) {
		// Sorted by rank in increasing order: <Rank,LocId>
		HashMap<Integer, Integer> rankedList = new HashMap<Integer, Integer>();

		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select a partial record
			String sql = "select locid,recRank from "+ tableName+ " "
					+ " where userId="+userId
					+ " order by recRank asc";

			//System.out.println("The SQL query is: " + sql);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql);
			while (rs.next()) {
				Integer locId = rs.getInt("locid");
				Integer recRank = rs.getInt("recRank");
				rankedList.put(recRank, locId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return rankedList;
	}

	public static HashMap<Integer, Integer> getRankedRecList(String tableName,
			Integer userId, Double thresh, Connection con) {
		// Sorted by rank in increasing order: <Rank,LocId>
		HashMap<Integer, Integer> rankedList = new HashMap<Integer, Integer>();

		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select a partial record
			String sql = "select locid,recRank from "+ tableName+ " "
					+ " where userId="+userId
					+ " and threshold=" + thresh
					+ " order by recRank asc";

			System.out.println("The SQL query is: " + sql);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql);
			while (rs.next()) {
				Integer locId = rs.getInt("locid");
				Integer recRank = rs.getInt("recRank");
				rankedList.put(recRank, locId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return rankedList;
	}
	
	public static HashMap<Integer, Integer> getRankedRecList(String tableName,
			Integer userId, CheckinTime.TimeCategory timeCategory, Connection con) {
		// Sorted by rank in increasing order: <Rank,LocId>
		HashMap<Integer, Integer> rankedList = new HashMap<Integer, Integer>();

		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// select a partial record
			String sql = "select locid,recRank from "+ tableName+ " "
					+ " where userId="+userId
					+ " and timeCategory=\'" + timeCategory.toString() + "\'"
					+ " order by recRank asc";

			System.out.println("The SQL query is: " + sql);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql);
			while (rs.next()) {
				Integer locId = rs.getInt("locid");
				Integer recRank = rs.getInt("recRank");
				rankedList.put(recRank, locId);
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return rankedList;
	}

	public static HashMap<Integer, Integer> getRankedRecList(String tableName,
			Integer userId, double alpha0, double alpha1, Connection con) {
		// Sorted by rank in increasing order: <Rank,LocId>
		HashMap<Integer, Integer> rankedList = null;

		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// control if table exists
			boolean tableExists = false;
			String sql1 = "select * from "+ tableName+ " "
					+ " where alpha0=" + alpha0
					+ " and alpha1=" + alpha1
					+ " limit 1";
			System.out.println("The SQL query is: " + sql1);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql1);
			while (rs.next()) {
				// table exists
				tableExists = true;
			}

			if(tableExists){
				if(rankedList == null){
					rankedList = new HashMap<Integer, Integer>();
				}
				
				// select a partial record
				String sql = "select locid,recRank from "+ tableName+ " "
						+ " where userId="+userId
						+ " and alpha0=" + alpha0
						+ " and alpha1=" + alpha1
						+ " order by recRank asc";

				System.out.println("The SQL query is: " + sql);  // Echo for debugging
				rs = stmnt.executeQuery(sql);
				while (rs.next()) {
					Integer locId = rs.getInt("locid");
					Integer recRank = rs.getInt("recRank");

					rankedList.put(recRank, locId);
				}
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return rankedList;
	}

	public static HashMap<Integer, Integer> getRankedRecList(String tableName,
			Integer userId, double givenAlpha, double alpha0, double alpha1,
			Connection con) {
		// Sorted by rank in increasing order: <Rank,LocId>
		HashMap<Integer, Integer> rankedList = null;


		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// control if table exists
			boolean tableExists = false;
			String sql1 = "select * from "+ tableName+ " "
					+ " where alpha0=" + givenAlpha
					+ " and alpha1=" + alpha0
					+ " and alpha2=" + alpha1
					+ " limit 1";
			System.out.println("The SQL query is: " + sql1);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql1);
			while (rs.next()) {
				// table exists
				tableExists = true;
			}

			if(tableExists){
				if(rankedList == null){
					rankedList = new HashMap<Integer, Integer>();
				}
				// select a partial record
				String sql = "select locid,recRank from "+ tableName+ " "
						+ " where userId="+userId
						+ " and alpha0=" + givenAlpha
						+ " and alpha1=" + alpha0
						+ " and alpha2=" + alpha1
						+ " order by recRank asc";

				System.out.println("The SQL query is: " + sql);  // Echo for debugging
				rs = stmnt.executeQuery(sql);
				while (rs.next()) {
					Integer locId = rs.getInt("locid");
					Integer recRank = rs.getInt("recRank");

					rankedList.put(recRank, locId);
				}
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//may be in here i should set rankedList = null; to indicate that no such table exists
		}


		return rankedList;
	}

	public static HashMap<Integer, Integer> getRankedRecList(String tableName,
			Integer userId, double givenAlpha0, double givenAlpha1, double alpha0, double alpha1,
			Connection con) {
		// Sorted by rank in increasing order: <Rank,LocId>
		HashMap<Integer, Integer> rankedList = null;


		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// control if table exists
			boolean tableExists = false;
			String sql1 = "select * from "+ tableName+ " "
					+ " where alpha0=" + givenAlpha0
					+ " and alpha1=" + givenAlpha1
					+ " and alpha2=" + alpha0
					+ " and alpha3=" + alpha1
					+ " limit 1";
			System.out.println("The SQL query is: " + sql1);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql1);
			while (rs.next()) {
				// table exists
				tableExists = true;
			}

			if(tableExists){
				if(rankedList == null){
					rankedList = new HashMap<Integer, Integer>();
				}
				// select a partial record
				String sql = "select locid,recRank from "+ tableName+ " "
						+ " where userId="+userId
						+ " and alpha0=" + givenAlpha0
						+ " and alpha1=" + givenAlpha1
						+ " and alpha2=" + alpha0
						+ " and alpha3=" + alpha1
						+ " order by recRank asc";

				System.out.println("The SQL query is: " + sql);  // Echo for debugging
				rs = stmnt.executeQuery(sql);
				while (rs.next()) {
					Integer locId = rs.getInt("locid");
					Integer recRank = rs.getInt("recRank");

					rankedList.put(recRank, locId);
				}
			}
			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return rankedList;
	}

	public static Double calculateEucDist(LongLat longLat1, LongLat longLat2) {
		Double retVal = 0.0;

		Double val1 = Math.pow(longLat1.longtitude-longLat2.longtitude, 2);
		Double val2 = Math.pow(longLat1.latitude-longLat2.latitude, 2);
		retVal = Math.sqrt(val1+val2);

		longLat1 = null; 
		longLat2 = null;

		return retVal;
	}

	/**
	 * This JavaScript uses the Haversine Formula (shown below) expressed in terms of a 
	 * two-argument inverse tangent function to calculate the great circle distance between two points on the Earth.
	 *  This is the method recommended for calculating short distances by Bob Chamberlain (rgc@jpl.nasa.gov) 
	 *  of Caltech and NASA's Jet Propulsion Laboratory as described on the U.S. Census Bureau Web site. 
	 *  
	 *  http://andrew.hedges.name/experiments/haversine/
	 *  
	 * 	dlon = lon2 - lon1
			dlat = lat2 - lat1
			a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2
			c = 2 * atan2( sqrt(a), sqrt(1-a) )
			d = R * c (where R is the radius of the Earth) 

	 * @param longLat1
	 * @param longLat2
	 * @return distance in km 
	 */
	public static Double calculateHaversineDist(LongLat longLat1, LongLat longLat2) {
		Double retVal = 0.0;

		Double long1 = Math.toRadians(longLat1.longtitude);
		Double lat1 = Math.toRadians(longLat1.latitude);
		Double long2 = Math.toRadians(longLat2.longtitude);
		Double lat2 = Math.toRadians(longLat2.latitude);

		Double dLon = long1-long2;
		Double dLat = lat1-lat2;

		Double a = Math.pow(Math.sin(dLat/2), 2) + 
				Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon/2), 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		retVal = earthRadious * c;

		longLat1 = null; 
		longLat2 = null;

		return retVal;
	}

	public static double calculateEucDistSim(double eucDist) {
		double retVal = 1.0/(1.0+eucDist);
		return retVal;
	}

	public static HashSet<Integer> getVisitedLocsByTimeCategory(Connection con,
			Integer userId, String condDate, String condDate2, 
			TimeCategory timeCategory) {
		
		HashSet<Integer> retSet = new HashSet<Integer>();
		try {
			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();
			// select a partial record
			String sql = "select cin.locid as locid, cin.time as time"
					+ " from checkins as cin" 
					+ " where cin.time between \'" + condDate +"\'"
					+ " and \'" + condDate2 + "\'"
					+ " and cin.userid="+ userId 
					+ " and cin.locid in (select distinct(locid) as locid from checkinsJan)"
					+ " order by cin.locid";

			//System.out.println("The SQL query is: " + sql);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql);
			while (rs.next()) {
				Integer locId = rs.getInt("locid");
				String time = rs.getString("time");

				// control if time of checkin is in given timeCategory
				boolean inSameTimeCategory = FindUpperBound.isSameTimeCategory(timeCategory, time);
				if(inSameTimeCategory){
					retSet.add(locId);
				}
			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retSet;
		
	}

	public static boolean doesContain(Connection con, Integer userId,
			Integer locId, String tableName, String condDate, String condDate2,
			TimeCategory timeCategory) {
		boolean retVal = false;
		try {


			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// select a partial record
			String sql = "select userid, time, locid from "+ tableName+ " "
					+ " where userId="+userId 
					+ " and locid="+locId 
					+ " and time between \'" + condDate +"\'"
					+ " and \'" + condDate2 + "\'";
		
			//System.out.println("The SQL query is: " + sql);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sql);
			if (rs.next()) {
				String time = rs.getString("time");

				// control if time of checkin is in given timeCategory
				boolean inSameTimeCategory = FindUpperBound.isSameTimeCategory(timeCategory, time);
				if(inSameTimeCategory){
					retVal = true;
				}

			}

			//Close the Statement & connection
			stmnt.close();
			rs.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;
	}

	public static HashSet<Integer> getIds(Connection con, String tableName,
			String columnName, String condDateStart, String condDateEnd) {
		// for evaluation perform calculations only for users whose items are removed!!
				HashSet<Integer> idSet = new HashSet<Integer>();

				// get ids -- given in the column 
				try {
					//Using the Connection Object now Create a Statement
					Statement stmnt = con.createStatement();


					// select  distinct userids
					String sqlSelect = "select distinct "
							+ columnName 
							+ " from " + tableName 
							+ " where time between \'" + condDateStart +"\'"
							+ " and \'" + condDateEnd + "\'";

					System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
					ResultSet rs = stmnt.executeQuery(sqlSelect);
					while (rs.next()) {
						Integer id = rs.getInt(columnName);
						idSet.add(id);
					}


					//Close the Statement & connection
					stmnt.close();
					rs.close();

				}  catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



				return idSet;
	}





}
