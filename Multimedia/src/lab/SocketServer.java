package lab;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class SocketServer {

	private static ServerSocket server;
	private static int port = 5000;
	private Hashtable<Integer, Student> students;

	public void startServer() throws IOException, ClassNotFoundException {

		students = new Hashtable<Integer, Student>();

		server = new ServerSocket(port);
		while (true) {
			System.out.println("Listening for client requests");
			Socket socket = server.accept();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String msgReceived = (String) ois.readObject();
			System.out.println("Message Received: " + msgReceived);
			String msgReply = "";
			String[] msgParts = msgReceived.split("#"); 
			if (msgParts[0].equals("LIST")) {
				msgReply = "LIST#" + getStudentsData();
			} else if (msgParts[0].equals("ADD")) {
				int am = Integer.parseInt(msgParts[1]);
				String name = msgParts[2];
				Student std =  new Student(am,name);
				students.put(am, std);
				msgReply = "ADD_OK";
			} else if (msgParts[0].equals("DEL")) {
				int am = Integer.parseInt(msgParts[1]);
				students.remove(am);
				msgReply = "DEL_OK";
			} else if (msgParts[0].equals("EXIT")) {
				msgReply = "EXIT_OK";
			} 
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(msgReply);
			ois.close();
			oos.close();
			socket.close();
			if (msgReply.equals("EXIT_OK")) break;
		}
		System.out.println("Closing Socket server");
		server.close();
	}

	private String getStudentsData() {
		String data = "";
		Set<Integer> setOfStudents = students.keySet();
		Iterator<Integer> iterator = setOfStudents.iterator();
		while (iterator.hasNext()) {
			Integer key = iterator.next();
			data = data + students.get(key).getData();
		}

		return data;
	}

	public static void main(String args[]) {
		SocketServer server = new SocketServer();
		try {
			server.startServer();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
