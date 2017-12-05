package hznu.edu.cn.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

/**
 * 父级activity
 */
public class AbActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 判断是不是字符串是不是空
     *
     * @param str
     * @return 空返回true
     */
    protected boolean isNullOrEmpty(String... str) {
        for (String s : str) {
            if (s == null) {
                return true;
            }
            if (s.equals("")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得一个随机的颜色
     *
     * @return
     */
    protected int getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}
