
package Model;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;

/**
 * 
 * @author Christopher Glania
 * Abstrakte Klasse um Funktionen im R2 definieren
 */
public abstract class R2Funktion implements IDrawElement {

	double[][][] gatter;
	public boolean zeichneSteigung ;
	public List<Ebene> ebenenliste = new ArrayList<Ebene>();
	double steigung;
	
	public R2Funktion(int anzahlAnZellen, double min, double max, boolean zeichneSteigung) {
		if (max < min) {
			double tmp = min;
			min = max;
			max = tmp;
			
		}
		this.zeichneSteigung = zeichneSteigung;
		double distance = max - min;

		double abstand = distance / anzahlAnZellen;

		gatter = new double[anzahlAnZellen][anzahlAnZellen][3];

		for (int x = 0; x < anzahlAnZellen; ++x) {
			for (int y = 0; y < anzahlAnZellen; ++y) {
				gatter[x][y][0] = min + (abstand * x);
				gatter[x][y][1] = min + (abstand * y);
				gatter[x][y][2] = getFunktionswert(gatter[x][y][0],
						gatter[x][y][1]);
			}
		}
	}
	
	@Override
	public BranchGroup draw() {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.setCapability(BranchGroup.ALLOW_DETACH);

		if (ebenenliste.size() == 0) {
			for (int x = 0; x < (gatter.length - 1); ++x) {
				for (int y = 0; y < (gatter[x].length - 1); ++y) {
					Point3d p1 = new Point3d(gatter[x + 1][y][0],
							gatter[x + 1][y][2], gatter[x + 1][y][1]);
					Point3d p2 = new Point3d(gatter[x][y][0], gatter[x][y][2],
							gatter[x][y][1]);
					Point3d p3 = new Point3d(gatter[x][y + 1][0],
							gatter[x][y + 1][2], gatter[x][y + 1][1]);
					Point3d p4 = new Point3d(gatter[x + 1][y + 1][0],
							gatter[x + 1][y + 1][2], gatter[x + 1][y + 1][1]);

					
					Ebene e = new Ebene(p1, p2, p3, p4, this);
					e.zeichneSteigung = zeichneSteigung;
					ebenenliste.add(e);
				}
			}

		}

		for (Ebene e : ebenenliste) {
			bg.addChild(e.draw());
		}
		return bg;
	}
	
	public double[][][] getgatter() {
		return gatter;
	}
	
/**
 * Abstrakte Methode um Funktionswert zu erhalten 
 * @param double x
 * @param double y
 * @return double Funktionswert
 */
	public abstract double getFunktionswert(double x, double y);
	
	//TODO: Kommentieren
	public abstract double getPartielleAbleitungX(double x, double y);

	//TODO: Kommentieren
	public abstract double getPartielleAbleitungY(double x, double y);
	
	public Vector2d getGradient(double x, double y)
	{
		double px = this.getPartielleAbleitungX(x, y),
				py = this.getPartielleAbleitungY(x, y);
		return new Vector2d(px, py);
	}
	
}
