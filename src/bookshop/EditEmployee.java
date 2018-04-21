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

public class EditEmployee extends JDialog {

    private static String GET_EMPLOYEE = "SELECT * FROM employees WHERE id_employee=%s";
    private static String UPDATE_EMPLOYEE = "UPDATE `employees` "
            + "SET `employee_surname` = '%s', `employee_name` = '%s', `employee_otchestvo` = '%s', `salary` = '%s'"
            + "WHERE `id_employee` = '%s'";
    private static String DELETE_EMPLOYEE = "DELETE FROM `employees` WHERE id_employee=%s";
    private String id;

    private JLabel surnameLabel = new JLabel("Фамилия");
    private JTextField surnameText = new JTextField();
    private JLabel nameLabel = new JLabel("Имя");
    private JTextField nameText = new JTextField();
    private JLabel othestvoLabel = new JLabel("Отчество");
    private JTextField otchestvoText = new JTextField();
    private JLabel salaryLabel = new JLabel("Зарплата");
    private JTextField salaryText = new JTextField();
    private JButton saveButton = new JButton("Сохранить");
    private JButton deleteButton = new JButton("Удалить");

    public EditEmployee(JDialog main, String id) {
        super(main, "Редактирование сотрудника");
        this.id = id;
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
        saveButton.addActionListener(new SaveButtonEventListener(getOwner()));
        container.add(saveButton);
        deleteButton.addActionListener(new DeleteButtonEventListener(getOwner()));
        container.add(deleteButton);
        loadEmployee();
    }

    private void loadEmployee() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = GetConnection.createConnection();
            stmt = con.createStatement();
            String query = String.format(GET_EMPLOYEE, id);
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                surnameText.setText(rs.getString(2));
                nameText.setText(rs.getString(3));
                otchestvoText.setText(rs.getString(4));
                salaryText.setText(String.valueOf(rs.getInt(5)));
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
                String surname = surnameText.getText();
                String name = nameText.getText();
                String otchestvo = otchestvoText.getText();
                String salary = salaryText.getText();
                String query = String.format(UPDATE_EMPLOYEE, surname, name, otchestvo, salary, id);
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
                String query = String.format(DELETE_EMPLOYEE, id);
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
