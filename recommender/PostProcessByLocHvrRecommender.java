package recommender;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.PriorityQueue;

import main.DbUtil;
import main.LongLat;
import outputWriter.Printer;
import data.Checkin;
import data.Recommendation;
import data.UserCheckin;

public abstract class PostProcessByLocHvrRecommender extends Recommender {

	public static Double[] distThresholdList = {1.0, 5.0, 10.0, 25.0, 100.0};
	protected Double distThreshold;
	protected Double eucDistSimThreshold;
	
	public PostProcessByLocHvrRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type, Double distThreshold, Double eucDistSimThreshold) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		this.distThreshold = distThreshold;
		this.eucDistSimThreshold = eucDistSimThreshold;
	}
	
	public PostProcessByLocHvrRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		this.distThreshold = 5.0;
		this.eucDistSimThreshold = 0.80;
	}
	
	@Override
	/**
	 * Add to recommendation list by first controlling the filter
	 */
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

	// DO NOT forget to override when necessary-hvr dist etc.
	/**
	 * Control the condition and if match return true, such that the recommendation can be add to the result
	 * @param userHometown
	 * @param r
	 * @param con
	 * @return
	 */
	protected boolean canAddToResult(UserCheckin u, Recommendation r, Connection con) {
		// control if loc-hometownfineTuned dist is in given threshold
		boolean retVal = false;
		Integer userId = u.getUserId();
		LongLat userHometown = reader.getHometownFinetuned(userId, con);
		
		Checkin checkin = (Checkin) r.getSubject();
		Integer locId = checkin.getLocationId();
		LongLat locCenter = reader.getLocationCenter(locId, con);
		
		Double dist = DbUtil.calculateHaversineDist(userHometown, locCenter);
		if(dist <= distThreshold){
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
									+ "(userid,latitude,longtitude,time,locId, score, recRank, threshold)"
									+ "VALUES(?,?,?,STR_TO_DATE(?,'%Y-%m-%e %H:%i:%s'),?,?,?, ?)");

					stmt.setInt(1, userid);
					stmt.setString(2, latitude.toString());
					stmt.setString(3, longtitude.toString());
					stmt.setString(4, time);
					stmt.setInt(5, locid);
					stmt.setString(6, score.toString());
					stmt.setInt(7, recRank);
					stmt.setString(8, distThreshold.toString());
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

		@Override
		protected void createRecTable(String dbName, Connection con) {
			try {
				//Using the Connection Object now Create a Statement
				Statement stmnt = con.createStatement();

				// create table
				String sqlCreate = "create table "+ dbName+
						"(userid int,latitude varchar(50),longtitude varchar(50),"
						+ "time datetime,locId int,score varchar(50), recRank int, " 
						+ "threshold varchar(50))";
				//System.out.println("The SQL query is: " + sqlCreate);  // Echo for debugging
				stmnt.execute(sqlCreate);

				String sqlIndex = "alter table "+ dbName + " add index (userid)"; 
				//System.out.println("The SQL query is: " + sqlIndex);  // Echo for debugging
				stmnt.execute(sqlIndex);

				//Close the Statement & connection
				stmnt.close();


			}  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	public Double getDistThreshold() {
		return distThreshold;
	}

	public void setDistThreshold(Double distThreshold) {
		this.distThreshold = distThreshold;
	}
	
	

	public Double getEucDistSimThreshold() {
		return eucDistSimThreshold;
	}

	public void setEucDistSimThreshold(Double eucDistSimThreshold) {
		this.eucDistSimThreshold = eucDistSimThreshold;
	}

	@Override
	public String toString() {
		return "PostProcessByLocRecommender [distThreshold=" + distThreshold
				+ ", eucDistSimThreshold=" + eucDistSimThreshold + "]";
	}

	
	
	
}
