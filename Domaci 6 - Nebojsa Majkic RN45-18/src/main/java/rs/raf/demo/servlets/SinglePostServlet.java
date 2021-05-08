package rs.raf.demo.servlets;

import rs.raf.demo.models.Quote;
import rs.raf.demo.models.Subject;
import rs.raf.demo.repository.quote.IQuoteRepository;
import rs.raf.demo.repository.quote.InMemoryQuoteRepository;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SinglePostServlet", value = "/posts/*")
public class SinglePostServlet extends HttpServlet {

    private IQuoteRepository subjectRepository;

    @Override
    public void init() throws ServletException {
        this.subjectRepository = new InMemoryQuoteRepository();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println(request.getPathInfo());
        int id = Integer.parseInt(request.getPathInfo().substring(1));
        Quote post = this.subjectRepository.find(id);
        request.setAttribute("post", post);

        request.getRequestDispatcher("/single-post.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getPathInfo().substring(1));
        Quote post = this.subjectRepository.find(id);
        List<Subject> comments = post.getComments();
        String author = req.getParameter("name");
        String comment = req.getParameter("comment");
        Subject newComment = new Subject(author, comment);
        comments.add(newComment);
        post.setComments(comments);
        this.subjectRepository.update(post);

        resp.sendRedirect(req.getContextPath() + "/posts/"+post.getId());
    }
}
