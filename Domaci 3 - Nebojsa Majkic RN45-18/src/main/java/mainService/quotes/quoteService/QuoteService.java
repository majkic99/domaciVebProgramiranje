package mainService.quotes.quoteService;

import mainService.quotes.Quote;
import mainService.quotes.quoteRepo.QuoteRepository;

import java.util.List;

public class QuoteService {

    private QuoteRepository quoteRepo;

    public QuoteService() {
        quoteRepo = new QuoteRepository();
    }

    public List<Quote> getAllQuotes(){
        return quoteRepo.getQuotes();
    }

    public Quote getQuoteAt(int at){
        return quoteRepo.getQuoteAt(at);
    }

    public void add(String author, String text) {
        quoteRepo.getQuotes().add(new Quote(author, text));
    }
}
