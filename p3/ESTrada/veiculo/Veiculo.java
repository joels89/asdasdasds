package p3.ESTrada.veiculo;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Area;

import p3.ESTrada.estrada.Faixa;
import prof.jogos2D.ComponenteVisual;
import prof.jogos2D.Vector2D;

public interface Veiculo {

	/**
	 * devolve o componente visual associado ao carro
	 * @return o componente visual associado ao carro
	 */
	ComponenteVisual getImagem();

	/**
	 * define o componente visual do carro
	 * @param imagem o novo componente visual do carro
	 */
	void setImagem(ComponenteVisual imagem);

	/**
	 * devolve a velocidade actual do carro
	 * @return a velocidade actual do carro
	 */
	int getVelocidade();

	/**
	 * define a velocidade do carro
	 * @param veloc a velocidade a colocar no carro
	 */
	void setVelocidade(int veloc);

	/**
	 * devolve a velocidade natural deste carro
	 * @return a velocidade natural deste carro
	 */
	int getVelocidadeNatural();

	/**
	 * define a posi��o no �cran para o carro.
	 * A posi��o � o ponto superior esquerdo da imagem
	 * @param pos a posi��o no �cran para o carro
	 */
	void setPosicao(Point pos);

	/**
	 * definir qual a posi��o do centro do carro
	 * @param pos a posi��o do centro do carro
	 */
	void setPosicaoCentro(Point pos);

	/**
	 * devolve a posi��o no �cran do carro.
	 * A posi��o � o ponto superior esquerdo da imagem
	 * @return a posi��o no �cran do carro.
	 */
	Point getPosicao();

	/**
	 * devolve a posi��o no �cran do centro do carro.
	 * @return a posi��o do centro do carro.
	 */
	Point getPosicaoCentro();

	/**
	 * m�todo que vai actualizar o carro, em cada turno de movimento
	 */
	void atualizar();

	/**
	 * devolve a �rea ocupada pelo carro
	 * @return a �rea ocupada pelo carro
	 */
	Area getArea();

	/**
	 * desenhar o carro
	 * @param g elemento onde desenhar o carro 
	 */
	void desenhar(Graphics g);

	/**
	 * verifica se o ponto pt est� dentro da �rea do carro
	 * @param pt ponto a verificar
	 * @return true se pt est� dentro do carro
	 */
	boolean estaDentro(Point pt);

	/**
	 * indica se o carro est� parado
	 * @return true quando o carro est� parado
	 */
	boolean estaParado();

	/**
	 * indica se o carro est� a virar
	 * @return true se o carro est� a virar
	 */
	boolean estaVirar();

	/**
	 * define o estado de paragem do carro
	 * @param parado estado de paragem a colocar
	 */
	void setParado(boolean parado);

	/**
	 * define qual a faixa em que o carro vai ficar
	 * @param f faixa a colocar o carro
	 */
	void setFaixa(Faixa f);

	/**
	 * devolve a faixa onde o carro se desloca
	 * @return a faixa onde o carro se desloca
	 */
	Faixa getFaixa();

	/**
	 * indica se o carro est� activo
	 * @return true se o carro est� activo
	 */
	boolean isActivo();

	/**
	 * define o estado do carro
	 * @param activo o estado a ficar
	 */
	void setActivo(boolean activo);

	/**
	 * indica se pode mudar de faixa
	 * @param f faixa ara onde mudar
	 * @param x posi��o da faixa para onde vai
	 * @return true se puder mudar de faixa
	 */
	boolean podeMudarFaixa(Faixa f, int x);

	/**
	 * mudar de faixa
	 * @param f faixa para onde mudar
	 * @param x posi��o da faixa para onde deve ir
	 */
	void mudarFaixa(Faixa f, int x);

	/**
	 * devolve a direc��o de movimento do carro
	 * @return a direc��o de movimento do carro
	 */
	Vector2D getDireccao();

	/**
	 * devolve a direc��o inicial do movimento do carro
	 * @return a direc��o inicial do movimento do carro
	 */
	Vector2D getDireccaoInicial();

	void setResistencia(int resistencia);

	int getResistencia();

	void reduzResistencia(int r);

}