package project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

// Client console to client GUID				// Runnable = 스레드 클래스 구현
public class KajaClientGUI extends JFrame implements Runnable, ActionListener {

	// console모드에서 넘어오는 3개 인자를 받아 저장할 필드
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String nickname;
	String ip;
	int port;
	Vector userList;

	// 그래픽 디자인 컴포넌트
	JPanel contentPane;
	JTextField textField;
	JList list;
	JLabel lblNowUser;
	JScrollPane scrollPane;
	JTextArea textArea;

	public KajaClientGUI(DataOutputStream outputStream, 
			DataInputStream inputStream, String nickname, String ip, int port, Vector userList) {	// 생성자
		this.outputStream = outputStream;	// 필드(속성) 초기화
		this.inputStream = inputStream;
		this.nickname = nickname;
		this.ip = ip;
		this.port = port;
		this.userList = userList;
		
		setTitle("6조TALK");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(224,230,243));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(true);
		
		// 현재 접속자
		lblNowUser = new JLabel();
		lblNowUser.setFont(new Font("맑은고딕", Font.BOLD, 16));
		lblNowUser.setText("현 재 접 속 자");
		lblNowUser.setBounds(163, 0, 137, 30);
		contentPane.add(lblNowUser);
		
		// 접속자가 출력되는 곳
		list = new JList();
		list.setBackground(Color.WHITE);
		list.setForeground(Color.BLACK);
		list.setFont(new Font("맑은고딕", Font.BOLD, 14));
		list.setBounds(0, 30, 434, 96);
		contentPane.add(list);
		
		// chat 입력
		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setForeground(Color.BLACK);
		textField.setFont(new Font("맑은고딕", Font.BOLD, 14));
		textField.setBounds(0, 516, 434, 45);
		textField.addActionListener(this);
		contentPane.add(textField);
		
		// chat 문자열이 출력되는 곳
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 124, 434, 394);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setBackground(new Color(224,230,243));
		textArea.setForeground(Color.BLACK);
		textArea.setFont(new Font("맑은고딕", Font.PLAIN, 14));
		textArea.setEditable(false);
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);	// 이걸 써줘야 "~퇴장하셨습니다." 메시지가 나온다.
				// 단, 1명 접속일 때는 나오지 않고 2명 이상일 때만 출력
				setVisible(false);
			}
		});

		// 서버로부터 받아서 TextArea에 뿌려주는 스레드
		Thread th = new Thread(this);
		th.start();

	}// 생성자-end

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==textField) {
			try {
				outputStream.writeUTF(nickname+" : " + textField.getText());
			} catch (IOException e1) {
//				e1.printStackTrace();
			}
			textField.setText("");	// 서버로 chat을 보내고 난 뒤 칸을 클리어
		}
	}// actionPerformed-end


	// chat이 올 때마다 beep음을 울리게 하려고 설정하는 부분
	Toolkit tk =Toolkit.getDefaultToolkit();


	@Override
	public void run() {	// for을 받는 thread
		try {
			while(true) {
				list.setListData(userList);
				
				String strServer;
				strServer = inputStream.readUTF();
				if(strServer == null) {	// chat이 아무것도 안왔을 때
					textArea.append("\n"+"종료");
					return;
				} else if(strServer != null) {
					
				}
				textArea.append("\n"+strServer);	// 서버에서 온 chat을 TextArea에 추가

				//-----------스크롤바가 생긴 후 맨 아래의 내용이 잘 보이게 하는 부분-----------
				int bottom = textArea.getText().length();
				textArea.setCaretPosition(bottom); // 커서를 맨 뒤로 설정, 맨 앞으로 설정할 땐 (0)을 줌
				tk.beep();// chat이 올 때마다 beep음 울리게 하는 부분
				//----------------------------------------------------------------
			}
		} catch (Exception e) {
			//			e.printStackTrace();
			textArea.append("\n"+e);
		}
	}
}

