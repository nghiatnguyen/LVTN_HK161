package coreferenceresolver.test;

import java.util.ArrayList;
import java.util.List;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.Review;

public class Evaluation {
	//find the cardinality of a corefChain
	public static Integer findSize(CorefChain corefChain){
		return corefChain.getChain().size();
	}
	
	public static Integer findNumPartitions(CorefChain corefChain, List<CorefChain> listCorefChains){
		List<Integer> listIdCluster = new ArrayList<Integer>();
		int idCluster = listCorefChains.size();
		for (Integer in : corefChain.getChain()){
			boolean check = false;
			for (int i = 0; i < listCorefChains.size(); i++)
					if (listCorefChains.get(i).getChain().contains(in)){
						check = true;
						if(!listIdCluster.contains(i))
							listIdCluster.add(i);
					}
			if (check == false){
					listIdCluster.add(idCluster);
					idCluster++;
			}		
		}
		return listIdCluster.size();
	}
	public static void scoreMUC(List<Review> listReview){
		//for counting the Recall
		int SumNumeratorRecall = 0; 
		int SumDenominatorRecall = 0;
		
		for (Review re : listReview)
			for (CorefChain cf : re.getCorefChainsActual()){
				SumNumeratorRecall = SumNumeratorRecall + findSize(cf) - findNumPartitions(cf, re.getCorefChainsPredict());
				SumDenominatorRecall = SumDenominatorRecall + findSize(cf) - 1;
			}
		Float recall = ((float)SumNumeratorRecall)/SumDenominatorRecall;
		
		//for counting the Precision
		int SumNumeratorPrecision = 0; 
		int SumDenominatorPrecision = 0;
		
		for (Review re : listReview)
			for (CorefChain cf : re.getCorefChainsPredict()){
				SumNumeratorPrecision = SumNumeratorPrecision + findSize(cf) - findNumPartitions(cf, re.getCorefChainsActual());
				SumDenominatorPrecision = SumDenominatorPrecision + findSize(cf) - 1;
			}
		Float precision = ((float)SumNumeratorRecall)/SumDenominatorRecall;
		
		System.out.println("Recall: " + recall);
		System.out.println("Precision: " + precision);
	}
}
