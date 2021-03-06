package daa38.MapColouring.Auxiliary;

public class MatrixPosition {
	public final int mRow,mCol;
	
	public MatrixPosition(int pRow, int pCol)
	{
		mRow = pRow;
		mCol = pCol;
	}
	
	public MatrixPosition up()
	{
		return new MatrixPosition( mRow-1 , mCol   );
	}
	
	public MatrixPosition left()
	{
		return new MatrixPosition( mRow   , mCol-1 );
	}
	
	public MatrixPosition down()
	{
		return new MatrixPosition( mRow+1 , mCol   );
	}
	
	public MatrixPosition right()
	{
		return new MatrixPosition( mRow   , mCol+1 );
	}
}
