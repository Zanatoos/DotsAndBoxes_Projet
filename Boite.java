import java.util.Hashtable;


public class Boite {
	private int taille;
	private int[][][] coord;
	private int []coordBot={0,0,0};
	private int bot, deuxEnUn;
	private double e; // contient l'epaisseur du trait qu'on veut
	private String[] joueurs= new String[2]; // contient les noms des joueurs qui joue
	private String joueur;
	private Hashtable<String, Integer> score= new Hashtable<String, Integer>(); // Initialisation du hastable servant au score
	
	private int l, d, t; // l: taille de la fenetre, d: espace entre chaque point, t: espace entre le bord de la fenetre et la grille
	private boolean ajoue; // variable valant true si c'est au bot de jouer, false sinon
	
	// Constructeur
	public Boite(int taille, int bot) {
		// On initialise les variables de classe
		this.taille=taille;
		this.bot=bot;
		this.joueurs[0]="J1";
		if(bot==0)
			this.joueurs[1]="J2";
		else
			this.joueurs[1]="Ordi";
		this.joueur=joueurs[0];
		score.put(this.joueurs[0], 0);
		score.put(this.joueurs[1], 0);
		this.coord= new int[taille][taille][2];
		this.ajoue=false;
		
		this.t=10;
		this.d=30;
		this.l=t+this.d*(this.taille-1)+40;
		this.e = 0.01;
		
		// On affiche la fenetre
		StdDraw.setXscale(0, l);
		StdDraw.setYscale(0, l);
		StdDraw.clear(StdDraw.GRAY);
		
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.text(l/2, l-t, "Dots and Boxes");
			// Affichage des lignes blanches
		for(int i=0;i<taille;i++) {
			StdDraw.line(t+i*d, t, t+i*d, t+(taille-1)*d);
			StdDraw.line(t, t+i*d, t+(taille-1)*d, t+i*d);
		}
		
		// Affichage des points
		StdDraw.setPenColor();
		int x,y;
		for(int i=0;i<this.taille;i++)  {
			for(int j=0;j<this.taille;j++) {
				x=t+d*i; y=t+d*j;
				StdDraw.filledCircle(x, y, 3);
			}
		}
		
		// On affiche la fenetre que l'on a modifié
		StdDraw.show(20);
		
	}
	
	public void jouer() {
		StdDraw.setPenRadius(e);
		
		double xClick, yClick, x, y,s;
		
		// Boucle principale du jeu qui s'arrête quand la fonction end vaut false
		while(this.end()) { 
			// Affichage du nom des joueurs et du score sur le côté
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.filledRectangle(l-t+3, t+d*taille/2, 30, 70);
			
			if(joueurs[0].equals(joueur)) // Met le nom du joueur qui joue en rouge
				StdDraw.setPenColor(StdDraw.BOOK_BLUE);
			StdDraw.textRight(l-2, l-40,joueurs[0]);
			StdDraw.setPenColor();
			StdDraw.textRight(l-2, l-50,score.get(joueurs[0]).toString());
			
			if(joueurs[1].equals(joueur))  // Met le nom du joueur qui joue en rouge
				StdDraw.setPenColor(StdDraw.BOOK_BLUE);
			StdDraw.textRight(l-2, l-70,joueurs[1]);
			StdDraw.setPenColor();
			StdDraw.textRight(l-2, l-80,score.get(joueurs[1]).toString());
			
			// Lance une boucle infini qui s'arrête que quand le joueur a cliqué
			while(!StdDraw.mousePressed()) {
				StdDraw.setPenRadius();
				StdDraw.setPenColor(StdDraw.MAGENTA);
				xClick=StdDraw.mouseX();yClick=StdDraw.mouseY();
				for(int i=0;i<this.taille;i++) {
					for(int j=0;j<this.taille;j++) {
						x=t+i*d;y=t+j*d; // calcule les coordonnées en pixels
						if(xClick>x+3 && xClick<x+d-3 && yClick>y-3 && yClick<y+3 && x<l-40) { // Si on se situe sur un trait horizontal
							if(!this.isCaseFilled(i, j, 0)) { // Si le trait ou l'on se situe n'est pas déjà rempli
								StdDraw.line(x+3, y, x+d-3, y); // On affiche le trait
								StdDraw.show(20);
								StdDraw.setPenColor(StdDraw.WHITE);
								StdDraw.line(x+3, y, x+d-3, y);
							}
						}
						if(yClick>y+3 && yClick<y+d-3 && xClick>x-3 && xClick<x+3 && y<l-40) { // même chose que précedemment mais pour les traits verticaux
							if(!this.isCaseFilled(i, j, 1)) {
								StdDraw.line(x, y+3, x, y+d-3);
								StdDraw.show(20);
								StdDraw.setPenColor(StdDraw.WHITE);
								StdDraw.line(x, y+3, x, y+d-3);
							}
						}
					}	
				}
			}
			// La boucle s'est arrêté donc le joueur a cliqué alors on recupere les coordonnées de la souris 
			xClick= StdDraw.mouseX();
			yClick= StdDraw.mouseY();
			
			StdDraw.setPenRadius(this.e);
			if(joueur==joueurs[0])
				StdDraw.setPenColor(StdDraw.RED);
			else
				StdDraw.setPenColor(StdDraw.BLUE);
			
			// Lance un boucle qui va tester tous les points de la grille
			for(int i=0;i<this.taille;i++) {
				for(int j=0;j<this.taille;j++) {
					x=t+i*d;y=t+j*d; // calcule les coordonnées en pixels
					if(xClick>x && xClick<x+d && yClick>y-3 && yClick<y+3 && x<l-40) { // Si on se situe sur un trait horizontal
						if(!this.isCaseFilled(i, j, 0)) { // Si le trait ou l'on se situe n'est pas déjà rempli
							this.coord[i][j][0]=1; // On remplit le trait ou l'on est
							this.ajoue=true;
							
							StdDraw.line(x, y, x+d, y); // On affiche le trait
							
							if(this.caseFermee(i, j, 0, joueur)) { // Si le trait qu'on a mis a fermé une case
								this.score.put(joueur, this.score.get(joueur)+this.deuxEnUn); // on augmente le score du joueur qui joue
								this.deuxEnUn=0; 
							}
							else { // si on a pas fermé de case on change de joueur
								if(joueur.equals(joueurs[0]))
									joueur=joueurs[1];
								else
									joueur=joueurs[0];
							}
								
						}
					}
					if(yClick>y && yClick<y+d && xClick>x-3 && xClick<x+3 && y<l-40) { // même chose que précedemment mais pour les traits verticaux
						if(!this.isCaseFilled(i, j, 1)) {
							this.coord[i][j][1]=1;
							this.ajoue=true;
							
							StdDraw.line(x, y, x, y+d);
							
							if(this.caseFermee(i, j, 1, joueur)) {
								this.score.put(joueur, this.score.get(joueur)+this.deuxEnUn);
								this.deuxEnUn=0;
							}
							else {
								if(joueur.equals(joueurs[0]))
									joueur=joueurs[1];
								else
									joueur=joueurs[0];
							}
						}
					}
				}
			}
			
			// Si le joueur a joué et que c'est au bot de jouer
			if(this.bot==1 && joueur.equals(joueurs[1]) && this.ajoue) {
				this.coordBot=this.IA(); // On récupère les coordonnées du point que le bot a choisi
				this.coord[coordBot[0]][coordBot[1]][coordBot[2]]=1; // On remplit le trait dans le tableau
				x=coordBot[0]*d+t;y=t+coordBot[1]*d;s=coordBot[2]; // On calcule les coordonnées en pixels
				StdDraw.setPenColor(StdDraw.BLUE);
				if(s==0) // Si le trait est horizontal on affiche le trait horizontal
					StdDraw.line(x, y, x+d, y);
				else
					StdDraw.line(x, y, x, y+d);
				
				// Comme précedemment
				if(this.caseFermee(coordBot[0], coordBot[1], coordBot[2], joueur)) {
					this.score.put(joueur, this.score.get(joueur)+this.deuxEnUn);
					this.deuxEnUn=0;
					StdDraw.setPenColor(StdDraw.GRAY);
					StdDraw.filledRectangle(l-t+3, t+d*taille/2, 30, 50);
				}
				else {
					joueur=joueurs[0];
					this.ajoue=false;
				}
			}
			
			StdDraw.show(20);
		
		}	
	}

	// Fonction qui test si le jouer a fermé un carré
	public boolean caseFermee(int i, int j, int s, String joueur) { 
		int caseFermee=0; // compte le nombre de trait fermé (vaut 0,1 ou 2)
		StdDraw.setPenColor();
		
		if(j==0 && s==0) { // en bas
			if(coord[i][j][s]==1 
					&& coord[i][j][1]==1 
					&& coord[i+1][j][1]==1
					&& coord[i][j+1][0]==1) {
				caseFermee++;
				StdDraw.text(t+i*d+d/2, t+j*d+d/2, this.joueur, 0);
			}
		}
		else if(j==this.taille-1 && s==0) { // en haut
			if(coord[i][j][s]==1 
					&& coord[i][j-1][0]==1 
					&& coord[i][j-1][1]==1
					&& coord[i+1][j-1][1]==1) {
				caseFermee++;
				StdDraw.text(t+i*d+d/2, t+j*d-d/2, this.joueur, 0);
			}
		}
		else if(i==0 && s==1) { // a gauche
			if(coord[i][j][s]==1 
					&& coord[i][j][0]==1 
					&& coord[i+1][j][1]==1
					&& coord[i][j+1][0]==1) {
				caseFermee++;
				StdDraw.text(t+i*d+d/2, t+j*d+d/2, this.joueur, 0);
			}
		}
		else if(i==this.taille-1 && s==1) { // a droite
			if(coord[i][j][s]==1 
					&& coord[i-1][j][0]==1 
					&& coord[i-1][j][1]==1
					&& coord[i-1][j+1][0]==1) {
				caseFermee++;
				StdDraw.text(t+i*d-d/2, t+j*d+d/2, this.joueur, 0);
			}
		}
		else { // si on est sur aucun des bords
			if(s==0) { // si on est sur un trait horizontal
				if(coord[i][j][s]==1  // on test en bas
						&& coord[i][j][1]==1 
						&& coord[i+1][j][1]==1
						&& coord[i][j+1][0]==1) {
					caseFermee++;
					StdDraw.text(t+i*d+d/2, t+j*d+d/2, this.joueur, 0);
				} 
				if(coord[i][j][s]==1 // on test en haut
						&& coord[i][j-1][0]==1 
						&& coord[i][j-1][1]==1
						&& coord[i+1][j-1][1]==1) {
					caseFermee++;
					StdDraw.text(t+i*d+d/2, t+j*d-d/2, this.joueur, 0);
				}
			}
			else { // On est sur un trait vertical
				if(coord[i][j][s]==1 // on test à gauche
						&& coord[i][j][0]==1 
						&& coord[i+1][j][1]==1
						&& coord[i][j+1][0]==1) {
					caseFermee++;
					StdDraw.text(t+i*d+d/2, t+j*d+d/2, this.joueur, 0);
				}
				if(coord[i][j][s]==1  // on test à droite
						&& coord[i-1][j][0]==1 
						&& coord[i-1][j][1]==1
						&& coord[i-1][j+1][0]==1) {
					caseFermee++;
					StdDraw.text(t+i*d-d/2, t+j*d+d/2, this.joueur, 0);
				}
			}
		}
		
		this.deuxEnUn=caseFermee; 
		
		// Si on a pas fermé de case on renvoie false
		if(caseFermee==0)
			return false;
		else
			return true;
	}

	// cette fonction m'a servi à débuguer le programme en affichant le tableau coord
	public void afficheCoord(int [][][]tab) {
		for(int i=0;i<tab.length;i++) {
			for(int j=0;j<tab.length;j++) {
				System.out.print("("+tab[i][j][0]+","+tab[i][j][1]+")");
			}
			System.out.println("");
		}

		System.out.println("");
	}

	// fonction similaire a caseFermee sauf qu'elle sert à voir si le bot peut fermé une case en mettant ce trait de coord i,j,s
	public boolean testCaseIA(int i, int j, int s) {
		int caseFermee=0;
		StdDraw.setPenColor();
		
		if(j==0 && s==0) { // en bas
			if(coord[i][j][1]==1 
					&& coord[i+1][j][1]==1
					&& coord[i][j+1][0]==1) {
				caseFermee++;
				StdDraw.text(t+i*d+d/2, t+j*d+d/2, this.joueur, 0);
			}
		}
		else if(j==this.taille-1 && s==0) { // en haut
			if(coord[i][j-1][0]==1 
					&& coord[i][j-1][1]==1
					&& coord[i+1][j-1][1]==1) {
				caseFermee++;
				StdDraw.text(t+i*d+d/2, t+j*d-d/2, this.joueur, 0);
			}
		}
		else if(i==0 && s==1) { // a gauche
			if(coord[i][j][0]==1 
					&& coord[i+1][j][1]==1
					&& coord[i][j+1][0]==1) {
				caseFermee++;
				StdDraw.text(t+i*d+d/2, t+j*d+d/2, this.joueur, 0);
			}
		}
		else if(i==this.taille-1 && s==1) { // a droite
			if(coord[i-1][j][0]==1 
					&& coord[i-1][j][1]==1
					&& coord[i-1][j+1][0]==1) {
				caseFermee++;
				StdDraw.text(t+i*d-d/2, t+j*d+d/2, this.joueur, 0);
			}
		}
		else {
			if(s==0) {
				if(coord[i][j][1]==1 
						&& coord[i+1][j][1]==1
						&& coord[i][j+1][0]==1) {
					caseFermee++;
					StdDraw.text(t+i*d+d/2, t+j*d+d/2, this.joueur, 0);
				}
				if(coord[i][j-1][0]==1 
						&& coord[i][j-1][1]==1
						&& coord[i+1][j-1][1]==1) {
					caseFermee++;
					StdDraw.text(t+i*d+d/2, t+j*d-d/2, this.joueur, 0);
				}
			}
			else {
				if(coord[i][j][0]==1 
						&& coord[i+1][j][1]==1
						&& coord[i][j+1][0]==1) {
					caseFermee++;
					StdDraw.text(t+i*d+d/2, t+j*d+d/2, this.joueur, 0);
				}
				if(coord[i-1][j][0]==1 
						&& coord[i-1][j][1]==1
						&& coord[i-1][j+1][0]==1) {
					caseFermee++;
					StdDraw.text(t+i*d-d/2, t+j*d+d/2, this.joueur, 0);
				}
			}
		}
		
		this.deuxEnUn=caseFermee;
		
		if(caseFermee==0)
			return false;
		else
			return true;
		
	}
	
	// fonction retournant les coord que le bot a choisi on test si le bot peut fermer une case, sinon il retourne des coord choisies au hasard
	public int[] IA() {
		int ligne, colonne, sens; 
		
		do {
			ligne=(int)Math.floor(Math.random()*taille);
			colonne=(int)Math.floor(Math.random()*taille);
			sens=(int)Math.floor(Math.random()*2);
		} while( ((ligne==this.taille-1 && sens==0) || (colonne==this.taille-1 && sens==1)) || this.isCaseFilled(ligne, colonne, sens));
		
		int[] coordIA={ligne, colonne, sens};
		
		for(int i=0;i<this.taille-1;i++) {
			for(int j=0;j<this.taille-1;j++) {
				for(int k=0;k<2;k++) {
					if( !( (i==this.taille-1 && k==0) || (j==this.taille-1 && k==1) || this.isCaseFilled(i,j,k)) ) {
						if(this.testCaseIA(i, j, k)) {	
							coordIA[0]=i;
							coordIA[1]=j;
							coordIA[2]=k;
							return coordIA;
						}
					}
				}
			}
		}
		
		coordIA[0]=ligne;coordIA[1]=colonne;coordIA[2]=sens;
		

		return coordIA;
	}
	
	// renvoie true si le trait de coord ligne, colonne, sens est rempli, false sinon
	public boolean isCaseFilled(int ligne, int colonne, int sens) {
		if(this.coord[ligne][colonne][sens]==1)
			return true;
		else
			return false;
	}
	
	// fonction qui teste si le jeu est fini, et affiche les scores finaux et renvoie false sinon renvoie true
	public boolean end() {
		if(this.score.get(joueurs[0])+this.score.get(joueurs[1])==Math.pow(this.taille-1, 2)) {
			String message;
			
			if(this.score.get(joueurs[0]) > this.score.get(joueurs[1]))
				message= "Le joueur "+joueurs[0]+" a gagné avec un score de "+this.score.get(joueurs[0])+" à "+this.score.get(joueurs[1])+" !";
			else if(this.score.get(joueurs[0]) < this.score.get(joueurs[1]))
				message= "Le joueur "+joueurs[1]+" a gagné avec un score de "+this.score.get(joueurs[1])+" à "+this.score.get(joueurs[0])+" !";
			else
				message= "Vous êtes ex-aequo !";
			
			StdDraw.clear(StdDraw.GRAY);
			StdDraw.setPenColor();
			StdDraw.textLeft(t , l/2, message);
			StdDraw.textRight(l , 0, "Le jeu redémarre...");
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.text(l/2, l-t, "Dots and Boxes");
			StdDraw.show(3000);
			
			this.reset();
			return false;
		}
		return true;
	}

	// fonction qui réiniti
	 public void reset() {
		 StdDraw.clear();
		 
		 
		 score.put(this.joueurs[0], 0);
		 score.put(this.joueurs[1], 0);
		 this.coord= new int[taille][taille][2];
		 this.ajoue=false;
			
		// On affiche la fenetre
		 StdDraw.setXscale(0, l);
		 StdDraw.setYscale(0, l);
		 StdDraw.clear(StdDraw.GRAY);
			
		 StdDraw.setPenColor(StdDraw.WHITE);
		 StdDraw.text(l/2, l-t, "Dots and Boxes");
				// Affichage des lignes blanches
		 StdDraw.setPenRadius();
		 for(int i=0;i<taille;i++) {
			 StdDraw.line(t+i*d, t, t+i*d, t+(taille-1)*d);
			 StdDraw.line(t, t+i*d, t+(taille-1)*d, t+i*d);
		 }
			
			// Affichage des points
		StdDraw.setPenColor();
		int x,y;
		for(int i=0;i<this.taille;i++)  {
			for(int j=0;j<this.taille;j++) {
				x=t+d*i; y=t+d*j;
				StdDraw.filledCircle(x, y, 3);
			}
		}
			
			// On affiche la fenetre que l'on a modifié
		StdDraw.show(20);
		this.jouer();
	 }
}
	




	
