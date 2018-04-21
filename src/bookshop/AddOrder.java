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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AddOrder extends JDialog {
    private static String ADD_ORDER = "INSERT INTO orders "
            + "(`id_client`, `id_book`, `id_employee`) "
            + "VALUES ('%s', '%s', '%s')";
    private static String GET_ALL_CLIENTS = "SELECT * FROM clients";
    private static String GET_ALL_BOOKS = "SELECT * FROM books";
    private static String GET_ALL_EMPLOYEES = "SELECT * FROM employees";

    private JLabel idClientLabel = new JLabel("Id клиента");
    private JComboBox<Integer> idClientComboBox = new JComboBox<>();
    private JLabel idBookLabel = new JLabel("Id книги");
    private JComboBox<Integer> idBookComboBox = new JComboBox<>();
    private JLabel idEmployeeLabel = new JLabel("Id сотрудника");
    private JComboBox<Integer> idEmployeeComboBox = new JComboBox<>();

    private JButton button = new JButton("Добавить");

    public AddOrder(JDialog main) {
        super(main, "Добавление нового заказа");
        this.setBounds(100, 100, 400, 400);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(4, 2, 2, 2));
        container.add(idClientLabel);
        container.add(idClientComboBox);
        container.add(idBookLabel);
        container.add(idBookComboBox);
        container.add(idEmployeeLabel);
        container.add(idEmployeeComboBox);
        button.addActionListener(new ButtonEventListener(getOwner()));
        container.add(button);

        load();
    }

    private void load() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = GetConnection.createConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(GET_ALL_CLIENTS);
            while (rs.next()) {
                int id_client = rs.getInt(1);
                idClientComboBox.addItem(id_client);
            }
            rs = stmt.executeQuery(GET_ALL_BOOKS);
            while (rs.next()) {
                int id_book = rs.getInt(1);
                idBookComboBox.addItem(id_book);
            }
            rs = stmt.executeQuery(GET_ALL_EMPLOYEES);
            while (rs.next()) {
                int id_employee = rs.getInt(1);
                idEmployeeComboBox.addItem(id_employee);
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
                int idClient = (int) idClientComboBox.getSelectedItem();
                int idBook = (int) idBookComboBox.getSelectedItem();
                int idEmployee = (int) idEmployeeComboBox.getSelectedItem();
                String query = String.format(ADD_ORDER, idClient, idBook, idEmployee);
                System.out.println("query" + query);
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
            Orders main = (Orders) owner;
            main.loadOrders();
            setVisible(false);
            dispose();
        }
    }
}
