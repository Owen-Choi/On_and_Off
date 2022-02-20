package org.techtown.sns_project.qr;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.String;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    String URL;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Parser(String URL) throws IOException {
        this.URL = URL;
    }

    public void StoreInfo() throws IOException {
        new Thread(() -> {
            String TAG = "Temp";
            Document doc = null;
            try {
                doc = Jsoup.connect(URL).userAgent("Mozilla/5.0").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<String> listTitle = new ArrayList<>();
            ArrayList<String> listName = new ArrayList<>();
            ArrayList<String> listUrl = new ArrayList<>();
            ArrayList<String> listPrice = new ArrayList<>();
            ArrayList<String> listUrll = new ArrayList<>();
            ArrayList<String> listSBrand = new ArrayList<>();
            ArrayList<String> listSTitle = new ArrayList<>();
            final Elements imgElem = doc.select("div[class=right_contents related-styling]  ul[class=style_list] li[class=list_item] img");
            //상품 코디 이미지
            final Elements Codi_title = doc.select("div[class=right_contents related-styling]  ul[class=style_list] li[class=list_item] h5");
            //상품 코디 제목
            final Elements Codi_Spec = doc.select("div[class=right_contents related-styling]  ul[class=style_list] li[class=list_item] p");
            //상품 코디 부가 설명
            //Elements elem = doc.select("td.pl");

            System.out.println("사진 리스트 "+imgElem.size());
            for (Element element : imgElem){
                System.out.println("https:"+element.attr("src"));
            }
            for(Element element: Codi_title) {
                System.out.println("title   "+element.text());
            }
            for (Element element : Codi_Spec) {
                System.out.println("SPEc    "+element.text());
            }
            final Elements productImg = doc.select("div[class=product-img] img");
            //상품 사진
            System.out.println("제품 사진");
            System.out.println("https:"+productImg.attr("src"));

            final Elements productBrand= doc.select("div[class=explan_product product_info_section] ul p[class=product_article_contents] a");
            //상품 회사 및 태그
            int count=0;
            final Elements productINFO= doc.select("span[class=product_title]");
            //상품 제품명
            System.out.println("제품 명 : " +productINFO.text());
            for (Element element : productBrand){
                if(count==0){
                    System.out.println("회사 명 : " + element.text());}
                else if(count>1)
                {
                    System.out.println("HashTag : "+element.text());
                }
                count++;
            }
            final Elements productPrice= doc.select("div[class=member_price] ul li ");
            Element elem = productPrice.select("span[class=txt_price_member]").first();
            //상품 가격
            System.out.println("Price : "+elem.text());

            final Elements productSimilar= doc.select("div[id=wrap_similar_product] div[class=list-box box list_related_product owl-carousel] ul li");
            //비슷한 상품
            final Elements productSimilarImg = productSimilar.select("div[class=list_img] img");
            //비슷한 상품 이미지
            final Elements productSimilarTitle= productSimilar.select("p[class=item_title]");
            //비슷한 상품 회사명
            final Elements productSimilarInfo= productSimilar.select("p[class=list_info]");
            //비슷한 상품 명
            final Elements productSimilarPrice= productSimilar.select("p[class=price]");
            //비슷한 상품 가격
            final Elements productSimilarUrl= productSimilar.select("div[class=list_img] a");
            //비슷한 상품 링크

            for(Element ele : productSimilarImg)
            {
                listUrl.add("https:"+ele.attr("src"));
            }
            for(Element ele : productSimilarUrl)
            {
                listUrll.add("https://store.musinsa.com"+ele.attr("href"));
            }
            for(Element ele : productSimilarTitle)
            {
                listTitle.add(ele.text());
            }
            for(Element ele : productSimilarInfo) {
                listName.add(ele.text());
            }
            for(Element ele : productSimilarPrice)
            {
                listPrice.add(ele.text());
            }

//        for(int i=0; i<listName.size(); i++)
//        {
//            System.out.println("URL : "+listUrl.get(i));
//            System.out.println("SIMILAR URL : "+listUrll.get(i));
//            System.out.println("TITLE : "+listTitle.get(i));
//            System.out.println("INFO : "+listName.get(i));
//            System.out.println("PRICE : "+listPrice.get(i));
//            System.out.println();
//        }
            for(int i=0; i<listName.size(); i++) {
                ProductInfo pi = new ProductInfo(listUrl.get(i), listUrll.get(i), listTitle.get(i)
                        , listName.get(i), listPrice.get(i));
                db.collection("InfoFromURL").document(user.getUid()).set(pi);
            }
        });
    }
}