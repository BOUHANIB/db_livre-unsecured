package ma.emsi.db_livre;

import ma.emsi.db_livre.entities.Exposant;
import ma.emsi.db_livre.entities.Livre;
import ma.emsi.db_livre.repositories.ExposantRepository;
import ma.emsi.db_livre.repositories.LivreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class DbLivreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbLivreApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(ExposantRepository exposantRepository, LivreRepository livreRepository){
        return args -> {

            livreRepository.save(new Livre(null,"Mohamed","Mohamed","Mohamed",new Date(),0,"Mohamed",null));

            livreRepository.save(new Livre(null,"Yassine","Mohamed","Mohamed",new Date(),0,"Mohamed",null));
            livreRepository.save(new Livre(null,"Ahmed","Mohamed","Mohamed",new Date(),0,"Mohamed",null));
            livreRepository.save(new Livre(null,"Said","Mohamed","Mohamed",new Date(),0,"Mohamed",null));
            livreRepository.save(new Livre(null,"Moussa","Mohamed","Mohamed",new Date(),0,"Mohamed",null));

            exposantRepository.save(new Exposant(null,"nom1","pays1","mail1","pwd1","tel1","site1","add1","resSalle1","res1","spe1","loc1",null));
            exposantRepository.save(new Exposant(null,"nom2","pays2","mail2","pwd2","tel2","site2","add2","resSalle2","res2","spe2","loc2",null));
            exposantRepository.save(new Exposant(null,"nom3","pays1","mail1","pwd1","tel1","site1","add2","resSalle3","res3","spe3","loc3",null));
                System.out.println("Hello");
        };
    }
}
