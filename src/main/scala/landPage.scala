package scala

import java.awt.{BorderLayout, Color, Dimension}
import javax.swing.{ImageIcon, JButton, JFrame, JPanel, WindowConstants}
import javax.imageio.ImageIO;
import java.io.File

object landPage extends App {

  val frame: JFrame = new JFrame("Board Games")
  val panel: JPanel = new JPanel()
  val chessButton: JButton = new JButton("Chess")
  val xoButton: JButton = new JButton("XO")
  val checkersButton: JButton = new JButton("Checkers")
  val connect4Button: JButton = new JButton("Connect-4")
  val DarkBlue: Color = new Color(8, 3, 23)

  frame.setMinimumSize(new Dimension(600, 600))
  frame.setLocationRelativeTo(null)
  frame.setResizable(false)
  frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
  frame.setLayout(new BorderLayout(10, 10))

  panel.setBounds(0, 50, 800, 800)
  panel.setLocation(0, 100)
  panel.setBackground(DarkBlue)

  /*val fileChess: File = new File("src\\main\\scala\\resources\\chess.png")
  val filTicTacToe: File = new File("src\\main\\scala\\resources\\xo.png")
  val fileCheckers: File = new File("src\\main\\scala\\resources\\checkers.png")
  val fileConnectFour: File = new File("src\\main\\scala\\resources\\connect-four.png")*/

  chessButton.setIcon(
    new ImageIcon(ImageIO.read(getClass().getClassLoader.getResource("scala/resources/chess.png")).getScaledInstance(100, 100, 5))
  )
  xoButton.setIcon(
    new ImageIcon(ImageIO.read(getClass().getClassLoader.getResource("scala/resources/xo.png")).getScaledInstance(100, 100, 5))
  )
  checkersButton.setIcon(
    new ImageIcon(ImageIO.read(getClass().getClassLoader.getResource("scala/resources/checkers.png")).getScaledInstance(100, 100, 5))
  )
  connect4Button.setIcon(
    new ImageIcon(ImageIO.read(getClass().getClassLoader.getResource("scala/resources/connect-four.png")).getScaledInstance(100, 100, 5))
  )

  chessButton.setBackground(Color.white)
  xoButton.setBackground(Color.white)
  checkersButton.setBackground(Color.white)
  connect4Button.setBackground(Color.white)

  chessButton.addActionListener(e => {
    val chess: ChessEngine = new ChessEngine();
    panel.setVisible(false);
    val b: BoardGameEngine =
      new BoardGameEngine(frame, chess.getController(), chess.getDrawer());
  })
  xoButton.addActionListener(e => {
    val ticTacToe: TicTacToeEngine = new TicTacToeEngine();
    panel.setVisible(false);
    val b: BoardGameEngine = new BoardGameEngine(
      frame,
      ticTacToe.getController(),
      ticTacToe.getDrawer()
    );
  })
  checkersButton.addActionListener(e => {
    val checkers: CheckersEngine = new CheckersEngine();
    panel.setVisible(false);
	val  horizontalIndex : HorizontalIndex = new HorizontalIndex(8)
	val  verticalIndex : VerticalIndex = new VerticalIndex(8)
	
    val b: BoardGameEngine = new BoardGameEngine(
      frame,
      checkers.getController(),
      checkers.getDrawer()
    );

  })
  connect4Button.addActionListener(e => {
    val connectFour: ConnectFourEngine = new ConnectFourEngine();
    panel.setVisible(false);
    val b: BoardGameEngine = new BoardGameEngine(
      frame,
      connectFour.getController(),
      connectFour.getDrawer()
    );
  })

  frame.add(panel)
  panel.add(chessButton)
  panel.add(xoButton)
  panel.add(checkersButton)
  panel.add(connect4Button)

  frame.pack()
  frame.setVisible(true)

}
