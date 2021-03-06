# Overview

This project is aimed at benchmarking databases for the EVA/EGA/AMP accessioning services.

# Pre-requisites

Running the benchmark from a host machine requires a local installation of [Apache JMeter](https://jmeter.apache.org/). The full path to Apache JMeter is specified with the command-line switch **-j** in the benchmarking program. Please read the section below for more details.   

# Running the benchmarks

To run the benchmarks for a given database, you can use the **database_benchmarking_suite-assembly** JAR file generated by building this project.
```bash
nohup bash -c "rm -f mongodb_heavy_read_workload.jtl && java -jar database_benchmarking_suite-assembly-0.1.jar -d mongodb -c mongodb://mongohost-1:27017,mongohost-2:27017 -s accessioning -t global_variant_lkp -j /home/centos/apache-jmeter-3.3 -o mongodb_heavy_read_workload.jtl -w heavy_read_workload.json" > mongodb_heavy_read_workload.out &
```
The individual command line arguments for the program and their possible formats for different databases can be listed by invoking the program with the **--help** switch. For example:
```bash
java -jar database_benchmarking_suite-assembly-0.1.jar --help
```

# Workload configuration file

The workloads to be run for a given database are specified in the JSON format in the workload configuration file. 

An example workload is shown below:
```json
{
  "write-workloads": [
    {
      "desc": "ins-256k-par",
      "thread-choices": [4,8,16,32],
      "num-ops-per-wu": 256000,
      "num-wu": 50
    }
  ],
  "read-workloads": [
    {
      "desc": "read-256k-par",
      "thread-choices": [4,8,16,32],
      "num-ops-per-wu": 256000,
      "num-wu": 50
    }
  ]
}
``` 

The configuration attributes are:

* **desc** - Description of the workload. Ideally, this would succinctly describe a workload but there is **no specific convention** required to run the benchmarks. For example: ins-256k-par denotes an insert workload that inserts 256,000 records with parallel threads.
* **thread-choices** - Different thread choices that the workload should be run with.
* **num-ops-per-wu** - Number of operations per "workload unit". In other words, the total number of operations to be performed by all threads for any given run. For example, to accomplish a 256k num-ops-per-wu with the thread choice 8, each thread would have to perform 32k (256k/8) inserts in parallel. Similarly, accomplishing the same num-ops-per-wu with 16 threads would require 16k inserts (256k/16) in parallel.
* **num-wu** - Total number of workload units. In other words, this represents the number of times each workload should be run for each thread choice. 

The workloads can be purely write-only, read-only or read+write. For more examples, see [here](https://github.com/EBIvariation/accession-commons/blob/master/database_benchmarking_suite/conf/heavy_write_workload.json) and [here](https://github.com/EBIvariation/accession-commons/blob/master/database_benchmarking_suite/conf/read_write_workload.json).

# Benchmark results

Results of the benchmarks run for different databases can be found [here](https://docs.google.com/document/d/1jCoZ4I0OA1LeD_U_tu4TASsZX7l1ArixHxINZvC5Fg0/edit?usp=sharing).