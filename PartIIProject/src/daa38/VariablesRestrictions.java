package daa38;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VariablesRestrictions {
	public HashMap<Variable, Collection<Integer> > mVRs;
	
	public VariablesRestrictions()
	{
		mVRs = new HashMap<Variable, Collection<Integer> >();
	}
	
	// Returns false if it attemps to remove a value from the domain of a variable and fails
	// Theoretically should never return false. Will implement error throwing later on this one.
	public boolean enforceRestrictions()
	{
		boolean lBadEnforce = false;
		for (Map.Entry<Variable, Collection<Integer> > lVR : mVRs.entrySet())
		{
			Variable lVar = lVR.getKey();
			for (Integer lInt : lVR.getValue())
			{
				if (!lVar.mDomain.remove(lInt))
					lBadEnforce = true;
			}
		}
		
		return (!lBadEnforce);
	}
	
	// POSSIBLE TIME EFFICIENCY IMPROVEMENT IF I DO NOT CHECK WHETHER THE DOMAIN CONTAINS THE VALUE
	// Returns false if it attemps to add a value to a domain, but the domain already contains that value.
	// Theoretically should never return false. Will implement error throwing later on this one.
	public boolean liftRestrictions()
	{
		boolean lBadLift = false;
		for (Map.Entry<Variable, Collection<Integer> > lVR : mVRs.entrySet())
		{
			Variable lVar = lVR.getKey();
			for (Integer lInt : lVR.getValue())
			{
				if (lVar.mDomain.contains(lInt))
					lBadLift = true;
				else
					lVar.mDomain.add(lInt);
			}
		}
		
		return (!lBadLift);
	}
	
	//Overrides current restrictions for pVar
	public void addRestrictions(Variable pVar, Collection<Integer> pRestrictions)
	{
		mVRs.put(pVar, pRestrictions);
	}
	
	//Adds to/creates restrictions for pVar
	public void addRestriction(Variable pVar, Integer pRestriction)
	{
		Collection<Integer> lRestrictions = mVRs.get(pVar);
		if (lRestrictions==null)
		{
			ArrayList<Integer> lNewRestrictions = new ArrayList<Integer>();
			lNewRestrictions.add(pRestriction);
			mVRs.put(pVar, lNewRestrictions);
		}
		else
		{
			lRestrictions.add(pRestriction);
		}
	}
}