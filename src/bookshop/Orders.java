package bookshop;

import java.awt.Container;
import java.awt.GridLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Orders extends JDialog {

    private static String GET_ALL_ORDERS = "SELECT * FROM orders";

    private JButton button = new JButton("Добавить заказ  ");
    private JLabel label = new JLabel("Заказы", SwingConstants.CENTER);
    private DefaultTableModel model = new DefaultTableModel();
    private JTable table = new JTable(model);

    public Orders(JFrame main) {
        super(main, "Заказы");
        this.setBounds(100, 100, 400, 400);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1, 2, 2));
        container.add(label);
        model.addColumn("Id");
        model.addColumn("Id клиента");
        model.addColumn("Id книги");
        model.addColumn("Id сотрудника");
        table.getSelectionModel().addListSelectionListener(new TableEventListener(this));
        container.add(new JScrollPane(table));
        button.addActionListener(new ButtonEventListener(this));
        container.add(button);
        loadOrders();
    }

    class ButtonEventListener implements ActionListener {
        private JDialog main;

        public ButtonEventListener(JDialog main) {
            this.main = main;
        }

        public void actionPerformed(ActionEvent e) {
            AddOrder addOrder = new AddOrder(main);
            addOrder.setVisible(true);
        }
    }

    class TableEventListener implements ListSelectionListener {
        private JDialog main;

        public TableEventListener(JDialog main) {
            this.main = main;
        }

        public void valueChanged(ListSelectionEvent event) {
            if (table.getSelectedRow() != -1) {
                String bookId = table.getValueAt(table.getSelectedRow(), 0).toString();
                EditBook editBook = new EditBook(main, bookId);
                editBook.setVisible(true);
            }
        }
    }

    public void loadOrders() {
        model.setRowCount(0);
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = GetConnection.createConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(GET_ALL_ORDERS);
            while (rs.next()) {
                int id = rs.getInt(1);
                int id_client = rs.getInt(2);
                int id_book = rs.getInt(3);
                int id_employee = rs.getInt(4);
                model.addRow(new Object[] { id, id_client, id_book, id_employee });
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

}
