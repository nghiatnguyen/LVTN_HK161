/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreferenceresolver.test;

import coreferenceresolver.element.NounPhrase;
import coreferenceresolver.element.Review;
import coreferenceresolver.gui.ClassifiedResultGUI;
import coreferenceresolver.process.TrainingMain;
import coreferenceresolver.util.StanfordUtil;
import static coreferenceresolver.util.StanfordUtil.pipeline;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author TRONGNGHIA
 */
public class Test {

    public static void main(String... args) throws IOException, Exception {
//        StanfordUtil.reviews = new ArrayList<>();
//        for (int i = 0; i < 50; ++i) {
//            Review review = new Review();
//            StanfordUtil.reviews.add(review);
//        }
//        Weka.j48Classify(".\\test.arff", ".\\classified.txt");
//        int reviewNo = 0;
//        for (Review review : StanfordUtil.reviews) {
//            if (review.getCorefChains().size() > 0) {
//                System.out.println("--REVIEW " + reviewNo + "--");
//            }
//            for (CorefChain cc : review.getCorefChains()) {
//                System.out.println("New chain: ");
//                for (int coref : cc.getChain()) {
//                    System.out.print(coref + " ");
//                }
//                System.out.println();
//            }
//            ++reviewNo;
//        }        

//        NounPhrase np1 = new NounPhrase();
//        np1.setOffsetBegin(0);
//        np1.setOffsetEnd(4);
//        NounPhrase np2 = new NounPhrase();
//        np2.setOffsetBegin(10);
//        np2.setOffsetEnd(15);
//        NounPhrase np3 = new NounPhrase();
//        np3.setOffsetBegin(16);
//        np3.setOffsetEnd(20);
//        NounPhrase np4 = new NounPhrase();
//        np4.setOffsetBegin(30);
//        np4.setOffsetEnd(40);
//        Review review1 = new Review();
//        review1.setRawContent("Chuyện điều động giáo viên đi tiếp khách ở Hà Tĩnh khiến dư luận chú ý suốt hơn một tuần qua và được đem ra chất vấn ở cả nghị trường Quốc hội.\n" +
//"Tôi thấy dường như có ba luồng quan điểm chính. Nhiều người bất bình khi các cô giáo bị điều động thực hiện một nhiệm vụ không phù hợp, ngoài chuyên môn. Một số coi đây là việc bình thường, không cần làm lớn chuyện. Còn một nhóm khác mà ý kiến của họ khiến tôi lưu tâm hơn cả: họ hiểu đây là chuyện không hay nhưng không thể làm khác bởi tiệc tùng đã trở thành một phần công việc.");
//        review1.addNounPhrase(np1);
//        review1.addNounPhrase(np2);
//        review1.addNounPhrase(np3);
//        review1.addNounPhrase(np4);
//        review1.addCorefChain(0, 1);
//        review1.addCorefChain(2, 3);
//        
//        Review review2 = new Review();
//        review2.setRawContent("Người phương Tây, nhất là thanh niên cũng hút thuốc và uống rượu khá nhiều. Nhưng với họ uống là uống, làm là làm. Rượu không phải là điều kiện giao tiếp và bàn nhậu không phải là nơi thể hiện bản lĩnh hay mặc cả công việc. Tôi thích những bữa tiệc nhẹ để chúc mừng thành công của hội thảo hay dự án của họ. Họ có uống, song chỉ là một ly nhẹ nhàng và ai không uống được thì cũng không bị ép. Đó là cách dùng rượu để nâng người ta lên chứ không hạ người ta xuống.\n" +
//"\n" +
//"Tôi tin rằng rượu tồn tại như một thứ tất yếu, không phải thế thì làm sao rượu có mặt ở mọi nơi trên thế giới với đủ loại, đủ hương vị. Chưa có một đạo luật nào từng cấm rượu thành công. Nhưng có một khoảng cách xa giữa những người biết thưởng thức và những người nghiện; giữa những người biết dùng rượu để nâng cao phẩm giá của mình và những người đánh mất phẩm giá của mình vì nó; giữa những người có tất cả sự thông tuệ sau chén rượu và những người dùng chén rượu để che đi sự rỗng tuếch.");
//        review2.addNounPhrase(np1);
//        review2.addNounPhrase(np2);
//        review2.addNounPhrase(np3);
//        review2.addCorefChain(0, 1);
//        review2.addCorefChain(1, 2);
//        
//        StanfordUtil.reviews = new ArrayList<>();
//        StanfordUtil.reviews.add(review1);
//        StanfordUtil.reviews.add(review2);
//        
//        ClassifiedResultGUI.main(args);

        String inputFilePath = "E:\\REPOSITORIES\\LVTN_HK161\\TOOLS\\COREFERENCE_RESOLVER\\input.txt";
        String markupFilePath = "F:\\markup.out.txt";
        String outFilePath = "F:\\test.arff";
        TrainingMain.run(inputFilePath, markupFilePath, outFilePath, true);
    }
}
