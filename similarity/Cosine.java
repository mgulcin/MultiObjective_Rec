package similarity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cosine {

	/**
	 * Converts collection to map 
	 * by counting number of element than mapping key-->count, 
	 * then calculates cosine sim 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> double cosine(Collection<T> a, Collection<T> b) {
		if(a == null || b == null){return 0.0;}

		// calculate nom
		Map<T,Double> aa = asBag(a);
		Map<T,Double> bb = asBag(b);
		
		return cosine(aa,bb);
	} 
	
	public static <T> double cosine(Map<T,Double> aMap, 
			Map<T,Double> bMap){
		// calculate numeretor
		double numerator = 0;

		for (T subject: aMap.keySet()) 
		{
			if (!bMap.containsKey(subject)) 
			{
				//System.out.println("Different elements in vector @ cosineRatingBased");
				continue;
			}
			numerator += aMap.get(subject) * bMap.get(subject);		
		}

		// calculate denominator
		double denominator = calculateDenom(aMap,bMap);

		// calculate result
		double retVal = 0; 
		if(denominator != 0)
		{
			retVal = numerator / denominator;
		}

		if(retVal < 0) 
		{
			System.out.println("Negative cosine!!!");
			retVal = 0.0;
		}
		return retVal;
	}

	
	private static <T> double calculateDenom(Map<T, Double> aa,
			Map<T, Double> bb) {
		double K = 0.001;
		double normAA= norm(aa);
		double normBB = norm(bb);
		double denominator = (normAA * normBB)+K;

		return denominator;
	}


	private static <T> double norm(Map<T, Double> map) {
		double sum = 0;
		for (double each: map.values()) sum += each * each;
		return Math.sqrt(sum);
	}

	private static <T> Map<T,Double> asBag(Collection<T> input) {
		Map<T,Double> bag = new HashMap<T,Double>();
		for (T element: input) {
			if (!bag.containsKey(element)) bag.put(element,0.0);
			bag.put(element, bag.get(element) + 1.0);
		}
		return bag;
	}

}