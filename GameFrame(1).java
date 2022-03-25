import javax.swing.JFrame;
import javax.swing.ImageIcon;
public class GameFrame extends JFrame {
	ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("snake.png"));
	
	GameFrame() {
		
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setIconImage(logo.getImage());

	}

}
ewewewewewewe
