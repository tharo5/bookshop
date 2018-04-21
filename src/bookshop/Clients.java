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

public class Clients extends JDialog {

    private static String GET_ALL_CLIENTS = "SELECT * FROM clients";

    private JButton button = new JButton("Добавить клиента");
    private JLabel label = new JLabel("Клиенты", SwingConstants.CENTER);
    private DefaultTableModel model = new DefaultTableModel();
    private JTable table = new JTable(model);

    public Clients(JFrame main) {
        super(main, "Клиенты");
        this.setBounds(100, 100, 400, 400);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1, 2, 2));
        container.add(label);
        model.addColumn("Id");
        model.addColumn("Клиент");
        model.addColumn("Номер");
        table.getSelectionModel().addListSelectionListener(new TableEventListener(this));
        container.add(new JScrollPane(table));
        button.addActionListener(new ButtonEventListener(this));
        container.add(button);
        loadCLients();
    }

    class ButtonEventListener implements ActionListener {
        private JDialog main;

        public ButtonEventListener(JDialog main) {
            this.main = main;
        }

        public void actionPerformed(ActionEvent e) {
            AddClient addClient = new AddClient(main);
            addClient.setVisible(true);
        }
    }

    class TableEventListener implements ListSelectionListener {
        private JDialog main;

        public TableEventListener(JDialog main) {
            this.main = main;
        }

        public void valueChanged(ListSelectionEvent event) {
            if (table.getSelectedRow() != -1) {
                String clientId = table.getValueAt(table.getSelectedRow(), 0).toString();
                EditClient editClient = new EditClient(main, clientId);
                editClient.setVisible(true);
            }
        }
    }

    public void loadCLients() {
        model.setRowCount(0);
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = GetConnection.createConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(GET_ALL_CLIENTS);
            while (rs.next()) {
                int id = rs.getInt(1);
                String client = rs.getString(2);
                String number = rs.getString(3);
                model.addRow(new Object[] { id, client, number });
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
