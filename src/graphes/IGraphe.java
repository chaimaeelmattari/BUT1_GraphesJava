package graphes;

import java.util.ArrayList;

public interface IGraphe {
	int INFINI = Integer.MAX_VALUE;

	int getNbSommets();
	void ajouterArc(int source, int valuation, int destination);
	int getValuation(int i, int j);
	boolean aArc(int i, int j);
	default int distance(ArrayList<Integer> cheminCalcul�){
		if (cheminCalcul�.size() == 1)
			return 0;
		int res = getValuation(cheminCalcul�.get(0),cheminCalcul�.get(1));
		for (int i=1; i<cheminCalcul�.size()-1; i++){
			res += getValuation(cheminCalcul�.get(i),cheminCalcul�.get(i+1));
		}
		return res;
	};
}
