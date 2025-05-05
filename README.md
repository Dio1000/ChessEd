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

When opening the application in gui mode, the user will be greeted by the following main menu.

<img src="screenshots/chess_main.png" width="300"/>

By clicking "Play vs Human" or "Play vs AI", the user will be navigated to the board. 

<img src="screenshots/board_initial.png" width="500"/>

In order to play a move, users just need to click on the piece they would like to move (which will highlight the piece) and then to the square they would like to move the piece to. When moving a piece, standard chess gameplay rules will be applied. If the move is not legal, the piece will be not be selected anymore.

This is the board after a variation of the Queen's Gambit declined opening.

<img src="screenshots/board_opening.png" width="500"/>

Since this application is not meant for competitive play, the user will be able to reset the game (revert all pieces to their original places) at any point by preessing the "Reset Game" button. 

Users will also have the option to Login and Register in order to be able to track their stats, although that requires some tweaks in the code itself.

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
