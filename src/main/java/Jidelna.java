import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;
import javax.swing.table.DefaultTableModel;

public class Jidelna {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField jmenoField;
    private JTextField prijmeniField;
    private JTextField stavKontaField;
    private JTextField adresaField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton exportButton;
    private JLabel averageLabel;
    private List<Stravnik> stravnik = new ArrayList<>();


    public Jidelna() {
        createGUI();
    }


    private void createGUI() {
        frame = new JFrame("Strávníci jídelny");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(5,2));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));

        inputPanel.add(new JLabel("Jméno:"));
        jmenoField = new JTextField();
        inputPanel.add(jmenoField);

        inputPanel.add(new JLabel("Příjmení:"));
        prijmeniField = new JTextField();
        inputPanel.add(prijmeniField);

        inputPanel.add(new JLabel("Stav konta (Kč):"));
        stavKontaField = new JTextField();
        inputPanel.add(stavKontaField);

        inputPanel.add(new JLabel("Adresa bydliště:"));
        adresaField = new JTextField();
        inputPanel.add(adresaField);

        addButton = new JButton("Přidat strávníka");
        addButton.addActionListener(new AddButtonListener());
        inputPanel.add(addButton);

        deleteButton = new JButton("Smazat strávníka");
        deleteButton.addActionListener(new DeleteButtonListener());
        inputPanel.add(deleteButton);

        exportButton = new JButton("Export do souboru");
        exportButton.addActionListener(new ExportButtonListener());
        inputPanel.add(exportButton);

        averageLabel = new JLabel("Průměrný stav konta: ");
        inputPanel.add(averageLabel);

        frame.add(inputPanel, BorderLayout.CENTER);
        tableModel = new DefaultTableModel(new String[] {"Jméno", "Příjmení", "Stav konta", "Adresa"}, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String jmeno = jmenoField.getText();
            String prijmeni = prijmeniField.getText();
            double stavKonta = Double.parseDouble(stavKontaField.getText());
            String adresa = adresaField.getText();

            Stravnik stravnik = new Stravnik(jmeno, prijmeni, stavKonta, adresa);
            Jidelna.this.stravnik.add(stravnik);

            tableModel.addRow(new Object[] {jmeno, prijmeni, stavKonta, adresa});

            updateAverageLabel();
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String jmeno = jmenoField.getText();
            String prijmeni = prijmeniField.getText();

            for (int i = 0; i < stravnik.size(); i++) {
                Stravnik stravnikToRemove = stravnik.get(i);
                if (stravnikToRemove.getJmeno().equals(jmeno) && stravnikToRemove.getPrijmeni().equals(prijmeni)) {
                    stravnik.remove(i);
                    tableModel.removeRow(i);
                    break;
                }
            }

            updateAverageLabel();
        }
    }

    private class ExportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (FileWriter writer = new FileWriter("stravnici.txt")) {
                for (Stravnik stravnik : stravnik) {
                    writer.write(stravnik.toString() + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Data byla úspěšně exportována!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Chyba při exportu dat: " + ex.getMessage());
            }
        }
    }

    private void updateAverageLabel() {
        double sum = 0;
        for (Stravnik stravnik : stravnik) {
            sum += stravnik.getStavKonta();
        }
        double average = sum / stravnik.size();
        averageLabel.setText("Průměrný stav konta: " + String.format("%.2f", average) + " CZK.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Jidelna::new);
    }

    class Stravnik {
        private String jmeno;
        private String prijmeni;
        private double stavKonta;
        private String adresa;

        public Stravnik(String jmeno, String prijmeni, double stavKonta, String adresa) {
            this.jmeno = jmeno;
            this.prijmeni = prijmeni;
            this.stavKonta = stavKonta;
            this.adresa = adresa;
        }

        public String getJmeno() {
            return jmeno;
        }

        public String getPrijmeni() {
            return prijmeni;
        }

        public double getStavKonta() {
            return stavKonta;
        }

        public String getAdresa() {
            return adresa;
        }

        @Override
        public String toString() {
            return "Stravnik{" +
                    "jmeno='" + jmeno + '\'' +
                    ", prijmeni='" + prijmeni + '\'' +
                    ", stavKonta=" + stavKonta +
                    ", adresa='" + adresa + '\'' +
                    '}';
        }
    }
}