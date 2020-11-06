package prof.jogos2D;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Interface que preresenta todos os componente visuais
 * @author F. S�rgio Barbosa
 */
public interface ComponenteVisual {
	
	/**
	 * desenhar o componente visual do jogo no ambiente gr�fico g
	 * @param g o ambiente gr�fico onde se desenha o elemento
	 */
	public void desenhar( Graphics g );
	
	/**
	 * indica em que posi��o do ecran se encontra o componente
	 * @return a posi��o do ecran
	 */
	public Point getPosicao();

	/**
	 * indica em que posi��o do ecran se encontra o centro do componente
	 * @return a posi��o do ecran
	 */
	public Point getPosicaoCentro();
	
	/**
	 * posiciona o componente na posi��o p do �cran
	 * @param p nova posi��o do componente
	 */
	public void setPosicao( Point p );
	
	/**
	 * posiciona o componente centrado na posi��o p do �cran
	 * @param p nova posi��o do centro do componente
	 */
	public void setPosicaoCentro(Point p);
	
	/**
	 * retorna o comprimento, em pixeis, do componente
	 * @return o comprimento, em pixeis
	 */
	public int getComprimento();
	
	
	/**
	 * retorna a altura, em pixeis, do componente
	 * @return a altura, em pixeis
	 */
	public int getAltura();
	
	/**
	 * retorna o rect�ngulo que engloba o componente 
	 * @return o rect�ngulo que engloba o componente
	 */
	public Rectangle getBounds();
	
	/**
	 * retorna a imagem do componente
	 * @return a imagem do componente
	 */
	public Image getSprite();
	
	
	/**
	 * permite alterar a imagem do componente
	 * @param sprite
	 */
	public void setSprite(Image sprite);
	
	/**
	 * roda o desenho 
	 * @param angulo o �ngulo de rota��o (em radianos) a aplicar
	 */
	public void rodar( double angulo );
	
	/**
	 * Coloca o desenho numa dada orienta��o 
	 * @param angulo o �ngulo da orienta��o (em radianos)
	 */
	public void setAngulo( double angulo );
	
	/**
	 * Devolve o �ngulo de que o desenho � rodado
	 * @return o �ngulo (em radianos) da imagem
	 */
	public double getAngulo();
}
