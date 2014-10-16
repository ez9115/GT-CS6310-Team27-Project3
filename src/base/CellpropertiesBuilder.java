package base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class CellpropertiesBuilder {
	//Earth Circumference in m
	double C= 4.003014E7;


	//#define A 5.10072E14
   double PI = 3.1415927;
	//Stefan-Boltzmann constant
	double SB =5.67E-8;

	//thermal conductivity of the water, in W/(m*k) 
	double k =38.0;

	//Specific heat capacity for water in J/(kg * K )
	int CW =4187;

	//The depth of trhe water we need to heat in m
	double  depth = 0.33;

	//The density of the water in kg/m^3
	int  Ro = 1000;

	//Earth emissivity 0.612
	double eps = 0.8;

	//Solar Constant
	 double S = 1.366E3;

	static int Cols =24;
	static int  Rows = 12;

	//class CellpropertiesBuilder cell= null;
	//double T_sun = 278;
	File  diag;
	static int flag;
	int lat, lon; //coordinates of the left bottom corner of the cell in degrees
	int c_lat, c_lon; // coordinates of the center of the cell in degrees
	double T, T_inc, lat_attenuation, area;
	double h; //height of the cell;
	double l_v; //length of the vertical side of the cell
    double l_b;  //length of the base of the cell
    double l_t;  // length of the top of the cell
    Neighbor[] neighbor = new  Neighbor[4];

	class Neighbor
	{
	    
	        int i_lat, i_lon;
	        double p; //this is the ration of l/d, i.e. the common border to the distance between centers of the adjacent cells
	        CellpropertiesBuilder cell;
	};

	public   CellpropertiesBuilder()
	{
 
	/*	int lat, lon; //coordinates of the left bottom corner of the cell in degrees
		int c_lat, c_lon; // coordinates of the center of the cell in degrees
		double T, T_inc, lat_attenuation, area;
		double h; //height of the cell;
		double l_v; //length of the vertical side of the cell
	    double l_b;  //length of the base of the cell
	    double l_t;  // length of the top of the cell

		//Neighbor[] neighbor = new  Neighbor[4];*/

	   /* init(  i_lat,  i_lon,  cols,   rows,   gs ) ;
		 
		void Calculate_T_inc( double sunPosition, int tau );
		void T_Update();
		void setNeighbors( int num, Cell cell );*/
	}




void setNeighbors( int num , CellpropertiesBuilder cell )
{
 
    neighbor[ num ].cell = this;

	switch ( num )
	{
	case 0: neighbor[ num ].p = l_t / ( 0.5 * (h + this.h ) ); //North neighbor
			break;
	case 1:	neighbor[ num ].p = l_v / ( l_t + l_b ); //West neighbor
			break;
	case 2:	neighbor[ num ].p = l_b / (  0.5 * (h + this.h) ); //South neighbor
			break;
	case 3: neighbor[ num ].p = l_v / (l_t + l_b ) ; //East neighbor
			break;
	default: break;
	}
}


void init( int i_lat, int i_lon, int cols, int rows, int gs )
{
    T = 288.0;
    System.out.println( " AM I IN " );

    lat = ( i_lat - rows / 2 ) * gs;

    if ( i_lon < (cols / 2 ) )
        lon = - ( i_lon + 1 ) * gs;
    else
        lon = 360 - ( i_lon + 1 ) * gs;

	c_lat = lat + gs / 2;
	c_lon = lon + gs / 2;

    l_v = C * gs / 360.0; //length of the vertical side of the cell
    l_b = Math.cos( lat * PI / 180.0 ) * l_v;  //length of the base of the cell
    l_t = Math.cos( ( lat + gs ) * PI / 180.0 ) * l_v;  // length of the top of the cell
    h =   Math.sqrt( l_v * l_v - 0.25 * ( l_b - l_t ) * ( l_b - l_t ) ); //height of the cell
 
    area = 0.5 * ( l_t + l_b ) * h;

    lat_attenuation = Math.cos( c_lat * PI / 180.0 );

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
    double angle_diff = Math.abs(sunPosition - c_lon );
    if ( angle_diff < 180 )
        sun_angle = angle_diff;
    else
        sun_angle = 360 - angle_diff;

    double lon_attenuation;

    if ( sun_angle < 90 )
        lon_attenuation = Math.cos( sun_angle * PI / 180.0 );
    else
        lon_attenuation = 0.0;

	double T_neighbor = 0.0;
    for ( int i = 0; i < 4; i++ )
    {
        T_neighbor = T_neighbor + ( neighbor[ i ].cell.T - T ) * neighbor[ i ].p;
    }
	T_neighbor = T_neighbor * k; // / area;

    double T_radiation = S * lon_attenuation * lat_attenuation - eps * SB * T * T * T * T;
	T_radiation = T_radiation / depth;

	T_inc = ( T_radiation + T_neighbor ) * tau * 60 / ( CW * Ro ) ;


   // System.out.println(  "c_lat = %d   c_lon = %d   sunPosition = %f   sun_angle = %f   lon_attenuation = %f   T = %f    T_neighbor = %f    T_radiation = %f    T_inc = %f\n"+
            //              c_lat +":"+  c_lon +":" + sunPosition +":" + sun_angle+":" +lon_attenuation +":" + T +":" +T_neighbor +":"+ T_radiation+":" +T_inc  );
}

void updateTemperature()
{
    T = T + T_inc;
}


public static void main(String[] args) {
{
	int Cols =24;
	int  Rows = 12;

   // diag = fopen( "diag.txt", "w");
    int tau = 30;

    int gs = 180 / Rows ;

    double sunPosition_inc = -360.0 * (double)tau / 1440.0; //West is negative direction
    double sunPosition = 0;  // initial position
    int time = 0;
  //CellpropertiesBuilder// cell = new CellpropertiesBuilder();
  

  CellpropertiesBuilder[][]  Grid= new CellpropertiesBuilder[ Rows ][ Cols ];

   for ( int i = 0; i < Rows; i++ )
    {
        for ( int j = 0; j < Cols; j++ )
        {
        	Grid[ i ][ j ] = new CellpropertiesBuilder();
            Grid[ i ][ j ].init( i, j, Cols, Rows, gs);
        }
    }



   for ( int i = 0; i < Rows; i++ )
    {
        for ( int j = 0; j < Cols; j++ )
        {
            for ( int i_n = 0; i_n < 4; i_n ++ )
            {
                int i_lat = Grid[ i ][ j ].neighbor[ i_n ].i_lat;
                int i_lon = Grid[ i ][ j ].neighbor[ i_n ].i_lon;
                Grid[ i ][ j ].setNeighbors( i_n, ( Grid[ i_lat ][ i_lon ] ) );
            }
        }
    }



    //FILE * f_out = fopen ( "temp.txt", "w" );
    for ( int time_step = 0; time_step < 4800; time_step++ )
    {
        time = time + tau;

		double sunPosition_ave = sunPosition + sunPosition_inc / 2.0;
        
		sunPosition = sunPosition + sunPosition_inc;
		
        if ( sunPosition < -180.0  ) sunPosition = sunPosition + 360;  // keep  -180 < sunPosition < 180

        if ( time_step == 300 )
            flag = 1;
        else
            flag = 0;

        for ( int i = 0; i < Rows; i++ )
        {
            for ( int j = 0; j < Cols; j++ )
            {
                Grid[ i ][ j ].calculateTempIncrement( sunPosition_ave, tau );
            }
        }



        for ( int i = 0; i < Rows; i++ )
        {
            for ( int j = 0; j < Cols; j++ )
            {
                Grid[ i ][ j ].updateTemperature( );
            }
        }

        //ouput
        int sp = (int) sunPosition;
        GridData[][] gridDataArraylist = new GridData[Rows][Cols];
		double T_ave = 0.0;
		for ( int i = 0; i < Rows; i++ )
        {
			
            for ( int j = 0; j < Cols; j++ )
            {
            	GridData gridData = new GridData();
    			gridData.setLatitude(i); 
    			gridData.setTemp(Grid[ i ][ j ].T);
                T_ave += Grid[ i ][ j ].T;
                System.out.println("sunPositon" + sp + ",  i,j" +i + ":"+j +"=t="+ Grid[ i ][ j ].T);
                gridDataArraylist[i][j] = gridData;
            }
            
        }
		T_ave = T_ave /( Rows * Cols );
		System.out.println("T_eve" + T_ave);
		 
		SimulationResult result = new SimulationResult(gridDataArraylist);
		//return result
		
 }
}


       

}

    


public static void addToFile(String str, String tag ){
	 BufferedWriter writer = null;
	
       try {
           //create a temporary file
           String fileName ="../DB/propertiesList."+tag;
           File logFile = new File(fileName);

           // This will output the full path where the file will be written to...
           System.out.println(logFile.getCanonicalPath());

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