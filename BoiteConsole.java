import java.util.Hashtable;
import java.util.Scanner;

public class BoiteConsole {
	private int taille; // contient la taille de la grille
	private Hashtable<Integer, String> ht; // hashtable servant à convertir la lettre de la colonne en chiffre
	public int[][][] coord; // tableau à 3 dimensions contenant la ligne, la colonne et le sens du trait (1:rempli et 0:vide)
	private char[][]coordCarre; // tableau à deux dimensions contenant les lettres des joueurs qui finissent la case
	private int bot, deuxEnUn; // bot vaut 0 si on joue en 1vs1 et 1 si on joue en 1vsIA et deuxEnUn contient le nombre de point que le joueur a marqué (0, 1 ou 2)
	private char joueur; // char contenant le nom du joueur qui joue
	private Hashtable<Character, Integer> score= new Hashtable<Character, Integer>(); // hashtable associant le nom du joueur a son score
	
	public BoiteConsole(int taille, int bot) { // Constructeur

		this.ht=creationHt();
		this.taille = taille;
		this.bot=bot;
	    this.coord = new int[taille][taille][2];
	    this.coordCarre = new char[taille][taille];
	    this.joueur='A';
			
	    // Initialisation du hastable servant au score
 		this.score.put('A', 0);
 		this.score.put('B', 0);
	    
	    for(int i=0;i<taille-1;i++) {
	    	for(int j =0;j<taille-1;j++) {
	    		this.coordCarre[i][j]='X';
	    	}
	    }
		
		//Affichage de la boite avec les coordonnées - Guide
		int coord=1;
		
		for(int i=0;i<taille;i++) { // i, lignes 1 -> 4
			for(int j=0;j<taille;j++) 
				System.out.print("o---");
			System.out.println(coord+"");
			
			for(int j=0;j<taille;j++)
				System.out.print("|   ");
			System.out.println("");
			
			coord++;
		}
		for(int i=1;i<taille+1;i++) {
			System.out.print(ht.get(i)+"   ");
		}
		System.out.println("");
		
	}

	public void jouer() {
		Scanner sc = new Scanner(System.in);
		String rep;
		int ligne;
		String colonne, sens;
		int[] coordBot;
		
		
		System.out.println("");
		System.out.println("");
		this.affichaBoiteConsole(joueur); // On affiche la console en tenant compte du joueur qui joue 
		System.out.println("  Exemple: 1Bh est pour la premiere ligne, deuxième colonne, trait horizontale, 3Cv..");
		
		while(this.end(score)) { // Boucle qui continue tant que la fonction end() vaut false
			
			if(joueur=='A' || joueur=='B' && bot==0) { // Test si c'est au joueur de jouer 
				System.out.println("     Ou veux-tu jouer joueur "+joueur+" ?");
				rep=sc.nextLine();
			
				// Ces trois séparenet la réponse (ex : 3Bh) en nombres ( même ex : 3 2 0) 
				ligne = Integer.valueOf(rep.substring(0,1)).intValue(); 
				colonne= rep.substring(1,2);
				sens= rep.substring(2);
			}
			else { // C'est au bot de jouer
				coordBot=this.IA();           // Contient les coordonnées que l'IA a choisi
				
				ligne = coordBot[0];         
				colonne= ht.get(coordBot[1]);
				if(coordBot[2]==0)
					sens="h";
				else
					sens="v";
			}
			
			this.remplirCoord(ligne, colonne, sens); 
			
			// Si on a fermé une case, on ajoute le nombre de point que le joueur a marqué (1 ou 2 points) au score du joueur
			if(this.caseFermee(ligne, colonne, sens, joueur))             
				score.put(joueur,score.get(joueur)+this.deuxEnUn);
			this.affichaBoiteConsole(joueur);
			
			System.out.println("Le score des joueurs A et B est respectivement : "+score.get('A')+","+score.get('B')+"\n");
			
			if(joueur=='A' && !this.caseFermee(ligne, colonne, sens, joueur)) // Condition qui change de joueur si le joueur qui a joué à marquer un point
				joueur='B';
			else if(joueur=='B' && !this.caseFermee(ligne, colonne, sens, joueur))
				joueur='A';
		}
		
		sc.close();
	}
	
	// Fonction qui renvoie un tableau avec la colonne en chiffre et le sens en chiffre (ex : C en 3 et v en 1)
	public int[] getCoord(String colonne, String sens) {
		int []coord={0,0};
		ht=creationHt();
		for(int i=1;i<=ht.size();i++) {
			if(colonne.equals(ht.get(i))) {
				if(sens.equals("v")) {
					coord[0]=i;
					coord[1]=1;
				}	
				else 
					coord[0]=i;
			}
		}
		
		return coord;
	}
	
	// Fonction qui remplie le tableau coord en fonction des coordonnées qui lui sont transmises
	public void remplirCoord(int ligne, String colonne, String sens) {
		int []coord= this.getCoord(colonne, sens);
		this.coord[ligne-1][coord[0]-1][coord[1]]=1;
	}
	
	// Affiche la grille en lisant le tableau coord
	public void affichaBoiteConsole(char joueur) {
		
		ht=creationHt();
		
		for(int i=1;i<=taille;i++) { // i, lignes 1 -> 4
			for(int j=1;j<=taille;j++) { 
				if(coord[i-1][j-1][0]==1)
					System.out.print("o---");
				else
					System.out.print("o   ");
			}
			System.out.println("");
			
			for(int j=1;j<=taille;j++) {
				if(coord[i-1][j-1][1]==1) {
					if(this.coordCarre[i-1][j-1]=='A' || this.coordCarre[i-1][j-1]=='B') {
						System.out.print("| "+coordCarre[i-1][j-1]+" ");
					}
					else	
						System.out.print("|   ");
				}
				else
					System.out.print("    ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	// Créer le hashtable permettant d'obtenir le numéro de la colonne ( ex : D en 4)
	public Hashtable<Integer, String> creationHt() {
		Hashtable<Integer, String> ht = new Hashtable<Integer, String>();
		ht.put(1, "A");
		ht.put(2, "B");
		ht.put(3, "C");
		ht.put(4, "D");
		ht.put(5, "E");
		ht.put(6, "F");
		
		return ht;
	}
	
	// Affiche le tableau coord 
	public void afficheCoord(int [][][]tab) {
		for(int i=1;i<=tab.length;i++) {
			for(int j=1;j<=tab.length;j++) {
				System.out.print("("+tab[i-1][j-1][0]+","+tab[i-1][j-1][1]+") \n");

				System.out.println(this.isCaseFilled(i,j, 0));
				System.out.println(this.isCaseFilled(i,j, 1));
			}
			System.out.println("");
		}
	}
	
	// Affiche le tableau coordCarre qui contient les lettres des joueurs ayant fermés les cases
	public void afficheCoordCarre() {
		for(int i=0;i<taille;i++) {
			for(int j=0;j<taille;j++) {
				System.out.print(this.coordCarre[i][j]+" ");
			}
			System.out.println("");
		}
	}
	
	/* Fonction qui renvoie vraie si l'on a fermé une case en mettant un trait aux coord : ligne, colonne, sens
	 *
	 * son fonctionnement, on teste les différentes possibilités
	 * de manière générale on teste sur les 3 autres traits autour de celui ou on est sont remplis
	 */
	public boolean caseFermee(int ligne, String colonne, String sens, char joueur) { 
		int []c = getCoord(colonne, sens);  // sens =0 pour horizontal et =1 pour vertical
		int caseFermee=0;
		
		if(c[0]==1 && c[1]==1) {   //Si on est sur la gauche verticale
			if(coord[ligne-1][c[0]-1][0]==1 && coord[ligne-1][c[0]-1][1]==1 && coord[ligne][c[0]-1][0]==1 && coord[ligne-1][c[0]][c[1]]==1) {
				this.coordCarre[ligne-1][c[0]-1]=joueur;
				caseFermee++;
			}
		}
		else if(c[0]==this.taille && c[1]==1) { // si on est sur la droite verticale
			if(coord[ligne-1][c[0]-2][0]==1 && coord[ligne-1][c[0]-2][1]==1 && coord[ligne][c[0]-2][0]==1 && coord[ligne-1][c[0]-1][c[1]]==1) {
				this.coordCarre[ligne-1][c[0]-2]=joueur;
				caseFermee++;
			}
		}
		else if(ligne==1 && c[1]==0) { // si on est sur le haut horizontale
			if(coord[ligne-1][c[0]-1][0]==1 && coord[ligne-1][c[0]-1][1]==1 && coord[ligne][c[0]-1][0]==1 && coord[ligne-1][c[0]][1]==1) {
				this.coordCarre[ligne-1][c[0]-1]=joueur;
				caseFermee++;
			}
		}
		else if(ligne==this.taille && c[1]==0) { // si on est sur le bas horizontale
			if(coord[ligne-2][c[0]-1][0]==1 && coord[ligne-2][c[0]-1][1]==1 && coord[ligne-2][c[0]][1]==1 && coord[ligne-1][c[0]-1][c[1]]==1){
				this.coordCarre[ligne-2][c[0]-1]=joueur;
				caseFermee++;
			}
		}
		else { // si on est au milieu, horizontal ou vertical
			if(c[1]==0) {
				if(coord[ligne-1][c[0]-1][0]==1 && coord[ligne-1][c[0]-1][1]==1 && coord[ligne][c[0]-1][0]==1 && coord[ligne-1][c[0]][1]==1) {
					this.coordCarre[ligne-1][c[0]-1]=joueur;
					caseFermee++;
				}
				if(coord[ligne-2][c[0]-1][0]==1 && coord[ligne-2][c[0]-1][1]==1 && coord[ligne-2][c[0]][1]==1 && coord[ligne-1][c[0]-1][c[1]]==1){
					this.coordCarre[ligne-2][c[0]-1]=joueur;
					caseFermee++;
				}
			}
			else {
				if(coord[ligne-1][c[0]-1][0]==1 && coord[ligne-1][c[0]-1][1]==1 && coord[ligne][c[0]-1][0]==1 && coord[ligne-1][c[0]][c[1]]==1) {
					this.coordCarre[ligne-1][c[0]-1]=joueur;
					caseFermee++;
				}
				if(coord[ligne-1][c[0]-2][0]==1 && coord[ligne-1][c[0]-2][1]==1 && coord[ligne][c[0]-2][0]==1 && coord[ligne-1][c[0]-1][c[1]]==1) {
					this.coordCarre[ligne-1][c[0]-2]=joueur;
					caseFermee++;
				}
			}
			
			this.deuxEnUn=caseFermee;
		}
		
		if(caseFermee==0)
			return false;
		else
			return true;
	}
	
	// Fonction servant à tester si le bot peut fermer une case (fonction différente de caseFermee() car le trait n'a pas déjà été tracé
	public boolean testCaseIA(int ligne, int colonne, int sens) {
		int []c = {colonne, sens};
		
		if(c[0]==1 && c[1]==1) {   //Si on est sur la gauche verticale
			if(coord[ligne-1][c[0]-1][0]==1 && coord[ligne][c[0]-1][0]==1 && coord[ligne-1][c[0]][c[1]]==1) {
				return true;
			}
		}
		else if(c[0]==this.taille && c[1]==1) { // si on est sur la droite verticale
			if(coord[ligne-1][c[0]-2][0]==1 && coord[ligne-1][c[0]-2][1]==1 && coord[ligne][c[0]-2][0]==1) {
				return true;
			}
		}
		else if(ligne==1 && c[1]==0) { // si on est sur le haut horizontale
			if(coord[ligne-1][c[0]-1][1]==1 && coord[ligne][c[0]-1][0]==1 && coord[ligne-1][c[0]][1]==1) {
				return true;
			}
		}
		else if(ligne==this.taille && c[1]==0) { // si on est sur le bas horizontale
			if(coord[ligne-2][c[0]-1][0]==1 && coord[ligne-2][c[0]-1][1]==1 && coord[ligne-2][c[0]][1]==1){
				return true;
			}
		}
		else { // si on est au milieu, horizontal ou vertical
			if(c[1]==0) {
				if(coord[ligne-1][c[0]-1][1]==1 && coord[ligne][c[0]-1][0]==1 && coord[ligne-1][c[0]][1]==1) {
					return true;
				}
				if(coord[ligne-2][c[0]-1][0]==1 && coord[ligne-2][c[0]-1][1]==1 && coord[ligne-2][c[0]][1]==1){
					return true;
				}
			}
			else {
				if(coord[ligne-1][c[0]-1][0]==1 && coord[ligne][c[0]-1][0]==1 && coord[ligne-1][c[0]][1]==1) {
					return true;
				}
				if(coord[ligne-1][c[0]-2][0]==1 && coord[ligne-1][c[0]-2][1]==1 && coord[ligne][c[0]-2][0]==1) {
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	// Fonction qui d'abord vérifie si le bot peut fermer une case et s'il ne peut pas, il place un trait aléatoirement
	public int[] IA() {
		int ligne, colonne, sens; 
		
		do {
			ligne=(int)Math.floor(Math.random()*taille)+1;
			colonne=(int)Math.floor(Math.random()*taille)+1;
			sens=(int)Math.floor(Math.random()*2);
		} while( ((ligne==this.taille && sens==1) || (colonne==this.taille && sens==0)) || this.isCaseFilled(ligne, colonne, sens));
		
		int[] coordIA={ligne, colonne, sens};
		
		//this.afficheCoord(this.coord);
		
		
		
		coordIA[0]=ligne;coordIA[1]=colonne;coordIA[2]=sens;
		

		return coordIA;
	}
	
	// Fonction qui renvoie true si le trait aux coord ligne, colonne et sens est rempli, false sinon
	public boolean isCaseFilled(int ligne, int colonne, int sens) {
		if(this.coord[ligne-1][colonne-1][sens]==1)
			return true;
		else
			return false;
	}
	
	// Fonction vérifiant si le jeu est fini en additionnant le score des deux joueurs et en regardant si on a atteint le score maximum
	public boolean end(Hashtable<Character, Integer> score) {
		if(score.get('A')+score.get('B')==Math.pow(this.taille-1, 2)) {
			if(score.get('A') > score.get('B'))
				System.out.println("Le joueur A a gagné avec un score de "+score.get('A')+" à "+score.get('B'));
			else if(score.get('A') < score.get('B'))
				System.out.println("Le joueur B a gagné avec un score de "+score.get('B')+" à "+score.get('A'));
			else
				System.out.println("Vous êtes ex-aequo");
			return false;
		}
		return true;
	}
}
