package daa38.CSP.ValueSelection;

import daa38.CSP.Main.Solver;

public class PartialLookAhead extends LookAhead {

	public PartialLookAhead(Solver pSolver)
	{
		super(pSolver);
	}
	
	@Override
	protected int secondVariableStart(int pFirstVariableStart) {
		return pFirstVariableStart+1;
	}

}
