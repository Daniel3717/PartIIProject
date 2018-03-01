package daa38.CSP.Auxiliary;

public class UnreasonablyLongTimeException extends Exception {
	
	long mTimeStopped;
	
	public UnreasonablyLongTimeException(long pTimeStopped)
	{
		super();
		mTimeStopped = pTimeStopped;
	}
	
	public long getTimeStopped()
	{
		return mTimeStopped;
	}
	
	public void outputError()
	{
		System.out.println("Exceeded "+mTimeStopped+" nanoseconds");
	}
}
