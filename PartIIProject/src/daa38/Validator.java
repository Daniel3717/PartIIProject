package daa38;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Validator {
	
	private ArrayList<Variable> mVariables;
	private ArrayList<Constraint> mConstraints;
	
	public Validator(String pProblemPath, String pSolutionPath) throws IOException
	{
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();
		
		Scanner lProblemInput = new Scanner(new File(pProblemPath));

		int lVariableLines = lProblemInput.nextInt();
		for (int lVarIt=0;lVarIt<lVariableLines;lVarIt++)
		{
			Variable lNewVar = new Variable();
			mVariables.add(lNewVar);
			
			lNewVar.mName = lVarIt;
			
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
			mConstraints.add(lNewCon);
			
			int lVarIndex1=lProblemInput.nextInt();
			int lVarIndex2=lProblemInput.nextInt();
			
			Variable lVar1 = mVariables.get(lVarIndex1);
			Variable lVar2 = mVariables.get(lVarIndex2);
			
			lVar1.mConstraints.add(lNewCon);
			lVar2.mConstraints.add(lNewCon);
			
			lNewCon.mVariable1 = lVar1;
			lNewCon.mVariable2 = lVar2;
			
			int lPairCount = lProblemInput.nextInt();;
			for (int lPairIt=0;lPairIt<lPairCount;lPairIt++)
			{
				int lVal1=lProblemInput.nextInt();
				int lVal2=lProblemInput.nextInt();
				lNewCon.mValues.add(new PairInts(lVal1,lVal2));
			}
		}

		Scanner lSolutionInput = new Scanner(new File(pSolutionPath));
		for (Variable lVar : mVariables)
			lVar.mValue = lSolutionInput.nextInt();
	}
	
	public boolean check()
	{
		for (Variable lVar : mVariables)
		{
			if (!lVar.mDomain.contains(lVar.mValue))
			{
				System.out.println(lVar.mName+" cannot have value "+lVar.mValue);
				return false;
			}
		}
		
		for (Constraint lCon : mConstraints)
		{
			for (PairInts lPair : lCon.mValues)
			{
				if ((lCon.mVariable1.mValue==lPair.first)&&(lCon.mVariable2.mValue==lPair.second))
				{
					System.out.println(lCon.mVariable1.mName+" and "+lCon.mVariable2.mName+
							" cannot have values ("+lCon.mVariable1.mValue+","+lCon.mVariable2.mValue+")");
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) throws IOException {
		Validator lV = new Validator("3_problem.txt","3_solution.txt");
		System.out.println(lV.check());
	}

}
