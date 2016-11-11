import java.util.ArrayList;

import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

public class CountStepsBlank {

	/***
	 * Counts the number of steps based on sensor data.
	 * 
	 * @param times
	 *            a 1d-array with the elapsed times in miliseconds for each row
	 *            in the sensorData array.
	 * 
	 * @param sensorData
	 *            a 2d-array where rows represent successive sensor data
	 *            samples, and the columns represent different sensors. We
	 *            assume there are 6 columns. Columns 0 - 2 are data from the x,
	 *            y, and z axes of an accelerometer, and 3-5 are data from the
	 *            x, y, and z axes of a gyro.
	 * 
	 * @return an int representing the number of steps
	 */
	public static int countSteps(double[] times, double[][] accelData) {
		double[] accelMags = calculateMagnitudesFor(accelData);

		double accelMagMean = calculateMean(accelMags);

		double accelStanDev = calculateStandardDeviation(accelMags, accelMagMean);

		int stepCounter = 0;
		for (int i = 1; i < accelMags.length - 1; i++) {
			if (accelMags[i] > accelMags[i - 1] && accelMags[i] > accelMags[i + 1]) {
				if (accelMags[i] > accelMagMean + accelStanDev)
					stepCounter++;
			}
		}
		return stepCounter;
	}

	/***
	 * Calculate the magnitude for a vector with x, y, and z components.
	 * 
	 * @param x
	 *            the x component
	 * @param y
	 *            the y component
	 * @param z
	 *            the z component
	 * @return the magnitude of the vector
	 */
	public static double calculateMagnitude(double x, double y, double z) {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/***
	 * Takes a 2d array with 3 columns representing the 3 axes of a sensor.
	 * Calculates the magnitude of the vector represented by each row. Returns a
	 * new array with the same number of rows where each element contains this
	 * magnitude.
	 * 
	 * @param sensorData
	 *            an array with n rows and 3 columns. Each row represents a
	 *            different measurement, and each column represents a different
	 *            axis of the sensor.
	 * 
	 * @return an array with n rows and each element is the magnitude of the
	 *         vector for the corresponding row in the sensorData array
	 */
	public static double[] calculateMagnitudesFor(double[][] sensorData) {
		double[] mags = new double[sensorData.length];

		for (int r = 0; r < mags.length; r++) {
			mags[r] = calculateMagnitude(sensorData[r][0], sensorData[r][1], sensorData[r][2]);
		}

		return mags;
	}

	/***
	 * Return the standard deviation of the data.
	 * 
	 * @param arr
	 *            the array of the data
	 * @param mean
	 *            the mean of the data (must be pre-calculated).
	 * @return the standard deviation of the data.
	 */
	static double calculateStandardDeviation(double[] arr, double mean) {
		double totalSum = 0;
		for (int i = 0; i < arr.length; i++) {
			double x = (arr[i] - mean) * (arr[i] - mean);
			totalSum += x;
		}
		totalSum /= arr.length - 1;
		totalSum = Math.sqrt(totalSum);
		return totalSum;
	}

	/***
	 * Return the mean of the data in the array
	 * 
	 * @param arr
	 *            the array of values
	 * @return the mean of the data
	 */
	static double calculateMean(double[] arr) {
		double totalSum = 0;
		for (int i = 0; i < arr.length; i++) {
			totalSum += arr[i];
		}
		totalSum /= arr.length;
		return totalSum;
	}

	/***
	 * Counts the number of steps based on sensor data.
	 * 
	 * @param times
	 *            a 1d-array with the elapsed times in miliseconds for each row
	 *            in the sensorData array.
	 * @param accelData
	 *            a 2d-array where rows represent successive sensor data
	 *            samples, and the columns represent x-, y-, and z- accelerations.
	 * @return an int representing the number of steps
	 */
	public static int countStepsImproved1(double[] times, double[][] accelData) {
		double[] accelMags = calculateMagnitudesFor(accelData);

		double accelMagMean = calculateMean(accelMags);

		double accelStanDev = calculateStandardDeviation(accelMags, accelMagMean);

		double coefficient = 0.8;

		int stepCounter = 0;
		boolean belowLowerThresh = false;
		double currentHighestPeak = 0;
		for (int i = 1; i < accelMags.length - 1; i++) {
			if (accelMags[i] > accelMagMean + accelStanDev) {
				belowLowerThresh = false;
				if (accelMags[i] > currentHighestPeak)
					currentHighestPeak = accelMags[i];
			}
			if (accelMags[i] < (coefficient * currentHighestPeak)) {
				belowLowerThresh = true;
			}
			if (belowLowerThresh) {
				stepCounter++;
				currentHighestPeak = 0;
				belowLowerThresh = false;
			}

		}
		return stepCounter;
	}

	/***
	 * Counts the number of steps based on sensor data.
	 * 
	 * @param times
	 *            a 1d-array with the elapsed times in miliseconds for each row
	 *            in the sensorData array.
	 * @param accelData
	 *            a 2d-array where rows represent successive sensor data
	 *            samples, and the columns represent x-, y-, and z- accelerations.
	 * @return an int representing the number of steps
	 */
	public static int countStepsImproved2(double[] times, double[][] accelData) {
		double[] accelMags = calculateMagnitudesFor(accelData);

		double accelMagMean = calculateMean(accelMags);

		double accelStanDev = calculateStandardDeviation(accelMags, accelMagMean);

		int stepCounter = 0;

		double currentMaxPeak = accelMags[0];
		double currentMinTrough = accelMags[0];
		for (int i = 1; i < accelMags.length - 2; i++) {
			if (accelMags[i] > accelMags[i + 1] && accelMags[i] > accelMags[i - 1]) {
				currentMaxPeak = accelMags[i];
			}
			if (accelMags[i] < accelMags[i + 1] && accelMags[i] < accelMags[i - 1]) {
				currentMinTrough = accelMags[i];
			}
			if ((currentMaxPeak - currentMinTrough) > accelMagMean + accelStanDev) {
				stepCounter++;
			}
		}
		return stepCounter;
	}

	/***
	 * Counts the number of steps based on sensor data.
	 * 
	 * @param times
	 *            a 1d-array with the elapsed times in miliseconds for each row
	 *            in the sensorData array.
	 * @param accelData
	 *            a 2d-array where rows represent successive sensor data
	 *            samples, and the columns represent x-, y-, and z- accelerations.
	 * @return an int representing the number of steps
	 */
	public static int countStepsImproved3(double[] times, double[][] sensorData) {
		double[] accelMags = calculateMagnitudesFor(sensorData);
		int thresholdWindow = 1000;
		int stepSpacing = 25;

		int stepCounter = 0;
		double[] accelMagsSmooth = new double[accelMags.length / 3];
		for (int i = 1; i < accelMags.length - 1; i++) {

			int start = i - thresholdWindow;
			int end = i + thresholdWindow;
			if (start < 0)
				start = 0;
			if (end > accelMags.length)
				end = accelMags.length;
			double[] accelMagsWindow = new double[(end - start)];
			for (int i1 = start; i1 < end; i1++) {
				accelMagsWindow[i1 - start] = accelMags[i1];
			}
			double accelMagMean = calculateMean(accelMagsWindow);
			double accelStanDev = calculateStandardDeviation(accelMagsWindow, accelMagMean);

			if (accelMags[i] > accelMags[i - 1] && accelMags[i] > accelMags[i + 1]) {
				if (accelMags[i] > accelMagMean + accelStanDev * 0.4)

					stepCounter++;
				i += stepSpacing;

			}
		}
		// for(int i=1;i<gyroMags.length-1;i++){
		// if(gyroMags[i]>gyroMags[i-1]&&gyroMags[i]>gyroMags[i+1]){
		// if(gyroMags[i]>gyroMagMean+gyroStanDev)stepCounter++
		//
		// }
		// }
		return (stepCounter);
	}

	public static void displayJFrame(Plot2DPanel plot) {
		JFrame frame = new JFrame("Results");
		frame.setSize(800, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}

}