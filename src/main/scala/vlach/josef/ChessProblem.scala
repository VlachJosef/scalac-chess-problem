package vlach.josef

import scala.annotation.tailrec

object ChessProblem extends App {

  def putPieceOnChessBoard(piece: Piece, board: ChessBoard): List[ChessBoard] = {
    @tailrec
    def loop(piece: Piece, emptySquares: Set[Square], board: ChessBoard, acc: List[ChessBoard], calls: Long): List[ChessBoard] = {
      if (emptySquares.isEmpty) {
        acc
      } else {
        val h = emptySquares.head
        val t = emptySquares.tail
        val position = Position(piece, h)
        val moves = getMoves(position, board);
        if (moves.isEmpty) {
          loop(piece, t, board, acc, calls + 1)
        } else {
          val newSquares = board.emptySquares.filterNot(moves)
          val cb = ChessBoard(board.x, board.y, board.positions + position, board.occupiedSquare + h, newSquares)
          loop(piece, t, board, cb :: acc, calls + 1)
        }
      }
    }
    loop(piece, board.emptySquares, board, Nil, 1)
  }

  def getMoves(position: Position, chessBoard: ChessBoard): Set[Square] = {
    position.piece match {
      case Queen => Queen.getMoves(position.square, chessBoard)
      case Rook => Rook.getMoves(position.square, chessBoard)
      case Bishop => Bishop.getMoves(position.square, chessBoard)
      case Knight => Knight.getMoves(position.square, chessBoard)
      case King => King.getMoves(position.square, chessBoard)
    }
  }

  def getResults(input: List[Piece], cb: ChessBoard): Set[ChessBoard] = {
    @tailrec
    def loopOverPieces(input: List[Piece], cbs: List[ChessBoard]): Set[ChessBoard] = {
      println(s"loopOverPieces input size ${input.size}, cbs.size: ${cbs.size}: input: $input")
      input match {
        case h :: t => {
          loopOverPieces(t, loopOverChessBoard(h, cbs, Set.empty, 1))
        }
        case Nil => cbs.toSet
      }
    }
    @tailrec
    def loopOverChessBoard(piece: Piece, cbs: List[ChessBoard], acc: Set[ChessBoard], calls: Long): List[ChessBoard] = {
      if (calls % 1000 == 0) {
        println(s"loopOverChessboard was called $calls, cbs.size: ${cbs.size}, acc.size: ${acc.size}")
      }
      cbs match {
        case cb :: cbs => {
          if (cb.emptySquares.isEmpty) {
            loopOverChessBoard(piece, cbs, acc, calls + 1)
          } else {
            val ncb = putPieceOnChessBoard(piece, cb)
            loopOverChessBoard(piece, cbs, acc ++ ncb.toSet, calls + 1)
          }
        }
        case _ => acc.toList
      }
    }
    loopOverPieces(input.sortBy(_.priority), List(cb))
  }

  val x = 7
  val y = 7

  val fullBoard = for (
    y <- 1 to y;
    x <- 1 to x
  ) yield Square(x, y)

  //val input = List(Queen, Queen)
  //val input = List(Rook, King, King)
  //val input = List(Rook, Rook, Knight, Knight, Knight, Knight)
  val input= List(Queen, Queen, Bishop, Bishop, Knight, King, King)
  val cb = ChessBoard(x, y, Set.empty, Set.empty, fullBoard.toSet)
  val t0 = System.nanoTime()
  val results = getResults(input, cb)
  val t1 = System.nanoTime()

  // Lets print some solutions
  results.take(10).foreach(cb => {
    println(cb)
  })
  println(s"Number of solutions: ${results.size}")
  println("Elapsed time: " + (t1 - t0) / (1000 * 1000) + "ms")

}