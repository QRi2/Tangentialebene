//package Model;
//
//
//public class NewtonFunktionA implements INewtonFunktion {
//
//	Vector3d richtung;
//	Vector3d startwert;
//	
//	public NewtonFunktionA(Vector3d richtung, Vector3d startwert) {
//		this.richtung = new Vector3d(richtung);
//		this.startwert = new Vector3d(startwert);
//	}
//	
//	@Override
//	public double berechneFunktionswert(double c) {
//		double x = (c * richtung.x) + startwert.x;
//		double y = (c * richtung.y) + startwert.y;
//		
//		double value = FunktionA.getFunktionswert(x, y) - ((c* richtung.z) + startwert.z);
//		
//		return value;
//	}
//
//	@Override
//	public double berechneAbleitung(double c) {
//		// FunktionA(x,y) = Math.sin(x+y)
//		// NewtonFuntion = FunktionA(c*r_x+s_x,c*r_y+s_y)-c*r_z+s_z
//		// NewtonFuntion' = cos(c*r_x+c*r_y+s_x+s_y)*(r_x+r_y)-r_z 
//		return (Math.cos((c*(richtung.x+richtung.y))+startwert.x+startwert.y) * (richtung.x+richtung.y)) - richtung.z;
//	}
//
//}
