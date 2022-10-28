
import java.io.*;
import java.net.*;

public class ServerHost
{
	ServerSocket socket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	Server s;

	public static void main(String args[])
	{
		ServerHost server = new ServerHost();
		while (true)
		{
			server.run();
		}
	}

	ServerHost()
	{
	}

	void run()
	{
		try
		{
			// 1. creating a server socket
			socket = new ServerSocket(2004, 10);
			// 2. Wait for connection
			while (true)
			{
				System.out.println("Waiting for connection");
				connection = socket.accept();
				System.out.println("Connection received from " + connection.getInetAddress().getHostName());
				s = new Server(connection);
				s.start();
			}
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
		finally
		{
			// 4: Closing connection
			try
			{
				in.close();
				out.close();
				socket.close();
			}
			catch (IOException ioException)
			{
				ioException.printStackTrace();
			}
		}
	}

	void sendMessage(String msg)
	{
		try
		{
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

}
