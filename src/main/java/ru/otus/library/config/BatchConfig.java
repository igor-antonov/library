package ru.otus.library.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookMongo;

import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private final MongoTemplate mongoTemplate;
    private final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    public BatchConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public ItemReader<BookMongo> reader(){
        return new MongoItemReaderBuilder<BookMongo>()
                .name("mongoBookReader")
                .template(mongoTemplate)
                .targetType(BookMongo.class)
                .collection("books")
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public BookItemProcessor processor(){
        return new BookItemProcessor();
    }

    @Bean BookItemWriter writer() {
        return new BookItemWriter();
    }

    @Bean
    public Job importBookJob(Step bookStep) {
        return jobBuilderFactory.get("importBookJob")
                .incrementer(new RunIdIncrementer())
                .flow(bookStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("Начало job");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step bookStep(ItemReader<BookMongo> reader, BookItemProcessor processor, BookItemWriter writer) {
        return stepBuilderFactory.get("bookStep")
                .<BookMongo, Book> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
