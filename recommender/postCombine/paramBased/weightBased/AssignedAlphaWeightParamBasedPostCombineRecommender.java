package recommender.postCombine.paramBased.weightBased;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import outputWriter.Printer;
import recommender.postCombine.paramBased.WeightParamBasedPostCombineRecommender;
import data.Checkin;
import data.Recommendation;

public abstract class AssignedAlphaWeightParamBasedPostCombineRecommender
extends WeightParamBasedPostCombineRecommender {

	// test for all the elements of alphaValL�st
	public static List<Double[]> alphaValList;


	// abstract methods
	abstract protected void decideAlphaValArray();


	// methods	
	public AssignedAlphaWeightParamBasedPostCombineRecommender(
			Integer numberOfSimilarUsers, Double userSimilarityThreshold,
			Double minScoreVal, Double maxScoreVal, Printer printer,
			Checkin2011DBReader reader, RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);

	}

	@Override
	protected void assignAlphaValues() {
		// It is assigned later on, in a loop by calling setAlphaValues(..)
		int size = recTypeList.size();
		this.alphaList = new ArrayList<Double>(size);
		
		// decide on which alpha values will be used for evaluation
		decideAlphaValArray();
	}
	
	@Override
	protected Double getAlphaValue(int index) {
		return alphaList.get(index);
	}

	public void setAlphaValues(Double[] alphaValues) {
		this.alphaList.clear();
		for(Double val:alphaValues){
			this.alphaList.add(val);
		}
	}

	public List<Double[]> getAlphaValList() {
		return alphaValList;
	}

	//TODO overwrite by AssignedAlpha Recommenders
		protected void populateDatabase(String dbName, Integer userid, 
				ArrayList<Recommendation> recList,
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
					String statementStr = "insert into "+dbName+" " 
							+ "(userid,latitude,longtitude,time,locId, score, recRank, ";
					int size = alphaList.size();
					for(int i=0; i<size-1; i++){
						statementStr += "alpha"+ i + ", ";
					}
					statementStr += "alpha"+ (size-1) + ")";

					statementStr += "VALUES(?,?,?,STR_TO_DATE(?,'%Y-%m-%e %H:%i:%s'),?,?,?,";
					for(int i=0; i<size-1; i++){
						statementStr += "?,";
					}
					statementStr += "?)";
					PreparedStatement stmt = con.prepareStatement(statementStr);

					stmt.setInt(1, userid);
					stmt.setString(2, latitude.toString());
					stmt.setString(3, longtitude.toString());
					stmt.setString(4, time);
					stmt.setInt(5, locid);
					stmt.setString(6, score.toString());
					stmt.setInt(7, recRank);
					for(int i = 0; i< size; i++){
						stmt.setString(8+i, alphaList.get(i).toString());	
					}
					
					

					recRank++;

					int countInserted = stmt.executeUpdate();	
					
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
					+ "time datetime,locId int,score varchar(50), recRank int, ";
			int size = recTypeList.size(); // TODO See VoteBased vercion of this function
			for(int i=0; i<size-1; i++){
				sqlCreate += "alpha"+ i + " varchar(50), ";
			}
			sqlCreate += "alpha"+ (size-1) + " varchar(50))";

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

}
