package gridinit;

public class GridElement {
	double temperature;
	double toplengthinmeters;
	double bottomlengthinmeters;
	double sidelengthinmeters;
	double areainmeters;
	double perimeterinmeters;
	double areainsquaremeters;
	
	public double getAreainsquaremeters() {
		return areainsquaremeters;
	}

	public void setAreainsquaremeters(double areainsquaremeters) {
		this.areainsquaremeters = areainsquaremeters;
	}

	public double getPerimeterinmeters() {
		return perimeterinmeters;
	}

	public void setPerimeterinmeters(double perimeterinmeters) {
		this.perimeterinmeters = perimeterinmeters;
	}

	public double getAreainmeters() {
		return areainmeters;
	}

	public void setAreainmeters(double areainmeters) {
		this.areainmeters = areainmeters;
	}

	public double getHeightinmeters() {
		return heightinmeters;
	}

	public void setHeightinmeters(double heightinmeters) {
		this.heightinmeters = heightinmeters;
	}

	double heightinmeters;
	
	public GridElement(double vl, double bl, double tl, double h, double pm, double a) {
		sidelengthinmeters = vl;
		bottomlengthinmeters = bl;
		toplengthinmeters = tl;
		heightinmeters = h;
		perimeterinmeters = pm;
		areainsquaremeters = a;
 		temperature = 288;
	}

	public double getToplengthinmeters() {
		return toplengthinmeters;
	}

	public void setToplengthinmeters(double toplength) {
		this.toplengthinmeters = toplength;
	}

	public double getBottomlengthinmeters() {
		return bottomlengthinmeters;
	}

	public void setBottomlengthinmeters(double bottomlengthinmeters) {
		this.bottomlengthinmeters = bottomlengthinmeters;
	}

	public double getSidelengthinmeters() {
		return sidelengthinmeters;
	}

	public void setSidelengthinmeters(double sidelengthinmeters) {
		this.sidelengthinmeters = sidelengthinmeters;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
}
