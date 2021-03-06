package daa38.Statistics.Auxiliary;

import daa38.CSP.LookBack.Backtrack;
import daa38.CSP.LookBack.GaschnigsBackjumping;
import daa38.CSP.LookBack.GraphBasedBackjumping;
import daa38.CSP.LookBack.LookBack;
import daa38.CSP.Main.Solver;
import daa38.CSP.ValueSelection.ArcConsistency;
import daa38.CSP.ValueSelection.ConsistentAssignmentValueSelection;
import daa38.CSP.ValueSelection.ForwardChecking;
import daa38.CSP.ValueSelection.FullLookAhead;
import daa38.CSP.ValueSelection.PartialLookAhead;
import daa38.CSP.ValueSelection.ValueSelection;
import daa38.CSP.VariableOrdering.LeastConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.MostConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.RandomVariableOrdering;
import daa38.CSP.VariableOrdering.VariableOrdering;

public class InputProcessing {
	
	//Requires Solver to be passed due to VariableOrdering constructor
	public static VariableOrdering intToVO(int pOrder, Solver pSolver)
	{
		VariableOrdering lVO = new RandomVariableOrdering(pSolver);
		
		if (pOrder == 1)
			lVO = new MostConstrainedVariableOrdering(pSolver);

		if (pOrder == 2)
			lVO = new LeastConstrainedVariableOrdering(pSolver);
		
		return lVO;
	}

	//Requires Solver to be passed due to ValueSelection constructor
	public static ValueSelection intToVS(int pSelect, Solver pSolver)
	{
		ValueSelection lVS = new ConsistentAssignmentValueSelection(pSolver);
		
		if (pSelect == 1)
			lVS = new ForwardChecking(pSolver);
		
		if (pSelect == 2)
			lVS = new FullLookAhead(pSolver);
		
		if (pSelect == 3)
			lVS = new ArcConsistency(pSolver);
		
		if (pSelect == 4)
			lVS = new PartialLookAhead(pSolver);
		
		return lVS;
	}

	//Requires Solver to be passed due to LookBack constructor
	public static LookBack intToLB(int pBack, Solver pSolver)
	{
		LookBack lLB = new Backtrack(pSolver);
		
		if (pBack == 1)
			lLB = new GaschnigsBackjumping(pSolver);
		
		if (pBack == 2)
			lLB = new GraphBasedBackjumping(pSolver);
		
		return lLB;
	}
}
