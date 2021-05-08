package rs.raf.demo.servlets;

import rs.raf.demo.models.Quote;
import rs.raf.demo.repository.quote.IQuoteRepository;
import rs.raf.demo.repository.quote.InMemoryQuoteRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "QuotesServlet", value = "/new-post")
public class NewPostServlet extends HttpServlet {

    private static int counter = 4;

    private IQuoteRepository quoteRepository;

    @Override
    public void init() throws ServletException {
        this.quoteRepository = new InMemoryQuoteRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getPathInfo().substring(1));
        Quote post = this.quoteRepository.find(id);
        req.setAttribute("post", post);


        req.getRequestDispatcher("/single-post.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String author = req.getParameter("author");
        String title = req.getParameter("title");
        String text = req.getParameter("text");
        synchronized (quoteRepository) {
            Quote q = new Quote(quoteRepository.allPosts().size() + 1, author, title, text, new ArrayList<>(), java.time.LocalDate.now());
            this.quoteRepository.insert(q);
		}
        resp.sendRedirect(req.getContextPath() + "/posts");
    }
}
