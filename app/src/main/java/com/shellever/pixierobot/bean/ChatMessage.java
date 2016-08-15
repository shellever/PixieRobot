package com.shellever.pixierobot.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linuxfor on 8/13/2016.
 */
public class ChatMessage {
    private String name;    //发送人
    private String msg;     //消息内容
    private Type type;      //消息类型
    private Date date;      //消息日期
    private String dateStr; //消息日期的字符串格式

    public enum Type{
        INPUT, OUTPUT
    }

    public ChatMessage() {
    }

    public ChatMessage(Type type, String msg) {
        this.type = type;
        this.msg = msg;
        setDate(new Date());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateStr = df.format(date);
    }

    public String getDateStr(){
        return dateStr;
    }
}
