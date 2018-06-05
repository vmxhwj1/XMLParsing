package pk.co.kr.a0601xmlparsing;

import java.io.Serializable;

public class Weather implements Serializable{
    private String tmEf;
    private String tmx;
    private String tmn;
    private String wf;

    public String getTmEf() {
        return tmEf;
    }

    public void setTmEf(String tmEf) {
        this.tmEf = tmEf;
    }

    public String getTmx() {
        return tmx;
    }

    public void setTmx(String tmx) {
        this.tmx = tmx;
    }

    public String getTmn() {
        return tmn;
    }

    public void setTmn(String tmn) {
        this.tmn = tmn;
    }

    public String getWf() {
        return wf;
    }


    public void setWf(String wf) {
        this.wf = wf;

    }
        @Override
        public String toString() {
            return "Weather{" +
                    "시간='" + tmEf + '\'' +
                    ", 최고온도='" + tmx + '\'' +
                    ", 최저온도='" + tmn + '\'' +
                    ", 날씨='" + wf + '\'' +
                    '}';
        }

    }

