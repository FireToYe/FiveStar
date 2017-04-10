package cn.ycl.com.fivestar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    WaveCircleView wcv;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv.setText(msg.obj.toString());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final FiveStarView fsv = (FiveStarView) findViewById(R.id.fsv);
//        tv = (TextView) findViewById(R.id.tv);
//        fsv.setIntMarkFlag(false);
//        wcv = (WaveCircleView) findViewById(R.id.wcv);
//        ofFloat(wcv,"distance",15.f,0.0f).setDuration(8000).start();
//        wcv.setListener(new WaveCircleView.OnClickListener() {
//            @Override
//            public void click() {
//                ObjectAnimator animator = ObjectAnimator.ofFloat(wcv,"distance",15.f,0.0f).setDuration(8000);
//                animator.setInterpolator(new DecelerateInterpolator());
//                animator.start();
//            }
//        });
//        fsv.setListenner(new FiveStarView.OnstarChangeListenner() {
//            @Override
//            public void onchangerListener() {
//                Message message = new Message();
//                message.what = 0x123;
//                message.obj = fsv.getStarMark();
//                handler.sendMessage(message);
//            }
//        });
    }
}
