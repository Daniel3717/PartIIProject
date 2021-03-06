package daa38.Statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.CSPFileHandler;
import daa38.MapColouring.Main.MapColouring;
import daa38.nQueens.Main.NQueens;

public class Generator {
	
	public static void generateNQueensCSP(String pFile, int pNrQueens) throws IOException
	{
		ArrayList<Variable> lVars = new ArrayList<Variable>();
		ArrayList<Constraint> lCons = new ArrayList<Constraint>();

		NQueens.generateCSP(pNrQueens,lVars,lCons);
		CSPFileHandler.writeFileProblem(pFile, lVars, lCons);
	}
	
	//Return values are in parameters, they are pFilesIn and pFilesOut. They have to be instantiated before calling massGenerate
	public static void massGenerateNQueensCSPs(String pBasePath, int pInstances, ArrayList<String> pFilesIn, ArrayList<String> pFilesOut) throws IOException
	{
		for (int lInstance = 4; lInstance <= 3+pInstances; lInstance++)
		{
			System.out.println("Generating nQueens instance "+lInstance);
			String lInPath = pBasePath + lInstance + ".in";
			String lOutPath = pBasePath + lInstance + ".out";
			Generator.generateNQueensCSP(lInPath, lInstance);
			
			pFilesIn.add(lInPath);
			pFilesOut.add(lOutPath);
		}
	}
	
	public static void generateMapColouringCSP(String pFile, int pNrZones, int pMapSize) throws IOException
	{
		int[][] lMap = MapColouring.generateMap(pNrZones, pMapSize);

		Collection<Variable> lVars = new ArrayList<Variable>();
		Collection<Constraint> lCons = new ArrayList<Constraint>();
		MapColouring.mapToCSP(lMap,lVars,lCons);
		
		CSPFileHandler.writeFileProblem(pFile, lVars, lCons);
	}
	
	//Return values are in parameters, they are pFilesIn and pFilesOut. They have to be instantiated before calling massGenerate
	public static void massGenerateMapColouringCSPs(String pBasePath, int pInstances, ArrayList<String> pFilesIn, ArrayList<String> pFilesOut) throws IOException
	{
		for (int lInstance = 1; lInstance <= pInstances; lInstance++)
		{
			System.out.println("Generating MapColouring instance "+lInstance);
			String lInPath = pBasePath + lInstance + ".in";
			String lOutPath = pBasePath + lInstance + ".out";
			Generator.generateMapColouringCSP(lInPath, lInstance, lInstance);
			
			pFilesIn.add(lInPath);
			pFilesOut.add(lOutPath);
		}
		
	}
}
