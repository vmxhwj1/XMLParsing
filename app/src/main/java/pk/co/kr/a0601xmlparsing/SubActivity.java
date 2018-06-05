package pk.co.kr.a0601xmlparsing;

        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;

        import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);

        //ListView에 이전 Activity가 넘겨준 데이터 출력
        ListView listView = (ListView)findViewById(R.id.sublist);
        //이전 Activity에서 넘겨준 데이터 가져오기
        Intent intent = getIntent();
        ArrayList<Weather> list = (ArrayList<Weather>)intent.getSerializableExtra("subdata");
        ArrayAdapter<Weather>adapter = new ArrayAdapter<>(
                SubActivity.this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        listView.setDivider(new ColorDrawable(Color.RED));
        listView.setDividerHeight(2);

        //버튼의 클릭 이벤트 작성
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //현재 엑티비티 종료
                finish();
            }
        });
    }
}
