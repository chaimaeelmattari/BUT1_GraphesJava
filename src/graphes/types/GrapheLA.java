package graphes.types;

import java.util.ArrayList;
import java.util.List;

public class GrapheLA extends Graphe{
	private static class Stub {
		public int valuation, cible;
		public Stub (int valuation, int cible) {
			this.valuation = valuation;
			this.cible = cible;
		}
	}
	private	List<Stub> la[];
	
	@SuppressWarnings("unchecked")
	public GrapheLA(int nbNoeuds) {
		super();
		la = new List[nbNoeuds]; // necessite le SuppressWarnings ci-dessus
		for (int i = 0; i < nbNoeuds; ++i)
			la[i] = new ArrayList<>();
	}

	@Override
	public int getNbSommets() {
		return la.length;
	}

	@Override
	public int getValuation(int a, int b) {
		assert estArcOK(a,b);
		List<Stub> stubs = la[a-1];
		for (Stub s : stubs)
			if (s.cible == b)
				return s.valuation;
		return INFINI;
	}

	public void ajouterArc(int a, int v, int b) {
		assert ! aArc(a,b);
		la[a-1].add(new Stub(v, b));
	}

	

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i< la.length; ++i) {
			str.append(i + 1).append(" =>");
			for (Stub s : la[i]) 
				str.append(" ").append(s.cible).append("(").append(s.valuation).append(")");
			str.append("\n");
		}
		return str.toString();
	}
}
