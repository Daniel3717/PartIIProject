package daa38.Statistics.Auxiliary;

public class SyncMemoryConsumptionTracker {
	private long mTotalBytesUsed;
	private long mMaxBytesUsed;
	private boolean mTracking;
	
	public SyncMemoryConsumptionTracker()
	{
		mTotalBytesUsed = 0;
		mMaxBytesUsed = 0;
		mTracking = true;
	}
	
	public synchronized void addBytes(long pBytes)
	{
		//System.out.println("Add bytes called with "+pBytes);
		if (mTracking)
			mTotalBytesUsed += pBytes;
		//else
		//	System.out.println("But we finished tracking");
	}
	
	public synchronized void maxBytes(long pBytes)
	{
		//System.out.println("Max bytes called with "+pBytes);
		if (mTracking)
			if (pBytes > mMaxBytesUsed)
				mMaxBytesUsed = pBytes;
	}
	
	public synchronized void endTracking()
	{
		long lBytesAtEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		//System.out.println("End tracking called. Adding "+lBytesAtEnd);
		mTotalBytesUsed += lBytesAtEnd;
		if (lBytesAtEnd > mMaxBytesUsed)
			mMaxBytesUsed = lBytesAtEnd;
		mTracking = false;
	}
	
	public synchronized long getTotalBytesUsed()
	{
		return mTotalBytesUsed;
	}
	
	public synchronized long getMaxBytesUsed()
	{
		return mMaxBytesUsed;
	}
	
}
