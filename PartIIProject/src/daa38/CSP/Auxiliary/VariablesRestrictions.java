package daa38.CSP.Auxiliary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VariablesRestrictions {
	private HashMap<Variable, Collection<Integer> > mVRs;
	
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
				{
					lBadEnforce = true;
				}
			}
		}
		
		if (lBadEnforce)
		{
			//ERROR:
			System.out.println("Bad enforce");
		}
		
		return (lBadEnforce);
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
				{
					lBadLift = true;
				}
				else
				{	
					lVar.mDomain.add(lInt);
				}
			}
		}
		
		if (lBadLift)
		{
			//ERROR:
			System.out.println("Bad lift");
		}
		
		return (lBadLift);
	}
	
	//Overrides current restrictions for pVar
	//pRestrictions must be non-zero
	public void overrideRestrictions(Variable pVar, Collection<Integer> pRestrictions)
	{
		if (pRestrictions.size()==0)
		{
			//ERROR:
			System.out.println("Tried to add a zero-sized Collection of Restrictions "
					+ "with VariablesRestrictions.overrideRestrictions");
		}
		
		mVRs.put(pVar, pRestrictions);
	}
	
	//Adds to/creates a restriction for pVar
	public void addRestriction(Variable pVar, Integer pRestriction)
	{
		Collection<Integer> lRestrictions = mVRs.get(pVar);
		if (lRestrictions==null)
		{
			lRestrictions = new ArrayList<Integer>();
			mVRs.put(pVar, lRestrictions);
		}
		lRestrictions.add(pRestriction);
	}
	
	//Adds to/creates restrictions for pVar
	//pRestrictions must be non-zero in size
	//It's more efficient than calling addRestriction multiple times since
	//we have only one mVRs.get call
	public void addRestrictions(Variable pVar, Collection<Integer> pRestrictions)
	{
		if (pRestrictions.size()==0)
		{
			//ERROR:
			System.out.println("Tried to add a zero-sized Collection of Restrictions "
					+ "with VariablesRestrictions.addRestrictions");
		}
		
		
		Collection<Integer> lRestrictions = mVRs.get(pVar);
		if (lRestrictions==null)
		{
			lRestrictions = new ArrayList<Integer>();
			mVRs.put(pVar, lRestrictions);
		}
		
		for (Integer lInt : pRestrictions)
		{
			lRestrictions.add(lInt);
		}
		
	}
	
	public Collection<Integer> getVarRestrictions(Variable pVar)
	{
		return mVRs.get(pVar);
	}
	
	public Map<Variable, Collection<Integer> > getAllRestrictions()
	{
		return mVRs;
	}
}
