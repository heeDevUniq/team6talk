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

	public ServerClass(int portNo) throws IOException {	// 생성자
		Socket s = null;	// 접속 객체

		// listen
		ServerSocket ss = new ServerSocket(portNo);
		System.out.println("서버 가동...");

		while(true) {
			s = ss.accept();
			System.out.println("접속주소 : "+s.getInetAddress()+", 접속포트 : "+s.getPort());
			// user 접속 마다 스레드 객체 생성해서 스레드 위에 올려 놓음
			ThreadServerClass tServer = new ThreadServerClass(s);	// 스레드 객체 생성
			tServer.start();	// 스레드 시작

			threadList.add(tServer);	// 스레드 객체 리스트에 넣기 : 접속자 수 추가
			System.out.println("접속자 수 : "+threadList.size());
		}

	}


	// 점속 중인 스레드에게 chat 내용을 보냄
	public void sendChat(String chat) throws IOException {
		for(int i=0; i<threadList.size(); i++)
			// chat 내용이 outputStream을 통해서 리스트에 들어간다.
			threadList.get(i).outputStream.writeUTF(chat);
	}


	class ThreadServerClass extends Thread {

		Socket socket1;
		DataInputStream inputStream;
		DataOutputStream outputStream;

		public ThreadServerClass(Socket s) throws IOException {	// 생성자
			socket1 = s;
			inputStream = new DataInputStream(s.getInputStream());
			outputStream = new DataOutputStream(s.getOutputStream());
		}

		public void run() {	// 한사람 서버로 접속한 경우
			String nickname = "";
			try {
				if(inputStream != null) {
					nickname = inputStream.readUTF();	// Stream에 들어온 값 = 닉네임
					sendChat(nickname+"님이 입장하셨습니다.");	// 닉네임을 채팅 관련 모든 사람에게 전송
				}

				// 정상 채팅일 경우엔 while문이 계속 반복된다.
				while(inputStream != null) {
					sendChat(inputStream.readUTF());	// 채팅 내용을 모든 사람에게 전송
				}

			} catch (IOException e) {
				//				e.printStackTrace();
			} finally {	// 에러가 나든 안나든 실행되는 구문
				// 나간 스레드의 인덱스 찾기
				for(int i=0; i<threadList.size(); i++) {
					// 나간 스레드의 소켓이 누구인지 확인
					if(socket1.equals(threadList.get(i).socket1)) {
						threadList.remove(i);
						try {
							sendChat(nickname+"님이 퇴장하셨습니다.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				System.out.println("접속자 수 : "+threadList.size()+"명");
			}//finally-end
		}//run-end
	}//ThreadServerClass-end
}//ServerClass-end



public class TcpServer {

	public static void main(String[] args) throws NumberFormatException, IOException {

		if(args.length != 1) {
			System.out.println("사용법 : 서버 실행은 \'java 패키지명.파일명 포트번호\' 형식으로 입력");
		}

		new ServerClass(Integer.parseInt(args[0]));

	}

}
