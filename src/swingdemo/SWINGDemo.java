package swingdemo;

import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.FileReader;

public class SWINGDemo {

    private final JEditorPane log;
    private final JButton show;
    private final JButton report;
    private final JComboBox clients;

    public SWINGDemo() {

        log = new JEditorPane("text/html", "");
        log.setPreferredSize(new Dimension(250, 150));

        show = new JButton("Show");
        report = new JButton("Report");

        clients = new JComboBox();

        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            clients.addItem(
                    Bank.getCustomer(i).getLastName() + ", " +
                    Bank.getCustomer(i).getFirstName()
            );
        }
    }

    private void launchFrame() {

        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());

        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 3));

        cpane.add(clients);
        cpane.add(show);
        cpane.add(report);

        frame.add(cpane, BorderLayout.NORTH);
        frame.add(new JScrollPane(log), BorderLayout.CENTER);

        // show btn
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Customer current =
                        Bank.getCustomer(clients.getSelectedIndex());

                StringBuilder info = new StringBuilder();

                info.append("<html><h2>")
                        .append(current.getLastName())
                        .append(", ")
                        .append(current.getFirstName())
                        .append("</h2><hr>");

                for (int i = 0;
                     i < current.getNumberOfAccounts();
                     i++) {

                    String type;

                    if (current.getAccount(i) instanceof SavingsAccount) {
                        type = "Savings Account";
                    } else {
                        type = "Checking Account";
                    }

                    info.append("<b>")
                            .append(type)
                            .append("</b><br>")
                            .append("Balance: $")
                            .append(current.getAccount(i).getBalance())
                            .append("<br><br>");
                }

                info.append("</html>");

                log.setText(info.toString());
            }
        });
        System.out.println("Customers = " + Bank.getNumberOfCustomers());
        // report btn
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                StringBuilder rep = new StringBuilder();

                rep.append("<html>")
                        .append("<h2>CUSTOMERS REPORT</h2><hr>");

                for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {

                    Customer customer = Bank.getCustomer(i);

                    rep.append("<b>")
                            .append(customer.getLastName())
                            .append(", ")
                            .append(customer.getFirstName())
                            .append("</b><br>");

                    for (int j = 0;
                         j < customer.getNumberOfAccounts();
                         j++) {

                        String type;

                        if (customer.getAccount(j)
                                instanceof SavingsAccount) {

                            type = "Savings Account";

                        } else {
                            type = "Checking Account";
                        }

                        rep.append("&nbsp;&nbsp;")
                                .append(type)
                                .append(": $")
                                .append(customer.getAccount(j).getBalance())
                                .append("<br>");
                    }

                    rep.append("<br>");
                }

                rep.append("</html>");

                log.setText(rep.toString());
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // load data from test.dat
    private static void loadData() {

        try (BufferedReader br =
                     new BufferedReader(new FileReader("test.dat"))) {

            String line;
            Customer currentCustomer = null;

            while ((line = br.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\t");

                if (parts.length == 3
                        && !parts[0].equals("S")
                        && !parts[0].equals("C")) {

                    Bank.addCustomer(parts[0], parts[1]);

                    currentCustomer =
                            Bank.getCustomer(
                                    Bank.getNumberOfCustomers() - 1);

                } else if (parts[0].equals("S")) {

                    currentCustomer.addAccount(
                            new SavingsAccount(
                                    Double.parseDouble(parts[1]),
                                    Double.parseDouble(parts[2])
                            )
                    );

                } else if (parts[0].equals("C")) {

                    currentCustomer.addAccount(
                            new CheckingAccount(
                                    Double.parseDouble(parts[1])
                            )
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        loadData();

        SWINGDemo demo = new SWINGDemo();
        demo.launchFrame();
    }
}