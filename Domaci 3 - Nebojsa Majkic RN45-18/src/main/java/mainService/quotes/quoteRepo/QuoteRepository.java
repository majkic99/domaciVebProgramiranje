package mainService.quotes.quoteRepo;

import mainService.quotes.Quote;

import java.util.ArrayList;
import java.util.List;

public class QuoteRepository {

    private List<Quote> quotes;

    public QuoteRepository() {
        this.quotes = new ArrayList<>();
        this.quotes.add(new Quote("Anne Frank", "Whoever is happy will make others happy too."));
        this.quotes.add(new Quote("Nebojsa Majkic", "RAF RAF RAF RAF!"));
        this.quotes.add(new Quote("Aristotle", "It is during our darkest moments that we must focus to see the light." ));
        this.quotes.add(new Quote("Ralph Waldo Emerson","Do not go where the path may lead, go instead where there is no path and leave a trail."  ));
        this.quotes.add(new Quote("Test Testic", "Testiramo testic testirajuci testiranje"));
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public Quote getQuoteAt(int at){
        return quotes.get(at);
    }
}
