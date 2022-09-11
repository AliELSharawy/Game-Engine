package scala

import java.awt.{Color, Font, GridLayout, BorderLayout}
import javax.swing.{BorderFactory, JFrame, JLabel, JPanel, SwingConstants}
import javax.swing.JTextField

class TicTacToeEngine() {

  var board: JPanel = null
   val rows: Int = 3
   val cols: Int = 3

 val horizontalIndex: HorizontalIndex = new HorizontalIndex(cols)
 val verticalIndex: VerticalIndex = new VerticalIndex(rows)

  var firstPlayerTurn: Boolean = true


  def drawer(panel : JPanel, frame : JFrame): Unit = {
    
    board = new JPanel()
    board.setLayout(new GridLayout(rows, cols))
    for (_ <- 1 to rows * cols) {
      val jLabel = new JLabel("", SwingConstants.CENTER)
      jLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5))
      jLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 75))
      board.add(jLabel)
    }
    firstPlayerTurn = true
    frame.add(horizontalIndex.jPanel, BorderLayout.NORTH)
    panel.add(horizontalIndex.copy().jPanel, 0)
    frame.add(verticalIndex.jPanel, BorderLayout.WEST)
    frame.add(verticalIndex.copy().jPanel, BorderLayout.EAST)
    frame.add(board, BorderLayout.CENTER)
    frame.revalidate()

  };
  def controller(textField : JTextField, frame : JFrame): Unit = {
    val input = textField.getText
    if (
      input.length() != 2
      || !horizontalIndex.range.contains(input.charAt(0))
      || !verticalIndex.range.contains(input.charAt(1) - 48)
    ) { return }

    val index: Int =
      cols * ((rows - 1) - (input.charAt(1) - 49)) + (input.charAt(0) - 65)
    if (board.getComponent(index).asInstanceOf[JLabel].getText != "")
      return

    board
      .getComponent(index)
      .asInstanceOf[JLabel]
      .setText(if (firstPlayerTurn) "X" else "O")

    textField.setText("")
    firstPlayerTurn = !firstPlayerTurn
  }

  def getController() : (JTextField, JFrame) => Unit = return this.controller;
  def getDrawer() : (JPanel, JFrame) => Unit = return this.drawer;

}
