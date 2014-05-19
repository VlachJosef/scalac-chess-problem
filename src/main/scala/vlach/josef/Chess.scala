package vlach.josef

sealed trait Piece {

  val priority: Int

  def getMoves(myPosition: Square, chessBoard: ChessBoard, f: (Square, Square) => Boolean): Set[Square] = {
    val predicate: Square => Boolean = {
      sq =>
        sq match {
          case s @ Square(_, _) if f(myPosition, s) => true
          case _ => false
        }
    }
    val occupied = chessBoard.occupiedSquare
    val collision = occupied.find { predicate }
    collision match {
      case Some(_) => Set.empty // piece on this position is taking some other piece
      case None => chessBoard.emptySquares.filter { predicate } // we can safely put this piece to this place 
    }
  }
}

case object Queen extends Piece {
  override def toString(): String = "Q"
  override val priority = 1

  def getMoves(myPosition: Square, chessBoard: ChessBoard): Set[Square] =
    getMoves(myPosition, chessBoard, (m: Square, s: Square) => (m.x == s.x ||
      m.y == s.y ||
      Math.abs(m.x - s.x) == Math.abs(m.y - s.y)))
}

case object Rook extends Piece {
  override def toString(): String = "R"
  override val priority = 2

  def getMoves(myPosition: Square, chessBoard: ChessBoard): Set[Square] =
    getMoves(myPosition, chessBoard, (m: Square, s: Square) => (m.x == s.x || m.y == s.y))
}

case object Bishop extends Piece {
  override def toString(): String = "B"
  override val priority = 3

  def getMoves(myPosition: Square, chessBoard: ChessBoard): Set[Square] =
    getMoves(myPosition, chessBoard, (m: Square, s: Square) => (Math.abs(m.x - s.x) == Math.abs(m.y - s.y)))
}

case object Knight extends Piece {
  override def toString(): String = "N"
  override val priority = 4

  def getMoves(myPosition: Square, chessBoard: ChessBoard): Set[Square] =
    getMoves(myPosition, chessBoard, (m: Square, s: Square) => ((s.x == m.x && s.y == m.y) ||
      (Math.abs(s.x - m.x) == 1 &&
        Math.abs(s.y - m.y) == 2) ||
        (Math.abs(s.x - m.x) == 2 &&
          Math.abs(s.y - m.y) == 1)))
}

case object King extends Piece {
  override def toString(): String = "K"
  override val priority = 5

  def getMoves(myPosition: Square, chessBoard: ChessBoard): Set[Square] =
    getMoves(myPosition, chessBoard, (m: Square, s: Square) => (Math.abs(s.y - m.y) <= 1 && Math.abs(s.x - m.x) <= 1))
}

case class Square(x: Int, y: Int)
case class Position(piece: Piece, square: Square)
case class ChessBoard(x: Int, y: Int, positions: Set[Position], occupiedSquare: Set[Square], emptySquares: Set[Square]) {
  override def hashCode = {
    positions.hashCode
  }

  override def equals(o: Any) = o match {
    case that: ChessBoard => that.positions.equals(this.positions)
    case _ => false
  }
  override def toString(): String = {
    val str = for (
      y1 <- 1 to y;
      x1 <- 1 to x
    ) yield {
      val s = positions.find(c => c match {
        case Position(_, Square(x2, y2)) if x2 == x1 && y2 == y1 => true
        case _ => false
      }) match {
        case Some(p) => p.piece.toString // square with some Piece
        case None => {
          emptySquares.find(s => s match {
            case Square(x2, y2) if x2 == x1 && y2 == y1 => true
            case _ => false
          }) match {
            case Some(s) => "*" // empty square
            case None => "-" // square protected by some Piece
          }
        }
      }
      if (x == x1) {
        s + "\n"
      } else {
        s
      }
    }
    str.mkString
  }
}

