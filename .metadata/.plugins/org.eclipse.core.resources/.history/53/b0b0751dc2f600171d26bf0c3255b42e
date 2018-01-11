package daa38.CSP.ValueSelection;

import java.util.ArrayList;
import java.util.HashMap;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.PairInts;
import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Auxiliary.VariablesRestrictions;

public class ConsistentAssignmentValueSelection implements ValueSelection {

	@Override
	public void select(StepFrame pSF) {
		Variable lNowVar = pSF.mVar;
		
		HashMap<Integer, VariablesRestrictions> lVR = new HashMap<Integer, VariablesRestrictions>();
		
		for (Integer lInt : lNowVar.mDomain)
		{
			lVR.put(lInt, new VariablesRestrictions());
		}
		
		for (Constraint lCon : lNowVar.mConstraints)
		{
			int lNowIndex=0;
			Variable lOtherVar;
			if (lCon.mVariable1==lNowVar)
			{
				lNowIndex = 1;
				lOtherVar = lCon.mVariable2;
			}
			else
			{
				lNowIndex = 2;
				lOtherVar = lCon.mVariable1;
			}
			
			for (PairInts lPI : lCon.mValues)
			{
				if (lOtherVar.mDomain.contains(lPI.getAtIndex(3-lNowIndex)))
				{
					if (lVR.get(lPI.getAtIndex(lNowIndex))!=null)
						lVR.get(lPI.getAtIndex(lNowIndex)).addRestriction(lOtherVar, lPI.getAtIndex(3-lNowIndex));
				}
			}
			
		}
		
		for (Integer lInt : lNowVar.mDomain)
		{
			pSF.mValsToGo.add(lInt);
			pSF.mRes.add(lVR.get(lInt));
		}
		
		pSF.mNowValIndex = 0;
	}

}
