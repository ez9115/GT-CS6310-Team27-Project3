package simulation;

import java.awt.Color;

public class GridData {
	private int longitude;
	private double temp;
	private int latitude;
	private int sunPosition;
	private Color color;
	public int getLong(){
		return this.longitude;
	}
	public void setLatitude(int latitude){
		this.latitude=latitude;
	}
	public int getLatitude(){
		return this.latitude;
	}
	public void setLongitude(int longitude){
		this.longitude=longitude;
	}
	public double getTemp(){
		return this.temp;
	}
	public void setTemp(double t){
		this.temp= t;
	}
	public void setColor ( Color color){
		this.color = color;
	}
	public Color getColor(int temp){
		return this.color;
	}
	 
	public int getSunPosition() {
		return sunPosition;
	}
	public void setSunPosition(int sunPosition) {
		this.sunPosition = sunPosition;
	}
	public String toString(){
		   StringBuffer stringBuffer = new StringBuffer();
		   stringBuffer.append("latidute: ");
		   stringBuffer.append(this.latitude);
		   stringBuffer.append(",longitude: ");
		   stringBuffer.append(this.longitude);
		   stringBuffer.append(",temp:");
		   stringBuffer.append(this.temp);
		   stringBuffer.append(", color:");
		   stringBuffer.append(color.toString());
		   stringBuffer.append(this.sunPosition);
		   return stringBuffer.toString();
			}
}
