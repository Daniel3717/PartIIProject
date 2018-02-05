package daa38.CSP.ValueSelection;

import daa38.CSP.Main.Solver;

public class FullLookAhead extends LookAhead {

	public FullLookAhead(Solver pSolver)
	{
		super(pSolver);
	}
	
	@Override
	protected int secondVariableStart(int pFirstVariableStart) {
		return 0;
	}

}
