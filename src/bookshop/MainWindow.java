package bookshop;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

    private JButton employeesButton = new JButton("Сотрудники");
    private JButton clientsButton = new JButton("Клиенты");
    private JButton ordersButton = new JButton("Заказы");
    private JButton booksButton = new JButton("Книги");
    private JButton bookCharactersisticsBbutton = new JButton("Характеристики книг");
    private JButton charactersisticsBbutton = new JButton("Характеристики");

    public MainWindow() {
        super("Основное меню");
        this.setBounds(100, 100, 600, 400);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(2, 3, 2, 2));
        employeesButton.addActionListener(new ButtonEventListener(this));
        container.add(employeesButton);
        clientsButton.addActionListener(new ButtonEventListener(this));
        container.add(clientsButton);
        booksButton.addActionListener(new ButtonEventListener(this));
        container.add(booksButton);
        ordersButton.addActionListener(new ButtonEventListener(this));
        container.add(ordersButton);
        bookCharactersisticsBbutton.addActionListener(new ButtonEventListener(this));
        container.add(bookCharactersisticsBbutton);
        charactersisticsBbutton.addActionListener(new ButtonEventListener(this));
        container.add(charactersisticsBbutton);
    }

    class ButtonEventListener implements ActionListener {
        private JFrame main;

        public ButtonEventListener(JFrame main) {
            this.main = main;
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == employeesButton) {
                Employees employees = new Employees(main);
                employees.setVisible(true);
            } else if (e.getSource() == booksButton) {
                Books books = new Books(main);
                books.setVisible(true);
            } else if (e.getSource() == clientsButton) {
                Clients clients = new Clients(main);
                clients.setVisible(true);
            } else if (e.getSource() == ordersButton) {
                Orders orders = new Orders(main);
                orders.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        MainWindow app = new MainWindow();
        app.setVisible(true);
    }
}
