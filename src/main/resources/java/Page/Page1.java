package Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Page1 {
    public void page() {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("智慧校园导览系统");
        frame.setSize(600, 800);
        frame.setLocation(20, 15);
        JPanel jpnel=new JPanel();
        jpnel.setLayout(null);

        //创建用户名
        JLabel userLabel = new JLabel("User:");
        userLabel.setBounds(165,30,80,30);
        jpnel.add(userLabel);
        JTextField userText = new JTextField(20);
        userText.setBounds(260,30,175,30);
        jpnel.add(userText);

        //创建用户密码
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(165,80,80,30);
        jpnel.add(passwordLabel);
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(260,80,175,30);
        jpnel.add(passwordText);

        //创建登陆按钮
        JButton loginButton = new JButton("login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Page2 user = new Page2();
                user.page();
            }
        });
        loginButton.setBounds(260, 150, 80, 25);
        jpnel.add(loginButton);

        ImageIcon image = new ImageIcon("src/main/resources/java/Image/buptBadge.jpeg");
        image.setImage(image.getImage().getScaledInstance(300,300,Image.SCALE_DEFAULT));
        JLabel im = new JLabel(image);
        im.setBounds(0, 200, 600, 600);
        frame.add(im);

        jpnel.setBackground(Color.pink);
        frame.add(jpnel);
        frame.setVisible(true);
    }
}
