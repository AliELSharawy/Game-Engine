package scala
import java.awt.geom.RoundRectangle2D;
import java.awt.{Color, Font, GridLayout, geom, BorderLayout}
import javax.swing.{BorderFactory, JFrame, JLabel, JPanel, SwingConstants}
import javax.swing.border.LineBorder
import java.awt.geom.Rectangle2D
import java.util.ArrayList
import javax.swing.JTextField
import java.awt.Graphics
import java.awt.Graphics2D;
import java.awt.Paint
import javax.swing.ImageIcon;
import java.awt.Image
import javax.imageio.ImageIO
class ConnectFourEngine(){

  val blueCircle : ImageIcon = new ImageIcon(ImageIO.read(getClass().getClassLoader.getResource("scala/resources/circle-blue-102.png")))
  val redCircle : ImageIcon = new ImageIcon(ImageIO.read(getClass().getClassLoader.getResource("scala/resources/circle-red-102.png")))
  val empty = Array.ofDim[Int](7)

  var board: JPanel = null
  val rows: Int = 6
  val cols: Int = 7

  val horizontalIndex: HorizontalIndex = new HorizontalIndex(cols)
  val verticalIndex: VerticalIndex = new VerticalIndex(rows)

  var firstPlayerTurn: Boolean = true

  def decidePlay(turn: Boolean, cell: JLabel) = turn match {
    case true  => { cell.setIcon(redCircle) }
    case false => { cell.setIcon(blueCircle) }
  }

  def getCell(index: Int): JLabel =
    board.getComponent(index).asInstanceOf[JLabel]

  def controller(textField: JTextField, frame : JFrame): Unit = {
    val input = textField.getText
    if (
      input.length() != 1
      || !horizontalIndex.range.contains(input.charAt(0))
    ) { return }
    val index: Int =
      cols * ((rows - 1) - (empty(input.charAt(0) - 65))) + (input.charAt(
        0
      ) - 65)
    if (empty(input.charAt(0) - 65) >= 6) return

    decidePlay(firstPlayerTurn, getCell(index))
    empty(input.charAt(0) - 65) += 1

    textField.setText("")
    firstPlayerTurn = !firstPlayerTurn
  }
  def drawer(panel: JPanel, frame: JFrame): Unit = {
    val circleWhite : ImageIcon = new ImageIcon(ImageIO.read(getClass().getClassLoader.getResource("scala/resources/circle-outline-102.png")));
    board = new JPanel()
    board.setBackground(new Color(255, 255, 204))
    board.setLayout(new GridLayout(rows, cols))
    board.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    
    
    for (_ <- 1 to rows * cols) {
      /*val jLabel = new JLabel("", SwingConstants.CENTER)
      
      jLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 100))
      jLabel.setOpaque(true)
      board.add(jLabel)*/
      val jLabel = new JLabel();
      jLabel.setAlignmentX(SwingConstants.LEFT)
      jLabel.setIcon(circleWhite);

      board.add(jLabel);
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
