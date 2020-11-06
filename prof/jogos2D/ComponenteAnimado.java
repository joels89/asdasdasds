package prof.jogos2D;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Representa um componente visual que tem uma animação
 * @author F. Sérgio Barbosa
 */
public class ComponenteAnimado extends ComponenteSimples {

	private Image frames[];
	private int   frame = 0;
	private int   nFrames = 0;
	private int   delay = 0;
	private int   actualDelay = 0;
	
	public ComponenteAnimado() {
	}

	/**
	 * Cria o componente animado segundo os parâmetros indicados
	 * @param p posição no écran
	 * @param fichImagem ficheiro com a imagem
	 * @param nFrames número de frames na animação
	 * @param delay factor que serve para controlar a animação (quanto maios menor a velocidade da animação)
	 * @throws IOException
	 */
	public ComponenteAnimado(Point p, String fichImagem, int nFrames, int delay ) throws IOException {
		super( p, fichImagem );
		setPosicao( p );		
		BufferedImage img = ImageIO.read( new File( fichImagem ) );
		
		produzirFrames(nFrames, img);
		this.delay = delay;
	}

	/**
	 * Cria o componente animado segundo os parâmetros indicados
	 * @param p posição no écran
	 * @param img imagem do componente
	 * @param nFrames número de frames na animação
	 * @param delay factor que serve para controlar a animação (quanto maios menor a velocidade da animação)
	 */
	public ComponenteAnimado(Point p, BufferedImage img, int nFrames, int delay ) {
		setPosicao( p );
		produzirFrames(nFrames, img);
		this.delay = delay;
	}

	/**
	 * Cria o componentye animado segundo os parâmetros indicados
	 * @param p posição no écran
	 * @param img array de iman«gens com as animações
	 * @param delay factor que serve para controlar a animação (quanto maios menor a velocidade da animação)
	 */
	public ComponenteAnimado(Point p, Image img[], int delay ) {
		setPosicao( p );
		frames = img;		
		this.delay = delay;
		nFrames = img.length;
		super.setSprite( frames[ 0 ] );
	}

	
	// vai produzir as frames apartir da imagem total
	private void produzirFrames(int nFrames, BufferedImage img) {
		this.nFrames = nFrames;
		frames = new Image[ nFrames ];
		int comp = img.getWidth( ) / nFrames;
		int alt = img.getHeight();
		for( int i = 0; i < nFrames; i++ ){
			frames[ i ] = img.getSubimage(i*comp, 0, comp, alt);
		}
		super.setSprite( frames[ frame ] );				
	}

	
	/**
	 * desenha este componente no ambiente gráfico g
	 */	
	public void desenhar(Graphics g) {
		super.desenhar(g);
		proximaFrame();
	}
	

	/**
	 * passa para a próxima frame
	 */
	public void proximaFrame() {
		actualDelay++;
		if( actualDelay == delay ){
			actualDelay = 0;
			frame++;
			if( frame >= nFrames ) frame = 0;
			super.setSprite( frames[ frame ] );
		}
	}
	
	/**
	 * começa a animação numa dada frame
	 * @param f a frame onde deve começar a animação
	 */	
	public void setFrameNum( int f ){
		frame = f;
		super.setSprite( frames[ frame ] );
	}
	
	/**
	 * indica qual a frame que está a ser desenhada
	 * @return a frame que está a ser desenhada
	 */	
	public int getFrameNum(  ){
		return frame;
	}
	
}
