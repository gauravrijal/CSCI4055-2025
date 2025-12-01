**CSCI 4055 – Employee Search GUI Project**
This repository contains the final implementation of the Employee Search GUI project for CSCI 4055. The program includes a Java Swing interface and a SQLite database backend, allowing users to filter employees by Department, Project, and optional NOT conditions. All data is stored in the included company.db file.

**How To Use This Project**

**1. Clone or download the repository**
You may download the ZIP or clone using:
git clone https://github.com/gauravrijal/CSCI4055-2025.git

**2. Import the project into Eclipse**
File → Import → “Existing Projects into Workspace” → Select this project folder → Finish.

**3. Add the SQLite JDBC driver (IMPORTANT)**
The file sqlite-jdbc.jar in the repo must be added to the Build Path.
Right-click project → Build Path → Add External Archives → Select sqlite-jdbc.jar.
Without this step, the program will show: ClassNotFoundException: org.sqlite.JDBC

**4. Run the program**
Open EmployeeSearchFrame.java → Run As → Java Application.
In the GUI’s database field, enter: company.db
Click Fill to load Department and Project data.

**Repository Notes / Development History**
During development, we attempted to push changes one by one. At one point, we accidentally pushed unnecessary .class files, and also pushed to the master branch once, which caused confusion with the repository state. Because we were running short on time, we finalized the entire project locally, verified everything worked, and then pushed the completed version to GitHub.
This repository now contains only the final, clean, working implementation.

**File Structure**

src/
  - DatabaseManager.java
  - EmployeeSearchFrame.java

company.db – Full SQLite database

company_sqlite.sql – Schema + insert statements

sqlite-jdbc.jar – Required JDBC driver

GitNotes.txt

README.md

**Credits**

Instructor: Lon Smith, Ph.D.

Course: CSCI 4055

Group Participants:
  - Gaurav Rijal
  - Prajwol Ramtel
  - Court Lopez
