package p3.ESTrada.veiculo;

import p3.ESTrada.estrada.Faixa;
import prof.jogos2D.ComponenteVisual;

public class Estado extends Carro {

	public Estado(ComponenteVisual img, Faixa f, int veloc, int resistencia) {
		super(img, f, veloc, resistencia);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void checkAbrandar() {
	}

	@Override
	protected void checkBloqueios() {
	}

	@Override
	public void setParado(boolean parado) {
		if(!parado)
			super.setParado(parado);
	}

	@Override
	public boolean podeMudarFaixa(Faixa f, int x) {
		return false;
	}

}
