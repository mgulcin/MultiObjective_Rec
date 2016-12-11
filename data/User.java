package data;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class User {
	protected Integer userId;
	protected ArrayList<Integer> friends;
	protected ArrayList<Rating> ratings;
	
	protected HashMap<Integer,Similarity> similarities;//<friendId,similarity>
	protected Double avgRating;

	
	// methods
	
	public User(Integer userId) {
		super();
		this.userId = userId;
		this.friends = null;
		this.similarities = new HashMap<Integer, Similarity>();//<friendId,similarity>
		this.avgRating = 0.0;
		this.ratings = null;
	}

	/**
	 * @return the friends
	 */
	public ArrayList<Integer> getFriends() {
		return friends;
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	/**
	 * @param friends the friends to set
	 */
	public void setFriends(ArrayList<Integer> friends) {
		this.friends = friends;
	}

	/**
	 * @param ratings the ratings to set
	 */
	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
		
		Double avg = calculateAvgRatingBased(ratings); 
		this.setAvgRating(avg);
		
		
	}

	protected Double calculateAvgRatingBased(ArrayList<Rating> values) {
		Double avg = 0.0;
		
		if(values.size() == 0)
		{
			return avg;
		}
		
		for(Rating r:values)
		{
			Double val = r.getRatingScore();
			avg += val;
		}
		avg = avg/values.size();
		
		return avg;
	}
	
	protected Double calculateAvg(ArrayList<Double> values) {
		Double avg = 0.0;
		
		if(values.size() == 0)
		{
			return avg;
		}
		
		for(Double val:values)
		{
			avg += val;
		}
		avg = avg/values.size();
		
		return avg;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the similarities
	 */
	public HashMap<Integer,Similarity> getSimilarities() {
		return similarities;
	}

	/**
	 * @param similarities the similarities to set
	 */
	public void setSimilarities(HashMap<Integer,Similarity> similarities) {
		this.similarities = similarities;
	}


	public Double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(Double avgRating) {
		this.avgRating = avgRating;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId 
				+ ", friends=" + friends
				+ ", similarities=" + similarities 
				+ ", avgRating=" + avgRating + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	
	
}
