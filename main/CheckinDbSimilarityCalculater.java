package main;

import inputReader.Checkin2011DBReader;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import outputWriter.FilePrinter;
import similarity.Cosine;
import data.Checkin;
import data.CheckinTime;
import data.CheckinTime.TimeCategory;
import data.UserCheckin;

public class CheckinDbSimilarityCalculater {

	public static void main(String[] args) {
		// Create a table of user x user
		// + simVals (loc sim, checkin sim, friends sim, isFriend...)

		Checkin2011DBReader reader = new Checkin2011DBReader();
		FilePrinter printer = new FilePrinter(false);


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

		// create tables
		//System.out.println("Don't forget to create related tables!!");
		//createTables(con);

		// get ids of all users
		String tableName = "checkinsJan";
		String columnName = "userid";
		HashSet<Integer> allUserIds = DbUtil.getIds(con, tableName, columnName );

		// used by HvrDist fields 
		HashMap<Integer, LongLat> allHtuned = null;
		HashMap<Integer, LongLat> allLocCenter = null;


		// perform calculations for each user
		for(Integer userId1: allUserIds)
		{
			long startTime = System.currentTimeMillis();

			//String fieldName = "friendship"; boolean isSymetrric = false;//ok
			/*String fieldName = "hometownSim";//ok
			boolean isSymetrric = false;*/
			//String fieldName = "commonFriendSim";//ok
			//String fieldName = "checkinLocSim";//ok
			//String fieldName = "checkinLocSimCos";//ok
			//String fieldName = "friendSimCos";//ok
			//String fieldName = "hometownFinetunedSim"; boolean isSymetrric = true;//ok 
			//String fieldName = "hometownFinetunedUbyUHvrDist"; boolean isSymetrric = false;
			/*if(allHtuned == null){
				allHtuned = reader.getAllHometownFinetuned(con);
			}*/
			String fieldName = "timeSimCos";  boolean isSymetrric = false; 


			// if user is seen in table continue;
			if(DbUtil.doesContainUserid(userId1, "userid1", fieldName+"Table", con) 
					== true ){
				// if user is seen in table continue;
				continue;
			}

			// perform analysis now
			System.out.println(fieldName+" "+userId1);
			HashMap<Integer, Double> simValMap = null;

			// get simVals if symmetric
			if(isSymetrric){
				simValMap = populateSimValMap(userId1, fieldName, reader, con);
			} else {
				simValMap = new HashMap<Integer, Double>();
			}

			calculateTimeSimVals(userId1, allUserIds, fieldName, simValMap, reader, con);

/*			// find sim/dist and write to db
			// find simVals among users
			for(Integer userId2: allUserIds)
			{	
				if(userId1.equals(userId2) || simValMap.containsKey(userId2)){
					continue;
				}else{

					//					try {
					//						Thread.sleep(1);
					//					} catch (InterruptedException e) {
					//						// TODO Auto-generated catch block
					//						e.printStackTrace();
					//					}

					// calculate similarities &  write to db

					// 1) friendship relevance ("friendship")
					Double friendship = findFriendship(userId1,userId2, reader, con);
					if(friendship > 0){
						writeToDb(userId1, userId2, friendship.toString(), fieldName, con);
					}

					
					// 2) Living in the same loc ("hometownSim")
					Double hometownSim = findHometownSim(userId1,userId2, reader, con);
					if(hometownSim > 0){
						writeToDb(userId1, userId2, hometownSim.toString(), fieldName, con);
					}
					 

					// 3) Common friend count ("commonFriendSim") - jaccard sim 
					Double commonFriendSim = findCommonFriendSim(userId1,userId2, reader, con);
					if(commonFriendSim > 0){
						writeToDb(userId1, userId2, commonFriendSim.toString(), fieldName, con);
					}


					// 5) Checkin location(in terms of ~city) sim ("checkinLocSim")-jaccard sim //TODO tam jacard degil
					Double checkinLocSim = findCheckinLocSim(userId1,userId2, reader, con);
					if(checkinLocSim > 0){
						writeToDb(userId1, userId2, checkinLocSim.toString(), fieldName, con);
					}

					// 6) CosineSim - Checkin location sim ("checkinLocSimCos")
					Double checkinLocSimCos = findCheckinLocSimCos(userId1,userId2, reader, con);
					if(checkinLocSimCos > 0){
						writeToDb(userId1, userId2, checkinLocSimCos.toString(), fieldName, con);
					}

					// 7) CosineSim - Checkin friends sim ("friendSimCos")
					Double friendSimCos = findFriendSimCos(userId1,userId2, reader, con);
					if(friendSimCos > 0){
						writeToDb(userId1, userId2, friendSimCos.toString(), fieldName, con);
					}

					// 8) Living in the same loc ("hometownFineTunedSim")
					Double hometownFinetunedSim = findHometownFineTunedSim(userId1,userId2, reader, con);
					if(hometownFinetunedSim > 0){
						simValMap.put(userId2, hometownFinetunedSim);
						//writeToDb(userId1, userId2, hometownFinetunedSim.toString(), fieldName, con);
						//System.out.println(simValMap.size());
					 	

					// 9) Distance of the users ("hometownFineTuned")
						Double hometownFinetunedDist = findHometownFineTunedDist(userId1,userId2, allHtuned);
						//Double hometownFinetunedDist = findHometownFineTunedDist(userId1,userId2, reader, con);
						if(hometownFinetunedDist > 0){
							simValMap.put(userId2, hometownFinetunedDist);
							//writeToDb(userId1, userId2, hometownFinetunedSim.toString(), fieldName, con);
							//System.out.println(simValMap.size());	
						}

					// 10) CosineSim - Checkin time (based on 2013-Liu et al. (Aberer vs. Epfl'den)) ("timeSimCos")
					Double timeSimCos = findTimeSimCos(userId1,userId2, reader, con);
					if(timeSimCos > 0){
						writeToDb(userId1, userId2, timeSimCos.toString(), fieldName, con);
					}
				}
			}*/

			long endTime = System.currentTimeMillis();
			System.out.println("simCalc msec: " + (endTime-startTime));

			writeToDb(userId1, simValMap, fieldName, con);
			simValMap=null;

			long endTime2 = System.currentTimeMillis();
			try {
				printer.printString(".//result//simCalcTime","simCalc msec: " + (endTime2-startTime));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private static void calculateTimeSimVals(Integer userId1,
			HashSet<Integer> allUserIds, String fieldName,
			HashMap<Integer, Double> simValMap, Checkin2011DBReader reader,
			Connection con) {
		// get user1's checkins time  info 
		ArrayList<String> checkinTimeStrList1 = reader.getCheckinTimeFromCheckinJan(userId1, con);
		HashMap<TimeCategory, Double> checkinTimes1 = CheckinTime.mapToTimeCategory(checkinTimeStrList1);

		// find sim/dist and write to db
		// find simVals among users
		for(Integer userId2: allUserIds)
		{	
			if(userId1.equals(userId2) || simValMap.containsKey(userId2)){
				continue;
			}else{
				// 10) CosineSim - Checkin time (based on 2013-Liu et al. (Aberer vs. Epfl'den)) ("timeSimCos")
				// get user2's checkins time  info first
				ArrayList<String> checkinTimeStrList2 = reader.getCheckinTimeFromCheckinJan(userId2, con);
				HashMap<TimeCategory, Double> checkinTimes2 = CheckinTime.mapToTimeCategory(checkinTimeStrList2);
				// calculate sim
				Double timeSimCos = Cosine.cosine(checkinTimes1, checkinTimes2);
				if(timeSimCos > 0){
					//writeToDb(userId1, userId2, timeSimCos.toString(), fieldName, con);
					simValMap.put(userId2, timeSimCos);
					//System.out.println(simValMap.size());	
				}
			}
		}

	}


	private static HashMap<Integer, Double> populateSimValMap(Integer userId1, String fieldName, 
			Checkin2011DBReader reader, Connection con) {
		HashMap<Integer, Double> simValMap = reader.getSimValsTo(userId1,fieldName, con);
		return simValMap;
	}

	/**
	 * Find cosine sim between user1 and user2 in terms of checkin time
	 * Cluster checkin time to sub groups based on Liu et al.(2013, Personalized Point-of-Interest Recommendation by... @ Epfl)
	 * @param userId1
	 * @param userId2
	 * @param reader
	 * @param con
	 * @return
	 */
	private static Double findTimeSimCos(Integer userId1, Integer userId2,
			Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		try {
			// read checkins time of the users
			ArrayList<String> checkinTimeStrList1 = reader.getCheckinTimeFromCheckinJan(userId1, con);
			ArrayList<String> checkinTimeStrList2 = reader.getCheckinTimeFromCheckinJan(userId2, con);
			HashMap<TimeCategory, Double> checkinTimes1 = CheckinTime.mapToTimeCategory(checkinTimeStrList1);
			HashMap<TimeCategory, Double> checkinTimes2 = CheckinTime.mapToTimeCategory(checkinTimeStrList2);

			retVal = Cosine.cosine(checkinTimes1, checkinTimes2);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	/**
	 * Find if user1 and user2 are from same hometown
	 * @param userId1
	 * @param userId2
	 * @param reader
	 * @return
	 */
	private static Double findHometownFineTunedSim(Integer userId1,
			Integer userId2, Checkin2011DBReader reader, Connection con) {

		Double retVal = 0.0;
		try {
			double eucDist= calculateEucDist(userId1, userId2, reader, con);
			retVal = DbUtil.calculateEucDistSim(eucDist);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	/**
	 * Find if user1 and user2 are from same hometown
	 * @param userId1
	 * @param userId2
	 * @param reader
	 * @return
	 */
	private static Double findHometownFineTunedSimHeversin(Integer userId1,
			Integer userId2, Checkin2011DBReader reader, Connection con) {

		Double retVal = 0.0;
		try {
			double dist= reader.calculateHeversineDist(userId1, userId2, con);
			double dMax = DbUtil.earthRadious;
			retVal = 1.0-(Math.min(dist,dMax)/dMax);
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	/**
	 * Find dist among user1 and user2 
	 * @param userId1
	 * @param userId2
	 * @param reader
	 * @return
	 */
	private static Double findHometownFineTunedDist(Integer userId1,
			Integer userId2, Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		try {
			retVal = reader.calculateHeversineDist(userId1, userId2, con);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	private static Double findHometownFineTunedDist(Integer userId1,
			Integer userId2, HashMap<Integer, LongLat> allHtuned) {
		Double retVal = 0.0;
		try {
			LongLat longLat1 = allHtuned.get(userId1);
			LongLat longLat2 = allHtuned.get(userId2);

			retVal = DbUtil.calculateHaversineDist(longLat1, longLat2);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	private static Double findHtownLocCenterDist(Integer userId1,
			Integer locid, HashMap<Integer, LongLat> allHtuned,
			HashMap<Integer, LongLat> allLocCenter) {
		Double retVal = 0.0;
		try {
			LongLat longLat1 = allHtuned.get(userId1);
			LongLat longLat2 = allLocCenter.get(locid);

			retVal = DbUtil.calculateHaversineDist(longLat1, longLat2);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}
	private static Double calculateEucDist(Integer userId1, Integer userId2,
			Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		HashMap<Integer,LongLat> longLatMap = reader.getHometownFinetuned(userId1, userId2, con);
		LongLat longLat1 = longLatMap.get(userId1);
		LongLat longLat2 = longLatMap.get(userId2);

		retVal = DbUtil.calculateEucDist(longLat1, longLat2);

		longLat1 = null; 
		longLat2 = null;
		longLatMap = null;

		return retVal;
	}

	private static Double findFriendSimCos(Integer userId1, Integer userId2,
			Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		try {
			UserCheckin user1 = new UserCheckin(userId1);
			UserCheckin user2 = new UserCheckin(userId2);

			// read friends of the users
			ArrayList<Integer> friends1 = reader.getFriendsIds(user1, con);
			ArrayList<Integer> friends2 = reader.getFriendsIds(user2, con);

			retVal = Cosine.cosine(friends1, friends2);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	private static Double findCheckinLocSimCos(Integer userId1,
			Integer userId2, Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		try {
			UserCheckin user1 = new UserCheckin(userId1);
			UserCheckin user2 = new UserCheckin(userId2);

			// read checkins of the users
			ArrayList<Checkin> checkins1 = reader.getCheckinFromCheckinJan(user1.getUserId(), con);
			ArrayList<Checkin> checkins2 = reader.getCheckinFromCheckinJan(user2.getUserId(), con);

			retVal = Cosine.cosine(checkins1, checkins2);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	private static void createTables(Connection con) {
		try {

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// create table friendshipTable
			String sqlCreate = "create table friendshipTable(userid1 int, userid2 int, friendship varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			String sqlIndex = "ALTER TABLE  friendshipTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table hometownSimTable
			sqlCreate = "create table hometownSimTable(userid1 int, userid2 int, hometownSim varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  hometownSimTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table commonFriendSimTable
			sqlCreate = "create table commonFriendSimTable(userid1 int, userid2 int, commonFriendSim varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  commonFriendSimTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table checkinLocSimTable
			sqlCreate = "create table checkinLocSimTable(userid1 int, userid2 int, checkinLocSim varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  checkinLocSimTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table checkinLocSimCosTable
			sqlCreate = "create table checkinLocSimCosTable(userid1 int, userid2 int, checkinLocSimCos varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  checkinLocSimCosTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table friendSimCosTable
			sqlCreate = "create table friendSimCosTable(userid1 int, userid2 int, friendSimCos varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  friendSimCosTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table hometownFinetunedSimTable
			sqlCreate = "create table hometownFineTunedSimTable(userid1 int, userid2 int, hometownFinetunedSim varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  hometownFineTunedSimTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table hometownFinetunedUbyUHvrDistTable
			sqlCreate = "create table hometownFinetunedUbyUHvrDistTable(userid1 int, userid2 int, hometownFinetunedUbyUHvrDist varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  hometownFinetunedUbyUHvrDistTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table hometownFinetunedUbyLocHvrDistTable
			sqlCreate = "create table hometownFinetunedUbyLocHvrDistTable(userid1 int, locid int, hometownFinetunedUbyLocHvrDist varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  hometownFinetunedUbyLocHvrDistTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			// create table timeSimCosTable
			sqlCreate = "create table timeSimCosTable(userid1 int, userid2 int,  timeSimCos varchar(50))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			sqlIndex = "ALTER TABLE  timeSimCosTable ADD INDEX (userid1)";
			System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
			stmnt.execute(sqlIndex);

			//Close the Statement & connection
			stmnt.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Find ratio of #commonFriends/#user1Friends
	 * (use friend's userid for comparisons)
	 * @param userId1
	 * @param userId2
	 * @param reader
	 * @return
	 */
	private static Double findCommonFriendSim(Integer userId1, Integer userId2,
			Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		try {
			UserCheckin user1 = new UserCheckin(userId1);
			UserCheckin user2 = new UserCheckin(userId2);

			// read friends of the users
			Integer friends1Size = reader.getFriendsCount(user1, con);
			Integer friends2Size = reader.getFriendsCount(user2, con);

			if(friends1Size == 0 || friends2Size == 0){
				return retVal;
			}

			// find common #commonFriends
			Double common = (double) reader.findCommonFriendCount(userId1, userId2, con);

			// set retVal
			retVal = common/(friends1Size + friends2Size - common);	

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	/**
	 * Find ratio of #commonCheckinLoc/#user1CheckinLoc
	 * (use locid for comparisons)
	 * @param userId1
	 * @param userId2
	 * @param reader
	 * @return
	 */
	private static Double findCheckinLocSim(Integer userId1, Integer userId2,
			Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		try {
			// read checkins of the users
			Integer checkin1Size = reader.getCheckinCount(userId1, con);
			Integer checkin2Size = reader.getCheckinCount(userId2, con);

			if(checkin1Size == 0 || checkin2Size == 0){
				return retVal;
			}

			// find common #commonCheckins
			Double common = (double) reader.findCommonCheckinCount(userId1, userId2, con);

			// set retVal
			retVal = common/(checkin1Size + checkin2Size - common);	

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	/**
	 * Find if user1 and user2 are from same hometown
	 * @param userId1
	 * @param userId2
	 * @param reader
	 * @return
	 */
	private static Double findHometownSim(Integer userId1, Integer userId2,
			Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		try {
			boolean isFromSameHometown = reader.isFromSameHometown(userId1, userId2, con);
			// if same location
			if(isFromSameHometown){
				retVal = 1.0;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return retVal;
	}

	/**
	 * Find if user2 is a friend of user1
	 * @param userId1
	 * @param userId2
	 * @param reader 
	 * @return
	 */
	private static Double findFriendship(Integer userId1, Integer userId2, 
			Checkin2011DBReader reader, Connection con) {
		Double retVal = 0.0;
		// get friends list of user1
		boolean arefriends = reader.areFriends(userId1, userId2, con);

		// if userid2 is element of friendsList return 1.0
		if(arefriends){
			retVal = 1.0;
		}

		return retVal;
	}

	static void writeToDb(Integer userId1, Integer userId2,
			String fieldVal, String fieldName, Connection con) {
		try {

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// INSERT a partial record
			String sqlInsert = "insert into "+ fieldName+ "Table(userid1,userid2,"+fieldName+") "
					+ "values ("+ userId1+","+userId2 +","+ fieldVal +")";
			//System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
			int countInserted = stmnt.executeUpdate(sqlInsert);
			//System.out.println(countInserted + " records inserted.\n");

			//Close the Statement & connection
			stmnt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

	}

	private static void writeToDb(Integer userId1,
			HashMap<Integer, Double> simValMap, String fieldName, Connection con) {
		long startTime = System.currentTimeMillis();
		try {

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// INSERT a partial record
			String sqlInsertBase = "insert into "+ fieldName+ "Table(userid1,userid2,"+fieldName+") values ";

			// insert altogether for 200 cases
			String sqlInsert = null;
			int valCount = 0;
			for(Entry<Integer, Double> e : simValMap.entrySet()){

				if(valCount == 0){
					sqlInsert = sqlInsertBase;
				}
				Integer userId2 = e.getKey();
				Double fieldVal = e.getValue();

				if(valCount != 0){
					sqlInsert += ",";
				}
				sqlInsert += " ("+ userId1+","+userId2 +","+ fieldVal +")";
				valCount++;

				if(valCount == 300){
					// System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
					int countInserted = stmnt.executeUpdate(sqlInsert);
					// System.out.println(countInserted + " records inserted.\n");
					valCount = 0;
					sqlInsert = null;
				}
			}

			//insert last ones
			if(valCount > 0){
				//System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
				int countInserted = stmnt.executeUpdate(sqlInsert);
				//System.out.println(countInserted + " records inserted.\n");
			}

			//Close the Statement & connection
			stmnt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		long endTime = System.currentTimeMillis();
		System.out.println("write msec: "+ (endTime-startTime));
	}


	private static void writeToDbUserLoc(Integer userId1,
			HashMap<Integer, Double> simValMap, String fieldName, Connection con) {
		long startTime = System.currentTimeMillis();
		try {

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();


			// INSERT a partial record
			String sqlInsertBase = "insert into "+ fieldName+ "Table(userid1,locid,"+fieldName+") values ";

			// insert altogether for 200 cases
			String sqlInsert = null;
			int valCount = 0;
			for(Entry<Integer, Double> e : simValMap.entrySet()){

				if(valCount == 0){
					sqlInsert = sqlInsertBase;
				}
				Integer locid = e.getKey();
				Double fieldVal = e.getValue();

				if(valCount != 0){
					sqlInsert += ",";
				}
				sqlInsert += " ("+ userId1+","+locid +","+ fieldVal +")";
				valCount++;

				if(valCount == 300){
					// System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
					int countInserted = stmnt.executeUpdate(sqlInsert);
					// System.out.println(countInserted + " records inserted.\n");
					valCount = 0;
					sqlInsert = null;
				}
			}

			//insert last ones
			if(valCount > 0){
				//System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
				int countInserted = stmnt.executeUpdate(sqlInsert);
				//System.out.println(countInserted + " records inserted.\n");
			}

			//Close the Statement & connection
			stmnt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		long endTime = System.currentTimeMillis();
		System.out.println("write msec: "+ (endTime-startTime));
	}



}
