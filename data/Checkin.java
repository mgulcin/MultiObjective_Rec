package data;

import java.util.ArrayList;


public class Checkin implements Subject{
	Double latitude;
	Double longtitude;
	String time;
	Integer locationId;
	
	// used when we consider checkintimes of users e.g in post process by time
	ArrayList<CheckinTime.TimeCategory> checkinTimeCatList;
	
	/**
	 * @param latitude
	 * @param longtitude
	 * @param time
	 * @param locationId
	 */
	public Checkin(Double latitude, Double longtitude, String time,
			Integer locationId) {
		super();
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.time = time;
		this.locationId = locationId;
		this.checkinTimeCatList = null;
	}
	
	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	/**
	 * @return the longtitude
	 */
	public Double getLongtitude() {
		return longtitude;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}
	
		
	public ArrayList<CheckinTime.TimeCategory> getCheckinTimeCatList() {
		return checkinTimeCatList;
	}

	public void setCheckinTimeCatList(
			ArrayList<CheckinTime.TimeCategory> checkinTimeCatList) {
		this.checkinTimeCatList = checkinTimeCatList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((locationId == null) ? 0 : locationId.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Checkin other = (Checkin) obj;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Checkin [latitude=" + latitude + ", longtitude=" + longtitude
				+ ", time=" + time + ", locationId=" + locationId
				+ ", checkinTimesList=" + checkinTimeCatList + "]";
	}
	
	
	
	
}
