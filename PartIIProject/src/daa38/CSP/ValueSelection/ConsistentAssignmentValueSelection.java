package daa38.CSP.ValueSelection;

import java.util.ArrayList;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.PairInts;
import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Auxiliary.VariablesRestrictions;

public class ConsistentAssignmentValueSelection implements ValueSelection {

	@Override
	public void process(StepFrame pSF) {
		Variable lNowVar = pSF.mVarsToGo.get(pSF.mNowVarIndex);
		
		int lMaxValue = 0;
		for (Integer lInt : lNowVar.mDomain)
		{
			if (lInt>lMaxValue)
				lMaxValue = lInt;
		}
		
		ArrayList< VariablesRestrictions > lVR = new ArrayList< VariablesRestrictions >();
		for (int i=0;i<=lMaxValue;i++)
			lVR.add(new VariablesRestrictions());
		
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
			
			//ArrayList<Integer> lValRes = new ArrayList<Integer>();
			for (PairInts lPI : lCon.mValues)
			{
				//no, this doesn't quite work. It needs to get separated by values.
				//lValRes.add(lPI.getAtIndex((2-lNowIndex)));
				
				//I'll use the slower one
				if (lPI.getAtIndex(lNowIndex)<=lMaxValue)
				{
					if (lOtherVar.mDomain.contains(lPI.getAtIndex(3-lNowIndex)))
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