package com.sami.Client;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class ConcurrencyHandler {

	private static Integer concurrency = 1;
	private static String clientDirectory = "./resources/ClientFiles/";


	public static Integer getPageCount(Integer concurrency) {
		File folder = new File(clientDirectory);
		File[] listOfFiles = folder.listFiles();
		Integer result = 1;
		if (listOfFiles.length < concurrency) {
			result = ((Integer) listOfFiles.length / concurrency) + 1;
		} else {
			result = ((Integer) listOfFiles.length / concurrency);
		}
		return result;
	}

	public static File[] getFiles(Integer pageNumber, Integer concurrency) {
		File folder = new File(clientDirectory);
		File[] listOfFiles = folder.listFiles();
		Integer start = (pageNumber - 1) * concurrency;
		Integer end = pageNumber * concurrency;
		return Arrays.copyOfRange(listOfFiles, start, end);
	}

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Please enter concurrency: ");
		Integer concurrency = 1;

		try {
			concurrency = scanner.nextInt();
		} catch (Exception exception) {
			concurrency = 1;
		}
		long start = System.currentTimeMillis();
		Integer pages = getPageCount(concurrency);
		for (Integer i = 1; i <= pages; i++) {
			try {
				CountDownLatch latch = new CountDownLatch(concurrency);
				File[] listOfFiles = getFiles(i, concurrency);
				for (File file : listOfFiles) {
					Runnable runnable = new FileTransferClient(5050, 1, "127.0.0.1", file, latch);
					Thread thread = new Thread(runnable);
					thread.start();
				}
				latch.await();
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		long transferTime = (end - start) / 1000;
		System.out.println("Transfer Time concurrency in second " + concurrency.toString() + " is:: " + Long.toString(transferTime));
	}

}
