package com.bluntsoftware.qovery.batch;

import com.bluntsoftware.qovery.model.Person;
import com.bluntsoftware.qovery.repository.PersonRepo;
import com.bluntsoftware.qovery.util.hot.HotFolderListener;
import com.bluntsoftware.qovery.util.hot.HotFolderWatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ConcurrentModel;
import java.io.File;
import java.util.Map;

@Slf4j
@Component
public class PersonJsonFileBatchJob implements FileBatchJob<Map<String, Object>, Person>, HotFolderListener {

    private final PersonRepo repository;
    private final ObjectMapper mapper;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;
    private final JobBuilderFactory jobBuilderFactory;

    public PersonJsonFileBatchJob(PersonRepo repository, StepBuilderFactory stepBuilderFactory, JobLauncher jobLauncher, JobBuilderFactory jobBuilderFactory, HotFolderWatcher hotFolderWatcher) {
        this.repository = repository;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;
        this.jobBuilderFactory = jobBuilderFactory;
        this.mapper = new ObjectMapper();
        hotFolderWatcher.addListener("batch/person",this);
    }

    @Override
    public void onChange(File file) {
        try {
          run(file,5);
        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException e) {
          log.error(e.getMessage());
        }
    }
    /*
       Json file must be in the form of a JSON Array ie [{},{}]
    */
    @Override
    public void run(File file, int chunkSize) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        var params = new JobParametersBuilder().addLong("jobId", System.currentTimeMillis()).toJobParameters();
        var job = jobBuilderFactory
                .get("job")
                .incrementer(new RunIdIncrementer())
                .flow(stepBuilderFactory
                        .get("step-" + file.getName())
                        .<Map<String, Object>,Person>chunk(chunkSize)
                        .reader(reader(file))
                        .processor(processor())
                        .writer(writer())
                        .build())
                .end()
                .build();
        jobLauncher.run(job, params);
    }

    @Override
    public ItemProcessor<Map<String, Object>, Person> processor() {
        return map -> mapper.convertValue(map,Person.class);
    }

    @Override
    public ItemWriter<Person> writer() {
       return repository::saveAll;
    }

    @Override
    public ItemReader<Map<String, Object>> reader(File file) {
        return new JsonItemReaderBuilder<Map<String, Object>>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(this.mapper, ConcurrentModel.class))
                .resource(new FileSystemResource(file))
                .name("JsonFileReader")
                .build();
    }
}
