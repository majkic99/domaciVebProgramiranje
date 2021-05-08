package rs.raf.demo.servlets;

import rs.raf.demo.repository.quote.IQuoteRepository;
import rs.raf.demo.repository.quote.InMemoryQuoteRepository;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "PostServlet", value = {"/", "/posts"})
public class PostsServlet extends HttpServlet {

    private IQuoteRepository subjectRepository;

    @Override
    public void init() throws ServletException {
        this.subjectRepository = new InMemoryQuoteRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("posts", this.subjectRepository.allPosts());

        request.getRequestDispatcher("/posts.jsp").forward(request, response);
    }
}
