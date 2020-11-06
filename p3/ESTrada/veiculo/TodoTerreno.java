package p3.ESTrada.veiculo;

import p3.ESTrada.estrada.Estrada;
import p3.ESTrada.estrada.Faixa;
import p3.ESTrada.estrada.MauPiso;
import prof.jogos2D.ComponenteVisual;

public class TodoTerreno extends Carro {

	public TodoTerreno(ComponenteVisual img, Faixa f, int veloc, int resist) {
		super(img, f, veloc, resist);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean podeMudarFaixa(Faixa f, int x) {//so pode mudar de faixa. A classe q sabe a distancia entre faixas é a estrada
		if(!super.podeMudarFaixa(f, x))// ver se cumpre as regras base de um carro 
			return false;
		//Estrada e; // assim da erro tem de ser inicializado. null ou nova estrada fica mal pois eu quero a minha estrada
		
		Estrada e = getFaixa().getEstrada();// o carro sabe a faixa e a faixa sabe a estrada
		return e.distanciaEntreFaixas(f, getFaixa()) <= 2; // se a distancia entre a q estou e para a q quero mudar é menor q 2

	}
	
	@Override
	protected void estaMauPiso(MauPiso mp) {// neste caso nao faz nadas
	}
	
	
}