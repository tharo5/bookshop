package bookshop;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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

public class Books extends JDialog {
    private static String GET_ALL_BOOKS = "SELECT * FROM books";
    private static String GET_BOOK_CHARACTERISTIC = "SELECT * FROM book_characteristic WHERE id_book='%s'";
    private static String GET_CHARACTERISTIC = "SELECT * FROM  characteristics WHERE id_characteristic='%s'";

    private JButton button = new JButton("Добавить книгу");
    private JLabel label = new JLabel("Книги", SwingConstants.CENTER);
    private DefaultTableModel model = new DefaultTableModel();
    private JTable table = new JTable(model);

    public Books(JFrame main) {
        super(main, "Книги");
        this.setBounds(100, 100, 400, 400);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1, 2, 2));
        container.add(label);
        model.addColumn("Id");
        model.addColumn("Название");
        model.addColumn("Автор");
        model.addColumn("Цена");
        model.addColumn("Характеристики");
        table.getSelectionModel().addListSelectionListener(new TableEventListener(this));
        container.add(new JScrollPane(table));
        button.addActionListener(new ButtonEventListener(this));
        container.add(button);
        loadBooks();
    }

    class ButtonEventListener implements ActionListener {
        private JDialog main;

        public ButtonEventListener(JDialog main) {
            this.main = main;
        }

        public void actionPerformed(ActionEvent e) {
            AddBook addBook = new AddBook(main);
            addBook.setVisible(true);
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

    public void loadBooks() {
        model.setRowCount(0);
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = GetConnection.createConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(GET_ALL_BOOKS);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String author = rs.getString(3);
                int price = rs.getInt(4);
                Map<String, String> characteristics = getCharacteristics(con, id);
                model.addRow(new Object[] { id, name, author, price, characteristics });
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

    private Map<String, String> getCharacteristics(Connection con, int bookId) throws SQLException {
        Map<String, String> result = new HashMap<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = GetConnection.createConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(String.format(GET_BOOK_CHARACTERISTIC, bookId));
            if (rs.next()) {
                int idCharacteristick = rs.getInt(2);
                String value = rs.getString(3);
                rs = stmt.executeQuery(String.format(GET_CHARACTERISTIC, idCharacteristick));
                if (rs.next()) {
                    String name = rs.getString(2);
                    result.put(name, value);
                }
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
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
        return result;
    }

}
