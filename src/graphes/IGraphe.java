package graphes;

import java.util.ArrayList;

public interface IGraphe {
	int INFINI = Integer.MAX_VALUE;

	int getNbSommets();
	void ajouterArc(int source, int valuation, int destination);
	int getValuation(int i, int j);
	boolean aArc(int i, int j);
	default int distance(ArrayList<Integer> cheminCalculé){
		if (cheminCalculé.size() == 1)
			return 0;
		int res = getValuation(cheminCalculé.get(0),cheminCalculé.get(1));
		for (int i=1; i<cheminCalculé.size()-1; i++){
			res += getValuation(cheminCalculé.get(i),cheminCalculé.get(i+1));
		}
		return res;
	};
}
