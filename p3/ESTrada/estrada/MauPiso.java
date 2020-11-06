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
	private Area area;                // área ocupado pelo bloqueio 
	private int velMax;               // velocidade máxima permitida pelo mau piso
	
	/**
	 * construtor do Mau piso
	 * @param imagem imagem do mau piso
	 * @param velMax velocidade máxima permitida pelo mau piso
	 */
	public MauPiso( ComponenteVisual imagem, int velMax ){
		this.imagem = imagem;
		area = new Area( imagem.getBounds() );
		this.velMax = velMax;
	}
	
	/**
	 * devolve a área ocupada pelo mau piso
	 * @return a área ocupada pelo mau piso
	 */
	public Area getArea(){
		return area; 
	}

	/**
	 * devolve a velocidade máxima permitida pelo mau piso
	 * @return a velocidade máxima permitida pelo mau piso
	 */
	public int getVelMaxima() {
		return velMax;
	}

	/**
	 * Define a velocidade máxima permitida pelo mau piso
	 * @param velMax a velocidade máxima permitida pelo mau piso
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
