package p3.ESTrada.veiculo;

import java.awt.Point;

import p3.ESTrada.estrada.Bloqueio;
import p3.ESTrada.estrada.Estrada;
import p3.ESTrada.estrada.Faixa;
import p3.ESTrada.estrada.MauPiso;
import prof.jogos2D.ComponenteVisual;

public class Camiao extends Carro {

	public Camiao(ComponenteVisual img, Faixa f, int veloc, int resistencia) {
		super(img, f, veloc, resistencia);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean podeMudarFaixa(Faixa f, int x) {
		
		if(!super.podeMudarFaixa(f, x))
			return false;
				
		Estrada e = super.getFaixa().getEstrada();	
		return e.distanciaEntreFaixas(f, getFaixa()) <=1;	
		
	}
	
	

	@Override
	protected void estaMauPiso(MauPiso mp) {
		super.estaMauPiso(mp);
		reduzResistencia(1);
	}
	
	@Override
	protected void bateuBloqueio(Bloqueio b, Point p) {
		reduzResistencia(2);
	}

}
