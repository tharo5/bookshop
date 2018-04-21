package bookshop;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddCharacterstic extends JDialog {
    private JLabel nameLabel = new JLabel("Название");
    private JLabel valueLabel = new JLabel("Значение");
    private JTextField nameText = new JTextField();
    private JTextField valueText = new JTextField();
    private JButton saveButton = new JButton("Сохранить");

    public AddCharacterstic(JDialog main) {
        super(main, "Добавление характеристики");
        this.setBounds(100, 100, 400, 200);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 2, 2, 2));
        container.add(nameLabel);
        container.add(valueLabel);
        container.add(nameText);
        container.add(valueText);
        saveButton.addActionListener(new SaveButtonEventListener(getOwner()));
        container.add(saveButton);
    }

    class SaveButtonEventListener implements ActionListener {
        private Window owner;

        public SaveButtonEventListener(Window owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            String name = nameText.getText();
            String value = valueText.getText();

            AddBook main = (AddBook) owner;
            main.addCharacteristic(name, value);
            setVisible(false);
            dispose();
        }
    }
}
