package simulation;

import base.SimulationMethod;
import base.SimulationResult;

public class SimulationMethodImpl implements SimulationMethod {
	
	// TODO: Maybe change this to public SimulationReseult simulate(SimulationResult previousResults, int degreeSeparation, int time)
	public SimulationResult simulate() throws InterruptedException {
		int degreeSeparation = 15; // 1 - 180
		int time = 15; // 0 - 1440 minutes
		
		int Cols = 360 / degreeSeparation;
		int Rows = 360 / degreeSeparation;
		
		int tau = 30;
		
		int gs = 180 / Rows ;
		
		double sunPosition_inc = -360.0 * (double)tau / 1440.0; //West is negative direction
		double sunPosition = 0;  // initial position
		//int time = 0;
		
		CellpropertiesBuilder[][]  Grid= new CellpropertiesBuilder[ Rows ][ Cols ];
		
		for ( int i = 0; i < Rows; i++ ) {
		    for ( int j = 0; j < Cols; j++ ) {
		    	Grid[ i ][ j ] = new CellpropertiesBuilder();
		        Grid[ i ][ j ].init( i, j, Cols, Rows, gs);
		    }
		}
		
		for ( int i = 0; i < Rows; i++ ) {
			for ( int j = 0; j < Cols; j++ ) {
				for ( int i_n = 0; i_n < 4; i_n ++ ) {
					int i_lat = Grid[ i ][ j ].neighbor[ i_n ].i_lat;
					int i_lon = Grid[ i ][ j ].neighbor[ i_n ].i_lon;
					Grid[ i ][ j ].setNeighbors( i_n, ( Grid[ i_lat ][ i_lon ] ) );
				}
			}
		}
		
	    for ( int time_step = 0; time_step < 4800; time_step++ ) {
	    	time = time + tau;
	    	
			double sunPosition_ave = sunPosition + sunPosition_inc / 2.0;
			
			sunPosition = sunPosition + sunPosition_inc;

	        if ( sunPosition < -180.0  ) sunPosition = sunPosition + 360;  // keep  -180 < sunPosition < 180

	        for ( int i = 0; i < Rows; i++ ) {
	            for ( int j = 0; j < Cols; j++ ) {
	                Grid[ i ][ j ].calculateTempIncrement( sunPosition_ave, tau );
	            }
	        }
	        
	        for ( int i = 0; i < Rows; i++ ) {
	            for ( int j = 0; j < Cols; j++ ) {
	                Grid[ i ][ j ].updateTemperature( );
	            }
	        }
	    }

        //ouput
        int sp = (int) sunPosition;
        GridData[][] gridDataArraylist = new GridData[Rows][Cols];
		double T_ave = 0.0;
		for ( int i = 0; i < Rows; i++ ) {
            for ( int j = 0; j < Cols; j++ ) {
            	GridData gridData = new GridData();
    			gridData.setLatitude(i);
    			gridData.setTemp(Grid[ i ][ j ].T);
                T_ave += Grid[ i ][ j ].T;
                //System.out.println("sunPositon" + sp + ",  i,j" +i + ":"+j +"=t="+ Grid[ i ][ j ].T);
                gridDataArraylist[i][j] = gridData;
            }

        }
		
		T_ave = T_ave /( Rows * Cols );
		//System.out.println("T_eve" + T_ave);

		return new SimulationResult(gridDataArraylist, (float) sunPosition);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
