package prof.jogos2D;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Esta classe representa um componente que possui várias animações
 * e permite que se escolha qual utilizar. è útil para situações em
 * que se tem várias direcções por exemplo e se escolhe a animação
 * consoante a direcção do boneco
 * @author F. Sérgio Barbosa
 *
 */
public class ComponenteMultiAnimado extends ComponenteSimples {

	private Image frames[][];
	private int   frame = 0;
	private int   nFrames = 0;
	private int   nAnims = 0; 
	private int   actualAnim = 0;
	private int   delay = 0;
	private int   actualDelay = 0;
	
	public ComponenteMultiAnimado() {
	}

	/**
	 * cria um componente multi animado com os parametros indicados
	 * @param p posição no écran
	 * @param fichImagem ficheiro onde está a imagem do componente
	 * @param nAnims quantas animações o componente possui
	 * @param nFrames quantas frames tem cada animação
	 * @param delay factor que serve para controlar a animação (quanto maior menor a velocidade da animação)
	 * @throws IOException
	 */
	public ComponenteMultiAnimado(Point p, String fichImagem, int nAnims, int nFrames, int delay ) throws IOException {
		super( p, fichImagem );
		setPosicao( p );		
		BufferedImage img = ImageIO.read( new File( fichImagem ) );
		
		produzirFrames(nAnims, nFrames, img);
		this.delay = delay;
	}

	/**
	 * cria um componente multi animado com os parametros indicados
	 * @param p posição no écran
	 * @param img imagem da animação
	 * @param nAnims quantas animações o componente possui
	 * @param nFrames quantas frames tem cada animação
	 * @param delay factor que serve para controlar a animação (quanto maior menor a velocidade da animação)
	 */
	public ComponenteMultiAnimado(Point p, BufferedImage img, int nAnims, int nFrames, int delay ) {
		setPosicao( p );
		produzirFrames(nAnims, nFrames, img);
		this.delay = delay;
	}

	/**
	 * cria um componente multi animado com os parametros indicados
	 * @param p posição no écran
	 * @param img arrays de imagens a usar no componente pela ordem
	 * @param nAnims quantas animações o componente possui
	 * @param nFrames quantas frames tem cada animação
	 * @param delay factor que serve para controlar a animação (quanto maior menor a velocidade da animação)
	 */
	public ComponenteMultiAnimado(Point p, Image img[][], int delay ) {
		setPosicao( p );
		frames = img;
		nAnims = img.length;
		nFrames = img[0].length;
		this.delay = delay;
		setFrameNum( 0 );
	}
	
	
	// vai produzir as frames apartir da imagem total
	private void produzirFrames(int nAnims, int nFrames, BufferedImage img) {
		this.nFrames = nFrames;
		frames = new Image[nAnims][ nFrames ];
		int comp = img.getWidth( ) / nFrames;
		int alt = img.getHeight() / nAnims;
		for( int a = 0; a  < nAnims; a++ ) {
			for( int i = 0; i < nFrames; i++ ){
				frames[ a ][ i ] = img.getSubimage(i*comp, a*alt, comp, alt);
			}
		}
		super.setSprite( frames[ actualAnim ][ frame ] );				
	}

	
	/**
	 * desenha este componente no ambiente gráfico g
	 */
	public void desenhar(Graphics g) {
//		for( int k = 0; k < frames.length; k++)
//			for( int i = 0; i < frames[k].length; i++)
//				g.drawImage(frames[k][i], getPosicao().x+i*getComprimento(), getPosicao().y+k*getAltura(), null);
		
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
			super.setSprite( frames[ actualAnim ][ frame ] );
		}
	}

	/**
	 * começa a animação numa dada frame
	 * @param f a frame onde deve começar a animação
	 */
	public void setFrameNum( int f ){
		frame = f;
		super.setSprite( frames[ actualAnim ][ frame ] );
	}
	
	/**
	 * indica qual a frame que está a ser desenhada
	 * @return a frame que está a ser desenhada
	 */
	public int getFrameNum() {
		return frame;
	}
	
	/**
	 * Muda a animação para a. usar quando se pretende mudar de animação
	 * @param a a nova animação a utilizar
	 */
	public void setAnim( int a ){
		actualAnim = a;
		super.setSprite( frames[ actualAnim ][ frame ] );
	}
	
	/**
	 * indica qual a animação que está a ser reproduzida
	 * @return a animação que está a ser reproduzida
	 */
	public int getAnim() {
		return actualAnim;
	}
}
