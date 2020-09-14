package com.sami.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sami.Client.FileTransferClient;

public class ThreadHandler implements Runnable {

	public byte[] byte_array;
	public Socket connection = null;
	FileRecieveServer serverClass;
	public DataInputStream ois = null;
	public DataOutputStream oos = null;
	public FileInputStream fis = null;
	public FileOutputStream fos = null;
	String fileName;
	String directory = "./resources/ServerFiles/";

	public ThreadHandler(Socket connection, FileRecieveServer server, String fileName) {
		System.out.println("New thread started");
		this.connection = connection;
		this.serverClass = server;
		this.fileName = fileName;
		try {
			ois = new DataInputStream(connection.getInputStream());
			oos = new DataOutputStream(connection.getOutputStream());
			System.out.println("Thread Handler started in port " + connection.getPort());
		} catch (Exception ex) {
			Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private String calculateCheckSum(File file2) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("");
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

			FileInputStream finput = new FileInputStream(file2);
			byte[] dataBytes = new byte[1024];

			int bytesRead = 0;

			while ((bytesRead = finput.read(dataBytes)) != -1) {
				messageDigest.update(dataBytes, 0, bytesRead);
			}

			byte[] digestBytes = messageDigest.digest();

			for (int i = 0; i < digestBytes.length; i++) {
				sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			System.out.println("file check sum value is " + sb.toString());
			finput.close();
		} catch (Exception ex) {
			// TODO: handle exception
			Logger.getLogger(FileTransferClient.class.getName()).log(Level.SEVERE, null, ex);
		}

		return sb.toString();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			byte[] bytes = new byte[1024];
			int bytesRead;
			String fileName = ois.readUTF();
			OutputStream output = new FileOutputStream(directory+fileName);
			String receivedCheckSum = ois.readUTF();
			//long size = ois.readLong();
			byte[] buffer = new byte[1024];
			while ((bytesRead = ois.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
				//size -= bytesRead;
			}

			String checksumVal = calculateCheckSum(new File(directory + fileName));
			System.out.println("Server Side check sum value is " + checksumVal);

			// String receivedCheckSum=(String)ois.readObject();
			System.out.println("client side checksum " + receivedCheckSum);
			if (receivedCheckSum.equalsIgnoreCase(checksumVal)) {
				System.out.println("*****************Data is transfer without any error *********************");
			} else {
				System.out.println("Integrity verification failed !");
			}
			// }

			oos.close();
			ois.close();
		} catch (Exception ex) {
			// TODO: handle exception
			Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
