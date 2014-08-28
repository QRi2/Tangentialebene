package Model;

import javax.vecmath.Vector3d;

public class Gerade3D {
	public Vector3d Ortsvektor,
		Richtungsvektor;
	
	public Gerade3D(double ox, double oy, double oz, double rx, double ry, double rz)
	{
		this.Ortsvektor = new Vector3d(ox, oy, oz);
		this.Richtungsvektor = new Vector3d(rx, ry, rz);
	}
	
	public Gerade3D(Vector3d ort, Vector3d richtung) {
		this.Ortsvektor = ort;
		this.Richtungsvektor = richtung;
	}
	
	public Vector3d getPoint(double c) {
		return new Vector3d((Richtungsvektor.x * c) + Ortsvektor.x, (Richtungsvektor.y * c) + Ortsvektor.y, (Richtungsvektor.z * c) + Ortsvektor.z);
	}
	
}
