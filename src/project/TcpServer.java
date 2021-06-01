package project;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

class ServerClass {

	ArrayList<ThreadServerClass> threadList = new ArrayList<>();

	Socket socket;
	DataOutputStream outputStream;
	Vector userList;

	public ServerClass(int portNo) throws IOException {	// ������
		Socket s = null;	// ���� ��ü

		// listen
		ServerSocket ss = new ServerSocket(portNo);
		System.out.println("���� ����...");

		while(true) {
			s = ss.accept();
			System.out.println("�����ּ� : "+s.getInetAddress()+", ������Ʈ : "+s.getPort());
			// user ���� ���� ������ ��ü �����ؼ� ������ ���� �÷� ����
			ThreadServerClass tServer = new ThreadServerClass(s);	// ������ ��ü ����
			tServer.start();	// ������ ����

			threadList.add(tServer);	// ������ ��ü ����Ʈ�� �ֱ� : ������ �� �߰�
			System.out.println("������ �� : "+threadList.size());
		}

	}


	// ���� ���� �����忡�� chat ������ ����
	public void sendChat(String chat) throws IOException {
		for(int i=0; i<threadList.size(); i++)
			// chat ������ outputStream�� ���ؼ� ����Ʈ�� ����.
			threadList.get(i).outputStream.writeUTF(chat);
	}


	class ThreadServerClass extends Thread {

		Socket socket1;
		DataInputStream inputStream;
		DataOutputStream outputStream;

		public ThreadServerClass(Socket s) throws IOException {	// ������
			socket1 = s;
			inputStream = new DataInputStream(s.getInputStream());
			outputStream = new DataOutputStream(s.getOutputStream());
		}

		public void run() {	// �ѻ�� ������ ������ ���
			String nickname = "";
			try {
				if(inputStream != null) {
					nickname = inputStream.readUTF();	// Stream�� ���� �� = �г���
					sendChat(nickname+"���� �����ϼ̽��ϴ�.");	// �г����� ä�� ���� ��� ������� ����
				}

				// ���� ä���� ��쿣 while���� ��� �ݺ��ȴ�.
				while(inputStream != null) {
					sendChat(inputStream.readUTF());	// ä�� ������ ��� ������� ����
				}

			} catch (IOException e) {
				//				e.printStackTrace();
			} finally {	// ������ ���� �ȳ��� ����Ǵ� ����
				// ���� �������� �ε��� ã��
				for(int i=0; i<threadList.size(); i++) {
					// ���� �������� ������ �������� Ȯ��
					if(socket1.equals(threadList.get(i).socket1)) {
						threadList.remove(i);
						try {
							sendChat(nickname+"���� �����ϼ̽��ϴ�.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				System.out.println("������ �� : "+threadList.size()+"��");
			}//finally-end
		}//run-end
	}//ThreadServerClass-end
}//ServerClass-end



public class TcpServer {

	public static void main(String[] args) throws NumberFormatException, IOException {

		if(args.length != 1) {
			System.out.println("���� : ���� ������ \'java ��Ű����.���ϸ� ��Ʈ��ȣ\' �������� �Է�");
		}

		new ServerClass(Integer.parseInt(args[0]));

	}

}
