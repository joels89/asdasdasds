package prof.jogos2D;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Interface que preresenta todos os componente visuais
 * @author F. Sérgio Barbosa
 */
public interface ComponenteVisual {
	
	/**
	 * desenhar o componente visual do jogo no ambiente gráfico g
	 * @param g o ambiente gráfico onde se desenha o elemento
	 */
	public void desenhar( Graphics g );
	
	/**
	 * indica em que posição do ecran se encontra o componente
	 * @return a posição do ecran
	 */
	public Point getPosicao();

	/**
	 * indica em que posição do ecran se encontra o centro do componente
	 * @return a posição do ecran
	 */
	public Point getPosicaoCentro();
	
	/**
	 * posiciona o componente na posição p do écran
	 * @param p nova posição do componente
	 */
	public void setPosicao( Point p );
	
	/**
	 * posiciona o componente centrado na posição p do écran
	 * @param p nova posição do centro do componente
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
	 * retorna o rectângulo que engloba o componente 
	 * @return o rectângulo que engloba o componente
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
	 * @param angulo o ãngulo de rotação (em radianos) a aplicar
	 */
	public void rodar( double angulo );
	
	/**
	 * Coloca o desenho numa dada orientação 
	 * @param angulo o ãngulo da orientação (em radianos)
	 */
	public void setAngulo( double angulo );
	
	/**
	 * Devolve o ângulo de que o desenho é rodado
	 * @return o ângulo (em radianos) da imagem
	 */
	public double getAngulo();
}
