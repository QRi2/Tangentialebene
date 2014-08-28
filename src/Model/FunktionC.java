package Model;

public class FunktionC extends R2Funktion {
	public FunktionC(int anzahlAnZellen, double min, double max, boolean zeichneSteigung) {
		super(anzahlAnZellen, min, max, zeichneSteigung);
	}

	@Override
	public double getFunktionswert(double x, double y) {
		return Math.pow(Math.E, Math.sin((2*Math.PI)/(Math.pow(x,2)+Math.pow(y,2)+1)));
	}

	@Override
	public double getPartielleAbleitungX(double x, double y) {
		return -(4*Math.PI*x*Math.pow(Math.E, Math.sin((2*Math.PI) / (Math.pow(x,2)+Math.pow(y,2) + 1))*Math.cos((2*Math.PI)/(Math.pow(x,2)+Math.pow(y,2) + 1))))/Math.pow((Math.pow(x,2)+Math.pow(y,2) + 1), 2);
	}

	@Override
	public double getPartielleAbleitungY(double x, double y) {
		return -(4*Math.PI*y*Math.pow(Math.E, Math.sin((2*Math.PI) / (Math.pow(x,2)+Math.pow(y,2) + 1))*Math.cos((2*Math.PI)/(Math.pow(x,2)+Math.pow(y,2) + 1))))/Math.pow((Math.pow(x,2)+Math.pow(y,2) + 1), 2);
	}
}

