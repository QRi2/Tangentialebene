package Model;

public class FunktionD extends R2Funktion {
	public FunktionD(int anzahlAnZellen, double min, double max, boolean zeichneSteigung) {
		super(anzahlAnZellen, min, max, zeichneSteigung);
	}

	@Override
	public double getFunktionswert(double x, double y) {
		
		return Math.pow(x, 3);
	}

	@Override
	public double getPartielleAbleitungX(double x, double y) {
		return 3*Math.pow(x,2);
	}

	@Override
	public double getPartielleAbleitungY(double x, double y) {
		
		return 0;
	}
}

