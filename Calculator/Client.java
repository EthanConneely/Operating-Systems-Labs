import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	Scanner input;

	public static void main(String args[])
	{
		System.out.println("\n\n-- Client --\n\n");

		// Create and run the client
		Client client = new Client();
		client.run();
	}

	Client()
	{
		input = new Scanner(System.in);
	}

	void run()
	{
		try
		{
			// 1. creating a socket to connect to the server
			socket = new Socket();

			// This was not done in the class just something i add that will make it so if
			// you launch the client first it wont crash if ther server is not running
			int count = 1;
			while (!socket.isConnected())
			{
				try
				{
					socket.connect(new InetSocketAddress("localhost", 2004), 500);
				}
				catch (Exception e)
				{
				}

				Thread.sleep(1000);
				System.out.println("Reconnection Attempt - " + count);
				count++;
			}

			System.out.println("Connected to localhost in port 2004");

			// 2. get Input and Output streams
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

			handleLogic();
		}
		catch (UnknownHostException unknownHost)
		{
			System.out.println("You are trying to connect to an unknown host!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
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

	private void handleLogic() throws UnknownHostException, IOException, Exception
	{
		while (true)
		{
			// Handle the add or multiply request
			handleRequest();

			// Change the way we handle the incoming messages from the server
			if (message.equalsIgnoreCase("1"))
			{
				handleAdd();
			}
			else if (message.equalsIgnoreCase("2"))
			{
				handleMul();
			}

			// Handle the quit request
			handleRequest();

			if (message.equalsIgnoreCase("yes"))
			{
				break;// close out of the connection and the infinite loop
			}
		}
	}

	private void handleRequest() throws Exception
	{
		readResponse();
		sendInput();
	}

	private void handleAdd() throws Exception
	{
		for (int i = 1; i <= 3; i++)
		{
			handleRequest();
		}

		// Read Result response
		readResponse();
	}

	private void sendInput()
	{
		message = input.nextLine();
		sendMessage(message);
	}

	private void readResponse() throws Exception
	{
		message = (String) in.readObject();
		System.out.println(message);
	}

	private void handleMul() throws Exception
	{
		for (int i = 1; i <= 2; i++)
		{
			handleRequest();
		}

		// Read Result response
		readResponse();
	}

	void sendMessage(String msg)
	{
		try
		{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
}
