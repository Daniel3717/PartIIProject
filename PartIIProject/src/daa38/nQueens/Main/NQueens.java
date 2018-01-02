package daa38.nQueens.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.PairInts;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.CSPFileHandler;
import daa38.CSP.Main.Solver;

public class NQueens {

	public static void generateCSP(int pNrQueens, Collection<Variable> pVars, Collection<Constraint> pCons)
	{
		pVars.clear();
		pCons.clear();
		
		HashMap<Integer, Variable> lITV = new HashMap<Integer, Variable>();
		
		ArrayList<Integer> lValues = new ArrayList<Integer>();
		for (int lVal = 0; lVal<pNrQueens; lVal++)
		{
			lValues.add(lVal);
		}
		
		for (int lRow = 0; lRow<pNrQueens; lRow++)
		{
			Variable lVar = new Variable();
			lVar.mIndex = lRow;
			lVar.mDomain = (Collection<Integer>) lValues.clone();
			pVars.add(lVar);
			
			lITV.put(lVar.mIndex, lVar);
		}
		
		for (int lRow1 = 0; lRow1<pNrQueens; lRow1++)
		{
			for (int lRow2 = lRow1+1; lRow2<pNrQueens; lRow2++)
			{
				Constraint lCon = new Constraint();
				lCon.mVariable1 = lITV.get(lRow1);
				lCon.mVariable2 = lITV.get(lRow2);
				
				for (int lInt = 0; lInt<pNrQueens; lInt++)
				{
					lCon.mValues.add(new PairInts(lInt,lInt));
				}
				
				int lDif = lRow2-lRow1;
				for (int lInt = 0; lInt+lDif<pNrQueens; lInt++)
				{
					lCon.mValues.add(new PairInts(lInt,lInt+lDif));
					lCon.mValues.add(new PairInts(lInt+lDif,lInt));
				}
				
				pCons.add(lCon);
			}
		}
	}
	
	public static int[][] CSPToNQueens(Collection<Variable> pVars)
	{
		int[][] lMatrix = new int[pVars.size()][pVars.size()];
		
		for (Variable lVar : pVars)
		{
			lMatrix[lVar.mIndex][lVar.mValue]=1;
		}
		
		return lMatrix;
	}
	
	public static boolean insideMatrix(int pSize, int pRow, int pCol)
	{
		if (pRow<0)
			return false;
		
		if (pRow>=pSize)
			return false;
		
		if (pCol<0)
			return false;
		
		if (pCol>=pSize)
			return false;
		
		return true;
	}
	
	public static boolean nQueensCheckMatrix(int[][] pMatrix)
	{	
		int lNrQueens = 0;
		int lSize = pMatrix.length;
		for (int lRow = 0; lRow<lSize; lRow++)
		{
			for (int lCol = 0; lCol<lSize; lCol++)
			{
				if (pMatrix[lRow][lCol]==1)
				{
					lNrQueens++;
					int lKQueens = 0;
					for (int lCheck = 0; lCheck<lSize; lCheck++)
					{
						if (pMatrix[lRow][lCheck]==1)
							lKQueens++;
						if (pMatrix[lCheck][lCol]==1)
							lKQueens++;

						for (int lRowModif = -1; lRowModif<=1; lRowModif+=2)
						{
							for (int lColModif = -1; lColModif<=1; lColModif+=2)
							{
								int lNewRow = lRow + lCheck * lRowModif;
								int lNewCol = lCol + lCheck * lColModif;
								
								if (insideMatrix(lSize,lNewRow,lNewCol)&&pMatrix[lNewRow][lNewCol]==1)
									lKQueens++;
							}
						}
					}
					
					//There should be a total of 6 counts of queens on the queen's row,column,diagonals
					//(which are actually 6 duplicates of the queen in question)
					if (lKQueens<6)
						System.out.println("nQueensCheck has found less than 4 queens on the CHECK part, which should be impossible");
					if (lKQueens>6)
						return false; //queens attack each other
				}
			}
		}
		
		if (lNrQueens>lSize)
			System.out.println("nQueensCheck has found more than lSize queens without conflict, which should be impossible");
		if (lNrQueens<lSize)
			return false; //not enough queens
		
		return true;
	}

	public static void main(String[] args) throws IOException {
		
		for (int lInstance=4;lInstance<15;lInstance++)
		{
			ArrayList<Variable> lVars = new ArrayList<Variable>();
			ArrayList<Constraint> lCons = new ArrayList<Constraint>();
			
			int NUMBER_OF_QUEENS = lInstance;
			
			String lCSPProblemPath = "nQueens/"+lInstance+"_nQueensCSP_problem.txt";
			generateCSP(NUMBER_OF_QUEENS,lVars,lCons);
			CSPFileHandler.writeFileProblem(lCSPProblemPath, lVars, lCons);
			
			String lCSPSolutionPath = "nQueens/"+lInstance+"_nQueensCSP_solution.txt";
			Solver.solve(lCSPProblemPath, lCSPSolutionPath);
			
			ArrayList<Variable> lVarsRead = new ArrayList<Variable>();
			for (int i=0;i<NUMBER_OF_QUEENS;i++)
			{
				Variable lVar = new Variable();
				lVar.mIndex = i;
				lVarsRead.add(lVar);
			}
			CSPFileHandler.readFileAssignment(lCSPSolutionPath, lVarsRead);
			
			int[][] lMatrix = CSPToNQueens(lVarsRead);
			String lNQueensSolutionPath = "nQueens/"+lInstance+"_nQueens_solution.txt";
			NQueensFileHandler.writeNQueensSolution(lNQueensSolutionPath, lMatrix);
			
			System.out.println(NQueensFileHandler.nQueensCheckFile(lNQueensSolutionPath));
		}
	}

}