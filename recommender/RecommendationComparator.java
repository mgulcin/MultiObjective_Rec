package recommender;

import java.util.Comparator;

import data.Recommendation;

public class RecommendationComparator implements Comparator<Recommendation> {

	@Override
	public int compare(Recommendation r1, Recommendation r2) {
		if(r1.getScore() > r2.getScore()){
			return -1;
		} else if(r1.getScore() < r2.getScore()){
			return 1;
		}
        return 0;
	}

}
