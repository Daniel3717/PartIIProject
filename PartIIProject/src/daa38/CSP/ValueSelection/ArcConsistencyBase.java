 package daa38.CSP.ValueSelection;

import java.util.ArrayList;
import java.util.Collection;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.Auxiliary.VariablesRestrictions;
import daa38.CSP.Main.Solver;

public abstract class ArcConsistencyBase extends ValueSelection {

	
	
	private boolean consistentPair(Variable pVar1, Integer pVal1, Variable pVar2, Integer pVal2)
	{
		Collection<Constraint> lCons = pVar1.mConstraints.get(pVar2);
		
		if (lCons!=null)
		{
			for (Constraint lCon : lCons)
			{
				Integer lValFirst, lValSecond;
				if (lCon.mVariable1==pVar1)
				{
					lValFirst = pVal1;
					
					lValSecond = pVal2;
				}
				else
				{
					lValFirst = pVal2;
					
					lValSecond = pVal1;
				}
				
				if (!lCon.consistent(lValFirst,lValSecond))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean consistentTriple(Variable pVar1, Integer pVal1, Variable pVar2, Integer pVal2, Variable pVar3)
	{
		for (Integer lVal3 : pVar3.mDomain)
		{
			boolean lConsistent13 = true;
			boolean lConsistent23 = true;
			
			if (pVar3.mConstraints.get(pVar1)!=null)
			{
				for (Constraint lCon13 : pVar3.mConstraints.get(pVar1))
				{
					if (!lCon13.consistent(pVal1, lVal3))
					{
						lConsistent13 = false;
						break;
					}
				}
			}
			
			if (lConsistent13)
			{

				if (pVar3.mConstraints.get(pVar2)!=null)
				{
					for (Constraint lCon23 : pVar3.mConstraints.get(pVar2))			
					{
						if (!lCon23.consistent(pVal2, lVal3))
						{
							lConsistent23 = false;
							break;
						}
					}
				}
				
				if (lConsistent23)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected ArcConsistencyBase(Solver pSolver)
	{
		super(pSolver);
	}
	
	protected abstract int secondVariableStart(int pFirstVariableStart);
	protected abstract boolean repeatWhile(boolean pRestrictedSomething);
	
	@Override
	public void select(StepFrame pSF) {
		
		ArrayList<Variable> lVarsLeft = mSolver.mVarsLeft;
		
		Variable lNowVar = pSF.mVar;
		for (Integer lNowVal : lNowVar.mDomain)
		{
			VariablesRestrictions lVR = new VariablesRestrictions();
			boolean lFoundEmptyDomain = false;
			boolean lRestrictedSomething = true;
			do
			{
				lRestrictedSomething = false;
				for (int lIt1 = 0; lIt1 < lVarsLeft.size(); lIt1++)
				{
					Variable lVar1 = lVarsLeft.get(lIt1);
					Collection<Integer> lVar1Restrictions = new ArrayList<Integer>();
					for (Integer lVal1 : lVar1.mDomain)
					{
						if (consistentPair(lNowVar,lNowVal,lVar1,lVal1))
						{
							for (int lIt2 = secondVariableStart(lIt1); lIt2 < lVarsLeft.size(); lIt2++)
							{
								if (lIt2 == lIt1)
								{
									continue;
								}
								
								Variable lVar2 = lVarsLeft.get(lIt2);
								if (!consistentTriple(lNowVar,lNowVal,lVar1,lVal1,lVar2))
								{
									lVar1Restrictions.add(lVal1);
									break;
								}
							}
						}
						else
						{
							lVar1Restrictions.add(lVal1);
						}
					}
					
					if (lVar1Restrictions.size()>0)
					{
						lRestrictedSomething = true;
						lVR.addRestrictions(lVar1, lVar1Restrictions);
						
						for (Integer lInt : lVar1Restrictions)
						{
							lVar1.mDomain.remove(lInt);
						}
						
						if (lVar1.mDomain.size()==0)
						{
							lFoundEmptyDomain=true;
							break;
						}
					}
				}
				
			}
			while ((repeatWhile(lRestrictedSomething))&&(!lFoundEmptyDomain));
			
			lVR.liftRestrictions();
			
			if (!lFoundEmptyDomain)
			{
				pSF.mValsToGo.add(lNowVal);
				pSF.mRes.add(lVR);
			}
		}
		
		pSF.mNowValIndex = 0;
	}

}
