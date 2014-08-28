package Model;

public class FunktionB extends R2Funktion {
	public FunktionB(int anzahlAnZellen, double min, double max, boolean zeichneSteigung) {
		super(anzahlAnZellen, min, max, zeichneSteigung);
	}

	@Override
	public double getFunktionswert(double x, double y) {
		return Math.pow(Math.E, x+y);
	}

	@Override
	public double getPartielleAbleitungX(double x, double y) {
		return Math.pow(Math.E, x+y);
	}

	@Override
	public double getPartielleAbleitungY(double x, double y) {
		return Math.pow(Math.E, x+y);
	}
}