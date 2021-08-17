package com.bluntsoftware.qovery.batch;

import com.bluntsoftware.qovery.config.BatchDataSourceConfig;
import com.bluntsoftware.qovery.config.BatchRepositoryConfig;
import com.bluntsoftware.qovery.repository.PersonRepo;
import com.bluntsoftware.qovery.util.hot.HotFolderWatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Scope("test")
@SpringJUnitConfig({BatchDataSourceConfig.class, BatchRepositoryConfig.class})
class PersonJsonFileBatchJobTest {

  PersonJsonFileBatchJob batchJob;

  @Mock
  PersonRepo repository;

  @Autowired
  StepBuilderFactory stepBuilderFactory;
  @Mock
  JobLauncher jobLauncher;
  @Autowired
  JobBuilderFactory jobBuilderFactory;
  @Mock
  HotFolderWatcher hotFolderWatcher;

  File batchTestFile;

  @BeforeEach
  void before()  throws IOException {
    MockitoAnnotations.initMocks(this);
    File resourceFolder = ResourceUtils.getFile("classpath:");
    File hotFolderBase = new File(resourceFolder,"batch");
    hotFolderBase.mkdirs();
    batchTestFile = new File(hotFolderBase,"batch-test.json");
    FileUtils.createNewFile(batchTestFile);
    batchJob = new PersonJsonFileBatchJob( repository,  stepBuilderFactory,  jobLauncher,  jobBuilderFactory,hotFolderWatcher);
  }

  @Test
  void onChangeTest() {
    Assertions.assertDoesNotThrow(()->batchJob.onChange(batchTestFile));
  }

  @Test
  void onChangeJobParametersInvalidExceptionTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    when(jobLauncher.run(any(),any())).thenThrow(new JobExecutionAlreadyRunningException(""));
    Assertions.assertDoesNotThrow(()-> batchJob.onChange(batchTestFile));
  }

  @Test
  void onChangeJobRestartExceptionTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    when(jobLauncher.run(any(),any())).thenThrow(new JobRestartException(""));
    Assertions.assertDoesNotThrow(()-> batchJob.onChange(batchTestFile));
  }

  @Test
  void onChangeJobExecutionAlreadyRunningExceptionTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    when(jobLauncher.run(any(),any())).thenThrow(new JobExecutionAlreadyRunningException(""));
    Assertions.assertDoesNotThrow(()-> batchJob.onChange(batchTestFile));
  }

  @Test
  void onChangeJobInstanceAlreadyCompleteExceptionTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    when(jobLauncher.run(any(),any())).thenThrow(new JobInstanceAlreadyCompleteException(""));
    Assertions.assertDoesNotThrow(()-> batchJob.onChange(batchTestFile));
  }

}
