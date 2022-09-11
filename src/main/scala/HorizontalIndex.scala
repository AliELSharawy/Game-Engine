package scala

import java.awt.GridLayout
import javax.swing.border.EmptyBorder
import javax.swing.{JLabel, JPanel, SwingConstants}
import scala.collection.immutable.NumericRange

case class HorizontalIndex(cols: Int) {
  val jPanel = new JPanel()
  val range: NumericRange.Inclusive[Char] = 'A' to ('A'.toInt + cols - 1).toChar
  jPanel.setLayout(new GridLayout(1, cols))
  jPanel.setBorder(new EmptyBorder(10, 10, 10, 10))
  for (i <- range){
    val jLabel = new JLabel(i.toString, SwingConstants.CENTER)
    jPanel.add(jLabel)
  }
}
