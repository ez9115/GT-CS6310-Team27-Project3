package simulation;

import java.util.ArrayList;

public class TemperatureCalcUtil {

	 
		
		private static final int power = 174;//pettawatts

		public static double getTempeartureByLongitude(int longitude) {
		    double temp = power * Math.cos(Math.toRadians(90d - longitude));
		    return temp;
		  }
		public static double getTempeartureByLongitudeRev(int longitude) {
		    double temp = power *1/ Math.sin(Math.toRadians(90d - longitude));
		    return temp;
		  }
		public static ArrayList<TempData> getTemperatureByLongitudeForGrid(int longitude){
			
			//moving from 0 to North until required longitude
			ArrayList<TempData> tempDataArrayList = new ArrayList<TempData>();
			for (int i=0 ;i<=longitude;i++){
				TempData tempData = new TempData();
				int temp = (int) (power * Math.sin(Math.toRadians(90d - longitude)));
				tempData.setLatitude(0);// assume it is mid of euator
				tempData.setLongitude(i);
				tempData.setTemp(temp);
				tempDataArrayList.add(tempData);
				
			}
			return tempDataArrayList;
			
		}
	}

