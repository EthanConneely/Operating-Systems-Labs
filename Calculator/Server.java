import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Server extends Thread
{
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;

	public Server(Socket s)
	{
		socket = s;
	}

	public void run()
	{
		try
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

			handleLogic();
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

	private void handleLogic() throws IOException, ClassNotFoundException, Exception
	{
		while (true)
		{
			sendMessage("Please enter 1 to Add Three Numbers or 2 to Multiply Two Number");
			message = (String) in.readObject();

			if (message.equalsIgnoreCase("1"))
			{
				handleAdd();
			}
			else if (message.equalsIgnoreCase("2"))
			{
				handleMul();
			}

			sendMessage("Would you like to quit? (yes/no)");
			message = (String) in.readObject();

			if (message.equalsIgnoreCase("yes"))
			{
				System.out.println("Client Disconnected!");
				return;// close out of the connection and the infinite loop
			}
		}
	}

	public void handleAdd() throws Exception
	{
		int result = 0;
		for (int i = 1; i <= 3; i++)
		{
			result += requestNumber(i);
		}

		// Send the result back to the client
		sendMessage("The result is " + result);
	}

	private int requestNumber(int i) throws IOException, ClassNotFoundException
	{
		sendMessage("Input Number " + i);
		message = (String) in.readObject();
		return Integer.parseInt(message);
	}

	private void handleMul() throws Exception
	{
		int num1 = requestNumber(1);
		int num2 = requestNumber(2);
		int result = num1 * num2;

		// Send the result back to the client
		sendMessage("The result is " + result);
	}

	public void sendMessage(String msg)
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
