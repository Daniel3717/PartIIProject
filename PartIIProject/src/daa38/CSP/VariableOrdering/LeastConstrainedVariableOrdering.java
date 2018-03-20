package daa38.CSP.VariableOrdering;

import java.util.ArrayList;
import java.util.Collections;

import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.Solver;



public class LeastConstrainedVariableOrdering extends VariableOrdering {

	public LeastConstrainedVariableOrdering(Solver pSolver)
	{
		super(pSolver);
	}
	
	//Basically, we do MostConstrainedVariableOrdering, but return min instead of max
	@Override
	public Variable order(ArrayList<Variable> pVars) {

		ArrayList<VCV> mOrder = new ArrayList<VCV>();
		for (Variable lV : pVars)
		{
			mOrder.add(new VCV(lV.mDomain.size(),lV.mConstraints.size(),lV));
		}
		
		return (Collections.min(mOrder).mVariable);
	}

}
