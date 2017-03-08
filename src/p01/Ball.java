package p01;

import java.awt.Image;
import javax.swing.ImageIcon;
// Transform the code to be used safely in a concurrent context.  
public class Ball {
       //Find an archive named Ball.png 
	private String Ball = "ball.png"; 

	private double x,y,dx,dy;
	private double v,fi;
	private final Image image;
	private final int IMG_TAM_X = 32;
	private final int IMG_TAM_Y = 32;

	public Ball() {
		ImageIcon ii = new ImageIcon(this.getClass().getResource(Ball));
		image = ii.getImage().getScaledInstance(IMG_TAM_X, IMG_TAM_Y, 0);
		x = Billiards.Width/4-16;
		y = Billiards.Height/2-16;
		v = 5;
		fi =  Math.random() * Math.PI * 2;
	}

	public synchronized void move() {
		v = v*Math.exp(-v/1000);
		dx = v*Math.cos(fi);
		dy = v*Math.sin(fi);
		if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
			dx = 0;
			dy = 0;
		}
		x += dx;   
		y += dy;
		//Check postcondition
		checkPostconditions();
	}

	public synchronized void reflect() {
		if (Math.abs(x + 32 - Board.RIGHTBOARD) <  Math.abs(dx)) {
			fi = Math.PI - fi;
		}
		if (Math.abs(y + 32 - Board.BOTTOMBOARD) <  Math.abs(dy)) {
			fi = - fi;
		}
		if (Math.abs(x - Board.LEFTBOARD) <  Math.abs(dx)) {
			fi = Math.PI - fi;
		}
		if (Math.abs(y - Board.TOPBOARD) <  Math.abs(dy)) {
			fi = - fi;
		}
		
		//Check postcondition	
		checkPostconditions();
	}

	/**
	 * Se comprueba que las nuevas coordenadas x e y se encuentran dentro del tablero
	 * del billar. Es decir en un estado consistente.
	 */
	private void checkPostconditions(){
		assert ( x  > Board.LEFTBOARD) : "Error: LEFTBOARD"+"x:"+x+" y:"+y;
		assert ( x + IMG_TAM_X < Board.RIGHTBOARD) : "Error: RIGHTBOARD"+"x:"+x+" y:"+y;
		assert ( y  > Board.TOPBOARD) : "Error: TOPBOARD"+"x:"+x+" y:"+y;
		assert ( y + IMG_TAM_Y < Board.BOTTOMBOARD) : "Error:BOTTONBOARD"+"x:"+x+" y:"+y;
	}
	
	public synchronized int getX() {
		return (int)x;
	}
	
	public synchronized int getY() {
		return (int)y;
	}
	
	public synchronized double getFi() {
		return fi;
	}

	public synchronized double getdr() {
		return Math.sqrt(dx*dx+dy*dy);
	}

	public synchronized void setX(double x) {
		this.x = x;
	}

	public synchronized void setY(double y) {
		this.y = y;
	}

	public Image getImage() {
		return image;
	}

}

