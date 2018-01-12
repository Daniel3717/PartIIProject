package daa38.CSP.Main;

import java.io.IOException;
import java.util.Collection;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.PairInts;
import daa38.CSP.Auxiliary.Variable;

import java.util.ArrayList;

public class Validator {
	
	private Collection<Variable> mVariables;
	private Collection<Constraint> mConstraints;
	
	public Validator(String pProblemPath, String pSolutionPath) throws IOException
	{
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();
		
		CSPFileHandler.readFileProblem(pProblemPath, mVariables, mConstraints);
		CSPFileHandler.readFileAssignment(pSolutionPath, mVariables);
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
				if ((lCon.mVariable1.mValue==lPair.getAtIndex(1))&&(lCon.mVariable2.mValue==lPair.getAtIndex(2)))
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
		Validator lV = new Validator("1_problem.txt","1_solution.txt");
		System.out.println(lV.check());
	}

}
