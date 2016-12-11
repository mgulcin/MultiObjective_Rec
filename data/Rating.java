package data;


public class Rating {
	Integer userId;
	Subject subject; 
	Double ratingScore;
	
	/**
	 * @param userId
	 * @param locationId
	 * @param activityId
	 */
	public Rating(Integer userId, Subject subject, Double rating) {
		super();
		this.userId = userId;
		this.subject = subject;
		this.ratingScore = rating;
	}
	
	
	public Rating(Rating r) {
		this.userId = r.getUserId();
		this.subject = r.getSubject();
		this.ratingScore = r.getRating();
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * @return the rating
	 */
	public Double getRating() {
		return ratingScore;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Double rating) {
		this.ratingScore = rating;
	}
	

	/**
	 * @return the subject
	 */
	public Subject getSubject() {
		return subject;
	}


	/**
	 * @return the ratingScore
	 */
	public Double getRatingScore() {
		return ratingScore;
	}


	/**
	 * @param subject the subject to set
	 */
	public void setSubject(Subject subject) {
		this.subject = subject;
	}


	/**
	 * @param ratingScore the ratingScore to set
	 */
	public void setRatingScore(Double ratingScore) {
		this.ratingScore = ratingScore;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rating [userId=" + userId + ", subject=" + subject
				+ ", ratingScore=" + ratingScore + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		Rating other = (Rating) obj;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} 
		return true;
	}





	
	
}
