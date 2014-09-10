package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import Model.Ebene;
import Model.FunktionA;
import Model.FunktionB;
import Model.FunktionC;
import Model.FunktionD;
import Model.Gerade3D;
import Model.R2Funktion;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Koordinatensystem extends JFrame {
	private static final double universScale = 0.2;
	private final int preferredSize = 600;
	// Wurzelknoten
	private BranchGroup _bgRoot;
	private BranchGroup bgtan;
	
	public boolean steigungErsichtlich = false;

	// Transformknoten	
	private TransformGroup _tgMouseBehaviour;

	// BranchGroup der aktuellen Ebene
	private BranchGroup bgFunktion;
	private BranchGroup bgSchnittpunkt;
	private Ebene schnittMitMaus;
	boolean tangentialEbeneSteht = false;
	Transform3D camTransform;
	private SimpleUniverse universe;

	// private LineArray linie;

	private boolean funktionSteht1 = false;
	private boolean funktionSteht2 = false;
	private boolean funktionSteht3 = false;
	private boolean funktionSteht4 = false;
	private boolean funktionSteht1mitAnstieg = false;
	private boolean funktionSteht2mitAnstieg = false;
	private boolean funktionSteht3mitAnstieg = false;
	private boolean funktionSteht4mitAnstieg = false;
	private boolean funktionSteht = false;

	private double[][][] gatter;
	
	private final boolean[] funktionsteht = new boolean[]{false,false,false,false,false};
	private final boolean[] funktionMitAnstiegSteht = new boolean[]{false,false,false,false,false};

	Canvas3D canvas;
	
	R2Funktion f;

	public Koordinatensystem() {

		super("Tangentialebene - Christopher Glania - MTS_21");
		erstellen();
	}

	private void erstellen() {
		this.setSize(preferredSize + 200, preferredSize);
		this.setLocation(50, 50);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		this.add(canvas);

		canvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
			
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (!tangentialEbeneSteht & funktionSteht) {
					gatter = f.getgatter();

					Transform3D t3 = new Transform3D();

					Vector3d t = new Vector3d();
					Matrix3d R = new Matrix3d();

					_tgMouseBehaviour.getTransform(t3);

					t3.get(R, t);

					// äquivalente Umformung Rx+t = y <=> R^T y - R^T t = x

					R.transpose();
					t = multiply(R, t);
					t.scale(-1.0 / universScale);

					

					TransformGroup tgView = universe.getViewingPlatform()
							.getViewPlatformTransform();
					Transform3D t3dView = new Transform3D();
					Vector3d vektorView = new Vector3d();

					tgView.getTransform(t3dView);
					t3dView.get(vektorView);

					Vector3d cameraPosition = new Vector3d(vektorView);
					cameraPosition.scale(1.0 / universScale);

					Vector3d cameraDirection = new Vector3d(0, 0, -1);

					Vector3d richtung = multiply(R, cameraDirection);


					Vector3d startwert = multiply(R, cameraPosition);
					// Um Startwert translation mitzugeben
					startwert.add(t);
					
					Gerade3D mausGerade = new Gerade3D(startwert, richtung);

					double abstand = Double.MAX_VALUE;

					for (Ebene e1 : f.ebenenliste) {
						double a = e1.SchneidetMit(mausGerade);
						if ((a < abstand) && (a > 0)) {

							if (schnittMitMaus != null) {
								schnittMitMaus.active = false;
							}
							schnittMitMaus = e1;
							schnittMitMaus.active = true;
							abstand = a;
						}
					}

					if (bgSchnittpunkt != null) {
						_tgMouseBehaviour.removeChild(bgSchnittpunkt);
					}
					if (schnittMitMaus != null) {
						
						bgSchnittpunkt = schnittMitMaus.draw();
						draw(bgSchnittpunkt);
					}
				}

			}
		});

		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {

				if ((e.getKeyCode() == KeyEvent.VK_1) && !funktionSteht1) {
					if (bgFunktion != null) {
						_tgMouseBehaviour.removeChild(bgFunktion);
					}
					if (bgSchnittpunkt != null) {
						_tgMouseBehaviour.removeChild(bgSchnittpunkt);
						f.ebenenliste.clear();
						
					}
					if(tangentialEbeneSteht){
						_tgMouseBehaviour.removeChild(bgtan);
					}

					f = new FunktionA(150, -2, 2, false);					
					funktionSteht1 = true;
					funktionSteht2 = false;
					funktionSteht3 = false;
					funktionSteht4 = false;
					funktionSteht = true;
					tangentialEbeneSteht = false;
					gatter = f.getgatter();
					steigungErsichtlich = false;
					draw((bgFunktion =f.draw()));
					funktionSteht1mitAnstieg = false;
					funktionSteht2mitAnstieg = false;
					funktionSteht3mitAnstieg = false;
					funktionSteht4mitAnstieg = false;
				}

				if ((e.getKeyCode() == KeyEvent.VK_2) && !funktionSteht2) {
					if (bgFunktion != null) {
						_tgMouseBehaviour.removeChild(bgFunktion);

					}
					if (bgSchnittpunkt != null) {
						_tgMouseBehaviour.removeChild(bgSchnittpunkt);
						f.ebenenliste.clear();
					}
					if(tangentialEbeneSteht){
						_tgMouseBehaviour.removeChild(bgtan);
					}
					f = new FunktionB(150, -2, 2, false);
					draw((bgFunktion = f.draw()));
					funktionSteht1 = false;
					funktionSteht2 = true;
					funktionSteht3 = false;
					funktionSteht4 = false;
					funktionSteht = true;
					tangentialEbeneSteht = false;
					steigungErsichtlich = false;
					gatter = f.getgatter();
					funktionSteht1mitAnstieg = false;
					funktionSteht2mitAnstieg = false;
					funktionSteht3mitAnstieg = false;
					funktionSteht4mitAnstieg = false;
				}

				if ((e.getKeyCode() == KeyEvent.VK_3) && !funktionSteht3) {
					if (bgFunktion != null) {
						_tgMouseBehaviour.removeChild(bgFunktion);
					}
					if (bgSchnittpunkt != null) {
						_tgMouseBehaviour.removeChild(bgSchnittpunkt);
						f.ebenenliste.clear();
					}
					if(tangentialEbeneSteht){
						_tgMouseBehaviour.removeChild(bgtan);
					}
					
					f = new FunktionC(150, -2, 2, false);
					draw((bgFunktion = f.draw()));
					funktionSteht1 = false;
					funktionSteht2 = false;
					funktionSteht3 = true;
					funktionSteht4 = false;
					funktionSteht = true;
					tangentialEbeneSteht = false;
					gatter = f.getgatter();
					steigungErsichtlich = false;

					funktionSteht1mitAnstieg = false;
					funktionSteht2mitAnstieg = false;
					funktionSteht3mitAnstieg = false;
					funktionSteht4mitAnstieg = false;

				}
				if((e.getKeyCode() == KeyEvent.VK_4) && !funktionSteht4) {
					if (bgFunktion != null) {
						_tgMouseBehaviour.removeChild(bgFunktion);
					}
					if (bgSchnittpunkt != null) {
						_tgMouseBehaviour.removeChild(bgSchnittpunkt);
						f.ebenenliste.clear();
					}
					if(tangentialEbeneSteht){
						_tgMouseBehaviour.removeChild(bgtan);
					}
					
					f = new FunktionD(150, -2, 2, false);
					draw((bgFunktion = f.draw()));
					funktionSteht1 = false;
					funktionSteht2 = false;
					funktionSteht3 = false;
					funktionSteht4 = true;
					funktionSteht = true;
					tangentialEbeneSteht = false;
					gatter = f.getgatter();
					steigungErsichtlich = false;

					funktionSteht1mitAnstieg = false;
					funktionSteht2mitAnstieg = false;
					funktionSteht3mitAnstieg = false;
					funktionSteht4mitAnstieg = false;
				}
				if ((e.getKeyCode() == KeyEvent.VK_SPACE) && funktionSteht
						&& (schnittMitMaus != null)) {
					
					Ebene tan = schnittMitMaus.EbenenScale(100);
					bgtan = tan.draw();
					draw(bgtan);
					tangentialEbeneSteht = true;
					
					System.out.println(f.getPartielleAbleitungX(tan.p1.x, tan.p1.y) + ";"+f.getPartielleAbleitungY(tan.p1.x, tan.p1.y) );
				}
				if((e.getKeyCode() == KeyEvent.VK_A) && !steigungErsichtlich && funktionSteht){
					steigungErsichtlich = true;
					f.zeichneSteigung = true;
					
					
					if (bgFunktion != null) {
						_tgMouseBehaviour.removeChild(bgFunktion);
					}
					if (bgSchnittpunkt != null) {
						_tgMouseBehaviour.removeChild(bgSchnittpunkt);
						f.ebenenliste.clear();
						//schnittMitMaus.active = false;
					}
					if(tangentialEbeneSteht){
						_tgMouseBehaviour.removeChild(bgtan);
					}
					
					if (funktionSteht1 && !funktionSteht1mitAnstieg)
					{
						f = new FunktionA(150, -2, 2, true);
						funktionSteht1 = false;
						funktionSteht1mitAnstieg = true;
					}
					if (funktionSteht2 && !funktionSteht2mitAnstieg){
						f= new FunktionB(150, -2,2,true);
						funktionSteht2 = false;
						funktionSteht2mitAnstieg = true;
					}
					if(funktionSteht3 && !funktionSteht3mitAnstieg){
						f= new FunktionC(150, -2, 2, true);
						funktionSteht3 = false;
						funktionSteht3mitAnstieg = true;
					}
					if(funktionSteht4 && !funktionSteht4mitAnstieg){
						f= new FunktionD(150, -2, 2, true);
						funktionSteht4 = false;
						funktionSteht4mitAnstieg = true;
					}
					draw((bgFunktion = f.draw()));

					tangentialEbeneSteht = false;
					gatter = f.getgatter();
				}

				// gatter = f.getgatter();
				//
				// /*********************************************/
				// Transform3D t3 = new Transform3D();
				//
				// Vector3d t = new Vector3d();
				// Matrix3d R = new Matrix3d();
				//
				// _tgMouseBehaviour.getTransform(t3);
				//
				// t3.get(R, t);
				//
				// // äquivalente Umformung Rx+t = y <=> R^T y - R^T t = x
				//
				// R.transpose();
				// t = multiply(R, t);
				// t.scale(-1.0 / universScale);
				//
				// System.out.println("Scale: " + universScale);
				//
				// TransformGroup tgView = universe.getViewingPlatform()
				// .getViewPlatformTransform();
				// Transform3D t3dView = new Transform3D();
				// Vector3d vektorView = new Vector3d();
				//
				// tgView.getTransform(t3dView);
				// t3dView.get(vektorView);
				//
				// Vector3d cameraPosition = new Vector3d(vektorView);
				// cameraPosition.scale(1.0 / universScale);
				//
				// Vector3d cameraDirection = new Vector3d(0, 0, -1);
				//
				// Vector3d richtung = multiply(R, cameraDirection);
				//
				// System.out.println("richtung: " + richtung);
				//
				// Vector3d startwert = multiply(R, cameraPosition);
				// //Um Startwert translation mitzugeben
				// startwert.add(t);
				//
				// /*****************************************/
				//
				// System.out.println("Startvektor der cam: " + vektorView);
				//
				// /*****************************************/
				//
				// // NewtonFunktionA na = new NewtonFunktionA(richtung,
				// // startwert);
				// // NewtonVerfahren nv = new NewtonVerfahren(na, 1.0);
				// // double scale = nv.iteriere(50);
				// //
				// // double testX = (scale * richtung.x) + startwert.x;
				// // double testY = (scale * richtung.y) + startwert.y;
				// // double testZ = (scale * richtung.z) + startwert.z;
				// //
				// // double fZ = FunktionA.getFunktionswert(testX, testY);
				// //
				// // System.out
				// // .println("Abstand z nach Newton: " + (testZ - fZ));
				// Point3d[] array = calcStartEndPoint(richtung, startwert,
				// 50);
				//
				// Gerade3D mausGerade = new Gerade3D(startwert, richtung);
				//
				// double abstand = Double.MAX_VALUE;
				//
				// schnittEbenenTest = new ArrayList<Ebene>();
				// for (Ebene e1 : f.ebenenliste) {
				// double a = e1.SchneidetMit(mausGerade);
				// if ((a < abstand) && (a > 0)) {
				//
				// if (schnittMitMaus != null) {
				// schnittMitMaus.active = false;
				// }
				// schnittMitMaus = e1;
				// schnittMitMaus.active = true;
				// abstand = a;
				// schnittEbenenTest.add(e1);
				// }
				// }
				//
				// if(bgSchnittpunkt != null) {
				// _tgMouseBehaviour.removeChild(bgSchnittpunkt);
				// }
				// if(schnittMitMaus != null) {
				// bgSchnittpunkt = schnittMitMaus.draw();
				// draw(bgSchnittpunkt);
				// }

				// } else {
				// linie = new LineArray(2, LineArray.COORDINATES);
				// linie.setCoordinates(0, array);
				// BranchGroup bgLinie = new BranchGroup();
				// Shape3D Achse = new Shape3D(linie,
				// getAppearance(Color.RED));
				//
				// bgLinie.addChild(Achse);
				// draw(bgLinie);

			}
		});

		universe = new SimpleUniverse(canvas);

		ViewingPlatform vp = universe.getViewingPlatform();
		vp.setNominalViewingTransform();

		// Füllen des Universums
		universe.addBranchGraph(erstelleKoordinatensystem());

		TransformGroup ttt = universe.getViewingPlatform()
				.getViewPlatformTransform();
		camTransform = new Transform3D();
		ttt.getTransform(camTransform);

		TransformGroup tg = new TransformGroup(camTransform);
		tg.setTransform(camTransform);
		BranchGroup bgTransform = new BranchGroup();
		bgTransform.addChild(tg);
		_tgMouseBehaviour.addChild(bgTransform);

		this.setVisible(true);
		this.setSize(preferredSize + 200
				+ (this.getWidth() - canvas.getWidth()),
				preferredSize + (this.getHeight() - canvas.getHeight()));
	}

	public Point3d getnaehestenPunkt(double[][][] gatter, Point3d testpunkt) {
		Point3d punktMin = new Point3d(gatter[0][0][0], gatter[0][0][1],
				gatter[0][0][2]);

		double abstandMin = abstand2(punktMin.x, punktMin.y, punktMin.z,
				testpunkt.x, testpunkt.y, testpunkt.z);

		for (int i = 0; i < gatter.length; ++i) {
			for (int j = 0; j < gatter[i].length; ++j) {
				for (int d = 0; d < gatter[i][j].length; ++d) {
					double abstandNeu = abstand2(gatter[i][j][0],
							gatter[i][j][1], gatter[i][j][2], testpunkt.x,
							testpunkt.y, testpunkt.z);
					;
					if (abstandMin > abstandNeu) {
						abstandMin = abstandNeu;
						punktMin.x = gatter[i][j][0];
						punktMin.y = gatter[i][j][1];
						punktMin.z = gatter[i][j][2];
					}
				}
			}
		}
		return punktMin;
	}
	
	

	private static double abstand2(double x1, double y1, double z1, double x2,
			double y2, double z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)
				+ Math.pow(z1 - z2, 2));
	}

	public BranchGroup getText(String uebergebenerText, Point3f position){
		BranchGroup bgText = new BranchGroup();
		Font3D f3d = new Font3D(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 1),
				new FontExtrusion());
		Text3D text = new Text3D(f3d, new String(uebergebenerText), new Point3f(position.x,
				position.y, position.z));
		Shape3D sh = new Shape3D();
		sh.setGeometry(text);
		
		TransformGroup tg = new TransformGroup();
		Transform3D t3d = new Transform3D();
		
		Vector3f v3f = new Vector3f(position.x,
				position.y, position.z);
		t3d.setTranslation(v3f);
		
		tg.setTransform(t3d);
		tg.addChild(sh);
		
		bgText.addChild(tg);
		return bgText;
	}
	
	public BranchGroup erstelleKoordinatensystem() {

		_bgRoot = new BranchGroup();
		_bgRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		_bgRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		_bgRoot.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		_bgRoot.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		BranchGroup bgKoordinatenSystem = new BranchGroup();

		getTgMouseBehaviour();
		
		// X-Achse
		Point3d[] punkteX = new Point3d[2];
		punkteX[0] = new Point3d(0.0, 0.0, 0.0);
		punkteX[1] = new Point3d(2.0, 0.0, 0.0);

		LineArray linieX = new LineArray(2, LineArray.COORDINATES);

		linieX.setCoordinates(0, punkteX);

		Shape3D xAchse = new Shape3D(linieX, getAppearance(Color.RED));

		bgKoordinatenSystem.addChild(xAchse);
		bgKoordinatenSystem.addChild(getText("X", new Point3f(2.05f,0f,0f)));

		// Y-Achse
		Point3d[] punkteY = new Point3d[2];
		punkteY[0] = new Point3d(0.0, 0.0, 0.0);
		punkteY[1] = new Point3d(0.0, 2.0, 0.0);

		LineArray linieY = new LineArray(2, LineArray.COORDINATES);

		linieY.setCoordinates(0, punkteY);

		Shape3D yAchse = new Shape3D(linieY, getAppearance(Color.BLUE));
		bgKoordinatenSystem.addChild(yAchse);
		bgKoordinatenSystem.addChild(getText("Y", new Point3f(0f,2.05f,0f)));

		// Z-Achse
		Point3d[] punkteZ = new Point3d[2];
		punkteZ[0] = new Point3d(0.0, 0.0, 0.0);
		punkteZ[1] = new Point3d(0.0, 0.0, 2.0);

		LineArray linieZ = new LineArray(2, LineArray.COORDINATES);

		linieZ.setCoordinates(0, punkteZ);

		Shape3D zAchse = new Shape3D(linieZ, getAppearance(Color.GREEN));
		bgKoordinatenSystem.addChild(zAchse);
		bgKoordinatenSystem.addChild(getText("Z", new Point3f(0f,0f,2.05f)));
		
		_tgMouseBehaviour.addChild(bgKoordinatenSystem);

		_bgRoot.addChild(_tgMouseBehaviour);

		return _bgRoot;
	}

	// Methode um Matrix mit Vektor zu multiplizieren
	public static Vector3d multiply(Matrix3d matrix, Vector3d vektor) {
		// Vektor wird als Matrix umgeformt
		Matrix3d mCopy = new Matrix3d(matrix);

		Matrix3d v3dAsMatrix = new Matrix3d(vektor.getX(), 0, 0, vektor.getY(),
				0, 0, vektor.getZ(), 0, 0);

		// R mit Vektor multipliziert
		mCopy.mul(v3dAsMatrix);

		// Richtungsvektor
		Vector3d ergebnisvektor = new Vector3d(mCopy.m00, mCopy.m10, mCopy.m20);

		return ergebnisvektor;
	}

	/*
	 * Methode um 2 Vektoren zu addieren
	 */
	public static Vector3d add(Vector3d v1, Vector3d v2) {
		Vector3d v1Copy = new Vector3d(v1);
		v1Copy.add(v2);
		return v1Copy;
	}

	/**
	 * Methode um Aussehen(Farbe) zu bestimmen
	 * 
	 * @param Color
	 *            farbe
	 * @return Appearance
	 */
	public Appearance getAppearance(Color farbe) {
		Appearance optik = new Appearance();
		optik.setColoringAttributes(new ColoringAttributes(farbe.getRed(),
				farbe.getGreen(), farbe.getBlue(), 0));
		// PolygonAttributes attribute = new PolygonAttributes(
		// PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0f);
		// optik.setPolygonAttributes(attribute);
		return optik;
	}

	/**
	 * Methode welche die Eigenschaften der Maus steuert - Rotieren mit linker
	 * Maustaste - Zoom mit mittlerer Maustaste (Rädchen) - Verschiebungen mit
	 * rechter Maustaste
	 * 
	 * @return Transformgroup mit Mauseigenschaften
	 */
	private void getTgMouseBehaviour() {
		_tgMouseBehaviour = new TransformGroup();

		 Vector3d v1 = new Vector3d(-0.85, -0.5, -0.6);
		//Vector3d v1 = new Vector3d(0, 0, 0);

		Transform3D t3D = new Transform3D();
		t3D.setScale(universScale);
		t3D.setTranslation(v1);

		_tgMouseBehaviour.setTransform(t3D);

		_tgMouseBehaviour.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		_tgMouseBehaviour.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		_tgMouseBehaviour.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		_tgMouseBehaviour.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		_tgMouseBehaviour.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		_tgMouseBehaviour.setCapability(BranchGroup.ALLOW_DETACH);

		// Behaviours definieren
		// Bounds, innerhalb deren man sich befinden muss
		BoundingSphere behaveBounds = new BoundingSphere();

		// rotieren mit der linken Maustaste
		MouseRotate behavior4 = new MouseRotate(_tgMouseBehaviour);
		behavior4.setSchedulingBounds(behaveBounds);
		_tgMouseBehaviour.addChild(behavior4);

		// zoomen mit Mausrad
		MouseWheelZoom mouseBeh5 = new MouseWheelZoom(_tgMouseBehaviour);
		mouseBeh5.setSchedulingBounds(behaveBounds);
		_tgMouseBehaviour.addChild(mouseBeh5);

		// verschieben mit der rechten Maustaste
		MouseTranslate mouseBeh6 = new MouseTranslate(_tgMouseBehaviour);
		mouseBeh6.setSchedulingBounds(behaveBounds);
		_tgMouseBehaviour.addChild(mouseBeh6);
	}

	/**
	 * Zeichnet eine BranchGroup in das Universum
	 * 
	 * @param bg
	 * @return
	 */
	public void draw(BranchGroup bg) {
		_tgMouseBehaviour.addChild(bg);
	}

//	private static Point3d[] calcStartEndPoint(Vector3d richtung,
//			Vector3d startwert, double scale) {
//		Vector3d neuRichtung = new Vector3d(richtung);
//		neuRichtung.scale(scale);
//
//		Vector3d endwert = add(startwert, neuRichtung);
//
//		start = new Point3d();
//		start.x = startwert.x;
//		start.y = startwert.y;
//		start.z = startwert.z;
//		//
//		Point3d end = new Point3d();
//		end.x = endwert.x;
//		end.y = endwert.y;
//		end.z = endwert.z;
//
//		Point3d[] array = new Point3d[] { start, end };
//		return array;
//	}
}
