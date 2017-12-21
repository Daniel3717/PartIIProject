package daa38.Auxiliary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class FileHandler {

	public static void readFileProblem(String pPath, Collection<Variable> pVariables, Collection<Constraint> pConstraints) throws FileNotFoundException
	{
		HashMap<Integer, Variable> lIntToVarMap = new HashMap<Integer,Variable>();
		
		Scanner lProblemInput = new Scanner(new File(pPath));

		int lVariableLines = lProblemInput.nextInt();
		for (int lVarIt=0;lVarIt<lVariableLines;lVarIt++)
		{
			Variable lNewVar = new Variable();
			pVariables.add(lNewVar);
			int lIndex = lProblemInput.nextInt();
			lIntToVarMap.put(lIndex, lNewVar);
			
			lNewVar.mIndex = lIndex;
			
			int lValueCount = lProblemInput.nextInt();
			for (int lValIt=0;lValIt<lValueCount;lValIt++)
			{
				lNewVar.mDomain.add(lProblemInput.nextInt());
			}
		}
		
		int lConstraintLines = lProblemInput.nextInt();;
		for (int lConIt=0;lConIt<lConstraintLines;lConIt++)
		{
			Constraint lNewCon = new Constraint();
			pConstraints.add(lNewCon);
			
			int lVarIndex1 = lProblemInput.nextInt();
			int lVarIndex2 = lProblemInput.nextInt();
			
			Variable lVar1 = lIntToVarMap.get(lVarIndex1);
			Variable lVar2 = lIntToVarMap.get(lVarIndex2);
			
			lVar1.mConstraints.add(lNewCon);
			lVar2.mConstraints.add(lNewCon);
			
			lNewCon.mVariable1 = lVar1;
			lNewCon.mVariable2 = lVar2;
			
			int lPairCount = lProblemInput.nextInt();
			for (int lPairIt=0;lPairIt<lPairCount;lPairIt++)
			{
				int lVal1=lProblemInput.nextInt();
				int lVal2=lProblemInput.nextInt();
				lNewCon.mValues.add(new PairInts(lVal1,lVal2));
			}
		}
		
		lProblemInput.close();
	}
	
	public static void readFileAssignment(String pPath, Collection<Variable> pVariables) throws FileNotFoundException
	{
		HashMap<Integer, Variable> lIntToVarMap = new HashMap<Integer, Variable>();
		for (Variable lV : pVariables)
			lIntToVarMap.put(lV.mIndex, lV);

		Scanner lAssignmentInput = new Scanner(new File(pPath));
		int lNumberOfVariables = lAssignmentInput.nextInt();
		for (int i=0;i<lNumberOfVariables;i++)
		{
			int lIndex = lAssignmentInput.nextInt();
			int lValue = lAssignmentInput.nextInt();
			lIntToVarMap.get(lIndex).mValue=lValue;
		}
		
		lAssignmentInput.close();
	}
	
	public static void writeFileAssignment(String pPath, Collection<Variable> pVariables) throws IOException
	{
		Writer lAssignmentOutput = new FileWriter(new File(pPath));
		
		lAssignmentOutput.write(pVariables.size()+"\r\n");
		for (Variable lV : pVariables)
		{
			lAssignmentOutput.write(lV.mIndex+" "+lV.mValue+"\r\n");
		}
		
		lAssignmentOutput.close();
	}
	
	//NOTE: Variables need to have only mIndex and mDomain assigned
	//NOTE: Constraints need to have both variables and the ArrayList of PairInts assigned
	public static void writeFileProblem(String pPath, Collection<Variable> pVariables, Collection<Constraint> pConstraints) throws IOException
	{
		Writer lProblemOutput = new FileWriter(new File(pPath));
		
		lProblemOutput.write(pVariables.size()+"\r\n");
		for (Variable lVar : pVariables)
		{
			lProblemOutput.write(lVar.mIndex+" ");
			lProblemOutput.write(lVar.mDomain.size()+"");
			
			for (Integer lInt : lVar.mDomain)
			{
				lProblemOutput.write(" " + lInt);
			}
			lProblemOutput.write("\r\n");
		}
		
		lProblemOutput.write(pConstraints.size()+"\r\n");
		for (Constraint lCon : pConstraints)
		{
			lProblemOutput.write(lCon.mVariable1.mIndex+" "+lCon.mVariable2.mIndex+" "+lCon.mValues.size());
			
			for (PairInts lPI : lCon.mValues)
			{
				lProblemOutput.write(" "+lPI.first+" "+lPI.second);
			}
			
			lProblemOutput.write("\r\n");
		}
		
		lProblemOutput.close();
	}
	
	public static void writeNQueensSolution(String pPath, int[][] pMatrix) throws IOException
	{
		Writer lProblemOutput = new FileWriter(new File(pPath));
		
		int lSize = pMatrix.length;
		
		lProblemOutput.write(lSize+"\r\n");
		
		for (int lLine = 0; lLine < lSize; lLine++)
		{
			String lStringLine ="";
			for (int lColumn = 0; lColumn < lSize; lColumn++)
			{
				lStringLine += pMatrix[lLine][lColumn]+" ";
			}
			lStringLine+="\r\n";
			lProblemOutput.write(lStringLine);
		}
		
		lProblemOutput.close();
	}
	
	
	public static int[][] readNQueensSolution(String pPath) throws FileNotFoundException
	{
		Scanner lNQueensInput = new Scanner(new File(pPath));
		
		int lSize = lNQueensInput.nextInt();
		int[][] lMatrix = new int[lSize][lSize];
		
		for (int lLine = 0; lLine<lSize; lLine++)
		{
			for (int lColumn = 0; lColumn<lSize; lColumn++)
			{
				lMatrix[lLine][lColumn] = lNQueensInput.nextInt();
			}
		}
		
		lNQueensInput.close();
		
		return lMatrix;
	}
	
	private static int countDigits(int pInt)
	{
		int lNrDigits = 0;
		while (pInt>0)
		{
			lNrDigits++;
			pInt/=10;
		}
		
		return lNrDigits;
	}
	
	private static String spaceFormat(int pInt, int pNrDigits)
	{
		int lNrDigits = countDigits(pInt);
		
		String lOut = "";
		while (lNrDigits<pNrDigits)
		{
			lOut+=" ";
			lNrDigits++;
		}
		lOut+=pInt;
		
		return lOut;
	}
	
	//The map must be a matrix (so all lines have the same number of columns)
	//and must have at least one element. All elements must be positive.
	public static void writeMap(String pPath, int[][] pMap) throws IOException	
	{
		//It is not mandatory, but I will make it so that in the out file
		//all elements occupy the same character space.
		//This is for better visualisation
		
		Writer lMapOut = new FileWriter(new File(pPath));
	
		int lRows = pMap.length;
		int lCols = pMap[0].length;
		
		lMapOut.write(lRows+" "+lCols+"\r\n");
		
		int lMax = 0;
		for (int lRow = 0; lRow < lRows; lRow++)
		{
			for (int lCol = 0; lCol < lCols; lCol++)
			{
				if (pMap[lRow][lCol]>lMax)
					lMax=pMap[lRow][lCol];
			}
		}
		
		int lNrDigits = countDigits(lMax);
		
		for (int lRow = 0; lRow < lRows; lRow++)
		{
			for (int lCol = 0; lCol < lCols; lCol++)
			{
				lMapOut.write(spaceFormat(pMap[lRow][lCol],lNrDigits)+" ");
			}
			lMapOut.write("\r\n");
		}
		
		lMapOut.close();
	}
	
	public static int[][] readMap(String pPath) throws FileNotFoundException
	{
		Scanner lMapIn = new Scanner(new File(pPath));
		
		int lRows = lMapIn.nextInt();
		int lCols = lMapIn.nextInt();
		
		int[][] lMap = new int[lRows][lCols];
		
		for (int lRow = 0; lRow < lRows; lRow++)
		{
			for (int lCol = 0; lCol < lCols; lCol++)
			{
				lMap[lRow][lCol] = lMapIn.nextInt();
			}
		}
		
		lMapIn.close();
		
		return lMap;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		String lPath = "gentest_problem.txt";
		ArrayList<Variable> lVars = new ArrayList<Variable>();
		ArrayList<Constraint> lCons = new ArrayList<Constraint>();
		
		Variable lV1 = new Variable();
		lV1.mIndex = 0;
		lV1.mDomain.add(1);
		lV1.mDomain.add(2);
		lVars.add(lV1);
		
		Variable lV2 = new Variable();
		lV2.mIndex = 1;
		lV2.mDomain.add(1);
		lV2.mDomain.add(2);
		lVars.add(lV2);
	
		Constraint lCon = new Constraint();
		lCon.mVariable1=lV1;
		lCon.mVariable2=lV2;
		lCon.mValues.add(new PairInts(1,1));
		lCon.mValues.add(new PairInts(2,2));
		lCons.add(lCon);
		
		writeFileProblem(lPath,lVars,lCons);
		
	}
}