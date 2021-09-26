package com.example.newprojectmobileapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.adapters.CustomeAdapter;
import com.example.newprojectmobileapp.model.DocBao;
import com.example.newprojectmobileapp.model.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsFragment extends Fragment {

    ListView listView;
    CustomeAdapter customeAdapter ;
    ArrayList<DocBao> mangdocbao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        listView = view.findViewById(R.id.listview);
        mangdocbao = new ArrayList<>();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadData().execute("https://vnexpress.net/rss/giai-tri.rss");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
//                intent.putExtra("link",mangdocbao.get(i).link);
//                startActivity(intent);
                String sendLine = mangdocbao.get(position).link;
                Bundle bundle = new Bundle();
                bundle.putString("str_link", sendLine);


                FragmentNewsDetail fragmentNewsDetail = new FragmentNewsDetail();
                fragmentNewsDetail.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_news_detail, fragmentNewsDetail);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private class ReadData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return readContent(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListdecription = document.getElementsByTagName("description");
            String hinhanh ="";
            String title = "";
            String link ="";
            for (int i=0;i<nodeList.getLength();i++){
                String cdata = nodeListdecription.item(i + 1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cdata);
                if (matcher.find()){
                    hinhanh = matcher.group(1);
                }
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element,"title");
                link = parser.getValue(element,"link");
                mangdocbao.add(new DocBao(title,link,hinhanh));
            }
            customeAdapter = new CustomeAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,mangdocbao);
            listView.setAdapter(customeAdapter);
            super.onPostExecute(s);
        }
    }

    private String readContent(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {

            URL url = new URL(theUrl);

            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            Log.d("content", content.toString());
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
