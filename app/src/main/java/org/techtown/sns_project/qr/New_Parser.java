package org.techtown.sns_project.qr;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.techtown.sns_project.R;
import org.techtown.sns_project.cameraexample.Activity_codi;
import org.techtown.sns_project.cameraexample.CodiDTO;

import java.io.IOException;
import java.util.ArrayList;

public class New_Parser {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    String URL;

    public New_Parser(FirebaseAuth firebaseAuth, FirebaseUser user, FirebaseFirestore db, String URL) {
        this.firebaseAuth = firebaseAuth;
        this.user = user;
        this.db = db;
        this.URL = URL;
        this.getData();
    }

    private void getData() {
        CodiJsoup jsoupAsyncTask = new CodiJsoup();
        jsoupAsyncTask.execute();
    }

    private class CodiJsoup extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect(URL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<String> listTitle = new ArrayList<>();
            ArrayList<String> listName = new ArrayList<>();
            ArrayList<String> listUrl = new ArrayList<>();
            ArrayList<String> listPrice = new ArrayList<>();
            ArrayList<String> listUrll = new ArrayList<>();


            final Elements productBrand = doc.select("div[class=explan_product product_info_section] ul p[class=product_article_contents] a");
            //상품 회사 및 태그
            int count = 0;
            final Elements productINFO = doc.select("span[class=product_title]");
            //상품 제품명
            System.out.println("제품 명 : " + productINFO.text());
            for (Element element : productBrand) {
                if (count == 0) {
                    System.out.println("회사 명 : " + element.text());
                } else if (count > 1) {
                    System.out.println("HashTag : " + element.text());
                }
                count++;
            }
            final Elements productPrice = doc.select("div[class=member_price] ul li ");
            Element elem = productPrice.select("span[class=txt_price_member]").first();
            //상품 가격
            System.out.println("Price : " + elem.text());

            final Elements productSimilar = doc.select("div[id=wrap_similar_product] div[class=list-box box list_related_product owl-carousel] ul li");
            //비슷한 상품
            final Elements productSimilarImg = productSimilar.select("div[class=list_img] img");
            //비슷한 상품 이미지
            final Elements productSimilarTitle = productSimilar.select("p[class=item_title]");
            //비슷한 상품 회사명
            final Elements productSimilarInfo = productSimilar.select("p[class=list_info]");
            //비슷한 상품 명
            final Elements productSimilarPrice = productSimilar.select("p[class=price]");
            //비슷한 상품 가격
            final Elements productSimilarUrl = productSimilar.select("div[class=list_img] a");
            //비슷한 상품 링크

            for (Element ele : productSimilarImg) {
                listUrl.add("https:" + ele.attr("src"));
            }
            for (Element ele : productSimilarUrl) {
                listUrll.add("https://store.musinsa.com" + ele.attr("href"));
            }
            for (Element ele : productSimilarTitle) {
                listTitle.add(ele.text());
            }
            for (Element ele : productSimilarInfo) {
                listName.add(ele.text());
            }
            for (Element ele : productSimilarPrice) {
                listPrice.add(ele.text());
            }

            for (int i = 0; i < listName.size(); i++) {
                ProductInfo pi = new ProductInfo(listUrl.get(i), listUrll.get(i), listTitle.get(i)
                        , listName.get(i), listPrice.get(i));
                db.collection("InfoFromURL").document(user.getUid()).set(pi);
            }

            return null;
        }
    }
}