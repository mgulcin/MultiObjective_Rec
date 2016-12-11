package data;

import java.util.Comparator;
import java.util.HashMap;

public class Similarity {
	// used in user class
	private User similarUser;//e.g. neighbor or friend
	private HashMap< Similarity.SimType, Double> similarityMap; // <simType,simVal> 

    // sn: common friends checkinList: only checkinList info fsn: friendship based
    public enum SimType { SOCIALNW, CHECKIN, FSN, HOMETOWN, TIME;}
    
    // TODO move to somewhere more appropriate 
    // NOTE: order is decided by the evaluation results based on methods using single criterion
    // TODO Time is added after usage of rankedListSimTypes, control!!
	public static SimType[] rankedListOfSimTypes =  {SimType.CHECKIN, SimType.HOMETOWN, 
													 SimType.SOCIALNW,  SimType.FSN, SimType.TIME};
	
	/**
	 * @param similarUser
	 * @param similarityVal
     * @param similarityType
	 */
	public Similarity(User similarUser, Double similarityVal, Similarity.SimType similarityType) {
		super();
		this.similarUser = similarUser;
		if(similarityMap == null)
		{
			similarityMap = new HashMap<Similarity.SimType, Double>();
		}
		
		similarityMap.put(similarityType, similarityVal);
	}

	public User getSimilarUser() {
		return similarUser;
	}

	public void setSimilarUser(User similarUser) {
		this.similarUser = similarUser;
	}

	public HashMap< Similarity.SimType, Double> getSimilarityMap() {
		return similarityMap;
	}

	public void setSimilarityMap(HashMap< Similarity.SimType, Double> similarityMap) {
		this.similarityMap = similarityMap;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Similarity [user=" + similarUser.getUserId() + ", similarityMap=" + similarityMap
				+ "]";
	}

	
	
	public static Comparator<Similarity> SimilarityComparatorCheckinBased = new Comparator<Similarity>() {

		public int compare(Similarity o1, Similarity o2) {
			
			return o2.getSimilarityMap().get(SimType.CHECKIN).compareTo(o1.getSimilarityMap().get(SimType.CHECKIN));
		}

	};

	public static Comparator<Similarity> SimilarityComparatorCheckinBasedIncreasing = new Comparator<Similarity>() {

		public int compare(Similarity o1, Similarity o2) {
			return -1* o2.getSimilarityMap().get(SimType.CHECKIN).compareTo(o1.getSimilarityMap().get(SimType.CHECKIN));
		}

	};
	
    public static Comparator<Similarity> SimilarityComparatorSocialNwBased = new Comparator<Similarity>() {

        public int compare(Similarity o1, Similarity o2) {
        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.SOCIALNW);
			Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.SOCIALNW);
			
			if(o2Sim == null || o1Sim == null){
				return 0;
			}
			
            return o2.getSimilarityMap().get(Similarity.SimType.SOCIALNW).
            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.SOCIALNW));
        }

    };
    
    public static Comparator<Similarity> SimilarityComparatorSocialNwBasedIncreasing = new Comparator<Similarity>() {

        public int compare(Similarity o1, Similarity o2) {
        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.SOCIALNW);
			Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.SOCIALNW);
			
			if(o2Sim == null || o1Sim == null){
				return 0;
			}
			
            return -1 * o2.getSimilarityMap().get(Similarity.SimType.SOCIALNW).
            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.SOCIALNW));
        }

    };

    public static Comparator<Similarity> SimilarityComparatorFSNBased = new Comparator<Similarity>() {

        public int compare(Similarity o1, Similarity o2) {
        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.FSN);
			Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.FSN);
			
			if(o2Sim == null || o1Sim == null){
				return 0;
			}
			
            return o2.getSimilarityMap().get(Similarity.SimType.FSN).
            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.FSN));
        }

    };
    
    public static Comparator<Similarity> SimilarityComparatorFSNBasedIncreasing = new Comparator<Similarity>() {

        public int compare(Similarity o1, Similarity o2) {
        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.FSN);
			Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.FSN);
			
			if(o2Sim == null || o1Sim == null){
				return 0;
			}
			
            return -1 * o2.getSimilarityMap().get(Similarity.SimType.FSN).
            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.FSN));
        }

    };
    
    public static Comparator<Similarity> SimilarityComparatorHometownBased = new Comparator<Similarity>() {

        public int compare(Similarity o1, Similarity o2) {
        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.HOMETOWN);
			Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.HOMETOWN);
			
			if(o2Sim == null || o1Sim == null){
				return 0;
			}
			
            return o2.getSimilarityMap().get(Similarity.SimType.HOMETOWN).
            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.HOMETOWN));
        }

    };
    
    public static Comparator<Similarity> SimilarityComparatorHometownBasedIncreasing = new Comparator<Similarity>() {

        public int compare(Similarity o1, Similarity o2) {
        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.HOMETOWN);
			Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.HOMETOWN);
			
			if(o2Sim == null || o1Sim == null){
				return 0;
			}
			
            return -1 * o2.getSimilarityMap().get(Similarity.SimType.HOMETOWN).
            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.HOMETOWN));
        }

    };

    public static Comparator<Similarity> SimilarityComparatorTimeBased = new Comparator<Similarity>() {

        public int compare(Similarity o1, Similarity o2) {
        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.TIME);
			Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.TIME);
			
			if(o2Sim == null || o1Sim == null){
				return 0;
			}
			
            return o2.getSimilarityMap().get(Similarity.SimType.TIME).
            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.TIME));
        }

    };
    
    public static Comparator<Similarity> SimilarityComparatorTimeBasedIncreasing = new Comparator<Similarity>() {

        public int compare(Similarity o1, Similarity o2) {
        	Double o2Sim = o2.getSimilarityMap().get(Similarity.SimType.TIME);
			Double o1Sim = o1.getSimilarityMap().get(Similarity.SimType.TIME);
			
			if(o2Sim == null || o1Sim == null){
				return 0;
			}
			
            return -1 * o2.getSimilarityMap().get(Similarity.SimType.TIME).
            		compareTo(o1.getSimilarityMap().get(Similarity.SimType.TIME));
        }

    };
    
	public static Comparator<Similarity> getComparatorType(Similarity.SimType type)
	{
		Comparator<Similarity> retType = null;
		switch(type)
		{
		case SOCIALNW: retType = Similarity.SimilarityComparatorSocialNwBased; break;
        case CHECKIN: retType = Similarity.SimilarityComparatorCheckinBased; break;
        case FSN: retType = Similarity.SimilarityComparatorFSNBased; break;
        case HOMETOWN: retType = Similarity.SimilarityComparatorHometownBased; break;
        case TIME: retType = Similarity.SimilarityComparatorTimeBased; break;
		default: System.out.println("Wrong type of sim"); System.exit(-1); break;
		}		
		return retType;
	}
    
}
