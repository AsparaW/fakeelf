package com.mantouland.fakeelf.bean;

/**
 * Created by asparaw on 2019/5/18.
 */
public class LyricBean {


    /**
     * lrc : {"version":6,"lyric":"..."}
     * code : 200
     */

    private LrcBean lrc;
    private int code;

    public LrcBean getLrc() {
        return lrc;
    }

    public void setLrc(LrcBean lrc) {
        this.lrc = lrc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class LrcBean {
        /**
         * version : 6
         * lyric : ...
         */

        private int version;
        private String lyric;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getLyric() {
            return lyric;
        }

        public void setLyric(String lyric) {
            this.lyric = lyric;
        }
    }
}
