package com.meteoweb;

import com.meteoweb.domain.StationInformation;
import com.meteoweb.repository.StationInformationRepository;
import com.meteoweb.service.AService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static javafx.application.Platform.exit;

@SpringBootApplication
@ComponentScan(excludeFilters = {@ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				value = {StationInformation.class})
		})
public class MeteoWebApplication implements CommandLineRunner {




	public static void main(String[] args) {
		SpringApplication.run(MeteoWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
