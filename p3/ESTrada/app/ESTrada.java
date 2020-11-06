package p3.ESTrada.app;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import jdk.internal.net.http.common.SequentialScheduler.RestartableTask;
import p3.ESTrada.estrada.Bloqueio;
import p3.ESTrada.estrada.Estrada;
import p3.ESTrada.estrada.Faixa;
import p3.ESTrada.estrada.MauPiso;
import p3.ESTrada.veiculo.Carro;
import p3.ESTrada.veiculo.Veiculo;
import prof.jogos2D.ComponenteAnimado;
import prof.jogos2D.ComponenteSimples;

/**
 * Esta classe representa o jogo em si. 
 */
public class ESTrada extends JFrame {

	private static final Color COLOR_PONTOS = Color.black;
	private static final Font FONT_PONTOS = new Font("Roman", Font.BOLD, 32);
	// variáveis para os vários elementos visuais do jogo
	private JPanel jContentPane = null;
	private JPanel zonaJogo = null;
	
	// imagem usada para melhorar as animações
	private Image ecran;
	
	// imagem de fundo do jogo
	private ComponenteSimples fundo;
	
	// elementos do jogo
	Estrada estrada = new Estrada();   // a estrada a usar		
	Veiculo sel = null;                  // o carro seleccionado 
	Faixa dest = null;                 // a faixa de destino do carro quando há uma mudança de faixa 
	Point origem = null, fim = null;   // o pontode partida e o ponto e chegada quando há uma mudança de faixa 

	// o criador de veículos
	private CriadorVeiculos criador;

	// estilos de linha e efeito alfa para desenhar a mudança de faixa
	Stroke estiloLinhaExterior = new BasicStroke(8.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	Stroke estiloLinhaInterior = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	Composite alphaMeio = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.5f );
	Composite alphaFull = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f );
	
	private int pontuacao = 0;
	private long tempoInicio;

	/**
	 * construtor da aplicação
	 */
	public ESTrada() {
		super();
		initialize();
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
	/**
	 *  vai inicializar a aplicação
	 */
	private void initialize() {
		// características da janela
		this.setResizable(false);		
		this.setContentPane( getJContentPane() );
		this.setTitle("ESTrada");
	    this.pack(); 	    
	    this.setLocationRelativeTo( null );
		
		// carregar o desenho do fundo da estrada
		try {
			fundo = new ComponenteSimples( "art/estrada.gif" );						
		} catch (IOException e) {
			System.out.println("Não consegui ler o ficheiro " + e.getLocalizedMessage() );
		}
		// criar a imagem para melhorar as animações e configurá-la para isso mesmo
		ecran = new BufferedImage( 800,600, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D ge = (Graphics2D )ecran.getGraphics();		
		ge.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
	    ge.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BILINEAR);	
	    
	    // criar as faixas da estrada
	    for( int i= 0; i < 4; i++)
	    	estrada.addFaixa( new Faixa( new Point(-60, 97 + i*50), 860, 30, Faixa.EsqDir ) );	    
	    for( int i= 0; i < 4; i++)
	    	estrada.addFaixa( new Faixa( new Point(-60, 310 + i*50), 860, 30, Faixa.DirEsq ) );
	    
	    // criar mau piso e bloqueio
	    try {
			MauPiso mp = new MauPiso( new ComponenteSimples(new Point(500,90), "art/maupiso.gif"), 2 );
			estrada.addMauPiso( mp );
			Bloqueio b = new Bloqueio( new ComponenteAnimado(new Point(100,302), "art/bloqueio.gif", 2, 8) );
			estrada.addBloqueio( b );			
		} catch (IOException e) {
			System.out.println("Sem ficheiros de mau piso ou bloqueio");
		}
	    
	}

	/**
	 * método auxiliar para configurar a janela
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getZonaJogo(),BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	/**
	 * método que vai ser usado para desenhar os componentes do jogo
	 * QUALQUER DESENHO DEVE SER FEITO AQUI
	 * @param g elemento onde se vai desenhar
	 */
	private void desenharJogo( Graphics2D g ){
		// passar para graphics2D pois este é mais vançado
		Graphics2D ge = (Graphics2D )ecran.getGraphics();

		// desenhar a imagem da estrada 
		fundo.desenhar( ge );
		// desenhar a mudança de faixa de houver
		if( fim != null ){
			desenharMudancaFaixa(ge);
		}			
		// desenhar a estrada (vai também desenhar os carros)
		estrada.desenhar( ge );
		
		
		ge.setFont(FONT_PONTOS);
		ge.setColor(COLOR_PONTOS);
		ge.drawString("Pontos:" + pontuacao, 30, 30);
		
		long segundos = (System.currentTimeMillis() - tempoInicio)/1000;
		ge.drawString("Tempo: " + segundos, 600, 30);
		

		// agora que está tudo desenhado na imagem auxiliar, desenhar no ecrán
		g.drawImage( ecran, 0, 0, null );		
	}

	/**
	 * método auxiliar para desenhar a mudança de faixa
	 * @param g onde desenhar
	 */
	private void desenharMudancaFaixa(Graphics2D g) {
		// como se vai mudar os estilos de linha é preciso usar um ambiente alternativo
		Graphics2D ge = (Graphics2D)g.create();
		
		// vai desenhar desde o centro do carro seleccionado até ao fim
		Point ini = sel.getPosicaoCentro();
		// com a cor amarelo se pode, ou vermelho se não pode mudar de faixa
		if( sel.podeMudarFaixa(dest, fim.x))
			ge.setPaint(Color.YELLOW);
		else
			ge.setPaint(Color.RED);
		
		// cria a linha e desenha em duas fazes, uma mais grossa mas meio transparente
		// e outra mais fina mas opaca, para dar um efeito "bonito"
		Line2D.Double line = new Line2D.Double( ini, fim );
		ge.setComposite( alphaMeio );
		ge.setStroke( estiloLinhaExterior );											
		ge.draw( line );		
		ge.setComposite( alphaFull );
		ge.setStroke( estiloLinhaInterior );										
		ge.draw( line );
		ge.dispose();
	}
	
	/**
	 * método para arrancar com o jogo
	 */
	public void start(){
		
		
		// criar o criador de veículos
	    criador = new CriadorVeiculos( estrada.getFaixas(), 30, 50);
	    pontuacao = 0;
	    tempoInicio = System.currentTimeMillis();
	    

	    
	    
		Actualizador actualiza = new Actualizador();
		actualiza.start();
		
	}

	/** 
	 * método chamado sempre que é necessário actualizar qualquer coisa na aplicação.
	 * Atenção! Este método NÃO desenha nada. Usar o método desenharJogo para isso
	 * @return -1 se é para terminar a aplicação, ou outro qualquer se for para continuar 
	 */
	private int actualizarJogo() {		
		// mexer os carros actuais, se retornar -1 quer dizer que terminou
		int res = estrada.actualizar();
		if( res == -1 ) return res;
		
		pontuacao += res;
		
		// criar os novos carros
		Veiculo []criados = criador.criarCarros( );
		for( Veiculo c : criados )
			estrada.addCarro( c );
		
		
		return res;
	}	

	/**
	 * método chamado sempre que se pressiona o rato em cima do terreno de jogo 
	 * @param pt ponto onde o rato foi premido
	 */
	private void ratoPremido( Point pt ){
		// ver qual o carro em que se clicou (se há algum)
		sel = estrada.getCarroAt( pt );
		if( sel != null ){
			// se clicou em cima o carro tem de parar
			sel.setParado( !sel.estaParado() );
			// marcar o início da viragem como sendo o
			// ponto mais próximo do centro da faixa em relação ao rato
			origem = sel.getFaixa().getPontoProximo(pt);			
		}
	}
	
	/**
	 * método chamado quando o rato é libertado em cima do terreno de jogo
	 * @param pt ponto onde o rato foi libertado
	 */
	private void ratoLibertado( Point pt ){
		// se não tiver carro seleccionado ou não se tiver afastado
		// pelo menos 4 pixeis (4*4 = 16) da origem não faz nada
		if( sel == null ) return;
		if( origem.distanceSq( pt ) < 16 ) return;
		
		// ver qual a faixa seleccionada, para isso tem o rato de estar dentro da faixa
		// ou a uma distância inferior a 30 pixeis do centro da antiga faixa seleccionada
		Faixa f = estrada.getFaixaAt( pt );
		if( f != null || dest.getPontoProximo(pt).distance( pt ) < 30 ){
			if( f != null ) dest = f;
			// ver qual o ponto do centro da faixa mais perto do rato
			fim = dest.getPontoProximo(pt);
			sel.mudarFaixa(dest, fim.x );				
		}	
		// desmarcar as selecções
		sel = null;
		fim = null;
	}

	/**
	 * método chamado quando o rato é arrastado no terreno de jogo
	 * @param pt ponto onde o rato se encontra
	 */
	private void ratoArrastado( Point pt ){
		// se não tem selecção ou o rato está a menos de 7 (7*7 = 49) pixeis da
		// origem é porque não quer mudar de faixa mas parar o carro
		if( sel == null ) return;
		if( origem.distanceSq( pt ) < 49 ) return;

		// se se afastou da origem é porque quer mudar de faixa e não parar
		// nesse caso, não pode estar parado e deve andar
		if( sel.estaParado() )
			sel.setParado( false );
		
		// ver qual a faixa seleccionada, é aquela em que o rato estiver por cima
		Faixa f = estrada.getFaixaAt( pt );
		if( f != null ){
			// ver qual o ponto do centro da faixa mais perto do rato
			fim = f.getPontoProximo(pt);
			dest = f;
		}
	}
	
	/**
	 * Este método inicializa a zonaJogo, AQUI NÂO DEVEM ALTERAR NADA 	
	 */
	private JPanel getZonaJogo() {
		if (zonaJogo == null) {
			zonaJogo = new JPanel(){
				public void paintComponent(Graphics g) {
					desenharJogo( (Graphics2D)g );
				}
			};
			Dimension d = new Dimension(800, 600);
			zonaJogo.setPreferredSize( d );
			zonaJogo.setSize( d );
			zonaJogo.setMinimumSize( d );
			zonaJogo.setBackground(Color.pink);
			zonaJogo.addMouseListener( new MouseAdapter(){
				public void mousePressed(MouseEvent e) {
					ratoPremido( e.getPoint() );
				}
				public void mouseReleased(MouseEvent e) {
					ratoLibertado( e.getPoint() );
				}				
			});
			zonaJogo.addMouseMotionListener( new MouseMotionAdapter(){
				public void mouseDragged(MouseEvent e) {
					ratoArrastado( e.getPoint() );
				}
			});						
		}
		return zonaJogo;
	}
	
	/**
	 * Classe responsável pela criação da thread que vai actualizar o mundo de x em x tempo
	 * AQUI NÃO DEVEM ALTERAR NADA 
	 * @author F. Sergio Barbosa
	 */
	class Actualizador extends Thread {
		public void run(){
			long mili = System.currentTimeMillis();
			long target = mili + 33;
			int res = 0;
			do {
				res = actualizarJogo();		
				zonaJogo.repaint();
				// esperar 33 milisegundos o que dá umas 30 frames por segundo
				while( mili < target ) mili = System.currentTimeMillis();
				target = mili + 33;
			} while( res != -1 );
			
			int opcao = JOptionPane.showConfirmDialog(ESTrada.this, "Quer Jogar Novamente?","Fim de Jogo",JOptionPane.YES_NO_OPTION);
			
			if(opcao == JOptionPane.YES_OPTION) {
				restart();			
			}
			else
			{
				System.exit(0);
			}
		}
		
	};	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ESTrada jogo = new ESTrada();
		jogo.setVisible( true );
		jogo.start();
	}
	
	public void restart() {
		estrada.limpar();
		start();
	}
	
	

}
