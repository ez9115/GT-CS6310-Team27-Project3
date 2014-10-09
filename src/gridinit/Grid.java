package gridinit;

public class Grid {
	private static final double circumferenceInMeters = 4.003014e7;
	private static final double radiusInMeters = 6.371e6;
	private static final double surfaceAreaInSqMeters = 5.10072e14;
	private static final int defaultTemp = 288;
	
	private int rows;
	private int cols;
	private GridElement[][] earth;
	private GridElement[][] newEarth;
	
	public Grid(int degrees) {
		int rows = 180/degrees;
		int cols = 360/degrees;
		double proportionOfEquator = degrees/360.0;
		double vl = 0;
		double bl = 0;
		double tl = 0;
		double h = 0;
		double pm = 0;
		double a = 0;
		double ra = 0;
		int latitude;
		int longitude;
		earth = new GridElement[rows][cols];
				
		for (int row = 0; row < rows; row = row + 1) {
			latitude = (row - (rows/2)) * degrees;
			System.out.println("lat " + latitude);
			for (int col = 0; col < cols; col = col + 1) {
				System.out.println(row + " " + col);
				longitude = (col - (cols/2)) * degrees;
				System.out.println("lon " + longitude);
				vl = circumferenceInMeters * proportionOfEquator;
				vl = Math.round(vl*100);
				vl = vl/100;
				bl = 2 * Math.PI * radiusInMeters * Math.cos(latitude * (Math.PI/180)) * proportionOfEquator;
				bl = Math.round(bl*100);
				bl = bl/100;
				tl = radiusInMeters * Math.cos((latitude + degrees) * (Math.PI/180)) * 2 * Math.PI * proportionOfEquator;
				tl = Math.round(tl*100);
				tl = tl/100;
				h = Math.sqrt(Math.pow(vl,2) - 1.0/4 * Math.pow(bl-tl,2));
				h = Math.round(h*100);
				h = h/100;
				pm = tl + bl + 2 * vl;
				pm = Math.round(pm*100);
				pm = pm/100;
				a = (1.0/2) * h * (tl + bl);
				a = Math.round(a*100);
				a = a/100;
				ra = a/surfaceAreaInSqMeters;
				earth[row][col] = new GridElement(vl, bl, tl, h, pm, a, ra);
			}
		}
		for (int row = 0; row < rows; row = row + 1) {
			for (int col = 0; col < cols; col = col + 1) {
				//System.out.println(row + " " + col);
				System.out.print(earth[row][col].getTemperature()+" ");
				System.out.print(earth[row][col].getSidelengthinmeters()+" ");
				System.out.print(earth[row][col].getBottomlengthinmeters()+" ");
				System.out.print(earth[row][col].getToplengthinmeters()+" ");
				System.out.print(earth[row][col].getHeightinmeters()+" ");
				System.out.print(earth[row][col].getPerimeterinmeters()+" ");
				System.out.print(earth[row][col].getAreainsquaremeters()+" ");
				System.out.print(earth[row][col].getPropofearthsainsquaremeters()+" ");
			}
			System.out.println();
		}
	}
}
