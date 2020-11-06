package prof.jogos2D;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Permite manter uma indica��o de quais as teclas que est�o premidas
 * @author F. S�rgio Barbosa
 */
public class SKeyboard {
		
	private Component owner;                        // quem � o dono deste leitor de teclado
	private boolean asTeclas[] = new boolean[ 200 ]; // ser�o 200 suficientes??
	
	private MyKeyListener leitorTeclas = new MyKeyListener();
	private MyFocusListener leitorFocus  = new MyFocusListener();
	
	
	/**
	 * Cria um SKeyboard para ler o estado das teclas
	 * @param owner
	 */ 
	public SKeyboard( ) {				
		this( KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() );
		System.out.println("focus: " + KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
	}

	
	/**
	 * Cria um SKeyboard para ler o estado das teclas
	 * @param owner
	 */ 
	public SKeyboard( Component owner ) {				
		setOwner(owner);
	}
	
	/**
	 * Indica se uma dada tecla est� premida
	 * @param keyCode o c�digo da tecla que se pretende ver se est� premida
	 * @return true se a tecla est� premida, false caso contr�rio
	 */
	public boolean estaPremida( int keyCode ){
		return asTeclas[ keyCode ];
	}
	
	
	/**
	 * Como este keybord tem de estar sempre associado ao componente 
	 * com o focus se o focus mudar este tamb�m tem de mudar
	 * @param newOwner novo elemento com o focus
	 */
	public void setOwner(Component newOwner ) {
		if( owner != null ){
			owner.removeKeyListener( leitorTeclas );
			owner.removeFocusListener( leitorFocus );		
		}
		owner = newOwner;
		if( owner != null ){
			owner.addKeyListener( leitorTeclas );		
			owner.addFocusListener( leitorFocus );
		}
	}
	

	private class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent key) {
			asTeclas[ key.getKeyCode() ] = true;
		}

		public void keyReleased(KeyEvent key) {
			asTeclas[ key.getKeyCode() ] = false;
		}		
	}
	
	private class MyFocusListener extends FocusAdapter {		
		public void focusLost(FocusEvent e) {						
			setOwner( e.getOppositeComponent() );
		}
	}
}
