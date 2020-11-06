package p3.ESTrada.veiculo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

import p3.ESTrada.estrada.Bloqueio;
import p3.ESTrada.estrada.Faixa;
import p3.ESTrada.estrada.MauPiso;
import prof.jogos2D.ComponenteVisual;
import prof.jogos2D.Vector2D;

/**
 * Esta classe representa um carro
 * @author F. Sergio e <outros autores>
 */
public class Carro implements Veiculo {


	private ComponenteVisual imagem;  // imagem do carro
	private Area area;                // área ocupada pelo carro, para efeitos de colisões
	private int veloc, velocInicial;  // velocidade actual e natural do carro
	private Vector2D dir, dirInicial; // direcção actual e inicial do carro
	private Faixa faixa, faixaDestino; // faixa actual e a faixa para onde o carro se dirige
	private boolean activo = true;    // se o carro está activo
	private boolean parado = false;   // se o carro está parado
	private boolean virar = false;    // se o carro está a virar
	private Point2D.Double pos;       // posição do carro (usada apenas para efeitos de aproximações)
	
	private int resistencia, resistenciaMaxima;

	/**
	 * Construtor de um carro
	 * @param img desenho do carro
	 * @param f faixa em que o carro vai ser colocado
	 * @param veloc velocidade natural do carro
	 */
	public Carro( ComponenteVisual img, Faixa f, int veloc, int resistencia ){
		this.resistencia = resistencia;
		this.resistenciaMaxima = resistencia;
		// definir a imagem e onde ela fica no ecrán
		imagem = img;
		imagem.setPosicaoCentro( f.getInicioFaixa() );
		area = new Area( imagem.getBounds() );
		
		// definir a velocidade actual e inicial
		this.veloc = velocInicial = veloc;
		
		// definir se está virado para a esquerda ou para a direita
		if( f.getSentido() == Faixa.DirEsq ){
			dirInicial = new Vector2D(-1,0);
			setAngulo( Math.PI );			
		}
		else 
			dirInicial = new Vector2D(1,0);
		
		// ver qual a posição real (e não em inteiros) da imagem
		pos = new Point2D.Double( img.getPosicao().x, img.getPosicao().y );
		
		// definir a direção e faixa de rodagem
		dir = dirInicial;
		this.faixa = f;
	}
	
	/**
	 * virar a imagem de um dado ângulo
	 * @param ang ângulo para usar na viragem
	 */
	protected void setAngulo(double ang) {
		double angArea = ang - imagem.getAngulo();
		imagem.setAngulo( ang );
		Point centro = imagem.getPosicaoCentro();
		//rodar também a área de colisão
		AffineTransform rot = AffineTransform.getRotateInstance(angArea, centro.x, centro.y);
		area.transform( rot );
	}

	/**
	 * devolve o componente visual associado ao carro
	 * @return o componente visual associado ao carro
	 */
	@Override
	public ComponenteVisual getImagem() {
		return imagem;
	}

	/**
	 * define o componente visual do carro
	 * @param imagem o novo componente visual do carro
	 */
	@Override
	public void setImagem(ComponenteVisual imagem) {
		this.imagem = imagem;
	}

	/**
	 * devolve a velocidade actual do carro
	 * @return a velocidade actual do carro
	 */
	@Override
	public int getVelocidade() {
		return veloc;
	}

	/**
	 * define a velocidade do carro
	 * @param veloc a velocidade a colocar no carro
	 */
	@Override
	public void setVelocidade(int veloc) {
		this.veloc = veloc;
	}
	
	/**
	 * devolve a velocidade natural deste carro
	 * @return a velocidade natural deste carro
	 */
	@Override
	public int getVelocidadeNatural() {
		return velocInicial;
	}	
	
	/**
	 * define a posição no écran para o carro.
	 * A posição é o ponto superior esquerdo da imagem
	 * @param pos a posição no écran para o carro
	 */
	@Override
	public void setPosicao( Point pos ){
		imagem.setPosicao( pos );		
		this.pos = new Point2D.Double( pos.x, pos.y );
		// definir a área de colisões
		area = new Area( imagem.getBounds() );
		setAngulo( imagem.getAngulo() );		
	}
	
	/**
	 * definir qual a posição do centro do carro
	 * @param pos a posição do centro do carro
	 */
	@Override
	public void setPosicaoCentro( Point pos ){		
		imagem.setPosicaoCentro( pos );		
		// definir a área de colisões
		area = new Area( imagem.getBounds() );
		setAngulo( imagem.getAngulo() );
	}

	/**
	 * devolve a posição no écran do carro.
	 * A posição é o ponto superior esquerdo da imagem
	 * @return a posição no écran do carro.
	 */
	@Override
	public Point getPosicao(){
		return imagem.getPosicao();
	}

	/**
	 * devolve a posição no écran do centro do carro.
	 * @return a posição do centro do carro.
	 */
	@Override
	public Point getPosicaoCentro(){
		return imagem.getPosicaoCentro();
	}

	/**
	 * método que vai actualizar o carro, em cada turno de movimento
	 */
	@Override
	public final void atualizar(){
		// calcular a nova posição e nova área de colisões
		mover();

		// ver se o carro está parado
		checkParado();
		
		// se está a virar tem de virar
		if( virar )	virar();
		// se não está a virar ou parado verifica se abranda
		if( !virar && !parado )
			veloc = velocInicial;
			//checkAbrandar();
		// se não está parado verifica se bateu
		if( veloc > 0 ) {
			checkBateu();
			checkMauPiso();
			checkBloqueios();
		}
		checkSaida();
		checkResistencia();
	}

	protected void mover() {
		double dx = veloc * dir.x;
		double dy = veloc * dir.y;
		pos.x += dx;
		pos.y += dy;
		imagem.setPosicao( new Point( (int)pos.x, (int)pos.y ) );
		area.transform( AffineTransform.getTranslateInstance( dx, dy) );
	}

	/**
	 * vai verificar se o carro está parado e processar caso esteja
	 */
	protected void checkParado( ){
		if( estaParado() )
			emEspera( );		
	}
	
	/**
	 * método que é chamdo sempre que o carro está em espera, isto é, aprado
	 */
	protected void emEspera() {
		reduzResistencia(1);
	}

	/**
	 * testa se o carro ficou sem resistência, se sim
	 * deve reportar um acidente
	 */
	protected void checkResistencia() {
		if (resistencia == 0)
			faixa.getEstrada().addCrash(getPosicaoCentro());				
	}

	/**
	 * vai ver se é preciso abrandar
	 */
	protected void checkAbrandar() {
		// vamos assumir que a velocidade pode ser a máxima
		// se não der para isso volta a abrandar
		
		// distância minima para abrandar
		int distMin = 2*velocInicial + imagem.getComprimento()/2;
		Point centro = getPosicaoCentro();
		
		// ter de ver em todos os carros da sua faixa se há algum à frente dele
		for( Veiculo c : faixa.getCarros() ){
			// não vamos processar: o próprio carro; os que estão em sentido contrário
			if( c == this || !mesmoSentido( c )) continue;
			// se estiver atrás não entra nas contas
			int dist = c.getPosicaoCentro().x - centro.x;
			if( dist * dir.x < 0 ) continue;
			// se for mais veloz nem vale a pena abrandar
			if( c.getVelocidade() > veloc ) continue;
			// se estiver mais longe que o minimo não se processa
			if( Math.abs( dist ) > distMin + c.getImagem().getComprimento()/2 ) continue;
			
			// se chegar aqui é porque tem de abrandar
			abranda( c );
			return;
		}		
	}

	/**
	 * abrandar o carro devido a outro carro à frente
	 * @param c carro que está à frente dele
	 */
	protected void abranda( Veiculo c ){
		setVelocidade( c.getVelocidade() );
		reduzResistencia(1);
	}

	/**
	 * verifica se o carro c está no mesmo sentido que o carro
	 * @param c carro que pode estar ou não no mesmo sentido
	 * @return true se c estiver a andar no mesmo sentido
	 */
	protected boolean mesmoSentido( Veiculo c ){
		// complicado? vê-se o produto interno das direcções
		// se for positivo é porque estão a andar para o mesmo lado		
		return dirInicial.produtoInterno( c.getDireccaoInicial() ) >= 0;
	}
	

	/**
	 * verifica se bateu em algum carro
	 */
	protected void checkBateu() {
		// para evitar choques com os carros ao lado quando se vira
		// usa-se nos testes uma área só na parte da frente
		Area frente = new Area( new Rectangle(imagem.getPosicaoCentro().x, imagem.getPosicao().y, imagem.getComprimento()/2, imagem.getAltura()) );
		frente.transform( AffineTransform.getRotateInstance( imagem.getAngulo(), imagem.getPosicaoCentro().x, imagem.getPosicaoCentro().y));		
		
		// temos de ver todos os carros da faixa 
		for( Veiculo c : faixa.getCarros() ){
			// não se processa o mesmo carro
			if( c == this || !c.isActivo() ) continue;
			Area teste = (Area)frente.clone();	
			teste.intersect( c.getArea() );
			// não se intersectam
			if( teste.isEmpty() ) continue;
			
			// se chega aqui é porque batem
			Rectangle meio = teste.getBounds();
			faixa.getEstrada().addCrash( new Point( meio.x + meio.width/2, meio.y + meio.height/2) );
			return;
		}	
	}
	
	/**
	 * vai verificar se está num mau piso
	 */
	protected void checkMauPiso() {
		// temos de ver todos os maus pisos da estrada 
		for( MauPiso mp : faixa.getEstrada().getMauPiso() ){
			Area teste = (Area)area.clone();
			teste.intersect( mp.getArea() );
			if( teste.isEmpty() ) continue;
			
			// chega aqui quando esta no mau piso
			estaMauPiso( mp );
			return;
		}
	}
	
	/**
	 * processa quando está em mau piso
	 * @param mp o mau piso em que está
	 */
	protected void estaMauPiso( MauPiso mp ){
		setVelocidade( mp.getVelMaxima() );
		reduzResistencia(1);
	}

	/**
	 * verifica se está a bater em algum bloqueio
	 */
	protected void checkBloqueios() {
		// temos de ver todos os bloqueios da estrada 
		for( Bloqueio b : faixa.getEstrada().getBloqueios() ){
			Area teste = (Area)area.clone();
			teste.intersect( b.getArea() );
			if( teste.isEmpty() ) continue;
			Rectangle meio = teste.getBounds();
			Point choque = new Point( meio.x + meio.width/2, meio.y + meio.height/2);
			bateuBloqueio( b, choque );
			return;						
		}
	}

	/**
	 * processo o bater num bloqueio
	 * @param b bloqueio onde bateu
	 * @param p ponto onde está em contacto com o bloqueio
	 */
	protected void bateuBloqueio( Bloqueio b, Point p ){
		faixa.getEstrada().addCrash( p );		
	}

	/**
	 * devolve a área ocupada pelo carro
	 * @return a área ocupada pelo carro
	 */
	@Override
	public Area getArea(){		
		return area;
	}

	/**
	 * o carro está a mudar de faixa
	 */
	private void virar() {
		// ver se está noutra faixa
		Point centroImg = imagem.getPosicaoCentro();
		Faixa f = faixa.getEstrada().getFaixaAt( centroImg );
		if( f != null && f != faixa ) setFaixa( f );
				
		// ver se já chegou á faixa de destino
		Point centroFaixa = faixaDestino.getPontoProximo( centroImg ); 	
		if( centroFaixa.distance( centroImg ) < veloc  ){
			// já chegou por isso é preciso virá-lo novamente para a frente
			imagem.setPosicaoCentro( new Point( centroImg.x, centroFaixa.y) );
			virar = false;
			dir = dirInicial;
			setAngulo( dir.getAngulo() );
			area = new Area( imagem.getBounds() );			
		}
	}

	/**
	 * verifica se já saiu da faixa
	 */
	private void checkSaida() {
		// se não está dentro da faixa de rodagem é porque já saiu
		if( !virar && !faixa.estaDentro( imagem.getPosicaoCentro() ) )
			activo = false;
	}
	
	/**
	 * desenhar o carro
	 * @param g elemento onde desenhar o carro 
	 */
	@Override
	public void desenhar( Graphics g ){
		// desenhar a linha de "vida", a vida deve ir de 0 a 10
		desenharResistencia(g, imagem.getPosicao());
		
		imagem.desenhar(g);
		
		// desenhar a área de contacto, descomentar para debug, se o desejarem
		//g.setColor( Color.blue );
		//((Graphics2D)g).draw( area );
		
		// verifica a área de teste da zona da frente, descomentar para debug, se o desejarem 
		//Area frente = new Area( new Rectangle(imagem.getPosicaoCentro().x, imagem.getPosicao().y, imagem.getComprimento()/2, imagem.getAltura()) );
		//frente.transform( AffineTransform.getRotateInstance( imagem.getAngulo(), imagem.getPosicaoCentro().x, imagem.getPosicaoCentro().y));		
		//((Graphics2D)g).draw( frente );
	}	

	/**
	 * desenha a linha de vida
	 * @param g onde desenhar
	 * @param p coordenada onde desenhar
	 */
	private void desenharResistencia( Graphics g, Point p ){
		g.setColor( Color.black );
		for( int y = -3; y <= -1; y++ ) 
			g.drawLine( p.x, p.y+y, p.x + 22, p.y+y );
		// por cor verde/amarela ou vermelha
		int nPixeis = (20*resistencia) / resistenciaMaxima;
		
		if(nPixeis > 10)			
			g.setColor( Color.green);
		else if(nPixeis > 6)
			g.setColor(Color.yellow);
		else
			g.setColor(Color.red);
		// terá um máximo de 20 pixeis
		g.drawLine( p.x + 1, p.y-2, p.x + 1 + nPixeis, p.y -2 );
	}

	/**
	 * verifica se o ponto pt está dentro da área do carro
	 * @param pt ponto a verificar
	 * @return true se pt está dentro do carro
	 */
	@Override
	public boolean estaDentro( Point pt ){
		return imagem.getBounds().contains( pt );
	}
	
	/**
	 * indica se o carro está parado
	 * @return true quando o carro está parado
	 */
	@Override
	public boolean estaParado(){
		return veloc == 0;
	}
	
	/**
	 * indica se o carro está a virar
	 * @return true se o carro está a virar
	 */
	@Override
	public boolean estaVirar( ){
		return virar;
	}
	
	/**
	 * define o estado de paragem do carro
	 * @param parado estado de paragem a colocar
	 */
	@Override
	public void setParado( boolean parado ){
		this.parado = parado;
		if( parado ) veloc = 0;
		else veloc = velocInicial;
	}

	/**
	 * define qual a faixa em que o carro vai ficar
	 * @param f faixa a colocar o carro
	 */
	@Override
	public void setFaixa( Faixa f ){
		// se vai para a mesma não faz nada
		if( faixa == f ) return;
		// mudou de faixa tem de passar para a outra
		faixa.removeCarro( this );
		faixa = f;
		f.addCarro( this );		
	}
	
	/**
	 * devolve a faixa onde o carro se desloca
	 * @return a faixa onde o carro se desloca
	 */
	@Override
	public Faixa getFaixa(){
		return faixa;
	}

	/**
	 * indica se o carro está activo
	 * @return true se o carro está activo
	 */
	@Override
	public boolean isActivo() {
		return activo;
	}

	/**
	 * define o estado do carro
	 * @param activo o estado a ficar
	 */
	@Override
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	/**
	 * indica se pode mudar de faixa
	 * @param f faixa ara onde mudar
	 * @param x posição da faixa para onde vai
	 * @return true se puder mudar de faixa
	 */
	@Override
	public boolean podeMudarFaixa( Faixa f, int x ){
		// se já está a virar ou a faixa é null não pode voltar a mudar de faixa
		if( virar || f == null) return false;
		
		// se for para andar para trás também não vira de faixa
		Point centro = getPosicaoCentro();
		if( (x - centro.x) * dir.x < 0 ) return false;
		
		return true;
	}

	/**
	 * mudar de faixa
	 * @param f faixa para onde mudar
	 * @param x posição da faixa para onde deve ir
	 */
	@Override
	public void mudarFaixa( Faixa f, int x ){
		// ver se realmente pode mudar de faixa
		if( !podeMudarFaixa( f, x ) ) return;
		
		// calcular a direção que tem de tomar para mudar de faixa
		Vector2D novaDir = new Vector2D( getPosicaoCentro(), new Point( x, f.getInicioFaixa().y ) );
		novaDir.normalizar();
		dir = novaDir;
		setAngulo( dir.getAngulo() );
		faixaDestino = f;
		virar = true;
	}	

	/**
	 * devolve a direcção de movimento do carro
	 * @return a direcção de movimento do carro
	 */
	@Override
	public Vector2D getDireccao(){
		return dir;
	}
	
	/**
	 * devolve a direcção inicial do movimento do carro
	 * @return a direcção inicial do movimento do carro
	 */
	@Override
	public Vector2D getDireccaoInicial() {
		return dirInicial;
	}
	
	@Override
	public void setResistencia(int resistencia) {
		this.resistencia = resistencia;
	}
	
	@Override
	public int getResistencia() {
		return resistencia;
	}
	
	@Override
	public void reduzResistencia(int r)
	{
		resistencia -= r;
		if (resistencia <0)
			resistencia = 0;
			
	}
	
}
