package main;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import similarity.CosineRating;
import data.Rating;
import data.UserCheckin;

public class CheckinDbRatedSimilarityCalculater {

	public static void main(String[] args) {

		// Create a table of user x user
		// + simVals (loc sim, checkin sim, friends sim, isFriend...)

		Checkin2011DBReader reader = new Checkin2011DBReader();

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
		createTables(con);

		// get ids of all users
		String tableName = "checkinsJan";
		String columnName = "userid";
		HashSet<Integer> allUserIds = DbUtil.getIds(con, tableName, columnName );

		// perform calculations for each user
		for(Integer userId1: allUserIds)
		{
			//feldname
			String fieldName = "checkinLocSimCosRated";//ok
			if(DbUtil.doesContainUserid(userId1, "userid1", fieldName+"Table", con) 
					== true ){
				// if user is seen in table continue;
				continue;
			}

			System.out.println(fieldName);
			for(Integer userId2: allUserIds)
			{	
				if(userId1.equals(userId2)){
					continue;
				}else{

					// calculate similarities &  write to db

					// 6) CosineSim - Checkin location sim ("checkinLocSimCos")
					Double checkinLocSimCosRated = findCheckinLocSimCosRated(userId1, userId2, 
							reader, con, DbUtil.thresholds); 
					if(checkinLocSimCosRated > 0){
						CheckinDbSimilarityCalculater.writeToDb(userId1, userId2, checkinLocSimCosRated.toString(), fieldName, con);
					}

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


	private static Double findCheckinLocSimCosRated(Integer userId1,
			Integer userId2, Checkin2011DBReader reader, Connection con,
			Double[] thresholds) {
		if(userId2==84){
			System.out.println("debug");
		}
		Double retVal = 0.0;
		try {
			UserCheckin user1 = new UserCheckin(userId1);
			UserCheckin user2 = new UserCheckin(userId2);

			// read checkins&count of the users
			ArrayList<Rating> checkins1 = reader.getRatingFromCheckinJan(user1.getUserId(), con);
			ArrayList<Rating> checkins2 = reader.getRatingFromCheckinJan(user2.getUserId(), con);

			// update ratings scores based on thresholds 
			// & find means values at the same time
			Double mean1 = updateRatingScores(thresholds, checkins1);
			Double mean2 = updateRatingScores(thresholds, checkins2);

			// calculate rate based cosine
			//TODO note that I set means to 0 in order not to calculate ajusted cossim
			mean1 = 0.0;
			mean2 = 0.0;
			retVal = CosineRating.cosine(checkins1, mean1, checkins2, mean2);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return retVal;
	}

	/**
	 *  update rating scores based on thresholds,
	 *  also find mean using the updated values
	 * @param thresholds
	 * @param checkinRatings
	 * @return 
	 */
	private static Double updateRatingScores(Double[] thresholds,
			ArrayList<Rating> checkinRatings) {
		Double mean = 0.0;

		for(Rating rating: checkinRatings){
			DbUtil.updateRatingScore(thresholds,rating);

			// sum vals on mean
			mean += rating.getRating();
		}

		// get avg on mean using size
		mean = new Double (mean/checkinRatings.size());

		return mean;
	}

	private static void createTables(Connection con) {
		try {

			//Using the Connection Object now Create a Statement
			Statement stmnt = con.createStatement();

			// create table
			String sqlCreate = "create table checkinLocSimCosRatedTable(userid1 int, userid2 int, "
					+ "checkinLocSimCosRated varchar(50), primary key(userid1,userid2))";
			System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
			stmnt.execute(sqlCreate);

			//Close the Statement & connection
			stmnt.close();

		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}







}
