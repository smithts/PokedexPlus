import java.awt.*;
import java.util.*;
import java.io.*;

public class PokedexPlus {
	/**
	 * Improvements:
	 * 	- General Redundancy
	 * 	- Needs to factor in both types (when available)
	 * 	- Run using a .jar somehow
	 * 
	 * */
	
	private static final String[] LABELS = {"HP","Atk","Def","Sp.Atk","Sp.Def", "Speed"};
	private static final String[] TYPES = {"Normal", "Fighting", "Flying", "Poison", "Ground",
			"Rock", "Bug", "Ghost", "Steel", "Fire", "Water", "Grass", "Electric", "Psychic", 
			"Ice", "Dragon", "Dark"};
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    
    private static Map<String, double[]> strengths;
    private static String myPoke;
    private static String otherPoke;
    private static Map<String, Color> colors;
    private static ArrayList<Stat> myStats;
    private static String[] myTypes;
    private static ArrayList<Stat> otherStats;
    private static String[] otherTypes;
    private static DrawingPanel p;
    private static Graphics g;

    private static PokedexPlusHTTPClient pHTTPClient = new PokedexPlusHTTPClient();
    
	
	public static void main (String[] args) throws IOException {
        //pHTTPClient.getStats();
		intro();
		int analyze = 1;
		Scanner console = new Scanner(System.in);
        while (analyze == 1) {
        	// input chosen pokemon name and gather necessary data  
            myPoke = getName("Choose your Pokemon! ");
            myStats = getStats(myPoke);
            while (myStats == null) {
                errorMessage();
                myPoke = getName("Choose your Pokemon! ");
                myStats = getStats(myPoke);
            }

            myTypes = getType(myPoke);
            
            // input opponent pokemon name and gather necessary data
            otherPoke = getName("Choose it's opponent! ");
            otherStats = getStats(otherPoke);
            while (otherStats == null) {
                errorMessage();
                otherPoke = getName("Choose it's opponent! ");
                otherStats = getStats(otherPoke);
            }
            otherTypes = getType(otherPoke);
    		
            printInfo();
            PokemonMaps maps = new PokemonMaps();
            if (myPoke.equalsIgnoreCase(otherPoke)) {
            	System.out.println("These two pokemon are the same.  No reason to have them battle, is there?");
            } else {
            	strengths = maps.getStrengths();
            	battle();
            }
            System.out.println();
            
            // Graphical display
            colors = maps.getColors();
            p = new DrawingPanel(WIDTH, HEIGHT);
            p.setBackground(Color.LIGHT_GRAY);
            g = p.getGraphics();
            drawGraph();
            drawStats();
            
            System.out.print("Would you like to try again?  (1 = Yes, AnyOther = No) ");
            analyze = console.nextInt();
            System.out.println();
            
            if (analyze != 1) {
            	outtro();
            }
            
        }
        
	
	}
	
	public static void errorMessage() {
		System.out.println("That's not a recognized Pokemon!");
        System.out.println("Be sure to check your spelling.");
        System.out.println("(Pokemon with 2 word names should be inputted without the space)");
        System.out.println();
	}
	
    public static void intro() {
        System.out.println("Welcome, Pokemon Master!");
        System.out.println();
        System.out.println("I may look like just a Pokedex, but I am much more than that.");
        System.out.println("I do things like find stats, types, and other information...");
        System.out.println("but I will also simulate a battle for you.");
        System.out.println("Don't believe me?  Just try for yourself!");
        System.out.println();
    }
    
    public static void outtro() {
		System.out.println("Thanks for using Pokedex+.  Come back Some other time!");
    	System.out.println();
    	System.out.println("Created by: Tanner J. Smith, inc.");
    	System.out.println("Math 480, Spring, 2014");
	}
    
    public static void printInfo() {
    	System.out.print(myTypes[0] + " is a " + myTypes[1]);
    	if (!myTypes[2].equals("None")) {
    		System.out.print(" and " + myTypes[2]);
    	}
    	System.out.println(" type Pokemon.");
    	
    	System.out.println("It utilizes " + myTypes[3] + " as its ability.");
    	System.out.println();
    	
    }
    
	// Prompts the user for a name, and returns it to the Main.
	public static String getName (String s) {
		Scanner console = new Scanner(System.in);
		System.out.println(s);
        System.out.print("I CHOOSE YOU... ");
		String name = console.next();
        System.out.println();
		return name;
	}
    
    public static ArrayList<Stat> getStats(String name) throws IOException {
	    return pHTTPClient.getStats(name);
    }
    
    public static String[] getType(String name) throws FileNotFoundException {
        Scanner typeScan = new Scanner(new File("res/poke_types.txt"));
        while (typeScan.hasNextLine()) {
            String line = typeScan.nextLine();
            Scanner lineScan = new Scanner(line);
            String lineName = lineScan.next();
            
            if (lineName.equalsIgnoreCase(name)) {
                String[] types = new String[4];
                types[0] = lineName;
                types[1] = lineScan.next();
                types[2] = lineScan.next();
                types[3] = lineScan.nextLine().trim();
                return types;
            }
        }
        return null;
    }
    
    public static void drawGraph() {
        // Drawing the header and footer
        g.setColor(Color.RED);
        g.fillRect(0, 0, WIDTH, 50);
        g.fillRect(0, HEIGHT - 50, WIDTH, 50);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, 50);
        g.drawRect(0, HEIGHT - 50, WIDTH, 50);
        
        // Drawing the name boxes
        g.setColor(Color.WHITE);
        g.fillRect(5, 5, WIDTH / 2 - 50, 35);
        g.fillRect(335, 5, WIDTH / 2 - 50, 35);
        g.setColor(Color.BLACK);
        g.drawRect(5, 5, WIDTH / 2 - 50, 35);
        g.drawRect(335, 5, WIDTH / 2 - 50, 35);
        
        g.drawString("VS.", WIDTH / 2 - 10, 25);
        
        // Drawing the Type Labels
        g.setColor(Color.WHITE);
        for (int i = 0; i < 6; i++) {
           g.drawString(LABELS[i], 30 + i * 100, HEIGHT - 35); 
        }
    }
    
    public static void drawStats() {
        g.setColor(Color.BLACK);
        g.drawString(myTypes[0] + " is using " + myTypes[3], 15, 25);
        g.drawString(otherTypes[0] + " is using " + otherTypes[3], 345, 25);
        
        Color myPokeColor = colors.get(myTypes[1]);
        Color otherPokeColor = colors.get(otherTypes[1]);
        
        
        for (int i = 0; i < myStats.size(); i++) {
        	int base = HEIGHT - 50;
        	
        	g.setColor(myPokeColor);
        	int myBasePower = myStats.get(i).getBasePower();
        	int otherBasePower = otherStats.get(i).getBasePower();

            int myPokeStart = HEIGHT - (50 + 2 * myBasePower);
            g.fillRect(20 + 100 * i, myPokeStart, 20, base - myPokeStart);
            g.drawString(myBasePower + "", 20 + 100 * i, myPokeStart );
            
            g.setColor(otherPokeColor);
            int otherPokeStart = HEIGHT - (50 + 2 * otherBasePower);
            g.fillRect(40 + 100 * i, otherPokeStart, 20, base - otherPokeStart);
            g.drawString(otherBasePower + "", 40 + 100 * i, otherPokeStart );
            
            g.setColor(Color.BLACK);
            g.drawRect(20 + 100 * i, myPokeStart, 20, base - myPokeStart);
            g.drawString(myBasePower + "", 20 + 100 * i + 1, myPokeStart );
            g.drawRect(40 + 100 * i, otherPokeStart, 20, base - otherPokeStart);
            g.drawString(otherBasePower + "", 40 + 100 * i + 1, otherPokeStart );
        }
    }   
    
    public static void battle() {
        Random rand = new Random();
        int r = rand.nextInt(16) + 85;
        
        // Bulbasaur Example: 
        // [45,  49,  49, 65, 65, 45, 318]
        // [HP, ATK, DEF, SA, SD, SP, TOT]
        
        // averages the two attack stats, making "AttackPower"
        double myAtkPwr = (myStats.get(1).getBasePower() + myStats.get(3).getBasePower()) / 2.0;
        double otherAtkPwr = (otherStats.get(1).getBasePower() + otherStats.get(3).getBasePower()) / 2.0;
        
        // Gathers the type advantages and disadvantages of both Pokemon
        double myAdvantage = getDamage(myTypes[1], otherTypes[1]);
        double otherAdvantage = getDamage(otherTypes[1], myTypes[1]);
        
        // Calculates the physical attack and special attack power dealt by the users chosen pokemon
        double myPDamage = ((((22) * myStats.get(1).getBasePower() * myAtkPwr / otherStats.get(2).getBasePower()) / 50) + 2)  * r / 100;
        double mySDamage = ((((22) * myStats.get(3).getBasePower() * myAtkPwr / otherStats.get(4).getBasePower()) / 50) + 2) * myAdvantage * r / 100;
        
        // Calculates the physical attack and special attack power dealt by the enemy pokemon
        double otherPDamage = ((((22) * otherStats.get(1).getBasePower() * otherAtkPwr / myStats.get(2).getBasePower()) / 50) + 2) * r / 100;
        double otherSDamage = ((((22) * otherStats.get(3).getBasePower() * otherAtkPwr / myStats.get(4).getBasePower()) / 50) + 2) * otherAdvantage * r / 100;
        
        System.out.println("Now...let the Pokemon battle commence!");
    	System.out.println("**Imagine the 8 bit Pokemon battle music now!**");
    	System.out.println();
        process(myPDamage, mySDamage, otherPDamage, otherSDamage);
    }
    
    public static void process(double myPDamage, double mySDamage, double otherPDamage, double otherSDamage) {
    	int mpd = (int) Math.round(myPDamage);
    	int msd = (int) Math.round(mySDamage);
    	int opd = (int) Math.round(otherPDamage);
    	int osd = (int) Math.round(otherSDamage);
    	
    	int myWins = 0;
    	int otherWins = 0;
    	
    	int mySpeed = myStats.get(5).getBasePower();
    	int otherSpeed = otherStats.get(5).getBasePower();
    	
    	String mine = myTypes[0];
    	String other = otherTypes[0];
    	
    	//-------------- Speed Battle --------------//
    	boolean meFirst;
    	if (mySpeed > otherSpeed) {
    		System.out.println(mine + " will strike first! Way to go " + mine + "!");
    		myWins++;
    		meFirst = true;
    	} else if (mySpeed < otherSpeed) {
    		System.out.println(other + " will strike first.  What a bummer!");
    		otherWins++;
    		meFirst = false;
    	} else {
    		System.out.println("These Pokemon's speeds are identical!  There's no telling who will strike first!");
    		Random r = new Random();
    		meFirst = r.nextBoolean();
    	}
    	
    	//-------------- Physical Battle --------------//
    	System.out.println("\n" + "Physical Attacks: ");
    	if (meFirst) {
    		System.out.println("Using only physical attacks, " + mine + " will hit for " + mpd + " damage points.");
    		System.out.println("Meanwhile, " + other + " will hit for " + opd + " damage points.");
    	} else {
    		System.out.println("Using only physical attacks, " + other + " will hit for " + opd + " damage points.");
    		System.out.println("Meanwhile, " + mine + " will hit for " + mpd + " damage points.");
    	}
    	
    	if (mpd > opd) {
    		System.out.println("Looks like " + mine + " will win this round! Way to go!");
    		myWins++;
    	} else if (mpd < opd) {
    		System.out.println("Looks like " + other + " will win this round! Damn!");
    		otherWins++;
    	} else {
    		System.out.println("These two are neck and neck! Can't wait to see how it pans out!");
    	}
    	
    	//-------------- Special Battle --------------//
    	System.out.println("\n" + "Special Attacks: ");
    	if (meFirst) {
    		System.out.println("Using only special attacks, " + mine + " will hit for " + msd + " damage points.");
    		System.out.println("Meanwhile, " + other + " will hit for " + osd + " damage points.");
    	} else {
    		System.out.println("Using only physical attacks, " + other + " will hit for " + osd + " damage points.");
    		System.out.println("Meanwhile, " + mine + " will hit for " + msd + " damage points.");
    	}
    	
    	if (msd > osd) {
    		System.out.println("Looks like " + mine + " is a bit more \"special\"! Hyuh hyuh hyuh hyuh");
    		myWins++;
    	} else if (msd < osd) {
    		System.out.println("Looks like " + other + " has got more specialishness! DAMN!");
    		otherWins++;
    	} else {
    		System.out.println("These two are equally as special! What is this, a youth soccer game?!");
    	}
    	System.out.println();
    	
    	//-------------- Final Tally --------------//
    	if (myWins > otherWins) {
    		System.out.println(mine + " won " + myWins + " of the three possible categories!");
    		System.out.println("Looks like you chose wisely with " + mine + ".");
    		System.out.println("Together, you two will be unstoppable!");
    	} else if (myWins < otherWins) {
    		System.out.println(mine + " won only " + myWins + " of the three possible categories.");
    		System.out.println("Looks like you should have gone with " + other + ".");
    		System.out.println("But hey, at least you can carry 6 Pokemon total, right?!");
    	} else {
    		System.out.println(mine + " and " + other + " are very very evenly matched.");
    		System.out.println("Maybe you should have them both in your party?");
    	}
    }
    
    public static double getDamage(String attacker, String defender) {
    	double[] stats = strengths.get(attacker);
    	for (int i = 0; i < TYPES.length; i++) {
    		if (TYPES[i].equalsIgnoreCase(defender)) {
    			return stats[i];
    		}
    	}
    	return -1;
    }
    
}