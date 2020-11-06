package prof.jogos2D;

import java.awt.Point;
import java.awt.geom.Point2D;


public class Vector2D {

	public double x, y;  // valores de x e y
		
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D( Point2D.Double p1, Point2D.Double p2 ){
		x = p2.x - p1.x;
		y = p2.y - p1.y;
	}
	
	public Vector2D( Point p1, Point p2 ){
		x = p2.x - p1.x;
		y = p2.y - p1.y;
	}
	
	public void normalizar(){
		double comp = getComprimento();
		x /= comp;
		y /= comp;
	}
	
	public double getComprimento(){
		return Math.sqrt( getComprimentoSqr() );
	}
	
	public double getComprimentoSqr(){
		return x*x + y*y;
	}
	
	public double getAngulo(){
		return Math.atan2(y, x);
	}
	
	public double produtoInterno( Vector2D v ){
		return x*v.x + y*v.y;
	}
	
	public String toString(){
		return "("+x + ", " + y + ")";
	}
}
