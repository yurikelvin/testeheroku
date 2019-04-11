package br.com.ufcg.util.response;

public class Response {

    private Object data;

    private String message;

    private Integer status;

    public Response(String message) {
        this(message, null, null);
    }

    public Response() {

    }

    public Response(String message, Integer status) {
        this(message, status, null);
    }

    public Response(String message, Integer status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void getStatus(Integer status) {
        this.status = status;
    }

}