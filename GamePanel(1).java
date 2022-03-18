import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static int delay = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 1;
	int strawBerrysEaten;
	int strawBerryX;
	int strawBerryY;
	int bananaX;
	int bananaY;
	int scissorsX;
	int scissorsY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	JButton t;
	JButton l;
	JButton s;

	JOptionPane p = new JOptionPane();

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.gray);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();

	}

	public void startGame() {
		newStrawBerry();
		newBanana();
		newScissors();
		running = true;
		// Geschwindigkeit des Spiels
		timer = new Timer(delay, this);
		timer.start();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);

	}

	public void draw(Graphics g) {
		if (running) {

			// Erdbeere wird konstant angezeigt
			g.setColor(Color.red);
			g.fillOval(strawBerryX, strawBerryY, UNIT_SIZE, UNIT_SIZE);

			// Banane wird konstant angezeigt
			g.setColor(Color.YELLOW);
			g.fillRect(bananaX, bananaY, UNIT_SIZE, UNIT_SIZE / 2);

			// Schere wird konstant angezeigt
			g.setColor(Color.BLUE);
			g.fillRect(scissorsX, scissorsY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.blue);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + strawBerrysEaten,
					(SCREEN_WIDTH - metrics.stringWidth("Score: " + strawBerrysEaten)) / 2, g.getFont().getSize());

		} else {
			gameOver(g);
		}

	}

	public void newStrawBerry() {
		strawBerryX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		strawBerryY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void newBanana() {

		bananaX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		bananaY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void newScissors() {

		scissorsX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		scissorsY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {

		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;

		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;

		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;

		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

	}

	public void checkStrawBerry() {
		if ((x[0] == strawBerryX) && (y[0] == strawBerryY)) {
			bodyParts++;
			strawBerrysEaten++;
			newStrawBerry();
		}
	}

	public void checkBanana() {
		if ((x[0] == bananaX) && (y[0] == bananaY)) {
			bodyParts += 5;
			strawBerrysEaten += 5;
			newBanana();
		}
	}

	public void checkScissors() {
		if ((x[0] == scissorsX) && (y[0] == scissorsY)) {
			if (bodyParts > 4)
				bodyParts -= 3;
			strawBerrysEaten++;
			newScissors();
		}
	}

//Kollisionsüberprüfung
	public void checkCollision() {
//Schlangensegment
		for (int i = bodyParts; i > 0; i--) {

			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// Linker Rand
		if (x[0] < 0) {
			running = false;
		}
		// Rechter Rand
		if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
			running = false;
		}
		// Oberer Rand
		if (y[0] < 0) {
			running = false;
		}
		// Unterer Rand
		if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
			running = false;
		}

		if (!running) {
			timer.stop();
		}

	}

	public void gameOver(Graphics g) {
//Score
		g.setColor(Color.blue);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + strawBerrysEaten,
				(SCREEN_WIDTH - metrics1.stringWidth("Score: " + strawBerrysEaten)) / 2, g.getFont().getSize());
//Game Over text
		g.setColor(Color.black);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
		if (t == null) {
			t = new JButton("Erneut versuchen");
			t.setBounds(250, 320, 150, 50);
			t.setBackground(new java.awt.Color(85, 85, 85));
			t.setForeground(Color.WHITE);
			t.setCursor(new Cursor(Cursor.HAND_CURSOR));
			t.addActionListener(this);
		}

		if (s == null) {
			s = new JButton("Schneller");
			s.setBounds(350, 120, 150, 50);
			s.setBackground(new java.awt.Color(85, 85, 85));
			s.setForeground(Color.WHITE);
			s.setCursor(new Cursor(Cursor.HAND_CURSOR));
			s.addActionListener(this);
		}

		if (l == null) {
			l = new JButton("Langsamer");
			l.setBounds(150, 120, 150, 50);
			l.setBackground(new java.awt.Color(85, 85, 85));
			l.setForeground(Color.WHITE);
			l.setCursor(new Cursor(Cursor.HAND_CURSOR));
			l.addActionListener(this);

		}
		this.add(t);
		this.add(l);
		this.add(s);
		this.add(p);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			move();
			checkStrawBerry();
			checkBanana();
			checkScissors();
			checkCollision();
		}
		repaint();

		if (e.getSource() == t) {
			startGame();
			this.remove(t);
			this.remove(l);
			this.remove(s);
			running = true;
			resetSnake();
			repaint();

		}

		if (e.getSource() == s) {
			if (this.delay >= 40)
				this.delay -= 10;
			else
				JOptionPane.showMessageDialog(this, "Du hast bereits die schnellste Geschwindigkeit eingestellt!");

		}

		if (e.getSource() == l) {
			if (this.delay <= 120)
				this.delay += 10;
			else {
				JOptionPane.showMessageDialog(this, "Du hast bereits die langsamste Geschwindigkeit eingestellt!");
			}

		}
	}

	public void resetSnake() {
		bodyParts = 1;
		strawBerrysEaten = 0;
		x[0] = 250;
		y[0] = 250;
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;

			}

		}

	}

}
