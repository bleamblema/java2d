package en.bleamblema.java2dgame.gfx;

public class Colours {
	
	public static int get(int col1, int col2, int col3, int col4){

		return ((get(col4)<<24) + (get(col3)<<16) + (get(col2)<<8) + get(col1));
	}

	private static int get(int col) {
		if(col < 0) return 255;
		int r = col / 100 % 10;
		int g = col / 10  % 10;
		int b = col 	  % 10;
		return r*36 + g*6 + b;
	}
}
