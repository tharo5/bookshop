package bookshop;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EditClient extends JDialog {

    private static String GET_CLIENT = "SELECT * FROM clients WHERE id_client=%s";
    private static String UPDATE_CLIENT = "UPDATE `clients` "
            + "SET `client_name` = '%s', `number` = '%s'"
            + "WHERE `id_client` = '%s'";
    private static String DELETE_CLIENT = "DELETE FROM `clients` WHERE id_client=%s";
    private String id;

    private JLabel clientLabel = new JLabel("Клиент");
    private JTextField clientText = new JTextField();
    private JLabel numberLabel = new JLabel("Номер");
    private JTextField numberText = new JTextField();
    private JButton saveButton = new JButton("Сохранить");
    private JButton deleteButton = new JButton("Удалить");

    public EditClient(JDialog main, String id) {
        super(main, "Редактирование сотрудника");
        this.id = id;
        this.setBounds(100, 100, 400, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5, 2, 2, 2));
        container.add(clientLabel);
        container.add(clientText);
        container.add(numberLabel);
        container.add(numberText);
        saveButton.addActionListener(new SaveButtonEventListener(getOwner()));
        container.add(saveButton);
        deleteButton.addActionListener(new DeleteButtonEventListener(getOwner()));
        container.add(deleteButton);
        loadBook();
    }

    private void loadBook() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = GetConnection.createConnection();
            stmt = con.createStatement();
            String query = String.format(GET_CLIENT, id);
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                clientText.setText(rs.getString(2));
                numberText.setText(rs.getString(3));
            }
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
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
            }
        }
    }

    class SaveButtonEventListener implements ActionListener {
        private Window owner;

        public SaveButtonEventListener(Window owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            Connection con = null;
            Statement stmt = null;

            try {
                con = GetConnection.createConnection();
                stmt = con.createStatement();
                String client = clientText.getText();
                String number = numberText.getText();
                String query = String.format(UPDATE_CLIENT, client, number, id);
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

    class DeleteButtonEventListener implements ActionListener {
        private Window owner;

        public DeleteButtonEventListener(Window owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            Connection con = null;
            Statement stmt = null;

            try {
                con = GetConnection.createConnection();
                stmt = con.createStatement();
                String query = String.format(DELETE_CLIENT, id);
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
