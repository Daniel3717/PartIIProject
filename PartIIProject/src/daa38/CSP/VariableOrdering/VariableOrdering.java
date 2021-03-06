package daa38.CSP.VariableOrdering;

import java.util.ArrayList;

import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.Solver;

public abstract class VariableOrdering {
	
	Solver mSolver;
	
	protected VariableOrdering(Solver pSolver)
	{
		mSolver = pSolver;
	}
	
	//pVars will be the variables from which we select the next variable
	//It assumes that all variables inside pVars are not assigned
	public abstract Variable order(ArrayList<Variable> pVars);
}
