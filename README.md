# Bangkok Transit Navigator

**Author:** Sagar Koirala\
**Student ID:** 6501023620159\
**Course:** DSA (Data Structures and Algorithms)\
**Submission for:** Dr. Ruslee Sutthaweekul, ECE KMUTNB

---

## 1. Project Objective

This project is a command-line Java application that models the Bangkok mass transit network as a graph to find optimal routes between stations. It implements Dijkstra's algorithm, focusing on efficient data structures and robust object-oriented design to handle real-world scenarios like time-based travel, line transfers, and station closures.

For a comprehensive analysis of the design, algorithms, and data structures, please see the full **Project Report.pdf**.

## 2. Features

This application successfully implements all the requirements of the assignment, including:

*   **Fastest Route Calculation:** Finds the path with the minimum travel time, including a 10-minute penalty for transfers (Model B).
*   **Fewest Transfers Route:** Calculates an alternative path that minimizes the number of line changes, even if it takes longer.
*   **Line-by-Line Visualization:** Displays the route in a clear, turn-by-turn format, indicating which transit line to take for each segment of the journey.
*   **Exclude Station (Detour):** Allows the user to "close" one or more stations and calculates the best alternative route.
*   **Interactive Menu:** An easy-to-use menu to explore the different routing options after an initial path is found.
*   **Robust Error Handling:** Validates all user input and provides clear error messages for unknown station names.

## 3. Technology Stack

*   **Language:** Java
*   **JDK Version:** JDK 11 or higher
*   **Build/Run:** Standard `javac` and `java` command-line tools

## 4. Project Structure

The project is organized into a clean package structure to separate concerns, making the codebase modular and easy to navigate.

```
BangkokTransitNavigator/
├── src/
│   └── bangkoktransitnavigator/
│       ├── algorithm/
│       │   ├── Pathfinder.java         # Interface for pathfinding strategies
│       │   └── DijkstraPathfinder.java # Weighted-sum Dijkstra's implementation
│       ├── graph/
│       │   └── TransitGraph.java       # Manages graph creation from CSV
│       ├── model/
│       │   ├── Station.java
│       │   ├── Edge.java
│       │   ├── Cost.java
│       │   ├── RouteLeg.java
│       │   └── RouteResult.java
│       └── Main.java                   # Main application entry point and UI
├── data/
│   └── connections.csv                 # The graph data file
├── report/
│   └── Project Report.pdf              # The detailed project analysis document
└── README.md                           # This file
```

## 5. How to Compile and Run

The project can be run from a standard terminal or any Java-compatible IDE.

### 5.1. Using the Terminal

**Prerequisites:**
*   Java JDK (version 11 or newer) must be installed and accessible from the command line.

**Steps:**
1.  Open a terminal and navigate to the project's **`src`** directory.
2.  Compile all `.java` files using the following command:
    ```shell
    javac -d . bangkoktransitnavigator/model/*.java bangkoktransitnavigator/graph/*.java bangkoktransitnavigator/algorithm/*.java bangkoktransitnavigator/Main.java
    ```
3.  Run the main application. Make sure you are still in the `src` directory.
    ```shell
    java bangkoktransitnavigator.Main
    ```

### 5.2. Using an IDE (IntelliJ IDEA, VS Code, etc.)

**Prerequisites:**
*   An IDE with Java support (e.g., IntelliJ IDEA, VS Code with the Java Extension Pack).

**Steps:**
1.  Open the root `BangkokTransitNavigator` folder as a new project in your IDE.
2.  Locate the `src/bangkoktransitnavigator/Main.java` file.
3.  Click the "Run" button provided by the IDE, which is typically located next to the `main` method.

## 6. Example Usage

Here is a sample interaction with the program:
```shell
Loading transit data...
Data loaded. Welcome to the Bangkok Transit Navigator!

Enter your starting station (or type 'exit' to quit):
Lak Song
Enter your destination station:
Kheha

--- Fastest Route ---
Time: 117 minutes
Transfers: 2
--- Detailed Route ---
  Start at: Lak Song
  -> Take MRT Blue Line
     - Bang Khae
     - Phasi Charoen
     - Phetkasem 48
     - Bang Wa
  -> Take BTS Silom Line
     - Wutthakat
     - Talat Phlu
     - Pho Nimit
     - Wongwian Yai
     - Krung Thon Buri
     - Saphan Taksin
     - Surasak
     - Saint Louis
     - Chong Nonsi
     - Sala Daeng
     - Ratchadamri
     - Siam
  -> Take BTS Sukhumvit Line
     - Chit Lom
     - Phloen Chit
     - Nana
     - Asok
     - Phrom Phong
     - Thong Lo
     - Ekkamai
     - Phra Khanong
     - On Nut
     - Bang Chak
     - Punnawithi
     - Udom Suk
     - Bang Na
     - Bearing
     - Samrong
     - Pu Chao
     - Chang Erawan
     - Royal Thai Naval Academy
     - Chang Erawan
     - Royal Thai Naval Academy
     - Pak Nam
     - Pak Nam
     - Srinagarindra
     - Srinagarindra
     - Phraek Sa
     - Sai Luat
     - Phraek Sa
     - Sai Luat
     - Sai Luat
     - Kheha
     - Kheha
--------------

Options:
Options:
1. Find other route with lowest transfer
2. Exclude station and find detour
3. Restart (new journey)
4. Exit
Choose an option (1-4): 4
Thank you for using the Bangkok Transit Navigator!
```