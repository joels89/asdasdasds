package p3.ESTrada.estrada;

import java.awt.Graphics;
import java.awt.geom.Area;

import prof.jogos2D.ComponenteVisual;

/**
 * Classe que representa um bloqueio
 * @author F. Sergio e <outros autores, caso a modifiquem>
 */
public class Bloqueio {
	// elementos gr�ficos do bloqueio
	private ComponenteVisual imagem;  
	private Area area;
	
	public Bloqueio( ComponenteVisual imagem ){
		this.imagem = imagem;
		area = new Area( imagem.getBounds() );
	}
	
	/**
	 * devolve a �rea do bloqueio
	 * @return a �rea do bloqueio
	 */
	public Area getArea(){
		return area; 
	}

	/**
	 * desenha o bloqueio
	 * @param g elemento onde desenhar
	 */
	public void desenhar( Graphics g ){
		imagem.desenhar( g );
	}

}
