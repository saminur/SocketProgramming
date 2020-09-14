package com.sami.createFile;

import java.io.RandomAccessFile;

public class DataSetCreationGB {
	public static String directory = "./resources/ClientFiles/";


	public static void main(String args[]) throws Exception {
		createDataset(10, 1024);
	}

	private static void createDataset(int filenumber, int filesize) throws Exception {
		// TODO Auto-generated method stub
		for (int i = 1; i <= filenumber; i++) {
			RandomAccessFile file = new RandomAccessFile(directory + "dataset" + i, "rw");
			file.setLength(1024 * 1024 * filesize);
		}
	}
}
