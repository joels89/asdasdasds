package prof.jogos2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Representa um componente visual composto apenas por uma imagem
 * @author F. Sérgio Barbosa
 */
public class ComponenteSimples implements ComponenteVisual {

	// posição do componente visual no ecran
	private Point posicao;
	
	// imagem do componente
	private Image sprite;
		
	// rotação do componente
	private double angulo = 0;
	
	/**
	 * Cria um componente simples sem imagem e na posição (0,0) do ecran
	 */
	public ComponenteSimples( ){		
	}

	/**
	 * cria um componente simples com a imagem contida no ficheiro fichImagem
	 * @param fichImagem nome do ficheiro com a imagem que representará o componente
	 */ 
	public ComponenteSimples( String fichImagem) throws IOException {
		this( new Point(0,0), fichImagem);		
	}

	/**
	 * cria um componente simples com a imagem img
	 * @param img a imagem que representa o componente
	 */
	public ComponenteSimples( Image img ){
		this(new Point(0,0), img);
	}
	
	
	/**
	 * cria um componente simples na posição p e com a imagem contida no ficheiro fichImagem
	 * @param p posição onde irá ficar o componente
	 * @param fichImagem nome do ficheiro com a imagem que representará o componente
	 */ 
	public ComponenteSimples( Point p, String fichImagem) throws IOException {
		posicao = p;
		sprite = ImageIO.read( new File( fichImagem ) );
	}
	
	/**
	 * cria um componente simples na posição p e com a imagem img
	 * @param p posição onde irá ficar o componente
	 * @param img a imagem que representa o componente
	 */
	public ComponenteSimples( Point p, Image img){
		setPosicao( p );
		sprite = img;
	}
			
	/**
	 * desenha o componente no ambiente gráfico g
	 */
	public void desenhar(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;   
		if( sprite != null ){
			AffineTransform t = g2.getTransform();
			int meioComp = getComprimento() / 2;			
			int meiaAlt = getAltura() / 2;
			//g2.translate( meioComp, meiaAlt );
			Point meio = getPosicaoCentro();
			g2.translate( meio.x, meio.y );
			g2.rotate( angulo );
			//g2.translate( -meioComp, -meiaAlt );
			g2.translate( -meio.x, -meio.y );
			g.drawImage(sprite, getPosicao().x, getPosicao().y, null);
			//fundo.desenhar( g );
			g2.setTransform( t );
		}
	}

	public int getAltura() {
		// se tem imagem retorna a altura da imagem senão retortna 0
		return sprite == null? 0: sprite.getHeight( null );
	}

	public int getComprimento() {
		// se tem imagem retona o comprimento da imagem senão retortna 0
		return sprite == null? 0: sprite.getWidth( null );
	}

	public Point getPosicao() {
		return posicao;
	}

	public void setPosicao(Point p) {
		posicao = p;
	}
	
	public void setPosicaoCentro(Point p) {
		posicao = new Point( p.x - getComprimento()/2, p.y - getAltura()/2 );
	}	
	
	public Point getPosicaoCentro() {
		return new Point( getPosicao().x + getComprimento()/2, getPosicao().y + getAltura() / 2 );
	}
	
	public Rectangle getBounds(){
		return new Rectangle( getPosicao().x, getPosicao().y, getComprimento(), getAltura() );
	}

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	public void rodar( double angulo ){
		this.angulo += angulo;
	}
	
	public void setAngulo( double angulo ){
		this.angulo = angulo;
	}	
	
	public double getAngulo() {
		return angulo;
	}
}
