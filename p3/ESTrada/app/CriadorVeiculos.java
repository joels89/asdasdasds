package p3.ESTrada.app;

import java.awt.Point;
import java.io.IOException;
import java.util.Random;

import p3.ESTrada.estrada.Faixa;
import p3.ESTrada.veiculo.*;
import prof.jogos2D.ComponenteAnimado;
import prof.jogos2D.ComponenteSimples;
import prof.jogos2D.ComponenteVisual;

/**
 * Classe respons�vel por criar os v�rios ve�culos do jogo
 * Essa cria��o ser� feita aleatoriamente. Os par�metros aleat�rios poder�o ser alterados 
 * @author F. Sergio e <outros autores aqui>
 */
public class CriadorVeiculos {	
	
	// os v�rios ve�culos que se apresentam no jogo
	private static final int DESPORTIVO = 0;
	private static final int MONOVOLUME = 1;
	private static final int NORMAL = 2;
	private static final int ANTIGO = 3;
	private static final int JIPE = 4;
	private static final int PICKUP = 5;
	private static final int CAMIAO = 6;
	private static final int POLICIA = 7;
	private static final int AMBULANCIA = 8;
	private static final int LIMUSINE = 9;
	
	// criador de n�s aleat�rios 
	private Random aleatorios = new Random( );  // cri
	
	// as faixas onde se podem criar carros
	private Faixa []faixas;
	
	// vari�veis de controlo de turnos e cria��o
	private int turnoActual = 0;      // n� do turno actual
	private int proximoTurno = 0;     // pr�ximo turno de cria��o
	private int []proximasVezes;      // pr�ximo turno de cria��o por faixa
	private int minTurnos = 0;        // m�nimo de turnos que ter�o de passar at� poder criar um novo ve�culo, em qualquer faixa
	private int minTurnosFaixa = 0;   // m�nimo de turnos que ter�o de passar at� poder criar um novo ve�culo numa dada faixa
 	
	// probabilidades acumuladas para cada ve�culo
	private int probabilidadesCarro[] = { 90, // desportivo    9% dos carros s�o desporticos
										 180, // monovolume    9% dos carros s�o monovolumes
										 405, // normal     22,5% dos carros s�o normais 
										 450, // antigo      4,5% dos carros s�o antigos 
										 600, // jipe         15% dos carros s�o jipes
										 700, // pickup       10% dos carros s�o pickups
										 900, // cami�o       20% dos carros s�o cami�es   
										 950, // policia       5% dos carros s�o pol�cias
										 970, // ambul�ncia    2% dos carros s�o ambul�ncias
										 1000 // limusine      3% dos carros s�o de estado
	}; 
	
	/**
	 * construtor do criador de ve�culos
	 * @param faixas faixas nas quais se podem criar ve�culos
	 * @param minTurnos minimo n�mero de turnos entre a cria��o de um ve�culo pelo criador
	 * @param minTurnosFaixa min�mo n� de turnos entre a cria��o de um ve�culo numa faixa
	 */
	public CriadorVeiculos( Faixa []faixas, int minTurnos, int minTurnosFaixa ){
		this.faixas = faixas;
		proximasVezes = new int[ faixas.length ];
		this.minTurnos = minTurnos;
		this.minTurnosFaixa = minTurnosFaixa;
	}
	
	/**
	 * vai gerar aleatoriamente uma s�rie de carros
	 * @return os carros criados neste turno
	 */
	public Carro[] criarCarros( ){
		// ver qual o turno actual e se ainda n�o tiver chegado ao pr�ximo turno de 
		// cria��o n�o cria nenhum carro
		turnoActual++;
		if( proximoTurno > turnoActual )
			return new Carro[0]; 
		
		// ver quantos ve�culos pode criar no m�ximo, para isso
		// vai ver quantas faixas j� passaram o turno de cria��o
		int maxCarros = 0;
		for( int prox : proximasVezes ){
			if( prox <= turnoActual ) maxCarros++;
		}	
		
		// este gerador cria no m�ximo 4 ve�culos em cada turno
		int nCarros = (int)Math.abs( aleatorios.nextGaussian() * 2 );
		nCarros = Math.min( nCarros, Math.min(maxCarros, 4));
		
		// se vai criar veiculos o pr�ximo turno de cria��o ser� daqui a quantos turnos?
		if( nCarros > 0 )
			proximoTurno = turnoActual + minTurnos;
		
		// criar agora os ve�culos
		Carro carros[] = new Carro[ nCarros ];
		for( int i = 0; i < nCarros; i++ ){
			// escolher aleatoriemnte a faixa
			int fidx = aleatorios.nextInt( faixas.length );
			// garantir que a faixa escolhida � eleg�vel
			while( proximasVezes[fidx] > turnoActual )
				fidx = aleatorios.nextInt( faixas.length );
			proximasVezes[ fidx ] = turnoActual + minTurnosFaixa;
			Faixa f = faixas[ fidx ];
			
			// escolher aleatoriamente o carro
			int prob = aleatorios.nextInt( 1000 );
			int tipoCarro = 0;
			for( int k = 0; k < probabilidadesCarro.length; k++ ){
				if( prob <= probabilidadesCarro[ k ] ){
					tipoCarro = k;
					break;
				}
			}
			Carro c = null;
			switch( tipoCarro ){
			case DESPORTIVO: c = criaDesportivo( f ); break; 
			case MONOVOLUME: c = criaMonovolume( f ); break;
			case NORMAL:     c = criaNormal( f );     break;
			case ANTIGO:     c = criaAntigo( f );     break;
			case JIPE:		 c = criaJipe( f );		  break;
			case PICKUP:	 c = criaPickup( f );	  break;
			case CAMIAO:     c = criaCamiao( f );     break;
			case POLICIA:    c = criaPolicia( f );    break;
			case AMBULANCIA: c = criaAmbulancia( f ); break;		
			case LIMUSINE:   c = criaLimusine( f ); break;		
			default: c = criaDesportivo( f ); break;
			}
			carros[i] = c;
			
		}
		return carros;
	}

	/**
	 * M�todos de cria��o para cada um dos ve�culos
	 * @param f faixa onde vai ficar o ve�culo
	 * @return p ve�culo criado
	 */
	
	private Carro criaDesportivo( Faixa f){
		try {
			ComponenteVisual image = new ComponenteSimples( "art/alfa.gif" );
			return new Carro( image, f, 8 , 200);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/alfa.gif");
		}			
		return null;
	}

	private Carro criaMonovolume( Faixa f){
		try {
			ComponenteVisual image = new ComponenteSimples( "art/monovolume.gif" );
			return new Carro( image, f, 5 , 200);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/monovolume.gif");
		}			
		return null;
	}	
	
	private Carro criaNormal( Faixa f){
		int n = aleatorios.nextInt( 2 );
		String file = "art/normal" + n + ".gif";
		try {
			ComponenteVisual image = new ComponenteSimples( file );
			return new Carro( image, f, 6 , 200);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro " + file);
		}			
		return null;
	}		
	
	private Carro criaAntigo( Faixa f){
		try {
			ComponenteVisual image = new ComponenteSimples( "art/antigo.gif" );
			return new Carro( image, f, 4, 150);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/antigo.gif");
		}			
		return null;
	}
	
	private Carro criaJipe( Faixa f){
		try {
			ComponenteVisual image = new ComponenteSimples( "art/jipe.gif" );
			return new TodoTerreno( image, f, 5 , 200);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/jipe.gif");
		}			
		return null;
	}
	
	private Carro criaPickup( Faixa f){
		try {
			ComponenteVisual image = new ComponenteSimples( "art/pickup.gif" );
			return new Carro( image, f, 7 , 200);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/pickup.gif");
		}			
		return null;
	}

	private Carro criaCamiao( Faixa f){
		try {
			ComponenteVisual image = new ComponenteSimples( "art/camiao.gif" );
			return new Carro( image, f, 4 , 200);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/camiao.gif");
		}			
		return null;
	}	
	
	private Carro criaPolicia( Faixa f){
		try {
			ComponenteVisual image = new ComponenteAnimado( new Point(), "art/policia.png", 2, 10 );
			return new Carro( image, f, 9 , 200);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/policia.png");
		}			
		return null;
	}
	
	private Carro criaAmbulancia( Faixa f){
		try {
			ComponenteVisual image = new ComponenteSimples( "art/ambulancia.gif" );
			return new Carro( image, f, 7 , 150);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/ambulancia.gif");
		}			
		return null;
	}	
	
	private Carro criaLimusine( Faixa f){
		try {
			ComponenteVisual image = new ComponenteSimples( "art/limusine.gif" );
			return new Carro( image, f, 8 , 200);
		} catch (IOException e) {
			System.out.println("Erro a ler o ficheiro art/limusine.gif");
		}			
		return null;
	}	
}
