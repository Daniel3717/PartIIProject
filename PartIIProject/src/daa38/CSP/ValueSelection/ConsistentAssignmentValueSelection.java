package daa38.CSP.ValueSelection;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.PairInts;
import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Auxiliary.VariablesRestrictions;
import daa38.CSP.Main.Solver;

public class ConsistentAssignmentValueSelection extends ValueSelection {
	
	public ConsistentAssignmentValueSelection(Solver pSolver)
	{
		super(pSolver);
	}
	
	protected Map<Integer, VariablesRestrictions> createVariablesRestrictions(Variable pVar)
	{
		HashMap<Integer, VariablesRestrictions> lVR = new HashMap<Integer, VariablesRestrictions>();
		
		for (Integer lInt : pVar.mDomain)
		{
			lVR.put(lInt, new VariablesRestrictions());
		}
		
		for (Constraint lCon : pVar.mConstraints)
		{
			int lNowIndex=0;
			Variable lOtherVar;
			if (lCon.mVariable1==pVar)
			{
				lNowIndex = 1;
				lOtherVar = lCon.mVariable2;
			}
			else
			{
				lNowIndex = 2;
				lOtherVar = lCon.mVariable1;
			}
			
			if (lOtherVar.mValue == null)
			{
				for (PairInts lPI : lCon.mValues)
				{
					if (lOtherVar.mDomain.contains(lPI.getAtIndex(3-lNowIndex)))
					{
						if (lVR.get(lPI.getAtIndex(lNowIndex))!=null)
							lVR.get(lPI.getAtIndex(lNowIndex)).addRestriction(lOtherVar, lPI.getAtIndex(3-lNowIndex));
					}
				}
			}
			
		}
		
		return lVR;
		
	}
	
	@Override
	public void select(StepFrame pSF) {
		Variable lNowVar = pSF.mVar;
		
		Map<Integer, VariablesRestrictions> lVR = createVariablesRestrictions(lNowVar);
		
		for (Entry<Integer, VariablesRestrictions> lEntry : lVR.entrySet())
		{
			pSF.mValsToGo.add(lEntry.getKey());
			pSF.mRes.add(lEntry.getValue());
		}
		
		pSF.mNowValIndex = 0;
	}

}
