package algos;

import java.util.ArrayList;
import java.util.HashMap;

import exceptions.CircuitEx;
import graphes.IGraphe;
import exceptions.NoPathEx;
import graphes.IPCC;

/**
 * @file Bellman.java
 * @Brief Classe qui implémente l'algorithme de Bellman
 * @author Li Yohan, Ainouche Rayan, El Mattari Chaimae
 * @version 1.0
 * @since 16/05/2022
 */
public class Bellman implements IPCC {
	/**
	 * Énumération Visite utilisée pour la recherche d'un circuit
	 */
	private enum Visite {PAS_VISITE, VISITE_EN_COURS_, VISITE_TERMINEE};

	private ArrayList<ArrayList<Integer>> successeurs;
	private ArrayList<ArrayList<Integer>> prédécesseurs;
	private ArrayList<ArrayList<Integer>> topologie;
	private int rangSource;
	private int rangDestination;

	/**
	 * Constructeur de la classe Bellman
	 */
	public Bellman() {
		successeurs = new ArrayList<>();
		prédécesseurs = new ArrayList<>();
		topologie = new ArrayList<>();
		rangSource = 0;
		rangDestination = 0;
	}

	/**
	 * Initialise la liste des successeurs de tous les noeuds du graphe
	 * @param graphe
	 */
	private void successeurs(IGraphe graphe) {
		successeurs.clear();
		for (int i = 1; i <= graphe.getNbSommets(); ++i) {
			ArrayList<Integer> liste = new ArrayList<>();
			for (int j = 1; j <= graphe.getNbSommets(); ++j) {
				if(graphe.aArc(i, j)) {
					liste.add(j);
				}
			}
			successeurs.add(liste);
		}
	}

	/**
	 * Initialise la liste des prédécesseurs de tous les noeuds du graphe
	 * @param graphe
	 */
	private void prédécesseurs(IGraphe graphe) {
		prédécesseurs.clear();
		for (int i = 1; i <= graphe.getNbSommets(); ++i) {
			ArrayList<Integer> liste = new ArrayList<>();
			for (int j = 1; j <= graphe.getNbSommets(); ++j){
				if (graphe.aArc(j, i)) {
					liste.add(j);
				}
			}
			prédécesseurs.add(liste);
		}
	}

	/**
	 * Effectue le tri topologique du graphe
	 * @param graphe
	 */
	private void topologie(IGraphe graphe) {
		prédécesseurs(graphe);
		topologie.clear();
		ArrayList<Integer> noeudsRestants = new ArrayList<>();
		for (int i = 1; i <= graphe.getNbSommets(); ++i) {
			noeudsRestants.add(i);
		}
		while(!noeudsRestants.isEmpty()) {
			ArrayList<Integer> noeudsPlacés = new ArrayList<>();
			for (int i : noeudsRestants) {
				if (prédécesseurs.get(i-1).isEmpty()) {
					noeudsPlacés.add(i);
				}
			}
			topologie.add(noeudsPlacés);
			for (ArrayList<Integer> liste : prédécesseurs) {
				for (int i : noeudsPlacés) {
					if (liste.contains(i)) {
						liste.remove((Integer) i);
					}
				}
			}
			for (int i : noeudsPlacés) {
				noeudsRestants.remove((Integer) i);
			}
		}
	}

	/**
	 * Recherche si il y a un circuit dans le graphe (parcours en profondeur)
	 * @param graphe
	 * @return boolean
	 */
	private boolean circuitExistant(IGraphe graphe) {
		ArrayList<Visite> listeNoeuds = new ArrayList<>();

		for (int i = 0; i < graphe.getNbSommets(); ++i) {
			listeNoeuds.add(Visite.PAS_VISITE);
		}

		while(!tousVisités(listeNoeuds)){
			for (int i = 1; i <= graphe.getNbSommets(); ++i) {
				if (visite(listeNoeuds, i)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Vérifie si tous les noeuds du graphe ont été visités
	 * @param liste
	 * @return boolean
	 */
	private boolean tousVisités(ArrayList<Visite> liste) {
		for (Visite v : liste) {
			if (v != Visite.VISITE_TERMINEE) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Visite un noeud et ses successeurs, et retourne true s'il trouve un circuit, sinon false
	 * @param listeNoeuds
	 * @param noeud
	 * @return boolean
	 */
	private boolean visite(ArrayList<Visite> listeNoeuds, int noeud) {
		if (listeNoeuds.get(noeud-1) != Visite.VISITE_TERMINEE) {
			listeNoeuds.set(noeud-1, Visite.VISITE_EN_COURS_);
			if (!successeurs.get(noeud-1).isEmpty()) {
				for (int i : successeurs.get(noeud-1)) {
					if (listeNoeuds.get(i-1) == Visite.VISITE_EN_COURS_) {
						return true;
					}
					else if (listeNoeuds.get(i-1) == Visite.PAS_VISITE){
						if (visite(listeNoeuds, i)) {
							return true;
						}
					}
				}
			}
			listeNoeuds.set(noeud-1, Visite.VISITE_TERMINEE);
		}
		return false;
	}

	/**
	 * Défini le rang de la source et de la destination dans la topologie du graphe
	 * @param source
	 * @param destination
	 */
	private void setRangs(int source, int destination) {
		for (int i = 0; i < topologie.size(); ++i) {
			if (topologie.get(i).contains(source)) {
				rangSource = i;
			}
			if (topologie.get(i).contains(destination)) {
				rangDestination = i;
			}
		}
	}

	/**
	 * Vérifie si le graphe est compatible avec l'algorithme de Bellman
	 * @param graphe
	 * @return boolean
	 */
	@Override
	public boolean estOk(IGraphe graphe) {
		return circuitExistant(graphe);
	}

	/**
	 * Calcule la distance du plus court chemin dans un graphe d'un sommet à un autre, en utilisant l'algorithme de Bellman
	 * @param graphe
	 * @param source
	 * @param destination
	 * @param chemin
	 * @return int
	 * @throws CircuitEx
	 * @throws NoPathEx
	 */
	@Override
	public int pc(IGraphe graphe, int source, int destination, ArrayList<Integer> chemin) throws CircuitEx, NoPathEx {

		int dist = 0;
		successeurs(graphe);

		if (estOk(graphe)) {
			throw new CircuitEx();
		}

		if (source == destination) {
			chemin.add(source);
			return 0;
		}

		topologie(graphe);
		setRangs(source, destination);
		if (rangDestination < rangSource) {
			throw new NoPathEx();
		}
		HashMap<Integer, Integer> distance = new HashMap<>();
		HashMap<Integer, Integer> précédent = new HashMap<>();
		for (int i : successeurs.get(source-1)) {
			distance.put(i, graphe.getValuation(source, i));
			précédent.put(i, source);
		}
		++rangSource;

		while(rangSource < rangDestination) {
			for (int i : topologie.get(rangSource))
				if (distance.containsKey(i)) {
					for (int j : successeurs.get(i-1)) {
						int d = distance.get(i) + graphe.getValuation(i, j);
						if (distance.containsKey(j)){
							if (d < distance.get(j)) {
								distance.put(j, d);
								précédent.put(j, i);
							}
						}
						else {
							distance.put(j, d);;
							précédent.put(j, i);
						}
					}
				}
			++rangSource;
		}

		if (!distance.containsKey(destination)) {
			throw new NoPathEx();
		}

		chemin.add(destination);
		while (!chemin.contains(source)) {
			chemin.add(0, précédent.get(chemin.get(0)));
			dist += graphe.getValuation(chemin.get(0),chemin.get(1));
		}
		return dist;
	}
}
