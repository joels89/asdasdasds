package p3.ESTrada.veiculo;

import java.awt.Point;



import p3.ESTrada.estrada.Faixa;
import p3.ESTrada.estrada.MauPiso;
import prof.jogos2D.ComponenteVisual;

public class Emergencia extends Carro {

	public Emergencia(ComponenteVisual img, Faixa f, int veloc, int resistencia) {
		super(img, f, veloc, resistencia);
		// TODO Auto-generated constructor stub
	}
	
	public void setParado (boolean parar) {
		if(!parar)
			setParado(parar);
	}
	
	public boolean podeMudarFaixa (Faixa f, int x) {
		if(!super.podeMudarFaixa(f, x))
				return false;
		
		return super.getFaixa().getSentido() == f.getSentido();
	}

	protected void estaMauPiso(MauPiso mp) {
		super.estaMauPiso(mp);
		reduzResistencia(1);
	}

	protected void checkBloqueios() {
		
	}
	
	protected void abranda (Veiculo c) {
		super.abranda(c);
		reduzResistencia(1);
	}
	
	

}
