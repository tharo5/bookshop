package bookshop;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddBook extends JDialog {
    private static String ADD_EMPLOYEE = "INSERT INTO books "
            + "(`book`, `author`, `price`) "
            + "VALUES ('%s', '%s', '%s')";

    private JLabel nameLabel = new JLabel("Название");
    private JTextField nameText = new JTextField();
    private JLabel authorLabel = new JLabel("Автор");
    private JTextField authorText = new JTextField();
    private JLabel priceLabel = new JLabel("Цена");
    private JTextField priceText = new JTextField();
    private JButton button = new JButton("Добавить");

    public AddBook(JDialog main) {
        super(main, "Добавление новой книги");
        this.setBounds(100, 100, 400, 400);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5, 2, 2, 2));
        container.add(nameLabel);
        container.add(nameText);
        container.add(authorLabel);
        container.add(authorText);
        container.add(priceLabel);
        container.add(priceText);
        button.addActionListener(new ButtonEventListener(getOwner()));
        container.add(button);

    }

    class ButtonEventListener implements ActionListener {
        private Window owner;

        public ButtonEventListener(Window owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            Connection con = null;
            Statement stmt = null;

            try {
                con = GetConnection.createConnection();
                stmt = con.createStatement();
                String name = nameText.getText();
                String author = authorText.getText();
                String price = priceText.getText();
                String query = String.format(ADD_EMPLOYEE, name, author, price);
                stmt.executeUpdate(query);
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException se) {
                }
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException se) {
                }
            }
            Books main = (Books) owner;
            main.loadBooks();
            setVisible(false);
            dispose();

        }
    }
}
