# ChessEd

## About the Project
ChessEd is a Java application with a Swing graphical interface which implements standard Chess gameplay along with an AI-driven move evaluator.

## Features

### Standard Application
- **Standard gameplay**: Implementation of the full game of Chess with a beautiful graphical interface.
- **Intuitive interface**: ChessEd offers an intuitive interface which makes the app easily navigable.
- **Database Connectivity**: Optional MySQL connectivity for users who want to store player stats.
- **Play against human**: Play against a human opponent on the same machine.
- **Play against AI**: Play against an AI that chooses moves using neural network-driven evaluation.

### AI-driven Move Evaluation
- **Move evaluation**: ML model written in Python for optimal move selection.
- **Alpha-Beta pruning**: Implementation of the Alpha-Beta algorithm to compute position scores.
- **Interoperability with Python**: Uses TCP sockets for communication between ChessEd and the engine.

## How to Use

Soon

## Prerequisites

Before running ChessEd, ensure you have the following installed:

1. **Java Development Kit (JDK) 11 or higher**  
   Required to compile and run the Java application.  
   Download from: [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://openjdk.java.net/install/)

2. **Python 3.8+ (Optional - Only for AI mode)**  
   Required to run the ChessEngine AI.  
   Download from: [Python Official Site](https://www.python.org/downloads/)

3. **ChessEngine (Optional - Only for AI mode)**  
   The neural network-based chess engine must be running locally.  
   Clone and set up from: [https://github.com/Dio1000/ChessEngine](https://github.com/Dio1000/ChessEngine)  
   Follow its README for installation and execution instructions.

4. **MySQL (Optional - Only for database features)**  
   Required if using player stats tracking.  
   Download from: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

## Usage

To build the `ChessEd.jar` file yourself, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Dio1000/ChessEd.git
   cd ChessEd
   ```
   
2. **Compile the project**:
   ```
   mvn clean install
   ```
After building, the ChessEd.jar file will be available in the ```target/``` directory.

### Running the Application
1. **Without AI (Human vs Human)**  
   ```bash
   java -jar ChessEd.jar

2. **With AI (Human Vs AI)**
   First, start by launching the engine
   ```bash
   python ChessEngine/engine_server.py
   ```

   Then, from the application, click the "Play vs AI" button.

## Contact

Email: [sandru.darian@gmail.com](mailto:sandru.darian@gmail.com)  

ChessEd: [https://github.com/Dio1000/ChessEd](https://github.com/Dio1000/ChessEd)  
AI Engine: [https://github.com/Dio1000/ChessEngine](https://github.com/Dio1000/ChessEngine)  
