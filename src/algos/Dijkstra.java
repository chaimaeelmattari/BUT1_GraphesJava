package algos;

import exceptions.ArcNegatifEx;
import graphes.IGraphe;
import graphes.IPCC;
import exceptions.NoPathEx;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @file Dijkstra.java
 * @brief Classe qui implémente l'algorithme de Dijkstra
 * @author Li Yohan, Ainouche Rayan, El Mattari Chaimae
 * @since 16/05/2022
 * @version 1.0
 */

public class Dijkstra implements IPCC {
    private static final int INFINI = Integer.MAX_VALUE;
    private ArrayList<int[]> sommets;
    private ArrayList<Boolean> sommetsTraites;
    private int courant;

    /**
     * Constructeur de la classe Dijkstra
     */

    public Dijkstra(){
        sommets = new ArrayList<>();
        sommetsTraites = new ArrayList<>();
    }

    /**
     * Initialise l'algorithme de Dijkstra
     * @param g le graphe
     * @param src le sommet de départ
     */

    private void initialiser(IGraphe g, int src){
        sommets.clear();
        sommetsTraites.clear();
        courant = src;
        for(int i=0; i<g.getNbSommets(); i++){
            sommets.add(new int[]{src, INFINI});
            sommetsTraites.add(false);
        }
        sommets.set(src-1,new int[]{src, 0});
        sommetsTraites.set(src-1,true);
    }

    /**
     * Actualise le sommet courant
     * @param t le nombre de sommets du graphe
     */

    private void traiterNoeud(int t){
        int min = INFINI;
        for (int i=0; i<t; i++)
            if (!sommetsTraites.get(i))
                if (sommets.get(i)[1]<min) {
                    min = sommets.get(i)[1];
                    courant = i+1;
                }
        sommetsTraites.set(courant-1,true);
    }

    /**
     * Définit la fin de l'algorithme de Dijkstra
     * @param noeudFin le sommet d'arrivé
     * @return boolean
     */

    private boolean finAlgo(int noeudFin) {
        return sommetsTraites.get(noeudFin-1);
    }

    /**
     * Stock le plus court chemin dans la liste en paramètre
     * @param src le sommet de départ
     * @param fin le sommet d'arrivé
     * @param path la liste qui contiendra le plus court chemin
     */

    private void chemin(int src, int fin, ArrayList<Integer> path){
        int n = fin;
        if(src != fin)
            path.add(fin);
        do {
            path.add(sommets.get(n-1)[0]);
            n = sommets.get(n-1)[0];
        } while (n != src);
        Collections.reverse(path);
    }

    /**
     * Vérifie que le graphe est compatible aves l'algorithme de Dijkstra
     * @param g le graphe
     * @return boolean
     */

    @Override
    public boolean estOk(IGraphe g){
        for(int i=0; i<g.getNbSommets(); i++)
            for (int j=0; j<g.getNbSommets(); j++)
                if(g.aArc(i+1,j+1))
                    if(g.getValuation(i+1,j+1)<0)
                        return false;
        return true;
    }

    /**
     * Calcule la distance du plus court chemin dans un graphe d'un sommet à un autre, en utilisant l'algorithme de Dijkstra
     * @param g le graphe
     * @param src le sommet de départ
     * @param destination le sommet d'arrivé
     * @param cheminCalcule la liste qui va contenir le plus court chemin entre les deux sommets
     * @return int
     * @throws ArcNegatifEx
     * @throws NoPathEx
     */

    @Override
    public int pc(IGraphe g, int src, int destination, ArrayList<Integer> cheminCalcule) throws ArcNegatifEx, NoPathEx{

        if (!estOk(g))
            throw new ArcNegatifEx();

        initialiser(g, src);

        int etapes=1;

        while(!finAlgo(destination) && etapes<g.getNbSommets()){
            for (int i=0; i<g.getNbSommets(); i++){
                if (!sommetsTraites.get(i))
                    if(g.aArc(courant, i+1))
                        if (g.getValuation(courant, i+1) + (sommets.get(courant-1)[1]) < sommets.get(i)[1]){
                            sommets.get(i)[1] = g.getValuation(courant, i+1) + (sommets.get(courant-1)[1]);
                            sommets.get(i)[0] = courant;
                        }
            }
            etapes++;
            traiterNoeud(g.getNbSommets());
        }

        chemin(src, destination, cheminCalcule);

        if (!sommetsTraites.get(destination-1))
            throw new NoPathEx();

        return sommets.get(courant-1)[1];
    }
    
}
