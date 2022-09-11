package scala
import java.awt.{BorderLayout, Color, FlowLayout, Font, GridLayout}
import java.io.File
import javax.swing._
import javax.imageio.ImageIO
import scala.util.control.Breaks.{break, breakable}

class CheckersEngine {

 var board: JPanel = null
   val rows: Int = 8
   val cols: Int = 8

 val horizontalIndex: HorizontalIndex = new HorizontalIndex(cols)
 val verticalIndex: VerticalIndex = new VerticalIndex(rows)

  var firstPlayerTurn: Boolean = true
  val player1Pieces = "o"
  val player1King = "O"
  val player2Pieces = "x"
  val player2King = "X"
  val empty = ""

  val blackChecker : File = new File("src/main/scala/resources/black checker.jpg")
  val redChecker : File = new File("src/main/scala/resources/red checker.jpg")
  val blackKingChecker : File = new File("src/main/scala/resources/black king checker.jpg")
  val redKingChecker : File = new File("src/main/scala/resources/red king checker.jpg")



  def isValidInput(textField : JTextField, frame : JFrame): Boolean = {
    val input = textField.getText
    // validate input
    if (input.length() != 4
      || !horizontalIndex.range.contains(input.charAt(0))
      || !verticalIndex.range.contains(input.charAt(1) - 48)
      || !horizontalIndex.range.contains(input.charAt(2))
      || !verticalIndex.range.contains(input.charAt(3) - 48)
    ) {
      errorMessage("invalid input", frame)
      return false
    }
    true
  }


  def isValidPiece(indexArr: Array[Int], source: String, des: String, frame : JFrame): Boolean = {
    val player = getCurrentPlayer
    source match {
      case player._1 | player._2 =>
        if (!checkValidMove(indexArr, source, des, frame)) {
          println("I will Return  ")
           false
        } else {
           true
        }

      case _ =>
        errorMessage("Not Your piece", frame)
        false
    }
  }

  def Move(textField : JTextField, sourceCell: JLabel, destinationCell: JLabel, indexArr: Array[Int], frame : JFrame): Unit = {
    val destinationIndexRow = indexArr(2)
    if (checkUpgrade(sourceCell.getText, destinationIndexRow)) {
      println("upgrade true")
      destinationCell.setText(upgrade(sourceCell.getText, frame, textField))
      println(upgrade(sourceCell.getText, frame, textField))
    } else {
      destinationCell.setText(sourceCell.getText)
    }
    sourceCell.setText(empty)
    setIcon(destinationCell)
    setIcon(sourceCell)
    textField.setText(empty)
    firstPlayerTurn = !firstPlayerTurn
  }


  // src is either x or o only because checkUpgrade filters O and X
  def upgrade(src: Any, frame : JFrame, textField : JTextField): String = {
    src match {
      case this.player1Pieces => player1King
      case this.player2Pieces => player2King
      case _ =>
        errorMessage("Wrong Upgrade!!", frame)
        null
    }
  }

  def getIndexArr(textField : JTextField): Array[Int] = {
    // get int values of index
    val input = textField.getText
    val sourceIndexRow: Int = input.charAt(1) - 49
    val sourceIndexCol: Int = input.charAt(0) - 65
    val destinationIndexRow: Int = input.charAt(3) - 49
    val destinationIndexCol: Int = input.charAt(2) - 65
    val indexArr: Array[Int] = Array(sourceIndexRow, sourceIndexCol, destinationIndexRow, destinationIndexCol)
    indexArr
  }

  def checkUpgrade(src: String, destinationRow: Int): Boolean = {
    if (src == player1Pieces && destinationRow == 7) {
      return true
    } else if (src == player2Pieces && destinationRow == 0) {
      return true
    }
    false
  }

  def getOpponentPieces(player: String): (String, String) = player match {
    case this.player1Pieces | this.player1King => (player2Pieces, player2King)
    case _ => (player1Pieces, player1King)
  }

  def getCurrentPlayer: (String, String) = {
    if (firstPlayerTurn) {
      return (player1Pieces, player1King)
    }
    (player2Pieces, player2King)
  }

  def CheckForcedEatingDone(sourceIndex: Int, destinationIndex: Int, frame : JFrame): Boolean = {
    val moves = getEatingMoves(getCurrentPlayer)
    println(moves)
    println(moves.isEmpty)
    if (moves.nonEmpty && !moves.contains((sourceIndex, destinationIndex))) {
      errorMessage("You must eat", frame)
      return false
    }
    true
  }

  def getOrdinate(arr: Array[Int]): Int = {
    if (arr(2) - arr(0) * arr(3) - arr(1) > 0) {
      if (arr(2) - arr(0) > 0) return 1
      else return 3
    }
    if (arr(2) - arr(0) > 0) 2
    else 4
  }

  def checkBoundary(x: Int, y: Int): Boolean = {
    if (x >= 0 && x <= 7) {
      if (y >= 0 && y <= 7)
        return true
    }
    false
  }

  def getBoardPos(row: Int, col: Int): Int = {
    return cols * ((rows - 1) - row) + col
  }

  def getComponent(getPos: (Int, Int) => Int): (Int, Int) => String = {
    (x: Int, y: Int) => board.getComponent(getPos(x, y)).asInstanceOf[JLabel].getText
  }

  def validEating(eaten: String, src: String, frame : JFrame): Boolean = {
    println("Eaten " + eaten)
    println("src: " + src)
    if (eaten == empty) {
      errorMessage("Invalid Move", frame)
      false
    }
    else if (isDifferent(src, eaten)) {
      true
    }
    else {
      errorMessage("Error Trying to eat your own piece", frame)
      false
    }
  }

  def isDifferent(currentPlayer: String, otherPlayer: String): Boolean = {
    val opponent = getOpponentPieces(currentPlayer)
    val opponent2 = getOpponentPieces(otherPlayer)
    opponent match {
      case (opponent2._1, opponent2._2) =>
        return false
      case _ =>
        println("different")
        return true
    }
  }

  def getEatenCell(indexArr: Array[Int]): (Int, Int) = {
    val eatenRow = if (indexArr(2) > indexArr(0)) indexArr(0) + 1 else indexArr(0) - 1
    val eatenColumn = if (indexArr(3) > indexArr(1)) indexArr(1) + 1 else indexArr(1) - 1
    return (eatenRow, eatenColumn)
  }

  def checkValidMove(indexArr: Array[Int], srcComp: String, destComp: String, frame : JFrame): Boolean = {
    // input in border checked after taking input
    // if destination  has piece or not diagonal move then invalid and enter valid input
    var returnFlag = false
    if (destComp != empty) {
      errorMessage("Invalid Move", frame)
      return false
    }
     if ((indexArr(2) - indexArr(0)).abs != (indexArr(3) - indexArr(1)).abs) {
      errorMessage("Invalid Move", frame)
      return false
    }
     if ((indexArr(2) - indexArr(0)).abs > 2 || (indexArr(2) - indexArr(0)).abs < 0 || indexArr(2) == indexArr(0)) {
      errorMessage("Invalid Move", frame)
      return false
    }
    if ((indexArr(3) - indexArr(1)).abs > 2 || (indexArr(3) - indexArr(1)).abs < 0 || indexArr(3) == indexArr(1)) {
      errorMessage("Invalid Move", frame)
      return false
    }


    if (firstPlayerTurn && srcComp != player1King && indexArr(2) < indexArr(0)) {
      errorMessage("invalid player 1 want to go back", frame)
      return false
    }
    if (!firstPlayerTurn && srcComp != player2King && indexArr(2) > indexArr(0)) {
      errorMessage("invalid player 2 back", frame)
      return false
    }
    true
  }

  def errorMessage(message: String, frame : JFrame): Unit = {
    val panel = new JPanel
    val layout = new FlowLayout
    panel.setLayout(layout)
    JOptionPane.showMessageDialog(frame, message,
      "Error", JOptionPane.WARNING_MESSAGE)
  }

  def getEatingMoves(player: (String, String)): List[(Int, Int)] = {
    val opponent = getOpponentPieces(player._1)
    var res: List[(Int, Int)] = List()
    for (i <- 0 to rows - 1) {
      for (j <- 0 to cols - 1) {
        /*println(i + " " + j)*/
        val currentCell = getComponent(getBoardPos)(i, j)
        if (currentCell == player._1 || currentCell == player._2) {
          val list: List[(Int, Int)] = IndicesThatCanBeEaten(getComponent(getBoardPos), (i, j), opponent)
          res = res.appendedAll(list)
        }
      }
    }
    res
  }

  // Given index of the piece and the enemy it will return the indices that can be eaten by this piece
  def IndicesThatCanBeEaten(getComponent: (Int, Int) => String, arr: (Int, Int), eatenAssumed: (String, String)): List[(Int, Int)] = {
    // as array zero based diagonal & sqr diagonal (i) means quad i+1
    var diagonals: List[String] = List()
    var sqrDiagonal = List[String]()
    var diagonalsCellNum = List[Int]()
    var sqrDiagonalCellNum = List[Int]()
    val unitStep = List(1, -1)

    unitStep.foreach(a =>
      unitStep.foreach(b =>
        if (checkBoundary(arr._1 + a, arr._2 + b)) {
          diagonals = diagonals.appended(getComponent(arr._1 + a, arr._2 + b))
          diagonalsCellNum = diagonalsCellNum.appended(getBoardPos(arr._1 + a, arr._2 + b))
        }
        else {
          diagonals = diagonals.appended("i")
          diagonalsCellNum = diagonalsCellNum.appended(-1)
        }
      )
    )

    val twoSteps = List(2, -2)
    twoSteps.foreach(a =>
      twoSteps.foreach(b =>
        if (checkBoundary(arr._1 + a, arr._2 + b)) {
          /*println(a + " " + b)*/
          sqrDiagonal = sqrDiagonal.appended(getComponent(arr._1 + a, arr._2 + b))
          sqrDiagonalCellNum = sqrDiagonalCellNum.appended(getBoardPos(arr._1 + a, arr._2 + b))
        }
        else {
          /*println(a + " " + b)*/
          sqrDiagonal = sqrDiagonal.appended("i")
          sqrDiagonalCellNum = sqrDiagonalCellNum.appended(-1)
        }
      ))

    var res = List[(Int, Int)]()
    val pos = getBoardPos(arr._1, arr._2)
    for (i <- 0 to 3) {
      breakable {
        if (getComponent(arr._1, arr._2) != player2King && getComponent(arr._1, arr._2) != player1King) {
          if (firstPlayerTurn && i > 1) {
            break
          }
          else if (!firstPlayerTurn && i < 2) break
        }
        if (diagonals(i) == eatenAssumed._1 || diagonals(i) == eatenAssumed._2) {
          if (sqrDiagonal(i) == empty)
            res = res.appended((pos, sqrDiagonalCellNum(i)))
        }
      }
    }
    res
  }

  def setIcon(jLabel: JLabel) : Unit = {
    jLabel.getText match {
      case "o" => jLabel.setIcon(new ImageIcon(ImageIO.read(redChecker).getScaledInstance(50, 50, 5)))
      case "O" => jLabel.setIcon(new ImageIcon(ImageIO.read(redKingChecker).getScaledInstance(50, 50, 5)))
      case "x" => jLabel.setIcon(new ImageIcon(ImageIO.read(blackChecker).getScaledInstance(50, 50, 5)))
      case "X" => jLabel.setIcon(new ImageIcon(ImageIO.read(blackKingChecker).getScaledInstance(50, 50, 5)))
      case "" =>  jLabel.setIcon(null)
    }
  }

   def controller(textField : JTextField, frame : JFrame): Unit = {
    if (!isValidInput(textField, frame)) {
      return
    }

    val indexArr = getIndexArr(textField)
    val sourceIndex: Int = getBoardPos(indexArr(0), indexArr(1))
    val destinationIndex: Int = getBoardPos(indexArr(2), indexArr(3))
    // get square content using square number
    val sourceCell = board.getComponent(sourceIndex).asInstanceOf[JLabel]
    val destinationCell = board.getComponent(destinationIndex).asInstanceOf[JLabel]


    if (!isValidPiece(indexArr, sourceCell.getText, destinationCell.getText, frame)) {
      return
    }
    if (!CheckForcedEatingDone(sourceIndex, destinationIndex, frame)) {
      return
    }

    if ((indexArr(2) - indexArr(0)).abs == 2) {
      val eatenIndex = getEatenCell(indexArr)
      val eaten = getComponent(getBoardPos)(eatenIndex._1, eatenIndex._2)
      if (validEating(eaten, sourceCell.getText, frame)) {
        println("Eat!!!!!!!!!!!!!!!")
        val eatenCell = board.getComponent(getBoardPos(eatenIndex._1, eatenIndex._2)).asInstanceOf[JLabel]
        eatenCell.setText(empty)
        setIcon(eatenCell)
      } else {
        return
      }
    }
    Move(textField, sourceCell, destinationCell, indexArr, frame)

  };
   def drawer(panel : JPanel, frame : JFrame) : Unit = {
    board = new JPanel()
    board.setLayout(new GridLayout(rows, cols))
     for (i <- 1 to rows) {
       for (j <- 1 to cols) {
         val jLabel = new JLabel(empty, SwingConstants.CENTER)
         jLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 1))

         jLabel.setBackground(new Color(0, 0, 0))
         if (i % 2 == 0 && j % 2 != 0 || (i % 2 != 0 && j % 2 == 0)) {
           jLabel.setBackground(new Color(255, 255, 255))
         }
         jLabel.setForeground(new Color(255, 255, 255))

         if (i <= 3) {
           if (i % 2 == 0 && j % 2 != 0 || (i % 2 != 0 && j % 2 == 0)) {
             jLabel.setIcon(new ImageIcon(ImageIO.read(blackChecker).getScaledInstance(50, 50, 5)))
             jLabel.setText(player2Pieces)
           }
           else jLabel.setText(empty)
         }
         else if (i >= 6) {
           if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)){
             jLabel.setIcon(new ImageIcon(ImageIO.read(redChecker).getScaledInstance(50, 50, 5)))
             jLabel.setText(player1Pieces)
           }
           else jLabel.setText(empty)
         }
         else jLabel.setText(empty)


         jLabel.setOpaque(true)
         board.add(jLabel)
       }
     }
    firstPlayerTurn = true
    frame.add(horizontalIndex.jPanel, BorderLayout.NORTH)
    panel.add(horizontalIndex.copy().jPanel, 0)
    frame.add(verticalIndex.jPanel, BorderLayout.WEST)
    frame.add(verticalIndex.copy().jPanel, BorderLayout.EAST)
    frame.add(board, BorderLayout.CENTER)
    frame.revalidate()
  }
   def getController() : (JTextField, JFrame) => Unit = return this.controller;
  def getDrawer() : (JPanel, JFrame) => Unit = return this.drawer;
}