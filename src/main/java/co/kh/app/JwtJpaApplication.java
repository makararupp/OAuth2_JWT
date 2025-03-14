package co.kh.app;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Book API",version = "1.0",description = "Book Management System"))
public class JwtJpaApplication{
	public static void main(String[] args)	 {

		SpringApplication.run(JwtJpaApplication.class, args);

	}

	// @Autowired
	// private UserRepository userRepository;

	// @Autowired
	// private BookRepository bookRepository;

	// @Override
	// public void run(String... args) throws Exception {

	// 	System.out.println(userRepository.findByEmail("makara1@gmail.com"));

	// 	Book book = new Book();
	// 	book.setTitle("The lord of the Ring");
	// 	book.setAuthor("Prime");
	// 	book.setStatus(true);
	// 	bookRepository.save(book);

	// }
}