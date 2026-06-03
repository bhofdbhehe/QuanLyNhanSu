package gui;

import dao.TaiKhoanDAO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private JCheckBox chkShowPassword;

    public LoginGUI() {
        setTitle("Đăng nhập hệ thống");
        setSize(450, 340); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40)); 

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(41, 128, 185)); 
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridLayout(5, 1, 0, 5));
        panelCenter.setBackground(Color.WHITE);

        JLabel lblUser = new JLabel("Tài khoản:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(110, 120, 130));
        
        txtUsername = createStyledTextField();

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(110, 120, 130));
        
        txtPassword = createStyledPasswordField();

        chkShowPassword = new JCheckBox("Hiển thị mật khẩu");
        chkShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkShowPassword.setBackground(Color.WHITE);
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.setForeground(new Color(140, 150, 160));

        panelCenter.add(lblUser);
        panelCenter.add(txtUsername);
        panelCenter.add(lblPass);
        panelCenter.add(txtPassword);
        panelCenter.add(chkShowPassword);
        
        mainPanel.add(panelCenter, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panelBottom.setBackground(Color.WHITE);

        btnLogin = styleButton("Đăng nhập", new Color(41, 128, 185), new Color(52, 152, 219));
        btnExit = styleButton("Thoát", new Color(231, 76, 60), new Color(242, 109, 93));

        panelBottom.add(btnLogin);
        panelBottom.add(btnExit);
        
        mainPanel.add(panelBottom, BorderLayout.SOUTH);
        add(mainPanel);

        this.getRootPane().setDefaultButton(btnLogin);

        addEvents();
    }

    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(0, 10, 0, 10)
        ));
        return txt;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField txt = new JPasswordField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(0, 10, 0, 10)
        ));
        return txt;
    }

    private JButton styleButton(String text, Color normalColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(normalColor);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(140, 40)); 
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(normalColor); }
        });
        return btn;
    }

    private void addEvents() {
        btnExit.addActionListener(e -> System.exit(0));

        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TaiKhoanDAO dao = new TaiKhoanDAO();
            if (dao.kiemTraDangNhap(username, password)) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                new HRManagerGUI().setVisible(true);
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu. Vui lòng thử lại!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}