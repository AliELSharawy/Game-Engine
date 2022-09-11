package scala

import java.awt.GridLayout
import javax.swing.border.EmptyBorder
import javax.swing.{JLabel, JPanel, SwingConstants}

case class VerticalIndex(rows: Int) {
  val jPanel = new JPanel()
  val range: Range = rows to 1 by -1
  jPanel.setLayout(new GridLayout(rows, 1))
  jPanel.setBorder(new EmptyBorder(10, 10, 10, 10))
  for (i <- range){
    val jLabel = new JLabel(i.toString, SwingConstants.CENTER)
    jPanel.add(jLabel)
  }
}
