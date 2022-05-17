package org.techtown.sns_project.Camera;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import org.techtown.sns_project.fragment.profile.Closet.ClosetMainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

    String THeadS1;
    String THeadS2;
    String[] THeadSA;
    String[] THeadSA1;
    String[] THeadSA2;
    ArrayList<String>[] sizeArr;
    String pattern = "[^a-zA-Z0-9]*$";
    int n=1;
    private TableLayout sizeTable ;
    TableRow[] tablerow;
    Button closetButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codi);


        Intent intent = getIntent();

        String key = intent.getStringExtra("key");
        key = key.replace("/0","");
        key=key.replaceAll("[^0-9]", "");
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
        closetButton = findViewById(R.id.MoveToClosetButton);
        getData();

        Cadapter.setOnItemClickListener (new CodiAdapter.OnItemClickListener() {
            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {
                System.out.println("position"+position);
                System.out.println(listSImgLink.size());
                System.out.println(listImgLink.size());
                StartActivity(listImgLink.get(position));
            }

        });
        Sadapter.setOnItemClickListener (new SimilarAdapter.OnItemClickListener() {
            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {
                System.out.println("position"+position);
                System.out.println(listSImgLink.size());
                System.out.println(listImgLink.size());
                StartCodiActivity(listSImgLink.get(position));
            }

        });

        closetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivityWithClass(ClosetMainActivity.class);
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

                //사이즈 어떤 타입인지
                final Elements THead= doc.select("table[class=table_th_grey] thead");
                //사이즈 어떤 종류인지
                final Elements THead1= doc.select("table[class=table_th_grey] tbody tr th");
                //사이즈 어떤 데이터인지
                final Elements THead2= doc.select("table[class=table_th_grey] tbody tr td");

                Handler handler = new Handler(Looper.getMainLooper()); // 객체생성
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        THeadSA = THead.text().split(" ");

                        THeadS1  = THead1.text().replace("MY ","");
                        THeadSA1 = THeadS1.split(" ");

                        THeadS2 = THead2.text().replace("가지고 계신 제품의 실측을 입력해 보세요~! 위 구매 내역의 사이즈를 저장하시겠습니까? ","");
                        THeadSA2 = THeadS2.split(" ");

                        sizeArr = new ArrayList[THeadSA2.length/(THeadSA.length-1)+1];
                        for (int i = 0; i < THeadSA2.length/(THeadSA.length-1)+1; i++) {
                            sizeArr[i] = new ArrayList<>();
                        }//어레이 리스트 만들기
                        for(int i=0; i<THeadSA.length; i++)
                        {
                            sizeArr[0].add(THeadSA[i]);
                        }//첫줄 데이터 입력


                        for(int i=0; i<THeadSA1.length; i++)
                        {
                            if(THeadSA1[i].matches(pattern))
                            {
                                sizeArr[n-1].set(0,sizeArr[n-1].get(0)+THeadSA1[i]);
                            }
                            else
                            {
                                sizeArr[n].add(THeadSA1[i]);
                                n++;
                            }
                        }

                        for(int i=1; i<THeadSA2.length/(THeadSA.length-1)+1; i++)
                        {
                            for(int j=(THeadSA.length-1)*(i-1); j<(THeadSA.length-1)*i; j++)
                            {
                                sizeArr[i].add(THeadSA2[j]);
                            }
                        }//데이터 입력

                        for(int i=0; i<sizeArr.length; i++)
                        {
                            System.out.println(sizeArr[i]);

                        }

                        sizeTable = (TableLayout)findViewById(R.id.size);
                        tablerow = new TableRow[sizeArr.length];
                        for(int i=0; i<sizeArr.length; i++)
                        {

                            tablerow[i] = new TableRow(getApplicationContext());
                            tablerow[i].setLayoutParams(new TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                        }
                        System.out.println("SA"+sizeArr.length);
                        System.out.println("SA"+THeadSA.length);
                        System.out.println("asdas"+sizeArr.length);
                        System.out.println("asdas"+sizeArr[0].size());
                        TextView textView[][] = new TextView[sizeArr.length][THeadSA.length];
                        for(int j=0; j<sizeArr.length; j++) {
                            for (int i = 0; i < sizeArr[0].size(); i++) {
                                textView[j][i] = new TextView(getApplicationContext());
                                textView[j][i].setText(sizeArr[j].get(i));
                                textView[j][i].setGravity(Gravity.CENTER);
                                textView[j][i].setTextSize(10);
                                textView[j][i].setTextColor(Color.WHITE);
                                tablerow[j].addView(textView[j][i]);
                            }
                            sizeTable.addView(tablerow[j]);
                        }
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
                        Collections.reverse(listTitle);
                        //가수정보
                        for (Element element : Codi_Spec) {
                            listBrand.add(element.text());
                        }
                        Collections.reverse(listBrand);
                        // 이미지정보
                        for (Element element : Codi_Img){
                            listUrl.add("https:"+element.attr("src"));
                        }
                        Collections.reverse(listUrl);
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
                        Collections.reverse(listImgLink);

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

    private void StartActivityWithClass(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void StartCodiActivity(String Key) {
        Intent intent = new Intent(getApplicationContext(),Activity_codi.class);
        String key = Key;
        intent.putExtra("key", key);
        startActivity(intent);
    }

}
