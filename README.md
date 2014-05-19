scalac-chess-problem
====================

Program to find all unique configurations of a set of normal chess pieces on a chess board with dimensions M×N where none of the pieces is in a position to take any of the others. Assume the colour of the piece does not matter, and that there are no pawns among the pieces.

To build and run the code:

    $ sbt run

If run as is, program will provide solution for  **7×7 board with 2 Kings, 2 Queens, 2 Bishops and 1 Knight**.

If you want provide your own board size and composition of pieces, then it is required to redefine board size [here](https://github.com/VlachJosef/scalac-chess-problem/blob/master/src/main/scala/vlach/josef/ChessProblem.scala#L70-L71]) and composition of pieces [here](https://github.com/VlachJosef/scalac-chess-problem/blob/master/src/main/scala/vlach/josef/ChessProblem.scala#L81)
