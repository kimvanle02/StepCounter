
public class StepCounter {

	public static int countSteps(double[] times, double[][] sensorData) {
		double[][] accelData = CSVData.getColumns(sensorData, 0, 3);

		double[][] gyroData = CSVData.getColumns(sensorData, 3, 6);

		System.out.println();
		double[] accelMags = calculateMagnitudesFor(accelData);
		double[] gyroMags = calculateMagnitudesFor(gyroData);
		double accelMagMean = calculateMean(accelMags);
		double gyroMagMean = calculateMean(gyroMags);
		double accelStanDev = calculateStandardDeviation(accelMags, accelMagMean);
		double gyroStanDev = calculateStandardDeviation(gyroMags, gyroMagMean);
		int stepCounter = 0;
		for (int i = 1; i < accelMags.length - 1; i++) {
			if (accelMags[i] > accelMags[i - 1] && accelMags[i] > accelMags[i + 1]) {
				if (accelMags[i] > accelMagMean + accelStanDev)
					stepCounter++;

			}
		}
		for (int i = 1; i < gyroMags.length - 1; i++) {
			if (gyroMags[i] > gyroMags[i - 1] && gyroMags[i] > gyroMags[i + 1]) {
				if (gyroMags[i] > gyroMagMean + gyroStanDev)
					stepCounter++;

			}
		}
		return (stepCounter /= 2);
	}

	private static Object accelMags(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	private static double calculateStandardDeviation(double[] arr, double mean) {
		double totalSum = 0;
		for (int i = 0; i < arr.length; i++) {
			double x = (arr[i] - mean) * (arr[i] - mean);
			totalSum += x;
		}
		totalSum /= arr.length - 1;
		totalSum = Math.sqrt(totalSum);
		return totalSum;

	}

	private static double calculateMean(double[] arr) {
		double totalSum = 0;
		for (int i = 0; i < arr.length; i++) {
			totalSum += arr[i];
		}
		totalSum /= arr.length;
		return totalSum;
	}

	private static double calculateMagnitude(double x, double y, double z) {
		return (Math.sqrt((x * x) + (y * y) + (z * z)));
	}

	private static double[] calculateMagnitudesFor(double[][] sensorData) {
		double[] result = new double[sensorData.length];
		for (int i = 0; i < sensorData.length; i++) {
			result[i] = calculateMagnitude(sensorData[i][0], sensorData[i][1], sensorData[i][2]);
		}
		return result;
	}
}
