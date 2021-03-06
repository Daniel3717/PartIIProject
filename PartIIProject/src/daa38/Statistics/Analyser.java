package daa38.Statistics;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.CSPFileHandler;

public class Analyser {
	
	Collection<Variable> mVars;
	Collection<Constraint> mCons;
	
	public Analyser(String pCSPFile) throws FileNotFoundException
	{
		mVars = new ArrayList<Variable>();
		mCons = new ArrayList<Constraint>();
		CSPFileHandler.readFileProblem(pCSPFile, mVars, mCons);
	}

	public int getNrVariables()
	{
		return mVars.size();
	}
	
	public double getAverageDomainSize()
	{
		double lAvDS = 0;
		
		for (Variable lVar : mVars)
		{
			lAvDS += lVar.mDomain.size();
		}
		
		lAvDS = lAvDS / getNrVariables();
		
		return lAvDS;
	}
	
	public double getAverageNrConstraints()
	{
		double lAvCon = 0;
		
		for (Variable lVar : mVars)
		{
			for (Collection<Constraint> lVarCons : lVar.mConstraints.values())
			{
				for (Constraint lCon : lVarCons)
				{
					lAvCon += lCon.mValues.size();
				}
			}
		}
		
		lAvCon = lAvCon / getNrVariables();
		
		return lAvCon;
	}
}
