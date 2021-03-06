package daa38.CSP.ValueSelection;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Auxiliary.VariablesRestrictions;
import daa38.CSP.Main.Solver;

public class ForwardChecking extends ConsistentAssignmentValueSelection {

	public ForwardChecking(Solver pSolver)
	{
		super(pSolver);
	}
	
	@Override
	public void select(StepFrame pSF) {
		Variable lNowVar = pSF.mVar;
		
		Map<Integer, VariablesRestrictions> lAllVR = super.createVariablesRestrictions(lNowVar);
		
		for (Entry<Integer, VariablesRestrictions> lEntryAllVR : lAllVR.entrySet())
		{
			Integer lInt = lEntryAllVR.getKey();
			VariablesRestrictions lOneVR = lEntryAllVR.getValue();
			Map<Variable, Collection<Integer> > lVarToRes = lOneVR.getAllRestrictions();
			
			boolean lFoundEmptyDomain = false;
			for (Entry<Variable, Collection<Integer> > lEntryVarToRes : lVarToRes.entrySet())
			{
				if (lEntryVarToRes.getKey().mDomain.size() == lEntryVarToRes.getValue().size())
				{
					lFoundEmptyDomain = true;
					break;
				}
			}
			
			if (!lFoundEmptyDomain)
			{
				pSF.mValsToGo.add(lInt);
				pSF.mRes.add(lOneVR);
			}
			
		}
		
		pSF.mNowValIndex=0;

	}

}
