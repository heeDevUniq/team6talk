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

// Client console to client GUID				// Runnable = ������ Ŭ���� ����
public class KajaClientGUI extends JFrame implements Runnable, ActionListener {

	// console��忡�� �Ѿ���� 3�� ���ڸ� �޾� ������ �ʵ�
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String nickname;
	String ip;
	int port;
	Vector userList;

	// �׷��� ������ ������Ʈ
	JPanel contentPane;
	JTextField textField;
	JList list;
	JLabel lblNowUser;
	JScrollPane scrollPane;
	JTextArea textArea;

	public KajaClientGUI(DataOutputStream outputStream, 
			DataInputStream inputStream, String nickname, String ip, int port, Vector userList) {	// ������
		this.outputStream = outputStream;	// �ʵ�(�Ӽ�) �ʱ�ȭ
		this.inputStream = inputStream;
		this.nickname = nickname;
		this.ip = ip;
		this.port = port;
		this.userList = userList;
		
		setTitle("6��TALK");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(224,230,243));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(true);
		
		// ���� ������
		lblNowUser = new JLabel();
		lblNowUser.setFont(new Font("�������", Font.BOLD, 16));
		lblNowUser.setText("�� �� �� �� ��");
		lblNowUser.setBounds(163, 0, 137, 30);
		contentPane.add(lblNowUser);
		
		// �����ڰ� ��µǴ� ��
		list = new JList();
		list.setBackground(Color.WHITE);
		list.setForeground(Color.BLACK);
		list.setFont(new Font("�������", Font.BOLD, 14));
		list.setBounds(0, 30, 434, 96);
		contentPane.add(list);
		
		// chat �Է�
		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setForeground(Color.BLACK);
		textField.setFont(new Font("�������", Font.BOLD, 14));
		textField.setBounds(0, 516, 434, 45);
		textField.addActionListener(this);
		contentPane.add(textField);
		
		// chat ���ڿ��� ��µǴ� ��
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 124, 434, 394);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setBackground(new Color(224,230,243));
		textArea.setForeground(Color.BLACK);
		textArea.setFont(new Font("�������", Font.PLAIN, 14));
		textArea.setEditable(false);
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);	// �̰� ����� "~�����ϼ̽��ϴ�." �޽����� ���´�.
				// ��, 1�� ������ ���� ������ �ʰ� 2�� �̻��� ���� ���
				setVisible(false);
			}
		});

		// �����κ��� �޾Ƽ� TextArea�� �ѷ��ִ� ������
		Thread th = new Thread(this);
		th.start();

	}// ������-end

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==textField) {
			try {
				outputStream.writeUTF(nickname+" : " + textField.getText());
			} catch (IOException e1) {
//				e1.printStackTrace();
			}
			textField.setText("");	// ������ chat�� ������ �� �� ĭ�� Ŭ����
		}
	}// actionPerformed-end


	// chat�� �� ������ beep���� �︮�� �Ϸ��� �����ϴ� �κ�
	Toolkit tk =Toolkit.getDefaultToolkit();


	@Override
	public void run() {	// for�� �޴� thread
		try {
			while(true) {
				list.setListData(userList);
				
				String strServer;
				strServer = inputStream.readUTF();
				if(strServer == null) {	// chat�� �ƹ��͵� �ȿ��� ��
					textArea.append("\n"+"����");
					return;
				} else if(strServer != null) {
					
				}
				textArea.append("\n"+strServer);	// �������� �� chat�� TextArea�� �߰�

				//-----------��ũ�ѹٰ� ���� �� �� �Ʒ��� ������ �� ���̰� �ϴ� �κ�-----------
				int bottom = textArea.getText().length();
				textArea.setCaretPosition(bottom); // Ŀ���� �� �ڷ� ����, �� ������ ������ �� (0)�� ��
				tk.beep();// chat�� �� ������ beep�� �︮�� �ϴ� �κ�
				//----------------------------------------------------------------
			}
		} catch (Exception e) {
			//			e.printStackTrace();
			textArea.append("\n"+e);
		}
	}
}

