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
	int bodyParts = 6;
	int strawBerrysEaten;
	int strawBerryX;
	int strawBerryY;
	int bananaX;
	int bananaY;
	int scissorsX;
	int scissorsY;
	char direction = 'R';
	boolean running = false;
	long lastBanana = 0;
	long lastScissors = 0;
	Timer timer;
	Random random;

	JButton t;
	JButton lvl1;
	JButton lvl2;
	JButton lvl3;
	

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
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

			//Für jedes Segment der Schlange wird nun eine Iteration durchgeführt
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.BOLD, 40));
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
	// Loeschen der Items durch Verschiebung ausserhalb des Rahmens
	public void deleteBanana() {

		bananaX = 650;
		bananaY = 650;
	}

	public void newScissors() {

		scissorsX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		scissorsY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
	}
	
	public void deleteScissors() {

		scissorsX = 650;
		scissorsY = 650;
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
			deleteBanana();
			lastBanana = System.currentTimeMillis();
		}
	}

	public void checkScissors() {
		if ((x[0] == scissorsX) && (y[0] == scissorsY)) {
		    bodyParts -= 3;
			strawBerrysEaten++;
			deleteScissors();
			lastScissors = System.currentTimeMillis();
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

		if (bodyParts<2) {
			running=false;
		}
		if (!running) {
			timer.stop();
		}

	}

	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier New", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + strawBerrysEaten,
				(SCREEN_WIDTH - metrics1.stringWidth("Score: " + strawBerrysEaten)) / 2, g.getFont().getSize());
//Game Over text
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over!", (SCREEN_WIDTH - metrics2.stringWidth("Game Over!")) / 2, SCREEN_HEIGHT / 2);
		
		if (t == null) {
			t = new JButton("Spiel beenden");
			t.setSize(150, 50);
			t.setLocation(240, 400);
			t.setBackground(new java.awt.Color(85, 85, 85));
			t.setForeground(Color.WHITE);
			t.setCursor(new Cursor(Cursor.HAND_CURSOR));
			t.setFont(new Font("Courier New", Font.BOLD, 12));
			t.addActionListener(this);
		}

		if (lvl1 == null) {
			lvl1 = new JButton("Level 1");
			lvl1.setSize(100, 50);
			lvl1.setLocation(90, 70);
			lvl1.setBackground(new java.awt.Color(85, 85, 85));
			lvl1.setForeground(Color.WHITE);
			lvl1.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lvl1.setFont(new Font("Courier New", Font.BOLD, 12));
			lvl1.addActionListener(this);
		}

		if (lvl2 == null) {
			lvl2 = new JButton("Level 2");
			lvl2.setSize(100, 50);
			lvl2.setLocation(260, 70);
			lvl2.setBackground(new java.awt.Color(85, 85, 85));
			lvl2.setForeground(Color.WHITE);
			lvl2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lvl2.setFont(new Font("Courier New", Font.BOLD, 12));
			lvl2.addActionListener(this);
		}
		
		if (lvl3 == null) {
			lvl3 = new JButton("Level 3");
			lvl3.setSize(100, 50);
			lvl3.setLocation(430, 70);
			lvl3.setBackground(new java.awt.Color(85, 85, 85));
			lvl3.setForeground(Color.WHITE);
			lvl3.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lvl3.setFont(new Font("Courier New", Font.BOLD, 12));
			lvl3.addActionListener(this);
		}
		
		
		this.add(t);
		this.add(lvl1);
		this.add(lvl2);
		this.add(lvl3);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			move();
			checkStrawBerry();
			checkBanana();
			checkScissors();
			checkCollision();
			if (System.currentTimeMillis() > lastBanana +20000 && bananaX == 650 && bananaY == 650)
			{
				newBanana();
			}
			if (System.currentTimeMillis() > lastScissors +35000 && scissorsX == 650 && scissorsY == 650)
			{
				newScissors();
			}
		}
		repaint();

		if (e.getSource() == t) {
			System.exit(0);
		}

		if (e.getSource() == lvl1) {
			
			this.delay = 100;
			startGame();
			this.remove(t);
			this.remove(lvl3);
			this.remove(lvl2);
			this.remove(lvl1);
			running = true;
			resetSnake();
			repaint();

		}

		if (e.getSource() == lvl2) {
			this.delay = 70;
			startGame();
			this.remove(t);
			this.remove(lvl3);
			this.remove(lvl2);
			this.remove(lvl1);
			running = true;
			resetSnake();
			repaint();
		}
		
		if (e.getSource() == lvl3) {
			this.delay = 50;
			startGame();
			this.remove(t);
			this.remove(lvl3);
			this.remove(lvl2);
			this.remove(lvl1);
			running = true;
			resetSnake();
			repaint();
			
		}
	}

	public void resetSnake() {
		bodyParts = 6;
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
