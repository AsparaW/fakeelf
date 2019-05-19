package com.mantouland.fakeelf.bean;

/**
 * Created by asparaw on 2019/5/18.
 */

/**
 * - METHOD GET
 * - PATH /getSongListID.php
 * - PARAMETER
 *   type: String
 *   可取值有：HAPPY,UNHAPPY,CLAM,EXCITING
 */


public class ListBean {


    /**
     * status : 200
     * message : 成功
     * data : {"id":2332160280}
     */

    private int status;
    private String message;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2332160280
         */

        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
