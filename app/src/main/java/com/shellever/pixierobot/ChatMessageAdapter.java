package com.shellever.pixierobot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shellever.pixierobot.bean.ChatMessage;

import java.util.List;

/**
 * Created by linuxfor on 8/14/2016.
 */
public class ChatMessageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ChatMessage> mDataList;

    public ChatMessageAdapter(Context context, List<ChatMessage> mDataList) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position){
        ChatMessage msg = mDataList.get(position);
        return msg.getType() == ChatMessage.Type.INPUT ? 1 : 0;
    }

    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage mChatMessage = mDataList.get(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            if(mChatMessage.getType() == ChatMessage.Type.INPUT){
                convertView = mInflater.inflate(R.layout.main_chat_from_msg, parent, false);
                viewHolder.createDate = (TextView) convertView.findViewById(R.id.chat_from_createDate);
                viewHolder.content = (TextView) convertView.findViewById(R.id.chat_from_content);
                convertView.setTag(viewHolder);
            }else {
                convertView = mInflater.inflate(R.layout.main_chat_send_msg, parent, false);
                viewHolder.createDate = (TextView) convertView.findViewById(R.id.chat_send_createDate);
                viewHolder.content = (TextView) convertView.findViewById(R.id.chat_send_content);
                convertView.setTag(viewHolder);
            }
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.createDate.setText(mChatMessage.getDateStr());
        viewHolder.content.setText(mChatMessage.getMsg());

        return convertView;
    }

    private class ViewHolder{
        TextView createDate;
//        TextView name;
        TextView content;
    }
}
