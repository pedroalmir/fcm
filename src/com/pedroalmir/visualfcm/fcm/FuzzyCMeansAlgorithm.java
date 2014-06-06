/**
 * 
 */
package com.pedroalmir.visualfcm.fcm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.pedroalmir.visualfcm.util.Matrix;
import com.pedroalmir.visualfcm.util.Matrix.Builder;

/**
 * @author Pedro Almir
 *
 */
public class FuzzyCMeansAlgorithm {
	/** Actual matrix */
	private Matrix matrixU;
	/** Old matrix */
	private Matrix oldMatrixU;
	
	/** Tolerance level, predefined threshold */
	private double toleranceLevel;
	/** Fuzziness value */
	private double m;
	
	/** Features or points at cartesian plane */
	private List<double[]> features;
	/** List of centroids */
	private ArrayList<double[]> centroids;
	/** Number of clusters */
	private int nbCluster;
	
	/** Auxiliary variables */
	private boolean initialized;
	private boolean finish;
	private int iteration;
	private double actualError;
	
	/**
	 * @param nbCluster
	 * @param initFeatures
	 */
	public FuzzyCMeansAlgorithm(int nbCluster, double m, List<double[]> initFeatures, double toleranceLevel, double[][] matrix) {
		this.m = m;
		this.iteration = 0;
		
		this.nbCluster = nbCluster;
		this.features = initFeatures;
		this.toleranceLevel = toleranceLevel;
		
		this.centroids = new ArrayList<double[]>(this.nbCluster);
		if(matrix != null){
			initMatrixU(matrix);
			this.initialized = true;
		}else{
			this.initialized = false;
		}
	}
	
	/**
	 * Execute Fuzzy C-Means clustering algorithm: iterative optimization 
	 */
	public ArrayList<double[]> execute(){
		if(!initialized){
			initialize();
		}
		do{
			calculateClusterCenters();
			updateAllDataSamples();
			iteration++;
		} while(verifyToleranceLevel());
		
		this.finish = true;
		return this.centroids;
	}
	
	/**
	 * Execute an iteration
	 */
	public ArrayList<double[]> executeAnIteration(){
		if(!initialized){
			initialize();
		}
		calculateClusterCenters();
		updateAllDataSamples();
		
		this.finish = !verifyToleranceLevel();
		return this.centroids;
	}
	
	/**
	 * Step 1: Initialize
	 */
	private void initialize(){
		initMatrixU();
		this.initialized = true;
	}
	
	/**
	 * Step 2: Calculate cluster centers
	 */
	private void calculateClusterCenters() {
		ArrayList<double[]> list = new ArrayList<double[]>(this.nbCluster);
		
		for(int clusterCenter = 0; clusterCenter < this.nbCluster; clusterCenter++){
			double sumX = 0.0, sumY = 0.0, amount = 0.0;
			
			for(int point = 0; point < this.features.size(); point++){
				sumX += Math.pow(this.matrixU.valueAt(clusterCenter, point), this.m) * this.features.get(point)[0];
				sumY += Math.pow(this.matrixU.valueAt(clusterCenter, point), this.m) * this.features.get(point)[1];
				
				amount += Math.pow(this.matrixU.valueAt(clusterCenter, point), this.m);
			}
			
			/* Create the new centroid */
			double[] centroid = new double[]{sumX/amount, sumY/amount};
			list.add(centroid);
		}
		/* Update list of centroids */
		this.centroids = list;
	}
	
	/**
	 * Step 3: For all clusters and for all data samples, update matrix U.
	 */
	private void updateAllDataSamples(){
		Builder builder = Matrix.builder().dimensions(this.nbCluster, this.features.size());
		double exponent = 2.0 / (this.m - 1);
		/* loop over clusters */
		for(int line = 0; line < this.nbCluster; line++){
			/* loop over features */
			for(int col = 0; col < this.features.size(); col++){
				double denominator = 0.0;
				/* dik = d(xk - vi) */
				double dik = euclideanDistance(this.features.get(col), this.centroids.get(line));
				if(dik != 0.0){
					for(int s = 0; s < this.nbCluster; s++){
						double dsk = euclideanDistance(this.features.get(col), this.centroids.get(s));
						double ratio = Math.pow((dik/dsk), exponent);
						denominator += ratio;
					}
					builder.valueAt(line, col, 1.0/denominator);
				}else{
					builder.valueAt(line, col, 0.0);
				}
			}
		}
		
		this.oldMatrixU = this.matrixU;
		this.matrixU = builder.build();
	}
	
	/**
	 * Step 4: Tolerance Level
	 */
	private boolean verifyToleranceLevel(){
		double sum = 0.0;
		for(int line = 0; line < this.nbCluster; line++){
			double[] matrixUCol = new double[this.features.size()];
			double[] oldMatrixUCol = new double[this.features.size()];
			
			for(int col = 0; col < this.features.size(); col++){
				matrixUCol[col] = this.matrixU.valueAt(line, col);
				oldMatrixUCol[col] = this.oldMatrixU.valueAt(line, col);
			}
			
			sum += euclideanDistance(matrixUCol, oldMatrixUCol);
		}
		this.actualError = sum;
		return (sum > this.toleranceLevel);
	}
	
	/**
	 * Initialize the U matrix
	 */
	private void initMatrixU(){
		Builder builder = Matrix.builder().dimensions(this.nbCluster, this.features.size());
		for(int col = 0; col < this.features.size(); col++){
			double[] values = getNRandomValues(this.nbCluster);
			for(int line = 0; line < this.nbCluster; line++){
				builder.valueAt(line, col, values[line]);
			}
		}
		this.matrixU = builder.build();
	}
	
	/**
	 * Initialize the U matrix
	 */
	private void initMatrixU(double[][] matrix){
		Builder builder = Matrix.builder().dimensions(this.nbCluster, this.features.size());
		for(int line = 0; line < this.nbCluster; line++){
			for(int col = 0; col < this.features.size(); col++){
				builder.valueAt(line, col, matrix[line][col]);
			}
		}
		this.matrixU = builder.build();
	}
	
	/**
	 * @param nbCluster
	 * @return
	 */
	private double[] getNRandomValues(int nbCluster) {
		Random random = new Random();
		double[] values = new double[nbCluster];
		double min = 0.0, sum = 0.0;
		for(int i = 0; i < nbCluster-1; i++){
			double nextDouble = min + (random.nextDouble() * (1.0 - min));
			sum += nextDouble - min;
			values[i] = nextDouble - min;
			min = nextDouble;
		}
		values[nbCluster-1] = 1.0 - sum;
		return values;
	}

	/**
	 * Compute euclidian distance
	 * 
	 * @param a
	 * @param b
	 * @return Euclidian distance between two points
	 */
	private static double euclideanDistance(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new IllegalArgumentException("The dimensions have to be equal!");
		}

		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += Math.pow(a[i] - b[i], 2);
		}

		return Math.sqrt(sum);
	}

	/**
	 * @return the matrixU
	 */
	public Matrix getMatrixU() {
		return matrixU;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FCM Algorithm [matrixU=" + matrixU + ", features=" + Arrays.toString(features.toArray()) 
				+ ", centroids=" + Arrays.toString(centroids.toArray()) + "]";
	}
	
	/**
	 * @return
	 */
	public String toStringCentroids(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for(double[] array : this.centroids){
			buffer.append(Arrays.toString(array));
		}
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * @return the centroids
	 */
	public List<double[]> getCentroids() {
		return centroids;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FuzzyCMeansAlgorithm algorithm = new FuzzyCMeansAlgorithm(2, 1.25, Arrays.asList(new double[]{1.0, 3.0}, 
				new double[]{1.5, 3.2}, new double[]{1.3, 2.8}, new double[]{3.0, 1.0}), 0.0, null);
		algorithm.execute();
		
		System.out.println("Matrix U: ");
		System.out.println(algorithm.getMatrixU().toString());
		System.out.println("Centroids: " + algorithm.toStringCentroids());
	}

	/**
	 * @return the finish
	 */
	public boolean isFinish() {
		return finish;
	}

	/**
	 * @return the iteration
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * @return the actualError
	 */
	public double getActualError() {
		return actualError;
	}
}
