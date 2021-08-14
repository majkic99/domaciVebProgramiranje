package rafnews.backend;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import rafnews.backend.repositories.category.ICategoryRepository;
import rafnews.backend.repositories.category.MySQLCategoryRepository;
import rafnews.backend.repositories.comment.ICommentRepository;
import rafnews.backend.repositories.comment.MySQLCommentRepository;
import rafnews.backend.repositories.keyword.IKeywordRepository;
import rafnews.backend.repositories.keyword.MySQLKeywordRepository;
import rafnews.backend.repositories.news.INewsRepository;
import rafnews.backend.repositories.news.MySQLNewsRepository;
import rafnews.backend.repositories.user.IUserRepository;
import rafnews.backend.repositories.user.MySQLUserRepository;
import rafnews.backend.services.CategoryService;
import rafnews.backend.services.CommentService;
import rafnews.backend.services.KeywordService;
import rafnews.backend.services.NewsService;
import rafnews.backend.services.UserService;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class App extends ResourceConfig {

    public App() {
    	
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);


        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                this.bind(MySQLNewsRepository.class).to(INewsRepository.class).in(Singleton.class);
                this.bind(MySQLUserRepository.class).to(IUserRepository.class).in(Singleton.class);
                this.bind(MySQLCategoryRepository.class).to(ICategoryRepository.class).in(Singleton.class);
                this.bind(MySQLKeywordRepository.class).to(IKeywordRepository.class).in(Singleton.class);
                this.bind(MySQLCommentRepository.class).to(ICommentRepository.class).in(Singleton.class);

                this.bindAsContract(NewsService.class);
                this.bindAsContract(UserService.class);
                this.bindAsContract(CategoryService.class);
                this.bindAsContract(KeywordService.class);
                this.bindAsContract(CommentService.class);
            }
        };
        register(binder);

        packages("rafnews.backend");
    }
}