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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class EditBook extends JDialog {

    private static String GET_BOOK = "SELECT * FROM books WHERE id_book=%s";
    private static String UPDATE_BOOK = "UPDATE `books` "
            + "SET `book` = '%s', `author` = '%s', `price` = '%s'"
            + "WHERE `id_book` = '%s'";
    private static String DELETE_BOOK = "DELETE FROM `books` WHERE id_book=%s";
    private static String GET_BOOK_CHARACTERISTIC = "SELECT * FROM book_characteristic WHERE id_book='%s'";
    private static String GET_CHARACTERISTIC = "SELECT * FROM  characteristics WHERE id_characteristic='%s'";
    private String id;

    private JLabel nameLabel = new JLabel("Название");
    private JTextField nameText = new JTextField();
    private JLabel authorLabel = new JLabel("Автор");
    private JTextField authorText = new JTextField();
    private JLabel priceLabel = new JLabel("Цена");
    private JTextField priceText = new JTextField();
    private JLabel characteristicsLabel = new JLabel("Характеристики");
    private DefaultTableModel model = new DefaultTableModel();
    private JTable table = new JTable(model);
    private JButton saveButton = new JButton("Сохранить");
    private JButton deleteButton = new JButton("Удалить");

    public EditBook(JDialog main, String id) {
        super(main, "Редактирование сотрудника");
        this.id = id;
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
        container.add(characteristicsLabel);
        model.addColumn("Название");
        model.addColumn("Значение");
        loadCharacteristics();
        container.add(new JScrollPane(table));
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
            String query = String.format(GET_BOOK, id);
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                nameText.setText(rs.getString(2));
                authorText.setText(rs.getString(3));
                priceText.setText(String.valueOf(rs.getInt(4)));
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

    public void loadCharacteristics() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = GetConnection.createConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(String.format(GET_BOOK_CHARACTERISTIC, id));
            if (rs.next()) {
                int idCharacteristick = rs.getInt(2);
                String value = rs.getString(3);
                rs = stmt.executeQuery(String.format(GET_CHARACTERISTIC, idCharacteristick));
                if (rs.next()) {
                    String name = rs.getString(2);
                    model.addRow(new Object[] { name, value });
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
                String name = nameText.getText();
                String author = authorText.getText();
                String price = priceText.getText();
                String query = String.format(UPDATE_BOOK, name, author, price, id);
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
                String query = String.format(DELETE_BOOK, id);
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
