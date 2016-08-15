package com.shellever.pixierobot;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shellever.pixierobot.bean.ChatMessage;
import com.shellever.pixierobot.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int FROM_MSG = 0x1;

    private ListView mChatView;     //展示消息的ListView
    private EditText mMessage;      //文本域
    private Button mSendButton;     //发送按钮
    private List<ChatMessage> mDataList = new ArrayList<ChatMessage>(); //存储聊天消息
    private ChatMessageAdapter mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FROM_MSG) {
                ChatMessage from = (ChatMessage) msg.obj;
                mDataList.add(from);
                mAdapter.notifyDataSetChanged();        //通知ListView数据更新
                mChatView.setSelection(mDataList.size() - 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();

        mAdapter = new ChatMessageAdapter(this, mDataList);
        mChatView.setAdapter(mAdapter);
    }

    private void initView() {
        mChatView = (ListView) findViewById(R.id.id_chat_listView);
        mMessage = (EditText) findViewById(R.id.id_chat_msg);
        String welcome = getResources().getString(R.string.chat_welcome);
        mDataList.add(new ChatMessage(ChatMessage.Type.INPUT, welcome));

        mSendButton = (Button) findViewById(R.id.id_chat_send);
        mSendButton.setOnClickListener(this);       //注册发送按钮的监听事件
    }

    public void sendMessage() {
        final String msg = mMessage.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(MainActivity.this, R.string.empty_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        ChatMessage to = new ChatMessage(ChatMessage.Type.OUTPUT, msg);
        to.setDate(new Date());
        mDataList.add(to);

        mAdapter.notifyDataSetChanged();
        mChatView.setSelection(mDataList.size() - 1);

        mMessage.setText("");   //清空内容

        //关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        //耗时的网络请求，故在子线程中进行以避免影响主线程UI的渲染
        new Thread() {
            @Override
            public void run() {
                ChatMessage from;
                try {
                    from = HttpUtils.sendMessage(msg);      //默认使用POST请求
                    if("ErrorCode".equals(from.getMsg())){
                        String tips = getResources().getString(R.string.server_yet_developing);
                        from.setMsg(tips);
                    }
                } catch (Exception e) {
                    String tips = getResources().getString(R.string.server_side_error);
                    from = new ChatMessage(ChatMessage.Type.INPUT, tips);
                }
                Message message = Message.obtain();
                message.what = FROM_MSG;
                message.obj = from;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_chat_send:
                sendMessage();
                break;
        }
    }
}
