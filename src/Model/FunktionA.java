package Model;


public class FunktionA extends R2Funktion {

	public FunktionA(int anzahlAnZellen, double min, double max,
			boolean steigungZeichnen) {
		super(anzahlAnZellen, min, max, steigungZeichnen);
	}

	@Override
	public double getFunktionswert(double x, double y) {
		return Math.sin(x + y);
	}

	@Override
	public double getPartielleAbleitungX(double x, double y) {
		return Math.cos(x + y);
	}

	@Override
	public double getPartielleAbleitungY(double x, double y) {
		return Math.cos(x + y);
	}

	public double calcTangentialebene(double x0, double y0, double x, double y) {
		double f = getFunktionswert(x0, y0);
		double fx = getPartielleAbleitungX(x0, y0);
		double fy = getPartielleAbleitungY(x0, y0);
		double dx = x - x0;
		double dy = y - y0;

		return f + (fx *dx)+(fy*dy);
	}
}
