package daa38.CSP.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.PairInts;
import daa38.CSP.Auxiliary.Variable;

public class CSPFileHandler {

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
			
			lNewVar.mName = lIndex;
			
			int lValueCount = lProblemInput.nextInt();
			for (int lValIt=0;lValIt<lValueCount;lValIt++)
			{
				lNewVar.mDomain.add(lProblemInput.nextInt());
			}
		}
		
		int lConstraintLines = lProblemInput.nextInt();
		for (int lConIt=0;lConIt<lConstraintLines;lConIt++)
		{
			Constraint lNewCon = new Constraint();
			pConstraints.add(lNewCon);
			
			int lVarIndex1 = lProblemInput.nextInt();
			int lVarIndex2 = lProblemInput.nextInt();
			
			Variable lVar1 = lIntToVarMap.get(lVarIndex1);
			Variable lVar2 = lIntToVarMap.get(lVarIndex2);

			
			Collection<Constraint> lVar2InVar1 = lVar1.mConstraints.get(lVar2);
			if (lVar2InVar1 == null)
			{
				lVar2InVar1 = new ArrayList<Constraint>();
				lVar1.mConstraints.put(lVar2, lVar2InVar1);
			}
			lVar2InVar1.add(lNewCon);

			Collection<Constraint> lVar1InVar2 = lVar2.mConstraints.get(lVar1);
			if (lVar1InVar2 == null)
			{
				lVar1InVar2 = new ArrayList<Constraint>();
				lVar2.mConstraints.put(lVar1, lVar1InVar2);
			}
			lVar1InVar2.add(lNewCon);
			
			lNewCon.mVariable1 = lVar1;
			lNewCon.mVariable2 = lVar2;
			
			int lPairCount = lProblemInput.nextInt();
			for (int lPairIt=0;lPairIt<lPairCount;lPairIt++)
			{
				int lVal1=lProblemInput.nextInt();
				int lVal2=lProblemInput.nextInt();
				lNewCon.addPair(new PairInts(lVal1,lVal2));
			}
		}
		
		lProblemInput.close();
	}
	
	public static void readFileAssignment(String pPath, Collection<Variable> pVariables) throws FileNotFoundException
	{
		HashMap<Integer, Variable> lIntToVarMap = new HashMap<Integer, Variable>();
		for (Variable lV : pVariables)
			lIntToVarMap.put(lV.mName, lV);

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
			lAssignmentOutput.write(lV.mName+" "+lV.mValue+"\r\n");
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
			lProblemOutput.write(lVar.mName+" ");
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
			lProblemOutput.write(lCon.mVariable1.mName+" "+lCon.mVariable2.mName+" "+lCon.mValues.size());
			
			for (PairInts lPI : lCon.mValues)
			{
				lProblemOutput.write("   "+lPI.getAtIndex(1)+" "+lPI.getAtIndex(2));
			}
			
			lProblemOutput.write("\r\n");
		}
		
		lProblemOutput.close();
	}
	
	public static void main(String[] args) throws IOException {
		
		String lPath = "gentest_problem.txt";
		ArrayList<Variable> lVars = new ArrayList<Variable>();
		ArrayList<Constraint> lCons = new ArrayList<Constraint>();
		
		Variable lV1 = new Variable();
		lV1.mName = 0;
		lV1.mDomain.add(1);
		lV1.mDomain.add(2);
		lVars.add(lV1);
		
		Variable lV2 = new Variable();
		lV2.mName = 1;
		lV2.mDomain.add(1);
		lV2.mDomain.add(2);
		lVars.add(lV2);
	
		Constraint lCon = new Constraint();
		lCon.mVariable1=lV1;
		lCon.mVariable2=lV2;
		lCon.addPair(new PairInts(1,1));
		lCon.addPair(new PairInts(2,2));
		lCons.add(lCon);
		
		writeFileProblem(lPath,lVars,lCons);
		
	}
}
