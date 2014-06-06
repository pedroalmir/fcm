package com.pedroalmir.visualfcm.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pedroalmir.visualfcm.fcm.FuzzyCMeansAlgorithm;
import com.pedroalmir.visualfcm.model.VisualFCModel;

@SuppressWarnings("serial")
public class FCMServlet extends HttpServlet {
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String json = request.getParameter("json");
		Boolean runAll = Boolean.valueOf(request.getParameter("runAll"));
		Gson gson = new Gson();
		
		VisualFCModel model = gson.fromJson(json, VisualFCModel.class);
		if(!model.isFinish()){
			FuzzyCMeansAlgorithm fuzzyCMeansAlgorithm = new FuzzyCMeansAlgorithm(model.getNbClusters(), model.getFuzziness(), 
					model.getDataSample(), model.getTolerance(), model.getMatrix());
			
			/* Execute the ant system */
			ArrayList<double[]> list = null;
			if(runAll){
				list = fuzzyCMeansAlgorithm.execute();
				model.setActualIteration(fuzzyCMeansAlgorithm.getIteration());
			}else{
				list = fuzzyCMeansAlgorithm.executeAnIteration();
				model.setActualIteration(model.getActualIteration()+1);
			}
			
			model.setError(fuzzyCMeansAlgorithm.getActualError());
			model.setFinish(fuzzyCMeansAlgorithm.isFinish());
			model.setMatrix(fuzzyCMeansAlgorithm.getMatrixU().getContents());
			model.setClusters(list);
			
			/* Set content type of the response so that jQuery knows what it can expect. */
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			
			/* Write response body. */
			response.getWriter().write(gson.toJson(model));
		}else{
			/* Set content type of the response so that jQuery knows what it can expect. */
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			
			/* Write response body. */
			response.getWriter().write(gson.toJson("finish"));
		}
	}
}
