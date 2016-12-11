package data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Collects the information on checkin time
 * Besides clusters time such as 
 * weekday,weekend & morning, afternoon, night, afternight
 * & their combinations
 * Based on Liu et al. (2013, Personalized ..., 1st author from Epfl)
 * @author mg
 *
 */
public class CheckinTime {
	
	/**
	 * Time category enum
	 * WE: WeekEnd WD: WeekDay
	 * Morning: 6 am - 12 am (06.00-12.00)
	 * Afternoon: 12 am - 6 pm (12.00-18.00)
	 * Evening: 6 pm - 12 pm (18.00-00.00)
	 * Night: 12 pm - 6 am (00.00-06.00)
	 * @author mg
	 *
	 */
	public enum TimeCategory{WE_MORNING, WE_AFTERNOON, WE_EVENING, WE_NIGHT,
		WD_MORNING, WD_AFTERNOON, WD_EVENING, WD_NIGHT,}
	
	private enum HourCategory{MORNING, AFTERNOON, EVENING, NIGHT}
	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.S";
	
	/**
	 * Maps elements of checkinTimeList to timeCategories
	 * Also find the frequency of related timeCategory 
	 * (vals in Double, because of other methods using the output :))
	 * @param checkinTimeList
	 * @return
	 */
	public static HashMap<TimeCategory, Double> mapToTimeCategory(
			List<String> checkinTimeList) {
		HashMap<TimeCategory, Double> timeCategoryMap = 
				new HashMap<CheckinTime.TimeCategory, Double>();
		
		for(String checkinTime:checkinTimeList){
			TimeCategory cat = mapToTimeCategory(checkinTime);
			if(timeCategoryMap.containsKey(cat)){
				// already in map, increase the frequency
				Double newVal = timeCategoryMap.get(cat) + 1.0;
				timeCategoryMap.put(cat, newVal);
			} else {
				// a new time category
				timeCategoryMap.put(cat, 1.0);
			}
		}
		
		// normalize
		int size = checkinTimeList.size();
		for(Entry<TimeCategory, Double> e: timeCategoryMap.entrySet()){
			TimeCategory key = e.getKey();
			Double oldVal = e.getValue();
			Double newVal = oldVal/size;
			timeCategoryMap.put(key, newVal);
		}
		
		// return
		return timeCategoryMap;
	}

	/**
	 * Map given date string to our TimeCategory enum
	 * @param checkinTime
	 * @return
	 */
	public static TimeCategory mapToTimeCategory(String checkinTime) {
		TimeCategory category = null;
		
		/* 
		 * parsing date strings
		 * LocalDate fromIsoDate = LocalDate.parse("2014-01-20");
		 * LocalDate fromIsoWeekDate = LocalDate.parse("2014-W14-2", DateTimeFormatter.ISO_WEEK_DATE);
		 * LocalDate fromCustomPattern = LocalDate.parse("20.01.2014", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		 * 
		 * format ISO date time (2014-02-20T20:04:05.867)--DateTimeFormatter.ISO_DATE_TIME
		 */
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
		LocalDateTime checkinDate = LocalDateTime.parse(checkinTime, formatter);
		
		
		boolean weekEnd = isWeekEnd(checkinDate);
		HourCategory hCategory = findHourCategory(checkinDate);
		
		switch(hCategory){
		case MORNING:
			if(weekEnd){category = TimeCategory.WE_MORNING;}
			else {category = TimeCategory.WD_MORNING;}
			break;
		case AFTERNOON: 
			if(weekEnd){category = TimeCategory.WE_AFTERNOON;}
			else {category = TimeCategory.WD_AFTERNOON;}
			break;
		case EVENING: 
			if(weekEnd){category = TimeCategory.WE_EVENING;}
			else {category = TimeCategory.WD_EVENING;}
			break;
		case NIGHT: 
			if(weekEnd){category = TimeCategory.WE_NIGHT;}
			else {category = TimeCategory.WD_NIGHT;}
		break;
		default: break;
		}
		
		formatter = null;
		hCategory = null;
		checkinDate = null;
				
		return category;
	}

	/**
	 * Map to HourCategory enum
	 * Morning: 6 am - 12 am (06.00-11.59)
	 * Afternoon: 12 am - 6 pm (12.00-17.59)
	 * Evening: 6 pm - 12 pm (18.00-23.59)
	 * Night: 12 pm - 6 am (00.00-05.59)
	 * @param checkinDate
	 * @return
	 * 
	 * TODO unitTestle filan kontrol et bunu
	 */
	private static HourCategory findHourCategory(LocalDateTime checkinDate) {
		HourCategory hCat = null;
		
		int hour = checkinDate.getHour();
		//int min = checkinDate.getMinute();
		
		if(hour >= 0 && hour<6){
			hCat = HourCategory.NIGHT;
		} else if(hour >= 6 && hour<12){
			hCat = HourCategory.MORNING;
		}else if(hour >= 12 && hour<18){
			hCat = HourCategory.AFTERNOON;
		}else if(hour >= 18 && hour<=23){
			hCat = HourCategory.EVENING;
		}
		
		return hCat;
	}

	/**
	 * Is Saturday or Sunday?
	 * @param checkinDate
	 * @return
	 */
	private static boolean isWeekEnd(LocalDateTime checkinDate) {
		boolean weekend = false;
		
		if(checkinDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)||
				checkinDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
			weekend = true;
		}
		
		return weekend;
	}

	

}
