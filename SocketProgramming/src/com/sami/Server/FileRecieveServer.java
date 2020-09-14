package com.sami.Server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileRecieveServer implements Runnable {

	public static ServerSocket serverSocket = null;
	public static Socket connection = null;
	public int Counter = 1;
	public int serverPortNO;
	public ThreadHandler handler;

	public FileRecieveServer(int pNo) {
		this.serverPortNO = pNo;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			serverSocket = new ServerSocket(serverPortNO);
			System.out.println("Server Started\n");
			Integer count = 0;
			while (true) {
				try {
					count++;
					waitForConnection(count);
				} catch (Exception eofException) {
					System.out.println("\nServer terminated connection " + eofException);
				} finally {
					Counter++;
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(FileRecieveServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void waitForConnection(Integer count) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Waiting for connection\n");
		System.out.println("Received a request, downloading file in background! ");
		connection = serverSocket.accept();
		System.out.println("Connection received from " + connection.getInetAddress());
		System.out.println("Connected to a client\n");
		handler = new ThreadHandler(connection, this, count.toString());
		Thread threadHandler = new Thread(handler);
		threadHandler.start();
		System.out.println("Connection " + (Counter++) + " received from: " + connection.getInetAddress().getHostAddress() + "\n");
	}

	public static void main(String[] args) {
		FileRecieveServer frs = new FileRecieveServer(5050);
		frs.run();
	}
}
