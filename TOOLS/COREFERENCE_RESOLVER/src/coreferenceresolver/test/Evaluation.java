package coreferenceresolver.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.Combinations;

import coreferenceresolver.element.CorefChain;
import coreferenceresolver.element.Review;

public class Evaluation {
    //find the cardinality of a corefChain

	private static int kFold = CrossValidation.getkFold();
	private static float sumRecallMUC = 0;
	private static float sumPrecisionMUC = 0;
	private static float sumF1MUC = 0;
	private static float sumRecallB3 = 0;
	private static float sumPrecisionB3 = 0;
	private static float sumF1B3 = 0;

    public static Integer findSize(CorefChain corefChain) {
        return corefChain.getChain().size();
    }

    public static Integer findNumPartitions(CorefChain corefChain, List<CorefChain> listCorefChains) {
        List<Integer> listIdCluster = new ArrayList<Integer>();
        int idCluster = listCorefChains.size();
        for (Integer in : corefChain.getChain()) {
            boolean check = false;
            for (int i = 0; i < listCorefChains.size(); i++) {
                if (listCorefChains.get(i).getChain().contains(in)) {
                    check = true;
                    if (!listIdCluster.contains(i)) {
                        listIdCluster.add(i);
                    }
                }
            }
            if (check == false) {
                listIdCluster.add(idCluster);
                idCluster++;
            }
        }
        return listIdCluster.size();
    }

    public static void scoreMUC(List<Review> listReview) {        
        //for counting the Recall
        int SumNumeratorRecall = 0;
        int SumDenominatorRecall = 0;
               
//        for (Review re : listReview) {            
//            Iterator<CorefChain> itr = re.getCorefChainsPredicted().iterator();
//            while (itr.hasNext()) {
//                CorefChain curCc = itr.next();
//                boolean isSatisfied = false;
//                for (int npId : curCc.getChain()) {
//                    int curNpType = re.getNounPhrases().get(npId).getType();
//                    if (curNpType == 0 || curNpType == 3) {
//                        isSatisfied = true;
//                        break;
//                    }
//                }                
//                if (!isSatisfied) {
//                    itr.remove();
//                }
//            }
//        }

        for (Review re : listReview) {
            for (CorefChain cf : re.getCorefChainsActual()) {
                SumNumeratorRecall = SumNumeratorRecall + findSize(cf) - findNumPartitions(cf, re.getCorefChainsPredicted());
                SumDenominatorRecall = SumDenominatorRecall + findSize(cf) - 1;
            }
        }
        float recall = ((float) SumNumeratorRecall) / SumDenominatorRecall;
        
        //for counting the Precision
        int SumNumeratorPrecision = 0;
        int SumDenominatorPrecision = 0;

        for (Review re : listReview) {
            for (CorefChain cf : re.getCorefChainsPredicted()) {
                SumNumeratorPrecision = SumNumeratorPrecision + findSize(cf) - findNumPartitions(cf, re.getCorefChainsActual());
                SumDenominatorPrecision = SumDenominatorPrecision + findSize(cf) - 1;
            }
        }
        float precision = ((float) SumNumeratorPrecision) / SumDenominatorPrecision;
        float f1 = findF1(recall, precision);

        System.out.println("-----MUC score-----");
        System.out.println("Recall: " + recall);
        System.out.println("Precision: " + precision);
        System.out.println("F1 measure: " + f1);
        sumRecallMUC +=recall;
        sumPrecisionMUC += precision;
        sumF1MUC += f1; 
    }

    public static float findF1(float r, float p) {
        return 2 * p * r / (p + r);
    }

    /*----------------B3 score---------------------*/
    public static int numOfEntityIntersecting(List<Integer> l1, List<Integer> l2) {
        int no = 0;
        for (Integer in : l1) {
            if (l2.contains(in)) {
                no++;
            }
        }
        return no;
    }

    public static void scoreB3(List<Review> listReview) {
        int numOfEntityAllReview = 0;
        int numOfEntityAllReviewPrecision = 0;
        float recall = 0;
        float precision = 0;

        for (Review re : listReview) {
            for (CorefChain cf : re.getCorefChainsActual()) {
                for (Integer in : cf.getChain()) {
                    numOfEntityAllReview++;
                    numOfEntityAllReviewPrecision++;
                    int numOfEntityIntersecting = 0;
                    int numOfEntityChainReal = 0;
                    int numOfEntityChainPredict = 0;
                    boolean check = true;
                    for (CorefChain cf1 : re.getCorefChainsPredicted()) {
                        if (cf1.getChain().contains(in)) {
                            check = false;
                            numOfEntityIntersecting = numOfEntityIntersecting(cf1.getChain(), cf.getChain());
                            numOfEntityChainPredict = cf1.getChain().size();
                            numOfEntityChainReal = cf.getChain().size();

                            recall = recall + ((float) numOfEntityIntersecting / numOfEntityChainReal);
                            precision = precision + ((float) numOfEntityIntersecting / numOfEntityChainPredict);
                            break;
                        }
                    }
                    if (check == true) {
                        numOfEntityAllReviewPrecision--;
                        numOfEntityIntersecting = 1;
                        numOfEntityChainPredict = 1;
                        numOfEntityChainReal = cf.getChain().size();

                        recall = recall + ((float) numOfEntityIntersecting / numOfEntityChainReal);
//						precision = precision + ((float) numOfEntityIntersecting/numOfEntityChainPredict);
                    }
                }
            }
        }

        recall = recall / numOfEntityAllReview;
        precision = precision / numOfEntityAllReviewPrecision;

        float f1 = findF1(recall, precision);

        System.out.println("-----B3 score-----");
        System.out.println("Recall: " + recall);
        System.out.println("Precision: " + precision);
        System.out.println("F1 measure: " + f1);
        sumRecallB3 += recall;
        sumPrecisionB3 += precision;
        sumF1B3 += f1;
    }

    /*--------------CEAF phi4----------------*/
    public static float findPhi4(CorefChain cf1, CorefChain cf2) {
        return 2 * ((float) numOfEntityIntersecting(cf1.getChain(), cf2.getChain()) / (findSize(cf1) + findSize(cf2)));
    }

    public static void addElementtoList(List<Integer> list, int el) {
        if (!list.contains(el)) {
            list.add(el);
        }
    }

    public static void scoreCEAF4(List<Review> reviews) {
        float recall = 0;
        float precision = 0;
        int number = 0;
        for (int idreview = 0; idreview < reviews.size(); idreview++) {
            int min = 0;
//		System.out.print("-----------Review " + idreview);
            if (reviews.get(idreview).getCorefChainsActual().size() > reviews.get(idreview).getCorefChainsPredicted().size()) {
                min = reviews.get(idreview).getCorefChainsPredicted().size();
            } else {
                min = reviews.get(idreview).getCorefChainsActual().size();
            }

            List<CorefChain> listEntityReal = reviews.get(idreview).getCorefChainsActual();
            List<CorefChain> listEntityPredict = reviews.get(idreview).getCorefChainsPredicted();
            List<Float> listValueOf2Mentions = new ArrayList<>();
            ArrayList<ArrayList<Integer>> listPairMentions = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i < listEntityReal.size(); i++) {
                for (int j = 0; j < listEntityPredict.size(); j++) {
                    listValueOf2Mentions.add(findPhi4(listEntityReal.get(i), listEntityPredict.get(j)));
                    ArrayList<Integer> idOf2Mentions = new ArrayList<>();
                    idOf2Mentions.add(i);
                    idOf2Mentions.add(j + listEntityReal.size());
                    listPairMentions.add(idOf2Mentions);
                }
            }
//		System.out.println("Review: 27");
//		for (CorefChain cf : reviews.get(idreview).getCorefChainsActual())
//			System.out.println("Actual Chain: " + cf.getChain().toString());
//		for (CorefChain cf : reviews.get(idreview).getCorefChainsPredict())
//			System.out.println("Predict Chain: " + cf.getChain().toString());
//		for (ArrayList<Integer> ls : listPairMentions){
//			System.out.println("Pair " + ls.get(0) + "+" + ls.get(1));
//			System.out.println("Value: " + listValueOf2Mentions);
//		}
            List<Float> ListValueOfEachSubsets = new ArrayList<>();
            ArrayList<ArrayList<Integer>> listAllIdEachSubsets = new ArrayList<ArrayList<Integer>>();
//		int [] indexs = new int[listPairMentions.size()];
//		for (int i = 0; i < listPairMentions.size(); i++)
//			indexs[i] = i;
            int count = 0;

            List<Integer> listEntityTheBestSimilarity = new ArrayList<>();
            float maxValue = 0;
            for (Iterator<int[]> iter = new Combinations(listPairMentions.size(), min).iterator(); iter.hasNext();) {
                int[] next = iter.next();
//			System.out.println(Arrays.toString(next) + next.length);
                float sumValue = 0;
                ArrayList<Integer> idOfAllMentionsInSubset = new ArrayList<Integer>();
                for (int j = 0; j < min; j++) {
                    sumValue = sumValue + listValueOf2Mentions.get(next[j]);
                    addElementtoList(idOfAllMentionsInSubset, listPairMentions.get(next[j]).get(0));
                    addElementtoList(idOfAllMentionsInSubset, listPairMentions.get(next[j]).get(1));
                }
//			System.out.println("SumValue: " + sumValue);
//			for (Integer in : idOfAllMentionsInSubset)
//				System.out.println("Id Chain in Subset" + in);
                ListValueOfEachSubsets.add(sumValue);
                listAllIdEachSubsets.add(idOfAllMentionsInSubset);
                count++;

                if (sumValue > maxValue && idOfAllMentionsInSubset.size() == (2 * min)) {
                    maxValue = sumValue;
                    listEntityTheBestSimilarity.clear();
//				System.out.print("MaxValue: " + SumNumerator);
                    for (Integer in : idOfAllMentionsInSubset) {
//					System.out.println("Id All Chains similarity" + in);
                        listEntityTheBestSimilarity.add(in);
                    }
                }
            }

            float SumNumerator = 0;
            float SumDenominatorPrecision = 0;
            float SumDenominatorRecall = 0;

            SumNumerator = SumNumerator + maxValue;
//		for (int k = 0; k < listEntityTheBestSimilarity.size(); k++){
//			if (listEntityTheBestSimilarity.get(k) < listEntityReal.size()){
//				SumDenominatorRecall = SumDenominatorRecall + findPhi4(listEntityReal.get(listEntityTheBestSimilarity.get(k)), listEntityReal.get(listEntityTheBestSimilarity.get(k)));
//				System.out.println("id recall:" + findPhi4(listEntityReal.get(listEntityTheBestSimilarity.get(k)), listEntityReal.get(listEntityTheBestSimilarity.get(k))));
//			}
//			else {
//				SumDenominatorPrecision = SumDenominatorPrecision + findPhi4(listEntityPredict.get(listEntityTheBestSimilarity.get(k) - listEntityReal.size()), listEntityPredict.get(listEntityTheBestSimilarity.get(k) - listEntityReal.size()));
//				System.out.println("id precision:" + findPhi4(listEntityPredict.get(listEntityTheBestSimilarity.get(k) - listEntityReal.size()), listEntityPredict.get(listEntityTheBestSimilarity.get(k) - listEntityReal.size())));
//			}
//		}

            SumDenominatorRecall = listEntityReal.size();
            SumDenominatorPrecision = listEntityPredict.size();
            recall = recall + SumNumerator / SumDenominatorRecall;
            if (SumDenominatorPrecision == 0) {
                number++;
            } else {
                precision = precision + SumNumerator / SumDenominatorPrecision;
            }
        }
        recall = recall / reviews.size();
        precision = precision / (reviews.size() - number);
        float f1 = findF1(recall, precision);
        System.out.println("-----CEAF 4 score-----");
        System.out.println("Recall: " + recall);
        System.out.println("Precision: " + precision);
        System.out.println("F1 measure: " + f1);
    }
    
    public static void resultFinal(){
    	float recallMUC = sumRecallMUC/kFold;
    	float precisionMUC = sumPrecisionMUC/kFold;
    	float f1MUC = sumF1MUC/kFold;
    	float recallB3 = sumRecallB3/kFold;
    	float precisionB3 = sumPrecisionB3/kFold;
    	float f1B3 = sumF1B3/kFold;
    	System.out.println("----------Result Final---------");
    	System.out.println("-------MUC score-----");
        System.out.println("Recall: " + recallMUC);
        System.out.println("Precision: " + precisionMUC);
        System.out.println("F1 measure: " + f1MUC);
        System.out.println("-------B3 score-----");
        System.out.println("Recall: " + recallB3);
        System.out.println("Precision: " + precisionB3);
        System.out.println("F1 measure: " + f1B3);
    }
}
