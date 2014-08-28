package Model;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import View.Koordinatensystem;

public class Ebene implements IDrawElement {
	public Point3d p1;
	Point3d p2;
	Point3d p3;
	Point3d p4;
	public boolean active = false;
	public boolean isTangentialEbene = false;
	public boolean isSteigung = false;
	public boolean zeichneSteigung = false;
	public double Anstieg;
	public double steigung;

	public Ebene(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;

	}

	public Ebene(Point3d p1, Point3d p2, Point3d p3, Point3d p4,
			R2Funktion funktion) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.steigung = funktion.getPartielleAbleitungX(p1.y, p1.y);
		this.isSteigung = funktion.zeichneSteigung;
	}

	public Ebene() {
		p1 = new Point3d(0.5, 0, 0);
		p2 = new Point3d(0, 0, 0.5);
		p3 = new Point3d(-0.5, 0, 0);
		p4 = new Point3d(0, 0, -0.5); 
	}

	public Ebene EbenenScale(double scale) {
		Vector3d mittelpunkt = new Vector3d((p1.x + p2.x + p3.x + p4.x) / 4,
				(p1.y + p2.y + p3.y + p4.y) / 4,
				(p1.z + p2.z + p3.z + p4.z) / 4), v1 = new Vector3d(
				(p2.x - p1.x) / 2, (p2.y - p1.y) / 2, (p2.z - p1.z) / 2), v2 = new Vector3d(
				(p3.x - p1.x) / 2, (p3.y - p1.y) / 2, (p3.z - p1.z) / 2);
		Ebene result = new Ebene(new Point3d(mittelpunkt.x - (scale * v1.x)
				- (scale * v2.x), mittelpunkt.y - (scale * v1.y)
				- (scale * v2.y), mittelpunkt.z - (scale * v1.z)
				- (scale * v2.z)), new Point3d((mittelpunkt.x - (scale * v1.x))
				+ (scale * v2.x), (mittelpunkt.y - (scale * v1.y))
				+ (scale * v2.y), (mittelpunkt.z - (scale * v1.z))
				+ (scale * v2.z)), new Point3d(mittelpunkt.x + (scale * v1.x)
				+ (scale * v2.x), mittelpunkt.y + (scale * v1.y)
				+ (scale * v2.y), mittelpunkt.z + (scale * v1.z)
				+ (scale * v2.z)), new Point3d((mittelpunkt.x + (scale * v1.x))
				- (scale * v2.x), (mittelpunkt.y + (scale * v1.y))
				- (scale * v2.y), (mittelpunkt.z + (scale * v1.z))
				- (scale * v2.z)));
		result.isTangentialEbene = true;
		return result;
	}

	/**
	 * 
	 * @param g
	 *            Vector3d: (Scalar Ebene1, Scalar Ebene2, Scalar Gerade) zum
	 *            Schnittpunkt
	 * @return
	 */
	private Vector3d SchnittpunktMitGerade(Gerade3D g) {
		Vector3d startEbene = new Vector3d(p1);
		Vector3d richtung1Ebene = new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z
				- p1.z);
		Vector3d richtung2Ebene = new Vector3d(p3.x - p1.x, p3.y - p1.y, p3.z
				- p1.z);

		Vector3d startGerade = new Vector3d(g.Ortsvektor);
		Vector3d richtungGerade = new Vector3d(g.Richtungsvektor);

		Matrix3d A = new Matrix3d();
		A.m00 = richtung1Ebene.x;
		A.m01 = richtung2Ebene.x;
		A.m02 = -richtungGerade.x;

		A.m10 = richtung1Ebene.y;
		A.m11 = richtung2Ebene.y;
		A.m12 = -richtungGerade.y;

		A.m20 = richtung1Ebene.z;
		A.m21 = richtung2Ebene.z;
		A.m22 = -richtungGerade.z;

		Vector3d b = new Vector3d();
		b.x = startGerade.x - startEbene.x;
		b.y = startGerade.y - startEbene.y;
		b.z = startGerade.z - startEbene.z;

		A.invert();
		Vector3d x = Koordinatensystem.multiply(A, b);
		
		return x;
	}

	public double SchneidetMit(Gerade3D g) {
		Vector3d p12 = new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z), p13 = new Vector3d(
				p3.x - p1.x, p3.y - p1.y, p3.z - p1.z), n = new Vector3d();
		n.cross(p12, p13);
		
		if (g.Richtungsvektor.dot(n) != 0) {
			Vector3d x = this.SchnittpunktMitGerade(g);

			if ((x.x <= 1) && (x.x >= 0) && (x.y <= 1) && (x.y >= 0)) {
				return x.z;
			}
		}
		return -1;
	}

	@Override
	public BranchGroup draw() {
		BranchGroup ebene = new BranchGroup();
		ebene.setCapability(BranchGroup.ALLOW_DETACH);
		QuadArray planeVerteciesXZ = new QuadArray(4, QuadArray.COORDINATES);

		Point3d[] eckenXZ = new Point3d[4];

		eckenXZ[0] = p1;
		eckenXZ[1] = p2;
		eckenXZ[2] = p3;
		eckenXZ[3] = p4;

		
		planeVerteciesXZ.setCoordinates(0, eckenXZ);
		Shape3D plane = new Shape3D(planeVerteciesXZ);
		if(!active && ! isTangentialEbene && !zeichneSteigung){
			plane.setAppearance(getTransparentAppearance(false));
		}
		if (active) {
			plane.setAppearance(getAppearance(Color.RED));
		} if (isTangentialEbene) {
			plane.setAppearance(getTransparentAppearance(Color.cyan));
		} 
		if(zeichneSteigung)
		{
			plane.setAppearance(getTransparentAppearance(true));
		}

		ebene.addChild(plane);
		return ebene;
	}

	/**
	 * Methode um Aussehen(Farbe) zu bestimmen
	 * 
	 * @param Color
	 *            farbe
	 * @return Appearance
	 */
	public Appearance getTransparentAppearance(Color farbe) {
		Appearance optik = new Appearance();
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparencyMode(ta.NICEST);
		ta.setTransparency(0.75f);
		PolygonAttributes attribute = new PolygonAttributes(
				PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0f);
		optik.setPolygonAttributes(attribute);
		optik.setColoringAttributes(new ColoringAttributes(farbe.getRed(),
				farbe.getGreen(), farbe.getBlue(), 0));
		optik.setTransparencyAttributes(ta);

		return optik;
	}

	public Appearance getTransparentAppearance(boolean fuerSteigung) {
		if(fuerSteigung == true){
		Appearance optik = new Appearance();
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparencyMode(ta.NICEST);
		ta.setTransparency(0.75f);
		PolygonAttributes attribute = new PolygonAttributes(
				PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0f);
		optik.setPolygonAttributes(attribute);
	
		
			if (steigung > 0.5) {
				optik.setColoringAttributes(new ColoringAttributes((int)(Math.floor(255*(1-(steigung*-1)))),(int)Math.floor(255 * (steigung*-1)),0, 0));
			}if( steigung<=0.5){
					optik.setColoringAttributes(new ColoringAttributes((int)Math.floor(255 * steigung), (int)(Math.floor(255*(1-steigung))),0, 0));
			}
			if (steigung<-0.5){
				optik.setColoringAttributes(new ColoringAttributes((int)Math.floor(255 * (1-steigung)), (int)(Math.floor(255*steigung)),0, 0));
			}
			
		optik.setTransparencyAttributes(ta);

		return optik;
		}
		else{
			return this.getTransparentAppearance(Color.white);
		}
	}

	

	public Appearance getAppearance(Color farbe) {
		Appearance optik = new Appearance();
		optik.setColoringAttributes(new ColoringAttributes(farbe.getRed(),
				farbe.getGreen(), farbe.getBlue(), 0));
		PolygonAttributes attribute = new PolygonAttributes(
				PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0f);
		optik.setPolygonAttributes(attribute);
		return optik;
	}

}
