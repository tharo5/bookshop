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

public class AddBook extends JDialog {
    private static String ADD_BOOK = "INSERT INTO books "
            + "(`book`, `author`, `price`) "
            + "VALUES ('%s', '%s', '%s')";
    private static String ADD_CHARACTERISTIC = "INSERT INTO characteristics " +
            "(`id_characteristic`, `characteristic_name`) "
            + "VALUES ('%s', '%s')";
    private static String ADD_BOOK_CHARACTERISITIC = "INSERT INTO book_characteristic "
            + "(`id_book`, `id_characteristic`, `value`) "
            + "VALUES ('%', '%', '%')";

    private JLabel nameLabel = new JLabel("Название");
    private JTextField nameText = new JTextField();
    private JLabel authorLabel = new JLabel("Автор");
    private JTextField authorText = new JTextField();
    private JLabel priceLabel = new JLabel("Цена");
    private JTextField priceText = new JTextField();
    private JLabel charateristicsLabel = new JLabel("Характеристики");
    private DefaultTableModel model = new DefaultTableModel();
    private JTable table = new JTable(model);
    private JButton button = new JButton("Добавить характеристику");
    private JButton button2 = new JButton("Добавить");

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
        container.add(charateristicsLabel);
        model.addColumn("Название");
        model.addColumn("Значение");
        container.add(new JScrollPane(table));
        button.addActionListener(new CharactersiticsButtonEventListener(this));
        container.add(button);
        button2.addActionListener(new SaveButtonEventListener(getOwner()));
        container.add(button2);
    }

    public void addCharacteristic(String name, String value) {
        model.addRow(new Object[] { name, value });
    }

    class SaveButtonEventListener implements ActionListener {
        private Window owner;

        public SaveButtonEventListener(Window owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            Connection con = null;
            Statement stmt = null;
            long bookId = -1;

            try {
                con = GetConnection.createConnection();
                stmt = con.createStatement();
                String name = nameText.getText();
                String author = authorText.getText();
                String price = priceText.getText();
                String query = String.format(ADD_BOOK, name, author, price);
                stmt.executeUpdate(query);
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    bookId = rs.getLong(1);
                    System.out.println("bookId="+bookId);
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
            }
            
            
            Books main = (Books) owner;
            main.loadBooks();
            setVisible(false);
            dispose();
        }
        

    }

    class CharactersiticsButtonEventListener implements ActionListener {
        private JDialog owner;

        public CharactersiticsButtonEventListener(JDialog owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            AddCharacterstic addCharacterstic = new AddCharacterstic(owner);
            addCharacterstic.setVisible(true);
        }
    }
}
