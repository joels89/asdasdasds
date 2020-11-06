package p3.ESTrada.estrada;

import java.awt.Graphics;
import java.awt.geom.Area;

import prof.jogos2D.ComponenteVisual;

/**
 * Classe que representa um mau piso numa estrada
 * @author F. Sergio e <outros autores se alterarem>
 *
 */
public class MauPiso {

	private ComponenteVisual imagem;  // imagem do mau piso
	private Area area;                // �rea ocupado pelo bloqueio 
	private int velMax;               // velocidade m�xima permitida pelo mau piso
	
	/**
	 * construtor do Mau piso
	 * @param imagem imagem do mau piso
	 * @param velMax velocidade m�xima permitida pelo mau piso
	 */
	public MauPiso( ComponenteVisual imagem, int velMax ){
		this.imagem = imagem;
		area = new Area( imagem.getBounds() );
		this.velMax = velMax;
	}
	
	/**
	 * devolve a �rea ocupada pelo mau piso
	 * @return a �rea ocupada pelo mau piso
	 */
	public Area getArea(){
		return area; 
	}

	/**
	 * devolve a velocidade m�xima permitida pelo mau piso
	 * @return a velocidade m�xima permitida pelo mau piso
	 */
	public int getVelMaxima() {
		return velMax;
	}

	/**
	 * Define a velocidade m�xima permitida pelo mau piso
	 * @param velMax a velocidade m�xima permitida pelo mau piso
	 */
	public void setVelMaxima(int velMax) {
		this.velMax = velMax;
	}
	
	/**
	 * desenhar o mau piso
	 * @param g onde desenhar
	 */
	public void desenhar( Graphics g ){
		imagem.desenhar( g );
	}
}
