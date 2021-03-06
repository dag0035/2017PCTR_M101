package p01;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Billiards extends JFrame {

	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	private Board board;

	// Update with number of group label. See practice statement.
	// Grupo 1 + 3 bolas
	private final int N_BALL = 4;
	private Ball[] balls;
	private Thread[] threads;

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Práctica programación concurrente objetos móviles independientes");
		setResizable(false);
		setVisible(true);
	}

	private void initBalls() {
		// init balls
		balls = new Ball[N_BALL];
		for ( int i = 0; i < N_BALL; i++){
			balls[i] = new Ball();
		}
		
		board.setBalls(balls);
	}

	private class ThreadBall extends Thread{
		
		private Ball ball;
		public ThreadBall(Ball ball){
			this.ball = ball;
		}
		
		@Override
		public void run() {
			try {
				for(;;) {
					ball.move();
					ball.reflect();
					repaint();
					Thread.sleep(50);
				}
			}
			catch (InterruptedException e) {return; }
		}
	}
	
	// Code is executed when start button is pushed
	private class StartListener implements ActionListener {
		@Override
		/**
		 * Usamos cerrojo para evitar que se borren los hilos
		 * mientras se estan creando en StarListener.
		 */
		public void actionPerformed(ActionEvent arg0) {
			if ( threads == null){
				threads = new ThreadBall[N_BALL];
				initBalls();board.setBalls(balls);
				for ( int i = 0; i < N_BALL; i++){
					threads[i] = new ThreadBall(balls[i]);
					threads[i].start();
				}
			}
		}
	}

	// Code is executed when stop button is pushed
	private class StopListener implements ActionListener {
		@Override
		/**
		 * Usamos cerrojo para evitar que se creen hilos 
		 * mientras estamos borrando los objetos.
		 */
		public void actionPerformed(ActionEvent arg0) {
			if (threads != null){
				for ( int i = 0; i < N_BALL; i++){
					threads[i].interrupt();
				}
				threads = null;
			}
		}
	}

	public static void main(String[] args) {
		new Billiards();
	}
}