import java.util.Scanner;

public class DotsAndBoxes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Initialisation des variables
		Scanner sc = new Scanner(System.in);
		String rep;
		int taille=0, bot; 

 		// Début du jeu
		System.out.println("============= DOTS AND BOXES =============");
		System.out.println("Voulez -vous jouer en mode console ou mode Graphique? (C/G)");
		rep= sc.nextLine();
		
		System.out.println("Voulez -vous jouez en 1V1 ou 1VIA ? (0:1V1, 1:1VIA)");
		bot= sc.nextInt();
		
		while(taille<3 ||taille>6) {
			System.out.println("Veuillez choisir la taille de votre Grille (entre 3 et 6) :  ");
			taille=sc.nextInt();
			sc.nextLine();
		}
		
		// Si le joueur a demandé à jouer en mode console on éxécute le jeu en console
		if(rep.equals("C")) { 
			
			BoiteConsole grille = new BoiteConsole(taille, bot);
			grille.jouer();
		}
		
		// Si le joueur a demandé à jouer en mode graphique on éxécute le jeu en graphique
		if(rep.equals("G")) {
			
			Boite grille = new Boite(taille, bot);
			grille.jouer();
		}
		sc.close();
	}

}
