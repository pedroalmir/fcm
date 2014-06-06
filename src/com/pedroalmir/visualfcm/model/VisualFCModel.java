/**
 * 
 */
package com.pedroalmir.visualfcm.model;

import java.util.ArrayList;

/**
 * @author Pedro Almir
 *
 */
public class VisualFCModel {
	private int nbClusters;
	private int actualIteration;
	
	private double tolerance;
	private double fuzziness;
	
	private double[][] matrix;
	private ArrayList<double[]> clusters;
	private ArrayList<double[]> dataSample;
	
	private boolean finish;
	private double error;
	
	/**
	 * Visual FCM Model default constructor
	 */
	public VisualFCModel() {
		
	}
	
	/**
	 * @return the tolerance
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * @param tolerance the tolerance to set
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * @return the fuzziness
	 */
	public double getFuzziness() {
		return fuzziness;
	}

	/**
	 * @param fuzziness the fuzziness to set
	 */
	public void setFuzziness(double fuzziness) {
		this.fuzziness = fuzziness;
	}

	/**
	 * @return the matrix
	 */
	public double[][] getMatrix() {
		return matrix;
	}

	/**
	 * @param matrix the matrix to set
	 */
	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}

	/**
	 * @return the nbClusters
	 */
	public int getNbClusters() {
		return nbClusters;
	}

	/**
	 * @param nbClusters the nbClusters to set
	 */
	public void setNbClusters(int nbClusters) {
		this.nbClusters = nbClusters;
	}

	/**
	 * @return the clusters
	 */
	public ArrayList<double[]> getClusters() {
		return clusters;
	}

	/**
	 * @param clusters the clusters to set
	 */
	public void setClusters(ArrayList<double[]> clusters) {
		this.clusters = clusters;
	}

	/**
	 * @return the dataSample
	 */
	public ArrayList<double[]> getDataSample() {
		return dataSample;
	}

	/**
	 * @param dataSample the dataSample to set
	 */
	public void setDataSample(ArrayList<double[]> dataSample) {
		this.dataSample = dataSample;
	}

	/**
	 * @return the actualIteration
	 */
	public int getActualIteration() {
		return actualIteration;
	}

	/**
	 * @param actualIteration the actualIteration to set
	 */
	public void setActualIteration(int actualIteration) {
		this.actualIteration = actualIteration;
	}

	/**
	 * @return the finish
	 */
	public boolean isFinish() {
		return finish;
	}

	/**
	 * @param finish the finish to set
	 */
	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	/**
	 * @return the error
	 */
	public double getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(double error) {
		this.error = error;
	}
}
