package daa38.MapColouring.Auxiliary;

public class IntMatrixElement {

	private final int[][] mMatrix;
	private final MatrixPosition mPos;
	
	public IntMatrixElement(int[][] pMatrix, MatrixPosition pPos)
	{
		mMatrix=pMatrix;
		mPos = pPos;
	}
	
	public IntMatrixElement up()
	{
		return new IntMatrixElement(mMatrix,mPos.up()    );
	}

	public IntMatrixElement left()
	{
		return new IntMatrixElement(mMatrix,mPos.left()  );
	}

	public IntMatrixElement down()
	{
		return new IntMatrixElement(mMatrix,mPos.down()  );
	}

	public IntMatrixElement right()
	{
		return new IntMatrixElement(mMatrix,mPos.right() );
	}
	
	public boolean isInsideMatrix()
	{
		if (mPos.mRow>=mMatrix.length)
			return false;
		if (mPos.mRow<0)
			return false;
		
		if (mPos.mCol>=mMatrix[mPos.mRow].length)
			return false;
		if (mPos.mCol<0)
			return false;
		
		return true;
	}
	
	public int get()
	{
		return mMatrix[mPos.mRow][mPos.mCol];
	}
	
	public void set(int pInt)
	{
		mMatrix[mPos.mRow][mPos.mCol] = pInt;
	}
}
