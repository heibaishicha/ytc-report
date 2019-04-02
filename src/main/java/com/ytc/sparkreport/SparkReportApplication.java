package com.ytc.sparkreport;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {AppScanIndex.class })
public class SparkReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(SparkReportApplication.class, args);
    }
}
