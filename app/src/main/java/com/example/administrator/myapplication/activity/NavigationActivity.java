package com.example.administrator.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.DragAdapter;
import com.example.administrator.myapplication.adapter.OtherAdapter;
import com.example.administrator.myapplication.constans.ChannelManage;
import com.example.administrator.myapplication.entity.ChannelItem;
import com.example.administrator.myapplication.view.DragGrid;
import com.example.administrator.myapplication.view.ProgressDialog;

import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 分类界面
 */
public class NavigationActivity extends AbActivity implements AdapterView.OnItemClickListener {

    //用户栏目对应的GridView
    private DragGrid userGridView;
    //其他栏目
    // private OtherGridView otherGridView;
    //用户栏目对应的适配器
    private DragAdapter userAdapter;
    //其他栏目对应的适配器
    private OtherAdapter otherAdapter;
    /**
     * 其它栏目列表
     */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    /**
     * 用户栏目列表
     */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();

    //是否移动由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
    boolean isMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_activity);
        initView();
        x.view().inject(this);
        getGridViewData();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        //otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
    }

    //初始化数据
    private void getGridViewData() {
        otherChannelList = ChannelManage.defaultOtherChannels;
        userChannelList = ChannelManage.defaultUserChannels;
        userAdapter = new DragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(this, otherChannelList);
        //    otherGridView.setAdapter(this.otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        //otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.showDialog();
        dialog.updateProgressDialog("正在查询数据");
        BmobQuery<ChannelItem> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(this, new FindListener<ChannelItem>() {
            @Override
            public void onSuccess(List<ChannelItem> object) {
                userChannelList.clear();
                userChannelList.addAll(object);
                userAdapter.notifyDataSetChanged();
                dialog.closs();
            }

            @Override
            public void onError(int code, String msg) {
                dialog.closs();
                Toast.makeText(NavigationActivity.this, code + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Event(value = R.id.btn_addtype, type = View.OnClickListener.class)
    private void btn_addtype(View view) {
        CustomDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                ChannelItem item = new ChannelItem(0, "", 0, 0, "");
                item.setObjectId(userChannelList.get(position).getObjectId());
                item.delete(this);
                //position为 0 的不可以进行任何操作
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                    otherAdapter.setVisible(false);
                    //添加到最后一个
                    otherAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                //otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                userAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击出现弹框
     */
    public void CustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置相关参数
        builder.setTitle("分类名称");
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_type, null);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText et_dialog = (EditText) view.findViewById(R.id.et_dialog);
                RadioGroup group = (RadioGroup) view.findViewById(R.id.radio_group);
                String str = et_dialog.getText().toString();
                String szType = group.getCheckedRadioButtonId() == R.id.radioButton1 ? "支" : "收";
                if (isNullOrEmpty(str)) {
                    Toast.makeText(NavigationActivity.this, "分类数据为空", Toast.LENGTH_SHORT).show();
                } else {
                    addAItemToNet(str, szType);
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 添加分类到网络
     *
     * @param
     * @param
     */
    private void addAItemToNet(String name, String sztype) {
        final ChannelItem item = new ChannelItem(3, name + "[" + sztype + "]", 3, 1, BmobUser.getCurrentUser(NavigationActivity.this).getUsername());
        item.setSzType(sztype);
        // 先查询有没有这样的一个数据
        BmobQuery<ChannelItem> query = new BmobQuery<ChannelItem>();
        query.addWhereEqualTo("szType", sztype);
        query.addWhereEqualTo("NAME", name + "[" + sztype + "]");
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(NavigationActivity.this).getUsername());
        query.setLimit(50);
        query.count(NavigationActivity.this, ChannelItem.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (i == 0) {
                    //将这个添加到网络上的数据表中
                    item.save(NavigationActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            userChannelList.add(item);
                            userAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(NavigationActivity.this, "添加失败，请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(NavigationActivity.this, "添加失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /*
    获取点击的
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }
    //退出时保存选中的状态
}
