package prof.jogos2D;

import java.awt.Point;
import java.awt.Rectangle;

public class DetectorColisoes {

	
	public static int getDivisaoAngulo( Point p1, Point p2, int nDivisoes ){	 
		double angulo = getAngulo( p1,p2 );
		return getDivisaoAngulo( angulo, nDivisoes ); 
	}
	
	
	public static int getDivisaoAngulo( double angulo, int nDivisoes ){
		// converter para graus pois d� melhores resultados
		double anguloGraus = ((int)Math.toDegrees( angulo )) % 360;
		if( anguloGraus < 0 ){			
			anguloGraus = 360 + anguloGraus;
		}
				
		double divAng = 360.0 / nDivisoes;
		
		double gama = divAng / 2;
		
		anguloGraus = anguloGraus + gama;
		
		int div = (int)(anguloGraus) / (int)divAng;
		return div;
		/*
		anguloGraus = anguloGraus - gama;
		
		// descobrir qual das divis�es � a certa
		int div = 0;
		double angRef = 0;
		while( !estaPertoAngulo( angRef, anguloGraus, gama) ){
			div++;
			angRef += divAng;
			if( angRef >= 360 )
				angRef -= 360;
		}
		if( div >= nDivisoes ) div = 0;
		
		System.out.println( "   old div = " + div );
		return div;
		*/
				
	}
	
	public static boolean estaPertoAngulo( double ref, double angulo, double erro ){
		// o zero � um caso especial
		if( ref == 0 )
			return angulo < erro || angulo > 360-erro;
		return ref - erro < angulo && ref + erro > angulo;			
	}
	
	
	/**
	 * devolve o angulo do vector formado por dois pontos. 
	 * @param p1 1� ponto
	 * @param p2 2� ponto
	 * @return �ngulo de ambate
	 */
	public static double getAngulo( Point p1, Point p2 ){
		double distancia = p1.distance( p2 );
		double dx = p1.x - p2.x;
		double angulo = Math.acos( dx / distancia );
		// se estiver no quadrante de baixo � preciso actualizar o angulo
		if( p1.y < p2.y )
			angulo = 2*Math.PI - angulo;						
		return angulo;
	}
	
	/**
	 * indica se 2 circunfer�ncias se intersectam
	 * @param p1 centro da 1� circunfer�ncia
	 * @param raio1 raio da 1� circunfer�ncia
	 * @param p2 centro da 2� circunfer�ncia
	 * @param raio2 raio da 2� circunfer�ncia
	 * @return se bateram
	 */
	public static boolean intersectam( Point p1, int raio1, Point p2, int raio2 ){
		// para terem batido a dist�ncia entre as circunfer�ncias tem de ser
		// menor que a soma dos seus raios
		int somaRaios = raio1 + raio2;
		double distancia = p1.distance( p2 );
		return distancia < somaRaios;		
	}
	
	
	/**
	 * indica se um rect�ngulo e uma circunfer�ncia se intersectam
	 * @param r
	 * @param c
	 * @param raio
	 * @return
	 */
	public static boolean intersectam( Rectangle r, Point c, int raio  ){
		// ver se o ponto esquerdo est� para l� do circulo
		int dx1 = r.x - c.x;
		if( dx1 > raio )
			return false;
		
		// ver se o ponto direito est� para c� do circulo
		int dx2 = r.x+r.width - c.x;
		if( dx2 < -raio)
			return false;
		
		// ver se o ponto inferior est� para cima do circulo		
		int dy1 = r.y - c.y;
		if( dy1 > raio )
			return false;
		
		// ver se o ponto superior est� para baixo do circulo		
		int dy2 = r.y +r.height - c.y;
		if( dy2 < -raio )
			return false;
		
		// ok, sabemos que o rectangulo r est� dentro do rectangulo que delimita o circulo
		// se ambos os pontos estiverem em quadrantes diferentes � se intersectam
		if( dx1 < 0 && dx2 > 0 )
			return true;
		if( dy1 < 0 && dy2 > 0 )
			return true;
		
		// ok, no m�ximo est� um ponto dentro do c�rculo (qual?)
		Point teste;
		// ver se � um ponto dolado esquerdo
		if( dx1 > 0 ){ 
			if( dy1 > 0 )
				teste = r.getLocation();
			else
				teste = new Point( r.x, r.y+r.height);
		}
		else {
			if( dy1 > 0 )
				teste = new Point( r.x+r.width, r.y);
			else
				teste = new Point( r.x+r.width, r.y+r.height);			
		}
		
		// est� dentro se a dist�ncia entre esse ponto e o centro for menor que o raio
		return c.distanceSq( teste ) < raio*raio;
	}
	
}
