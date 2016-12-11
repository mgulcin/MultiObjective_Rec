package data;

import java.util.ArrayList;

public class UserCheckin extends User{

	protected ArrayList<Checkin> checkins;
	protected Integer hometownId;
	
	
	public UserCheckin(Integer userId) {
		super(userId);
		checkins = null;
		hometownId = null;
	}

    public UserCheckin(Integer userId, Integer loc) {
        super(userId);
        checkins = null;
        hometownId = loc;
    }


	/**
	 * @return the checkins
	 */
	public ArrayList<Checkin> getCheckins() {
		return checkins;
	}

	/**
	 * @param checkins the checkins to set
	 */
	public void setCheckins(ArrayList<Checkin> checkins) {
		this.checkins = checkins;
	}

    public Integer getHometownId() {
        return hometownId;
    }

    public void setHometown(Integer hometownId) {
        this.hometownId = hometownId;
    }

	
	public Rating findRatingByCheckin(Checkin checkin, Integer recommenderType) {
		Rating retRating = null;
        for(Rating r:ratings)
        {
            Checkin rItem = (Checkin) r.getSubject();
            if(rItem.getLocationId().equals(checkin.getLocationId()))
            {
                retRating = r;
                break;
            }
        }

        return retRating;
    }
	
	
}
