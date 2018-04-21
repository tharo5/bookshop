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

public class AddClient extends JDialog {
    private static String ADD_CLIENT = "INSERT INTO clients "
            + "(`client_name`, `number`) "
            + "VALUES ('%s', '%s')";

    private JLabel nameLabel = new JLabel("Клиент");
    private JTextField nameText = new JTextField();
    private JLabel numberLabel = new JLabel("Номер");
    private JTextField numberText = new JTextField();
    private JButton button = new JButton("Добавить");

    public AddClient(JDialog main) {
        super(main, "Добавление нового клиента");
        this.setBounds(100, 100, 400, 400);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 2, 2, 2));
        container.add(nameLabel);
        container.add(nameText);
        container.add(numberLabel);
        container.add(numberText);
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
                String number = numberText.getText();
                String query = String.format(ADD_CLIENT, name, number);
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
            Clients main = (Clients) owner;
            main.loadCLients();
            setVisible(false);
            dispose();
        }
    }
}
