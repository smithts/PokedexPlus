import java.awt.*;
import java.io.*;
import java.util.*;

public class PokemonMaps {
	private Map<String, Color> colors;
	private Map<String, double[]> strengths;
	
	public PokemonMaps() throws FileNotFoundException {
		// Makes the strength map
		strengths = new HashMap<String, double[]>();
    	Scanner strengthScan = new Scanner(new File("res/advantages.txt"));
    	while (strengthScan.hasNextLine()) {
    		String line = strengthScan.nextLine();
    		Scanner lineScan = new Scanner(line);
    		String type = lineScan.next();
    		double[] d = new double[17];
    		for (int i = 0; i < 17; i++) {
    			d[i] = lineScan.nextDouble();
    		}
    		strengths.put(type, d);
    	}
    	
    	// makes the color map
    	colors = new HashMap<String, Color>();
        Scanner colorScan = new Scanner(new File("res/colors.txt"));
        while (colorScan.hasNextLine()) {
        	String line = colorScan.nextLine();
        	Scanner lineScan = new Scanner(line);
        	String type = lineScan.next();
        	int r = lineScan.nextInt();
        	int g = lineScan.nextInt();
        	int b = lineScan.nextInt();
        	colors.put(type, new Color(r, g, b));
        }
	}
	
	public Map<String, Color> getColors() {
		return colors;
	}
	
	public Map<String, double[]> getStrengths() {
		return strengths;
	}
}
