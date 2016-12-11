package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


public class CheckinDbCreator {

	/* Note: Refer to http://www.ntu.edu.sg/home/ehchua/programming/java/JDBC_Basic.html
	 *  for basic SQL + Java
	 * 
	 */
	public static void main(String[] args) {
		// get connection
		Connection con = null;
		try {
			con = DbUtil.getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//createUsers();
		//createFriends();
		//createCheckins();
		//createCheckinsRemoved();
		//updateRemoved(con);


		//Double unvisitedCount = getUnVisitedCountRemoved(con);
		//System.out.println("unvisitedCount: "+unvisitedCount);

		/*// get first month 01.01.2011-01.02.2011
		String startDate = "2011-01-01";
		String endDate = "2011-02-01";
		String tableName = "checkinsJan";
		createSubcheckinTable(con, tableName, startDate, endDate);*/

		/*String startDate = "2011-01-01";
		String endDate = "2011-02-01";
		String countTableName = "checkinsJan";
		String latlongTableName = "checkins";
		String tableName = "hometownFineTuned";
		createHometownFinetuned(con, tableName, countTableName, latlongTableName, startDate, endDate);*/

		// find checkin places center point by averaging checkins long/lat 
		String startDate = "2011-01-01";
		String endDate = "2011-02-01";
		String countTableName = "checkinsJan";
		String latlongTableName = "checkins";
		String outputTableName = "checkinLocCenter";
		createcheckinPlaceCenter(con, outputTableName, countTableName, latlongTableName, startDate, endDate);

		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * For each checkin place find avg long/lat based on the user checkins in Jan at that location
	 * 	- Find avg(count) of checkins in Jan - use countTableName
	 *  - If count > avg(count) find avg lat/long (be careful to set the date to the Jan) - use latlongTableName
	 *  - Write avg lat/long to table
	 *  
	 * @param endDate 
	 * @param startDate 
	 * @param latlongTableName 
	 * @param countTableName 
	 * @param con 
	 * @param endDate2 
	 */
	private static void createcheckinPlaceCenter(Connection con,
			String outputTableName, String countTableName, String latlongTableName,
			String startDate, String endDate) {
		// get ids of all places visited in Jan
		String tableName = "checkinsJan";
		String columnName = "locid";
		HashSet<Integer> allLocationIds = DbUtil.getIds(con, tableName, columnName );

		// perform calculations for each place
		for(Integer locId: allLocationIds)
		{
			if(DbUtil.doesContainUserid(locId, "locID", outputTableName, con) == true ){
				// if id is seen in table continue;
				continue;
			}
			
			// Select checkins
			LongLat longLatVal = decideAvgLongLat(con, locId, latlongTableName, startDate, endDate);

			if(longLatVal != null){
				writeLongLatToDb(con, locId, "locId", longLatVal, outputTableName);
			} else{
				System.out.print(locId+ " has a null latlong val!! ");
			}
		}


	}

	private static LongLat decideAvgLongLat(Connection con, Integer locId,
			String latlongTableName, String startDate, String endDate) {
		LongLat avgLongLatVal = null;

		try {
			double totalLong=0.0;
			double totalLat=0.0;
			double count = 0.0;
				Statement stmt = con.createStatement();
				// Select locids
				String sqlSelect = "select latitude,longtitude " 
						+ " from " + latlongTableName 
						+ " where  locid="+locId
						+ " and time between "
						+ "\"" +startDate + "\"" 
						+ " and " + "\"" +endDate + "\"";

				//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
				ResultSet rs = stmt.executeQuery(sqlSelect);
				while (rs.next()) {
					double  latitude = rs.getFloat("latitude");
					double  longtitude = rs.getFloat("longtitude");

					totalLat+= latitude;
					totalLong+= longtitude;
					count++;
				}

			if(count > 0){
				avgLongLatVal = new LongLat(totalLong/count, totalLat/count);
			}
			
			stmt.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}



		return avgLongLatVal;
	}

	/**
	 * For each user find avg long/lat based on their checkins in Jan
	 * 	- Find avg(count) of checkins in Jan - use countTableName
	 *  - If count > avg(count) find avg lat/long (be careful to set the date to the Jan) - use latlongTableName
	 *  - Write avg lat/long to table
	 *  
	 * @param endDate 
	 * @param startDate 
	 * @param latlongTableName 
	 * @param countTableName 
	 * @param con 
	 * @param endDate2 
	 */
	private static void createHometownFinetuned(Connection con, String outputTableName, String countTableName, 
			String latlongTableName, String startDate, String endDate) {
		// get ids of all users
		String tableName = "checkinsJan";
		String columnName = "userid";
		HashSet<Integer> allUserIds = DbUtil.getIds(con, tableName, columnName );

		// perform calculations for each user
		for(Integer userId1: allUserIds)
		{
			// Select locids
			ArrayList<Integer> locIds = getLocIdsForHometownFineTune(con, countTableName, userId1);
			LongLat longLatVal = decideAvgLongLatForUser(con, userId1, locIds, latlongTableName, startDate, endDate);

			if(longLatVal != null){
				writeLongLatToDb(con, userId1, "userid", longLatVal, outputTableName);
			} else{
				System.out.print(userId1+ " , ");
			}
		}

	}


	private static void writeLongLatToDb(Connection con, Integer id,String firstFieldName,
			LongLat longLatVal, String outputTableName) {
		try{
			Statement stmt = con.createStatement();
			// INSERT a partial record
			String sqlInsert = "insert into " 
					+ outputTableName + "("+firstFieldName+",latitude,longtitude) "
					+ "values ("+ id +","+ longLatVal.latitude+","+longLatVal.longtitude +")";
			System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
			int countInserted = stmt.executeUpdate(sqlInsert);
			System.out.println(countInserted + " records inserted.\n");
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static LongLat decideAvgLongLatForUser(Connection con, Integer userId,
			ArrayList<Integer> locIds, String latlongTableName,
			String startDate, String endDate) {
		LongLat avgLongLatVal = null;

		try {
			double totalLong=0.0;
			double totalLat=0.0;
			double count = 0.0;
			for(Integer locId:locIds){
				Statement stmt = con.createStatement();
				// Select locids
				String sqlSelect = "select latitude,longtitude " 
						+ " from " + latlongTableName 
						+ " where userid=" + userId  
						+ " and locid="+locId
						+ " and time between "
						+ "\"" +startDate + "\"" 
						+ " and " + "\"" +endDate + "\"";

				//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
				ResultSet rs = stmt.executeQuery(sqlSelect);
				while (rs.next()) {
					double  latitude = rs.getFloat("latitude");
					double  longtitude = rs.getFloat("longtitude");

					totalLat+= latitude;
					totalLong+= longtitude;
					count++;
				}

				stmt.close();
				rs.close();

			}

			if(count > 0){
				avgLongLatVal = new LongLat(totalLong/count, totalLat/count);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}



		return avgLongLatVal;
	}

	private static ArrayList<Integer> getLocIdsForHometownFineTune(
			Connection con, String tableName, Integer userId) {

		ArrayList<Integer> locIds = new ArrayList<Integer>();
		try {
			Statement stmt = con.createStatement();
			// Select locids
			String sqlSelect = "select locid from " 
					+ "(select avg(count) as avgCount " 
					+ 	" from " + tableName 
					+ 	" where userid=" + userId + ")" 
					+ 	" as c1 " 
					+ "inner join " 
					+ "(select count,locid " 
					+	" from " + tableName 
					+ 	" where userid=" + userId + ")" 
					+	" as c2 "
					+ "on c2.count >= c1.avgCount";

			//System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) {
				int lId = rs.getInt("locid");
				locIds.add(lId);
			}

			stmt.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return locIds;
	}

	private static void createSubcheckinTable(Connection con, String tableName,
			String startDate, String endDate) {

		try {
			Statement stmt = con.createStatement();
			// INSERT a partial record
			String sqlInsert = "insert into "
					+ tableName
					+ " (userid, locId, count)"
					+ " select t1.userid, t1.locid, count(*) as count"
					+ " from(select "
					+ " userid as userid,locid as locid "
					+ " from checkins where time between "
					+ "\"" +startDate + "\"" 
					+ " and " + "\"" +endDate + "\"" 
					+ " ) as t1 "
					+ " group by t1.userid,t1.locid";

			System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
			int countInserted = stmt.executeUpdate(sqlInsert);
			System.out.println(countInserted + " records inserted to "+tableName);

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}



	}

	private static Double getUnVisitedCountRemoved(Connection con) {
		Double unvisitedCount = 0.0;
		try {

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get info from removed
			String sqlSelect = "select userid,locId from removed";

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rsRetrieve = stmnt.executeQuery(sqlSelect);


			while(rsRetrieve.next())
			{
				int userid = rsRetrieve.getInt("userid");
				int locid = rsRetrieve.getInt("locId");

				boolean doesCheckinsRemovedContains = doesContain(userid, locid, con);

				if(doesCheckinsRemovedContains == false){
					unvisitedCount++;
				}
			}

			//Close the Statement & connection
			stmnt.close();
			rsRetrieve.close();

		} catch (Exception e) {
			System.out.println("Error Connecting : "+e.getMessage());
		}

		return unvisitedCount;

	}

	private static boolean doesContain(Integer userId, Integer locId, Connection con) {
		boolean retVal = false;
		try {


			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// select a partial record
			String sql = "select * from checkinsRemoved "
					+ "where userId="+userId + " and locId="+locId;
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

	private static void updateRemoved(Connection con) {
		try {

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get info from removed
			String sqlSelect = "select userid,time,locId from removed";

			System.out.println("The SQL query is: " + sqlSelect);  // Echo for debugging
			ResultSet rsRetrieve = stmnt.executeQuery(sqlSelect);


			while(rsRetrieve.next())
			{
				int userid = rsRetrieve.getInt("userid");
				String latitude = rsRetrieve.getString("latitude");
				String longtitude = rsRetrieve.getString("longtitude");
				String timeTemp = rsRetrieve.getString("time");
				String time = timeTemp.substring(0, timeTemp.length()-2);
				int locId = rsRetrieve.getInt("locId");




				// remove from removedCheckins
				PreparedStatement stmt3 = con.prepareStatement("delete from removedCheckins " +
						"where userid=? and latitude=? and longtitude=? " +
						"and time=STR_TO_DATE(?,'%Y-%m-%e %H:%i:%s') and locId=?" );

				stmt3.setInt(1, userid);
				stmt3.setString(2, latitude);
				stmt3.setString(3, longtitude);
				stmt3.setString(4, time);
				stmt3.setInt(5, locId);


				System.out.println("The SQL query is: " + stmt3);  // Echo for debugging
				int rsRetrieve3 = stmt3.executeUpdate();


				stmt3.closeOnCompletion();

			}
			//Close the Statement & connection
			stmnt.close();
			rsRetrieve.close();

		} 

		catch (Exception e) {
			System.out.println("Error Connecting : "+e.getMessage());
		}


	}
	private static void createCheckinsRemoved() {
		try {
			// Loading the Database Connection Driver
			Class.forName("com.mysql.jdbc.Driver");

			//Connecting to MYSQL Database
			//SQL Database name is java
			//SQL server is localhost, username:root, password:nopassword 
			Connection con = DriverManager.getConnection("jdbc:mysql://path","","");

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// get size of checkins
			String sqlCount = "SELECT COUNT(*) AS count FROM checkinsRemoved";
			System.out.println("The SQL query is: " + sqlCount);  // Echo for debugging
			ResultSet rs = stmnt.executeQuery(sqlCount);
			rs.next();
			double size = rs.getInt("count");
			System.out.println(size + " records exists.\n");

			// get size of removed
			String sqlCount2 = "SELECT COUNT(*) AS count FROM removed";
			System.out.println("The SQL query is: " + sqlCount2);  // Echo for debugging
			ResultSet rs2 = stmnt.executeQuery(sqlCount2);
			rs2.next();
			double removedSize = rs2.getInt("count");
			System.out.println(removedSize + " records exists.\n");


			// remove %20 of the entires & add to removed table
			// SELECT * FROM table ORDER BY ID LIMIT n,1 should work. It says return one record starting at record n.
			int sizeTemp = (int)size;
			double removeSize = Math.floor(size*0.1)-removedSize;
			System.out.println("RemoveSize: " + removeSize);

			Random rand = new Random();			
			int randomVal = 0;
			for(int i=0; i<removeSize; i++)
			{
				randomVal = 1 + (rand.nextInt(Integer.MAX_VALUE) % sizeTemp);// in case o f 0 return 1(sql index starts from 1)
				System.out.println(randomVal + " is randomVal.");

				//get vals of that row
				String query = "select *" +
						"from checkinsRemoved order by userid limit "+ randomVal+",1";

				ResultSet rsRetrieve = stmnt.executeQuery(query);
				rsRetrieve.next();

				int userid = rsRetrieve.getInt("userid");
				String latitude = rsRetrieve.getString("latitude");
				String longtitude = rsRetrieve.getString("longtitude");
				String timeTemp = rsRetrieve.getString("time");
				String time = timeTemp.substring(0, timeTemp.length()-2);
				int locId = rsRetrieve.getInt("locId");


				// add to table removed 
				PreparedStatement stmt = con.prepareStatement(
						"insert into removed "
								+ "(userid,latitude,longtitude,time,locId)"
								+ "VALUES(?,?,?,STR_TO_DATE(?,'%Y-%m-%e %H:%i:%s'),?)");

				stmt.setInt(1, userid);
				stmt.setString(2, latitude);
				stmt.setString(3, longtitude);
				stmt.setString(4, time);
				stmt.setInt(5, locId);

				int countInserted = stmt.executeUpdate();
				System.out.println(countInserted + " records are inserted.\n");
				stmt.closeOnCompletion();

				// drop that row from checkinsRemoved
				PreparedStatement stmtDrop = con.prepareStatement(
						"delete from checkinsRemoved where" +
								" userid=" + "?" +
								" and latitude=" + "?" +
								" and longtitude=" + "?" +
								" and time=" + "?" +
								" and locId=" + "?");

				stmtDrop.setInt(1, userid);
				stmtDrop.setString(2, latitude);
				stmtDrop.setString(3, longtitude);
				stmtDrop.setString(4, time);
				stmtDrop.setInt(5, locId);

				int countDrop = stmtDrop.executeUpdate();
				System.out.println(countDrop + " records are dropped.\n");
				stmtDrop.closeOnCompletion();
				//
				sizeTemp--;//size is reduced since we have dropped a row
				
				rsRetrieve.close();
			}
			System.out.println();




			//Close the Statement & connection
			stmnt.close();
			con.close();
			rs.close();
			rs2.close();

		} 

		catch (Exception e) {
			System.out.println("Error Connecting : "+e.getMessage());
		}

	}
	public static void createUsers()
	{
		try {
			// Loading the Database Connection Driver
			Class.forName("com.mysql.jdbc.Driver");

			//Connecting to MYSQL Database
			//SQL Database name is java
			//SQL server is localhost, username:root, password:nopassword 
			Connection con = DriverManager.getConnection("jdbc:mysql://path","","");

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// Open the file
			FileInputStream fstream;
			String path = ".//dataset//checkin2011//hometown.csv";
			fstream = new FileInputStream(path);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			// read 1 line for header
			String strLine=br.readLine();
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   // read info
			{
				String[] splitted = strLine.split(",");
				String uid = splitted[0];
				Integer id = Integer.parseInt(uid);
				System.out.println("id: " + id);

				String hometownid = splitted[1];
				Integer htid = Integer.parseInt(hometownid);
				System.out.println("htid: " + htid);

				// INSERT a partial record
				String sqlInsert = "insert into users (id,hometownid) "
						+ "values ("+ id+","+htid +")";
				System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
				int countInserted = stmnt.executeUpdate(sqlInsert);
				System.out.println(countInserted + " records inserted.\n");

			}

			//Close the input stream
			in.close();

			//Close the Statement & connection
			stmnt.close();
			con.close();
		} 

		catch (Exception e) {
			System.out.println("Error Connecting : "+e.getMessage());
		}
	}

	public static void createFriends()
	{
		try {
			// Loading the Database Connection Driver
			Class.forName("com.mysql.jdbc.Driver");

			//Connecting to MYSQL Database
			//SQL Database name is java
			//SQL server is localhost, username:root, password:nopassword 
			Connection con = DriverManager.getConnection("jdbc:mysql://path","","");

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// Open the file
			FileInputStream fstream;
			String path = ".//dataset//checkin2011//FoursquareFriendship.csv";
			fstream = new FileInputStream(path);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			// read 1 line for headers
			String strLine = br.readLine();
			//Read File Line By Line
			int tempIndex = 0;
			while ((strLine = br.readLine()) != null)   // read info
			{
				String[] splitted = strLine.split(",");
				String uid1 = splitted[0];
				Integer id1 = Integer.parseInt(uid1);
				System.out.println("id1: " + id1);

				String uid2 = splitted[1];
				Integer id2 = Integer.parseInt(uid2);
				System.out.println("id2: " + id2);

				// INSERT a partial record
				String sqlInsert = "insert into friends (user1id,user2id) "
						+ "values ("+ id1+","+id2 +")";
				System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
				int countInserted = stmnt.executeUpdate(sqlInsert);
				System.out.println(countInserted + " records inserted.\n");


			}

			//Close the input stream
			in.close();

			//Close the Statement & connection
			stmnt.close();
			con.close();
		} 

		catch (Exception e) {
			System.out.println("Error Connecting : "+e.getMessage());
		}
	}


	public static void createCheckins()
	{
		try {
			// Loading the Database Connection Driver
			Class.forName("com.mysql.jdbc.Driver");

			//Connecting to MYSQL Database
			//SQL Database name is java
			//SQL server is localhost, username:root, password:nopassword 
			Connection con = DriverManager.getConnection("jdbc:mysql://path","","");

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// Open the file
			FileInputStream fstream;
			String path = ".//dataset//checkin2011//FoursquareCheckins20110101-20111231.csv";
			fstream = new FileInputStream(path);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			// read 1 line for headers
			String strLine = br.readLine();
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   // read info
			{	
				System.out.println(strLine);
				String[] splitted = strLine.split(",");
				String uid1 = splitted[0];
				Integer userid = Integer.parseInt(uid1);
				System.out.println("userid: " + userid);

				String latitude = splitted[1];
				System.out.println("latitude: " + latitude);

				String longtitude = splitted[2];
				System.out.println("longtitude: " + longtitude);

				String time = splitted[3];//2011-08-16 19:56:49
				System.out.println("time: " + time.toString());

				String locidStr = splitted[4];
				Integer locId = Integer.parseInt(locidStr);
				System.out.println("locId: " + locId);

				// INSERT a partial record
				PreparedStatement stmt = con.prepareStatement(
						"insert into checkins "
								+ "(userid,latitude,longtitude,time,locId)"
								+ "VALUES(?,?,?,STR_TO_DATE(?,'%Y-%m-%e %H:%i:%s'),?)");

				stmt.setInt(1, userid);
				stmt.setString(2, latitude);
				stmt.setString(3, longtitude);
				stmt.setString(4, time);
				stmt.setInt(5, locId);

				int countInserted = stmt.executeUpdate();
				System.out.println(countInserted + " records inserted.\n");
				stmt.closeOnCompletion();

			}
			//Close the input stream
			in.close();

			//Close the Statement & connection
			stmnt.close();
			con.close();
		} 

		catch (Exception e) {
			System.out.println("Error Connecting : "+e.getMessage());
		}
	}

}
