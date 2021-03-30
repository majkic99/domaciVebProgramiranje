package mainService.quotes;

public class Quote {

    private String author;
    private String text;

    public Quote(String author, String text) {
        this.author = author;
        this.text = text;
    }

    @Override
    public String toString() {
        return author + ": \""+ text +"\"" ;
    }
}
