package Model;


public class FunktionA extends R2Funktion {

	public FunktionA(int anzahlAnZellen, double min, double max , boolean steigungZeichnen) {
		super(anzahlAnZellen, min, max, steigungZeichnen);
	}

	@Override
	public double getFunktionswert(double x, double y) {
		return Math.sin(x + y);
	}

	@Override
	public double getPartielleAbleitungX(double x, double y) {		
		return Math.cos(x+y);
	}

	@Override
	public double getPartielleAbleitungY(double x, double y) {
		return Math.cos(x+y);
	}
}
