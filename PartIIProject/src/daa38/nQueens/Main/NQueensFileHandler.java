package daa38.nQueens.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class NQueensFileHandler {
	
	public static void writeNQueensSolution(String pPath, int[][] pMatrix) throws IOException
	{
		Writer lProblemOutput = new FileWriter(new File(pPath));
		
		int lSize = pMatrix.length;
		
		lProblemOutput.write(lSize+"\r\n");
		
		for (int lLine = 0; lLine < lSize; lLine++)
		{
			String lStringLine ="";
			for (int lColumn = 0; lColumn < lSize; lColumn++)
			{
				lStringLine += pMatrix[lLine][lColumn]+" ";
			}
			lStringLine+="\r\n";
			lProblemOutput.write(lStringLine);
		}
		
		lProblemOutput.close();
	}
	
	public static int[][] readNQueensSolution(String pPath) throws FileNotFoundException
	{
		Scanner lNQueensInput = new Scanner(new File(pPath));
		
		int lSize = lNQueensInput.nextInt();
		int[][] lMatrix = new int[lSize][lSize];
		
		for (int lLine = 0; lLine<lSize; lLine++)
		{
			for (int lColumn = 0; lColumn<lSize; lColumn++)
			{
				lMatrix[lLine][lColumn] = lNQueensInput.nextInt();
			}
		}
		
		lNQueensInput.close();
		
		return lMatrix;
	}
	
	public static boolean nQueensCheckFile(String pPath) throws FileNotFoundException
	{
		int[][] lMatrix = readNQueensSolution(pPath);
		return NQueens.nQueensCheckMatrix(lMatrix);
	}
}
