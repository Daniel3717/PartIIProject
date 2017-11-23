package daa38;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

public class Validator {
	
	private Collection<Variable> mVariables;
	private Collection<Constraint> mConstraints;
	
	public Validator(String pProblemPath, String pSolutionPath) throws IOException
	{
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();
		
		FileHandler.readFileProblem(pProblemPath, mVariables, mConstraints);
		FileHandler.readFileAssignment(pSolutionPath, mVariables);
	}
	
	public boolean check()
	{
		for (Variable lVar : mVariables)
		{
			if (!lVar.mDomain.contains(lVar.mValue))
			{
				System.out.println(lVar.mIndex+" cannot have value "+lVar.mValue);
				return false;
			}
		}
		
		for (Constraint lCon : mConstraints)
		{
			for (PairInts lPair : lCon.mValues)
			{
				if ((lCon.mVariable1.mValue==lPair.first)&&(lCon.mVariable2.mValue==lPair.second))
				{
					System.out.println(lCon.mVariable1.mIndex+" and "+lCon.mVariable2.mIndex+
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
