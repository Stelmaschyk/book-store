package mate.academy.bookstore;


import lombok.RequiredArgsConstructor;
import model.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import service.BookService;

import java.math.BigDecimal;


@SpringBootApplication
@RequiredArgsConstructor
public class BookStoreApplication {

	private final BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return  new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				Book book = new Book();
				book.setTitle("Neurofitness");
				book.setAuthor("Rahul Jandial");
				book.setIsbn("785001467991");
				book.setPrice(BigDecimal.valueOf(7));
				book.setDescription("From the operating room, where he performs some of the riskiest surgeries around, to the lab, where he works on leading clinical trials, Dr. Rahul Jandial is on the cutting edge of the latest advancements in neuroscience. This fascinating book draws on Dr. Jandial’s broad-spectrum expertise and brings together the best of various fields—surgery, science, brain structure, the conscious mind—all to explain the bigger picture of brain health and rejuvenation. It is a journey into his operating room, around the world on his surgical missions, inside his laboratory, and to the outer edges of neuroscience to reveal the latest brain breakthroughs that are turning science fiction into reality, translating their implications for everyday life. Busting myths along the way, Jandial helps readers get wired for success at work and school, perform better when the pressure is on, boost memory, control stress and emotions, minimize pain, stick to a healthy eating plan, unleash creativity, raise smarter kids, and stay sharp as they age. Combining the treatment guidelines he gives his patients, the most promising concepts from frontier science, and the smartest super-achiever hacks, he provides practical takeaways for optimizing brain function and leading a healthier, happier, more productive life.");
				bookService.save(book);
				System.out.println(bookService.findAll());
			}
		};
	}
}
