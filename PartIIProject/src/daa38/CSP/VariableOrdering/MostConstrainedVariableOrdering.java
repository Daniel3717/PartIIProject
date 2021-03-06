package daa38.CSP.VariableOrdering;

import java.util.ArrayList;
import java.util.Collections;

import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.Solver;

public class MostConstrainedVariableOrdering extends VariableOrdering {

	public MostConstrainedVariableOrdering(Solver pSolver)
	{
		super(pSolver);
	}
	
	//Most constrained = least values in domain
	//Theoretically, tie breakers should be on the most constraints with remaining variables
	//However, it is more efficient to not maintain the constraints up-to-date
	//Thus, tie-breakers will be decided based on the most original constraints
	//(Further tie-breakers will be decided based on Variable index, which should be unique)
	@Override
	public Variable order(ArrayList<Variable> pVars) {
		
		ArrayList<VCV> mOrder = new ArrayList<VCV>();
		for (Variable lV : pVars)
		{
			mOrder.add(new VCV(lV.mDomain.size(),lV.mConstraints.size(),lV));
		}
		
		return (Collections.max(mOrder).mVariable);
	}

}
