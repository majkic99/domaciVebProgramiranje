package rs.raf.demo.repository.quote;

import rs.raf.demo.models.Quote;

import java.util.List;

public interface IQuoteRepository {
    public void insert(Quote quote);

    public void update(Quote quote);

    public List<Quote> allPosts();

    public Quote find(int id);
}
