/**
 * Author: Lon Smith, Ph.D.
 * Description: This is the framework for the database program. Additional requirements and functionality
 *    are to be built by you and your group.
 *
 * Functionality added to the framework of database program
 */

import java.awt.EventQueue;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EmployeeSearchFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private DatabaseManager db = new DatabaseManager();

    private JPanel contentPane;
    private JTextField txtDatabase;

    private DefaultListModel<String> department = new DefaultListModel<>();
    private DefaultListModel<String> project = new DefaultListModel<>();

    private JList<String> lstDepartment;
    private JList<String> lstProject;

    private JCheckBox chckbxNotDept;
    private JCheckBox chckbxNotProject;

    private JTextArea textAreaEmployee;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EmployeeSearchFrame frame = new EmployeeSearchFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public EmployeeSearchFrame() {
        setTitle("Employee Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 460, 380);  // slightly taller for spacing
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- Top: Database controls ---

        JLabel lblNewLabel = new JLabel("Database:");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblNewLabel.setBounds(21, 23, 59, 14);
        contentPane.add(lblNewLabel);

        txtDatabase = new JTextField();
        txtDatabase.setBounds(90, 20, 193, 20);
        contentPane.add(txtDatabase);
        txtDatabase.setColumns(10);

        JButton btnDBFill = new JButton("Fill");
        btnDBFill.setFont(new Font("Times New Roman", Font.BOLD, 12));
        btnDBFill.setBounds(307, 19, 68, 23);
        contentPane.add(btnDBFill);

        // --- Middle: Department & Project lists + NOT checkboxes ---

        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblDepartment.setBounds(52, 63, 89, 14);
        contentPane.add(lblDepartment);

        JLabel lblProject = new JLabel("Project");
        lblProject.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblProject.setBounds(265, 63, 60, 14);
        contentPane.add(lblProject);

        // Department list
        lstDepartment = new JList<>(department);
        lstDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstDepartment.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollDept = new JScrollPane(lstDepartment);
        scrollDept.setBounds(36, 84, 172, 80);
        contentPane.add(scrollDept);

        // Project list
        lstProject = new JList<>(project);
        lstProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstProject.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollProj = new JScrollPane(lstProject);
        scrollProj.setBounds(235, 84, 172, 80);
        contentPane.add(scrollProj);

        // NOT checkboxes – clearly under each list, on background
        chckbxNotDept = new JCheckBox("Not");
        // center under department list
        chckbxNotDept.setBounds(36 + (172 - 60) / 2, 172, 60, 23);
        contentPane.add(chckbxNotDept);

        chckbxNotProject = new JCheckBox("Not");
        // center under project list
        chckbxNotProject.setBounds(235 + (172 - 60) / 2, 172, 60, 23);
        contentPane.add(chckbxNotProject);

        // --- Bottom: Employee results + buttons ---

        JLabel lblEmployee = new JLabel("Employee");
        lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblEmployee.setBounds(52, 205, 89, 14);
        contentPane.add(lblEmployee);

        textAreaEmployee = new JTextArea();
        textAreaEmployee.setLineWrap(true);
        textAreaEmployee.setWrapStyleWord(true);
        textAreaEmployee.setEditable(false);
        JScrollPane scrollEmp = new JScrollPane(textAreaEmployee);
        scrollEmp.setBounds(36, 223, 371, 100);
        contentPane.add(scrollEmp);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(100, 334, 100, 23);
        contentPane.add(btnSearch);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(250, 334, 100, 23);
        contentPane.add(btnClear);

        // --- Button actions ---

        // Fill button logic
        btnDBFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String dbName = txtDatabase.getText().trim();

                if (dbName.isEmpty()) {
                    JOptionPane.showMessageDialog(EmployeeSearchFrame.this,
                            "Please enter a database name (e.g., company.db).");
                    return;
                }

                if (!db.connect(dbName)) {
                    JOptionPane.showMessageDialog(EmployeeSearchFrame.this,
                            "Could not connect to database");
                    return;
                }

                department.clear();
                project.clear();

                ArrayList<String> deptList = db.getDepartments();
                for (String d : deptList) {
                    department.addElement(d);
                }

                ArrayList<String> projList = db.getProjects();
                for (String p : projList) {
                    project.addElement(p);
                }
            }
        });

        // Search button logic
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ArrayList<String> selectedDepts =
                        new ArrayList<>(lstDepartment.getSelectedValuesList());
                ArrayList<String> selectedProjs =
                        new ArrayList<>(lstProject.getSelectedValuesList());

                boolean notDept = chckbxNotDept.isSelected();
                boolean notProj = chckbxNotProject.isSelected();

                ArrayList<String> employees = db.searchEmployees(
                        selectedDepts, notDept,
                        selectedProjs, notProj);

                textAreaEmployee.setText("");
                for (String emp : employees) {
                    textAreaEmployee.append(emp + "\n");
                }
            }
        });

        // Clear button logic
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaEmployee.setText("");
            }
        });
    }
}
