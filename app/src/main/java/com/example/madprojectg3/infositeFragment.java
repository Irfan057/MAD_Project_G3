package com.example.madprojectg3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link infositeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class infositeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Article> ArticleList = new ArrayList<Article>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rAdapter;
    Context context;
    private SearchView searchView;
    private RecyclerView.LayoutManager layoutManager;
    public infositeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment infositeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static infositeFragment newInstance(String param1, String param2) {
        infositeFragment fragment = new infositeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_infosite, container, false);
    }

    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        fillArticleList();
        context = view.getContext();
        recyclerView = view.findViewById(R.id.RVArticle);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(layoutManager);
        rAdapter = new RecycleViewAdapter(ArticleList,context);
        recyclerView.setAdapter(rAdapter);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, (RecycleViewAdapter) rAdapter);
                return true;
            }
        });
    }
    private void fillArticleList() {
        Article at1 = new Article(1, "Malay Mail:\nBarnacle blues: Spain’s Christmas delicacy at risk as climate change and poaching take a toll",
                "https://cdn4.premiumread.com/?url=https://malaymail.com/malaymail/uploads/images/2024/12/24/251723.JPG&w=1000&q=100&f=jpg&t=6",
                "https://www.malaymail.com/news/life/2024/12/25/barnacle-blues-spains-christmas-delicacy-at-risk-as-climate-change-and-poaching-take-a-toll/160965");
        Article at2 = new Article(2,"UN Environment Programme:\nIn Bangladesh, brick houses provide shelter from the storms",
                "data:image/jpeg;base64,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",
                "https://www.unep.org/news-and-stories/story/bangladesh-brick-houses-provide-shelter-storms");
        Article at3 = new Article(3,"UN News:\nUN World Court concludes landmark hearings on States’ responsibility for climate change\n",
                "https://global.unitednations.entermediadb.net/assets/mediadb/services/module/asset/downloads/preset/Collections/Embargoed/07-08-2024-UNICEF-Tuvalu-06.jpg/image1170x530cropped.jpg",
                "https://news.un.org/en/story/2024/12/1158476");
        Article at4 = new Article(4,"Nature:\nActions before agreement",
                "https://media.springernature.com/lw703/springer-static/image/art%3A10.1038%2Fs41558-024-02223-8/MediaObjects/41558_2024_2223_Figa_HTML.png?as=webp",
                "https://www.nature.com/articles/s41558-024-02223-8");
        Article at5 = new Article(5, "The World Economic Forum\nHere's what was agreed at COP16 to combat global desertification",
                "https://assets.weforum.org/article/image/large_EHSl1CG91lKpvBIRT06iZhiS1A5M568AL0EzvSVdDc8.JPG",
                "https://www.weforum.org/stories/2024/12/cop16-what-just-happened-combat-global-desertification/");
        Article at6 = new Article(6,"UN Tourism\n Tourism Makes History at COP29 as 50 Countries Back Climate Action Declaration for Sector\n",
                "data:image/jpeg;base64,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",
                "https://www.unwto.org/news/tourism-makes-history-at-cop29-as-50-countries-back-climate-action-declaration-for-sector");
        Article at7 = new Article(7,"Forbes\n7 Transformative Ideas To Watch In 2025: Housing To Climate Action\n",
                "https://imageio.forbes.com/specials-images/imageserve/676aee8581b539939b4f253c/California-Governor-Newsom-Signs-Housing-Bill-In-San-Francisco/960x0.jpg?format=jpg&width=1440",
                "https://www.forbes.com/sites/globalcitizen/2024/12/24/7-transformative-ideas-to-watch-in-2025-housing-to-climate-action/");
        Article at8 = new Article(8,"The Guardian\nHow to teach climate change so 15-year-olds can act",
                "https://i.guim.co.uk/img/media/664941130d295d69dca941982f512d4d30103b27/0_231_4733_2840/master/4733.jpg?width=620&dpr=2&s=none&crop=none",
                "https://www.theguardian.com/environment/2024/dec/24/how-to-teach-climate-change-so-15-year-olds-can-act");
        Article at9 = new Article(9,"OECD\nThe Climate Action Monitor 2024",
                "https://www.oecd.org/adobe/dynamicmedia/deliver/dm-aid--8e935dd5-06a4-4dd2-93cc-d3c21fde4591/787786f6-en.jpg?width=1454&quality=80&preferwebp=true",
                "https://www.oecd.org/en/publications/the-climate-action-monitor-2024_787786f6-en.html");
        Article at10 = new Article(10,"Frontiers\nAdapting crops for climate change",
                "data:image/jpeg;base64,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",
                "https://www.frontiersin.org/journals/science/article-hubs/adapting-crops-for-climate-change-abiotic-stress/explainer");
        Article at11 = new Article(11, "Daily Sabah:\nTürkiye's 2024 environmental progress: Key initiatives and actions | Daily Sabah",
                "https://idsb.tmgrup.com.tr/ly/uploads/images/2024/12/25/thumbs/1200x675/361046.jpg?v=1735129681",
                "https://www.dailysabah.com/turkiye/turkiyes-2024-environmental-progress-key-initiatives-and-actions/news");
        Article at12 = new Article(12,"Euronews\n 'People feel grief as after a loss': How emotions can spark pro-climate action\n",
                "https://static.euronews.com/articles/stories/08/92/46/96/1920x1080_cmsv2_1ed43ff2-f24d-57ba-808c-cdcac6c43ba2-8924696.jpg",
                "https://www.euronews.com/green/2024/12/23/people-feel-grief-as-after-a-loss-how-emotions-can-spark-pro-climate-action");
        Article at13 = new Article(13, "gov.wales:\nClimate Action – How to Decarbonise your Business",
                "https://businesswales.gov.wales/sites/maingel2/files/styles/post_featured/public/images/decarbonisation%20.png.webp?itok=ALcP1brh",
                "https://businesswales.gov.wales/news-and-blog/climate-action-how-decarbonise-your-business");
        Article at14 = new Article(14,"World Meteorological Organization WMO\nUnited in Science: Reboot climate action",
                "https://wmo.int/sites/default/files/styles/featured_image_x1_768x512/public/2024-09/UiS%20Press%20release%20cover.jpg?h=d1cb525d&itok=Uu8AKJ4N",
                "https://wmo.int/news/media-centre/united-science-reboot-climate-action");
        Article at15 = new Article(15, "UN Environment Programme:\n",
                "https://cdn.unenvironment.org/s3fs-public/inline-images/13_Climate%20ActionFINAL.jpg",
                "https://www.unep.org/topics/sustainable-development-goals/why-do-sustainable-development-goals-matter/goal-13-climate");
        ArticleList.add(at1);
        ArticleList.add(at2);
        ArticleList.add(at3);
        ArticleList.add(at4);
        ArticleList.add(at5);
        ArticleList.add(at6);
        ArticleList.add(at7);
        ArticleList.add(at8);
        ArticleList.add(at9);
        ArticleList.add(at10);
        ArticleList.add(at11);
        ArticleList.add(at12);
        ArticleList.add(at13);
        ArticleList.add(at14);
        ArticleList.add(at15);
    }
    private void filterList(String text, RecycleViewAdapter adapter){
        List<Article> filteredlist = new ArrayList<Article>();
        for(Article item:ArticleList){
            if(item.getKeyword().toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(item);
            }
        }
        if(filteredlist.isEmpty()){
            adapter.setFilteredList(ArticleList);
        }else{
            adapter.setFilteredList(filteredlist);
        }
    }
}