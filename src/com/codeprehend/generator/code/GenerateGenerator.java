package com.codeprehend.generator.code;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.codeprehend.generator.utils.JavaTableEntity;


public class GenerateGenerator {
	
	private static List<String> strGenerator = new ArrayList<String>();
	private static LinkedHashSet<String> strRepetitiveLines = new LinkedHashSet<String>();
	
	
	
	public static List<String> generateGeneratorJavaClass(List<String> strReplaceV1, List<String> strReplaceV2, List<JavaTableEntity> tableEntities) {
		int [][] matrixRepetitiveLines = createMatrixRepetitiveLines(strReplaceV1);
		int [][] matrixRepetitiveBlocks = createMatrixRepetitiveBlocks(strReplaceV1);
		
		return strGenerator;
	}
	
	
	
	// CreataMatrixRepetetiveLines function returns a matrix with the lines having the format: (firstRepetitiveLine, lengthOfBlock).
	// Based on this matrix the generator function replaces the repetitive lines
	// A block with repetetiveLines needs to be delimited by empty lines or a line that do not have a variable name
	public static int[][] createMatrixRepetitiveLines(List<String> strReplaceV1){
		int [][] matrixRepetitiveLines  = new int [strReplaceV1.size()/2][strReplaceV1.size()/4];
		int indexForMatrix = 0;
		int indexForColumn;
		
		int i = 0;
		int j;
		int c = 0;
		String str1 = new String();
		
		i = 0;
		while(i < strReplaceV1.size()) {
			// str1 is used to go through each lines of strReplaceV1 and compare it with the next lines
			str1 = strReplaceV1.get(i);
			if (str1.contains("FieldName") || str1.contains("fieldName")) {
				j = i + 1;
				
				// Check if one of the next two line is the same. If yes, than the current and next line are part of a repetitive block.
				if (str1.equals(strReplaceV1.get(j)) || str1.equals(strReplaceV1.get(j+2))) {
					strRepetitiveLines.add(str1);
					
					// remember the number of the current line at position 0 in a new line of the matrix
					matrixRepetitiveLines[indexForMatrix][0] = i;
					
					indexForColumn = 2;
					c = 1;
					while (strReplaceV1.get(j).contains("FieldName") || strReplaceV1.get(j).contains("fieldName")) {
						// Line j is the block of repetitive lines, but has different format from the two first lines
						if ((strReplaceV1.get(j).length() - str1.length()) > 2 || !str1.substring(0, strReplaceV1.get(j).length()).equals(strReplaceV1.get(j))) {
							matrixRepetitiveLines[indexForMatrix][indexForColumn] = j;
							indexForColumn = indexForColumn + 1;
						}
						j = j + 1;
						c = c + 1;
					}
					
					matrixRepetitiveLines[indexForMatrix][1] = c;
					indexForMatrix = indexForMatrix + 1; 
					
					i = j;
				}
			}
			i = i + 1;
		}
		
		System.out.println("\nRepetetive lines: ");
		for (String str : strRepetitiveLines) {
			System.out.println(str);
		}
		
		int x = 0;
		int y = 0;
		while( x < matrixRepetitiveLines.length) {
			y = 0;
			while(matrixRepetitiveLines[x][y] != 0) {
				System.out.print(matrixRepetitiveLines[x][y]);
				System.out.print("; ");
				y = y + 1;
			}
			System.out.print("\n");
			if(matrixRepetitiveLines[x+1][0] == 0) {
				x = matrixRepetitiveLines.length;
			}
			x = x + 1;
		}
		
		return matrixRepetitiveLines;
	}
	
	// createMatrixREpetitiveBlocks returns a matrix with the lines having the format (startOfBlock1, startOfBlock2, ...., startOfBlock3)
	public static int[][] createMatrixRepetitiveBlocks(List<String> strReplaceV1){
		int [][] matrixRepetitiveBlocks  = new int [strReplaceV1.size()/2][strReplaceV1.size()/4];
		int indexForMatrix = 0;
		
		int i, j, k, len;
		int numberOfStarts;
		String str = new String();
		boolean bl, bl2;
		
		for(i = 0; i < strReplaceV1.size(); i++) {
			str = strReplaceV1.get(i);
			if(str.contains("FieldName") || str.contains("fieldName")){
				j = i + 1;
				if(str.equals(strReplaceV1.get(j))) {
					while(strReplaceV1.get(j).contains("FieldName") || strReplaceV1.get(j).contains("fieldName")) {
						j++;
					}
					i = j;
				} else {
					numberOfStarts = 1;
					bl2 = false;
					len = strReplaceV1.size();
					for(; j < strReplaceV1.size(); j++) {
						if(str.equals(strReplaceV1.get(j))) {
							bl = true;
							bl2 = true;
							for(k = 1; (k < j - i && k < len) && j + k < strReplaceV1.size(); k++) {
								if(!strReplaceV1.get(i + k).equals(strReplaceV1.get(j + k))) {
									bl = false;
									System.out.println("bl devine false cand compar " + String.valueOf(i + k) + String.valueOf(j + k));
								}
							}
							
							if(bl) {
								matrixRepetitiveBlocks[indexForMatrix][0] = i;
								matrixRepetitiveBlocks[indexForMatrix][numberOfStarts] = j;
								numberOfStarts++;
							}
							j = j + k - 1;
							len = k;
						}
					}
					
					if(bl2) {
						i = matrixRepetitiveBlocks[indexForMatrix][numberOfStarts - 1] + matrixRepetitiveBlocks[indexForMatrix][numberOfStarts - 1] + matrixRepetitiveBlocks[indexForMatrix][numberOfStarts - 2];
						indexForMatrix++;
					}
				}
			}
		}
		
		System.out.println("Repetitive blocks:");
		for (i = 0; i < indexForMatrix; i++) {
			j = 0;
			while(matrixRepetitiveBlocks[i][j] != 0) {
				System.out.print(matrixRepetitiveBlocks[i][j]);
				System.out.print("; ");
				j++;
			}
			System.out.print("\n");
		}
		
		return matrixRepetitiveBlocks;
	}

}
