package co.com.ceiba.mobile.pruebadeingreso.data;

public class Publication {
    private String title;
    private String body;

    public Publication(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
