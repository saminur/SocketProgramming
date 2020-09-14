package com.sami.Client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.security.MessageDigest;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileTransferClient implements Runnable {

	public int portNumber;
	public String IPAdress;
	public int ClientID;
	public Socket ClientSocket;

	CountDownLatch latch;
	public DataInputStream ois = null;
	public DataOutputStream oos = null;
	public FileOutputStream fos = null;
	public FileInputStream fis = null;
	public File file;

	public FileTransferClient(int portnumber, int ClientID, String IP, File file, CountDownLatch latch) {
		this.portNumber = portnumber;
		this.ClientID = ClientID;
		this.IPAdress = IP;
		this.file = file;
		this.latch = latch;

	}

	private void connectToServer() {
		// TODO Auto-generated method stub
		try {
			this.ClientSocket = new Socket(IPAdress, portNumber);
			getStream();
			System.out.println("Stream Received");
			System.out.println("Sending file to server");

			if (this.file.isFile()) {
				sendFile();
				ois.close();
				oos.close();
				this.ClientSocket.close();
				this.latch.countDown();
			}

		} catch (IOException ex) {
			Logger.getLogger(FileTransferClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void sendFile() {
		// TODO Auto-generated method stub
		try {
			String checksum = calculateCheckSum(this.file);
			byte[] byte_array = new byte[1024];
			FileInputStream fis = new FileInputStream(this.file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ois = new DataInputStream(bis);

			int byteLength;
			oos.writeUTF(this.file.getName());
			oos.writeUTF(checksum);
			while ((byteLength = ois.read(byte_array)) > 0) {
				oos.write(byte_array, 0, byteLength);
			}
			oos.flush();
			System.out.println("File sending to server");
		} catch (Exception ex) {
			// TODO: handle exception
			Logger.getLogger(FileTransferClient.class.getName()).log(Level.SEVERE, null, ex);
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

	public void getStream() {
		try {
			oos = new DataOutputStream(ClientSocket.getOutputStream());
			ois = new DataInputStream(ClientSocket.getInputStream());
		} catch (IOException ex) {
			Logger.getLogger(FileTransferClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Client is starting for sending file cuncurrently:");
		connectToServer();
	}
}
