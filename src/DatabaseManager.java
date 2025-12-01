import java.sql.*;
import java.util.ArrayList;
import java.io.File;

import javax.swing.JOptionPane;

public class DatabaseManager {

    private Connection conn;

    // Connect to SQLite database (company.db)
    public boolean connect(String dbName) {
        try {
            // Load SQLite driver (MUST have sqlite-jdbc-3.51.1.0.jar on classpath)
            Class.forName("org.sqlite.JDBC");

            // Resolve DB file relative to where the program is started
            File dbFile = new File(dbName);
            if (!dbFile.exists()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Database file not found:\n" + dbFile.getAbsolutePath(),
                        "DB Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }

            // Use absolute path so working directory cannot break this
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            conn = DriverManager.getConnection(url);

            // Activate foreign key support
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }

            return true;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "SQLite JDBC driver not found.\n" +
                    "Make sure sqlite-jdbc-3.51.1.0.jar is on the classpath.",
                    "DB Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "SQL error: " + e.getMessage(),
                    "DB Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Unexpected DB error: " + e.getMessage(),
                    "DB Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    public ArrayList<String> getDepartments() {
        ArrayList<String> list = new ArrayList<>();
        try {
            String sql = "SELECT Dname FROM DEPARTMENT";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                list.add(rs.getString("Dname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getProjects() {
        ArrayList<String> list = new ArrayList<>();
        try {
            String sql = "SELECT Pname FROM PROJECT";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                list.add(rs.getString("Pname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> searchEmployees(
            ArrayList<String> depts, boolean notDept,
            ArrayList<String> projs, boolean notProj) {

        ArrayList<String> results = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT DISTINCT E.Fname, E.Lname ");
            sb.append("FROM EMPLOYEE E ");
            sb.append("LEFT JOIN DEPARTMENT D ON E.Dno = D.Dnumber ");
            sb.append("LEFT JOIN WORKS_ON W ON E.Ssn = W.Essn ");
            sb.append("LEFT JOIN PROJECT P ON W.Pno = P.Pnumber ");
            sb.append("WHERE 1=1 ");

            // Department filters
            if (!depts.isEmpty()) {
                sb.append(" AND D.Dname ");
                sb.append(notDept ? "NOT IN (" : "IN (");

                for (int i = 0; i < depts.size(); i++) {
                    sb.append("'").append(depts.get(i)).append("'");
                    if (i < depts.size() - 1) sb.append(", ");
                }
                sb.append(") ");
            }

            // Project filters
            if (!projs.isEmpty()) {
                sb.append(" AND P.Pname ");
                sb.append(notProj ? "NOT IN (" : "IN (");

                for (int i = 0; i < projs.size(); i++) {
                    sb.append("'").append(projs.get(i)).append("'");
                    if (i < projs.size() - 1) sb.append(", ");
                }
                sb.append(") ");
            }

            ResultSet rs = conn.createStatement().executeQuery(sb.toString());

            while (rs.next()) {
                results.add(rs.getString("Fname") + " " + rs.getString("Lname"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
