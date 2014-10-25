package simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class CellPropertiesBuilder {
	// Earth Circumference in m
	double C = 4.003014E7;

	// #define A 5.10072E14
	double PI = 3.1415927;
	// Stefan-Boltzmann constant
	double SB = 5.67E-8;

	// thermal conductivity of the water, in W/(m*k)
	double k = 0.58;

	// Specific heat capacity for water in J/(kg * K )
	int CW = 4187;

	// The depth of trhe water we need to heat in m
	double depth = 0.33;

	// The density of the water in kg/m^3
	int Ro = 1000;

	// Earth emissivity 0.612
	double eps = 0.8;

	// Solar Constant
	double S = 1.366E3;

	static int numberOfColumns = 24;
	static int Rows = 12;

	int tempPenetrationCoefficient = 20000;
	// class CellpropertiesBuilder cell= null;
	// double T_sun = 278;
	File diag;
	static int flag;
	int lat, lon; // coordinates of the left bottom corner of the cell in
					// degrees
	int centerLatitude, centerLongitude; // coordinates of the center of the cell in degrees
	double temp, temperatureInc, latAttunuation, area;
	double height; // height of the cell;
	double lenghtVertical; // length of the vertical side of the cell
	double lengthBottom; // length of the base of the cell
	double lengthTop; // length of the top of the cell
	Neighbor[] neighbor = new Neighbor[4];

	class Neighbor {

		int i_lat, i_lon;
		double p; // this is the ration of l/d, i.e. the common border to the
					// distance between centers of the adjacent cells
		CellPropertiesBuilder cell;
	};

	public CellPropertiesBuilder() {

		/*
		 * int lat, lon; //coordinates of the left bottom corner of the cell in
		 * degrees int centerLatitude, centerlongitude; // coordinates of the center of the cell in
		 * degrees double temp, temperatureInc, latAttenuation, area; double height; //height of
		 * the cell; double l_v; //length of the vertical side of the cell
		 * double lengthBottom; //length of the base of the cell double l_t; // length of
		 * the top of the cell
		 * 
		 * //Neighbor[] neighbor = new Neighbor[4];
		 */

		/*
		 * init( i_lat, i_lon, cols, rows, gs ) ;
		 * 
		 * void Calculate_T_inc( double sunPosition, int tau ); void T_Update();
		 * void setNeighbors( int num, Cell cell );
		 */
	}

	void setNeighbors(int num, CellPropertiesBuilder cell) {

		neighbor[num].cell = cell;
		// System.out.println( " init happened"+ neighbor[ num ].cell.h);

		switch (num) {
		case 0:
			neighbor[num].p = lengthTop / tempPenetrationCoefficient;// ( 0.5 * (height +
																// cell.height ) );
																// //North
																// neighbor
			break;
		case 1:
			neighbor[num].p = lenghtVertical / tempPenetrationCoefficient;// ( lengthTop + lenghtBottom);
																// //West
																// neighbor
			break;
		case 2:
			neighbor[num].p = lengthBottom / tempPenetrationCoefficient;// ( 0.5 * (height +
																// cell.height) );
																// //South
																// neighbor
			break;
		case 3:
			neighbor[num].p = lenghtVertical / tempPenetrationCoefficient;// (lengthTop + lengthBottom ) ;
																// //East
																// neighbor
			break;
		default:
			break;
		}
		//System.out.println("We are in neighbor[ num ].p="+neighbor[ num ].cell.temp + "     nun =" + num);
	}


void init( int i_lat, int i_lon, int cols, int rows, int gs, double startingTemp )
{
    //T = 288.0;
	temp = startingTemp;
   

    lat = ( i_lat - rows / 2 ) * gs;

    if ( i_lon < (cols / 2 ) )
        lon = - ( i_lon + 1 ) * gs;
    else
        lon = 360 - ( i_lon + 1 ) * gs;

	centerLatitude = lat + gs / 2;
	centerLongitude = lon + gs / 2;

    lenghtVertical = C * gs / 360.0; //length of the vertical side of the cell
    lengthBottom = Math.cos( lat * PI / 180.0 ) * lenghtVertical;  //length of the base of the cell
    lengthTop = Math.cos( ( lat + gs ) * PI / 180.0 ) * lenghtVertical;  // length of the top of the cell
    height =   Math.sqrt( lenghtVertical * lenghtVertical - 0.25 * ( lengthBottom - lengthTop ) * ( lengthBottom - lengthTop ) ); //height of the cell

    area = 0.5 * ( lengthTop + lengthBottom ) * height;

    latAttunuation = Math.cos( centerLatitude * PI / 180.0 );

    //Neighbors:
    neighbor[0] = new Neighbor();
    neighbor[1] = new Neighbor();
    neighbor[2] = new Neighbor();
    neighbor[3] = new Neighbor();
    //North neighbor
    if ( i_lat < ( rows - 1 ) )
    {

        neighbor[ 0 ].i_lat = i_lat + 1;
        neighbor[ 0 ].i_lon = i_lon;
    }
    else
    {
        neighbor[ 0 ].i_lat = i_lat;
        neighbor[ 0 ].i_lon = ( i_lon + ( cols / 2 )  ) % cols ;
    }

    //West neighbor

    neighbor[ 1 ].i_lat = i_lat;
    neighbor[ 1 ].i_lon = ( i_lon + 1 ) % cols;

    //South neighbor
    if ( i_lat > 0 )
    {
        neighbor[ 2 ].i_lat = i_lat - 1;
        neighbor[ 2 ].i_lon = i_lon;
    }
    else
    {
        neighbor[ 2 ].i_lat = i_lat;
        neighbor[ 2 ].i_lon = ( i_lon + ( cols / 2 )  ) % cols ;
    }

    //East neighbor

    neighbor[ 3 ].i_lat = i_lat;
    neighbor[ 3 ].i_lon = ( i_lon - 1 + cols ) % cols;
}



void calculateTempIncrement( double sunPosition, int tau)
{
    double sun_angle;
    double angle_diff = Math.abs(sunPosition - centerLongitude );
    if ( angle_diff < 180 )
        sun_angle = angle_diff;
    else
        sun_angle = 360 - angle_diff;

    double lonAttinutation;

    if ( sun_angle < 90 )
        lonAttinutation = Math.cos( sun_angle * PI / 180.0 );
    else
        lonAttinutation = 0.0;

	double tempNeighbor = 0.0;
    for ( int i = 0; i < 4; i++ )
    {
        tempNeighbor = tempNeighbor + ( neighbor[ i ].cell.temp - temp ) * neighbor[ i ].p;
    }
	tempNeighbor = tempNeighbor * k; // / area;

    double tempRadiation = S * lonAttinutation * latAttunuation - eps * SB * temp * temp * temp * temp;
	tempRadiation = tempRadiation / depth;

	temperatureInc = ( tempRadiation + tempNeighbor ) * tau * 60 / ( CW * Ro ) ;


	//System.out.println( "centerLatitude ="+ centerLatitude +":"+"centerLongitude =" +centerLongitude +":" + "sunPosition ="+ sunPosition +":"+" temp="+temp +":" +"T_neighbor ="+tempNeighbor +":" + "T_radiation = "+ tempRadiation+":" +"temperatureInc = "+temperatureInc + "   neighbor[ 2 ].cell.T =" +  ( neighbor[ 2 ].cell.temp ) );
}

	void updateTemperature() {
		temp = temp + temperatureInc;
	}

	public static void main(String[] args) {
		{
			int degreeSeparation = 15;
			int Cols = 24;//360 / degreeSeparation;
			int Rows = 12;//360 / degreeSeparation;

			// diag = fopen( "diag.txt", "w");
			int tau = 30;

			int gs = 180 / Rows;

			double sunPosition_inc = -360.0 * (double) tau / 1440.0; // West is
																		// negative
																		// direction
			double sunPosition = 0; // initial position
			int time = 0;
			// CellpropertiesBuilder// cell = new CellpropertiesBuilder();

			CellPropertiesBuilder[][] Grid = new CellPropertiesBuilder[Rows][Cols];

			for (int i = 0; i < Rows; i++) {
				for (int j = 0; j < Cols; j++) {
					Grid[i][j] = new CellPropertiesBuilder();
					Grid[i][j].init(i, j, Cols, Rows, gs, 288.0);
				}
			}

			for (int i = 0; i < Rows; i++) {
				for (int j = 0; j < Cols; j++) {
					for (int i_n = 0; i_n < 4; i_n++) {
						int i_lat = Grid[i][j].neighbor[i_n].i_lat;
						int i_lon = Grid[i][j].neighbor[i_n].i_lon;
						Grid[i][j].setNeighbors(i_n, (Grid[i_lat][i_lon]));
					}
				}
			}

			String fileName = "../TempMatrix";
			File logFile = new File(fileName);
			StringBuffer buff = new StringBuffer();
			for (int timeStep = 0; timeStep < 4800; timeStep++) {
				time = time + tau;

				double sunPosition_ave = sunPosition + sunPosition_inc / 2.0;

				sunPosition = sunPosition + sunPosition_inc;

				if (sunPosition < -180.0)
					sunPosition = sunPosition + 360; // keep -180 < sunPosition
														// < 180

				if (timeStep == 300)
					flag = 1;
				else
					flag = 0;

				for (int i = 0; i < Rows; i++) {
					for (int j = 0; j < Cols; j++) {
						Grid[i][j].calculateTempIncrement(sunPosition_ave, tau);
					}
				}

				for (int i = 0; i < Rows; i++) {
					for (int j = 0; j < Cols; j++) {
						Grid[i][j].updateTemperature();
					}
				}

				// ouput

				int sp = (int) sunPosition;
				ArrayList<GridData> gridDataArraylist = new ArrayList<GridData>();
				double tempAve = 0.0;
				for (int i = 0; i < Rows; i++) {

					for (int j = 0; j < Cols; j++) {
						GridData gridData = new GridData();
						gridData.setLatitude(i);
						gridData.setTemp(Grid[i][j].temp);
						tempAve += Grid[i][j].temp;
						// System.out.println("sunPositon" + sp + ",  i,j" +i +
						// ":"+j +"=t="+ Grid[ i ][ j ].T);
						gridDataArraylist.add(gridData);
					}

				}
				tempAve = tempAve / (Rows * Cols);

				// SimulationResult result = new
				// SimulationResult(gridDataArraylist, (float) sunPosition);

				buff.append("\n\n Time = " + time + "\t\t sunPosition = " + sp
						+ " tempAve = " + tempAve + "\n    \t\t\t");
				for (int it = 0; it < Cols; it++)

					buff.append(Grid[0][it].lon + "\t");

				buff.append("\n");

				for (int i = 0; i < Rows; i++) {

					buff.append("centerLatitude=" + Grid[i][0].centerLatitude + "\t\t");

					for (int j = 0; j < Cols; j++) {
						int tt = (int) Grid[i][j].temp;
						// System.out.println ( "TT"+tt);
						buff.append(tt + "\t");
					}
					buff.append("\n");

				}

			}
			addToFile(buff.toString(), logFile, fileName);
			System.out.println("Konez");
		}

	}

	public static void addToFile(String str, File logFile, String fileName) {
		BufferedWriter writer = null;

		try {
			// create a temporary file
			// String fileName ="../DB/propertiesList."+tag;
			// File logFile = new File(fileName);

			// This will output the full path where the file will be written
			// to...
			// System.out.println(logFile.getCanonicalPath());

			writer = new BufferedWriter(new FileWriter(logFile));

			writer.write(str);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}

	}
}
