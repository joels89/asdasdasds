package p3.ESTrada.estrada;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import p3.ESTrada.veiculo.Carro;
import p3.ESTrada.veiculo.Veiculo;
import prof.jogos2D.ComponenteAnimado;

/**
 * Classe que representa uma estrada.
 * Uma estrada é um conjunto de faixas, carros, maus pisos e bloqueios
 * @author F. Sergio e <outros autores se modificarem>
 */
public class Estrada {
    // conjuntos das várias coisas que compõem uma estrada
	private ArrayList<Faixa> faixas = new ArrayList<Faixa>();
	private ArrayList<MauPiso> mauPiso = new ArrayList<MauPiso>();
	private ArrayList<Bloqueio> bloqueios = new ArrayList<Bloqueio>();
	
	// cada faixa tem os seus carros, mas como precisamos de rapidez para
	// os testes de colisões o melhor é duplicar a informação dos carros
	// também aqui.
	private ArrayList<Veiculo> carros = new ArrayList<Veiculo>();
	
	// animação de um acidente
	private ComponenteAnimado acidenteImg;
	
	// indicação se há acidente
	private boolean acidente = false;
	
	// indicação se o jogo deve continuar
	private boolean continua = true;
	
	/**
	 * construtor por defeito da estrada
	 */
	public Estrada( ){		
		try {
			// leitura da animação de acidente
			acidenteImg = new ComponenteAnimado( new Point(), "art/choque.gif", 17, 3);
		} catch (IOException e) {
			System.out.println("sem ficheiro de animação de explosão: choque.gif");
		}
	}

	/**
	 * adiciona uma faixa de rodagem à estrada
	 * @param f faixa a adicionar à estrada
	 */
	public void addFaixa( Faixa f ){
		f.setEstrada( this );
		faixas.add( f );
	}
	
	/**
	 * devolve um array com as faixas existentes na estrada
	 * @return um array com as faixas da estrada
	 */
	public Faixa[] getFaixas(){
		return faixas.toArray( new Faixa[ faixas.size() ]);
	}
	
	/**
	 * adiciona um carro à estrada
	 * @param c
	 */
	public void addCarro( Veiculo c ){
		carros.add( c );
		// também é preciso adicioanr o carro á faixa
		c.getFaixa().addCarro( c );
	}
	
	/**
	 * remove um carro da estrada
	 * @param c carro a remover da estrada
	 */
	public void removeCarro( Veiculo c ){
		carros.remove( c );
		// remover também da faixa
		c.getFaixa().removeCarro( c );
	}

	/**
	 * devolve o vector dos carros a andar da estrada
	 * @return um vector com os carros a andar da estrada
	 */
	public List<Veiculo> getCarros(){
		return Collections.unmodifiableList( carros );
	}
	
	/**
	 * actualizar a estrada e todos os seus componentes
	 * @return -1 quando é para terminar o jogo e outro valor para continuar
	 */
	public int actualizar(){
		// se há acidente não faz mais nada
		if( acidente ) return continua? 0: -1;

		// mover os carros todos
		for( Veiculo c : carros )
			c.atualizar();
		
		int nPontos = 0;
		
		// remover os carros desactivados
		for( int i = carros.size()-1; i >= 0; i-- ){
			Veiculo c = carros.get(i);
			if( !c.isActivo() ){
				nPontos += c.getResistencia();
				c.getFaixa().removeCarro( c );
				carros.remove(i);
			}
		}
		// retorna se já terminou
		return continua? nPontos: -1;
	}

	/** 
	 * desenhar a estrada e todos os seus componentes
	 * @param ge elemento onde desenhar
	 */
	public void desenhar(Graphics2D ge) {
		/*// desenhar as faixas, descomentar para debug se necessário
		for( Faixa f : faixas )
			f.desenhar( ge );
		*/
		
		for( MauPiso m : mauPiso )
			m.desenhar( ge );
		
		for( Bloqueio b : bloqueios )
			b.desenhar( ge );
		
		for( Veiculo c : carros )
			c.desenhar( ge );	
		
		if( acidente ){
			acidenteImg.desenhar( ge );
			if( acidenteImg.getFrameNum() == 16 ){
				continua = false;
				acidenteImg.setFrameNum( 0 );
			}
		}		
	}

	/**
	 * indica qual o carro presente na coordenada pt
	 * @param pt ponto a verificar se há carros
	 * @return o carro presente na coordenada pt, ou null se não houver nenhum
	 */
	public Veiculo getCarroAt(Point pt) {
		for( Veiculo c : carros ){
			if( c.estaDentro( pt )){
				return c;
			}
		}
		return null;
	}

	/**
	 * indica qual a faixa que contém o ponto pt 
	 * @param pt o ponto a ser testado
	 * @return a faixa que contém o ponto pt, ou null se nenhuma
	 */
	public Faixa getFaixaAt(Point pt) {
		for( Faixa f : faixas ){
			if( f.estaDentro(pt) ){
				return f;
			}
		}
		return null;
	}

	/**
	 * adiciona um acidente à estrada, num dado ponto
	 * @param point ponto onde ocorreu o acidente
	 */
	public void addCrash( Point point) {
		acidenteImg.setPosicaoCentro( point );
		acidente = true;
	}
	
	/**
	 * indica se a estrada tem algum acidente
	 * @return true se a estrada tem algum acidente
	 */
	public boolean temAcidente(){
		return acidente;
	}

	/**
	 * adiciona um bloqueio à estrda
	 * @param b bloqueio a adicionar
	 */
	public void addBloqueio( Bloqueio b ){
		bloqueios.add( b );
	}
	
	/**
	 * retorna os bloqueios presentes na estrada
	 * @return um vector com os bloqueios presentes na estrada
	 */
	public List<Bloqueio> getBloqueios() {
		return Collections.unmodifiableList( bloqueios );
	}

	/**
	 * adiciona um mau piso à estrada
	 * @param mp mau piso a adicionar
	 */
	public void addMauPiso( MauPiso mp ){
		mauPiso.add( mp );
	}
	
	/**
	 * retorna os maus pisos presentes na estrada
	 * @return um vector com os maus pisos presentes na estrada
	 */
	public List<MauPiso> getMauPiso() {
		return Collections.unmodifiableList( mauPiso );
	}

	/**
	 * Devolve a distância entre duas faixas. A distância é calculada
	 * de acordo com a ordem de entrada das faixas na estrada. 
	 * @param f1 primeira faixa
	 * @param f2 segunda faixa
	 * @return a distãncia entre as faixas. 
	 */
	public int distanciaEntreFaixas(Faixa f1, Faixa f2) {
		int f1idx = 0, f2idx = 0;
		for( int i = 0; i < faixas.size(); i++){
			if( faixas.get(i) == f1 ) f1idx = i;
			else if( faixas.get(i) == f2 ) f2idx = i;
		}
		return Math.abs( f1idx - f2idx );
	}
	
	public void limpar() {
		carros.clear();
		
		for (Faixa f : faixas)
			f.limpar();
		acidente = false;
		continua = true;
		
	}
}
