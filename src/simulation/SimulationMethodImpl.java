package simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import base.SimulationMethod;
import base.SimulationResult;

public class SimulationMethodImpl implements SimulationMethod {

	// TODO: Maybe change this to public SimulationReseult
	// simulate(SimulationResult previousResults, int degreeSeparation, int
	// time)
	public SimulationResult simulate(SimulationResult previousResults,
			int degreeSeparation, float sunPosition)
			throws InterruptedException {

		// Validate arguments
		if (previousResults == null) {
			throw new IllegalArgumentException(
					"Previous results cannot be null");
		}

		if (sunPosition < -180 || sunPosition > 180) {
			throw new IllegalArgumentException(
					"Sun position must be an integer from -180 to 180");
		}

		if (degreeSeparation < 0 || degreeSeparation > 180) {
			throw new IllegalArgumentException(
					"Degrees of separation must be an integer from 0 to 180");
		}

		int Cols = 360 / degreeSeparation;
		int Rows = 180 / degreeSeparation;

		int tau = 30;

		int gs = 180 / Rows;

		CellpropertiesBuilder[][] Grid = new CellpropertiesBuilder[Rows][Cols];

		String fileName = "../TempMatrix.";
		File logFile = new File(fileName);
		StringBuffer buff = new StringBuffer();

		for (int i = 0; i < Rows; i++) {
			for (int j = 0; j < Cols; j++) {
				Grid[i][j] = new CellpropertiesBuilder();
				Grid[i][j].init(i, j, Cols, Rows, gs,
						previousResults.getTemperature(i, j));
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

		for (int i = 0; i < Rows; i++) {
			for (int j = 0; j < Cols; j++) {
				Grid[i][j].calculateTempIncrement(sunPosition, tau);
			}
		}

		for (int i = 0; i < Rows; i++) {
			for (int j = 0; j < Cols; j++) {
				Grid[i][j].updateTemperature();
			}
		}

		// ouput
		int sp = (int) sunPosition;
		GridData[][] gridDataArraylist = new GridData[Rows][Cols];
		double T_ave = 0.0;
		for (int i = 0; i < Rows; i++) {
			for (int j = 0; j < Cols; j++) {
				GridData gridData = new GridData();
				gridData.setLatitude(i);
				gridData.setTemp(Grid[i][j].T);
				T_ave += Grid[i][j].T;
				// System.out.println("sunPositon" + sp + ",  i,j" +i + ":"+j
				// +"=t="+ Grid[ i ][ j ].T);
				gridDataArraylist[i][j] = gridData;
			}

		}

		buff.append("\n\n sunPosition = " + sp + " T_ave = " + T_ave
				+ "\n    \t\t\t");
		for (int it = 0; it < Cols; it++) {
			buff.append(Grid[0][it].lon + "\t");
		}

		buff.append("\n");

		for (int i = 0; i < Rows; i++) {
			buff.append("c_lat=" + Grid[i][0].c_lat + "\t\t");

			for (int j = 0; j < Cols; j++) {
				int tt = (int) Grid[i][j].T;
				buff.append(tt + "\t");
			}
			buff.append("\n");
		}
		addToFile(buff.toString(), logFile, fileName);

		T_ave = T_ave / (Rows * Cols);
		// System.out.println("T_eve" + T_ave);

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

	private static void addToFile(String str, File logFile, String fileName) {
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