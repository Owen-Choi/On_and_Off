package org.techtown.sns_project.Board.Upload;

import android.os.AsyncTask;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.techtown.sns_project.qr.ProductInfo;

import java.io.IOException;

public class upload_parser_class {
    String URL;
    UploadActivity uploadActivity;
    public upload_parser_class( String URL, UploadActivity uploadActivity) {
        this.URL = URL;
        this.getData();
        this.uploadActivity = uploadActivity;
    }

    private void getData() {
        ItemJSoup jsoupAsyncTask = new ItemJSoup();
        jsoupAsyncTask.execute();
    }

    private class ItemJSoup extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect(URL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final Elements productImg = doc.select("div[class=product-img] img");
            //상품 사진

            final Elements productINFO= doc.select("span[class=product_title]");
            //상품 제품명
            System.out.println("제품 명 : " +productINFO.text());

            final Elements productBrand= doc.select("div[class=explan_product product_info_section] ul p[class=product_article_contents] a");
            //상품 회사 및 태그
            String title = null;
            int count=0;
            for (Element element : productBrand){
                if(count==0){
                    title =  element.text();}
                else if(count>1)
                {
                    System.out.println("HashTag : "+element.text());
                }
                count++;
            }

            final Elements productPrice= doc.select("div[class=member_price] ul li");

            String product_price = productPrice.select("span[class=txt_price_member m_list_price]").first().text();
            //상품 가격


            // pi에 parsing한 정보가 모두 들어가있다. 이 정보를 adapter로 넘기자.
            ProductInfo pi = new ProductInfo(URL, "https:"+productImg.attr("src"), title
                    , productINFO.text(), product_price);
            make_injection(pi);
            return null;
        }

    }


    // 여기서 문제가 많이 생긴다. 외부에서 수정을 못하는 쓰레드 오류가 주로 발생함.
    private void make_injection(ProductInfo pi) {
        uploadActivity.parsing_injection(pi);
    }

}
