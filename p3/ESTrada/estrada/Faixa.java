package p3.ESTrada.estrada;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import p3.ESTrada.veiculo.Carro;
import p3.ESTrada.veiculo.Veiculo;

/**
 * Classe que representa uma faixa de rodagem numa estrada
 * @author F. Sergio e <outros autores>
 */
public class Faixa {

	// constantes para indicar qual o sentido da faixa
	public static final int EsqDir = 0;
	public static final int DirEsq = 1;
		
	private Estrada estrada = null; // estrada à qual pertence a faixa
	private Rectangle rect;         // área da estrada
	private int meio;               // ponto central da estrada
	private int sentido;            // sentido de rodagem da estrada
	// vector de carros que estão nesta faixa
	private ArrayList<Veiculo> carros = new ArrayList<Veiculo>(); 
	
	/** 
	 * construtor da faixa
	 * @param inicio ponto inical da faixa
	 * @param comprimento comprimento da faixa
	 * @param largura largura da faixa
	 * @param sentido sentido de rodagem na faixa
	 */
	public Faixa( Point inicio, int comprimento, int largura, int sentido ){
		rect = new Rectangle(inicio.x, inicio.y, comprimento, largura );
		meio = inicio.y + largura/2;
		this.sentido = sentido;
	}
	
	/**
	 * devolve a estrada a que pertence esta faixa
	 * @return a estrada a que pertence esta faixa
	 */
	public Estrada getEstrada() {
		return estrada;
	}

	/**
	 * Define qual a estrada a que esta faixa pertence
	 * @param estrada a estrada a que esta faixa vai pertencer
	 */
	public void setEstrada(Estrada estrada) {
		this.estrada = estrada;
	}

	/**
	 * indica se o ponto pt está dentro da faixa de rodagem
	 * @param pt ponto a testar
	 * @return true se o ponto está dentro da faixa de rodagem
	 */
	public boolean estaDentro( Point pt ){
		return rect.contains( pt );		
	}
	
	/**
	 * Devolve qual o ponto no centro da faixa mais perto do ponto de referência
	 * @param pt ponto a testar
	 * @return o ponto no centro da faixa mais perto do ponto de referência
	 */
	public Point getPontoProximo( Point pt ){		
		return new Point( pt.x, meio );
	}
	
	/**
	 * desenha a faixa
	 * @param g onde desenhar
	 */
	public void desenhar( Graphics2D g ){
		g.setColor( Color.blue );
		g.drawRect(rect.x, rect.y, rect.width, rect.height );
	}
	
	/**
	 * Adiciona um carro à faixa
	 * @param c carro a adicionar
	 */
	public void addCarro(  Veiculo c ){
		carros.add( c );
		c.setFaixa( this );
	}
	
	/**
	 * retira um carro da faixa
	 * @param c carro a retirar da faixa
	 */
	public void removeCarro( Veiculo c ){
		carros.remove( c );
	}
	
	/**
	 * devolve os carros presentes na faixa
	 * @return um vector com os carros da faixa 
	 */
	public List<Veiculo> getCarros(){
		return Collections.unmodifiableList( carros );
	}
	
	/**
	 * devolve o ponto de início da faixa (ponto superior esquerdo)
	 * @return o ponto de início da faixa
	 */
	public Point getInicioFaixa( ){
		if( sentido == EsqDir )
			return new Point( rect.x, meio );
		return new Point( rect.x + rect.width, meio );
	}
	
	/**
	 * devolve o sentido da faixa
	 * @return o sentido da faixa
	 */
	public int getSentido(){
		return sentido;
	}
	
	public void limpar() {
		carros.clear();
	}
}
