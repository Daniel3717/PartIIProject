package daa38.CSP.LookBack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Main.Solver;

public class GraphBasedBackjumping extends LookBack {

	Map<Variable, Collection<Variable>> mVarToDeadEnds;
	
	public GraphBasedBackjumping(Solver pSolver)
	{
		super(pSolver);
		mVarToDeadEnds = new HashMap<Variable, Collection<Variable> >();
	}
	
	@Override
	public int jump(ArrayList<StepFrame> pSteps, int pIndex) {
		
		//DEBUG:
		//System.out.println("At step "+pIndex+" the variable "+pSteps.get(pIndex).mVar.mName+" is a dead-end");
		
		Variable lNowVar = pSteps.get(pIndex).mVar;
		
		Collection<Variable> lDeadEnds = mVarToDeadEnds.get(lNowVar);
		
		if (lDeadEnds == null)
		{
			lDeadEnds = new HashSet<Variable>();
			mVarToDeadEnds.put(lNowVar, lDeadEnds);
		}

		lDeadEnds.add(lNowVar);
		
		boolean lFoundRestriction = false;
		StepFrame lNowFrame = pSteps.get(pIndex);
		
		while (!lFoundRestriction)
		{
			mVarToDeadEnds.remove(lNowFrame.mVar);
			lNowFrame.resetFrame();
			
			pIndex--;
			
			if (pIndex==-1)
				return pIndex;
			
			lNowFrame = pSteps.get(pIndex);
			for (Variable lV : lDeadEnds)
			{
				if (lNowFrame.restrictsVariable(lV))
				{
					lFoundRestriction = true;
				}
			}
		}
		
		Variable lJumpVar = lNowFrame.mVar;
		Collection<Variable> lJumpVarDeadEnds = mVarToDeadEnds.get(lJumpVar);
		
		if (lJumpVarDeadEnds==null)
		{
			lJumpVarDeadEnds = new HashSet<Variable>();
			mVarToDeadEnds.put(lJumpVar, lJumpVarDeadEnds);
		}
		
		for (Variable lV : lDeadEnds)
		{
			lJumpVarDeadEnds.add(lV);
		}
		
		lNowFrame.removeValue();
		lNowFrame.mNowValIndex++;
		
		//DEBUG:
		//System.out.println("Jumped to step "+pIndex+" to variable "+lNowFrame.mVar.mName);
		
		return pIndex;
	}

}
