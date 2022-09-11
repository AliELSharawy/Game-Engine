package scala

import java.awt.{BorderLayout, Color, FlowLayout, Font, GridLayout}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.{BorderFactory, ImageIcon, JFrame, JLabel, JOptionPane, JPanel, JTextField, SwingConstants}
import scala.collection.immutable.HashMap

class ChessEngine(){

  var board: JPanel = null
   val rows: Int = 8
   val cols: Int = 8

   val horizontalIndex: HorizontalIndex = new HorizontalIndex(cols)
   val verticalIndex: VerticalIndex = new VerticalIndex(rows)
   var firstPlayerTurn: Boolean = true

  val firstPlayerPieces: Array[String] = Array[String]("R", "N", "B", "Q", "K", "P")
  val secondPlayerPieces: Array[String] = Array[String]("r", "n", "b", "q", "k", "p")

  val blackPawn : File = new File("src/main/scala/resources/black pawn.png")
  val whitePawn : File = new File("src/main/scala/resources/white pawn.png")

  val blackRook : File = new File("src/main/scala/resources/black rook.png")
  val whiteRook : File = new File("src/main/scala/resources/white rook.png")

  val blackKnight : File = new File("src/main/scala/resources/black knight.png")
  val whiteKnight : File = new File("src/main/scala/resources/white knight.png")

  val blackBishop : File = new File("src/main/scala/resources/black bishop.png")
  val whiteBishop : File = new File("src/main/scala/resources/white bishop.png")

  val blackKing : File = new File("src/main/scala/resources/black king.png")
  val whiteKing : File = new File("src/main/scala/resources/white king.png")

  val blackQueen : File = new File("src/main/scala/resources/black queen.png")
  val whiteQueen : File = new File("src/main/scala/resources/white queen.png")

  def can_move (piece:String, input:String, sourceIndex:Int, destinationIndex:Int): Boolean ={
    val destinationCell = board.getComponent(destinationIndex).asInstanceOf[JLabel]
    (piece.toLowerCase(), input.charAt(0), input.charAt(1), input.charAt(2), input.charAt(3)) match {
      case ("r", x, _, w, _) if (x == w && input.length == 4) => {
        if (!rock_move(sourceIndex, destinationIndex, true)) return false else return true
      }
      case ("r", _, y, _, z) if (y == z && input.length == 4) => {
        if (!rock_move(sourceIndex, destinationIndex, false)) return false else return true
      }
      case ("b", x, y, w, z) if (Math.abs(x - w) == Math.abs(y - z) && input.length == 4) => {
        if (!bishop_move(sourceIndex, destinationIndex)) return false else return true
      }
      case ("q", x, _, w, _) if (x == w && input.length == 4) => {
        if (!rock_move(sourceIndex, destinationIndex, true)) return false else return true
      }
      case ("q", _, y, _, z) if (y == z && input.length == 4) => {
        if (!rock_move(sourceIndex, destinationIndex, false)) return false else return true
      }
      case ("q", x, y, w, z) if (Math.abs(x - w) == Math.abs(y - z) && input.length == 4) => {
        if (!bishop_move(sourceIndex, destinationIndex)) return false else return true
      }
      case ("n", x, y, w, z) if (Math.abs(x - w) == 1 && Math.abs(y - z) == 2 && input.length == 4) => return true
      case ("n", x, y, w, z) if (Math.abs(x - w) == 2 && Math.abs(y - z) == 1 && input.length == 4) => return true
      case ("k", x, y, w, z) if (x == w && Math.abs(y - z) == 1 && input.length == 4) => return true
      case ("k", x, y, w, z) if (Math.abs(x - w) == 1 && y == z && input.length == 4) => return true
      case ("k", x, y, w, z) if (Math.abs(x - w) == 1 && Math.abs(y - z) == 1 && input.length == 4) => return true
      case ("p", x, y, w, z) if (x == w && z - y == 1 && !secondPlayerPieces.contains(destinationCell.getText) && input.length == 4) => return true
      case ("p", x, y, w, z) if (x == w && z - y == -1 && !firstPlayerPieces.contains(destinationCell.getText) && input.length == 4) => return true
      case ("p", x, y, w, z) if (firstPlayerTurn && x == w && y == '2' && z == '4' &&
        !secondPlayerPieces.contains(destinationCell.getText) &&
        board.getComponent(sourceIndex - 8).asInstanceOf[JLabel].getText() == "" && input.length == 4) => return true

      case ("p", x, y, w, z) if (!firstPlayerTurn && x == w && y == '7' && z == '5' &&
        !firstPlayerPieces.contains(destinationCell.getText) &&
        board.getComponent(sourceIndex + 8).asInstanceOf[JLabel].getText() == "" && input.length == 4) => return true

      case ("p", x, y, w, z) if (firstPlayerTurn && secondPlayerPieces.contains(destinationCell.getText) &&
        z - y == 1 && Math.abs(x - w) == 1 && input.length == 4 && z != '8') => return true
      case ("p", x, y, w, z) if (!firstPlayerTurn && firstPlayerPieces.contains(destinationCell.getText) &&
        z - y == -1 && Math.abs(x - w) == 1 && input.length == 4 && z != '1') => return true
      case ("p", x, y, w, z) if (firstPlayerTurn && secondPlayerPieces.contains(destinationCell.getText) &&
        z - y == 1 && Math.abs(x - w) == 1 && z == '8' && input.length == 5) => return true
      case ("p", x, y, w, z) if (!firstPlayerTurn && firstPlayerPieces.contains(destinationCell.getText) &&
        z - y == -1 && Math.abs(x - w) == 1 && z == '1' && input.length == 5) => return true

      case _ => return false
    }
  }

  def rock_move (sourceIndex:Int, destinationIndex:Int, vertical:Boolean): Boolean ={
    (vertical) match {
      case true => {
        if (sourceIndex > destinationIndex) {
          for (i <- destinationIndex + 8 to sourceIndex - 8 by 8){
            if (board.getComponent(i).asInstanceOf[JLabel].getText() != ""){
              return false
            }
          }
        }
        else{
          for (i <- sourceIndex + 8 to destinationIndex - 8 by 8){
            if (board.getComponent(i).asInstanceOf[JLabel].getText() != ""){
              return false
            }
          }
        }
        true
      }
      case false => {
        if (sourceIndex > destinationIndex) {
          for (i <- destinationIndex + 1 to sourceIndex - 1 by 1){
            if (board.getComponent(i).asInstanceOf[JLabel].getText() != ""){
              return false
            }
          }
        }
        else{
          for (i <- sourceIndex + 1 to destinationIndex - 1 by 1){
            if (board.getComponent(i).asInstanceOf[JLabel].getText() != ""){
              return false
            }
          }
        }
        true
      }
    }
  }

  def bishop_move (sourceIndex:Int, destinationIndex:Int): Boolean ={

    (sourceIndex, destinationIndex) match {
      case (x,y) if  ((x > y) && ((x-y) % 9 == 0)) => {
        for (i <- y + 9 to x - 9 by 9){
          if (board.getComponent(i).asInstanceOf[JLabel].getText() != ""){
            return false
          }
        }
        true
      }
      case (x, y) if ((x > y) && ((x-y) % 7  == 0)) =>{
        for (i <- y + 7 to x - 7 by 7){
          if (board.getComponent(i).asInstanceOf[JLabel].getText() != ""){
            return false
          }
        }
        true
      }
      case (x, y) if ((x < y) && ((y-x) % 9 == 0)) =>{
        for (i <- x + 9 to y - 9 by 9){
          if (board.getComponent(i).asInstanceOf[JLabel].getText() != ""){
            return false
          }
        }
        true
      }
      case (x, y) if ((x<y) && ((y - x) % 7 == 0)) => {
        for (i <- x + 7 to y - 7 by 7){
          if (board.getComponent(i).asInstanceOf[JLabel].getText() != ""){
            return false
          }
        }
        true
      }
    }
  }

  def check (turn:Boolean, flip:Boolean): Boolean ={

    val pieces = if (!turn) firstPlayerPieces else secondPlayerPieces
    var kingPosition:Int = 0;
    var piece: String = if (turn) "K" else "k"

    for ( i <- 0 to 63){
      if (board.getComponent(i).asInstanceOf[JLabel].getText().equals(piece)) {
        kingPosition = i
      }
    }
    var firstLetter:String= ((kingPosition % 8) + 65).asInstanceOf[Char].toString()
    var secondLetter:String = ((8-(kingPosition / 8)) + 48).asInstanceOf[Char].toString()
    var i2:String = firstLetter.concat(secondLetter)
    if (flip)
      firstPlayerTurn = !firstPlayerTurn
    for (i <- 0 to 63) {
      var passIn:String = (((i % 8) + 65).asInstanceOf[Char].toString().concat(((8-(i / 8)) + 48).asInstanceOf[Char].toString())).concat(i2)
      if (pieces.contains(board.getComponent(i).asInstanceOf[JLabel].getText())) {
        if (board.getComponent(i).asInstanceOf[JLabel].getText.toLowerCase.equals("p") && (kingPosition<8 || kingPosition > 62)){
          passIn = if (turn) passIn + "Q" else passIn + "q"
        }
        if (can_move(board.getComponent(i).asInstanceOf[JLabel].getText().toLowerCase(), passIn, i, kingPosition)) {
          if (flip)
            firstPlayerTurn = !firstPlayerTurn
          return true
        }
      }
    }
    if (flip)
      firstPlayerTurn = !firstPlayerTurn
    return false
  }

  def errorMessage(message: String, frame : JFrame): Unit = {
    val panel = new JPanel
    val layout = new FlowLayout
    panel.setLayout(layout)
    JOptionPane.showMessageDialog(frame, message,
      "Error", JOptionPane.WARNING_MESSAGE)
  }

  def message (message:String, frame : JFrame): Unit ={
    val panel = new JPanel
    val layout = new FlowLayout
    panel.setLayout(layout)
    JOptionPane.showMessageDialog(frame, message,
      "Check", JOptionPane.OK_OPTION)
  }

   def controller(textField : JTextField, frame : JFrame): Unit = {
    val input = textField.getText
    if (!horizontalIndex.range.contains(input.charAt(0))
      || !verticalIndex.range.contains(input.charAt(1) - 48)
      || !horizontalIndex.range.contains(input.charAt(2))
      || !verticalIndex.range.contains(input.charAt(3) - 48)
    ) {return}

    val sourceIndex: Int = cols * ((rows - 1) - (input.charAt(1) - 49)) + (input.charAt(0) - 65)
    val destinationIndex: Int = cols * ((rows - 1) - (input.charAt(3) - 49)) + (input.charAt(2) - 65)

    val sourceCell = board.getComponent(sourceIndex).asInstanceOf[JLabel]
    val destinationCell = board.getComponent(destinationIndex).asInstanceOf[JLabel]
    val playerPieces = if (firstPlayerTurn) firstPlayerPieces else secondPlayerPieces
            
    if (!playerPieces.contains(sourceCell.getText))
      return
    if (playerPieces.contains(destinationCell.getText))
      return

    if (!can_move(sourceCell.getText.toLowerCase(), input, sourceIndex, destinationIndex)) return
    var oldPiece = destinationCell.getText
    var tempPiece:String = sourceCell.getText
    if (input.length == 5){
      if (!playerPieces.contains(input.charAt(4).toString) || input.charAt(4) == 'p' || input.charAt(4) == 'P'){
        return
      }
      destinationCell.setText(input.charAt(4).toString)
      setIcon(destinationCell)
      sourceCell.setText("")
      setIcon(sourceCell)
    }
    else{
      destinationCell.setText(sourceCell.getText)
      setIcon(destinationCell)
      sourceCell.setText("")
      setIcon(sourceCell)
    }


    if (check(firstPlayerTurn, true)){
      sourceCell.setText(tempPiece)
      setIcon(sourceCell)
      destinationCell.setText(oldPiece)
      setIcon(destinationCell)
      errorMessage("Your King would be in check", frame)
      return
    }

    if (check(!firstPlayerTurn, false)){
      message("Check", frame)
    }

    textField.setText("")
    firstPlayerTurn = !firstPlayerTurn
  }

  def setIcon(jLabel: JLabel) : Unit = {
    jLabel.getText match {
      case "p" => jLabel.setIcon(new ImageIcon(ImageIO.read(blackPawn).getScaledInstance(50, 50, 5)))
      case "P" => jLabel.setIcon(new ImageIcon(ImageIO.read(whitePawn).getScaledInstance(50, 50, 5)))
      case "r" => jLabel.setIcon(new ImageIcon(ImageIO.read(blackRook).getScaledInstance(50, 50, 5)))
      case "R" => jLabel.setIcon(new ImageIcon(ImageIO.read(whiteRook).getScaledInstance(50, 50, 5)))
      case "n" => jLabel.setIcon(new ImageIcon(ImageIO.read(blackKnight).getScaledInstance(50, 50, 5)))
      case "N" => jLabel.setIcon(new ImageIcon(ImageIO.read(whiteKnight).getScaledInstance(50, 50, 5)))
      case "b" => jLabel.setIcon(new ImageIcon(ImageIO.read(blackBishop).getScaledInstance(50, 50, 5)))
      case "B" => jLabel.setIcon(new ImageIcon(ImageIO.read(whiteBishop).getScaledInstance(50, 50, 5)))
      case "q" => jLabel.setIcon(new ImageIcon(ImageIO.read(blackQueen).getScaledInstance(50, 50, 5)))
      case "Q" => jLabel.setIcon(new ImageIcon(ImageIO.read(whiteQueen).getScaledInstance(50, 50, 5)))
      case "k" => jLabel.setIcon(new ImageIcon(ImageIO.read(blackKing).getScaledInstance(50, 50, 5)))
      case "K" => jLabel.setIcon(new ImageIcon(ImageIO.read(whiteKing).getScaledInstance(50, 50, 5)))
      case "" =>  jLabel.setIcon(null)
    }
  }

   def drawer(panel : JPanel, frame : JFrame) : Unit = {

    val defaultPiecesLocation = HashMap(1 -> "r", 2 -> "n", 3 -> "b", 4 -> "q", 5 -> "k", 6 -> "b", 7 -> "n", 8 -> "r",
      57 -> "R", 58 -> "N", 59 -> "B", 60 -> "Q", 61 -> "K", 62 -> "B", 63 -> "N", 64 -> "R")
    board = new JPanel()
    board.setLayout(new GridLayout(rows, cols))
    for (i <- 1 to rows) {
      var black:Boolean = if (i % 2 == 0) true else false
      for (j <- 1 to cols) {
        val jLabel = new JLabel("", SwingConstants.CENTER)
       // jLabel.setBorder(BorderFactory.createLineBorder(new Color(150, 60, 0), 5))
        jLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 1))
        jLabel.setForeground(new Color(100, 50, 0))
        jLabel.setText(if (defaultPiecesLocation.contains((i - 1) * cols + j)) defaultPiecesLocation((i - 1) * cols + j)
        else if ((9 to 16).contains((i - 1) * cols + j)) "p"
        else if ((49 to 56).contains((i - 1) * cols + j)) "P"
        else "")
        setIcon(jLabel)
        if (black) {
          jLabel.setBackground(new Color(118, 150, 86))
          jLabel.setOpaque(true)
        }
        else{
          jLabel.setBackground(new Color(238,238,210))
          jLabel.setOpaque(true)
        }
        black = !black
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
