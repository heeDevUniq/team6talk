package project;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class LoginClient extends JFrame {

	private JPanel contentPane;
	private JLabel lblIpAddress;
	private JLabel lblPortNo;
	private JLabel lblNickname;
	private JLabel lblImg;
	private static JTextField txtIp;
	private JTextField txtPort;
	private JTextField txtNick;
	private JButton btnEnter;
	Vector userList = new Vector();

	/**
	 * Launch the application.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			System.out.println("UIManager ERROR!");
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginClient frame = new LoginClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public LoginClient() throws ClassNotFoundException, SQLException {
		
		DAO dao = new DAO();
		
		setTitle("6��TALK");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblIpAddress = new JLabel("IP ADDRESS");
		lblIpAddress.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblIpAddress.setBounds(70, 293, 117, 32);
		contentPane.add(lblIpAddress);

		lblPortNo = new JLabel("PORT NO.");
		lblPortNo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPortNo.setBounds(70, 335, 117, 32);
		contentPane.add(lblPortNo);

		txtIp = new JTextField();
		txtIp.setFont(new Font("SansSerif", Font.PLAIN, 14));
		txtIp.setBounds(184, 294, 176, 32);
		contentPane.add(txtIp);
		txtIp.setColumns(10);

		txtPort = new JTextField();
		txtPort.setFont(new Font("SansSerif", Font.PLAIN, 14));
		txtPort.setColumns(10);
		txtPort.setBounds(184, 336, 176, 32);
		contentPane.add(txtPort);

		btnEnter = new JButton("\uC785\uC7A5\uD558\uAE30");
		btnEnter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {                       // ip주소         //포트번호
					Socket s1 = new Socket(txtIp.getText(),Integer.parseInt(txtPort.getText()));
					System.out.println("서버에 연결.....");//connect 

					DataOutputStream outputStream = new DataOutputStream(s1.getOutputStream());
					DataInputStream inputStream = new DataInputStream(s1.getInputStream());
					outputStream.writeUTF("## " + txtNick.getText()); 
					
					userList.add(txtNick.getText());
					
					new KajaClientGUI(outputStream, inputStream, txtNick.getText(),
							txtIp.getText(), Integer.parseInt(txtPort.getText()), userList) {
						public void closeWork() throws IOException {
							outputStream.close();
							inputStream.close();
							System.exit(0);
						}
					};
					
					// DB에 접속자 저장
					boolean savingUser = dao.insertTalker(txtNick.getText());
					if(savingUser) System.out.println("DB저장 성공!");
					else JOptionPane.showMessageDialog(null, "DB저장 실패!");
					
				} catch (Exception ee) { 
					//	ee.printStackTrace();
				}
			}
		});
		btnEnter.setForeground(new Color(0, 0, 0));
		btnEnter.setBackground(new Color(255, 255, 255));
		btnEnter.setFont(new Font("맑은고딕", Font.BOLD, 20));
		btnEnter.setBounds(132, 448, 176, 42);
		contentPane.add(btnEnter);

		lblNickname = new JLabel("NICKNAME");
		lblNickname.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblNickname.setBounds(70, 379, 117, 32);
		contentPane.add(lblNickname);

		txtNick = new JTextField();
		txtNick.setFont(new Font("SansSerif", Font.PLAIN, 14));
		txtNick.setColumns(10);
		txtNick.setBounds(184, 380, 176, 32);
		contentPane.add(txtNick);

		lblImg = new JLabel();
		lblImg.setIcon(new ImageIcon(LoginClient.class.getResource("/project/bg.jpg")));
		lblImg.setBounds(0, 0, 434, 563);
		contentPane.add(lblImg);
	}
}
