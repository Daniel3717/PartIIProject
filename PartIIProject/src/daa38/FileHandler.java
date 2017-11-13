package daa38;

import java.io.File;
import java.io.FileNotFoundException;
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
			lIntToVarMap.put(lVarIt, lNewVar);
			
			lNewVar.mIndex = lVarIt;
			
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
		ArrayList<Variable> lOrderedVariables = new ArrayList<Variable>();
		for (int lIt = 0;lIt<pVariables.size();lIt++)
			lOrderedVariables.add(null);
		
		for (Variable lVar : pVariables)
		{
			lOrderedVariables.set(lVar.mIndex,lVar);
		}

		Scanner lAssignmentInput = new Scanner(new File(pPath));
		for (Variable lVar : lOrderedVariables)
		{
			lVar.mValue = lAssignmentInput.nextInt();
		}
		
		lAssignmentInput.close();
	}
}