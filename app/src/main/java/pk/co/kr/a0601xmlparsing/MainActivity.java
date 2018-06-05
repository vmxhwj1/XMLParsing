package pk.co.kr.a0601xmlparsing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import android.os.*;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    //ListView 출력을 위한 변수
    ArrayList<String> cities; // ←데이터 저장
    ArrayAdapter<String> adapter; // ←데이터 연결

    //날씨 정보를 저장할 List
    ArrayList<Weather> weathers;


    //진행상태를 보여주기 위한 대화상자
    ProgressDialog progressDialog;



    //anonymous - 익명(이름 없는) 클래스의 객체 생성
    //객체는 1개만 생성됩니다.
    //handleMessage가 콜백 메소드(이벤트가 발생하면 자동으로 호출되는 메소드)이므로
    //객체를 1개만 만들면 됩니다.
    Handler handler = new Handler(){
        public void handleMessage(Message message){
            if(message.what == 0)
            //리스트 뷰를 재출력
            adapter.notifyDataSetChanged();
            else if(message.what == 1)
                Toast.makeText(MainActivity.this, "다운로드 에러", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };


    //스레드에서 run은 객체를 생성한 후 start()를 직접 호출해서 실행하는
    //메소드 이므로 여러 번 사용하려면 객체를 여러 개 만들어야 하는 경우가
    //있습니다.
    //대표적으로 스레드가 있는데 스레드는 한번 만들고 종료되면 재사용이 안되는 객체입니다.
    //그래서 매번 새로 만들어서 사용해야 하기 때문에 별도로 클래스를 만들어서
    //사용하거나 이벤트가 발생할 때 마다 익명 객체를 만들어서 사용해야 합니다.
    class ThreadEx extends Thread {
        public void run() {
           //웹에 있는 xml의 주소를 직접 연결해서 XML 파싱
            try{
                //XML DOM 파싱을 위한 객체 생성
                DocumentBuilderFactory factory =
                        DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder =
                        factory.newDocumentBuilder();
                //파싱을 할 URL을 연결 - 전체 내용을 메모리에 펼쳐내기
                Document document = documentBuilder.parse("http://www.weather.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108");
                //루트에 대한 포인터 가져오기
                Element root = document.getDocumentElement();
                //city 태그 전부 가져오기
                NodeList items = root.getElementsByTagName("city");
                //리스트 초기화
                cities.clear();
                //반복하기
                for(int i = 0; i < items.getLength(); i = i + 1 ){
                    //태그 하나 하나를 대입
                    Node item = items.item(i);
                    //태그 안의 내용 가져오기
                    Node city = item.getFirstChild();
                    String content = city.getNodeValue();
                    //리스트에 삽입
                    cities.add(content);
                }
                //리스트 초기화
                weathers.clear();

                //4개 태그 항목에 해당하는 데이터 전부 찾아오기
                NodeList items1 = root.getElementsByTagName("tmEf");
                NodeList items2 = root.getElementsByTagName("tmx");
                NodeList items3 = root.getElementsByTagName("tmn");
                NodeList items4 = root.getElementsByTagName("wf");

                for(int i = 0; i < items1. getLength(); i = i + 1){
                    //객체 생성
                    Weather weather = new Weather();
                    Node node = items1.item(i);
                    Node child = node.getFirstChild();
                    String content = child.getNodeValue();
                    weather.setTmEf(content);

                    node = items2.item(i);
                    child = node.getFirstChild();
                    content = child.getNodeValue();
                    weather.setTmx(content);

                    node = items3.item(i);
                    child = node.getFirstChild();
                    content = child.getNodeValue();
                    weather.setTmn(content);
                    //Wf 태그는 전체 날씨를 나타내는 태그가 한개 있으므로 +1을 해서 가져와야 합니다.
                    node = items4.item(i+1);
                    child = node.getFirstChild();
                    content = child.getNodeValue();
                    weather.setWf(content);

                    //객체를 리스트에 삽입
                    weathers.add(weather);
                }
                Log.e("weathers", weathers.toString());

                //핸들러에게 메시지 전송
                handler.sendEmptyMessage(0);

            }
            catch (Exception e){
                Log.e("xml 예외", e.getMessage());
                handler.sendEmptyMessage(1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //인스턴스 변수 초기화
        cities = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);

        //리스트 뷰 출력
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.RED));
        listView.setDividerHeight(3);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //버튼 클릭 이벤트 만들기
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(
                        MainActivity.this, "날씨 데이터", "가져오는 중");
                //스레드 생성 및 시작
                ThreadEx th = new ThreadEx();
                th.start();
            }
        });

        //하위 항목을 저장할 리스트 생성
        weathers = new ArrayList<>();

        //ListView에서 항목 뷰를 클릭했을 때 수행할 내용
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                //행번호에 해당하는 13개의 데이터를 가져와서 subList에 저장
                ArrayList<Weather>subList = new ArrayList<>();
                        for(int i = position*13; i<(position+1)*13; i = i + 1){
                            subList.add(weathers.get(i));
                }
                //데이터를 넘겨주기 위해서 저장
                intent.putExtra("subdata", subList);
                startActivity(intent);
            }
        });
    }
}











