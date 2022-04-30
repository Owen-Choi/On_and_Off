package org.techtown.sns_project.Board.profile.Closet;

import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Closet_Parser {

    FirebaseAuth firebaseAuth;
    String user;
    FirebaseFirestore db;
    String URL;
    int result;

    public Closet_Parser(FirebaseAuth firebaseAuth, String user, FirebaseFirestore db, String URL ) {
        this.firebaseAuth = firebaseAuth;
        this.user = user;
        this.db = db;
        this.URL = URL;
        this.getData();
    }

    private void getData() {
        CodiJsoup jsoupAsyncTask = new CodiJsoup();

        //스레드 실행시켜서 파싱 결과 값 가져오기
        try {
            result=jsoupAsyncTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CodiJsoup extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {

            Document doc = null;
            try {
                doc = Jsoup.connect(URL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }


            final Elements productImg = doc.select("div[class=product-img] img");
            //상품 사진

            final Elements productINFO = doc.select("span[class=product_title]");
            //상품 제품명
            System.out.println("제품 명 : " + productINFO.text());

            final Elements productBrand = doc.select("div[class=explan_product product_info_section] ul p[class=product_article_contents] a");
            //상품 회사 및 태그
            String title = null;
            int count = 0;
            for (Element element : productBrand) {
                if (count == 0) {
                    title = element.text();
                } else if (count > 1) {
                    System.out.println("HashTag : " + element.text());
                }
                count++;
            }

            final Elements productPrice = doc.select("div[class=member_price] ul li");

            String product_price = productPrice.select("span[class=txt_price_member m_list_price]").first().text();
            //상품 가격

            final Elements productCategory = doc.select("div[class=product_info]");
            System.out.println("test :" + productCategory.text());
            String[] sp = productCategory.text().split(" ");
            System.out.println(sp[0]);
            //sp[0]이 카테고리 분류되는 단어임

            //올바른 type의 옷이면 1 아니면 0 반환
            if (sp[0].equals("아우터") || sp[0].equals("상의") || sp[0].equals("바지") || sp[0].equals("신발")) {
                Closet_info CI = new Closet_info(productINFO.text(), title, sp[0], "https:" + productImg.attr("src"), URL);
                db.collection("users").document(user).collection(sp[0]).
                        document(CI.getUrl().replaceAll("[^0-9]", "")).set(CI);
                return 1;
            } else {
                return 0;
            }

        }
    }
}