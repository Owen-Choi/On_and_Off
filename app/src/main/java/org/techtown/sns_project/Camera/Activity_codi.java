package org.techtown.sns_project.Camera;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.techtown.sns_project.R;

import java.io.IOException;
import java.util.ArrayList;

public class Activity_codi extends AppCompatActivity {

    RecyclerView recyclerView_Codi, recyclerView_Similar;
    CodiAdapter Cadapter;
    SimilarAdapter Sadapter;
    String Codi_Url ="";
    ImageView txt_ProductImg ;
    TextView txt_ProductBrand, txt_ProductTitle,txt_ProductPrice,txt_ProductTag;
    String TAG="DONG";
    String DEFAULT_URL="https://store.musinsa.com/app/goods/";
    ArrayList<String> listTitle = new ArrayList<>();
    ArrayList<String> listBrand = new ArrayList<>();
    ArrayList<String> listUrl = new ArrayList<>();
    ArrayList<String> listTag = new ArrayList<>();
    ArrayList<String> listImgLink = new ArrayList<>();

    ArrayList<String> listSTitle = new ArrayList<>();
    ArrayList<String> listSBrand = new ArrayList<>();
    ArrayList<String> listSUrl = new ArrayList<>();
    ArrayList<String> listSPrice = new ArrayList<>();
    ArrayList<String> listSImgLink = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi);

        Intent intent = getIntent();

        String key = intent.getStringExtra("key");
        System.out.println("CODI KEY"+key);
        Codi_Url = DEFAULT_URL+key;

        txt_ProductBrand=findViewById(R.id.txt_ProductBrand);
        txt_ProductTitle=findViewById(R.id.txt_ProductTitle);
        txt_ProductPrice=findViewById(R.id.txt_ProductPrice);
        txt_ProductTag=findViewById(R.id.txt_ProductTag);

        recyclerView_Codi = findViewById(R.id.recyclerView_Codi);
        recyclerView_Similar=findViewById(R.id.recyclerView_Similar);

        LinearLayoutManager linearLayoutManager_Codi = new LinearLayoutManager(this,RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager_Similar = new LinearLayoutManager(this,RecyclerView.HORIZONTAL, false);
        recyclerView_Codi.setLayoutManager(linearLayoutManager_Codi);
        recyclerView_Similar.setLayoutManager(linearLayoutManager_Similar);
        Cadapter = new CodiAdapter();
        Sadapter = new SimilarAdapter();
        recyclerView_Codi.setAdapter(Cadapter);
        recyclerView_Similar.setAdapter(Sadapter);
//
        getData();

        Sadapter.setOnItemClickListener (new SimilarAdapter.OnItemClickListener() {
            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {
                System.out.println("position"+position);
                System.out.println(listSImgLink.size());
                System.out.println(listImgLink.size());
               StartActivity(listSImgLink.get(position));
            }

        });
    }

    private void getData(){
        CodiJsoup jsoupAsyncTask = new CodiJsoup();
        jsoupAsyncTask.execute();
    }

    private class CodiJsoup extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Document doc = Jsoup.connect(Codi_Url).get();

                final Elements Codi_Img = doc.select("div[class=right_contents related-styling]  ul[class=style_list] li[class=list_item] img");
                final Elements Codi_title = doc.select("div[class=right_contents related-styling]  ul[class=style_list] li[class=list_item] h5");
                final Elements Codi_Spec = doc.select("div[class=right_contents related-styling]  ul[class=style_list] li[class=list_item] p");
                final Elements Codi_Url = doc.select("div[class=right_contents related-styling]  ul[class=style_list] li[class=list_item] a");

                final Elements product_INFO= doc.select("span[class=product_title]");//제품명
                final Elements product_Brand= doc.select("div[class=explan_product product_info_section] ul p[class=product_article_contents] a");
                //회사명 및 해시태그
                final Elements product_Price= doc.select("div[class=member_price] ul li ");
                Element price = product_Price.select("span[class=txt_price_member]").first();
                //가격
                final Elements product_Similar= doc.select("div[id=wrap_similar_product] div[class=list-box box list_related_product owl-carousel] ul li");
                final Elements Similar_Img = product_Similar.select("div[class=list_img] img");
                final Elements Similar_Title= product_Similar.select("p[class=item_title]");
                final Elements Similar_Brand= product_Similar.select("p[class=list_info]");
                final Elements Similar_Price= product_Similar.select("p[class=price]");
                final Elements Similar_Url= product_Similar.select("div[class=list_img] a");

                Handler handler = new Handler(Looper.getMainLooper()); // 객체생성
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final Elements productImg = doc.select("div[class=product-img] img"); //제품사진
                        txt_ProductImg=  findViewById(R.id.txt_ProductImg);
                        Glide.with(txt_ProductImg).load("https:"+productImg.attr("src")).error(R.drawable.ic_launcher_background).into(txt_ProductImg);
                        txt_ProductTitle.setText(product_INFO.text());
                        txt_ProductPrice.setText(price.text());

                        int count=0;
                        for (Element element : product_Brand){
                            if(count==0){
                                txt_ProductBrand.setText(element.text());}
                            else if(count>1)
                            {
                                listTag.add(element.text());
                            }
                            count++;
                        }
                        StringBuffer Hashtag = new StringBuffer();
                        for(int i=0; i<listTag.size(); i++)
                        Hashtag.append(listTag.get(i));
                        txt_ProductTag.setText(Hashtag);

                        for(Element element: Codi_title) {
                            listTitle.add(element.text());
                        }
                        //가수정보
                        for (Element element : Codi_Spec) {
                            listBrand.add(element.text());
                        }
                        // 이미지정보
                        for (Element element : Codi_Img){
                            listUrl.add("https:"+element.attr("src"));
                        }
                        for (Element element : Codi_Url){
                            System.out.println("CODI IMG URL:"+element.attr("href"));
                            if(element.attr("href").contains("https://www.musinsa.com"))
                            {
                                System.out.println("CONTAIN : "+ element.attr("href"));
                                listImgLink.add(element.attr("href"));
                            }
                            else
                            {
                                System.out.println("NONCONTAIN : "+ element.attr("href"));
                                listImgLink.add("https://www.musinsa.com"+element.attr("href"));
                            }
                        }
                        for (Element element : Similar_Url){
                            System.out.println("S IMG URL:"+element.attr("href"));
                            if(element.attr("href").contains("https://www.musinsa.com"))
                            {
                                System.out.println("CONTAIN : "+ element.attr("href"));
                                listSImgLink.add(element.attr("href"));
                            }
                            else
                            {
                                listSImgLink.add("https://www.musinsa.com"+element.attr("href"));
                                System.out.println("NONCONTAIN : "+ "https://www.musinsa.com"+element.attr("href"));
                            }
                        }
                        System.out.println("SIZE : "+ listSImgLink);
                        for (int i = 0; i < listTitle.size() ; i++) {
                            CodiDTO data = new CodiDTO();

                            data.setTitle(listTitle.get(i));
                            data.setImageUrl(listUrl.get(i));
                            data.setBrand(listBrand.get(i));

                            Cadapter.addItem(data);
                        }

                     //비슷한 상품 출력
                        for(Element ele : Similar_Img)
                        {
                            listSUrl.add("https:"+ele.attr("src"));
                        }
                        for(Element ele : Similar_Brand)
                        {
                            listSBrand.add(ele.text());
                            listSBrand.add(ele.text());
                        }
                        for(Element ele : Similar_Title)
                        {
                            listSTitle.add(ele.text());
                        }
                        for(Element ele : Similar_Price) {
                            listSPrice.add(ele.text());
                        }


                        for(int i=0; i<listSTitle.size(); i++)
                        {
                            CodiDTO data = new CodiDTO();
                            data.setImageUrl(listSUrl.get(i));
                            data.setBrand(listSBrand.get(i));
                            data.setTitle(listSTitle.get(i));
                            data.setPrice(listSPrice.get(i));

                            Sadapter.addItem(data);

                        }
                        Cadapter.notifyDataSetChanged();
                        Sadapter.notifyDataSetChanged();
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    private void StartActivity(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(key));
        startActivity(intent);
    }

}
