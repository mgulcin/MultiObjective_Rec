package similarity;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import data.Rating;
import data.Subject;

public class CosineRating {

	public static double cosine(ArrayList<Rating> a, Double aMean, 
			ArrayList<Rating> b, Double bMean) {

		// get std deviation of rating values st. a[i].score = a[i].score-aMean
		Collection<Rating> aStd = updateValues(a,aMean);
		Collection<Rating> bStd = updateValues(b,bMean);

		// create maps 
		Map<Subject,Double> aMap = new HashMap<Subject,Double>();
		Map<Subject,Double> bMap = new HashMap<Subject,Double>();
		createMaps(aStd,bStd,aMap, bMap);

		// calculate cosine sim
		return Cosine.cosine(aMap, bMap);
	}

	public static Collection<Rating> updateValues(Collection<Rating> list,
			Double listMean) {
		Collection<Rating> retCollection = new ArrayList<Rating>();
		if(listMean.equals(0)){
			retCollection.addAll(list);
		} else{
			for(Rating r: list)
			{
				Double rScore = r.getRating();
				Rating newR = new Rating(r);
				Double newScore  = rScore-listMean;
				if(newScore < 0.0) {
					newScore = 0.0;
				}
				newR.setRating(newScore);

				retCollection.add(newR);
			}
		}
		return retCollection;
	}


	public static void createMaps(Collection<Rating> aList, Collection<Rating> bList, 
			Map<Subject, Double> aMap, Map<Subject, Double> bMap) {

		// ratings from aList & bList -- note passing over one list is enough to find common items
		for(Rating r: aList)
		{
			//put values of aList
			Double aScore = r.getRating();
			aMap.put(r.getSubject(), aScore);
		}
		
		for(Rating r: bList)
		{
			//put values of bList
			Double bScore = r.getRating();
			bMap.put(r.getSubject(), bScore);
		}

	}

	private static Double getRatingScore(Collection<Rating> bList, Rating rating) {
		Double retVal = -1.0;

		for(Rating r:bList)
		{
			if(r.equals(rating))
			{
				retVal = r.getRating();
				break;
			}
		}
		return retVal;
	}

	private static boolean contains(Collection<Rating> bList, Rating rating) {
		boolean retVal = false;

		for(Rating r:bList)
		{
			if(r.equals(rating))
			{
				retVal = true;
				break;
			}
		}
		return retVal;
	}

}