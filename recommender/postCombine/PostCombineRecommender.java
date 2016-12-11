package recommender.postCombine;

import inputReader.Checkin2011DBReader;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import outputWriter.Printer;
import recommender.Recommender;
import recommender.RecommenderCreater;
import data.UserCheckin;

public abstract class PostCombineRecommender extends Recommender {

	protected List<RecType> recTypeList = null;

	/*
	 * Abstract methods
	 */
	abstract protected ArrayList<UserCheckin> combineSimUsers(List<List<UserCheckin>> simUserList,
			UserCheckin targetUser, Connection con);
	
	/*
	 *  Methods
	 */
	
	public PostCombineRecommender(Integer numberOfSimilarUsers,
			Double userSimilarityThreshold, Double minScoreVal,
			Double maxScoreVal, Printer printer, Checkin2011DBReader reader,
			RecType type) {
		super(numberOfSimilarUsers, userSimilarityThreshold, minScoreVal, maxScoreVal,
				printer, reader, type);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	/**
	 * find k-many similar users
	 */
	public ArrayList<UserCheckin> getMostSimilarUsers(UserCheckin user,
			Connection con, Integer numberOfSimilarUsers,
			Double userSimilarityThreshold) {
		// create recommenders to be used
		List<Recommender> recommenderList = createRecommenders();

		// get neighbours of each recommender
		List<List<UserCheckin>> simUserList = getUsersList(recommenderList, user, con);

		// combine neihbours and return #numberOfSimilarUsers of them
		ArrayList<UserCheckin> neighbours = combineSimUsers(simUserList, user,con);
		
		return neighbours;
	}

	
	private List<List<UserCheckin>> getUsersList(
			List<Recommender> recommenderList, UserCheckin user, Connection con) {
		List<List<UserCheckin>> simUserList = new ArrayList<List<UserCheckin>>();
		
		// for each recommender find most similar users to the target user
		for(Recommender rec:recommenderList){
			List<UserCheckin> simUsers = rec.getMostSimilarUsers(user, con, 
					numberOfSimilarUsers, userSimilarityThreshold);
			simUserList.add(simUsers);
		}
		
		return simUserList;
	}

	private List<Recommender> createRecommenders() {
		List<Recommender> recommenderList = new ArrayList<Recommender>(); 
		// TODO surekli reccreater yaratmak dogru mu?
		RecommenderCreater recCreator = new RecommenderCreater();
		for(RecType recType:recTypeList){
			Recommender rec = recCreator.createRecommender(recType, numberOfSimilarUsers, 
					userSimilarityThreshold, userSimilarityThreshold, userSimilarityThreshold, 
					userSimilarityThreshold, printer, reader);
			recommenderList.add(rec);
		}

		return recommenderList;
	}

	public List<RecType> getRecTypeList() {
		return recTypeList;
	}

	public void setRecTypeList(List<RecType> recTypeList) {
		this.recTypeList = recTypeList;
	}



}
