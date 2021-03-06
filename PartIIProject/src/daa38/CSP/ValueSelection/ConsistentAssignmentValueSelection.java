package daa38.CSP.ValueSelection;

import java.util.Collection;
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
		HashMap<Integer, VariablesRestrictions> lMapValToVR = new HashMap<Integer, VariablesRestrictions>();
		
		for (Integer lInt : pVar.mDomain)
		{
			lMapValToVR.put(lInt, new VariablesRestrictions());
		}
		
		
		for (Collection<Constraint> lConColl : pVar.mConstraints.values())
		{
			for (Constraint lCon : lConColl)
			{
				int lIndex = lCon.getIndex(pVar);
				Variable lOtherVar = lCon.otherVar(pVar);
				int lOtherIndex = lCon.getIndex(lOtherVar);
				
				if (lOtherVar.mValue == null)
				{
					for (PairInts lPI : lCon.mValues)
					{
						VariablesRestrictions lNowVR = lMapValToVR.get(lPI.getAtIndex(lIndex));
						//First check the HashMap, which is faster than whatever Collection is at lOtherVar.mDomain
						if (lNowVR != null) 
						{
							//this means that lPI.getAtIndex(lIndex) is a value in pVar's domain
							//I'm using it with the HashMap since I have no guarantees on what pVar's domain is
							if (lOtherVar.mDomain.contains(lPI.getAtIndex(lOtherIndex)))
							{
								lNowVR.addRestriction(lOtherVar, lPI.getAtIndex(lOtherIndex));
							}
						}
					}
				}
				
			}
		}
		
		return lMapValToVR;
		
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
