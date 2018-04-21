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

public class AddEmployee extends JDialog {
    private static String ADD_EMPLOYEE = "INSERT INTO employees "
            + "(`employee_surname`, `employee_name`, `employee_otchestvo`, `salary`) "
            + "VALUES ('%s', '%s', '%s', '%s')";

    private JLabel surnameLabel = new JLabel("Фамилия");
    private JTextField surnameText = new JTextField();
    private JLabel nameLabel = new JLabel("Имя");
    private JTextField nameText = new JTextField();
    private JLabel othestvoLabel = new JLabel("Отчество");
    private JTextField otchestvoText = new JTextField();
    private JLabel salaryLabel = new JLabel("Зарплата");
    private JTextField salaryText = new JTextField();
    private JButton button = new JButton("Добавить");

    public AddEmployee(JDialog main) {
        super(main, "Добавление нового сотрудника");
        this.setBounds(100, 100, 400, 400);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5, 2, 2, 2));
        container.add(surnameLabel);
        container.add(surnameText);
        container.add(nameLabel);
        container.add(nameText);
        container.add(othestvoLabel);
        container.add(otchestvoText);
        container.add(salaryLabel);
        container.add(salaryText);
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
                String surname = surnameText.getText();
                String name = nameText.getText();
                String otchestvo = otchestvoText.getText();
                String salary = salaryText.getText();
                String query = String.format(ADD_EMPLOYEE, surname, name, otchestvo, salary);
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
            Employees main = (Employees) owner;
            main.loadEmployees();
            setVisible(false);
            dispose();

        }
    }
}
