package daa38.CSP.VariableOrdering;

import java.util.ArrayList;
import java.util.Random;

import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.Solver;

public class RandomVariableOrdering extends VariableOrdering {

	public RandomVariableOrdering(Solver pSolver)
	{
		super(pSolver);
	}
	
	@Override
	public Variable order(ArrayList<Variable> pVars) {
		
		Random lRandom = new Random();
		int lRandomIndex = lRandom.nextInt(pVars.size());
		return pVars.get(lRandomIndex);
	}

}
