package rs.raf.demo.repository.quote;

import rs.raf.demo.models.Quote;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryQuoteRepository implements IQuoteRepository {
    private static final List<Quote> quotes = new CopyOnWriteArrayList<>();


    static {
        quotes.add( new Quote(1, "Petar Petrovic", "Birds", "Birds have wings and chicken is a bird too. Penguins can't fly, though. Birds have wings and chicken is a bird too. Penguins can't fly, though.", new ArrayList<>(), java.time.LocalDate.now()));
        quotes.add(new Quote(2,"Ivan Ivanovic", "Rocks", "Rocks are great pets for keeping, look at Patricks rock. Rocks are great pets for keeping, look at Patricks rock. Rocks are great pets for keeping, look at Patricks rock.",  new ArrayList<>(), java.time.LocalDate.now()));
        quotes.add(new Quote(3,"Mitar Miric", "Clouds", "Clouds are great for old men yelling at them. Clouds are great for old men yelling at them. Clouds are great for old men yelling at them.",  new ArrayList<>(), java.time.LocalDate.now()));
    }

    public void insert(Quote quote) {
        quotes.add(quote);
    }

    @Override
    public void update(Quote quote) {
        for(Quote q: quotes) {
            if(q.getId() == quote.getId()) {
                quotes.remove(q);
                quotes.add(quote);
            }
        }
    }

    @Override
    public List<Quote> allPosts() {
    	List<Quote> quoteList = new ArrayList<>();
        for(Quote q : quotes) {
        	quoteList.add(q);
        }
        return quoteList;
    }

    @Override
    public Quote find(int id) {
        for(Quote q: quotes) {
            if(q.getId() == id) {
                return q;
            }
        }
        return null;
    }

}
