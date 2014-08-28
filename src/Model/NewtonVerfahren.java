//package Model;
//
//public class NewtonVerfahren {
//	INewtonFunktion f;
//	double c;
//	
//	public NewtonVerfahren(INewtonFunktion funktion, double startwert) {
//		f = funktion;
//		c = startwert;
//	}
//	
//	private void iteration() {
//		c = c - (f.berechneFunktionswert(c) / f.berechneAbleitung(c));
//	}
//	
//	public double iteriere(int counter) {
//		for (int i = 0; i < counter; ++i) {
//			iteration();
//		}
//		
//		return c;
//	}
//	
//	
//}
